package schule.ngb.zm;

/**
 * Repräsentiert eine Farbe in der Zeichenmaschine.
 * <p>
 * Farben bestehen entweder aus einem Grauwert (zwischen <code>0</code> und
 * <code>255</code>) oder einem Rot-, Grün- und Blauanteil (jeweils zwischen
 * <code>0</code> und <code>255</code>).
 * <p>
 * Eine Farbe hat außerdem einen Transparenzwert zwischen <code>0</code>
 * (unsichtbar) und <code>255</code> (deckend).
 */
public class Color {

	//@formatter:off
	public static final Color BLACK = new Color(java.awt.Color.BLACK);
	public static final Color WHITE = new Color(java.awt.Color.WHITE);
	public static final Color GRAY = new Color(java.awt.Color.GRAY);
	public static final Color DARKGRAY = new Color(java.awt.Color.DARK_GRAY);
	public static final Color LIGHTGRAY = new Color(java.awt.Color.LIGHT_GRAY);

	public static final Color RED = new Color(java.awt.Color.RED);
	public static final Color GREEN = new Color(java.awt.Color.GREEN);
	public static final Color BLUE = new Color(java.awt.Color.BLUE);
	public static final Color YELLOW = new Color(java.awt.Color.YELLOW);
	public static final Color ORANGE = new Color(java.awt.Color.ORANGE);
	public static final Color CYAN = new Color(java.awt.Color.CYAN);
	public static final Color MAGENTA = new Color(java.awt.Color.MAGENTA);
	public static final Color PINK = new Color(java.awt.Color.PINK);

	public static final Color HGGREEN = new Color(0, 165, 81);
	public static final Color HGRED = new Color(151, 54, 60);
	//@formatter:on

	/**
	 * RGBA Wert der Farbe als Integer kodiert.
	 */
	private int rgba;

	/**
	 * Erstellt eine leere (schwarze) Farbe.
	 */
	public Color() {
		rgba = 0xFF000000;
	}

	/**
	 * Erstellt eine graue Farbe entsprechend des Grauwertes <var>gray</var>.
	 *
	 * @param gray Ein Grauwert zwischen <code>0</code> und <code>255</code>.
	 */
	public Color( int gray ) {
		this(gray, gray, gray, 255);
	}

	/**
	 * Erstellt eine graue Farbe entsprechend des Grauwertes <var>gray</var> und
	 * des Transparentwertes <var>alpha</var>.
	 *
	 * @param gray Ein Grauwert zwischen <code>0</code> und <code>255</code>.
	 */
	public Color( int gray, int alpha ) {
		this(gray, gray, gray, alpha);
	}

	/**
	 * Erstellt eine Farbe. Die Parameter <var>red</var>, <var>green</var> und
	 * <var>blue</var> geben die Rot-, Grün- und Blauanteile der Farbe. Die Werte
	 * liegen zwischen <code>0</code> und <code>255</code>.
	 *
	 * @param red   Rotwert zwischen <code>0</code> und <code>255</code>.
	 * @param green Grünwert zwischen <code>0</code> und <code>255</code>.
	 * @param blue  Blauwert zwischen <code>0</code> und <code>255</code>.
	 * @return Ein passendes Farbobjekt.
	 */
	public Color( int red, int green, int blue ) {
		this(red, green, blue, 255);
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
	 * @param red   Rotwert zwischen <code>0</code> und <code>255</code>.
	 * @param green Grünwert zwischen <code>0</code> und <code>255</code>.
	 * @param blue  Blauwert zwischen <code>0</code> und <code>255</code>.
	 * @param alpha Transparentwert zwischen <code>0</code> und <code>255</code>.
	 * @return Ein passendes Farbobjekt.
	 */
	public Color( int red, int green, int blue, int alpha ) {
		rgba = (alpha << 24) | (red << 16) | (green << 8) | blue;
	}

	/**
	 * Erstellt eine Farbe als Kopie von <var>color</var>.
	 *
	 * @param color
	 */
	public Color( Color color ) {
		this(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	/**
	 * Erstellt eine Farbe als Kopie von <var>color</var> und ersetzt den
	 * Transparentwert durch <var>alpha</var>.
	 *
	 * @param color
	 * @param alpha
	 */
	public Color( Color color, int alpha ) {
		this(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}

	public Color( java.awt.Color color ) {
		this(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	public Color( java.awt.Color color, int alpha ) {
		this(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}

	/**
	 * Erzeugt eine Farbe aus einem kodierten RGBA Integer-Wert.
	 *
	 * @param rgba
	 * @return
	 */
	public static Color parseRGB( int rgba ) {
		Color c = new Color();
		c.rgba = rgba;
		return c;
	}

	/**
	 * Erzeugt eine Farbe aus einem hexadezimalen Code. Der Hexcode kann
	 * sechs- oder achtstellig sein (wenn ein Transparentwert vorhanden ist).
	 * Dem Code kann ein <code>#</code> Zeichen vorangestellt sein.
	 *
	 * @param hexcode
	 * @return
	 */
	public static Color parseHexcode( String hexcode ) {
		if( hexcode.startsWith("#") ) {
			hexcode = hexcode.substring(1);
		}
		if( hexcode.length() != 3 && hexcode.length() != 6 && hexcode.length() != 8 ) {
			throw new IllegalArgumentException("color hexcodes have to be 3, 6 or 8 digits long, got " + hexcode.length());
		}

		// normalize hexcode to 8 digits
		int alpha = 255;
		if( hexcode.length() == 3 ) {
			hexcode = "" + hexcode.charAt(0) + hexcode.charAt(0) + hexcode.charAt(1) + hexcode.charAt(1) + hexcode.charAt(2) + hexcode.charAt(2);
		} else if( hexcode.length() == 8 ) {
			alpha = Integer.valueOf(hexcode.substring(6, 8), 16);
			hexcode = hexcode.substring(0, 6);
		} else {
			hexcode = hexcode;
		}

		Color c = new Color();
		c.rgba = (alpha << 24) | Integer.valueOf(hexcode, 16);
		return c;
	}

	public static Color morph( java.awt.Color pFarbe1, java.awt.Color pFarbe2, double pFactor ) {
		if( pFactor < 0.0 || pFarbe2 == null ) {
			return new Color(pFarbe1);
		}
		if( pFactor > 1.0 || pFarbe1 == null ) return new Color(pFarbe2);
		double pFactorInv = 1 - pFactor;
		return new Color((int) (pFactorInv * pFarbe1.getRed() + pFactor * pFarbe2.getRed()), (int) (pFactorInv * pFarbe1.getGreen() + pFactor * pFarbe2.getGreen()), (int) (pFactorInv * pFarbe1.getBlue() + pFactor * pFarbe2.getBlue()), (int) (pFactorInv * pFarbe1.getAlpha() + pFactor * pFarbe2.getAlpha()));
	}

	public static float[] RGBtoHSL( int rgb, float[] hsl ) {
		float r = ((rgb >> 16) & 255) / 255.0f;
		float g = ((rgb >> 8) & 255) / 255.0f;
		float b = (rgb & 255) / 255.0f;
		float max = Math.max(Math.max(r, g), b);
		float min = Math.min(Math.min(r, g), b);
		float c = max - min;

		if( hsl == null ) {
			hsl = new float[3];
		}

		float h_ = 0.f;
		if( c == 0 ) {
			h_ = 0;
		} else if( max == r ) {
			h_ = (float) (g - b) / c;
			if( h_ < 0 ) h_ += 6.f;
		} else if( max == g ) {
			h_ = (float) (b - r) / c + 2.f;
		} else if( max == b ) {
			h_ = (float) (r - g) / c + 4.f;
		}
		float h = 60.f * h_;

		float l = (max + min) * 0.5f;

		float s;
		if( c == 0 ) {
			s = 0.f;
		} else {
			s = c / (1 - Math.abs(2.f * l - 1.f));
		}

		hsl[0] = h;
		hsl[1] = s;
		hsl[2] = l;
		return hsl;
	}

	public static int HSLtoRGB( float[] hsl ) {
		return HSLtoRGB(hsl, 255);
	}

	public static int HSLtoRGB( float[] hsl, int alpha ) {
		float h = hsl[0];
		float s = hsl[1];
		float l = hsl[2];

		float c = (1 - Math.abs(2.f * l - 1.f)) * s;
		float h_ = h / 60.f;
		float h_mod2 = h_;
		if( h_mod2 >= 4.f ) h_mod2 -= 4.f;
		else if( h_mod2 >= 2.f ) h_mod2 -= 2.f;

		float x = c * (1 - Math.abs(h_mod2 - 1));
		float r_, g_, b_;
		if( h_ < 1 ) {
			r_ = c;
			g_ = x;
			b_ = 0;
		} else if( h_ < 2 ) {
			r_ = x;
			g_ = c;
			b_ = 0;
		} else if( h_ < 3 ) {
			r_ = 0;
			g_ = c;
			b_ = x;
		} else if( h_ < 4 ) {
			r_ = 0;
			g_ = x;
			b_ = c;
		} else if( h_ < 5 ) {
			r_ = x;
			g_ = 0;
			b_ = c;
		} else {
			r_ = c;
			g_ = 0;
			b_ = x;
		}

		float m = l - (0.5f * c);
		int r = (int) ((r_ + m) * (255.f) + 0.5f);
		int g = (int) ((g_ + m) * (255.f) + 0.5f);
		int b = (int) ((b_ + m) * (255.f) + 0.5f);
		return (alpha & 255) << 24 | r << 16 | g << 8 | b;
	}

	/*public void setRed( int red ) {
		rgba = (red << 16) | (0xFF00FFFF & rgba);
	}*/

	public Color copy() {
		return new Color(this);
	}

	/*public void setGreen( int green ) {
		rgba = (green << 8) | (0xFFFF00FF & rgba);
	}*/

	public int getRGBA() {
		return rgba;
	}

	/*public void setBlue( int blue ) {
		rgba = blue | (0xFFFFFF00 & rgba);
	}*/

	public int getRed() {
		return (rgba >> 16) & 255;
	}

	/*public void setAlpha( int alpha ) {
		rgba = (alpha << 24) | (0x00FFFFFF & rgba);
	}*/

	public int getGreen() {
		return (rgba >> 8) & 255;
	}

	public int getBlue() {
		return rgba & 255;
	}

	public int getAlpha() {
		return (rgba >> 24) & 255;
	}

	public java.awt.Color getColor() {
		return new java.awt.Color(rgba, true);
	}

	@Override
	public boolean equals( Object po ) {
		if( this == po ) return true;
		if( po == null || getClass() != po.getClass() ) return false;
		Color ppColor = (Color) po;
		return rgba == ppColor.rgba;
	}

	@Override
	public String toString() {
		return getClass().getCanonicalName() + '[' + "r=" + getRed() + ',' + "g=" + getGreen() + ',' + "b=" + getBlue() + ',' + "a=" + getAlpha() + ']';
	}

	public Color brighter() {
		return brighter(30);
	}

	public Color brighter( int percent ) {
		float[] hsl = RGBtoHSL(rgba, null);
		hsl[2] = Math.min(Math.max(hsl[2] * (1.0f + percent / 100.0f), 0.0f), 1.0f);
		return Color.parseRGB(HSLtoRGB(hsl, getAlpha()));
	}

	public Color darker() {
		return darker(30);
	}

	public Color darker( int percent ) {
		float[] hsl = RGBtoHSL(rgba, null);
		hsl[2] = Math.min(Math.max(hsl[2] * (1.0f - percent / 100.0f), 0.0f), 1.0f);
		return Color.parseRGB(HSLtoRGB(hsl, getAlpha()));
	}

}
