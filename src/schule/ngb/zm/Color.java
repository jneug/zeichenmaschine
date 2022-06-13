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
	/**
	 * Die Farbe Schwarz (Grauwert 0).
	 */
	public static final Color BLACK = new Color(0);

	/**
	 * Die Farbe Weiß (Grauwert 255).
	 */
	public static final Color WHITE = new Color(255);

	/**
	 * Die Farbe Grau (Grauwert 128).
	 */
	public static final Color GRAY = new Color(128);

	/**
	 * Die Farbe Dunkelgrau (Grauwert 64).
	 */
	public static final Color DARKGRAY = new Color(64);

	/**
	 * Die Farbe Hellgrau (Grauwert 192).
	 */
	public static final Color LIGHTGRAY = new Color(192);

	/**
	 * Die Farbe Zeichenmaschinen-Rot.
	 */
	public static final Color RED = new Color(240, 80, 37);
	/**
	 * Die Farbe Zeichenmaschinen-Grün.
	 */
	public static final Color GREEN = new Color(98, 199, 119);
	/**
	 * Die Farbe Zeichenmaschinen-Blau.
	 */
	public static final Color BLUE = new Color(49, 197, 244);
	/**
	 * Die Farbe Zeichenmaschinen-Gelb.
	 */
	public static final Color YELLOW = new Color(248, 239, 34);
	/**
	 * Die Farbe Zeichenmaschinen-Orange.
	 */
	public static final Color ORANGE = new Color(248, 158, 80);
	/**
	 * Die Farbe Zeichenmaschinen-Türkis.
	 */
	public static final Color CYAN = new Color(java.awt.Color.CYAN);
	/**
	 * Die Farbe Zeichenmaschinen-Magenta.
	 */
	public static final Color MAGENTA = new Color(java.awt.Color.MAGENTA);
	/**
	 * Die Farbe Zeichenmaschinen-Rosa.
	 */
	public static final Color PINK = new Color(240, 99, 164);
	/**
	 * Die Farbe Zeichenmaschinen-Lila.
	 */
	public static final Color PURPLE = new Color(101, 0, 191);
	/**
	 * Die Farbe Zeichenmaschinen-Braun.
	 */
	public static final Color BROWN = new Color(148, 82, 0);

	/**
	 * Die Farbe Helmholtz-Grün.
	 */
	public static final Color HGGREEN = new Color(0, 165, 81);
	/**
	 * Die Farbe Helmholtz-Rot.
	 */
	public static final Color HGRED = new Color(151, 54, 60);
	//@formatter:on

	/**
	 * RGBA Wert der Farbe als Integer kodiert.
	 */
	private final int rgba;

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
	 * Interner Konstruktor für die Initialisierung einer Farbe mit einem
	 * RGBA-Wert.
	 *
	 * Da der Konstruktor {@link #Color(int)} schon besetzt ist, muss hier der
	 * Parameter {@code isRGBA} auf {@code true} gesetzt werden, damit {@code rgba}
	 * korrekt interpretiert wird.
	 * @param rgba RGBA-wert der Farbe.
	 * @param isRGBA Sollte immer {@code true} sein.
	 */
	Color( int rgba, boolean isRGBA ) {
		if( !isRGBA ) {
			this.rgba = (255 << 24) | (rgba << 16) | (rgba << 8) | rgba;
		} else {
			this.rgba = rgba;
		}
	}

	/**
	 * Erzeugt eine Farbe aus einem kodierten RGBA Integer-Wert.
	 *
	 * @param rgba
	 * @return
	 */
	public static Color getRGBColor( int rgba ) {
		Color c = new Color(rgba, true);
		return c;
	}

	public static Color getHSBColor(double h, double s, double b) {
		return new Color(java.awt.Color.getHSBColor((float)h, (float)s, (float)b));
	}

	public static Color getHSLColor(double h, double s, double l) {
		int rgb = Color.HSLtoRGB(new float[]{(float)h, (float)s, (float)l});
		return Color.getRGBColor(rgb);
	}

	public static Color parseString( String pColor ) {
		pColor = pColor.toLowerCase().strip();
		if( pColor.contains("red") || pColor.contains("rot") ) {
			return Color.RED.copy();
		} else if( pColor.contains("blue") || pColor.contains("blau") ) {
			return Color.BLUE.copy();
		} else if( pColor.contains("green") || pColor.contains("grün") || pColor.contains("gruen") ) {
			return Color.GREEN.copy();
		} else if( pColor.contains("yellow") || pColor.contains("gelb") ) {
			return Color.YELLOW.copy();
		} else {
			return new Color();
		}
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

		return Color.getRGBColor((alpha << 24) | Integer.valueOf(hexcode, 16));
	}

	public static Color interpolate( java.awt.Color color1, java.awt.Color color2, double t ) {
		if( t < 0.0 || color2 == null ) {
			return new Color(color1);
		}
		if( t > 1.0 || color1 == null ) {
			return new Color(color2);
		}
		double pFactorInv = 1 - t;
		return new Color((int) (pFactorInv * color1.getRed() + t * color2.getRed()), (int) (pFactorInv * color1.getGreen() + t * color2.getGreen()), (int) (pFactorInv * color1.getBlue() + t * color2.getBlue()), (int) (pFactorInv * color1.getAlpha() + t * color2.getAlpha()));
	}

	public static Color interpolate( Color color1, Color color2, double t ) {
		if( color1 == null && color2 == null ) {
			throw new IllegalArgumentException("Color.interpolate() needs at least one color to be not null.");
		}
		if( t < 0.0 || color2 == null ) {
			return color1.copy();
		}
		if( t > 1.0 || color1 == null ) {
			return color2.copy();
		}
		double pFactorInv = 1 - t;
		return new Color((int) (pFactorInv * color1.getRed() + t * color2.getRed()), (int) (pFactorInv * color1.getGreen() + t * color2.getGreen()), (int) (pFactorInv * color1.getBlue() + t * color2.getBlue()), (int) (pFactorInv * color1.getAlpha() + t * color2.getAlpha()));
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

	/**
	 * Konvertiert eine Farbe mit Komponenten im HSL-Farbraum in den
	 * RGB-Farbraum.
	 *
	 * Die Farbkomponenten werden als Float-Array übergeben. Im Index 0 steht
	 * der h-Wert im Bereich 0 bis 360, Index 1 und 2 enthalten den s- und
	 * l-Wert im Bereich von 0 bis 1.
	 * @param hsl Die Farbkomponenten im HSL-Farbraum.
	 * @param alpha Ein Transparenzwert im Bereich 0 bis 255.
	 * @return Der RGBA-Wert der Farbe.
	 */
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

	/**
	 * Erzeugt eine Kopie dieser Farbe.
	 * @return Ein neues Farbobjekt.
	 */
	public Color copy() {
		return new Color(this);
	}

	/**
	 * Gibt den RGBA-Wert dieser Farbe zurück.
	 *
	 * Eine Farbe wird als 32-Bit Integer gespeichert. Bits 24-31 enthalten
	 * den Transparenzwert, 16-23 den Rotwert, 8-15 den Grünwert und 0-7 den
	 * Blauwert der Farbe.
	 * @return Der RGBA-Wert der Farbe.
	 * @see #getRed()
	 * @see #getGreen()
	 * @see #getBlue()
	 * @see #getAlpha()
	 */
	public int getRGBA() {
		return rgba;
	}

	/**
	 * Gibt den Rotwert dieser Farbe zurück.
	 * @return Der Rotwert der Farbe zwischen 0 und 255.
	 */
	public int getRed() {
		return (rgba >> 16) & 255;
	}

	/**
	 * Gibt den Grünwert dieser Farbe zurück.
	 * @return Der Grünwert der Farbe zwischen 0 und 255.
	 */
	public int getGreen() {
		return (rgba >> 8) & 255;
	}

	/**
	 * Gibt den Blauwert dieser Farbe zurück.
	 * @return Der Blauwert der Farbe zwischen 0 und 255.
	 */
	public int getBlue() {
		return rgba & 255;
	}

	/**
	 * Gibt den Transparenzwert dieser Farbe zurück.
	 * @return Der Transparenzwert der Farbe zwischen 0 und 255.
	 */
	public int getAlpha() {
		return (rgba >> 24) & 255;
	}

	/**
	 * Erzeugt ein {@link java.awt.Color}-Objekt aus dieser Farbe.
	 *
	 * Das erzeugte Farbobjekt hat dieselben Rot-, Grün-, Blau-
	 * und Transparenzwerte wie diese Farbe.
	 * @return Ein Java-Farbobjekt.
	 */
	public java.awt.Color getJavaColor() {
		return new java.awt.Color(rgba, true);
	}

	@Override
	/**
	 * Prüft, ob ein anderes Objekt zu diesem gleich ist.
	 *
	 * Die Methode gibt genau dann {@code true} zurück, wenn das andere
	 * Objekt nicht {@code null} ist, vom Typ {@code Color} ist und es
	 * dieselben Rot-, Grün-, Blau- und Transparenzwerte hat.
	 * @param obj Das zu vergleichende Objekt.
	 * @return {@code true}, wenn die Objekte gleich sind, sonst {@code false}.
	 */
	public boolean equals( Object obj ) {
		return obj instanceof Color && ((Color)obj).getRGBA() == this.rgba;
	}

	/**
	 * Erzeugt einen Text-String, der diese Farbe beschreibt.
	 * @return Eine Textversion der Farbe.
	 */
	@Override
	public String toString() {
		return getClass().getCanonicalName() + '[' + "r=" + getRed() + ',' + "g=" + getGreen() + ',' + "b=" + getBlue() + ',' + "a=" + getAlpha() + ']';
	}

	/**
	 * Berechnet einen Hashcode für dieses Farbobjekt.
	 * @return Ein Hashcode für diese Rabe.
	 */
	@Override
	public int hashCode() {
		return rgba;
	}

	/**
	 *  Erzeugt eine um 30% hellere Version dieser Farbe.
	 * @return Ein Farbobjekt mit einer helleren Farbe.
	 */
	public Color brighter() {
		return brighter(30);
	}

	/**
	 * Erzeugt eine um {@code percent} hellere Version dieser Farbe.
	 * @param percent Eine Prozentzahl zwischen 0 und 100.
	 * @return Ein Farbobjekt mit einer helleren Farbe.
	 */
	public Color brighter( int percent ) {
		float[] hsl = RGBtoHSL(rgba, null);
		hsl[2] = Math.min(Math.max(hsl[2] * (1.0f + percent / 100.0f), 0.0f), 1.0f);
		return Color.getRGBColor(HSLtoRGB(hsl, getAlpha()));
	}

	/**
	 * Erzeugt eine um 30% dunklere Version dieser Farbe.
	 * @return Ein Farbobjekt mit einer dunkleren Farbe.
	 */
	public Color darker() {
		return darker(30);
	}

	/**
	 * Erzeugt eine um {@code percent} dunklere Version dieser Farbe.
	 * @param percent Eine Prozentzahl zwischen 0 und 100.
	 * @return Ein Farbobjekt mit einer dunkleren Farbe.
	 */
	public Color darker( int percent ) {
		float[] hsl = RGBtoHSL(rgba, null);
		hsl[2] = Math.min(Math.max(hsl[2] * (1.0f - percent / 100.0f), 0.0f), 1.0f);
		return Color.getRGBColor(HSLtoRGB(hsl, getAlpha()));
	}

}
