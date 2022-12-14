package schule.ngb.zm.util;

import schule.ngb.zm.Color;
import schule.ngb.zm.Constants;
import schule.ngb.zm.util.io.ImageLoader;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.exp;
import static java.lang.Math.log;
import static schule.ngb.zm.Constants.random;

/**
 * Eine Hilfsklasse, um zufällige Beispieldaten zu erzeugen.
 * <p>
 * Die Klasse kann verschiedene Arten realistischer Beispieldaten erzeugen.
 * Unter anderem Namen, E-Mail-Adressen, Passwörter oder Platzhalter-Bilder.
 */
@SuppressWarnings( "unused" )
public final class Faker {

	/**
	 * URL, von der extern generierte Fake-Bilder geladen werden können.
	 * <p>
	 * Die URL wird als Format-String definiert mit zwei {@code %d}
	 * Platzhaltern, die durch die Breite und Höhe des gewünschten Bildes
	 * ersetzt werden.
	 */
	public static final String FAKE_IMG_URL = "https://loremflickr.com/%d/%d";

	/**
	 * Erzeugt ein Array mit den angegebenen Anzahl zufälliger Benutzerdaten.
	 * <p>
	 * Jeder Datensatz besteht aus einem String-Array mit den folgenden Daten
	 * <ul>
	 * <li><code>fakeUser[i][0]</code>: Vorname</li>
	 * <li><code>fakeUser[i][1]</code>: Nachname</li>
	 * <li><code>fakeUser[i][2]</code>: Geschlecht</li>
	 * <li><code>fakeUser[i][3]</code>: Nutzername</li>
	 * <li><code>fakeUser[i][4]</code>: Passwort</li>
	 * <li><code>fakeUser[i][5]</code>: E-Mail</li>
	 * <li><code>fakeUser[i][6]</code>: Geburtsdatum</li>
	 * </ul>
	 *
	 * @param count Anzahl der Beispieldaten.
	 * @return Ein Array mit den Beispieldaten.
	 */
	public static String[][] fakeUsers( int count ) {
		return randomSample("users", count, ( line ) -> line.split(","), String[].class);
	}

	/**
	 * Erzeugt ein Array mit den angegebenen Anzahl zufälliger Vornamen.
	 *
	 * @param count Anzahl der Beispieldaten.
	 * @return Ein Array mit den Beispieldaten.
	 */
	public static String[] fakeNames( int count ) {
		return randomSample("users", count, ( line ) -> line.split(",")[0], String.class);
	}


	/**
	 * Erzeugt ein Array mit den angegebenen Anzahl zufälliger Namen im Format
	 * "Vorname Nachname".
	 *
	 * @param count Anzahl der Beispieldaten.
	 * @return Ein Array mit den Beispieldaten.
	 */
	public static String[] fakeFullnames( int count ) {
		return randomSample("users", count, ( line ) -> {
			String[] parts = line.split(",");
			return parts[0] + " " + parts[1];
		}, String.class);
	}


	/**
	 * Erzeugt ein Array mit den angegebenen Anzahl zufälliger Nutzernamen.
	 *
	 * @param count Anzahl der Beispieldaten.
	 * @return Ein Array mit den Beispieldaten.
	 */
	public static String[] fakeUsernames( int count ) {
		return randomSample("users", count, ( line ) -> line.split(",")[3], String.class);
	}

	/**
	 * Erzeugt ein Array mit den angegebenen Anzahl zufälliger Passwörter.
	 *
	 * @param count Anzahl der Beispieldaten.
	 * @return Ein Array mit den Beispieldaten.
	 */
	public static String[] fakePasswords( int count ) {
		return randomSample("users", count, ( line ) -> line.split(",")[4], String.class);
	}

	/**
	 * Erzeugt ein Array mit den angegebenen Anzahl zufälliger E-Mail-Adressen.
	 *
	 * @param count Anzahl der Beispieldaten.
	 * @return Ein Array mit den Beispieldaten.
	 */
	public static String[] fakeEmails( int count ) {
		return randomSample("users", count, ( line ) -> line.split(",")[5], String.class);
	}

	/**
	 * Erzeugt ein Array mit den angegebenen Anzahl zufälliger deutscher
	 * Wörter.
	 *
	 * @param count Anzahl der Beispieldaten.
	 * @return Ein Array mit den Beispieldaten.
	 */
	public static String[] fakeStrings( int count ) {
		return randomSample("words", count, ( line ) -> line, String.class);
	}

	/**
	 * Erzeugt ein Array mit den angegebenen Anzahl zufälliger
	 * {@code LocalDate}-Objekte, die ein Datum ohne Uhrzeit beschreiben.
	 *
	 * @param count Anzahl der Beispieldaten.
	 * @return Ein Array mit den Beispieldaten.
	 */
	public static LocalDate[] fakeDates( int count ) {
		long nowEpoch = LocalDate.now().toEpochDay();
		long from = LocalDate.ofEpochDay(nowEpoch - 18 * 365).toEpochDay();
		long to = LocalDate.ofEpochDay(nowEpoch - 14 * 365).toEpochDay();

		LocalDate[] result = new LocalDate[count];
		for( int i = 0; i < count; i++ ) {
			result[i] = LocalDate.ofEpochDay((int) Constants.interpolate(from, to, random()));
		}

		return result;
	}

	/**
	 * Erzeugt ein Array mit den angegebenen Anzahl zufälliger
	 * {@code LocalDateTime}-Objekte, die einen Zeitpunkt mit Dateum und Uhrzeit
	 * beschreiben,
	 *
	 * @param count Anzahl der Beispieldaten.
	 * @return Ein Array mit den Beispieldaten.
	 */
	public static LocalDateTime[] fakeDatetimes( int count ) {
		long nowEpoch = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
		long from = LocalDateTime.ofEpochSecond(nowEpoch - 18 * 365, 0, ZoneOffset.UTC).toEpochSecond(ZoneOffset.UTC);
		long to = LocalDateTime.ofEpochSecond(nowEpoch - 14 * 365, 0, ZoneOffset.UTC).toEpochSecond(ZoneOffset.UTC);

		LocalDateTime[] result = new LocalDateTime[count];
		for( int i = 0; i < count; i++ ) {
			result[i] = LocalDateTime.ofEpochSecond((int) Constants.interpolate(from, to, random()), 0, ZoneOffset.UTC);
		}

		return result;
	}

	/**
	 * Erzeugt einen Blindtext mit der angegebenen Anzahl Worten, aufgeteilt in
	 * die angegebene Anzahl Absätze.
	 * <p>
	 * Abssätze werden duch einen doppelten Zeilenumbruch {@code \n\n}
	 * getrennt.
	 *
	 * @param words Anzahl Wörter im Text insgesamt.
	 * @param paragraphs Anzahl Absätze.
	 * @return Ein zufälliger Blindtext.
	 */
	public static String fakeText( int words, int paragraphs ) {
		String basetext = "";
		try(
			InputStream in = Faker.class.getResourceAsStream("mock-text.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in))
		) {
			basetext = reader.readLine();
		} catch( IOException ex ) {
			LOG.error(ex, "Error generating fake blindtext: " + ex.getMessage());
		}

		String result = basetext.repeat(words / 283);

		int w = (words % 283);
		if( w > 0 ) {
			Matcher m = Pattern.compile("([a-zA-Z_0-9\\u00C0-\\u017F]+\\W+){" + w + "}").matcher(basetext);
			if( m.find() ) {
				result += m.group().stripTrailing();
			}
		}

		if( paragraphs > 1 ) {
			int half = words / paragraphs;
			final int maxLength = result.length();
			Matcher m = Pattern.compile("([a-zA-Z_0-9\\u00C0-\\u017F]+\\W+){" + (half - 10) + "," + (half + 10) + "}([a-zA-Z_0-9\\u00C0-\\u017F]+)\\.").matcher(result);
			if( m.find() ) {
				result = m.replaceAll(( mr ) -> mr.end() == maxLength ? mr.group().trim() : mr.group().trim() + "\n\n");
			}
		}

		return result;
	}

	/**
	 * Erzeugt ein Array mit der angegebenen Anzahl zufällig erzeugter Integer
	 * im angegebenen Bereich.
	 *
	 * @param count Anzahl der Zahlen im Array.
	 * @param min Untere Grenze der Zufallszahlen.
	 * @param max Obere Grenze der Zufallszahlen.
	 * @return Ein Array mit Zufallszahlen.
	 * @see Constants#random(int, int)
	 */
	public static int[] fakeIntArray( int count, int min, int max ) {
		int[] arr = new int[count];
		for( int i = 0; i < count; i++ ) {
			arr[i] = random(min, max);
		}
		return arr;
	}

	/**
	 * Erzeugt eine Liste mit der angegebenen Anzahl zufällig erzeugter Integer
	 * im angegebenen Bereich.
	 * <p>
	 * Ist {@code list} ein Listenobjekt, werden dei Zahlen an diese angehängt.
	 * Wird {@code null} übergeben, wird eine neue {@link ArrayList} erzeugt.
	 *
	 * @param count Anzahl der erzeugten Zahlen.
	 * @param min Untere Grenze der Zufallszahlen.
	 * @param max Obere Grenze der Zufallszahlen.
	 * @param list Eine Liste, die befüllt werden soll, oder {@code null}.
	 * @return Ein Array mit Zufallszahlen.
	 * @see Constants#random(int, int)
	 */
	public static List<Integer> fakeIntegerList( int count, int min, int max, List<Integer> list ) {
		List<Integer> result = (list == null) ? new ArrayList<>(count) : list;
		fakeIntegers(count, min, max, result::add);
		return result;
	}

	/**
	 * Erzeugt die angegebene Anzahl Zufallszahlen im angegebenen Bereich und
	 * übergibt sie an den angegebenen {@code Consumer}.
	 *
	 * Ein typischer Aufruf, um eine {@code #LinkedList} mit 100 Zufallszahlen
	 * zu erzeugen könnte so aussehen:
	 * <pre><code>
	 * List&lt;Integer&gt; l = new LinkedList&lt;&gt;();
	 * Faker.fakeIntegers(100, 0, 100, l::add);
	 * </code></pre>
	 *
	 * @param count Anzahl der erzeugten Zahlen.
	 * @param min Untere Grenze der Zufallszahlen.
	 * @param max Obere Grenze der Zufallszahlen.
	 * @param con {@code Consumer} für die Zahlen.
	 */
	public static void fakeIntegers( int count, int min, int max, Consumer<Integer> con ) {
		for( int i = 0; i < count; i++ ) {
			con.accept(random(min, max));
		}
	}

	public static <L> L fakeIntegers( int count, int min, int max, Supplier<L> sup, BiConsumer<L, Integer> con ) {
		L result = sup.get();

		for( int i = 0; i < count; i++ ) {
			con.accept(result, random(min, max));
		}

		return result;
	}

	@SuppressWarnings( "unchecked" )
	private static <T> T[] randomSample( String filename, int count, Function<String, T> transformer, Class<T> type ) {
		T[] result = (T[]) Array.newInstance(type, count);

		int i = 0;
		double k = count; // cast to double

		double W = exp(log(random()) / k);

		try(
			InputStream in = Faker.class.getResourceAsStream("mock-" + filename + ".csv");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in))
		) {
			String line;
			while( (line = reader.readLine()) != null ) {
				if( i < count ) {
					result[i] = transformer.apply(line);
					i += 1;
				} else {
					int j = (int) (log(random()) / log(1 - W)) + 1;
					while( j > 0 ) {
						line = reader.readLine();
						j -= 1;
					}

					if( line != null ) {
						result[random(0, count - 1)] = transformer.apply(line);
						i += 1;
					} else {
						break;
					}

					W *= exp(log(random()) / k);
				}
			}

			// Fill remaining array
			while( i < count ) {
				result[i] = result[random(0, i - 1)];
				i += 1;
			}
		} catch( IOException ex ) {
			LOG.error(ex, "Error loading mock data file: " + ex.getMessage());
		}

		return result;
	}


	/**
	 * Erzeugt ein Platzhalter-Bild in der angegebenen Größe.
	 * <p>
	 * Das Bild ist ein aus dem Internet geladenes, zufälliges Motiv, dass unter
	 * einer freien Lizenz (Creative Commons) steht.
	 *
	 * @param width Breite des Bildes.
	 * @param height Höhe des Bildes.
	 * @return Ein zufälliges Bild in der angegebenen Größe.
	 */
	public static BufferedImage fakeImage( int width, int height ) {
		return fakeImage(width, height, true);
	}

	/**
	 * Erzeugt ein Platzhalter-Bild in der angegebenen Größe.
	 * <p>
	 * Wenn {@code fromWeb} auf {@code true} gesetzt ist, wird ein zufälliges
	 * Motiv, das unter einer freien Lizenz (Creative Commons) steht, geladen.
	 * Bei {@code false} wird das Bild lokal generiert.
	 *
	 * @param width Breite des Bildes.
	 * @param height Höhe des Bildes.
	 * @param fromWeb Bei {@code true} wird das Bild aus dem Internet geladen,
	 * 	bei {@code false} wird das Bild lokal erzeugt.
	 * @return Ein zufälliges Bild in der angegebenen Größe.
	 */
	public static BufferedImage fakeImage( int width, int height, boolean fromWeb ) {
		if( !fromWeb ) {
			BufferedImage img = ImageLoader.createImage(width, height);
			Graphics2D graphics = (Graphics2D) img.getGraphics().create();

			String text = width + " x " + height;

			Color clr = Constants.randomNiceColor();

			graphics.setBackground(clr.getJavaColor());
			graphics.clearRect(0, 0, width, height);

			graphics.setColor(clr.textcolor().getJavaColor());
			graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, (int) ((width + height) * 0.05)));
			FontMetrics fontMerics = graphics.getFontMetrics();
			LineMetrics lineMetrics = fontMerics.getLineMetrics(text, graphics);
			graphics.drawString(text,
				(width - fontMerics.stringWidth(text)) / 2,
				(int) (height / 2 - lineMetrics.getDescent() + lineMetrics.getAscent() / 2)
			);
			graphics.dispose();

			return img;
		} else {
			return ImageLoader.loadImage(String.format(FAKE_IMG_URL, width, height), false);
		}
	}

	private Faker() {
	}

	private static final Log LOG = Log.getLogger(Faker.class);

}
