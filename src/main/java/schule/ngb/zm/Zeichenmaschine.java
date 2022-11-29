package schule.ngb.zm;

import schule.ngb.zm.anim.Animation;
import schule.ngb.zm.layers.ColorLayer;
import schule.ngb.zm.layers.DrawingLayer;
import schule.ngb.zm.layers.ImageLayer;
import schule.ngb.zm.layers.ShapesLayer;
import schule.ngb.zm.util.io.ImageLoader;
import schule.ngb.zm.util.Log;
import schule.ngb.zm.util.tasks.TaskRunner;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hauptklasse der Zeichenmaschine.
 * <p>
 * Projekte der Zeichenmaschine sollten als Unterklasse implementiert werden.
 * Die Klasse übernimmt die Initialisierung eines Programmfensters und der
 * nötigen Komponenten.
 */
@SuppressWarnings( "unused" )
public class Zeichenmaschine extends Constants {

	/**
	 * Gibt an, ob die Zeichenmaschine aus BlueJ heraus gestartet wurde.
	 */
	public static final boolean IN_BLUEJ;

	static {
		IN_BLUEJ = System.getProperty("java.class.path").contains("bluej");
	}

	/*
	 * Objektvariablen, die von Unterklassen benutzt werden können.
	 */

	/**
	 * Die Leinwand, auf die alles gezeichnet wird. Die Leinwand enthält
	 * {@link Layer Ebenen}, die einzelne Zeichnungen enthalten. Die Inhalte
	 * aller Ebenen werden einmal pro Frame auf die Hauptleinwand übertragen.
	 */
	protected Zeichenleinwand canvas;

	/**
	 * Ebene mit der Hintergrundfarbe.
	 */
	protected static ColorLayer background;

	/**
	 * Zeichenebene
	 */
	protected static DrawingLayer drawing;

	/**
	 * Formenebene
	 */
	protected static ShapesLayer shapes;

	////////


	/*
	 * Interne Attribute zur Steuerung der Zeichenmaschine.
	 */
	//@formatter:off

	/**
	 * Das Zeichenfenster der Zeichenmaschine
	 */
	protected Zeichenfenster frame;

	// Aktueller Zustand der Zeichenmaschine.

	/**
	 * Zustand der Zeichenmaschine insgesamt
	 */
	private Options.AppState state;

	/**
	 * Zustand des update/draw Threads
	 */
	private Options.AppState updateState = Options.AppState.STOPPED;

	/**
	 * Ob der Zeichenthread noch laufen soll, oder beendet.
	 */
	private boolean running;

	private boolean terminateImediately = false;

	/**
	 * Ob die ZM nach dem nächsten Frame pausiert werden soll.
	 */
	private boolean pause_pending = false;

	/**
	 * Ob die ZM bei nicht überschriebener update() Methode stoppen soll,
	 * oder trotzdem weiterläuft.
	 */
	private final boolean run_once;

	/**
	 * Aktuelle Frames pro Sekunde der Zeichenmaschine.
	 */
	private int framesPerSecondInternal;

	/**
	 * Hauptthread der Zeichenmaschine.
	 */
	private final Thread mainThread;

	/**
	 * Queue für geplante Aufgaben
	 */
	private final DelayQueue<DelayedTask> taskQueue = new DelayQueue<>();

	/**
	 * Queue für abgefangene InputEvents
	 */
	private final BlockingQueue<InputEvent> eventQueue = new LinkedBlockingQueue<>();

	/**
	 * Gibt an, ob nach Ende des Hauptthreads das Programm beendet werden soll,
	 * oder das Zeichenfenster weiter geöffnet bleibt.
	 */
	private boolean quitAfterShutdown = false;

	// Mauszeiger
	/**
	 * Cache für den unsichtbaren Mauszeiger, wenn {@link #hideCursor()}
	 * aufgerufen wurde.
	 */
	private Cursor invisibleCursor = null;

	/**
	 * Ob der Mauszeiger derzeit sichtbar ist (bzw. sein sollte).
	 */
	protected boolean cursorVisible = true;
	//@formatter:on


	/**
	 * Erstellt eine neue Zeichenmaschine mit Standardwerten für Titel und
	 * Größe.
	 * <p>
	 * Siehe {@link #Zeichenmaschine(int, int, String, boolean)} für mehr
	 * Details.
	 */
	public Zeichenmaschine() {
		this(APP_NAME + " " + APP_VERSION);
	}

	/**
	 * Erstellt eine neue Zeichenmaschine mit Standardwerten für Titel und
	 * Größe.
	 * <p>
	 * Siehe {@link #Zeichenmaschine(int, int, String, boolean)} für mehr
	 * Details.
	 *
	 * @param run_once {@code true} beendet die Zeichenmaschine nach einem
	 * 	Aufruf von {@code draw()}.
	 */
	public Zeichenmaschine( boolean run_once ) {
		this(APP_NAME + " " + APP_VERSION, run_once);
	}

	/**
	 * Erstellt eine neue Zeichenmaschine mit dem angegebene Titel und
	 * Standardwerten für die Größe.
	 * <p>
	 * Siehe {@link #Zeichenmaschine(int, int, String, boolean)} für mehr
	 * Details.
	 *
	 * @param title Der Titel, der oben im Fenster steht.
	 */
	public Zeichenmaschine( String title ) {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT, title, true);
	}

	/**
	 * Erstellt eine neue Zeichenmaschine mit dem angegebene Titel und
	 * Standardwerten für die Größe.
	 * <p>
	 * Siehe {@link #Zeichenmaschine(int, int, String, boolean)} für mehr
	 * Details.
	 *
	 * @param title Der Titel, der oben im Fenster steht.
	 * @param run_once {@code true} beendet die Zeichenmaschine nach einem
	 * 	Aufruf von {@code draw()}.
	 */
	public Zeichenmaschine( String title, boolean run_once ) {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT, title, run_once);
	}

	/**
	 * Erstellt eine neue zeichenmaschine mit einer Leinwand der angegebenen
	 * Größe und dem angegebenen Titel.
	 * <p>
	 * Siehe {@link #Zeichenmaschine(int, int, String, boolean)} für mehr
	 * Details.
	 *
	 * @param width Breite der {@link Zeichenleinwand Zeichenleinwand}.
	 * @param height Höhe der {@link Zeichenleinwand Zeichenleinwand}.
	 * @param title Der Titel, der oben im Fenster steht.
	 */
	public Zeichenmaschine( int width, int height, String title ) {
		this(width, height, title, true);
	}

	/**
	 * Erstellt eine neue zeichenmaschine mit einer Leinwand der angegebenen
	 * Größe und dem angegebenen Titel.
	 * <p>
	 * Die Zeichenmaschine öffnet automatisch ein Fenster mit einer
	 * {@link Zeichenleinwand}, die {@code width} Pixel breit und {@code height}
	 * Pixel hoch ist. Die Leinwand hat immer eine Mindestgröße von 100x100
	 * Pixeln und kann nicht größer als der aktuelle Bildschirm werden. Das
	 * Fenster bekommt den angegebenen Titel.
	 * <p>
	 * Falls {@code run_once} gleich {@code false} ist, werden
	 * {@link #update(double)} und {@link #draw()} entsprechend der
	 * {@link #framesPerSecond} kontinuierlich aufgerufen. Falls das Programm
	 * als Unterklasse der Zeichenmaschine verfasst wird, dann kann auch,
	 * {@code update(double)} überschrieben werden, damit die Maschine nicht
	 * automatisch beendet.
	 *
	 * @param width Breite der {@link Zeichenleinwand Zeichenleinwand}.
	 * @param height Höhe der {@link Zeichenleinwand Zeichenleinwand}.
	 * @param title Der Titel, der oben im Fenster steht.
	 * @param run_once {@code true} beendet die Zeichenmaschine nach einem
	 * 	Aufruf von {@code draw()}.
	 */
	@SuppressWarnings("static-access")
	public Zeichenmaschine( int width, int height, String title, boolean run_once ) {
		LOG.info("Starting " + APP_NAME + " " + APP_VERSION);

		// Register Cmd+Q on macOS
		if( Constants.MACOS ) {
			System.setProperty("apple.eawt.quitStrategy", "CLOSE_ALL_WINDOWS");
		}

		// Erstellen der Leinwand
		canvas = new Zeichenleinwand(width, height);

		// Erstellen des Zeichenfensters
		frame = createFrame(canvas, title);

		// Wir kennen nun den Bildschirm und können die Breite / Höhe abrufen.
		java.awt.Rectangle canvasBounds = frame.getCanvasBounds();
		this.canvasWidth = canvasBounds.width;
		this.canvasHeight = canvasBounds.height;
		java.awt.Rectangle displayBounds = frame.getScreenBounds();
		this.screenWidth = displayBounds.width;
		this.screenHeight = displayBounds.height;

		// Die drei Standardebenen merken, für den einfachen Zugriff aus Unterklassen.
		background = getBackgroundLayer();
		drawing = getDrawingLayer();
		shapes = getShapesLayer();

		// FPS setzen
		framesPerSecondInternal = DEFAULT_FPS;
		this.run_once = run_once;

		// Settings der Unterklasse aufrufen, falls das Fenster vor dem Öffnen
		// verändert werden soll.
		// TODO: (ngb) Wann sollte settings() aufgerufen werden?
		settings();

		// Listener hinzufügen, um auf Maus- und Tastatureingaben zu hören.
		InputListener inputListener = new InputListener();
		canvas.addMouseListener(inputListener);
		canvas.addMouseMotionListener(inputListener);
		canvas.addMouseWheelListener(inputListener);
		canvas.addKeyListener(inputListener);

		/*KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent( KeyEvent e ) {
				enqueueEvent(e);
				return false;
			}
		});*/

		// Programm beenden, wenn Fenster geschlossen wird
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing( WindowEvent e ) {
				if( isTerminated() ) {
					quit(true);
				} else {
					exitNow();
				}
			}
		});

		// Fenster anzeigen
		frame.centerFrame();
		frame.setVisible(true);

		// Nach dem Anzeigen kann die Pufferstrategie erstellt werden.
		canvas.allocateBuffer();

		// Erstellen des Haupt-Zeichenthreads.
		running = true;
		mainThread = new Zeichenthread();

		//frame.requestFocusInWindow();
		canvas.requestFocus();

		// Fertig mit der Initialisierung!
		state = Options.AppState.INITIALIZED;
		// Los geht's ...
		mainThread.start();
	}

	/**
	 * Erstellt ein neues Zeichenfesnter mit der aktuellen Konfiguration.
	 *
	 * @param title
	 */
	private Zeichenfenster createFrame( Zeichenleinwand c, String title ) {
		while( frame == null ) {
			try {
				TaskRunner.invokeLater(() -> {
					Zeichenfenster.setLookAndFeel();
					frame = new Zeichenfenster(canvas, title);
				}).get();
			} catch( InterruptedException e ) {
				// Keep waiting
			} catch( ExecutionException e ) {
				LOG.error(e, "Error initializing application frame: %s", e.getMessage());
				throw new RuntimeException(e);
			}
		}
		return frame;
	}

	/**
	 * Zentriert das Zeichenfenster auf dem aktuellen Bildschirm.
	 */
	public final void centerFrame() {
		frame.centerFrame();
	}

	/**
	 * Aktiviert oder deaktiviert den Vollbildmodus für die Zeichenmaschine.
	 * <p>
	 * Der Vollbildmodus wird abhängig von {@code pEnable} entweder aktiviert
	 * oder deaktiviert. Wird die Zeichenmaschine in den Vollbildmodus versetzt,
	 * dann wird automatisch ein {@link KeyListener} aktiviert, der bei
	 * Betätigung der ESCAPE-Taste den Vollbildmodus verlässt. Wird der
	 * Vollbildmodus verlassen, wird die zuletzt gesetzte Fenstergröße
	 * wiederhergestellt.
	 *
	 * @param pEnable Wenn {@code true}, wird der Vollbildmodus aktiviert,
	 * 	ansonsten deaktiviert.
	 */
	public final void setFullscreen( boolean pEnable ) {
		if( pEnable && !frame.isFullscreen() ) {
			frame.setFullscreen(true);

			canvasWidth = canvas.getWidth();
			canvasHeight = canvas.getHeight();

			if( frame.isFullscreen() )
				fullscreenChanged();
		} else if( !pEnable && frame.isFullscreen() ) {
			frame.setFullscreen(false);

			canvasWidth = canvas.getWidth();
			canvasHeight = canvas.getHeight();

			if( !frame.isFullscreen() )
				fullscreenChanged();
		}
	}

	/**
	 * Prüft, ob das Zeichenfenster im Vollbild läuft.
	 *
	 * @return {@code true}, wenn sich das Fesnter im Vollbildmodus befindet,
	 *    {@code false} sonst.
	 */
	public boolean isFullscreen() {
		return frame.isFullscreen();
	}

	/**
	 * Gibt den aktuellen {@link Options.AppState Zustand} der Zeichenmaschine
	 * zurück.
	 *
	 * @return Der Zustand der Zeichenmaschine.
	 */
	public Options.AppState getState() {
		return state;
	}

	/**
	 * Zeigt das Zeichenfenster an.
	 */
	public final void show() {
		if( !frame.isVisible() ) {
			frame.setVisible(true);
		}
	}

	/**
	 * Versteckt das Zeichenfenster.
	 */
	public final void hide() {
		if( frame.isVisible() ) {
			frame.setVisible(false);
		}
	}

	/**
	 * Zeichnet die {@link Zeichenleinwand} neu und zeigt den aktuellen Inhalt
	 * im Zeichenfenster an.
	 */
	public final void redraw() {
		if( state == Options.AppState.PAUSED
			|| state == Options.AppState.TERMINATED ) {
			draw();
		}
		render();
	}

	/**
	 * Zeigt den aktuellen Inhalt der {@link Zeichenleinwand}
	 * im Zeichenfenster an, ohne vorher {@link #draw()} aufzurufen.
	 */
	public final void render() {
		canvas.render();
		// canvas.invalidate();
		// frame.repaint();
		// hide();
		// show();
	}

	/**
	 * Pausiert die Ausführung von {@link #update(double)} und {@link #draw()}
	 * nach dem nächsten vollständigen Frame.
	 * <p>
	 * Die Zeichenmaschine wechselt in den Zustand
	 * {@link Options.AppState#PAUSED}, sobald der aktuelle Frame beendet
	 * wurde.
	 */
	public final void pause() {
		pause_pending = true;
	}

	/**
	 * Setzt die Ausführung der Zeichenmaschine fort, nachdem diese mit
	 * {@link #pause()} pausiert wurde.
	 */
	public final void resume() {
		pause_pending = false;
		if( state == Options.AppState.PAUSED ) {
			state = Options.AppState.RUNNING;
		}
	}

	/**
	 * Prüft, ob die Zeichenmaschine gerade pausiert ist.
	 *
	 * @return
	 */
	public final boolean isPaused() {
		return state == Options.AppState.PAUSED;
	}

	public final boolean isTerminated() {
		return state == Options.AppState.TERMINATED;
	}

	public final boolean isTerminating() {
		return state == Options.AppState.STOPPED || state == Options.AppState.TERMINATED;
	}

	/**
	 * Stoppt die Zeichenmaschine.
	 * <p>
	 * Nachdem der aktuelle Frame gezeichnet wurde wechselt die Zeichenmaschine
	 * in den Zustand {@link Options.AppState#STOPPED} und ruft
	 * {@link #shutdown()} auf. Nachdem {@code teardown()} ausgeführt wurde
	 * wechselt der Zustand zu {@link Options.AppState#TERMINATED}. Das
	 * Zeichenfenster bleibt weiter geöffnet.
	 * <p>
	 * Die Zeichenmaschine reagiert in diesem Zustand weiter auf Eingaben,
	 * allerdings muss die Zeichnung nun manuell mit {@link #redraw()}
	 * aktualisiert werden.
	 */
	public final void stop() {
		running = false;
	}

	/**
	 * Führt interne Aufräumarbeiten durch.
	 * <p>
	 * Wird nach dem {@link #stop() Stopp} der Zeichenmaschine aufgerufen und
	 * verbleibende Threads, Tasks, etc. zu stoppen und aufzuräumen. Die
	 * Äquivalente Methode für Unterklassen ist {@link #shutdown()}, die direkt
	 * vor {@code cleanup()} aufgerufen wird.
	 */
	private void cleanup() {
		LOG.debug("%s shutting down.", APP_NAME);
		// Alle noch nicht ausgelösten Events werden entfernt
		eventQueue.clear();
		// Alle noch nicht ausgeführten Tasks werden entfernt
		taskQueue.clear();
		// TaskRunner stoppen
		TaskRunner.shutdown();
	}

	/**
	 * Beendet die Zeichenmaschine vollständig.
	 * <p>
	 * Das Programm wird {@link #quit() beendet} und alle geöffneten Fenster
	 * geschlossen. Falls die Maschine noch läuft, wird sie zunächst nach dem
	 * nächsten vollständigen Frame {@link #stop() gestoppt}.
	 */
	public final void exit() {
		if( running ) {
			running = false;
			quitAfterShutdown = true;
		} else {
			quit(true);
		}
	}

	public final void exitNow() {
		// Do nothing, when already quitting
		if( state == Options.AppState.QUITING ) {
			return;
		}

		if( running ) {
			running = false;
			terminateImediately = true;
			quitAfterShutdown = true;
			mainThread.interrupt();
		} else {
			quit(true);
		}
	}

	/**
	 * Beendet das Programm vollständig.
	 * <p>
	 * Enspricht dem Aufruf {@code quit(true)}.
	 *
	 * @see #quit(boolean)
	 */
	public final void quit() {
		//quit(!IN_BLUEJ);
		quit(true);
	}

	/**
	 * Beendet das Programm. Falls {@code exit} gleich {@code true} ist, wird
	 * die komplette VM beendet.
	 * <p>
	 * Die Methode sorgt nicht für ein ordnungsgemäßes herunterfahren und
	 * freigeben aller Ressourcen, da die Zeichenmaschine gegebenenfalls
	 * geöffnet bleiben und weitere Aufgaben erfüllen soll. Aufrufende Methoden
	 * sollten dies berücksichtigen.
	 * <p>
	 * Soll das Programm vollständig beendet werden, ist es ratsamer
	 * {@link #exit()} zu verwenden.
	 *
	 * @param exit Ob die VM beendet werden soll.
	 * @see System#exit(int)
	 */
	public final void quit( boolean exit ) {
		state = Options.AppState.QUITING;
		TaskRunner.invokeLater(() -> {
			frame.setVisible(false);
			canvas.dispose();
			frame.dispose();

			if( exit ) {
				System.exit(0);
			}
		});
	}

	/**
	 * Ändert die Größe der {@link Zeichenleinwand}.
	 *
	 * @param width Neue Breite der Zeichenleinwand.
	 * @param height Neue Höhe der Zeichenleinwand.
	 * @see Zeichenleinwand#setSize(int, int)
	 */
	public final void setSize( int width, int height ) {
		frame.setCanvasSize(width, height);

		java.awt.Rectangle canvasBounds = frame.getCanvasBounds();
		canvasWidth = canvasBounds.width;
		canvasHeight = canvasBounds.height;
	}

	/**
	 * Die Breite der {@link Zeichenleinwand}.
	 *
	 * @return Die Breite der {@link Zeichenleinwand}.
	 */
	public final int getWidth() {
		return canvasWidth;
	}

	/**
	 * Die Höhe der {@link Zeichenleinwand}.
	 *
	 * @return Die Höhe der {@link Zeichenleinwand}.
	 */
	public final int getHeight() {
		return canvasHeight;
	}

	/**
	 * Setzt den Titel des Zeichenfensters.
	 *
	 * @param title Der Titel, der oben im Zeichenfenster angezeigt wird.
	 */
	public final void setTitle( String title ) {
		frame.setTitle(title);
	}

	/**
	 * Gibt die Zeichenleinwand zurück.
	 *
	 * @return Die Zeichenleinwand.
	 */
	public final Zeichenleinwand getCanvas() {
		return canvas;
	}

	/**
	 * Fügt der {@link Zeichenleinwand} eine weitere {@link Layer Ebene} hinzu.
	 *
	 * @param layer Die neue Ebene.
	 */
	public final void addLayer( Layer layer ) {
		canvas.addLayer(layer);
		layer.setSize(getWidth(), getHeight());
	}

	/**
	 * Gibt die Anzahl der {@link Layer Ebenen} in der {@link Zeichenleinwand}
	 * zurück.
	 *
	 * @return Die Anzahl der Ebenen.
	 */
	public final int getLayerCount() {
		return canvas.getLayerCount();
	}

	/**
	 * Gibt die {@link Layer Ebene} am angegebenen Index zurück. Gibt es keine
	 * Ebene mit diesem Index.
	 *
	 * @param index
	 * @return
	 */
	public final Layer getLayer( int index ) {
		return canvas.getLayer(index);
	}

	/**
	 * Gibt die erste (unterste) {@link Layer Ebene} der angegebenen Klasse
	 * zurück.
	 *
	 * <pre><code>
	 * DrawingLayer draw = getLayer(DrawingLayer.class);
	 * </code></pre>
	 *
	 * @param layerClass
	 * @param <LT>
	 * @return
	 */
	public final <LT extends Layer> LT getLayer( Class<LT> layerClass ) {
		return canvas.getLayer(layerClass);
	}

	/**
	 * Gibt die {@link ColorLayer Ebene} mit der Hintergrundfarbe zurück. Gibt
	 * es keine solche Ebene, so wird eine erstellt und der
	 * {@link Zeichenleinwand} hinzugefügt.
	 * <p>
	 * In der Regel sollte dies dieselbe Ebene sein wie {@link #background}.
	 *
	 * @return Die Hintergrundebene.
	 */
	public final ColorLayer getBackgroundLayer() {
		ColorLayer layer = canvas.getLayer(ColorLayer.class);
		if( layer == null ) {
			layer = new ColorLayer(DEFAULT_BACKGROUND);
			canvas.addLayer(0, layer);
		}
		return layer;
	}

	/**
	 * Gibt die Standard-{@link DrawingLayer Zeichenebene} zurück. Gibt es keine
	 * solche Ebene, so wird eine erstellt und der {@link Zeichenleinwand}
	 * hinzugefügt.
	 * <p>
	 * In der Regel sollte dies dieselbe Ebene sein wie {@link #drawing}.
	 *
	 * @return Die Zeichenebene.
	 */
	public final DrawingLayer getDrawingLayer() {
		DrawingLayer layer = canvas.getLayer(DrawingLayer.class);
		if( layer == null ) {
			layer = new DrawingLayer(getWidth(), getHeight());
			canvas.addLayer(1, layer);
		}
		return layer;
	}

	/**
	 * Gibt die Standard-{@link ShapesLayer Formenebene} zurück. Gibt es keine
	 * solche Ebene, so wird eine erstellt und der {@link Zeichenleinwand}
	 * hinzugefügt.
	 * <p>
	 * In der Regel sollte dies dieselbe Ebene sein wie {@link #shapes}.
	 *
	 * @return Die Formenebene.
	 */
	public final ShapesLayer getShapesLayer() {
		ShapesLayer layer = canvas.getLayer(ShapesLayer.class);
		if( layer == null ) {
			layer = new ShapesLayer(getWidth(), getHeight());
			canvas.addLayer(2, layer);
		}
		return layer;
	}

	/**
	 * Gibt die aktuellen Frames pro Sekunde zurück.
	 *
	 * @return Angepeilte Frames pro Sekunde
	 */
	public final int getFramesPerSecond() {
		return framesPerSecondInternal;
	}

	/**
	 * Setzt die Anzahl an Frames pro Sekunde auf einen neuen Wert.
	 *
	 * @param pFramesPerSecond Neue FPS.
	 */
	public final void setFramesPerSecond( int pFramesPerSecond ) {
		if( pFramesPerSecond > 0 ) {
			framesPerSecondInternal = pFramesPerSecond;
		} else {
			framesPerSecondInternal = 1;
			// Logger ...
		}
		framesPerSecond = framesPerSecondInternal;
	}

	/**
	 * Speichert den aktuellen Inhalt der {@link Zeichenleinwand} in einer
	 * Bilddatei auf der Festplatte. Zur Auswahl der Zieldatei wird dem Nutzer
	 * ein {@link JFileChooser} angezeigt.
	 */
	public final void saveImage() {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jfc.setMultiSelectionEnabled(false);

		int status = jfc.showSaveDialog(frame);
		if( status == JFileChooser.APPROVE_OPTION ) {
			File outfile = jfc.getSelectedFile();
			if( outfile.isDirectory() ) {
				outfile = new File(outfile.getAbsolutePath() + File.separator + "zeichenmaschine.png");
			}
			saveImage(outfile.getAbsolutePath());
		}
	}

	/**
	 * Speichert den aktuellen Inhalt der {@link Zeichenleinwand} in einer
	 * Bilddatei im angegebenen Dateipfad auf der Festplatte.
	 */
	public final void saveImage( String filepath ) {
		BufferedImage img = ImageLoader.createImage(canvas.getWidth(), canvas.getHeight());

		Graphics2D g = img.createGraphics();
		g.setColor(DEFAULT_BACKGROUND.getJavaColor());
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		canvas.draw(g);
		g.dispose();

		try {
			ImageLoader.saveImage(img, new File(filepath), true);
		} catch( IOException ex ) {
			ex.printStackTrace();
		}
	}

	/**
	 * Erstellt eine Momentanaufnahme des aktuellen Inhalts der
	 * {@link Zeichenleinwand} und erstellt daraus eine
	 * {@link ImageLayer Bildebene}. Die Ebene wird automatisch der
	 * {@link Zeichenleinwand} vor dem {@link #background} hinzugefügt.
	 *
	 * @return Die neue Bildebene.
	 */
	public final ImageLayer snapshot() {
		BufferedImage img = ImageLoader.createImage(canvas.getWidth(), canvas.getHeight());

		Graphics2D g = img.createGraphics();
		g.setColor(DEFAULT_BACKGROUND.getJavaColor());
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		canvas.draw(g);
		g.dispose();

		/*
		float factor = 0.8f;
		float base = 255f * (1f - factor);
		RescaleOp op = new RescaleOp(factor, base, null);
		BufferedImage filteredImage
			= new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		op.filter(img, filteredImage);
		*/

		ImageLayer imgLayer = new ImageLayer(img);
		if( canvas.getLayer(0) instanceof ColorLayer ) {
			canvas.addLayer(1, imgLayer);
		} else {
			canvas.addLayer(0, imgLayer);
		}
		return imgLayer;
	}

	/**
	 * Pausiert die Schleife der Zeichenmaschine für die angegebene Anzahl an
	 * Millisekunden.
	 * <p>
	 * Falls {@code delay()} während eines Aufrufs von {@link #draw()}
	 * aufgerufen wird, dann wird der aktuelle Zustand der Leinwand angezeigt.
	 * <p>
	 * Die Methode übernimmt keine Garantie, dass die Wartezeit exakt {@code ms}
	 * Millisekunden beträgt. Sie kann etwas kürzer oder (für kurze Wartezeiten)
	 * etwas länger sein. Für zeitkritische Simulationen sollte daher die genaue
	 * Zeitdifferenz gemessen und berücksichtigt werden.
	 *
	 * @param ms Wartezeit in Millisekunden.
	 */
	public final void delay( int ms ) {
		if( ms <= 0 ) {
			return;
		}

		if( state == Options.AppState.INITIALIZING ||
			state == Options.AppState.INITIALIZED ) {
			LOG.warn("Don't use delay(int) from within settings() or setup().");
			return;
		}

		long timer = 0L;
		if( /*updateState == Options.AppState.DRAWING*/
			isTerminating() ) {
			// Falls gerade draw() ausgeführt wird, zeigen wir den aktuellen
			// Stand der Zeichnung auf der Leinwand an. Die Zeit für das
			// Rendern wird gemessen und von der Wartezeit abgezogen.
			timer = System.nanoTime();
			canvas.render();
			timer = System.nanoTime() - timer;
		}

		Options.AppState oldState = updateState;
		try {
			int sub = (int) Math.ceil(timer / 1000000.0);

			if( sub >= ms ) {
				return;
			}

			updateState = Options.AppState.DELAYED;
			Thread.sleep(ms - sub, (int) (timer % 1000000L));
		} catch( InterruptedException ignored ) {
			// Nothing
		} finally {
			updateState = oldState;
		}
	}

	/**
	 * Macht den Mauszeiger unsichtbar.
	 * <p>
	 * Nach dem Aufruf gilt {@code cursorVisible == false}.
	 * <p>
	 * Der Aufruf von {@code hideCursor()} ist dasselbe wie der Aufruf von
	 * {@link #setCursor(Cursor) setCursor(null)}.
	 */
	public final void hideCursor() {
		setCursor(null);
	}

	/**
	 * Zeigt den Mauszeiger wieder an, falls er zuvor
	 * {@link #hideCursor() versteckt} wurde.
	 * <p>
	 * Nach dem Aufruf gilt {@code cursorVisible == true}.
	 * <p>
	 * Der Aufruf von {@code hideCursor()} ist dasselbe wie der Aufruf von
	 * {@link #setCursor(int) setCursor(Cursor.DEFAULT_CURSOR)}.
	 */
	public final void showCursor() {
		setCursor(Cursor.DEFAULT_CURSOR);
	}

	/**
	 * Ändert den Mauszeiger auf ein eigenes Bild.
	 * <p>
	 * Das Bild darf die vom Betriebssystem vorgegebene Mindestgröße nicht
	 * überschreiten und kann aus einer beliebigen Quelle geladen werden, oder
	 * direkt im Programm erstellt werden. Die Koordinaten des Hotspot geben an,
	 * an welcher Stelle des Bildes sich die "Spitze" befindet. Die Koordinaten
	 * werden relativ zur oberen linken Ecke des Bildes angegeben.
	 *
	 * @param pCursorImage Ein Bild, das den Mauszeiger ersetzt.
	 * @param hotSpotX Relative x-Koordinate des Hotspots.
	 * @param hotSpotY Relative y-Koordinate des Hotspots.
	 * @see ImageLoader#loadImage(String)
	 * @see Toolkit#createCustomCursor(Image, Point, String)
	 */
	public final void setCursor( Image pCursorImage, int hotSpotX, int hotSpotY ) {
		Point hotSpot = new Point(hotSpotX, hotSpotY);
		setCursor(canvas.getToolkit().createCustomCursor(pCursorImage, hotSpot, "zmCursor"));
	}

	/**
	 * Setzt den Mauszeiger auf eines der vordefinierten Symbole.
	 * <p>
	 * Die Konstanten der Klasse {@link Cursor} definieren 13 Standardzeiger,
	 * die durch angabe der Nummer geladen werden können.
	 * <pre><code>
	 * setCursor(Cursor.HAND_CURSOR);
	 * </code></pre>
	 *
	 * @param pPredefinedCursor Eine der Cursor-Konstanten.
	 * @see Cursor
	 */
	public final void setCursor( int pPredefinedCursor ) {
		setCursor(Cursor.getPredefinedCursor(pPredefinedCursor));
	}

	/**
	 * Setzt den Mauszeiger auf das übergebenen Cursor-Objekt. Wenn
	 * {@code pCursor} {@code null} ist, wird der Mauszeiger unsichtbar gemacht
	 * (dies ist dasselbe wie der Aufruf von {@link #hideCursor()}).
	 *
	 * @param pCursor Ein Cursor-Objekt oder {@code null}.
	 * 	<p>
	 * 	Nach Aufruf der Methode kann über {@link #cursorVisible} abgefragt
	 * 	werden, ob der Cursor zurzeit sichtbar ist oder nicht.
	 */
	public final void setCursor( Cursor pCursor ) {
		if( pCursor == null && cursorVisible ) {
			// Falls null übergeben, Zeiger verstecken

			// Übernommen aus processing.awt.PSurfaceAWT von Processing4
			if( invisibleCursor == null ) {
				BufferedImage cursorImg =
					new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
				invisibleCursor =
					canvas.getToolkit().createCustomCursor(cursorImg, new Point(8, 8), "blank");
			}
			canvas.setCursor(invisibleCursor);
			cursorVisible = false;
		} else if( pCursor != null ) {
			// Zeiger neu zuweisen
			canvas.setCursor(pCursor);
			cursorVisible = true;
		}
	}

	/*
	 * Methoden, die von Unterklassen überschrieben werden können / sollen.
	 */

	/**
	 * Die Settings werden einmal beim Erstellten der Zeichenmaschine
	 * aufgerufen.
	 * <p>
	 * {@code settings()} wird nur selten benötigt, wenn das Zeichenfenster
	 */
	public void settings() {
		// Intentionally left blank
	}

	/**
	 * Methode, die von Unterklassen überschrieben werden sollte, um die
	 * Zeichenmaschine vor dem Start zu konfigurieren. Hier können vorbereitende
	 * Befehle ausgeführt werden, die die {@link Zeichenleinwand} zu
	 * initialisieren, neue Objekte instanziieren und Variablen initialisieren.
	 */
	public void setup() {
		// Intentionally left blank
	}

	/**
	 * {@code update()} wird einmal pro Frame vor {@link #draw()} aufgerufen, um
	 * notwendige Aktualisierungen vorzunehmen. Im Gegensatz zu {@link #draw()}
	 * bekommt {@code update()} zusätzlich {@link #delta} übergeben, um die
	 * Aktualisierungen abhängig von der echten Verzögerung zwischen zwei Frames
	 * zu berechnen.
	 * <p>
	 * {@code delta} wird in Sekunden angegeben. Um eine Form zum Beispiel um
	 * {@code 50} Pixel pro Sekunde in {@code x}-Richtung zu bewegen, kann so
	 * vorgegangen werden:
	 * <pre><code>
	 * shape.move(50*delta, 0.0);
	 * </code></pre>
	 *
	 * @param delta
	 */
	public void update( double delta ) {
		running = !run_once;
	}

	/**
	 * {@code draw()} wird einmal pro Frame aufgerufen. Bei einer
	 * {@link #getFramesPerSecond() Framerate} von 60 also in etwa 60-Mal pro
	 * Sekunde. In der {@code draw}-Methode wird der Inhalt der Ebenen
	 * manipuliert und deren Inhalte gezeichnet. Am Ende des Frames werden alle
	 * Ebenen auf die {@link Zeichenleinwand} übertragen.
	 * <p>
	 * {@code draw()} stellt die wichtigste Methode für eine Zeichenmaschine
	 * dar, da hier die Zeichnung des Programms erstellt wird.
	 */
	public void draw() {
		// Intentionally left blank
	}

	/**
	 * {@code teardown()} wird aufgerufen, sobald die Schleife des
	 * Hauptprogramms beendet wurde. Dies passiert entweder nach dem ersten
	 * Durchlauf (wenn keine eigene {@link #update(double)} erstellt wurde),
	 * nach dem Aufruf von {@link #stop()} oder nachdem das
	 * {@link Zeichenfenster} geschlossen wurde.
	 * <p>
	 * In {@code teardown()} kann zum Beispiel der Abschlussbildschirm eines
	 * Spiels oder der Abspann einer Animation angezeigt werden, oder mit
	 * {@link #saveImage()} die erstellte Zeichnung abgespeichert werden.
	 */
	public void shutdown() {
		// Intentionally left blank
	}

	/*
	 * Task scheduling
	 */

	public void scheduleTask( Runnable runnable, int delay ) {
		taskQueue.add(new DelayedTask(delay, runnable));
	}

	public void scheduleTask( Runnable runnable, int delay, boolean concurrent ) {
		DelayedTask task = new DelayedTask(delay, runnable);
		task.concurrent = concurrent;
		taskQueue.add(task);
	}

	private void runTasks() {
		synchronized( taskQueue ) {
			DelayedTask task = taskQueue.poll();
			while( task != null ) {
				if( task.concurrent ) {
					// SwingUtilities.invokeLater(task.runnable);
					TaskRunner.run(task.runnable);
				} else {
					task.runnable.run();
				}

				task = taskQueue.poll();
			}
		}
	}

	/*
	 * Input handling
	 */
	private void enqueueEvent( InputEvent evt ) {
		if( updateState != Options.AppState.DELAYED ) {
			eventQueue.add(evt);
		}

		if( isPaused() || isTerminated() ) {
			if( MouseEvent.class.isInstance(evt) ) {
				saveMousePosition((MouseEvent)evt);
			}
			dispatchEvents();
		}
	}

	private void dispatchEvents() {
		//synchronized( eventQueue ) {
		while( !eventQueue.isEmpty() ) {
			InputEvent evt = eventQueue.poll();

			switch( evt.getID() ) {
				case KeyEvent.KEY_TYPED:
				case KeyEvent.KEY_PRESSED:
				case KeyEvent.KEY_RELEASED:
					handleKeyEvent((KeyEvent) evt);
					break;

				case MouseEvent.MOUSE_CLICKED:
				case MouseEvent.MOUSE_PRESSED:
				case MouseEvent.MOUSE_RELEASED:
				case MouseEvent.MOUSE_MOVED:
				case MouseEvent.MOUSE_DRAGGED:
				case MouseEvent.MOUSE_WHEEL:
					handleMouseEvent((MouseEvent) evt);
					break;
			}
		}
		//}
	}

	private void handleKeyEvent( KeyEvent evt ) {
		keyEvent = evt;
		key = evt.getKeyChar();
		keyCode = evt.getKeyCode();

		switch( evt.getID() ) {
			case KeyEvent.KEY_TYPED:
				keyTyped(evt);
				break;
			case KeyEvent.KEY_PRESSED:
				keyPressed = true;
				keyPressed(evt);
				break;
			case KeyEvent.KEY_RELEASED:
				keyPressed = false;
				keyReleased(evt);
				break;
		}
	}

	private void handleMouseEvent( MouseEvent evt ) {
		mouseEvent = evt;

		switch( evt.getID() ) {
			case MouseEvent.MOUSE_CLICKED:
				mouseClicked(evt);
				break;
			case MouseEvent.MOUSE_PRESSED:
				mousePressed = true;
				mouseButton = evt.getButton();
				mousePressed(evt);
				break;
			case MouseEvent.MOUSE_RELEASED:
				mousePressed = false;
				mouseButton = NOMOUSE;
				mouseReleased(evt);
				break;
			case MouseEvent.MOUSE_DRAGGED:
				//saveMousePosition(evt);
				mouseDragged(evt);
				break;
			case MouseEvent.MOUSE_MOVED:
				//saveMousePosition(evt);
				mouseMoved(evt);
				break;
		}
	}

	public void mouseClicked( MouseEvent e ) {
		mouseClicked();
	}

	public void mouseClicked() {
		// Intentionally left blank
	}

	public void mousePressed( MouseEvent e ) {
		mousePressed();
	}

	public void mousePressed() {
		// Intentionally left blank
	}

	public void mouseReleased( MouseEvent e ) {
		mouseReleased();
	}

	public void mouseReleased() {
		// Intentionally left blank
	}

	public void mouseDragged( MouseEvent e ) {
		mouseDragged();
	}

	public void mouseDragged() {
		// Intentionally left blank
	}

	public void mouseMoved( MouseEvent e ) {
		mouseMoved();
	}

	public void mouseMoved() {
		// Intentionally left blank
	}

	private void saveMousePosition( MouseEvent event ) {
		if( mouseEvent != null && event.getComponent() == canvas ) {
			pmouseX = mouseX;
			pmouseY = mouseY;

			mouseX = cmouseX;
			mouseY = cmouseY;
		}
	}

	private void saveMousePosition() {
		pmouseX = mouseX;
		pmouseY = mouseY;

		// Calculates mouse position based on screen, not based on canvas
		java.awt.Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
		java.awt.Point compLoc = canvas.getLocationOnScreen();
		mouseX = mouseLoc.x - compLoc.x;
		mouseY = mouseLoc.y - compLoc.y;
	}

	/*
	 * Keyboard handling
	 */
	public void keyTyped( KeyEvent e ) {
		keyTyped();
	}

	public void keyTyped() {
		// Intentionally left blank
	}

	public void keyPressed( KeyEvent e ) {
		keyPressed();
	}

	public void keyPressed() {
		// Intentionally left blank
	}

	public void keyReleased( KeyEvent e ) {
		keyReleased();
	}

	public void keyReleased() {
		// Intentionally left blank
	}

	// Window changes
	public void fullscreenChanged() {
		// Intentionally left blank
	}

	////
	// Zeichenthread
	////

	/**
	 * Globaler Monitor, der einmal pro Frame vom Zeichenthread freigegeben
	 * wird. Andere Threads können {@link Object#wait()} auf dem Monitor
	 * aufrufen, um sich mit dem Zeichenthread zu synchronisieren. Der
	 * {@code wait()} Aufruf sollte sich zur Sicherheit in einer Schleife
	 * befinden, die prüft, ob sich der Aktuelle {@link #tick} erhöht hat.
	 * <pre><code>
	 * int lastTick = Constants.tick;
	 *
	 * // Do some work
	 *
	 * while( lastTick >= Constants.tick ) {
	 *     synchronized( Zeichenmaschine.globalSyncLock ) {
	 *         try {
	 *             Zeichenmaschine.globalSyncLock.wait();
	 *         } catch( InterruptedException ex ) {}
	 *     }
	 * }
	 * // Next frame has started
	 * </code></pre>
	 * <p>
	 * Die {@link schule.ngb.zm.util.tasks.FrameSynchronizedTask} implementiert
	 * eine {@link schule.ngb.zm.util.tasks.Task}, die sich automatisch auf
	 * diese Wiese mit dem Zeichenthread synchronisiert.
	 */
	public static final Object globalSyncLock = new Object[0];

	class Zeichenthread extends Thread {

		public Zeichenthread() {
			super(APP_NAME);
			//super(APP_NAME + " " + APP_VERSION);
		}

		@Override
		public final void run() {
			// Wait for full initialization before start
			while( state != Options.AppState.INITIALIZED ) {
				Thread.yield();
			}

			// ThreadExecutor for the update/draw Thread
			final UpdateThreadExecutor updateThreadExecutor = new UpdateThreadExecutor();

			// Start des Thread in ms
			final long start = System.currentTimeMillis();
			// Aktuelle Zeit in ns
			long beforeTime;
			long updateBeforeTime = System.nanoTime();
			// Speicher für Änderung
			long overslept = 0L;
			// Interne Zähler für tick und runtime
			int _tick = 0;
			long _runtime;
			// Öffentliche Zähler für Unterklassen
			tick = 0;
			runtime = 0;

			// setup() der Unterklasse aufrufen
			setup();

			// Alles startklar ...
			state = Options.AppState.RUNNING;
			while( running ) {
				// Aktuelle Zeit in ns merken
				beforeTime = System.nanoTime();

				// Mausposition einmal pro Frame merken
				saveMousePosition(mouseEvent);

				if( state != Options.AppState.PAUSED ) {
					// update() und draw() der Unterklasse werden in einem
					// eigenen Thread ausgeführt, aber der Zeichenthread
					// wartet, bis der Thread fertig ist. Außer die Unterklasse
					// ruft delay() auf und lässt den Thread eine länger Zeit
					// schlafen. Dann wird der nächst Frame vorzeitig gerendert,
					// bis der update-Thread wieder bereit ist. Dadurch können
					// nebenläufige Aufgaben (z.B. Animationen) weiterlaufen.
					if( !updateThreadExecutor.isRunning() ) {
						delta = (System.nanoTime() - updateBeforeTime) / 1000000000.0;
						updateBeforeTime = System.nanoTime();

						// uddate()/draw() ausführen
						updateThreadExecutor.execute(() -> {
							if( state == Options.AppState.RUNNING
								&& updateState == Options.AppState.IDLE ) {
								// Call to update()
								updateState = Options.AppState.UPDATING;
								Zeichenmaschine.this.update(delta);
								// Update Layers
								canvas.updateLayers(delta);
								// Call to draw()
								updateState = Options.AppState.DRAWING;
								Zeichenmaschine.this.draw();
								updateState = Options.AppState.DISPATCHING;
								// Send latest input events after finishing draw
								// since these may also block
								dispatchEvents();
								updateState = Options.AppState.IDLE;
							}
						});
					}

					// Wait for the update/draw Thread to finish
					while( updateThreadExecutor.isRunning()
						&& !updateThreadExecutor.isWaiting() ) {
						Thread.yield();

						if( Thread.interrupted() ) {
							running = false;
							terminateImediately = true;
							break;
						}
					}

					// Display the current buffer content
					if( canvas != null && frame.isDisplayable() ) {
						canvas.render();
						// canvas.invalidate();
						// frame.repaint();
					}

					// dispatchEvents();
				}

				// Running pending tasks and notify any
				// waiting FrameSynchonizedTasks
				// TODO: should this this also happen in the updateThread?
				runTasks();
				synchronized( globalSyncLock ) {
					globalSyncLock.notifyAll();
				}

				// delta time in ns
				long afterTime = System.nanoTime();
				long dt = afterTime - beforeTime;
				long sleep = ((1000000000L / framesPerSecondInternal) - dt) - overslept;

				// Sleep before next frame
				if( sleep > 0 ) {
					try {
						Thread.sleep(sleep / 1000000L, (int) (sleep % 1000000L));
					} catch( InterruptedException e ) {
						// Interrupt not relevant
					}

					overslept = (System.nanoTime() - afterTime) - sleep;
				} else {
					overslept = 0L;
				}

				// Update stats
				_tick += 1;
				_runtime = System.currentTimeMillis() - start;
				tick = _tick;
				runtime = _runtime;
				framesPerSecond = framesPerSecondInternal;

				// If pause requested, we pause now
				if( pause_pending ) {
					state = Options.AppState.PAUSED;
					pause_pending = false;
				}
			}
			state = Options.AppState.STOPPED;
			// Shutdown the updateThread
			while( !terminateImediately && updateThreadExecutor.isRunning() ) {
				Thread.yield();
			}
			updateThreadExecutor.shutdownNow();

			// Cleanup
			shutdown();
			cleanup();
			state = Options.AppState.TERMINATED;

			if( quitAfterShutdown ) {
				quit();
			}
		}

	}

	// TODO: Remove
	static class DelayedTask implements Delayed {

		long startTime; // in ms

		Runnable runnable;

		boolean concurrent = false;

		public DelayedTask( int delay, Runnable runnable ) {
			this.startTime = System.currentTimeMillis() + delay;
			this.runnable = runnable;
		}

		@Override
		public long getDelay( TimeUnit unit ) {
			int diff = (int) (startTime - System.currentTimeMillis());
			return unit.convert(diff, TimeUnit.MILLISECONDS);
		}

		@Override
		public int compareTo( Delayed o ) {
			return (int) (startTime - ((DelayedTask) o).startTime);
		}

	}

	class InputListener implements MouseInputListener, MouseMotionListener, MouseWheelListener, KeyListener {

		@Override
		public void mouseClicked( MouseEvent e ) {
			enqueueEvent(e);
		}

		@Override
		public void mousePressed( MouseEvent e ) {
			enqueueEvent(e);
		}

		@Override
		public void mouseReleased( MouseEvent e ) {
			enqueueEvent(e);
		}

		@Override
		public void mouseEntered( MouseEvent e ) {
			// Intentionally left blank
		}

		@Override
		public void mouseExited( MouseEvent e ) {
			// Intentionally left blank
		}

		@Override
		public void mouseDragged( MouseEvent e ) {
			cmouseX = e.getX();
			cmouseY = e.getY();
			enqueueEvent(e);
		}

		@Override
		public void mouseMoved( MouseEvent e ) {
			cmouseX = e.getX();
			cmouseY = e.getY();
			enqueueEvent(e);
		}

		@Override
		public void keyTyped( KeyEvent e ) {
			enqueueEvent(e);
		}

		@Override
		public void keyPressed( KeyEvent e ) {
			enqueueEvent(e);
		}

		@Override
		public void keyReleased( KeyEvent e ) {
			enqueueEvent(e);
		}

		@Override
		public void mouseWheelMoved( MouseWheelEvent e ) {
			// enqueueEvent(e);
		}

	}

	// TODO: (ngb) exception handling when update/draw throws ex
	class UpdateThreadExecutor extends ThreadPoolExecutor {

		private Thread updateThread;

		private boolean running = false;

		public UpdateThreadExecutor() {
			super(1, 1, 0L,
				TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
				new ThreadFactory() {
					private final AtomicInteger threadNumber = new AtomicInteger(1);
					@Override
					public Thread newThread( Runnable r ) {
						Thread t = new Thread(mainThread.getThreadGroup(), r,
							"updateThread-" + threadNumber.getAndIncrement(),
							0);
						t.setDaemon(true);
						return t;
					}
				});
			updateState = Options.AppState.IDLE;
		}

		@Override
		public void execute( Runnable command ) {
			running = true;
			super.execute(command);
		}

		@Override
		protected void beforeExecute( Thread t, Runnable r ) {
			// We store the one Thread this Executor holds,
			// but it might change if a new Thread needed to be spawned
			// due to en error.
			updateThread = t;
		}

		@Override
		protected void afterExecute( Runnable r, Throwable t ) {
			running = false;
			updateState = Options.AppState.IDLE;
		}

		/**
		 * Ermittelt, ob der interne Thread gerade eine update/draw Task
		 * ausführt.
		 *
		 * @return
		 */
		public boolean isRunning() {
			return running;
		}

		/**
		 * Ermittelt, ob der interne Thread gerade eine update/draw Task
		 * ausführt und dabei in einen Wartezustand versetzt wurde. Das bedeutet
		 * in der Regel, dass innerhalb von {@link #update(double)} oder
		 * {@link #draw()} ein {@link #delay(int)} ausgeführt wurde, oder aus
		 * einem anderen Grund beispielsweise {@link Thread#sleep(long)}
		 * aufgerufen wurde. (Dies kann zum Beispiel beim
		 * {@link Animation#await() Warten auf Animationen} der Fall sein.)
		 *
		 * @return
		 */
		public boolean isWaiting() {
			//return running && updateThread.getState() == Thread.State.TIMED_WAITING;
			//return running && updateThread != null && updateThread.getState() == Thread.State.TIMED_WAITING;
			return running && updateThread != null && updateState == Options.AppState.DELAYED;
		}

	}

	private static final Log LOG = Log.getLogger(Zeichenmaschine.class);

}
