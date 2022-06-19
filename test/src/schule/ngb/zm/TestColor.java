package schule.ngb.zm;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestColor {

	@Test
	public void colors() {
		Color c;

		c = new Color();
		assertEquals(0, c.getRed());
		assertEquals(0, c.getGreen());
		assertEquals(0, c.getBlue());
		assertEquals(255, c.getAlpha());

		c = Color.BLUE;
		assertEquals(0, c.getRed());
		assertEquals(0, c.getGreen());
		assertEquals(255, c.getBlue());
		assertEquals(255, c.getAlpha());

		c = new Color(50, 133, 64, 33);
		assertEquals(50, c.getRed());
		assertEquals(133, c.getGreen());
		assertEquals(64, c.getBlue());
		assertEquals(33, c.getAlpha());

		c = new Color(255, 0, 0);
		assertEquals(Color.RED, c);

		c = new Color(33, 50);
		assertEquals(33, c.getRed());
		assertEquals(33, c.getGreen());
		assertEquals(33, c.getBlue());
		assertEquals(50, c.getAlpha());
	}

	@Test
	public void parseColors() {
		Color c;

		c = Color.getRGBColor(0x00FF00FF);
		assertEquals(0x00FF00FF, c.getRGBA());
		assertEquals(255, c.getRed());
		assertEquals(0, c.getGreen());
		assertEquals(255, c.getBlue());
		assertEquals(0, c.getAlpha());

		c = Color.getRGBColor(0x33FF3333);
		assertEquals(0x33FF3333, c.getRGBA());
		assertEquals(255, c.getRed());
		assertEquals(51, c.getGreen());
		assertEquals(51, c.getBlue());
		assertEquals(51, c.getAlpha());

		c = Color.parseHexcode("FF00FF");
		assertEquals(0xFFFF00FF, c.getRGBA());
		assertEquals(255, c.getRed());
		assertEquals(0, c.getGreen());
		assertEquals(255, c.getBlue());
		assertEquals(255, c.getAlpha());

		c = Color.parseHexcode("#FF00FF00");
		assertEquals(0x00FF00FF, c.getRGBA());
		assertEquals(255, c.getRed());
		assertEquals(0, c.getGreen());
		assertEquals(255, c.getBlue());
		assertEquals(0, c.getAlpha());

		c = Color.parseHexcode("#333");
		assertEquals(0xFF333333, c.getRGBA());
		assertEquals(51, c.getRed());
		assertEquals(51, c.getGreen());
		assertEquals(51, c.getBlue());
		assertEquals(255, c.getAlpha());

		c = Color.parseHexcode("#33FF0033");
		assertEquals(0x3333FF00, c.getRGBA());
		assertEquals(51, c.getRed());
		assertEquals(255, c.getGreen());
		assertEquals(0, c.getBlue());
		assertEquals(51, c.getAlpha());
	}

	@Test
	public void hsl() {
		Color c;
		float[] hsl;

		c = Color.RED;
		hsl = Color.RGBtoHSL(c.getRGBA(), null);
		assertArrayEquals(new float[]{0f,1f,.5f}, hsl, 0.0001f);

		c = new Color(255, 33, 64);
		hsl = Color.RGBtoHSL(c.getRGBA(), null);
		assertEquals(352, hsl[0], 1.0f);
		assertEquals(1.0f, hsl[1], .0001f);
		assertEquals(.5647f, hsl[2], .0001f);
	}

	public static void main( String[] args ) {
		new ColorPalette();
	}

	static class ColorPalette extends Zeichenmaschine {
		public static final int SIZE = 10, COLORS = 16;

		public void setup() {
			setSize(SIZE*COLORS, SIZE*COLORS);
			setTitle("Colors");

			drawing.noStroke();
			drawing.setAnchor(NORTHWEST);

			int steps = (int) (255.0/COLORS);
			Color c;
			for( int i = 0; i < COLORS; i++ ) {
				for( int j = 0; j < COLORS; j++ ) {
					c = new Color(i * steps, j * steps, (i+j)/2 * steps);
					drawing.setFillColor(c);
					drawing.rect(i*SIZE, j*SIZE, SIZE, SIZE);
				}
			}
		}

		public void draw() {
			Color c = Color.HGGREEN;
			drawing.setFillColor(c);
			drawing.rect(0, 0, width/2, height);

			for( int p = 10; p < 100; p += 10 ) {
				drawing.setFillColor(c.brighter(p));
				drawing.rect(width / 2, 0, width / 2, height / 2);
				drawing.setFillColor(c.darker(p));
				drawing.rect(width / 2, height / 2, width / 2, height / 2);

				delay(1000);
			}
		}
	}

}
