package schule.ngb.zm.shapes;

import schule.ngb.zm.Options;

import java.awt.geom.Rectangle2D;

public class Rectangle extends Shape {

	protected double width;

	protected double height;

	public Rectangle( double x, double y, double width, double height ) {
		super(x, y);
		this.width = width;
		this.height = height;
		this.anchor = Options.Direction.NORTHWEST;
	}

	public Rectangle( Rectangle pRechteck ) {
		this(
			pRechteck.x, pRechteck.y,
			pRechteck.width, pRechteck.height);
		copyFrom(pRechteck);
	}

	public Rectangle( Bounds pBounds ) {
		this(
			pBounds.x, pBounds.y,
			pBounds.width, pBounds.height);
	}

	/**
	 * Erstellt ein Rechteck zur Darstellung der
	 * @param pShape
	 */
	public Rectangle( Shape pShape ) {
		this(pShape, true);
	}

	public Rectangle( Shape pShape, boolean transformed ) {
		java.awt.Shape s = pShape.getShape();
		if( transformed ) {
			s = pShape.getTransform().createTransformedShape(s);
		}
		Rectangle2D bounds = s.getBounds2D();
		x = bounds.getX();
		y = bounds.getY();
		width = bounds.getWidth();
		height = bounds.getHeight();
		fillColor = null;
		strokeType = DASHED;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getHeight() {
		return height;
	}

	public void setWidth( double width ) {
		this.width = width;
		invalidate();
	}

	public void setHeight( double height ) {
		this.height = height;
		invalidate();
	}

	@Override
	public Rectangle copy() {
		return new Rectangle(this);
	}

	@Override
	public void copyFrom( Shape shape ) {
		super.copyFrom(shape);
		if( shape instanceof Rectangle ) {
			Rectangle rechteck = (Rectangle) shape;
			width = rechteck.width;
			height = rechteck.height;
		}
	}

	@Override
	public void scale( double factor ) {
		super.scale(factor);
		width *= factor;
		height *= factor;
		invalidate();
	}

	@Override
	public java.awt.Shape getShape() {
		return new Rectangle2D.Double(0, 0, width, height);
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( o == null || getClass() != o.getClass() ) return false;
		Rectangle rechteck = (Rectangle) o;
		return super.equals(o) &&
			Double.compare(rechteck.width, width) == 0 &&
			Double.compare(rechteck.height, height) == 0;
	}

	@Override
	public String toString() {
		return getClass().getCanonicalName() + "[" +
			"x=" + x +
			",y=" + y +
			",width=" + width +
			",height=" + height +
			']';
	}

}
