package schule.ngb.zm.formen;

import java.awt.geom.RoundRectangle2D;

public class RoundedRectangle extends Rectangle {

	protected double borderRadius = 1.0;

	public RoundedRectangle( double x, double y, double width, double height, double borderRadius ) {
		super(x, y, width, height);
		this.borderRadius = borderRadius;
	}

	public RoundedRectangle( Rectangle pRechteck ) {
		super(
			pRechteck.x, pRechteck.y,
			pRechteck.width, pRechteck.height);
		copyFrom(pRechteck);
	}

	@Override
	public void copyFrom( Shape shape ) {
		super.copyFrom(shape);
		if( shape instanceof RoundedRectangle ) {
			RoundedRectangle rechteck = (RoundedRectangle) shape;
			borderRadius = rechteck.borderRadius;
		}
	}

	@Override
	public java.awt.Shape getShape() {
		return new RoundRectangle2D.Double(
			0, 0, width, height, borderRadius, borderRadius
		);
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( o == null || getClass() != o.getClass() ) return false;
		RoundedRectangle rechteck = (RoundedRectangle) o;
		return super.equals(o) &&
			Double.compare(rechteck.borderRadius, borderRadius) == 0;
	}

	@Override
	public String toString() {
		return getClass().getCanonicalName() + "[" +
			"x=" + x +
			",y=" + y +
			",width=" + width +
			",height=" + height +
			",rundung=" + borderRadius +
			']';
	}

}
