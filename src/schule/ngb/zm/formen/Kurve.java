package schule.ngb.zm.formen;

import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.util.Arrays;

public class Kurve extends Form {

	private double[] koordinaten;

	public Kurve( double pX, double pY, double pCx, double pCy, double pX2, double pY2 ) {
		super(pX, pY);

		koordinaten = new double[]{
			pCx, pCy, pX2, pY2
		};

		keineFuellung();
	}

	public Kurve( double pX, double pY, double pCx1, double pCy1, double pCx2, double pCy2, double pX2, double pY2 ) {
		super(pX, pY);

		koordinaten = new double[]{
			pCx1, pCy1, pCx2, pCy2, pX2, pY2
		};

		keineFuellung();
	}

	public Kurve( Kurve pKurve ) {
		super(pKurve.x, pKurve.y);
		koordinaten = Arrays.copyOf(pKurve.koordinaten, pKurve.koordinaten.length);
	}

	public Point2D getStartpunkt() {
		return new Point2D.Double(x, y);
	}

	public void setStartpunkt( double pX, double pY ) {
		x = pX;
		y = pY;
	}

	public Point2D getEndpunkt() {
		return new Point2D.Double(
			koordinaten[koordinaten.length - 2],
			koordinaten[koordinaten.length - 1]
		);
	}

	public void setEndpunkt( double pX, double pY ) {
		koordinaten[koordinaten.length - 2] = pX;
		koordinaten[koordinaten.length - 1] = pY;
	}

	public Point2D getKontrollpunkt1() {
		return new Point2D.Double(
			koordinaten[0],
			koordinaten[1]
		);
	}

	public void setKontrollpunkt1( double pX, double pY ) {
		koordinaten[0] = pX;
		koordinaten[1] = pY;
	}

	public Point2D getKontrollpunkt2() {
		return new Point2D.Double(
			koordinaten[koordinaten.length - 4],
			koordinaten[koordinaten.length - 3]
		);
	}

	public void setKontrollpunkt2( double pX, double pY ) {
		koordinaten[koordinaten.length - 4] = pX;
		koordinaten[koordinaten.length - 3] = pY;
	}

	public void setPunkte( double pX, double pY, double pCx, double pCy, double pX2, double pY2 ) {
		setStartpunkt(pX, pY);
		koordinaten = koordinaten = new double[]{
			pCx, pCy, pX2, pY2
		};
	}

	public void setPunkte( double pX, double pY, double pCx1, double pCy1, double pCx2, double pCy2, double pX2, double pY2 ) {
		setStartpunkt(pX, pY);
		koordinaten = new double[]{
			pCx1, pCy1, pCx2, pCy2, pX2, pY2
		};
	}

	public boolean istKubisch() {
		return koordinaten.length == 6;
	}

	public boolean istQuadratisch() {
		return koordinaten.length == 4;
	}

	@Override
	public Form kopie() {
		return new Kurve(this);
	}

	@Override
	public void kopiere( Form pForm ) {
		super.kopiere(pForm);
		if( pForm instanceof Kurve ) {
			Kurve k = (Kurve) pForm;
			koordinaten = Arrays.copyOf(k.koordinaten, k.koordinaten.length);
		}
	}

	@Override
	public Shape getShape() {
		if( istKubisch() ) {
			return new CubicCurve2D.Double(
				0, 0,
				koordinaten[0] - x, koordinaten[1] - y,
				koordinaten[2] - x, koordinaten[3] - y,
				koordinaten[4] - x, koordinaten[5] - y
			);
		} else {
			return new QuadCurve2D.Double(
				0, 0,
				koordinaten[0] - x, koordinaten[1] - y,
				koordinaten[2] - x, koordinaten[3] - y
			);
		}
	}

	@Override
	public void skalieren( double pFaktor ) {
		super.skalieren(pFaktor);
		koordinaten[koordinaten.length - 4] *= pFaktor;
		koordinaten[koordinaten.length - 3] *= pFaktor;
		koordinaten[koordinaten.length - 2] *= pFaktor;
		koordinaten[koordinaten.length - 1] *= pFaktor;
	}

	@Override
	public void verschieben( double dx, double dy ) {
		super.verschieben(dx, dy);
		for( int i = 0; i < koordinaten.length; i += 2 ) {
			koordinaten[i] = koordinaten[i] + dx;
			koordinaten[i + 1] = koordinaten[i + 1] + dy;
		}
	}

	@Override
	public void verschiebeNach( double pX, double pY ) {
		double dx = pX - x, dy = pY - y;
		verschieben(dx, dy);
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( o == null || getClass() != o.getClass() ) return false;
		Kurve kurve = (Kurve) o;
		return super.equals(o) &&
			getStartpunkt().equals(kurve.getStartpunkt()) &&
			getKontrollpunkt1().equals(kurve.getKontrollpunkt1()) &&
			getKontrollpunkt2().equals(kurve.getKontrollpunkt2()) &&
			getEndpunkt().equals(kurve.getEndpunkt());
	}

	@Override
	public String toString() {
		return getClass().getCanonicalName() + "[" +
			"x=" + x +
			",y=" + y +
			"x2=" + koordinaten[koordinaten.length - 2] +
			",y2=" + koordinaten[koordinaten.length - 1] +
			']';
	}

}
