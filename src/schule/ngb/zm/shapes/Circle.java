package schule.ngb.zm.shapes;

import schule.ngb.zm.Options;

import java.awt.geom.Ellipse2D;

public class Circle extends Shape {

	protected double radius;

	public Circle( double x, double y, double radius ) {
		super(x, y);
		this.radius = radius;
		setAnchor(CENTER);
	}

	public Circle( Circle circle ) {
		this(circle.x, circle.y, circle.radius);
		copyFrom(circle);
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius( double radius ) {
		this.radius = radius;
	}

	@Override
	public void scale( double factor ) {
		super.scale(factor);
		radius *= factor;
	}

	@Override
	public void copyFrom( Shape shape ) {
		super.copyFrom(shape);
		if( shape instanceof Circle ) {
			radius = ((Circle) shape).radius;
		}
	}

	@Override
	public Circle copy() {
		return new Circle(this);
	}

	@Override
	public java.awt.Shape getShape() {
		return new Ellipse2D.Double(0, 0, radius + radius, radius + radius);
	}

	@Override
	public void setAnchor( Options.Direction anchor ) {
		calculateAnchor(radius + radius, radius + radius, anchor);
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( o == null || getClass() != o.getClass() ) return false;
		Circle pCircle = (Circle) o;
		return super.equals(o) && Double.compare(pCircle.radius, radius) == 0;
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
