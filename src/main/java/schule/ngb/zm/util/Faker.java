package schule.ngb.zm.util;

import schule.ngb.zm.Color;
import schule.ngb.zm.Constants;
import schule.ngb.zm.Zeichenmaschine;
import schule.ngb.zm.util.io.FileLoader;
import schule.ngb.zm.util.io.FontLoader;
import schule.ngb.zm.util.io.ImageLoader;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public final class Faker {

	public static final String FAKE_IMG_URL = "https://loremflickr.com/%d/%d";



	public static void main( String[] args ) {
		Zeichenmaschine zm = new Zeichenmaschine(640, 480, "Faker");
		zm.getDrawingLayer().setAnchor(Constants.NORTHWEST);
		zm.getDrawingLayer().image(Faker.fakeImage(zm.getWidth(), zm.getHeight()), 0, 0);
		zm.redraw();
	}

	public static String[][] fakeUsers( int n ) {
		String[][] data = loadMockfile("users");
		String[][] result = new String[n][];
		for( int i = 0; i < n; i++ ) {
			result[i] = Constants.choice(data);
		}
		return result;
	}

	public static String[] fakeNames( int n ) {
		String[][] data = loadMockfile("users");
		String[] result = new String[n];
		for( int i = 0; i < n; i++ ) {
			String[] row = Constants.choice(data);
			result[i] = row[0];
		}
		return result;
	}

	public static String[] fakeFullnames( int n ) {
		String[][] data = loadMockfile("users");
		String[] result = new String[n];
		for( int i = 0; i < n; i++ ) {
			String[] row = Constants.choice(data);
			result[i] = row[0] + " " + row[1];
		}
		return result;
	}

	public static String[] fakeUsernames( int n ) {
		String[][] data = loadMockfile("users");
		String[] result = new String[n];
		for( int i = 0; i < n; i++ ) {
			String[] row = Constants.choice(data);
			result[i] = row[3];
		}
		return result;
	}

	public static String[] fakePasswords( int n ) {
		String[][] data = loadMockfile("users");
		String[] result = new String[n];
		for( int i = 0; i < n; i++ ) {
			String[] row = Constants.choice(data);
			result[i] = row[4];
		}
		return result;
	}

	public static String[] fakeEmails( int n ) {
		String[][] data = loadMockfile("users");
		String[] result = new String[n];
		for( int i = 0; i < n; i++ ) {
			String[] row = Constants.choice(data);
			result[i] = row[5];
		}
		return result;
	}

	public static BufferedImage fakeImage( int width, int height ) {
		return fakeImage(width, height, true);
	}

	public static BufferedImage fakeImage( int width, int height, boolean fromWeb ) {
		if( !fromWeb ) {
			BufferedImage img = ImageLoader.createImage(width, height);
			Graphics2D graphics = (Graphics2D)img.getGraphics().create();

			String text = width+" x "+height;

			Color clr = Constants.randomNiceColor();

			graphics.setBackground(clr.getJavaColor());
			graphics.clearRect(0, 0, width, height);

			graphics.setColor(clr.textcolor().getJavaColor());
			graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, (int)((width+height)*0.05)));
			FontMetrics fontMerics = graphics.getFontMetrics();
			LineMetrics lineMetrics = fontMerics.getLineMetrics(text, graphics);
			graphics.drawString(text,
				(int)((width - fontMerics.stringWidth(text))/2),
				(int)(height/2 - lineMetrics.getDescent() + lineMetrics.getAscent()/2)
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
		return FileLoader.loadCsv("schule/ngb/zm/util/mock-"+name+".csv", true);
	}

}
