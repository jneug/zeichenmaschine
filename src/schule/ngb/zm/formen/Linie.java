package schule.ngb.zm.formen;

import java.awt.*;
import java.awt.geom.Line2D;

public class Linie extends Form {

	protected double x2;

	protected double y2;

	public Linie( double pX1, double pY1, double pX2, double pY2 ) {
		super(pX1, pY1);
		x2 = pX2;
		y2 = pY2;
	}

	public Linie( Linie pLinie ) {
		this(pLinie.x, pLinie.y, pLinie.x2, pLinie.y2);
		kopiere(pLinie);
	}

	public void setX2( double pX ) {
		this.x2 = x;
	}

	public void setY2( double pY ) {
		this.y2 = y;
	}

	public double getX2() {
		return x2;
	}

	public double getY2() {
		return y2;
	}

	@Override
	public void skalieren( double pFaktor ) {
		super.skalieren(pFaktor);
		x2 *= pFaktor;
		y2 *= pFaktor;
	}

	@Override
	public void kopiere( Form pForm ) {
		super.kopiere(pForm);
		if( pForm instanceof Linie ) {
			Linie linie = (Linie) pForm;
			x2 = linie.x2;
			y2 = linie.y2;
		}
	}

	@Override
	public Linie kopie() {
		return new Linie(this);
	}

	@Override
	public void setAnkerpunkt( byte pAnker ) {
		ankerBerechnen(x2-x, y2-y, pAnker);
	}

	@Override
	public Shape getShape() {
		Line2D.Double linie = new Line2D.Double(0, 0, x2 - x, y2 - y);
		return linie;
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( o == null || getClass() != o.getClass() ) return false;
		Linie linie = (Linie) o;
		return super.equals(o) &&
			Double.compare(linie.x2, x2) == 0 &&
			Double.compare(linie.y2, y2) == 0;
	}

	@Override
	public String toString() {
		return getClass().getCanonicalName() + "[" +
			"x1=" + x +
			",y1=" + y +
			",x2=" + x2 +
			",y2=" + y2 +
			']';
	}

}
