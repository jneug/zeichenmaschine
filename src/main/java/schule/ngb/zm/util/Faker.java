package schule.ngb.zm.util;

import schule.ngb.zm.Color;
import schule.ngb.zm.Constants;
import schule.ngb.zm.Zeichenmaschine;
import schule.ngb.zm.util.io.FileLoader;
import schule.ngb.zm.util.io.ImageLoader;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;

/**
 * Hilfsklasse, um zufällige Beispieldaten zu erzeugen.
 * <p>
 * Die Klasse kann verschiedene Arten realistischer Beispieldaten erzeugen,
 * unter anderem Namen, E-Mail-Adressen, Passwörter oder Platzhalterbilder.
 */
public final class Faker {

	public static final String FAKE_IMG_URL = "https://loremflickr.com/%d/%d";

	/**
	 * Erzeugt ein Array mit den angegebenen Anzahl zufälliger Nutzernamen.
	 *
	 * @param n Anzahl der Beispieldaten.
	 * @return Ein Array mit den Beispieldaten.
	 */
	public static String[][] fakeUsers( int n ) {
		String[][] data = loadMockfile("users");
		String[][] result = new String[n][];
		for( int i = 0; i < n; i++ ) {
			result[i] = Constants.choice(data);
		}
		return result;
	}

	/**
	 * Erzeugt ein Array mit den angegebenen Anzahl zufälliger Vornamen.
	 *
	 * @param n Anzahl der Beispieldaten.
	 * @return Ein Array mit den Beispieldaten.
	 */
	public static String[] fakeNames( int n ) {
		String[][] data = loadMockfile("users");
		String[] result = new String[n];
		for( int i = 0; i < n; i++ ) {
			String[] row = Constants.choice(data);
			result[i] = row[0];
		}
		return result;
	}


	/**
	 * Erzeugt ein Array mit den angegebenen Anzahl zufälliger Namen (Vor- und
	 * Nachname).
	 *
	 * @param n Anzahl der Beispieldaten.
	 * @return Ein Array mit den Beispieldaten.
	 */
	public static String[] fakeFullnames( int n ) {
		String[][] data = loadMockfile("users");
		String[] result = new String[n];
		for( int i = 0; i < n; i++ ) {
			String[] row = Constants.choice(data);
			result[i] = row[0] + " " + row[1];
		}
		return result;
	}


	/**
	 * Erzeugt ein Array mit den angegebenen Anzahl zufälliger Nutzernamen.
	 *
	 * @param n Anzahl der Beispieldaten.
	 * @return Ein Array mit den Beispieldaten.
	 */
	public static String[] fakeUsernames( int n ) {
		String[][] data = loadMockfile("users");
		String[] result = new String[n];
		for( int i = 0; i < n; i++ ) {
			String[] row = Constants.choice(data);
			result[i] = row[3];
		}
		return result;
	}

	/**
	 * Erzeugt ein Array mit den angegebenen Anzahl zufälliger Passwörter.
	 *
	 * @param n Anzahl der Beispieldaten.
	 * @return Ein Array mit den Beispieldaten.
	 */
	public static String[] fakePasswords( int n ) {
		String[][] data = loadMockfile("users");
		String[] result = new String[n];
		for( int i = 0; i < n; i++ ) {
			String[] row = Constants.choice(data);
			result[i] = row[4];
		}
		return result;
	}

	/**
	 * Erzeugt ein Array mit den angegebenen Anzahl zufälliger E-Mail-Adressen.
	 *
	 * @param n Anzahl der Beispieldaten.
	 * @return Ein Array mit den Beispieldaten.
	 */
	public static String[] fakeEmails( int n ) {
		String[][] data = loadMockfile("users");
		String[] result = new String[n];
		for( int i = 0; i < n; i++ ) {
			String[] row = Constants.choice(data);
			result[i] = row[5];
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
				(int) ((width - fontMerics.stringWidth(text)) / 2),
				(int) (height / 2 - lineMetrics.getDescent() + lineMetrics.getAscent() / 2)
			);
			graphics.dispose();

			return img;
		} else {
			return ImageLoader.loadImage(String.format(FAKE_IMG_URL, width, height), false);
		}
	}

	public static String fakeText( int words, int paragraphs ) {
		return "";
	}

	private static String[][] loadMockfile( String name ) {
		return FileLoader.loadCsv("schule/ngb/zm/util/mock-" + name + ".csv", true);
	}

	private Faker() {
	}

}
