package schule.ngb.zm.formen;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Ellipse extends Form {

	private double breite;

	private double hoehe;

	public Ellipse( double pX, double pY, double pBreite, double pHoehe ) {
		super(pX, pY);
		breite = pBreite;
		hoehe = pHoehe;
		setAnkerpunkt(ZENTRUM);
	}

	public Ellipse( Ellipse pEllipse ) {
		this(pEllipse.x, pEllipse.y, pEllipse.breite, pEllipse.hoehe);
		kopiere(pEllipse);
	}

	public double getBreite() {
		return breite;
	}

	public void setBreite( double breite ) {
		this.breite = breite;
	}

	public double getHoehe() {
		return hoehe;
	}

	public void setHoehe( double hoehe ) {
		this.hoehe = hoehe;
	}

	@Override
	public void kopiere( Form pForm ) {
		super.kopiere(pForm);
		if( pForm instanceof Ellipse ) {
			Ellipse e = (Ellipse) pForm;
			breite = e.breite;
			hoehe = e.hoehe;
		}
	}

	@Override
	public Ellipse kopie() {
		return new Ellipse(this);
	}

	@Override
	public void skalieren( double pFaktor ) {
		super.skalieren(pFaktor);
		breite *= pFaktor;
		hoehe *= pFaktor;
	}

	@Override
	public void setAnkerpunkt( byte pAnker ) {
		ankerBerechnen(breite, hoehe, pAnker);
	}

	@Override
	public Shape getShape() {
		Shape shape = new Ellipse2D.Double(0, 0, breite, hoehe);
		return shape;
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( o == null || getClass() != o.getClass() ) return false;
		Ellipse ellipse = (Ellipse) o;
		return super.equals(o) &&
			Double.compare(ellipse.breite, breite) == 0 &&
			Double.compare(ellipse.hoehe, hoehe) == 0;
	}

	@Override
	public String toString() {
		return getClass().getCanonicalName() + '[' +
			"breite=" + breite +
			",hoehe=" + hoehe +
			",x=" + x +
			",y=" + y +
			']';
	}

}
