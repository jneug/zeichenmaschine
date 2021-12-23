package schule.ngb.zm;

import java.awt.*;

public class Farbe extends Color {

	public static final Farbe SCHWARZ = new Farbe(Color.BLACK);
	public static final Farbe WEISS = new Farbe(Color.WHITE);
	public static final Farbe GRAU = new Farbe(Color.GRAY);
	public static final Farbe DUNKELGRAU = new Farbe(Color.DARK_GRAY);
	public static final Farbe HELLGRAU = new Farbe(Color.LIGHT_GRAY);

	public static final Farbe ROT = new Farbe(Color.RED);
	public static final Farbe GRUEN = new Farbe(Color.GREEN);
	public static final Farbe BLAU = new Farbe(Color.BLUE);
	public static final Farbe GELB = new Farbe(Color.YELLOW);
	public static final Farbe ORANGE = new Farbe(Color.ORANGE);
	public static final Farbe CYAN = new Farbe(Color.CYAN);
	public static final Farbe MAGENTA = new Farbe(Color.MAGENTA);
	public static final Farbe PINK = new Farbe(Color.PINK);

	public static final Farbe HGGRUEN = new Farbe(0, 165, 81);
	public static final Farbe HGROT = new Farbe(151, 54, 60);


	public Farbe( int pGrau ) {
		super(pGrau, pGrau, pGrau, 255);
	}

	public Farbe( int pGrau, int pAlpha ) {
		super(pGrau, pGrau, pGrau, pAlpha);
	}


	public Farbe( int pRot, int pGruen, int pBlau ) {
		super(pRot, pGruen, pBlau);
	}

	public Farbe( int pRot, int pGruen, int pBlau, int pAlpha ) {
		super(pRot, pGruen, pBlau, pAlpha);
	}

	public Farbe( Color pColor ) {
		super(pColor.getRed(), pColor.getGreen(), pColor.getBlue(), pColor.getAlpha());
	}

	public Farbe( Color pColor, int pAlpha ) {
		super(pColor.getRed(), pColor.getGreen(), pColor.getBlue(), pAlpha);
	}

	public static Farbe vonRGB( int pRGB ) {
		return new Farbe(
			(pRGB >> 16) & 255,
			(pRGB >> 8) & 255,
			pRGB & 255,
			(pRGB >> 24) & 255);
	}

	public static Farbe vonHexcode( String pHexcode ) {
		if( pHexcode.startsWith("#") ) {
			pHexcode = pHexcode.substring(1);
		}

		int red = Integer.valueOf(pHexcode.substring(0, 2), 16);
		int green = Integer.valueOf(pHexcode.substring(2, 4), 16);
		int blue = Integer.valueOf(pHexcode.substring(4, 6), 16);

		int alpha = 255;
		if( pHexcode.length() == 8 ) {
			alpha = Integer.valueOf(pHexcode.substring(6, 8), 16);
		}

		return new Farbe(red, green, blue, alpha);
	}

	public static Farbe morphen( Color pFarbe1, Color pFarbe2, double pFactor ) {
		if( pFactor < 0.0 || pFarbe2 == null ) {
			return new Farbe(pFarbe1);
		}
		if( pFactor > 1.0 || pFarbe1 == null )
			return new Farbe(pFarbe2);
		double pFactorInv = 1 - pFactor;
		return new Farbe(
			(int) (pFactorInv * pFarbe1.getRed() + pFactor * pFarbe2.getRed()),
			(int) (pFactorInv * pFarbe1.getGreen() + pFactor * pFarbe2.getGreen()),
			(int) (pFactorInv * pFarbe1.getBlue() + pFactor * pFarbe2.getBlue()),
			(int) (pFactorInv * pFarbe1.getAlpha() + pFactor * pFarbe2.getAlpha())
		);
	}

	public Farbe kopie() {
		return new Farbe(this);
	}

}
