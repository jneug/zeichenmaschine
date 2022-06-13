package schule.ngb.zm;

import schule.ngb.zm.util.ImageLoader;

import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Constants {

	/**
	 * Name der Zeichenmaschine.
	 */
	public static final String APP_NAME = "Zeichenmaschine";

	/**
	 * Hauptversion der Zeichenmaschine.
	 */
	public static final int APP_VERSION_MAJ = 0;

	/**
	 * Unterversion der Zeichenmaschine.
	 */
	public static final int APP_VERSION_MIN = 0;

	/**
	 * Patchversion der Zeichenmaschine.
	 */
	public static final int APP_VERSION_REV = 12;

	/**
	 * Version der Zeichenmaschine als Text-String.
	 */
	public static final String APP_VERSION = APP_VERSION_MAJ + "." + APP_VERSION_MIN + "." + APP_VERSION_REV;


	/**
	 * Standardbreite eines Zeichenfensters.
	 */
	public static final int STD_WIDTH = 400;

	/**
	 * Standardhöhe eines Zeichenfensters.
	 */
	public static final int STD_HEIGHT = 400;

	/**
	 * Standardwert für die Frames pro Sekunde einer Zeichenmaschine.
	 */
	public static final int STD_FPS = 60;

	/**
	 * Standardfarbe der Füllungen.
	 */
	public static final Color STD_FILLCOLOR = Color.WHITE;

	/**
	 * Standardfarbe der Konturen.
	 */
	public static final Color STD_STROKECOLOR = Color.BLACK;

	/**
	 * Standardwert für die Dicke der Konturen.
	 */
	public static final double STD_STROKEWEIGHT = 1.0;

	/**
	 * Standardwert für die Schriftgröße.
	 */
	public static final int STD_FONTSIZE = 14;

	public static final Options.StrokeType SOLID = Options.StrokeType.SOLID;

	public static final Options.StrokeType DASHED = Options.StrokeType.DASHED;

	public static final Options.StrokeType DOTTED = Options.StrokeType.DOTTED;

	public static final Options.ArrowHead LINES = Options.ArrowHead.LINES;

	public static final Options.ArrowHead FILLED = Options.ArrowHead.FILLED;

	public static final Options.PathType OPEN = Options.PathType.OPEN;

	public static final Options.PathType CLOSED = Options.PathType.CLOSED;

	public static final Options.PathType PIE = Options.PathType.PIE;

	public static final Options.Direction CENTER = Options.Direction.CENTER;

	public static final Options.Direction NORTH = Options.Direction.NORTH;

	public static final Options.Direction EAST = Options.Direction.EAST;

	public static final Options.Direction SOUTH = Options.Direction.SOUTH;

	public static final Options.Direction WEST = Options.Direction.WEST;

	public static final Options.Direction NORTHEAST = Options.Direction.NORTHEAST;

	public static final Options.Direction SOUTHEAST = Options.Direction.SOUTHEAST;

	public static final Options.Direction NORTHWEST = Options.Direction.NORTHWEST;

	public static final Options.Direction SOUTHWEST = Options.Direction.SOUTHWEST;

	public static final Options.Direction MIDDLE = Options.Direction.MIDDLE;

	public static final Options.Direction UP = Options.Direction.UP;

	public static final Options.Direction RIGHT = Options.Direction.RIGHT;

	public static final Options.Direction DOWN = Options.Direction.DOWN;

	public static final Options.Direction LEFT = Options.Direction.LEFT;

	/**
	 * Schwarz
	 */
	public static final Color BLACK = Color.BLACK;

	/**
	 * Weiß
	 */
	public static final Color WHITE = Color.WHITE;

	/**
	 * Grau
	 */
	public static final Color GRAY = Color.GRAY;

	/**
	 * Dunkelgrau
	 */
	public static final Color DARKGRAY = Color.DARKGRAY;

	/**
	 * Hellgrau
	 */
	public static final Color LIGHTGRAY = Color.LIGHTGRAY;

	/**
	 * Rot
	 */
	public static final Color RED = Color.RED;

	/**
	 * Blau
	 */
	public static final Color BLUE = Color.BLUE;

	/**
	 * Grün
	 */
	public static final Color GREEN = Color.GREEN;

	/**
	 * Gelb
	 */
	public static final Color YELLOW = Color.YELLOW;

	/**
	 * Orange
	 */
	public static final Color ORANGE = Color.ORANGE;

	/**
	 * Türkis
	 */
	public static final Color CYAN = Color.CYAN;

	/**
	 * Magenta
	 */
	public static final Color MAGENTA = Color.MAGENTA;

	/**
	 * Pink
	 */
	public static final Color PINK = Color.PINK;

	/**
	 * Lila
	 */
	public static final Color PURPLE = Color.PURPLE;

	/**
	 * Braun
	 */
	public static final Color BROWN = Color.BROWN;

	/**
	 * Standardfarbe für den Hintergrund.
	 */
	public static final Color STD_BACKGROUND = new Color(200, 200, 200);

	/**
	 * Konstante zur Prüfung, ob ein Mausknopf gedrückt wurde.
	 */
	public static final int NOBUTTON = MouseEvent.NOBUTTON;

	/**
	 * Konstante zur Prüfung, ob Mausknopf 1 (links) gedrückt wurde.
	 */
	public static final int BUTTON1 = MouseEvent.BUTTON1;

	/**
	 * Konstante zur Prüfung, ob Mausknopf 2 (rechts) gedrückt wurde.
	 */
	public static final int BUTTON2 = MouseEvent.BUTTON2;

	/**
	 * Konstante zur Prüfung, ob Mausknopf 3 (mittig) gedrückt wurde.
	 */
	public static final int BUTTON3 = MouseEvent.BUTTON3;

	/**
	 * Konstante für die Kreiszahl Pi.
	 */
	public static final double PI = Math.PI;

	/**
	 * Konstante für die Hälfte der Kreiszahl Pi.
	 */
	public static final double HALF_PI = Math.PI / 2.0;

	/**
	 * Konstante für ein Viertel der Kreiszahl Pi.
	 */
	public static final double QUARTER_PI = Math.PI / 4.0;


	/**
	 * Globale Variablen, die von allen Klassen genutzt werden dürfen. Änderungen
	 * wirken sich auf die aktuelle Zeichenmaschine aus und sollten nur von der
	 * Zeichenmaschine selbst vorgenommen werden.
	 */

	/**
	 * Anzahl der Ticks (Frames), die das Programm bisher läuft.
	 */
	public static int tick = 0;

	/**
	 * Die Zeit in Millisekunden, die das Programm seit seinem Start läuft.
	 */
	public static long runtime = 0L;

	/**
	 * Der Zeitunterschied zum letzten Frame in Sekunden.
	 */
	public static double delta = 0.0;

	/**
	 * Die aktuelle {@code x}-Koordinate der Maus.
	 */
	public static double mouseX = 0.0;

	/**
	 * Die aktuelle {@code y}-Koordinate der Maus.
	 */
	public static double mouseY = 0.0;

	/**
	 * Die letzte {@code x}-Koordinate der Maus (wird einmal pro Frame
	 * aktualisiert).
	 */
	public static double pmouseX = 0.0;

	/**
	 * Die letzte {@code y}-Koordinate der Maus (wird einmal pro Frame
	 * aktualisiert).
	 */
	public static double pmouseY = 0.0;

	/**
	 * Gibt an, ob ein Mausknopf derzeit gedrückt ist.
	 */
	public static boolean mousePressed = false;

	/**
	 * Der aktuell gedrückte Mausknopf. Die Mausknöpfe werden durch die
	 * Konstanten {@link #NOBUTTON}, {@link #BUTTON1}, {@link #BUTTON2} und
	 * {@link #BUTTON3} angegeben. (Sie stimmen mit den Konstanten in
	 * {@link MouseEvent} überein.
	 *
	 * @see MouseEvent
	 */
	public static int mouseButton = NOBUTTON;

	/**
	 * Das zuletzt ausgelöste {@code MouseEvent}.
	 */
	public static MouseEvent mouseEvent;

	// Mauszeiger
	public static final int ARROW = Cursor.DEFAULT_CURSOR;

	public static final int CROSS = Cursor.CROSSHAIR_CURSOR;

	public static final int HAND = Cursor.HAND_CURSOR;

	public static final int MOVE = Cursor.MOVE_CURSOR;

	public static final int TEXT = Cursor.TEXT_CURSOR;

	public static final int WAIT = Cursor.WAIT_CURSOR;

	/**
	 * Gibt an, ob derzeit eine Taste gedrückt ist.
	 */
	public static boolean keyPressed = false;

	/**
	 * Das Zeichen der zuletzt gedrückten Taste.
	 */
	public static char key = ' ';

	/**
	 * Der Tastencode der zuletzt gedrückten Taste. Die Keycodes können in der
	 * Klasse {@link KeyEvent} nachgesehen werden. (Zum Beispiel
	 * {@link KeyEvent#VK_ENTER}.)
	 */
	public static int keyCode = 0;

	/**
	 * Das zuletzt ausgelöste {@link KeyEvent}.
	 */
	public static KeyEvent keyEvent;

	/**
	 * Die Höhe der Zeichenleinwand.
	 */
	public static int width;

	/**
	 * Die Breite der Zeichenleinwand.
	 */
	public static int height;

	/**
	 * Die Breite des Bildschirms, auf dem das Zeichenfenster geöffnet wurde.
	 * <p>
	 * Beachte, dass sich die Breite nicht anpasst, wenn das Zeichenfenster auf
	 * einen anderen Bildschirm verschoben wird.
	 */
	public static int screenWidth;

	/**
	 * Die Höhe des Bildschirms, auf dem das Zeichenfenster geöffnet wurde.
	 * <p>
	 * Beachte, dass sich die Höhe nicht anpasst, wenn das Zeichenfenster auf
	 * einen anderen Bildschirm verschoben wird.
	 */
	public static int screenHeight;


	// Farben

	/**
	 * Erstellt eine graue Farbe. Der Parameter {@code gray} gibt einen
	 * Grauwert zwischen <code>0</code> und <code>255</code> an, wobei
	 * <code>0</code> schwarz und <code>255</code> weiß ist.
	 *
	 * @param gray Grauwert zwischen <code>0</code> und <code>255</code>.
	 * @return Ein passendes Farbobjekt.
	 */
	public static final Color color( int gray ) {
		return color(gray, gray, gray, 255);
	}

	/**
	 * Erstellt eine graue Farbe. Der Parameter {@code gray} gibt einen
	 * Grauwert zwischen <code>0</code> und <code>255</code> an, wobei
	 * <code>0</code> schwarz und <code>255</code> weiß ist.
	 * {@code alpha} gibt den den Transparentwert an (auch zwischen
	 * <code>0</code> und <code>255</code>), wobei
	 * <code>0</code> komplett durchsichtig ist und <code>255</code> komplett
	 * deckend.
	 *
	 * @param gray Grauwert zwischen <code>0</code> und <code>255</code>.
	 * @param alpha Transparentwert zwischen <code>0</code> und
	 * 	<code>255</code>.
	 * @return Ein passendes Farbobjekt.
	 */
	public static final Color color( int gray, int alpha ) {
		return color(gray, gray, gray, alpha);
	}

	/**
	 * Erstellt eine Farbe. Die Parameter {@code red}, {@code green} und
	 * {@code blue} geben die Rot-, Grün- und Blauanteile der Farbe. Die
	 * Werte liegen zwischen <code>0</code> und <code>255</code>.
	 *
	 * @param red Rotwert zwischen <code>0</code> und <code>255</code>.
	 * @param green Grünwert zwischen <code>0</code> und <code>255</code>.
	 * @param blue Blauwert zwischen <code>0</code> und <code>255</code>.
	 * @return Ein passendes Farbobjekt.
	 */
	public static final Color color( int red, int green, int blue ) {
		return color(red, green, blue, 255);
	}

	/**
	 * Erstellt eine Farbe. Die Parameter {@code red}, {@code green} und
	 * {@code blue} geben die Rot-, Grün- und Blauanteile der Farbe. Die
	 * Werte liegen zwischen <code>0</code> und <code>255</code>.
	 * {@code alpha} gibt den den Transparentwert an (auch zwischen
	 * code>0</code> und <code>255</code>), wobei
	 * <code>0</code> komplett durchsichtig ist und <code>255</code> komplett
	 * deckend.
	 *
	 * @param red Rotwert zwischen <code>0</code> und <code>255</code>.
	 * @param green Grünwert zwischen <code>0</code> und <code>255</code>.
	 * @param blue Blauwert zwischen <code>0</code> und <code>255</code>.
	 * @param alpha Transparenzwert zwischen <code>0</code> und
	 * 	<code>255</code>.
	 * @return Ein passendes Farbobjekt.
	 */
	public static final Color color( int red, int green, int blue, int alpha ) {
		return new Color(red % 256, green % 256, blue % 256, alpha % 256);
	}

	/**
	 * Erzeugt eine zufällige Farbe.
	 * <p>
	 * Alle Farbkomponenten (Rot, Grün und Blau) werden zufällig im Bereich 0
	 * bis 255 gewählt.
	 *
	 * @return Ein zufälliges Farbobjekt.
	 */
	public static final Color randomColor() {
		return color(random(10, 255), random(10, 255), random(10, 255), 255);
	}

	/**
	 * Erzeugt eine "hübsche" zufällige Farbe. Die Farbe wird so gewählt, dass
	 * die Farben nicht zu verwaschen oder dunkel wirken.
	 *
	 * @return Ein zufälliges Farbobjekt.
	 */
	public static final Color randomNiceColor() {
		//return colorHsb(random(), 0.9, 0.75);
		return Color.getHSLColor(random(0.0, 360.0), random(0.75, 0.9), random(0.4, 0.6));
		//return Color.getHSBColor(random(0.0,360.0), random(0.75,0.9), 0.75);
	}

	/**
	 * Erstellt eine Farbe im HSB-Farbraum.
	 * <p>
	 * Als Parameter wird der Farbton (hue) im Bereich 0.0 bis 360.0 angegeben
	 * und die Sättigung (saturation) und Hellwert (brightness) im Bereich 0 bis
	 * 100.
	 *
	 * @param h Farbton im Bereich 0 bis 360.
	 * @param s Sättigung im Bereich 0 bis 100.
	 * @param b Hellwert im Bereich 0 bis 100.
	 * @return Ein Farbobjekt zu den angegebenen Werten.
	 * @see <a
	 * 	href="https://de.wikipedia.org/wiki/HSB-Farbraum">HSB-Farbraum</a>
	 */
	public static final Color colorHsb( double h, double s, double b ) {
		return Color.getHSBColor(h % 360.0, s / 100.0, b / 100.0);
	}

	/**
	 * Erstellt eine Farbe im HSL-Farbraum.
	 * <p>
	 * Als Parameter wird der Farbton (hue) im Bereich 0.0 bis 360.0 angegeben
	 * und die Sättigung (saturation) und Helligkeit (lightness) im Bereich 0
	 * bis 100.
	 *
	 * @param h Farbton im Bereich 0 bis 360.
	 * @param s Sättigung im Bereich 0 bis 100.
	 * @param l Helligkeit im Bereich 0 bis 100.
	 * @return Ein Farbobjekt zu den angegebenen Werten.
	 * @see <a
	 * 	href="https://de.wikipedia.org/wiki/HSL-Farbraum">HSL-Farbraum</a>
	 */
	public static final Color colorHsl( double h, double s, double l ) {
		return Color.getHSLColor(h % 360.0, s / 100.0, l / 100.0);
	}

	public static final BufferedImage loadImage( String name ) {
		return ImageLoader.loadImage(name);
	}

	// Mathematische Funktionen

	/**
	 * Berechnet das Minimum aller übergebenen Werte.
	 *
	 * @param numbers Die Werte, aus denen das Minimum ermittelt werden soll.
	 * @return Das Minimum der Werte.
	 * @throws IllegalArgumentException Wenn die Eingabe {@code null} oder leer
	 *                                  ist.
	 */
	public static final double min( double... numbers ) {
		if( numbers == null || numbers.length == 0 ) {
			throw new IllegalArgumentException("Array may not be <null> or empty.");
		}

		double min = numbers[0];
		for( int i = 1; i < numbers.length; i++ ) {
			min = Math.min(min, numbers[i]);
		}
		return min;
	}

	/**
	 * Berechnet das Maximum aller übergebenen Werte.
	 *
	 * @param numbers Die Werte, aus denen das Maximum ermittelt werden soll.
	 * @return Das Maximum der Werte.
	 * @throws IllegalArgumentException Wenn die Eingabe {@code null} oder leer
	 *                                  ist.
	 */
	public static final double max( double... numbers ) {
		if( numbers == null || numbers.length == 0 ) {
			throw new IllegalArgumentException("Array may not be <null> or empty.");
		}

		double max = numbers[0];
		for( int i = 1; i < numbers.length; i++ ) {
			max = Math.max(max, numbers[i]);
		}
		return max;
	}

	public static final double sum( double... numbers ) {
		if( numbers == null ) {
			throw new IllegalArgumentException("Array may not be <null> or empty.");
		}

		double sum = 0;
		for( int i = 0; i < numbers.length; i++ ) {
			sum += numbers[i];
		}
		return sum;
	}

	public static final double avg( double... numbers ) {
		if( numbers == null || numbers.length == 0 ) {
			throw new IllegalArgumentException("Array may not be <null> or empty.");
		}

		return sum(numbers) / (double) numbers.length;
	}

	/**
	 * Ermittelt den Absolutbetrag der Zahl {@code x}.
	 *
	 * @param x Eine Zahl.
	 * @return Der Absolutbetrag.
	 */
	public static final double abs( double x ) {
		return Math.abs(x);
	}

	/**
	 * Ermittelt das Vorzeichen der Zahl {@code x}.
	 *
	 * @param x Eine Zahl.
	 * @return <code>-1</code>, <code>1</code> oder <code>0</code>.
	 */
	public static final double sign( double x ) {
		return Math.signum(x);
	}

	/**
	 * Rundet die Zahl {@code x}.
	 *
	 * @param x Eine Zahl.
	 * @return Die gerundete Zahl.
	 */
	public static final double round( double x ) {
		return Math.round(x);
	}

	/**
	 * Rundet die Zahl {@code x} ab.
	 *
	 * @param x Eine Zahl.
	 * @return Die abgerundete Zahl.
	 */
	public static final double floor( double x ) {
		return Math.floor(x);
	}

	/**
	 * Rundet die Zahl {@code x} auf.
	 *
	 * @param x Eine Zahl.
	 * @return Die aufgerundete Zahl.
	 */
	public static final double ceil( double x ) {
		return Math.ceil(x);
	}

	/**
	 * Ermittelt die Quadratwurzel der Zahl {@code x}.
	 *
	 * @param x Eine Zahl.
	 * @return Die Quadratwurzel.
	 */
	public static final double sqrt( double x ) {
		return Math.sqrt(x);
	}

	/**
	 * Ermittelt die Potenz der Zahl {@code x} zum Exponenten {@code p}.
	 *
	 * @param x Eine Zahl.
	 * @param p Der Exponent.
	 * @return {@code x} hoch {@code p}.
	 */
	public static final double pow( double x, double p ) {
		return Math.pow(x, p);
	}

	/**
	 * Rechnet von Grad in Radian um.
	 *
	 * @param angle Ein Winkel in Grad.
	 * @return Der Winkel in Radian.
	 */
	public static final double radians( double angle ) {
		return Math.toRadians(angle);
	}

	/**
	 * Rechent von Radian in Grad um.
	 *
	 * @param radians Der Winkel in Radian.
	 * @return Der Winkel in Grad.
	 */
	public static final double degrees( double radians ) {
		return Math.toDegrees(radians);
	}

	/**
	 * Ermittelt den Sinus der Zahl {@code x}.
	 *
	 * @param x Eine Zahl.
	 * @return <code>sin(x)</code>.
	 */
	public static final double sin( double x ) {
		return Math.sin(x);
	}

	/**
	 * Ermittelt den Kosinus der Zahl {@code x}.
	 *
	 * @param x Eine Zahl.
	 * @return <code>cos(x)</code>.
	 */
	public static final double cos( double x ) {
		return Math.cos(x);
	}

	/**
	 * Ermittelt den Tangens der Zahl {@code x}.
	 *
	 * @param x Eine Zahl.
	 * @return <code>tan(x)</code>.
	 */
	public static final double tan( double x ) {
		return Math.tan(x);
	}

	/**
	 * Ermittelt den Arkussinus der Zahl {@code x}.
	 *
	 * @param x Eine Zahl.
	 * @return <code>asin(x)</code>.
	 */
	public static final double arcsin( double x ) {
		return Math.asin(x);
	}

	/**
	 * Ermittelt den Arkuskosinus der Zahl {@code x}.
	 *
	 * @param x Eine Zahl.
	 * @return <code>acos(x)</code>.
	 */
	public static final double arccos( double x ) {
		return Math.acos(x);
	}

	/**
	 * Ermittelt den Arkusktangens der Zahl {@code x}.
	 *
	 * @param x Eine Zahl.
	 * @return <code>atan(x)</code>.
	 */
	public static final double arctan( double x ) {
		return Math.atan(x);
	}

	/**
	 * Beschränkt die Zahl {@code x} auf das Intervall <code>[min,
	 * max]</code>. Liegt {@code x} außerhalb des Intervalls, wird eine der
	 * Grenzen zurückgegeben.
	 *
	 * @param x Eine Zahl.
	 * @param max Das Maximum des Intervalls.
	 * @return Eine Zahl im Intervall <code>[min, max]</code>.
	 */
	public static final double limit( double x, double min, double max ) {
		if( x > max ) {
			return max;
		}
		if( x < min ) {
			return min;
		}
		return x;
	}

	/**
	 * Beschränkt die Zahl {@code x} auf das Intervall <code>[min,
	 * max]</code>. Liegt {@code x} außerhalb des Intervalls, wird eine der
	 * Grenzen zurückgegeben.
	 *
	 * @param x Eine Zahl.
	 * @param max Das Maximum des Intervalls.
	 * @return Eine Zahl im Intervall <code>[min, max]</code>.
	 */
	public static final int limit( int x, int min, int max ) {
		if( x > max ) {
			return max;
		}
		if( x < min ) {
			return min;
		}
		return x;
	}

	/**
	 * Interpoliert einen Wert zwischen {@code from} und {@code to}
	 * linear, abhängig von {@code t}. Das Ergebnis entspricht der Formel
	 *
	 * <pre>
	 * from - t * (from + to)
	 * </pre>
	 *
	 * @param from Startwert
	 * @param to Zielwert
	 * @param t Wert zwischen <code>0</code> und <code>1</code>.
	 * @return Das Ergebnis der linearen Interpolation.
	 */
	public static final double interpolate( double from, double to, double t ) {
		return from + t * (to - from);
	}

	public static final double map( double value, double fromMin, double fromMax, double toMin, double toMax ) {
		return interpolate(toMin, toMax, (value - fromMin) / (fromMax - fromMin));
	}

	/**
	 * Shared Random instance
	 */
	private static Random random = null;

	private static final Random getRandom() {
		if( random == null ) {
			random = new Random();
		}
		return random;
	}

	/**
	 * Setzt den Seed für den internen Zufallsgenerator.
	 * <p>
	 * Muss vor der ersten Nutzung einer der {@code random} Methoden aufgerufen
	 * werden, da sonst der Zufallsgenerator schon initialisiert wurde.
	 *
	 * @param seed Ein Wert zur Initialisierung des Zufallsgenerators.
	 */
	public final static void setSeed( long seed ) {
		if( random == null ) {
			random = new Random(seed);
		}
	}

	/**
	 * Erzeugt eine Pseudozufallszahl im Intervall zwischen 0 und 1.
	 *
	 * @return Eine Zufallszahl.
	 */
	public static final double random() {
		return getRandom().nextDouble();
	}

	/**
	 * Erzeugt eine Pseudozufallszahl zwischen 0 und max.
	 *
	 * @param max Obere Grenze.
	 * @return Eine Zufallszahl.
	 */
	public static final double random( double max ) {
		return random(0.0, max);
	}

	/**
	 * Erzeugt eine Pseudozufallszahl zwischen {@code min} und {@code max}.
	 *
	 * @param min Untere Grenze.
	 * @param max Obere Grenze.
	 * @return Eine Zufallszahl.
	 */
	public static final double random( double min, double max ) {
		return random() * (max - min) + min;
	}

	/**
	 * Erzeugt eine ganze Pseudozufallszahl zwischen {@code 0} und {@code max}.
	 *
	 * @param max Obere Grenze.
	 * @return Eine Zufallszahl.
	 */
	public static final int random( int max ) {
		return random(0, max);
	}

	/**
	 * Erzeugt eine ganze Pseudozufallsganzzahl zwischen {@code min} und
	 * {@code max}.
	 *
	 * @param min Untere Grenze.
	 * @param max Obere Grenze.
	 * @return Eine Zufallszahl.
	 */
	public static final int random( int min, int max ) {
		max += 1;
		if( min < 0 ) {
			min -= 1;
		}
		return (int) (random() * (max - min) + min);
	}

	/**
	 * Erzeugt einen zufälligen Wahrheitswert. {@code true} und {@code false}
	 * werden mit einer Wahrscheinlichkeit von 50% erzeugt.
	 *
	 * @return Ein Wahrheitswert.
	 */
	public static final boolean randomBool() {
		return randomBool(.5);
	}

	/**
	 * Erzeugt einen zufälligen Wahrheitswert. {@code true} wird mit der
	 * Wahrscheinlichkeit {@code percent} Prozent erzeugt.
	 *
	 * @param percent Eine Prozentzahl zwischen <code>0</code> und
	 * 	<code>100</code>.
	 * @return Ein Wahrheitswert.
	 */
	public static final boolean randomBool( int percent ) {
		return randomBool(percent / 100.0);
	}

	/**
	 * Erzeugt einen zufälligen Wahrheitswert. {@code true} wird mit der
	 * Wahrscheinlichkeit {@code weight} erzeugt.
	 *
	 * @return Ein Wahrheitswert.
	 */
	public static final boolean randomBool( double weight ) {
		return random() < weight;
	}

	/**
	 * Erzeugt eine Pseudozufallszahl nach einer Gaussverteilung.
	 *
	 * @return Eine Zufallszahl.
	 * @see Random#nextGaussian()
	 */
	public static final double randomGaussian() {
		return getRandom().nextGaussian();
	}

	/**
	 * Wählt ein zufälliges Element aus dem Array aus.
	 *
	 * @param values Ein Array mit Werten, die zur Auswahl stehen.
	 * @return Ein zufälliges Element aus dem Array.
	 */
	public static final <T> T choice( T[] values ) {
		return values[random(0, values.length - 1)];
	}

	/**
	 * Wählt ein zufälliges Element aus dem Array aus.
	 *
	 * @param values Ein Array mit Werten, die zur Auswahl stehen.
	 * @return Ein zufälliges Element aus dem Array.
	 */
	public static final <T> T[] choice( T[] values, int n ) {
		Object[] result = new Object[n];
		for( int i = 0; i < n; i++ ) {
			result[i] = choice(values);
		}
		return (T[]) result;
	}

	/**
	 * Erzeugt den nächsten Wert eines Perlin-Noise.
	 *
	 * @return
	 */
	public static final double noise() {
		// TODO: Implementieren
		return 0.0;
	}

	// Typecasting
	/*
	public static final int i( char value ) {
		return (int) value;
	}

	public static final int i( byte value ) {
		return (int) value;
	}

	public static final int i( short value ) {
		return (int) value;
	}

	public static final int i( long value ) {
		return (int) value;
	}

	public static final int i( double value ) {
		return (int) value;
	}

	public static final int i( float value ) {
		return (int) value;
	}

	public static final int i( int value ) {
		return (int) value;
	}

	public static final int i( boolean value ) {
		return value ? 0 : 1;
	}

	public static final int i( String value ) {
		try {
			return Integer.parseInt(value);
		} catch( NumberFormatException ex ) {
			return 0;
		}
	}

	public static final double d( char value ) {
		return (double) value;
	}

	public static final double d( byte value ) {
		return (double) value;
	}

	public static final double d( short value ) {
		return (double) value;
	}

	public static final double d( long value ) {
		return (double) value;
	}

	public static final double d( double value ) {
		return value;
	}

	public static final double d( float value ) {
		return (double) value;
	}

	public static final double d( int value ) {
		return (double) value;
	}

	public static final double d( boolean value ) {
		return value ? 0.0 : 1.0;
	}

	public static final double d( String value ) {
		try {
			return Double.parseDouble(value);
		} catch( NumberFormatException ex ) {
			return 0.0;
		}
	}

	public static final boolean b( char value ) {
		return value != 0;
	}

	public static final boolean b( byte value ) {
		return value != 0;
	}

	public static final boolean b( short value ) {
		return value != 0;
	}

	public static final boolean b( int value ) {
		return value != 0;
	}

	public static final boolean b( long value ) {
		return value != 0L;
	}

	public static final boolean b( double value ) {
		return value != 0.0;
	}

	public static final boolean b( float value ) {
		return value != 0.0f;
	}

	public static final boolean b( boolean value ) {
		return value;
	}

	public static final boolean b( String value ) {
		return Boolean.parseBoolean(value);
	}
	*/

}
