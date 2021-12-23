package schule.ngb.zm;

import java.awt.geom.Point2D;

public class Vektor extends Point2D.Double {

    public Vektor() {
        x = 0.0;
        y = 0.0;
    }

    public Vektor(double pX, double pY) {
        x = pX;
        y = pY;
    }

    public Vektor(Point2D.Double pPunkt) {
        x = pPunkt.getX();
        x = pPunkt.getY();
    }

    public Vektor(Vektor pVektor) {
        x = pVektor.x;
        y = pVektor.y;
    }

	public static Vektor zufall() {
		return new Vektor(Math.random()*100, Math.random()*100);
	}

	public static Vektor zufall( double min, double max ) {
		return new Vektor(Math.random()*(max-min)+min, Math.random()*(max-min)+min);
	}

	public Vektor kopie() {
        return new Vektor(x, y);
    }

    public Vektor set(double pX, double pY) {
        x = pX;
        y = pY;
		return this;
    }

    public Vektor set(Vektor pVektor) {
        x = pVektor.x;
        y = pVektor.y;
		return this;
	}

    public Vektor set(Point2D.Double pPunkt) {
        x = pPunkt.getX();
        x = pPunkt.getY();
		return this;
    }

    public void setX(double pX) {
        x = pX;
    }

    public void setY(double pY) {
        y = pY;
    }

    public Point2D.Double getPunkt() {
        return new Point2D.Double(x, y);
    }

    public double laenge() {
        return Math.sqrt(x * x + y * y);
    }

    public double laengeQuad() {
        return x * x + y * y;
    }

    public Vektor setLaenge(double pLaenge) {
        normalisieren();
		return skalieren(pLaenge);
    }

    public static Vektor setLaenge(Vektor pVektor, double pLaenge) {
        Vektor vec = pVektor.kopie();
        vec.setLaenge(pLaenge);
        return vec;
    }

    public Vektor normalisieren() {
        double len = laenge();
        if (len != 0 && len != 1) {
            x /= len;
            y /= len;
        }
		return this;
    }

    public static Vektor normalisieren(Vektor pVektor) {
        Vektor vec = pVektor.kopie();
        vec.normalisieren();
        return vec;
    }

    public Vektor addieren(Vektor pVektor) {
        x += pVektor.x;
        y += pVektor.y;
		return this;
    }

    public Vektor addieren(double pX, double pY) {
        x += pX;
        y += pY;
		return this;
    }

    public static Vektor addieren(Vektor pVektor1, Vektor pVektor2) {
        Vektor vec = pVektor1.kopie();
        vec.addieren(pVektor2);
        return vec;
    }

    public void subtrahieren(Vektor pVektor) {
        x -= pVektor.x;
        y -= pVektor.y;
    }

    public void subtrahieren(double pX, double pY) {
        x -= pX;
        y -= pY;
    }

    public static Vektor subtrahieren(Vektor pVektor1, Vektor pVektor2) {
        Vektor vec = pVektor1.kopie();
        vec.subtrahieren(pVektor2);
        return vec;
    }

    public Vektor skalieren(double pSkalar) {
        x *= pSkalar;
        y *= pSkalar;
		return this;
    }

    public static Vektor skalieren(Vektor pVektor, double pSkalar) {
        Vektor vec = pVektor.kopie();
        vec.skalieren(pSkalar);
        return vec;
    }

    public Vektor dividieren(double pSkalar) {
        x /= pSkalar;
        y /= pSkalar;
		return this;
    }

    public static Vektor dividieren(Vektor pVektor, double pSkalar) {
        Vektor vec = pVektor.kopie();
        vec.dividieren(pSkalar);
        return vec;
    }

    public double abstand(Vektor pVektor) {
        double dx = x - pVektor.x;
        double dy = y - pVektor.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static double abstand(Vektor pVektor1, Vektor pVektor2) {
        return pVektor1.abstand(pVektor2);
    }

    public double dot(Vektor pVektor) {
        return x * pVektor.x + y * pVektor.y;
    }

    public double dot(double pX, double pY) {
        return x * pX + y * pY;
    }

    public static double dot(Vektor pVektor1, Vektor pVektor2) {
        return pVektor1.dot(pVektor2);
    }

    // See: http://allenchou.net/2013/07/cross-product-of-2d-vectors/
    public double cross(Vektor pVektor) {
        return x * pVektor.y - pVektor.x * y;
    }

    public static double cross(Vektor pVektor1, Vektor pVektor2) {
        return pVektor1.cross(pVektor2);
    }

    public void limitieren(double pMax) {
        if (laengeQuad() > pMax * pMax) {
            normalisieren();
            skalieren(pMax);
        }
    }

    public void beschraenken(double pMin, double pMax) {
        if (pMin > pMax) {
            throw new IllegalArgumentException("HVector.constrain(): pMin muss kleiner sein als pMax.");
        }
        if (laengeQuad() < pMin * pMin) {
            normalisieren();
            skalieren(pMin);
        }
        if (laengeQuad() > pMax * pMax) {
            normalisieren();
            skalieren(pMax);
        }
    }

    public double richtung() {
        double angle = Math.atan2(y, x);
        return angle;
    }

    public double winkel() {
        return richtung();
    }

    public Vektor drehen(double pWinkel) {
        double temp = x;
        x = x * Math.cos(pWinkel) - y * Math.sin(pWinkel);
        y = temp * Math.sin(pWinkel) + y * Math.cos(pWinkel);
		return this;
    }

    public static Vektor drehen(Vektor pVektor, double pWinkel) {
        Vektor vec = pVektor.kopie();
        vec.drehen(pWinkel);
        return vec;
    }

    public void linterp(Vektor pVektor, float t) {
        x = x + (pVektor.x - x) * t;
        y = y + (pVektor.y - y) * t;
    }

    public static Vektor linterp(Vektor pVektor1, Vektor pVektor2, float t) {
        Vektor vec = pVektor1.kopie();
        vec.linterp(pVektor2, t);
        return vec;
    }

    @Override
    public String toString() {
        return "schule.ngb.zm.Vektor{x = " + x + ", y = " + y + "}";
    }

}
