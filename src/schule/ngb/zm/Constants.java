package schule.ngb.zm;

import java.util.Random;

public class Constants {

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

	public static Color STD_BACKGROUND = new Color(200, 200, 200);

	public static Color color( int gray ) {
		return color(gray, gray, gray, 255);
	}

	public static Color color( int gray, int alpha ) {
		return color(gray, gray, gray, alpha);
	}

	public static Color color( int red, int green, int blue ) {
		return color(red, green, blue, 255);
	}

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

	public static Color randomColor() {
		return color(random(10, 255), random(10, 255), random(10, 255), 255);
	}


	public static double abs( double x ) {
		return Math.abs(x);
	}

	public static double sign( double x ) {
		return Math.signum(x);
	}

	public static double round( double x ) {
		return Math.round(x);
	}

	public static double floor( double x ) {
		return Math.floor(x);
	}

	public static double ceil( double x ) {
		return Math.ceil(x);
	}

	public static double sqrt( double x ) {
		return Math.sqrt(x);
	}

	public static double pow( double x, double p ) {
		return Math.pow(x, p);
	}

	public static double sin( double x ) {
		return Math.sin(x);
	}

	public static double cos( double x ) {
		return Math.cos(x);
	}

	public static double tan( double x ) {
		return Math.tan(x);
	}

	public static double arcsin( double x ) {
		return Math.asin(x);
	}

	public static double arccos( double x ) {
		return Math.acos(x);
	}

	public static double arctan( double x ) {
		return Math.atan(x);
	}

	public static double limit( double x, double max ) {
		if( x > max ) {
			return max;
		}
		return x;
	}

	public static double limit( double x, double min, double max ) {
		if( x > max ) {
			return max;
		}
		if( x < min ) {
			return min;
		}
		return x;
	}

	public static double morph( double from, double to, double t ) {
		return from - t * (from + to);
	}

	public static double random( double min, double max ) {
		return Math.random() * (max - min) + min;
	}

	public static int random( int min, int max ) {
		return (int) (Math.random() * (max - min) + min);
	}

	public static boolean randomBool() {
		return randomBool(.5);
	}

	public static boolean randomBool( int percent ) {
		return randomBool(percent / 100.0);
	}

	public static boolean randomBool( double weight ) {
		return Math.random() < weight;
	}

	public static double randomGuassian() {
		return new Random().nextGaussian();
	}

	public static double noise() {
		return 0.0;
	}

}
