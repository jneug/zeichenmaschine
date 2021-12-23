package schule.ngb.zm;

import schule.ngb.zm.formen.Konturform;

import java.awt.*;
import java.awt.geom.Arc2D;

public class Konstanten {

	public static final String APP_NAME = "Zeichenmaschine";

	public static final int STD_BREITE = 400;
	public static final int STD_HOEHE = 400;
	public static final int STD_FPS = 60;

	public static Color STD_HINTERGRUND = new Color(200, 200, 200);


    public static final int DURCHGEZOGEN = Konturform.DURCHGEZOGEN;
    public static final int GESTRICHELT = Konturform.GESTRICHELT;
    public static final int GEPUNKTET = Konturform.GEPUNKTET;

	public static final int OFFEN = Arc2D.OPEN;
	public static final int GESCHLOSSEN = Arc2D.CHORD;
	public static final int KREISTEIL = Arc2D.PIE;


	public static final byte ZENTRUM = 0;
	public static final byte NORDEN = 1 << 0;
	public static final byte OSTEN = 1 << 2;
	public static final byte SUEDEN = 1 << 3;
	public static final byte WESTEN = 1 << 4;

	public static final byte NORDOSTEN = NORDEN | OSTEN;
	public static final byte SUEDOSTEN = SUEDEN | OSTEN;
	public static final byte NORDWESTEN = NORDEN | WESTEN;
	public static final byte SUEDWESTEN = SUEDEN | WESTEN;

	public static final byte MITTE = ZENTRUM;
	public static final byte OBEN = NORDEN;
	public static final byte RECHTS = OSTEN;
	public static final byte UNTEN = SUEDEN;
	public static final byte LINKS = WESTEN;


    public static final Farbe SCHWARZ = Farbe.SCHWARZ;
    public static final Farbe WEISS = Farbe.WEISS;
    public static final Farbe ROT = Farbe.ROT;
    public static final Farbe BLAU = Farbe.BLAU;
    public static final Farbe GRUEN = Farbe.GRUEN;
    public static final Farbe GELB = Farbe.GELB;

	public Farbe farbe( int pGrau ) {
		return farbe(pGrau, pGrau, pGrau, 255);
	}

	public Farbe farbe( int pGrau, int pAlpha ) {
		return farbe(pGrau, pGrau, pGrau, pAlpha);
	}

	public Farbe farbe(int red, int green, int blue) {
		return farbe(red, green, blue, 255);
	}

	public Farbe farbe(int red, int green, int blue, int alpha) {
		if (red < 0 || red >= 256)
			throw new IllegalArgumentException("red must be between 0 and 255");
		if (green < 0 || green >= 256)
			throw new IllegalArgumentException("green must be between 0 and 255");
		if (blue < 0 || blue >= 256)
			throw new IllegalArgumentException("blue must be between 0 and 255");
		if (alpha < 0 || alpha >= 256)
			throw new IllegalArgumentException("alpha must be between 0 and 255");

		return new Farbe(red, green, blue, alpha);
	}



    public double abs( double x ) {
        return Math.abs(x);
    }

    public double vorzeichen( double x ) {
        return Math.signum(x);
    }

    public double runden( double x ) {
        return Math.round(x);
    }

    public double abrunden( double x ) {
        return Math.floor(x);
    }

    public double aufrunden( double x ) {
        return Math.ceil(x);
    }

    public double sin( double x ) {
        return Math.sin(x);
    }

    public double cos( double x ) {
        return Math.cos(x);
    }

    public double tan( double x ) {
        return Math.tan(x);
    }

    public double arcsin( double x ) {
        return Math.asin(x);
    }

    public double arccos( double x ) {
        return Math.acos(x);
    }

    public double arctan( double x ) {
        return Math.atan(x);
    }

    public double beschraenken( double x, double max ) {
        if( x > max ) {
            return max;
        }
        return x;
    }

    public double beschraenken( double x, double min, double max ) {
        if( x > max ) {
            return max;
        }
        if( x < min ) {
            return min;
        }
        return x;
    }

	public double morphen( double pVon, double pNach, double pFaktor ) {
		return pVon - pFaktor*(pVon+pNach);
	}

}
