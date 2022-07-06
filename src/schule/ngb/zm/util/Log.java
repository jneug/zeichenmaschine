package schule.ngb.zm.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.function.Supplier;
import java.util.logging.*;

import static java.util.logging.Level.*;

/**
 * Einfache Logging-API, die auf {@link java.util.logging} aufsetzt.
 * <p>
 * Klassen, die Informations- oder Fehlernachrichten loggen wollen, erstellen
 * ein internes {@code LOG} Objekt dieser Klasse. Die Zeichenmaschine erstellt
 * ihren Logger beispielsweise so:
 * <pre>
 *     private static final Log LOG = new Log(Zeichenmaschine.class);
 * </pre>
 * <p>
 * Jedes {@code Log} nutzt intern einen {@link Logger}, der über
 * {@link Logger#getLogger(String)} abgerufen wird. Die {@code Log}-Objekte
 * selbst werden nicht weiter zwischengespeichert, aber in der Regel wird pro
 * Klasse nur genau ein {@code Log}-Objekt erstellt. Mehrere {@code Log}s nutzen
 * dann aber denselben {@code Logger}.
 * <p>
 * Die API orientiert sich lose an <a href="#">Log4j</a> und vereinfacht die
 * Nutzung der Java logging API für die häufigsten Anwendungsfälle.
 */
public final class Log {

	private static final String ROOT_LOGGER = "schule.ngb.zm";

	private static boolean LOGGING_INIT = false;

	/**
	 * Aktiviert das Logging in der Zeichenmaschine global.
	 * <p>
	 * Die Methode sollte einmalig möglichst früh im Programm aufgerufen werden,
	 * um für alle bisher und danach erstellten {@link Logger} das minimale
	 * Logging-Level auf {@link Level#FINE} zu setzen. Dies entspricht allen
	 * Nachrichten die mit den Methoden (außer {@code trace}) eines {@link Log}
	 * erzeugt werden.
	 *
	 * @see #enableGlobalLevel(Level)
	 */
	public static final void enableGlobalDebugging() {
		enableGlobalLevel(ALL);
	}

	/**
	 * Setzt das Logging-Level aller bisher und danach erzeugten {@link Logger}
	 * auf den angegebenen {@code Level}.
	 * <p>
	 * Das Level für bestehende {@code Logger} wird nur abgesenkt, so dass
	 * Nachrrichten bis {@code level} auf jeden Fall ausgegeben werden. Besitzt
	 * der {@code Logger} schon ein niedrigeres Level, wird dieses nicht
	 * verändert. Gleiches gilt für alle {@link ConsoleHandler}, die den
	 * bestehenden {@code Logger}n hinzugefügt wurden.
	 * <p>
	 * <strong>Hinweis:</strong> Das Setzen des Logging-Level während der
	 * Programmausführung gilt als <em>bad practice</em>, also schlechter
	 * Programmierstil. Im Kontext der Zeichenmaschine macht dies Sinn, um
	 * Programmieranfängern eine einfache Möglichkeit zu geben, in ihren
	 * Programmen auf Fehlersuche zu gehen. Für andere Einsatzszenarien sollte
	 * auf die übliche Konfiguration des {@link java.util.logging} Pakets über
	 * eine Konfigurationsdatei zurückgegriffen werden.
	 *
	 * @param level Das Level, auf das alle {@code Logger} und {@code Handler}
	 * 	mindestens	herabgesenkt werden sollen.
	 */
	public static final void xenableGlobalLevel( Level level ) {
		int lvl = Validator.requireNotNull(level).intValue();

		Logger rootLogger = Logger.getLogger(ROOT_LOGGER);
		rootLogger.setLevel(level);

		for( Handler handler : rootLogger.getHandlers() ) {
			if( handler instanceof ConsoleHandler ) {
				Level handlerLevel = handler.getLevel();
				if( handlerLevel == null || handler.getLevel().intValue() > lvl ) {
					handler.setLevel(level);
				}
			}
		}
	}

	public static final void enableGlobalLevel( Level level ) {
		int lvl = Validator.requireNotNull(level).intValue();

		// Decrease level of root level ConsoleHandlers for outpu
		Logger rootLogger = Logger.getLogger("");
		for( Handler handler : rootLogger.getHandlers() ) {
			if( handler instanceof ConsoleHandler ) {
				Level handlerLevel = handler.getLevel();
				if( handlerLevel == null || handler.getLevel().intValue() > lvl ) {
					handler.setLevel(level);
				}
			}
		}

		// Decrease level of all existing ZM Loggers
		Iterator<String> loggerNames = LogManager.getLogManager().getLoggerNames().asIterator();
		while( loggerNames.hasNext() ) {
			String loggerName = loggerNames.next();

			if( loggerName.startsWith(ROOT_LOGGER) ) {
				Logger logger = Logger.getLogger(loggerName);
				logger.setLevel(level);

				for( Handler handler : logger.getHandlers() ) {
					if( handler instanceof ConsoleHandler ) {
						Level handlerLevel = handler.getLevel();
						if( handlerLevel == null || handler.getLevel().intValue() > lvl ) {
							handler.setLevel(level);
						}
					}
				}
			}
		}
	}

	public static final Log getLogger( Class<?> clazz ) {
		if( !LOGGING_INIT ) {
			Logger.getLogger(ROOT_LOGGER);
			LOGGING_INIT = true;
		}
		return new Log(clazz);
	}

	private final Logger LOGGER;

	private final Class<?> sourceClass;

	private Log( final Class<?> clazz ) {
		sourceClass = clazz;
		if( !clazz.getName().startsWith(ROOT_LOGGER) ) {
			LOGGER = Logger.getLogger(ROOT_LOGGER + "." + clazz.getSimpleName());
		} else {
			LOGGER = Logger.getLogger(clazz.getName());
		}
	}

	/*public Log( final String name ) {
		LOGGER = Logger.getLogger(name);
	}*/

	public void log( Level level, final CharSequence msg ) {
		//LOGGER.log(level, msg::toString);
		if( LOGGER.isLoggable(level) ) {
			//LOGGER.logp(level, sourceClass.getName(), inferCallerName(), msg::toString);
			doLog(level, null, msg::toString);
		}
	}

	public void log( Level level, final CharSequence msg, Object... params ) {
		if( LOGGER.isLoggable(level) ) {
			//LOGGER.logp(level, sourceClass.getName(), inferCallerName(), () -> String.format(msg.toString(), params));
			doLog(level, null, () -> String.format(msg.toString(), params));
		}
	}

	public void log( Level level, final Supplier<String> msgSupplier ) {
		if( LOGGER.isLoggable(level) ) {
			//LOGGER.logp(level, sourceClass.getName(), inferCallerName(), msgSupplier);
			doLog(level, null, msgSupplier);
		}
	}

	public void log( Level level, final Throwable throwable, final CharSequence msg, Object... params ) {
		if( LOGGER.isLoggable(level) ) {
			//LOGGER.logp(level, sourceClass.getName(), inferCallerName(), throwable, () -> String.format(msg.toString(), params));
			doLog(level, throwable, () -> String.format(msg.toString(), params));
		}
	}

	public void log( Level level, final Throwable throwable, final Supplier<String> msgSupplier ) {
		if( LOGGER.isLoggable(level) ) {
			//LOGGER.logp(level, sourceClass.getName(), inferCallerName(), throwable, msgSupplier);
			doLog(level, throwable, msgSupplier);
		}
	}

	private String inferCallerName() {
		StackTraceElement[] trace = new Throwable().getStackTrace();
		for( int i = 0; i < trace.length; i++ ) {
			/// if( trace[i].getClassName().equals(sourceClass.getName()) ) {
			if( !trace[i].getClassName().equals(Log.class.getName()) ) {
				return trace[i].getMethodName();
			}
		}
		return "unknown";
	}

	private void doLog( Level level, final Throwable throwable, final Supplier<String> msgSupplier ) {
		String clazz = sourceClass.getName();
		String method = "unknown";

		StackTraceElement[] trace = new Throwable().getStackTrace();
		for( int i = 0; i < trace.length; i++ ) {
			if( !trace[i].getClassName().equals(Log.class.getName()) ) {
				clazz = trace[i].getClassName();
				method = trace[i].getMethodName();
				break;
			}
		}

		if( throwable != null ) {
			LOGGER.logp(level, clazz, method, throwable, msgSupplier);
		} else {
			LOGGER.logp(level, clazz, method, msgSupplier);
		}
	}

	public boolean isLoggable( Level level ) {
		return LOGGER.isLoggable(level);
	}

	public void info( final CharSequence msg ) {
		this.log(INFO, msg);
	}

	public void info( final CharSequence msg, Object... params ) {
		this.log(INFO, () -> String.format(msg.toString(), params));
	}

	public void info( final Supplier<String> msgSupplier ) {
		this.log(INFO, msgSupplier);
	}

	public void info( final Throwable ex, final CharSequence msg, Object... params ) {
		this.log(INFO, ex, () -> String.format(msg.toString(), params));
	}

	public void info( final Throwable throwable, final Supplier<String> msgSupplier ) {
		this.log(INFO, throwable, msgSupplier);
	}

	public void warn( final CharSequence msg ) {
		this.log(WARNING, msg);
	}

	public void warn( final CharSequence msg, Object... params ) {
		this.log(WARNING, () -> String.format(msg.toString(), params));
	}

	public void warn( final Supplier<String> msgSupplier ) {
		this.log(WARNING, msgSupplier);
	}

	public void warn( final Throwable ex, final CharSequence msg, Object... params ) {
		this.log(WARNING, ex, () -> String.format(msg.toString(), params));
	}

	public void warn( final Throwable throwable, final Supplier<String> msgSupplier ) {
		this.log(WARNING, throwable, msgSupplier);
	}

	public void error( final CharSequence msg ) {
		this.log(SEVERE, msg);
	}

	public void error( final CharSequence msg, Object... params ) {
		this.log(SEVERE, () -> String.format(msg.toString(), params));
	}

	public void error( final Supplier<String> msgSupplier ) {
		this.log(SEVERE, msgSupplier);
	}

	public void error( final Throwable ex, final CharSequence msg, Object... params ) {
		this.log(SEVERE, ex, () -> String.format(msg.toString(), params));
	}

	public void error( final Throwable throwable, final Supplier<String> msgSupplier ) {
		this.log(SEVERE, throwable, msgSupplier);
	}

	public void debug( final CharSequence msg ) {
		this.log(FINE, msg);
	}

	public void debug( final CharSequence msg, Object... params ) {
		this.log(FINE, () -> String.format(msg.toString(), params));
	}

	public void debug( final Supplier<String> msgSupplier ) {
		this.log(FINE, msgSupplier);
	}

	public void debug( final Throwable ex, final CharSequence msg, Object... params ) {
		this.log(FINE, ex, () -> String.format(msg.toString(), params));
	}

	public void debug( final Throwable throwable, final Supplier<String> msgSupplier ) {
		this.log(FINE, throwable, msgSupplier);
	}

	public void trace( final CharSequence msg ) {
		this.log(FINER, msg);
	}

	public void trace( final CharSequence msg, Object... params ) {
		this.log(FINER, () -> String.format(msg.toString(), params));
	}

	public void trace( final Supplier<String> msgSupplier ) {
		this.log(FINER, msgSupplier);
	}

	public void trace( final Throwable ex, final CharSequence msg, Object... params ) {
		this.log(FINER, ex, () -> String.format(msg.toString(), params));
	}

	public void trace( final Throwable throwable, final Supplier<String> msgSupplier ) {
		this.log(FINER, throwable, msgSupplier);
	}

}
