package schule.ngb.zm.formen;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Kreis extends Form {

	public double radius;

	public Kreis( double pX, double pY, double pRadius ) {
		super(pX, pY);
		radius = pRadius;
		setAnkerpunkt(ZENTRUM);
	}

	public Kreis( Kreis pKreis ) {
		this(pKreis.x, pKreis.y, pKreis.radius);
		kopiere(pKreis);
	}

	@Override
	public void skalieren( double pFaktor ) {
		super.skalieren(pFaktor);
		radius *= pFaktor;
	}

	@Override
	public void kopiere( Form pForm ) {
		super.kopiere(pForm);
		if( pForm instanceof Kreis ) {
			radius = ((Kreis) pForm).radius;
		}
	}

	@Override
	public Kreis kopie() {
		return new Kreis(this);
	}

	@Override
	public Shape getShape() {
		return new Ellipse2D.Double(0, 0, radius * 2.0, radius * 2.0);
	}

	@Override
	public void setAnkerpunkt( byte pAnker ) {
		ankerBerechnen(radius + radius, radius + radius, pAnker);
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( o == null || getClass() != o.getClass() ) return false;
		Kreis kreis = (Kreis) o;
		return super.equals(o) && Double.compare(kreis.radius, radius) == 0;
	}

	@Override
	public String toString() {
		return getClass().getCanonicalName() + "[" +
			"x=" + x +
			",y=" + y +
			",radius=" + radius +
			']';
	}

}
