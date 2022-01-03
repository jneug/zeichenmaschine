package schule.ngb.zm;

import java.util.Random;

public class Constants {

	//@formatter:off
	public static final String APP_NAME = "Zeichenmaschine";

	public static final int APP_VERSION_MAJ = 0;
	public static final int APP_VERSION_MIN = 1;
	public static final int APP_VERSION_REV = 5;

	public static final String APP_VERSION = APP_VERSION_MAJ + "." + APP_VERSION_MIN + "." + APP_VERSION_REV;


	public static final int STD_WIDTH = 400;
	public static final int STD_HEIGHT = 400;
	public static final int STD_FPS = 60;

	public static final Color STD_FILLCOLOR = Color.WHITE;
	public static final Color STD_STROKECOLOR = Color.BLACK;
	public static final double STD_STROKEWEIGHT = 1.0;
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

	public static final Color BLACK = Color.BLACK;
	public static final Color WHITE = Color.WHITE;
	public static final Color RED = Color.RED;
	public static final Color BLUE = Color.BLUE;
	public static final Color GREEN = Color.GREEN;
	public static final Color YELLOW = Color.YELLOW;

	public static final Color STD_BACKGROUND = new Color(200, 200, 200);
	//@formatter:on


	// Farben
	/**
	 * Erstellt eine graue Farbe. Der Parameter <var>gray</var> gibt einen
	 * Grauwert zwischen <code>0</code> und <code>255</code> an, wobei
	 * <code>0</code> schwarz und <code>255</code> weiß ist.
	 *
	 * @param gray Grauwert zwischen <code>0</code> und <code>255</code>.
	 * @return Ein passendes Farbobjekt.
	 */
	public static Color color( int gray ) {
		return color(gray, gray, gray, 255);
	}

	/**
	 * Erstellt eine graue Farbe. Der Parameter <var>gray</var> gibt einen
	 * Grauwert zwischen <code>0</code> und <code>255</code> an, wobei
	 * <code>0</code> schwarz und <code>255</code> weiß ist.
	 * <var>alpha</var> gibt den den Transparentwert an (auch zwischen
	 * <code>0</code> und <code>255</code>), wobei
	 * <code>0</code> komplett durchsichtig ist und <code>255</code> komplett
	 * deckend.
	 *
	 * @param gray Grauwert zwischen <code>0</code> und <code>255</code>.
	 * @param alpha Transparentwert zwischen <code>0</code> und <code>255</code>.
	 * @return Ein passendes Farbobjekt.
	 */
	public static Color color( int gray, int alpha ) {
		return color(gray, gray, gray, alpha);
	}

	/**
	 * Erstellt eine Farbe. Die Parameter <var>red</var>, <var>green</var> und
	 * <var>blue</var> geben die Rot-, Grün- und Blauanteile der Farbe. Die Werte
	 * liegen zwischen <code>0</code> und <code>255</code>.
	 *
	 * @param red Rotwert zwischen <code>0</code> und <code>255</code>.
	 * @param green Grünwert zwischen <code>0</code> und <code>255</code>.
	 * @param blue Blauwert zwischen <code>0</code> und <code>255</code>.
	 * @return Ein passendes Farbobjekt.
	 */
	public static Color color( int red, int green, int blue ) {
		return color(red, green, blue, 255);
	}

	/**
	 * Erstellt eine Farbe. Die Parameter <var>red</var>, <var>green</var> und
	 * <var>blue</var> geben die Rot-, Grün- und Blauanteile der Farbe. Die Werte
	 * liegen zwischen <code>0</code> und <code>255</code>.
	 * <var>alpha</var> gibt den den Transparentwert an (auch zwischen
	 * code>0</code> und <code>255</code>), wobei
	 * <code>0</code> komplett durchsichtig ist und <code>255</code> komplett
	 * deckend.
	 *
	 * @param red Rotwert zwischen <code>0</code> und <code>255</code>.
	 * @param green Grünwert zwischen <code>0</code> und <code>255</code>.
	 * @param blue Blauwert zwischen <code>0</code> und <code>255</code>.
	 * @param alpha Transparentwert zwischen <code>0</code> und <code>255</code>.
	 * @return Ein passendes Farbobjekt.
	 */
	public static Color color( int red, int green, int blue, int alpha ) {
		if( red < 0 || red >= 256 )
			throw new IllegalArgumentException("red must be between 0 and 255");
		if( green < 0 || green >= 256 )
			throw new IllegalArgumentException("green must be between 0 and 255");
		if( blue < 0 || blue >= 256 )
			throw new IllegalArgumentException("blue must be between 0 and 255");
		if( alpha < 0 || alpha >= 256 )
			throw new IllegalArgumentException("alpha must be between 0 and 255");

		return new Color(red, green, blue, alpha);
	}

	/**
	 * Erzeugt eine zufällige Farbe.
	 * @return Ein zufälliges Farbobjekt.
	 */
	public static Color randomColor() {
		return color(random(10, 255), random(10, 255), random(10, 255), 255);
	}


	// Mathematische Funktionen

	/**
	 * Ermittelt den Absolutbetrag der Zahl <var>x</var>.
	 * @param x Eine Zahl.
	 * @return Der Absolutbetrag.
	 */
	public static double abs( double x ) {
		return Math.abs(x);
	}

	/**
	 * Ermittelt das Vorzeichen der Zahl <var>x</var>.
	 * @param x Eine Zahl.
	 * @return <code>-1</code>, <code>1</code> oder <code>0</code>.
	 */
	public static double sign( double x ) {
		return Math.signum(x);
	}

	/**
	 * Rundet die Zahl <var>x</var>.
	 * @param x Eine Zahl.
	 * @return Die gerundete Zahl.
	 */
	public static double round( double x ) {
		return Math.round(x);
	}

	/**
	 * Rundet die Zahl <var>x</var> ab.
	 * @param x Eine Zahl.
	 * @return Die abgerundete Zahl.
	 */
	public static double floor( double x ) {
		return Math.floor(x);
	}

	/**
	 * Rundet die Zahl <var>x</var> auf.
	 * @param x Eine Zahl.
	 * @return Die aufgerundete Zahl.
	 */
	public static double ceil( double x ) {
		return Math.ceil(x);
	}

	/**
	 * Ermittelt die Quadratwurzel der Zahl <var>x</var>.
	 * @param x Eine Zahl.
	 * @return Die Quadratwurzel.
	 */
	public static double sqrt( double x ) {
		return Math.sqrt(x);
	}

	/**
	 * Ermittelt die Potenz der Zahl <var>x</var> zum Exponenten <var>p</var>.
	 * @param x Eine Zahl.
	 * @param p Der Exponent.
	 * @return <var>x</var> hoch <var>p</var>.
	 */
	public static double pow( double x, double p ) {
		return Math.pow(x, p);
	}

	/**
	 * Ermittelt den Sinus der Zahl <var>x</var>.
	 * @param x Eine Zahl.
	 * @return <code>sin(x)</code>.
	 */
	public static double sin( double x ) {
		return Math.sin(x);
	}

	/**
	 * Ermittelt den Kosinus der Zahl <var>x</var>.
	 * @param x Eine Zahl.
	 * @return <code>cos(x)</code>.
	 */
	public static double cos( double x ) {
		return Math.cos(x);
	}

	/**
	 * Ermittelt den Tangens der Zahl <var>x</var>.
	 * @param x Eine Zahl.
	 * @return <code>tan(x)</code>.
	 */
	public static double tan( double x ) {
		return Math.tan(x);
	}

	/**
	 * Ermittelt den Arkussinus der Zahl <var>x</var>.
	 * @param x Eine Zahl.
	 * @return <code>asin(x)</code>.
	 */
	public static double arcsin( double x ) {
		return Math.asin(x);
	}

	/**
	 * Ermittelt den Arkuskosinus der Zahl <var>x</var>.
	 * @param x Eine Zahl.
	 * @return <code>acos(x)</code>.
	 */
	public static double arccos( double x ) {
		return Math.acos(x);
	}

	/**
	 * Ermittelt den Arkusktangens der Zahl <var>x</var>.
	 * @param x Eine Zahl.
	 * @return <code>atan(x)</code>.
	 */
	public static double arctan( double x ) {
		return Math.atan(x);
	}

	/**
	 * Beschränkt die Zahl <var>x</var> auf das Intervall <code>[0, max]</code>.
	 * Liegt <var>x</var> außerhalb des Intervalls, wird eine der Grenzen
	 * zurückgegeben.
	 * @param x Eine Zahl.
	 * @param max Das Maximum des Intervalls.
	 * @return Eine Zahl im Intervall <code>[0, max]</code>.
	 */
	public static double limit( double x, double max ) {
		if( x > max ) {
			return max;
		}
		return x;
	}

	/**
	 * Beschränkt die Zahl <var>x</var> auf das Intervall <code>[min, max]</code>.
	 * Liegt <var>x</var> außerhalb des Intervalls, wird eine der Grenzen
	 * zurückgegeben.
	 * @param x Eine Zahl.
	 * @param max Das Maximum des Intervalls.
	 * @return Eine Zahl im Intervall <code>[min, max]</code>.
	 */
	public static double limit( double x, double min, double max ) {
		if( x > max ) {
			return max;
		}
		if( x < min ) {
			return min;
		}
		return x;
	}

	/**
	 * Interpoliert einen Wert zwischen <var>from</var> und <var>to</var> linear,
	 * abhängig von <var>t</var>. Das Ergebnis entspricht der Formel
	 *
	 * <pre>
	 * from - t * (from + to)
	 * </pre>
	 * @param from Startwert
	 * @param to Zielwert
	 * @param t  Wert zwischen <code>0</code> und <code>1</code>.
	 * @return Das Ergebnis der linearen Interpolation.
	 */
	public static double morph( double from, double to, double t ) {
		return from - t * (from + to);
	}

	/**
	 * Erzeugt eine Pseudozufallszahl im Intervall <code>[min, max[</code>.
	 * @param min Untere Grenze.
	 * @param max Obere Grenze.
	 * @return Eine Zufallszahl.
	 */
	public static double random( double min, double max ) {
		return Math.random() * (max - min) + min;
	}

	/**
	 * Erzeugt eine Pseudozufallsganzzahl im Intervall <code>[min, max[</code>.
	 * @param min Untere Grenze.
	 * @param max Obere Grenze.
	 * @return Eine Zufallszahl.
	 */
	public static int random( int min, int max ) {
		return (int) (Math.random() * (max - min) + min);
	}

	/**
	 * Erzeugt einen zufälligen Wahrheitswert. <code>true</code> und
	 * <code>false</code> werden mit einer Wahrscheinlichkeit von 50%
	 * erzeugt.
	 * @return Ein Wahrheitswert.
	 */
	public static boolean randomBool() {
		return randomBool(.5);
	}

	/**
	 * Erzeugt einen zufälligen Wahrheitswert. <code>true</code> wird mit
	 * der Wahrscheinlichkeit <var>percent</var> Prozent erzeugt.
	 * @param percent Eine Prozentzahl zwischen <code>0</code> und <code>100</code>.
	 * @return Ein Wahrheitswert.
	 */
	public static boolean randomBool( int percent ) {
		return randomBool(percent / 100.0);
	}

	/**
	 * Erzeugt einen zufälligen Wahrheitswert. <code>true</code> wird mit
	 * der Wahrscheinlichkeit <var>weight</var> erzeugt.
	 * @return Ein Wahrheitswert.
	 */
	public static boolean randomBool( double weight ) {
		return Math.random() < weight;
	}

	/**
	 * Erzeugt eine Pseudozufallszahl nach einer Gaussverteilung.
	 * @return Eine Zufallszahl.
	 * @see Random#nextGaussian()
	 */
	public static double randomGuassian() {
		return new Random().nextGaussian();
	}

	/**
	 * Erzeugt den nächsten Wert eines Perlin-Noise.
	 * @return
	 */
	public static double noise() {
		return 0.0;
	}

}
