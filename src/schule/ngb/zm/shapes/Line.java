package schule.ngb.zm.shapes;

import schule.ngb.zm.Options;

import java.awt.geom.Line2D;

public class Line extends Shape {

	protected double x2;

	protected double y2;

	public Line( double x1, double y1, double x2, double y2 ) {
		super(x1, y1);
		this.x2 = x2;
		this.y2 = y2;
	}

	public Line( Line line ) {
		this(line.x, line.y, line.x2, line.y2);
		copyFrom(line);
	}

	public double getX2() {
		return x2;
	}

	public void setX2( double x ) {
		this.x2 = x;
	}

	public double getY2() {
		return y2;
	}

	public void setY2( double y ) {
		this.y2 = y;
	}

	@Override
	public void scale( double factor ) {
		super.scale(factor);
		x2 *= factor;
		y2 *= factor;
	}

	@Override
	public void copyFrom( Shape shape ) {
		super.copyFrom(shape);
		if( shape instanceof Line ) {
			Line pLine = (Line) shape;
			x2 = pLine.x2;
			y2 = pLine.y2;
		}
	}

	@Override
	public Line copy() {
		return new Line(this);
	}

	@Override
	public void move( double dx, double dy ) {
		super.move(dx, dy);
		x2 += dx;
		y2 += dx;
	}

	@Override
	public void moveTo( double x, double y ) {
		double dx = x2-this.x;
		double dy = y2-this.y;
		super.moveTo(x, y);
		x2 += dx;
		y2 += dy;
	}

	@Override
	public void setAnchor( Options.Direction anchor ) {
		calculateAnchor(x2 - x, y2 - y, anchor);
	}

	@Override
	public java.awt.Shape getShape() {
		return new Line2D.Double(0, 0, x2 - x, y2 - y);
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( o == null || getClass() != o.getClass() ) return false;
		Line pLine = (Line) o;
		return super.equals(o) &&
			Double.compare(pLine.x2, x2) == 0 &&
			Double.compare(pLine.y2, y2) == 0;
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
