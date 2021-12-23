package schule.ngb.zm.formen;

import java.awt.*;
import java.awt.geom.Path2D;

public class Drache extends Form {

	private double breite;

	private double hoehe;

	private double verhaeltnis;

	public Drache( double pX, double pY, double pBreite, double pHoehe ) {
		this(pX, pY, pBreite, pHoehe, 0.5);
	}

	public Drache( double pX, double pY, double pBreite, double pHoehe, double pVerhaeltnis ) {
		super(pX, pY);
		breite = pBreite;
		hoehe = pHoehe;
		verhaeltnis = pVerhaeltnis;
		setAnkerpunkt(ZENTRUM);
	}

	public Drache( Drache pDrache ) {
		this(pDrache.x, pDrache.y, pDrache.breite, pDrache.hoehe, pDrache.verhaeltnis);
		kopiere(pDrache);
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

	public double getVerhaeltnis() {
		return verhaeltnis;
	}

	public void setVerhaeltnis( double pVerhaeltnis ) {
		this.verhaeltnis = pVerhaeltnis;
	}

	@Override
	public void kopiere( Form pForm ) {
		super.kopiere(pForm);
		if( pForm instanceof Drache ) {
			Drache d = (Drache) pForm;
			breite = d.breite;
			hoehe = d.hoehe;
			verhaeltnis = d.verhaeltnis;
		}
	}

	@Override
	public Drache kopie() {
		return new Drache(this);
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
		double bHalb = breite * .5, hVerh = verhaeltnis*hoehe;
		Path2D shape = new Path2D.Double();
		shape.moveTo(bHalb, 0);
		shape.lineTo(breite, hVerh);
		shape.lineTo(bHalb, hoehe);
		shape.lineTo(0, hVerh);
		shape.closePath();
		return shape;
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( o == null || getClass() != o.getClass() ) return false;
		Drache d = (Drache) o;
		return super.equals(o) &&
			Double.compare(d.breite, breite) == 0 &&
			Double.compare(d.hoehe, hoehe) == 0 &&
			Double.compare(d.verhaeltnis, verhaeltnis) == 0;
	}

	@Override
	public String toString() {
		return getClass().getCanonicalName() + '[' +
			"breite=" + breite +
			",hoehe=" + hoehe +
			",verhaeltnis=" + verhaeltnis +
			",x=" + x +
			",y=" + y +
			']';
	}

}
