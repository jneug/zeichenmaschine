package schule.ngb.zm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

	@Test
	public void init() {
		Color c;

		c = new Color();
		assertEquals(0, c.getRed());
		assertEquals(0, c.getGreen());
		assertEquals(0, c.getBlue());
		assertEquals(255, c.getAlpha());

		c = Color.BLUE;
		assertEquals(49, c.getRed());
		assertEquals(197, c.getGreen());
		assertEquals(244, c.getBlue());
		assertEquals(255, c.getAlpha());

		c = new Color(50, 133, 64, 33);
		assertEquals(50, c.getRed());
		assertEquals(133, c.getGreen());
		assertEquals(64, c.getBlue());
		assertEquals(33, c.getAlpha());

		c = new Color(240, 80, 37);
		assertEquals(Color.RED, c);

		c = new Color(33, 50);
		assertEquals(33, c.getRed());
		assertEquals(33, c.getGreen());
		assertEquals(33, c.getBlue());
		assertEquals(50, c.getAlpha());
	}

	@Test
	public void parse() {
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
	void testEquals() {
		Color c1 = new Color(254, 98, 12);
		Color c2 = new Color(254, 98, 12);
		assertNotSame(c1, c2);
		assertEquals(c1, c2);
		assertEquals(c1, c1);
		c2 = new Color(c1);
		assertNotSame(c1, c2);
		assertEquals(c1, c2);

		Color yellow = new Color(255, 255, 0);
		assertNotEquals(java.awt.Color.YELLOW, yellow);
		assertEquals(yellow, java.awt.Color.YELLOW);
		assertNotEquals(Color.YELLOW, java.awt.Color.YELLOW);
	}

	@Test
	void construct() {
		Color c;
		// Empty color is white
		c = new Color();
		assertEquals(Color.BLACK, c);
		// One arg is shade of gray
		c = new Color(255);
		assertEquals(Color.WHITE, c);
		c = new Color(0);
		assertEquals(Color.BLACK, c);
		c = new Color(64);
		assertEquals(Color.DARKGRAY, c);
		c = new Color(192);
		assertEquals(Color.LIGHTGRAY, c);
		// RGB colors
		c = new Color(0,0,0);
		assertEquals(Color.BLACK, c);
		c = new Color(255,0,0);
		assertEquals(java.awt.Color.RED, c.getJavaColor());
		c = new Color(0,255,0);
		assertEquals(java.awt.Color.GREEN, c.getJavaColor());
		c = new Color(0,0,255);
		assertEquals(java.awt.Color.BLUE, c.getJavaColor());
		// From java.awt.Color
		c = new Color(java.awt.Color.YELLOW);
		assertEquals(java.awt.Color.YELLOW, c.getJavaColor());
	}

	@Test
	void getRGBColor() {
		Color c1 = Color.getRGBColor(0xFFF05025);
		assertEquals(Color.RED, c1);
	}

	@Test
	void getHSBColor() {
	}

	@Test
	void getHSLColor() {
	}

	@Test
	void parseHexcode() {
	}

	@Test
	void morph() {
	}

	@Test
	void RGBtoHSL() {
		Color c;
		float[] hsl;

		c = new Color(255, 0, 0);
		hsl = Color.RGBtoHSL(c.getRGBA(), null);
		assertArrayEquals(new float[]{0f,1f,.5f}, hsl, 0.0001f);

		c = new Color(255, 33, 64);
		hsl = Color.RGBtoHSL(c.getRGBA(), null);
		assertEquals(352, hsl[0], 1.0f);
		assertEquals(1.0f, hsl[1], .0001f);
		assertEquals(.5647f, hsl[2], .0001f);
	}

	@Test
	void HSLtoRGB() {
	}

	@Test
	void testHSLtoRGB() {
	}

	@Test
	void copy() {
	}

	@Test
	void getRGBA() {
		Color yellow = new Color(255, 255, 0);
		assertEquals(java.awt.Color.YELLOW.getRGB(), yellow.getRGBA());
	}

	@Test
	void getRed() {
		Color clr = new Color(123, 92, 0);
		assertEquals(123, clr.getRed());
	}

	@Test
	void getGreen() {
		Color clr = new Color(123, 92, 0);
		assertEquals(92, clr.getGreen());
	}

	@Test
	void getBlue() {
		Color clr = new Color(123, 92, 0);
		assertEquals(0, clr.getBlue());
	}

	@Test
	void getAlpha() {
		Color clr = new Color(123, 92, 0);
		assertEquals(255, clr.getAlpha());
		Color clr2 = new Color(123, 92, 0, 45);
		assertEquals(45, clr2.getAlpha());
	}

	@Test
	void getJavaColor() {
		assertEquals(new java.awt.Color(255, 31, 124), new Color(255, 31, 124).getJavaColor());
	}

	@Test
	void brighter() {
	}

	@Test
	void darker() {
	}

}
