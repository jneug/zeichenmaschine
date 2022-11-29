package schule.ngb.zm;

import schule.ngb.zm.anim.Easing;
import schule.ngb.zm.util.io.ImageLoader;
import schule.ngb.zm.util.Noise;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.function.DoubleUnaryOperator;

/**
 * Basisklasse für die meisten Objekte der Zeichenmaschine, die von Nutzern
 * erweitert werden können.
 * <p>
 * Die Konstanten stellen viele Funktionen zur einfachen Programmierung bereit
 * und enthält auch einige dynamische Werte, die von der Zeichenmaschine laufend
 * aktuell gehalten werden (beispielsweise {@link #runtime}).
 * <p>
 * Für die Implementierung eigener Klassen ist es oft hilfreich von
 * {@code Constants} zu erben, um die Methoden und Konstanten einfach im
 * Programm nutzen zu können.
 * <pre><code>
 * class MyClass extends Constants {
 *     public int summe( int a, int b ) {
 *         // sum ist durch Vererbung verfügbar,
 *         return sum(a, b);
 *     }
 * }
 * </code></pre>
 * <p>
 * Alternativ können die statischen Klassenmethoden auch direkt genutzt werden:
 * <pre><code>
 * Constants.sum(1,2,3,4); // 10
 * </code></pre>
 * <p>
 * Oder die Methoden statisch importiert werden:
 * <pre><code>
 * import static Constants.*;
 *
 * sum(1, 2, 3, 4); // 10
 * </code></pre>
 */
@SuppressWarnings( "unused" )
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
	public static final int APP_VERSION_REV = 32;

	/**
	 * Version der Zeichenmaschine als Text-String.
	 */
	public static final String APP_VERSION = APP_VERSION_MAJ + "." + APP_VERSION_MIN + "." + APP_VERSION_REV;

	/**
	 * Gibt an, ob die Zeichenmaschine unter macOS gestartet wurde.
	 */
	public static final boolean MACOS;

	/**
	 * Gibt an, ob die Zeichenmaschine unter Windows gestartet wurde.
	 */
	public static final boolean WINDOWS;

	/**
	 * Gibt an, ob die Zeichenmaschine unter Linux gestartet wurde.
	 */
	public static final boolean LINUX;

	static {
		final String name = System.getProperty("os.name");

		if( name.contains("Mac") ) {
			MACOS = true;
			WINDOWS = false;
			LINUX = false;
		} else if( name.contains("Windows") ) {
			MACOS = false;
			WINDOWS = true;
			LINUX = false;
		} else if( name.equals("Linux") ) {  // true for the ibm vm
			MACOS = false;
			WINDOWS = false;
			LINUX = true;
		} else {
			MACOS = false;
			WINDOWS = false;
			LINUX = false;
		}
	}

	/**
	 * Standardbreite eines Zeichenfensters.
	 */
	public static final int DEFAULT_WIDTH = 400;

	/**
	 * Standardhöhe eines Zeichenfensters.
	 */
	public static final int DEFAULT_HEIGHT = 400;

	/**
	 * Standardwert für die Frames pro Sekunde einer Zeichenmaschine.
	 */
	public static final int DEFAULT_FPS = 60;

	/**
	 * Standardfarbe der Füllungen.
	 */
	public static Color DEFAULT_FILLCOLOR = Color.WHITE;

	/**
	 * Standardfarbe der Konturen.
	 */
	public static Color DEFAULT_STROKECOLOR = Color.BLACK;

	/**
	 * Standardwert für die Dicke der Konturen.
	 */
	public static double DEFAULT_STROKEWEIGHT = 1.0;

	/**
	 * Standardwert für die Schriftgröße.
	 */
	public static int DEFAULT_FONTSIZE = 14;

	/**
	 * Standardwert für den Abstand von Formen.
	 */
	public static int DEFAULT_BUFFER = 10;

	public static int DEFAULT_ANIM_RUNTIME = 1000;

	public static DoubleUnaryOperator DEFAULT_EASING = Easing.DEFAULT_EASING;

	/**
	 * Option für durchgezogene Konturen und Linien.
	 */
	public static final Options.StrokeType SOLID = Options.StrokeType.SOLID;

	/**
	 * Option für gestrichelte Konturen und Linien.
	 */
	public static final Options.StrokeType DASHED = Options.StrokeType.DASHED;

	/**
	 * Option für gepunktete Konturen und Linien.
	 */
	public static final Options.StrokeType DOTTED = Options.StrokeType.DOTTED;

	/**
	 * Option für Pfeile mit Strichen als Kopf.
	 */
	public static final Options.ArrowHead LINES = Options.ArrowHead.LINES;

	/**
	 * Option für Pfeile mit gefüllten Köpfen.
	 */
	public static final Options.ArrowHead FILLED = Options.ArrowHead.FILLED;

	/**
	 * Option für den Abschluss eines Pfades oder Bogens, ohne die Enden zu
	 * verbinden.
	 */
	public static final Options.PathType OPEN = Options.PathType.OPEN;

	/**
	 * Option für den Abschluss eines Pfades oder Bogens durch Verbindung der
	 * Enden des Bogens mit einer Linie.
	 */
	public static final Options.PathType CLOSED = Options.PathType.CLOSED;

	/**
	 * Option für den Abschluss eines Bogens durch Verbindung der Enden des
	 * Bogens mit dem Mittelpunkt der zugrundeliegenden Ellipse. Dadurch
	 * entsteht ein Kreisausschnitt.
	 */
	public static final Options.PathType PIE = Options.PathType.PIE;

	/**
	 * Richtung: Mitte (bzw. keine Richtung)
	 */
	public static final Options.Direction CENTER = Options.Direction.CENTER;

	/**
	 * Richtung: Norden
	 *
	 * @see #UP
	 */
	public static final Options.Direction NORTH = Options.Direction.NORTH;

	/**
	 * Richtung: Osten
	 *
	 * @see #RIGHT
	 */
	public static final Options.Direction EAST = Options.Direction.EAST;

	/**
	 * Richtung: Süden
	 *
	 * @see #DOWN
	 */
	public static final Options.Direction SOUTH = Options.Direction.SOUTH;

	/**
	 * Richtung: Westen
	 *
	 * @see #LEFT
	 */
	public static final Options.Direction WEST = Options.Direction.WEST;

	/**
	 * Richtung: Nordosten
	 *
	 * @see #UPLEFT
	 */
	public static final Options.Direction NORTHEAST = Options.Direction.NORTHEAST;

	/**
	 * Richtung: Südosten
	 *
	 * @see #DOWNLEFT
	 */
	public static final Options.Direction SOUTHEAST = Options.Direction.SOUTHEAST;

	/**
	 * Richtung: Nordwesten
	 *
	 * @see #UPRIGHT
	 */
	public static final Options.Direction NORTHWEST = Options.Direction.NORTHWEST;

	/**
	 * Richtung: Südwesten
	 *
	 * @see #DOWNRIGHT
	 */
	public static final Options.Direction SOUTHWEST = Options.Direction.SOUTHWEST;

	/**
	 * Richtung: Mitte
	 *
	 * @see #CENTER
	 */
	public static final Options.Direction MIDDLE = Options.Direction.MIDDLE;

	/**
	 * Richtung: Oben
	 *
	 * @see #NORTH
	 */
	public static final Options.Direction UP = Options.Direction.UP;

	/**
	 * Richtung: Rechts
	 *
	 * @see #EAST
	 */
	public static final Options.Direction RIGHT = Options.Direction.RIGHT;

	/**
	 * Richtung: Unten
	 *
	 * @see #SOUTH
	 */
	public static final Options.Direction DOWN = Options.Direction.DOWN;

	/**
	 * Richtung: Links
	 *
	 * @see #WEST
	 */
	public static final Options.Direction LEFT = Options.Direction.LEFT;

	/**
	 * Richtung: Oben links
	 *
	 * @see #NORTHWEST
	 */
	public static final Options.Direction UPLEFT = Options.Direction.UPLEFT;

	/**
	 * Richtung: Unten links
	 *
	 * @see #SOUTHWEST
	 */
	public static final Options.Direction DOWNLEFT = Options.Direction.DOWNLEFT;

	/**
	 * Richtung: Oben rechts
	 *
	 * @see #UPRIGHT
	 */
	public static final Options.Direction UPRIGHT = Options.Direction.UPRIGHT;

	/**
	 * Richtung: Unten rechts
	 *
	 * @see #SOUTHEAST
	 */
	public static final Options.Direction DOWNRIGHT = Options.Direction.DOWNRIGHT;


	/**
	 * Farbe: Schwarz
	 */
	public static final Color BLACK = Color.BLACK;

	/**
	 * Farbe: Weiß
	 */
	public static final Color WHITE = Color.WHITE;

	/**
	 * Farbe: Grau
	 */
	public static final Color GRAY = Color.GRAY;

	/**
	 * Farbe: Dunkelgrau
	 */
	public static final Color DARKGRAY = Color.DARKGRAY;

	/**
	 * Farbe: Hellgrau
	 */
	public static final Color LIGHTGRAY = Color.LIGHTGRAY;

	/**
	 * Farbe: Rot
	 */
	public static final Color RED = Color.RED;

	/**
	 * Farbe: Blau
	 */
	public static final Color BLUE = Color.BLUE;

	/**
	 * Farbe: Grün
	 */
	public static final Color GREEN = Color.GREEN;

	/**
	 * Farbe: Gelb
	 */
	public static final Color YELLOW = Color.YELLOW;

	/**
	 * Farbe: Orange
	 */
	public static final Color ORANGE = Color.ORANGE;

	/**
	 * Farbe: Türkis
	 */
	public static final Color CYAN = Color.CYAN;

	/**
	 * Farbe: Magenta
	 */
	public static final Color MAGENTA = Color.MAGENTA;

	/**
	 * Farbe: Pink
	 */
	public static final Color PINK = Color.PINK;

	/**
	 * Farbe: Lila
	 */
	public static final Color PURPLE = Color.PURPLE;

	/**
	 * Farbe: Braun
	 */
	public static final Color BROWN = Color.BROWN;

	/**
	 * Standardfarbe für den Hintergrund.
	 */
	public static Color DEFAULT_BACKGROUND = new Color(200, 200, 200);

	/**
	 * Konstante zur Prüfung, ob kein Mausknopf gedrückt wurde.
	 */
	public static final int NOMOUSE = MouseEvent.NOBUTTON;

	/**
	 * Konstante zur Prüfung, ob Mausknopf 1 (links) gedrückt wurde.
	 */
	public static final int MOUSE1 = MouseEvent.BUTTON1;

	/**
	 * Konstante zur Prüfung, ob Mausknopf 2 (rechts) gedrückt wurde.
	 */
	public static final int MOUSE2 = MouseEvent.BUTTON2;

	/**
	 * Konstante zur Prüfung, ob Mausknopf 3 (mittig) gedrückt wurde.
	 */
	public static final int MOUSE3 = MouseEvent.BUTTON3;

	/**
	 * Konstante für die Kreiszahl Pi (entspricht 180 Grad).
	 */
	public static final double PI = Math.PI;

	/**
	 * Konstante für die Hälfte der Kreiszahl Pi (entspricht 90 Grad).
	 */
	public static final double HALF_PI = Math.PI / 2.0;

	/**
	 * Konstante für ein Viertel der Kreiszahl Pi (entspricht 45 Grad).
	 */
	public static final double QUARTER_PI = Math.PI / 4.0;

	/**
	 * Konstante für das Doppelte der Kreiszahl Pi (entspricht 360 Grad).
	 */
	public static final double TWO_PI = Math.PI * 2.0;

	/**
	 * Konstante für fette Schrift.
	 */
	public static final int BOLD = Font.BOLD;

	/**
	 * Konstante für kursive Schrift.
	 */
	public static final int ITALIC = Font.ITALIC;

	/**
	 * Konstante für normale Schrift.
	 */
	public static final int PLAIN = Font.PLAIN;

	/*
	 * Globale Variablen, die von allen Klassen genutzt werden dürfen. Änderungen
	 * wirken sich auf die aktuelle Zeichenmaschine aus und sollten nur von der
	 * Zeichenmaschine selbst vorgenommen werden.
	 */
	// TODO: (ngb) volatile ?

	/**
	 * Aktuell dargestellte Bilder pro Sekunde.
	 */
	public static int framesPerSecond = DEFAULT_FPS;

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
	 * Die aktuelle {@code x}-Koordinate der Maus. (Wird einmal pro Frame
	 * aktualisiert.)
	 */
	public static double mouseX = 0.0;

	/**
	 * Die aktuelle {@code y}-Koordinate der Maus. (Wird einmal pro Frame
	 * aktualisiert.)
	 */
	public static double mouseY = 0.0;

	/**
	 * Die letzte {@code x}-Koordinate der Maus. (Wird einmal pro Frame
	 * aktualisiert).
	 */
	public static double pmouseX = 0.0;

	/**
	 * Die letzte {@code y}-Koordinate der Maus. (Wird einmal pro Frame
	 * aktualisiert.)
	 */
	public static double pmouseY = 0.0;

	/**
	 * Die aktuelle (<em>current</em>) {@code x}-Koordinate der Maus. (Wird bei
	 * jeder Mausbewegung aktualisiert).
	 */
	public static double cmouseX = 0.0;

	/**
	 * Die aktuelle (<em>current</em>) {@code y}-Koordinate der Maus. (Wird bei
	 * jeder Mausbewegung aktualisiert).
	 */
	public static double cmouseY = 0.0;

	/**
	 * Gibt an, ob derzeit ein Mausknopf gedrückt ist.
	 */
	public static boolean mousePressed = false;

	/**
	 * Der aktuell gedrückte Mausknopf. Die Mausknöpfe werden durch die
	 * Konstanten {@link #NOMOUSE}, {@link #MOUSE1}, {@link #MOUSE2} und
	 * {@link #MOUSE3} angegeben.
	 */
	public static int mouseButton = NOMOUSE;

	/**
	 * Das zuletzt ausgelöste {@code MouseEvent}.
	 */
	public static MouseEvent mouseEvent;

	/**
	 * Mauszeiger: Pfeil
	 */
	public static final int ARROW = Cursor.DEFAULT_CURSOR;

	/**
	 * Mauszeiger: Fadenkreuz
	 */
	public static final int CROSS = Cursor.CROSSHAIR_CURSOR;

	/**
	 * Mauszeiger: Hand
	 */
	public static final int HAND = Cursor.HAND_CURSOR;

	/**
	 * Mauszeiger: Bewegungspfeile
	 */
	public static final int MOVE = Cursor.MOVE_CURSOR;

	/**
	 * Mauszeiger: Textzeiger
	 */
	public static final int TEXT = Cursor.TEXT_CURSOR;

	/**
	 * Mauszeiger: Ladezeiger
	 */
	public static final int WAIT = Cursor.WAIT_CURSOR;

	/**
	 * Gibt an, ob derzeit eine Taste gedrückt ist.
	 */
	public static boolean keyPressed = false;

	/**
	 * Das Text-Zeichen der zuletzt gedrückten Taste. Für Tasten ohne
	 * zugeordnetes Zeichen ist das Zeichen leer.
	 */
	public static char key = ' ';

	/**
	 * Der Tastencode der zuletzt gedrückten Taste. Die Keycodes können in der
	 * Klasse {@link KeyEvent} nachgesehen werden. Die Keycodes für die
	 * wichtigsten Tasten sind als Konstanten mit dem Prefix  {@code KEY_}
	 * vorhanden. (Zum Beispiel {@link #KEY_A}.)
	 */
	public static int keyCode = 0;

	/**
	 * Das zuletzt ausgelöste {@link KeyEvent}.
	 */
	public static KeyEvent keyEvent;

	/**
	 * Die Höhe der Zeichenleinwand.
	 */
	public static int canvasWidth;

	/**
	 * Die Breite der Zeichenleinwand.
	 */
	public static int canvasHeight;

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
	 * Erstellt eine graue Farbe. Der Parameter {@code gray} gibt einen Grauwert
	 * zwischen 0 und 255 an, wobei 0 schwarz und 255 weiß ist.
	 *
	 * <pre><code>
	 * Color iron_grey = color(94);
	 * </code></pre>
	 *
	 * @param gray Grauwert zwischen 0 und 255.
	 * @return Ein passendes Farbobjekt.
	 */
	public static final Color color( int gray ) {
		return color(gray, gray, gray, 255);
	}

	/**
	 * Erstellt eine graue Farbe. Der Parameter {@code gray} gibt einen Grauwert
	 * zwischen 0 und 255 an, wobei 0 schwarz und 255 weiß ist. {@code alpha}
	 * gibt den Transparenzwert an (auch zwischen 0 und 255), wobei 0 komplett
	 * durchsichtig ist und 255 komplett deckend.
	 *
	 * <pre><code>
	 * Color iron_grey_50 = color(94, 50);
	 * </code></pre>
	 *
	 * @param gray Grauwert zwischen 0 und 255.
	 * @param alpha Transparenzwert zwischen 0 und 255.
	 * @return Ein passendes Farbobjekt.
	 */
	public static final Color color( int gray, int alpha ) {
		return color(gray, gray, gray, alpha);
	}

	/**
	 * Erstellt eine Farbe. Die Parameter {@code red}, {@code green} und
	 * {@code blue} geben die Rot-, Grün- und Blauanteile der Farbe. Die Werte
	 * liegen zwischen 0 und 255.
	 *
	 * <pre><code>
	 * Color arctic_blue = color(149, 214, 220);
	 * </code></pre>
	 *
	 * @param red Rotwert zwischen 0 und 255.
	 * @param green Grünwert zwischen 0 und 255.
	 * @param blue Blauwert zwischen 0 und 255.
	 * @return Ein passendes Farbobjekt.
	 */
	public static final Color color( int red, int green, int blue ) {
		return color(red, green, blue, 255);
	}

	/**
	 * Erstellt eine Farbe. Die Parameter {@code red}, {@code green} und
	 * {@code blue} geben die Rot-, Grün- und Blauanteile der Farbe. Die Werte
	 * liegen zwischen 0 und 255. {@code alpha} gibt den Transparenzwert an
	 * (auch zwischen 0 und 255), wobei 0 komplett durchsichtig ist und 255
	 * komplett deckend.
	 *
	 * <pre><code>
	 * Color arctic_blue_50 = color(149, 214, 220, 50);
	 * </code></pre>
	 *
	 * @param red Rotwert zwischen 0 und 255.
	 * @param green Grünwert zwischen 0 und 255.
	 * @param blue Blauwert zwischen 0 und 255.
	 * @param alpha Transparenzwert zwischen 0 und 255.
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
	 * <pre><code>
	 * Color fillColor = randomColor();
	 * </code></pre>
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
	 * <pre><code>
	 * Color fillColor = randomNiceColor();
	 * </code></pre>
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
	 * <pre><code>
	 * Color arctic_blue = colorHsl(185, 32, 86);
	 * </code></pre>
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
	 * <pre><code>
	 * Color arctic_blue = colorHsl(185, 50, 72);
	 * </code></pre>
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

	// Ressourcen

	/**
	 * Lädt ein Bild aus einer Datei oder von einer Webadresse.
	 *
	 * @param source Ein Dateipfad oder eine Webadresse.
	 * @return Das geladene Bild.
	 * @see ImageLoader#loadImage(String)
	 */
	public static final BufferedImage loadImage( String source ) {
		return ImageLoader.loadImage(source);
	}

	// Mathematische Funktionen

	/**
	 * Berechnet das Minimum aller angegebenen Werte.
	 *
	 * <pre><code>
	 * double minimum = min(1.0, 5.1, 3.2); // 1.0
	 * </code></pre>
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
	 * Berechnet das Maximum aller angegebenen Werte.
	 *
	 * <pre><code>
	 * double maximum = max(1.0, 5.1, 3.2); // 5.1
	 * </code></pre>
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

	/**
	 * Berechnet die Summe alle angegebenen Werte.
	 *
	 * <pre><code>
	 * double summe = sum(1.0, 2.0, 3.2); // 6.2
	 * </code></pre>
	 *
	 * @param numbers Die Werte, aus denen die Summe berechnet werden soll.
	 * @return Die Summe der Werte.
	 */
	public static final double sum( double... numbers ) {
		if( numbers == null ) {
			throw new IllegalArgumentException("Array may not be <null> or empty.");
		}

		double sum = 0;
		for( double number : numbers ) {
			sum += number;
		}
		return sum;
	}

	/**
	 * Berechnet das arithmetische Mittel der angegebenen Werte.
	 *
	 * <pre><code>
	 * double summe = sum(1.0, 2.2, 3.1); // 2.1
	 * </code></pre>
	 *
	 * @param numbers Die Werte, aus denen der MIttelwert berechnet werden
	 * 	soll.
	 * @return Der Mittelwert der Werte.
	 */
	public static final double avg( double... numbers ) {
		if( numbers == null || numbers.length == 0 ) {
			throw new IllegalArgumentException("Array may not be <null> or empty.");
		}

		return sum(numbers) / (double) numbers.length;
	}

	/**
	 * Ermittelt den Absolutbetrag der angegebenen Zahl.
	 *
	 * <pre><code>
	 * double positiv = abs(-3.2); // 3.2
	 * </code></pre>
	 *
	 * @param x Eine Zahl.
	 * @return Der Absolutbetrag.
	 */
	public static final double abs( double x ) {
		return Math.abs(x);
	}

	/**
	 * Ermittelt das Vorzeichen der angegebenen Zahl.
	 *
	 * <pre><code>
	 * double vorzeichen = sign(-3.2); // -1.0
	 * </code></pre>
	 *
	 * @param x Eine Zahl.
	 * @return -1, 1 oder 0.
	 */
	public static final double sign( double x ) {
		return Math.signum(x);
	}

	/**
	 * Rundet die angegebene Zahl auf die nächste ganze Zahl.
	 *
	 * <pre><code>
	 * double gerundet = sign(3.2); // 3.0
	 * </code></pre>
	 *
	 * @param x Eine Zahl.
	 * @return Die gerundete Zahl.
	 */
	public static final double round( double x ) {
		return Math.round(x);
	}

	/**
	 * Rundet die angegebene Zahl ab.
	 *
	 * <pre><code>
	 * double abgerundet = sign(3.2); // 3.0
	 * </code></pre>
	 *
	 * @param x Eine Zahl.
	 * @return Die abgerundete Zahl.
	 */
	public static final double floor( double x ) {
		return Math.floor(x);
	}

	/**
	 * Rundet die angegebene Zahl auf.
	 *
	 * <pre><code>
	 * double aufgerundet = sign(3.2); // 4.0
	 * </code></pre>
	 *
	 * @param x Eine Zahl.
	 * @return Die aufgerundete Zahl.
	 */
	public static final double ceil( double x ) {
		return Math.ceil(x);
	}

	/**
	 * Ermittelt die Quadratwurzel der angegebenen Zahl.
	 *
	 * <pre><code>
	 * double wurzel = sqrt(16); // 4.0
	 * </code></pre>
	 *
	 * @param x Eine Zahl.
	 * @return Die Quadratwurzel.
	 */
	public static final double sqrt( double x ) {
		return Math.sqrt(x);
	}

	/**
	 * Ermittelt die Potenz der angegebenen Zahl zum angegebenen Exponenten.
	 *
	 * <pre><code>
	 * double hoch4 = sqrt(8, 4); // 4096.0
	 * </code></pre>
	 *
	 * @param x Eine Zahl.
	 * @param e Der Exponent.
	 * @return {@code x} hoch {@code e}.
	 */
	public static final double pow( double x, double e ) {
		return Math.pow(x, e);
	}

	/**
	 * Rechnet von Grad in Radian um.
	 *
	 * <pre><code>
	 * double radian = radians(360); // 6.28318530717959
	 * </code></pre>
	 *
	 * @param angle Ein Winkel in Grad.
	 * @return Der Winkel in Radian.
	 */
	public static final double radians( double angle ) {
		return Math.toRadians(angle);
	}

	/**
	 * Rechnet von Radian in Grad um.
	 *
	 * <pre><code>
	 * double grad = radians(HALF_PI); // 90.0
	 * </code></pre>
	 *
	 * @param radians Der Winkel in Radian.
	 * @return Der Winkel in Grad.
	 */
	public static final double degrees( double radians ) {
		return Math.toDegrees(radians);
	}

	/**
	 * Ermittelt den Sinus der angegebenen Zahl.
	 *
	 * <pre><code>
	 * double sinus = sin(0.0); // 0.0
	 * </code></pre>
	 *
	 * @param x Eine Zahl.
	 * @return {@code sin(x)}.
	 */
	public static final double sin( double x ) {
		return Math.sin(x);
	}

	/**
	 * Ermittelt den Kosinus der angegebenen Zahl.
	 *
	 * <pre><code>
	 * double kosinus = cos(0.0); // 1.0
	 * </code></pre>
	 *
	 * @param x Eine Zahl.
	 * @return {@code cos(x)}.
	 */
	public static final double cos( double x ) {
		return Math.cos(x);
	}

	/**
	 * Ermittelt den Tangens der angegebenen Zahl.
	 *
	 * <pre><code>
	 * double sinus = tan(1.0); // 1.5574077246549
	 * </code></pre>
	 *
	 * @param x Eine Zahl.
	 * @return {@code tan(x)}.
	 */
	public static final double tan( double x ) {
		return Math.tan(x);
	}

	/**
	 * Ermittelt den Arkussinus der angegebenen Zahl.
	 *
	 * @param x Eine Zahl.
	 * @return {@code asin(x)}.
	 */
	public static final double arcsin( double x ) {
		return Math.asin(x);
	}

	/**
	 * Ermittelt den Arcuskosinus der angegebenen Zahl.
	 *
	 * @param x Eine Zahl.
	 * @return {@code acos(x)}.
	 */
	public static final double arccos( double x ) {
		return Math.acos(x);
	}

	/**
	 * Ermittelt den Arcusktangens der angegebenen Zahl.
	 *
	 * @param x Eine Zahl.
	 * @return {@code atan(x)}.
	 */
	public static final double arctan( double x ) {
		return Math.atan(x);
	}

	/**
	 * Beschränkt die angegebene Zahl auf das Intervall {@code [min, max]}.
	 * Liegt {@code x} außerhalb des Intervalls, wird eine der Grenzen
	 * zurückgegeben.
	 *
	 * <pre><code>
	 * double beschraenkt1 = limit(2.1, 0.0, 3.0); // 2.1
	 * double beschraenkt2 = limit(4.1, 0.0, 3.0); // 1.0
	 * </code></pre>
	 *
	 * @param x Eine Zahl.
	 * @param min Das Minimum des Intervalls.
	 * @param max Das Maximum des Intervalls.
	 * @return Eine Zahl im Intervall {@code [min, max]}.
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
	 * Beschränkt die angegebene Zahl auf das Intervall {@code [min, max]}.
	 * Liegt {@code i} außerhalb des Intervalls, wird eine der Grenzen
	 * zurückgegeben.
	 *
	 * <pre><code>
	 * double beschraenkt1 = limit(2, 0, 3); // 2
	 * double beschraenkt2 = limit(4, 0, 3); // 3
	 * </code></pre>
	 *
	 * @param i Eine Zahl.
	 * @param min Das Minimum des Intervalls.
	 * @param max Das Maximum des Intervalls.
	 * @return Eine Zahl im Intervall {@code [min, max]}.
	 */
	public static final int limit( int i, int min, int max ) {
		if( i > max ) {
			return max;
		}
		if( i < min ) {
			return min;
		}
		return i;
	}

	/**
	 * Interpoliert einen Wert zwischen {@code from} und {@code to} linear,
	 * abhängig von {@code t}. Das Ergebnis entspricht der Formel
	 *
	 * <pre>
	 * from - t * (from + to)
	 * </pre>
	 * <p>
	 * In der Regel liegt {@code t} im Intervall {@code [0, 1]}. Für
	 * {@code t = 0} ist das Ergebnis {@code from} und für {@code t = 1} ist das
	 * Ergebnis {@code to}. {@code t} kann aber auch Werte außerhalb des
	 * Intervalls annehmen. Für {@code t = 2} ist das Ergebnis beispielsweise
	 * {@code 2*to};
	 *
	 * <pre><code>
	 * double interpoliert = interpolate(100.0, 500.0, 0.5); // 300.0
	 * double interpoliert = interpolate(100.0, 500.0, 1.0); // 500.0
	 * double interpoliert = interpolate(100.0, 500.0, 1.5); // 750.0
	 * </code></pre>
	 *
	 * @param from Startwert
	 * @param to Zielwert
	 * @param t Anteil des Ergebnisses auf der Strecke zwischen {@code from}
	 * 	und {@code to}.
	 * @return Das Ergebnis der linearen Interpolation.
	 */
	public static final double interpolate( double from, double to, double t ) {
		return from + t * (to - from);
	}

	/**
	 * Interpoliert einen Wert zwischen {@code from} und {@code to}, aber
	 * beschränkt {@code t} auf das Intervall {@code [0, 1]}.
	 *
	 * <pre><code>
	 * double interpoliert = interpolate(100.0, 500.0, 0.5); // 300.0
	 * double interpoliert = interpolate(100.0, 500.0, 1.0); // 500.0
	 * double interpoliert = interpolate(100.0, 500.0, 1.5); // 500.0
	 * </code></pre>
	 *
	 * @param from Startwert
	 * @param to Zielwert
	 * @param t Wert zwischen 0 und 1.
	 * @return Das Ergebnis der linearen Interpolation.
	 * @see #interpolate(double, double, double)
	 */
	public static final double morph( double from, double to, double t ) {
		return interpolate(from, to, limit(t, 0.0, 1.0));
	}

	/**
	 * Bestimmt für den angegebenen Wert aus dem Intervall
	 * {@code [fromMin, fromMax]} einen zugehörigen Wert aus dem Intervall
	 * {@code [toMin, toMax]}.
	 * <p>
	 * Für {@code value = fromMin} wird {@code toMin} zurückgegeben. Für
	 * {@code value = fromMax} ist das Ergebnis {@code toMax}. Befindet sich
	 * {@code value} genau mittig zwischen {@code fromMin} und {@code fromMax},
	 * dann ist das Ergebnis genau {@code interpolate(toMin, toMax, 0.5)}.
	 * <p>
	 * Mit {@code map()} lassen sich Werte eines Originalintervalls auf ein
	 * Zielintervalls transponieren. Dies ist beispielsweise hilfreich, um aus
	 * der Mausbewegung die relative Position auf der Zeichenfläche zu
	 * bestimmen:
	 *
	 * <pre><code>
	 * double relativeMouseX = map(mouseX, 0.0, canvasWidth, 0.0, 1.0);
	 * </code></pre>
	 *
	 * @param value Der Wert aus dem original Intervall.
	 * @param fromMin Untere Grenze des original Intervalls.
	 * @param fromMax Obere Grenze des original Intervalls.
	 * @param toMin Untere Grenze des Zielintervalls.
	 * @param toMax Obere Grenze des Zielintervalls.
	 * @return Der zugehörige Wert aus dem Zielintervall.
	 * @see #interpolate(double, double, double)
	 */
	public static final double map( double value, double fromMin, double fromMax, double toMin, double toMax ) {
		return interpolate(toMin, toMax, (value - fromMin) / (fromMax - fromMin));
	}

	/**
	 * Geteilte {@code Random}-Instanz für einheitliche Zufallszahlen.
	 */
	private static Random random = null;

	/**
	 * Gibt die geteilte {@code Random}-Instanz zurück.
	 *
	 * @return Die {@code Random}-Instanz.
	 */
	private static Random getRandom() {
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
	 * @param percent Eine Prozentzahl zwischen 0 und 100.
	 * @return Ein Wahrheitswert.
	 */
	public static final boolean randomBool( int percent ) {
		return randomBool(percent / 100.0);
	}

	/**
	 * Erzeugt einen zufälligen Wahrheitswert. {@code true} wird mit der
	 * Wahrscheinlichkeit {@code weight} erzeugt.
	 *
	 * @param weight Wahrscheinlichkeit für {@code true}.
	 * @return Ein Wahrheitswert.
	 */
	public static final boolean randomBool( double weight ) {
		return random() < weight;
	}

	/**
	 * Erzeugt eine Pseudozufallszahl zwischen -1 und 1 nach einer
	 * Normalverteilung mit Mittelwert 0 und Standardabweichung 1.
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
	public static final int choice( int[] values ) {
		return values[random(0, values.length - 1)];
	}

	/**
	 * Wählt ein zufälliges Element aus dem Array aus.
	 *
	 * @param values Ein Array mit Werten, die zur Auswahl stehen.
	 * @return Ein zufälliges Element aus dem Array.
	 */
	public static final double choice( double[] values ) {
		return values[random(0, values.length - 1)];
	}

	/**
	 * Wählt ein zufälliges Element aus dem Array aus.
	 *
	 * @param values Ein Array mit Werten, die zur Auswahl stehen.
	 * @param <T> Datentyp des Elements.
	 * @return Ein zufälliges Element aus dem Array.
	 */
	public static final <T> T choice( T[] values ) {
		return values[random(0, values.length - 1)];
	}

	/**
	 * Wählt die angegebene Anzahl Elemente aus dem Array aus.
	 *
	 * @param values Ein Array mit Werten, die zur Auswahl stehen.
	 * @param n Anzahl der auszuwählenden Elemente.
	 * @param unique Bei {@code true} werden Elemente im Array nur maximal
	 * 	einmal ausgewählt (Ziehen ohne Zurücklegen).
	 * @return Ein zufälliges Element aus dem Array.
	 * @throws IllegalArgumentException Wenn {@code unique == true} und
	 *                                  {@code values.length < n}, also nicht
	 *                                  genug Werte zur Wahl stehen.
	 */
	public static final int[] choice( int[] values, int n, boolean unique ) {
		if( unique && values.length < n )
			throw new IllegalArgumentException(
				String.format("Need at least <%d> values to choose <%d> unique values (<%d> given).", n, n, values.length)
			);

		int[] result = new int[n];
		int[] valuesCopy = Arrays.copyOf(values, values.length);
		for( int i = 0; i < n; i++ ) {
			int j = random(0, valuesCopy.length - 1);
			int l = valuesCopy.length - 1;

			result[i] = valuesCopy[j];
			valuesCopy[j] = valuesCopy[l];
			valuesCopy[l] = result[i];

			if( unique )
				valuesCopy = Arrays.copyOf(valuesCopy, l);
		}
		return result;
	}

	/**
	 * Wählt die angegebene Anzahl Elemente aus dem Array aus.
	 *
	 * @param values Ein Array mit Werten, die zur Auswahl stehen.
	 * @param n Anzahl der auszuwählenden Elemente.
	 * @param unique Bei {@code true} werden Elemente im Array nur maximal
	 * 	einmal ausgewählt (Ziehen ohne Zurücklegen).
	 * @return Ein zufälliges Element aus dem Array.
	 * @throws IllegalArgumentException Wenn {@code unique == true} und
	 *                                  {@code values.length < n}, also nicht
	 *                                  genug Werte zur Wahl stehen.
	 */
	public static final double[] choice( double[] values, int n, boolean unique ) {
		if( unique && values.length < n )
			throw new IllegalArgumentException(
				String.format("Need at least <%d> values to choose <%d> unique values (<%d> given).", n, n, values.length)
			);

		double[] result = new double[n];
		double[] valuesCopy = Arrays.copyOf(values, values.length);
		for( int i = 0; i < n; i++ ) {
			int j = random(0, valuesCopy.length - 1);
			int l = valuesCopy.length - 1;

			result[i] = valuesCopy[j];
			valuesCopy[j] = valuesCopy[l];
			valuesCopy[l] = result[i];

			if( unique )
				valuesCopy = Arrays.copyOf(valuesCopy, l);
		}
		return result;
	}

	/**
	 * Wählt die angegebene Anzahl Elemente aus dem Array aus.
	 *
	 * @param values Ein Array mit Werten, die zur Auswahl stehen.
	 * @param n Anzahl der auszuwählenden Elemente.
	 * @param unique Bei {@code true} werden Elemente im Array nur maximal
	 * 	einmal ausgewählt (Ziehen ohne Zurücklegen).
	 * @param <T> Datentyp der Elemente.
	 * @return Ein zufälliges Element aus dem Array.
	 * @throws IllegalArgumentException Wenn {@code unique == true} und
	 *                                  {@code values.length < n}, also nicht
	 *                                  genug Werte zur Wahl stehen.
	 */
	public static final <T> T[] choice( T[] values, int n, boolean unique ) {
		if( unique && values.length < n )
			throw new IllegalArgumentException(
				String.format("Need at least <%d> values to choose <%d> unique values (<%d> given).", n, n, values.length)
			);

		T[] result = Arrays.copyOf(values, n);
		T[] valuesCopy = Arrays.copyOf(values, values.length);
		for( int i = 0; i < n; i++ ) {
			int last = valuesCopy.length - 1;
			int j = random(0, last);

			result[i] = valuesCopy[j];
			valuesCopy[j] = valuesCopy[last];
			valuesCopy[last] = result[i];

			if( unique )
				valuesCopy = Arrays.copyOf(valuesCopy, last);
		}
		return result;
	}

	/**
	 * Bringt die Zahlen im Array in eine zufällige Reihenfolge.
	 *
	 * @param values Ein Array mit Zahlen, die gemischt werden sollen.
	 * @return Das Array in zufälliger Reihenfolge.
	 */
	public static final int[] shuffle( int[] values ) {
		for( int i = 0; i < values.length - 1; i++ ) {
			int j = random(i, values.length - 1);
			int tmp = values[i];
			values[i] = values[j];
			values[j] = tmp;
		}

		return values;
	}

	/**
	 * Bringt die Zahlen im Array in eine zufällige Reihenfolge.
	 *
	 * @param values Ein Array mit Zahlen, die gemischt werden sollen.
	 * @return Das Array in zufälliger Reihenfolge.
	 */
	public static final double[] shuffle( double[] values ) {
		for( int i = 0; i < values.length - 1; i++ ) {
			int j = random(i, values.length - 1);
			double tmp = values[i];
			values[i] = values[j];
			values[j] = tmp;
		}

		return values;
	}

	/**
	 * Bringt die Werte im Array in eine zufällige Reihenfolge.
	 *
	 * @param values Ein Array mit Werte, die gemischt werden sollen.
	 * @param <T> Datentyp der Elemente.
	 * @return Das Array in zufälliger Reihenfolge.
	 */
	public static final <T> T[] shuffle( T[] values ) {
		java.util.List<T> valueList = Arrays.asList(values);
		Collections.shuffle(valueList, random);
		return valueList.toArray(values);
	}

	/**
	 * Geteilte {@code Noise}-Instanz zur Erzeugung von Perlin-Noise.
	 */
	private static Noise noise = null;

	/**
	 * Zähler für den letzten generierten Noise-Wert.
	 */
	private static int N = 0;

	/**
	 * Gibt die geteilte {@code Random}-Instanz zurück.
	 *
	 * @return Die {@code Random}-Instanz.
	 */
	private static Noise getNoise() {
		if( noise == null ) {
			noise = new Noise(getRandom());
		}
		return noise;
	}

	/**
	 * Erzeugt den nächsten Wert eines Perlin-Noise.
	 *
	 * @return Ein zufälliger Wert.
	 */
	public static final double noise() {
		return getNoise().noise(0.005 * N++);
	}

	/**
	 * Erzeugt den nächsten Wert eines Perlin-Noise.
	 *
	 * @param x x-Wert für den Noise.
	 * @return Ein zufälliger Wert.
	 */
	public static final double noise( double x ) {
		return getNoise().noise(x);
	}

	/**
	 * Erzeugt den nächsten Wert eines zweidimensionalen Perlin-Noise.
	 *
	 * @param x x-Wert für den Noise.
	 * @param y y-Wert für den Noise.
	 * @return Ein zufälliger Wert.
	 */
	public static final double noise( double x, double y ) {
		return getNoise().noise(x, y);
	}

	/**
	 * Erzeugt den nächsten Wert eines dreidimensionalen Perlin-Noise.
	 *
	 * @param x x-Wert für den Noise.
	 * @param y y-Wert für den Noise.
	 * @param z z-Wert für den Noise.
	 * @return Ein zufälliger Wert.
	 */
	public static final double noise( double x, double y, double z ) {
		return getNoise().noise(x, y, z);
	}

	// Typecasting

	/**
	 * Konvertiert das angegebenen Zeichen in eine ganze Zahl. Das Zeichen wird
	 * jeweils in seinen ASCII-Codepoint transformiert.
	 *
	 * @param value Der Wert.
	 * @return Eine ganze Zahl.
	 */
	public static final int asInt( char value ) {
		return value;
	}

	/**
	 * Konvertiert den angegebenen Wert in eine ganze Zahl.
	 *
	 * @param value Der Wert.
	 * @return Der Wert.
	 */
	public static final int asInt( byte value ) {
		return value;
	}

	/**
	 * Konvertiert den angegebenen Wert in eine ganze Zahl.
	 *
	 * @param value Der Wert.
	 * @return Der Wert.
	 */
	public static final int asInt( short value ) {
		return value;
	}

	/**
	 * Konvertiert den angegebenen Wert in eine ganze Zahl. Zahlen größer als
	 * {@link Integer#MAX_VALUE} werden auf {@code MAX_VALUE} reduziert.
	 * Entsprechendes gilt für Werte kleiner {@link Integer#MIN_VALUE}.
	 *
	 * @param value Der Wert.
	 * @return Eine ganze Zahl.
	 */
	public static final int asInt( long value ) {
		return (int) value;
	}

	/**
	 * Konvertiert den angegebenen Wert in eine ganze Zahl.
	 *
	 * @param value Der Wert.
	 * @return Der abgerundete Wert.
	 */
	public static final int asInt( double value ) {
		return (int) value;
	}

	/**
	 * Konvertiert den angegebenen Wert in eine ganze Zahl.
	 *
	 * @param value Der Wert.
	 * @return Der abgerundete Wert.
	 */
	public static final int asInt( float value ) {
		return (int) value;
	}

	/**
	 * Konvertiert den angegebenen Wert in eine ganze Zahl.
	 *
	 * @param value Der Wert.
	 * @return Der Wert.
	 */
	public static final int asInt( int value ) {
		return value;
	}

	/**
	 * Konvertiert den angegebenen Wahrheitswert in eine ganze Zahl.
	 * {@code true} entspricht 1, {@code false} wird zu 0.
	 *
	 * @param value Der Wert.
	 * @return 1 oder 0.
	 */
	public static final int asInt( boolean value ) {
		return value ? 1 : 0;
	}

	/**
	 * Konvertiert den angegebenen Text in eine ganze Zahl. Kann der Text nicht
	 * umgewandelt werden, dann wird 0 zurückgegeben.
	 *
	 * @param value Der Wert.
	 * @return Eine ganze Zahl-
	 * @see Integer#parseInt(String)
	 */
	public static final int asInt( String value ) {
		try {
			return Integer.parseInt(value);
		} catch( NumberFormatException ex ) {
			return 0;
		}
	}

	/**
	 * Konvertiert einen char-Wert in einen double-Wert.
	 *
	 * @param value Der char-Wert.
	 * @return Ein double-Wert.
	 */
	public static final double asDouble( char value ) {
		return value;
	}

	/**
	 * Konvertiert einen byte-Wert in einen double-Wert.
	 *
	 * @param value Der byte-Wert.
	 * @return Ein double-Wert.
	 */
	public static final double asDouble( byte value ) {
		return value;
	}

	/**
	 * Konvertiert einen short-Wert in einen double-Wert.
	 *
	 * @param value Der short-Wert.
	 * @return Ein double-Wert.
	 */
	public static final double asDouble( short value ) {
		return value;
	}

	/**
	 * Konvertiert einen long-Wert in einen double-Wert.
	 *
	 * @param value Der long-Wert.
	 * @return Ein double-Wert.
	 */
	public static final double asDouble( long value ) {
		return (double) value;
	}

	/**
	 * Konvertiert einen double-Wert in einen double-Wert.
	 *
	 * @param value Der double-Wert.
	 * @return Ein double-Wert.
	 */
	public static final double asDouble( double value ) {
		return value;
	}

	/**
	 * Konvertiert einen float-Wert in einen double-Wert.
	 *
	 * @param value Der float-Wert.
	 * @return Ein double-Wert.
	 */
	public static final double asDouble( float value ) {
		return value;
	}

	/**
	 * Konvertiert einen int-Wert in einen double-Wert.
	 *
	 * @param value Der int-Wert.
	 * @return Ein double-Wert.
	 */
	public static final double asDouble( int value ) {
		return value;
	}

	/**
	 * Konvertiert einen boolean-Wert in einen double-Wert.
	 *
	 * @param value Der boolean-Wert.
	 * @return Ein double-Wert.
	 */
	public static final double asDouble( boolean value ) {
		return value ? 1.0 : 0.0;
	}

	/**
	 * Konvertiert einen String in einen double-Wert.
	 *
	 * @param value Der String.
	 * @return Ein double-Wert.
	 * @see Double#parseDouble(String)
	 */
	public static final double asDouble( String value ) {
		try {
			return Double.parseDouble(value);
		} catch( NumberFormatException ex ) {
			return 0.0;
		}
	}

	/**
	 * Konvertiert einen char-Wert in einen boolean-Wert.
	 *
	 * @param value Der char-Wert.
	 * @return Ein boolean-Wert.
	 */
	public static final boolean asBool( char value ) {
		return value != 0;
	}

	/**
	 * Konvertiert einen byte-Wert in einen boolean-Wert.
	 *
	 * @param value Der byte-Wert.
	 * @return Ein boolean-Wert.
	 */
	public static final boolean asBool( byte value ) {
		return value != 0;
	}

	/**
	 * Konvertiert einen short-Wert in einen boolean-Wert.
	 *
	 * @param value Der short-Wert.
	 * @return Ein boolean-Wert.
	 */
	public static final boolean asBool( short value ) {
		return value != 0;
	}

	/**
	 * Konvertiert einen int-Wert in einen boolean-Wert.
	 *
	 * @param value Der int-Wert.
	 * @return Ein boolean-Wert.
	 */
	public static final boolean asBool( int value ) {
		return value != 0;
	}

	/**
	 * Konvertiert einen long-Wert in einen boolean-Wert.
	 *
	 * @param value Der long-Wert.
	 * @return Ein boolean-Wert.
	 */
	public static final boolean asBool( long value ) {
		return value != 0L;
	}

	/**
	 * Konvertiert einen double-Wert in einen boolean-Wert.
	 *
	 * @param value Der double-Wert.
	 * @return Ein boolean-Wert.
	 */
	public static final boolean asBool( double value ) {
		return value != 0.0;
	}

	/**
	 * Konvertiert einen float-Wert in einen boolean-Wert.
	 *
	 * @param value Der float-Wert.
	 * @return Ein boolean-Wert.
	 */
	public static final boolean asBool( float value ) {
		return value != 0.0f;
	}

	/**
	 * Konvertiert einen boolean-Wert in einen boolean-Wert.
	 *
	 * @param value Der boolean-Wert.
	 * @return Ein boolean-Wert.
	 */
	public static final boolean asBool( boolean value ) {
		return value;
	}

	/**
	 * Konvertiert einen String in einen boolean-Wert.
	 *
	 * @param value Der String.
	 * @return Ein boolean-Wert.
	 * @see Boolean#parseBoolean(String)
	 */
	public static final boolean asBool( String value ) {
		return Boolean.parseBoolean(value);
	}

	/**
	 * Formt die angegebene Zahl in ihre Binärdarstellung um.
	 *
	 * <pre><code>
	 * int bin = binary(10); // "1010"
	 * </code></pre>
	 *
	 * @param i Eine Zahl.
	 * @return Die binäre Darstellung der Zahl als Text.
	 */
	public static final String binary( int i ) {
		return Integer.toBinaryString(i);
	}

	/**
	 * Formt die angegebene Binärzahl in eine Dezimalzahl um.
	 *
	 * <pre><code>
	 * int dezimal = romBinary("1010"); // 10
	 * </code></pre>
	 *
	 * @param binary Ein Text nur aus "0" und "1".
	 * @return Der Wert der Binärzahl.
	 */
	public static final int fromBinary( String binary ) {
		return Integer.valueOf(binary, 2);
	}

	/**
	 * Formt die angegebene Zahl in ihre hexadezimal Darstellung um.
	 *
	 * <pre><code>
	 * int hexa = hex(255); // "FF"
	 * </code></pre>
	 *
	 * @param i Eine Zahl.
	 * @return Die hexadezimal Darstellung der Zahl als Text.
	 */
	public static final String hex( int i ) {
		return Integer.toHexString(i);
	}

	/**
	 * Formt die angegebene Binärzahl in eine Dezimalzahl um.
	 *
	 * <pre><code>
	 * int dezimal = romHex("FF"); // 255
	 * </code></pre>
	 *
	 * @param binary Ein Text nur aus den Zeichen "0" bis "9" und "A" bis "F".
	 * @return Der Wert der Binärzahl.
	 */
	public static final int fromHex( String binary ) {
		return Integer.valueOf(binary, 16);
	}

	// Konstanten für Key events (Copied from KeyEvent)

	/**
	 * Constant for the ENTER virtual key.
	 */
	public static final int KEY_ENTER = KeyEvent.VK_ENTER;

	/**
	 * Constant for the BACK_SPACE virtual key.
	 */
	public static final int KEY_BACK_SPACE = KeyEvent.VK_BACK_SPACE;

	/**
	 * Constant for the TAB virtual key.
	 */
	public static final int KEY_TAB = KeyEvent.VK_TAB;

	/**
	 * Constant for the CANCEL virtual key.
	 */
	public static final int KEY_CANCEL = KeyEvent.VK_CANCEL;

	/**
	 * Constant for the CLEAR virtual key.
	 */
	public static final int KEY_CLEAR = KeyEvent.VK_CLEAR;

	/**
	 * Constant for the SHIFT virtual key.
	 */
	public static final int KEY_SHIFT = KeyEvent.VK_SHIFT;

	/**
	 * Constant for the CONTROL virtual key.
	 */
	public static final int KEY_CONTROL = KeyEvent.VK_CONTROL;

	/**
	 * Constant for the ALT virtual key.
	 */
	public static final int KEY_ALT = KeyEvent.VK_ALT;

	/**
	 * Constant for the PAUSE virtual key.
	 */
	public static final int KEY_PAUSE = KeyEvent.VK_PAUSE;

	/**
	 * Constant for the CAPS_LOCK virtual key.
	 */
	public static final int KEY_CAPS_LOCK = KeyEvent.VK_CAPS_LOCK;

	/**
	 * Constant for the ESCAPE virtual key.
	 */
	public static final int KEY_ESCAPE = KeyEvent.VK_ESCAPE;

	/**
	 * Constant for the SPACE virtual key.
	 */
	public static final int KEY_SPACE = KeyEvent.VK_SPACE;

	/**
	 * Constant for the PAGE_UP virtual key.
	 */
	public static final int KEY_PAGE_UP = KeyEvent.VK_PAGE_UP;

	/**
	 * Constant for the PAGE_DOWN virtual key.
	 */
	public static final int KEY_PAGE_DOWN = KeyEvent.VK_PAGE_DOWN;

	/**
	 * Constant for the END virtual key.
	 */
	public static final int KEY_END = KeyEvent.VK_END;

	/**
	 * Constant for the HOME virtual key.
	 */
	public static final int KEY_HOME = KeyEvent.VK_HOME;

	/**
	 * Constant for the non-numpad <b>left</b> arrow key.
	 */
	public static final int KEY_LEFT = KeyEvent.VK_LEFT;

	/**
	 * Constant for the non-numpad <b>up</b> arrow key.
	 */
	public static final int KEY_UP = KeyEvent.VK_UP;

	/**
	 * Constant for the non-numpad <b>right</b> arrow key.
	 */
	public static final int KEY_RIGHT = KeyEvent.VK_RIGHT;

	/**
	 * Constant for the non-numpad <b>down</b> arrow key.
	 */
	public static final int KEY_DOWN = KeyEvent.VK_DOWN;

	/**
	 * Constant for the comma key, ","
	 */
	public static final int KEY_COMMA = KeyEvent.VK_COMMA;

	/**
	 * Constant for the minus key, "-"
	 *
	 * @since 1.2
	 */
	public static final int KEY_MINUS = KeyEvent.VK_MINUS;

	/**
	 * Constant for the period key, "."
	 */
	public static final int KEY_PERIOD = KeyEvent.VK_PERIOD;

	/**
	 * Constant for the forward slash key, "/"
	 */
	public static final int KEY_SLASH = KeyEvent.VK_SLASH;

	/**
	 * Constant for the "0" key.
	 */
	public static final int KEY_0 = KeyEvent.VK_0;

	/**
	 * Constant for the "1" key.
	 */
	public static final int KEY_1 = KeyEvent.VK_1;

	/**
	 * Constant for the "2" key.
	 */
	public static final int KEY_2 = KeyEvent.VK_2;

	/**
	 * Constant for the "3" key.
	 */
	public static final int KEY_3 = KeyEvent.VK_3;

	/**
	 * Constant for the "4" key.
	 */
	public static final int KEY_4 = KeyEvent.VK_4;

	/**
	 * Constant for the "5" key.
	 */
	public static final int KEY_5 = KeyEvent.VK_5;

	/**
	 * Constant for the "6" key.
	 */
	public static final int KEY_6 = KeyEvent.VK_6;

	/**
	 * Constant for the "7" key.
	 */
	public static final int KEY_7 = KeyEvent.VK_7;

	/**
	 * Constant for the "8" key.
	 */
	public static final int KEY_8 = KeyEvent.VK_8;

	/**
	 * Constant for the "9" key.
	 */
	public static final int KEY_9 = KeyEvent.VK_9;

	/**
	 * Constant for the semicolon key, ";"
	 */
	public static final int KEY_SEMICOLON = KeyEvent.VK_SEMICOLON;

	/**
	 * Constant for the equals key, "="
	 */
	public static final int KEY_EQUALS = KeyEvent.VK_EQUALS;

	/* VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A) */

	/**
	 * Constant for the "A" key.
	 */
	public static final int KEY_A = KeyEvent.VK_A;

	/**
	 * Constant for the "B" key.
	 */
	public static final int KEY_B = KeyEvent.VK_B;

	/**
	 * Constant for the "C" key.
	 */
	public static final int KEY_C = KeyEvent.VK_C;

	/**
	 * Constant for the "D" key.
	 */
	public static final int KEY_D = KeyEvent.VK_D;

	/**
	 * Constant for the "E" key.
	 */
	public static final int KEY_E = KeyEvent.VK_E;

	/**
	 * Constant for the "F" key.
	 */
	public static final int KEY_F = KeyEvent.VK_F;

	/**
	 * Constant for the "G" key.
	 */
	public static final int KEY_G = KeyEvent.VK_G;

	/**
	 * Constant for the "H" key.
	 */
	public static final int KEY_H = KeyEvent.VK_H;

	/**
	 * Constant for the "I" key.
	 */
	public static final int KEY_I = KeyEvent.VK_I;

	/**
	 * Constant for the "J" key.
	 */
	public static final int KEY_J = KeyEvent.VK_J;

	/**
	 * Constant for the "K" key.
	 */
	public static final int KEY_K = KeyEvent.VK_K;

	/**
	 * Constant for the "L" key.
	 */
	public static final int KEY_L = KeyEvent.VK_L;

	/**
	 * Constant for the "M" key.
	 */
	public static final int KEY_M = KeyEvent.VK_M;

	/**
	 * Constant for the "N" key.
	 */
	public static final int KEY_N = KeyEvent.VK_N;

	/**
	 * Constant for the "O" key.
	 */
	public static final int KEY_O = KeyEvent.VK_O;

	/**
	 * Constant for the "P" key.
	 */
	public static final int KEY_P = KeyEvent.VK_P;

	/**
	 * Constant for the "Q" key.
	 */
	public static final int KEY_Q = KeyEvent.VK_Q;

	/**
	 * Constant for the "R" key.
	 */
	public static final int KEY_R = KeyEvent.VK_R;

	/**
	 * Constant for the "S" key.
	 */
	public static final int KEY_S = KeyEvent.VK_S;

	/**
	 * Constant for the "T" key.
	 */
	public static final int KEY_T = KeyEvent.VK_T;

	/**
	 * Constant for the "U" key.
	 */
	public static final int KEY_U = KeyEvent.VK_U;

	/**
	 * Constant for the "V" key.
	 */
	public static final int KEY_V = KeyEvent.VK_V;

	/**
	 * Constant for the "W" key.
	 */
	public static final int KEY_W = KeyEvent.VK_W;

	/**
	 * Constant for the "X" key.
	 */
	public static final int KEY_X = KeyEvent.VK_X;

	/**
	 * Constant for the "Y" key.
	 */
	public static final int KEY_Y = KeyEvent.VK_Y;

	/**
	 * Constant for the "Z" key.
	 */
	public static final int KEY_Z = KeyEvent.VK_Z;

}
