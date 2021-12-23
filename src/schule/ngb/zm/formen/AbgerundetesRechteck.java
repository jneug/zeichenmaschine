package schule.ngb.zm.formen;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class AbgerundetesRechteck extends Rechteck {

	protected double rundung = 1.0;

	public AbgerundetesRechteck( double pX, double pY, double pBreite, double pHoehe, double pRundung ) {
		super(pX, pY, pBreite, pHoehe);
		rundung = pRundung;
	}

	public AbgerundetesRechteck( Rechteck pRechteck ) {
		super(
			pRechteck.x, pRechteck.y,
			pRechteck.breite, pRechteck.hoehe);
		kopiere(pRechteck);
	}

	@Override
	public void kopiere( Form pForm ) {
		super.kopiere(pForm);
		if( pForm instanceof AbgerundetesRechteck ) {
			AbgerundetesRechteck rechteck = (AbgerundetesRechteck) pForm;
			rundung = rechteck.rundung;
		}
	}

	@Override
	public Shape getShape() {
		return new RoundRectangle2D.Double(
			0, 0, breite, hoehe, rundung, rundung
		);
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( o == null || getClass() != o.getClass() ) return false;
		AbgerundetesRechteck rechteck = (AbgerundetesRechteck) o;
		return super.equals(o) &&
			Double.compare(rechteck.rundung, rundung) == 0;
	}

	@Override
	public String toString() {
		return getClass().getCanonicalName() + "[" +
			"x=" + x +
			",y=" + y +
			",breite=" + breite +
			",hoehe=" + hoehe +
			",rundung=" + rundung +
			']';
	}

}
