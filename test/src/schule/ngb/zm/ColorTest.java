package schule.ngb.zm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {


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

		assertEquals(Color.YELLOW, java.awt.Color.YELLOW);
		assertNotEquals(java.awt.Color.YELLOW, Color.YELLOW);
	}

	@Test
	void construct() {
		Color c;
		// Empty color is white
		c = new Color();
		assertEquals(c, Color.BLACK);
		// One arg is shade of gray
		c = new Color(255);
		assertEquals(c, Color.WHITE);
		c = new Color(0);
		assertEquals(c, Color.BLACK);
		c = new Color(64);
		assertEquals(c, Color.DARKGRAY);
		c = new Color(192);
		assertEquals(c, Color.LIGHTGRAY);
		// RGB colors
		c = new Color(0,0,0);
		assertEquals(c, Color.BLACK);
		c = new Color(255,0,0);
		assertEquals(c, Color.RED);
		c = new Color(0,255,0);
		assertEquals(c, Color.GREEN);
		c = new Color(0,0,255);
		assertEquals(c, Color.BLUE);
		// From java.awt.Color
		c = new Color(java.awt.Color.YELLOW);
		assertEquals(c, Color.YELLOW);
	}

	@Test
	void getRGBColor() {
		Color c1 = Color.getRGBColor(0xFFFF0000);
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
		assertEquals(java.awt.Color.YELLOW.getRGB(), Color.YELLOW.getRGBA());
	}

	@Test
	void getRed() {
	}

	@Test
	void getGreen() {
	}

	@Test
	void getBlue() {
	}

	@Test
	void getAlpha() {
	}

	@Test
	void getColor() {
		assertEquals(java.awt.Color.YELLOW, Color.YELLOW.getJavaColor());
		assertEquals(new java.awt.Color(255, 31, 124), new Color(255, 31, 124).getJavaColor());
	}

	@Test
	void brighter() {
	}

	@Test
	void testBrighter() {
	}

	@Test
	void darker() {
	}

	@Test
	void testDarker() {
	}

}
