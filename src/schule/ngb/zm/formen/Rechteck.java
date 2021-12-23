package schule.ngb.zm.formen;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Rechteck extends Form {

	protected double breite;

	protected double hoehe;

	public Rechteck( double pX, double pY, double pBreite, double pHoehe ) {
		super(pX, pY);
		breite = pBreite;
		hoehe = pHoehe;
		setAnkerpunkt(NORDWESTEN);
	}

	public Rechteck( Rechteck pRechteck ) {
		this(
			pRechteck.x, pRechteck.y,
			pRechteck.breite, pRechteck.hoehe);
		kopiere(pRechteck);
	}

	public Rechteck( Form pForm ) {
		Shape s = pForm.getShape();
		s = pForm.getVerzerrung().createTransformedShape(s);
		Rectangle2D bounds = s.getBounds2D();
		x = bounds.getX();
		y = bounds.getY();
		breite = bounds.getWidth();
		hoehe = bounds.getHeight();
		fuellFarbe = null;
		konturArt = GESTRICHELT;
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
	public Rechteck kopie() {
		return new Rechteck(this);
	}

	@Override
	public void kopiere( Form pForm ) {
		super.kopiere(pForm);
		if( pForm instanceof Rechteck ) {
			Rechteck rechteck = (Rechteck) pForm;
			breite = rechteck.breite;
			hoehe = rechteck.hoehe;
		}
	}

	@Override
	public void skalieren( double pFaktor ) {
		super.skalieren(pFaktor);
		breite *= pFaktor;
		hoehe *= pFaktor;
	}

	@Override
	public Shape getShape() {
		return new Rectangle2D.Double(0, 0, breite, hoehe);
	}

	@Override
	public void setAnkerpunkt( byte pAnker ) {
		ankerBerechnen(breite, hoehe, pAnker);
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( o == null || getClass() != o.getClass() ) return false;
		Rechteck rechteck = (Rechteck) o;
		return super.equals(o) &&
			Double.compare(rechteck.breite, breite) == 0 &&
			Double.compare(rechteck.hoehe, hoehe) == 0;
	}

	@Override
	public String toString() {
		return getClass().getCanonicalName() + "[" +
			"x=" + x +
			",y=" + y +
			",breite=" + breite +
			",hoehe=" + hoehe +
			']';
	}

}
