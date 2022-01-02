package schule.ngb.zm.shapes;

import schule.ngb.zm.Options;

import java.awt.geom.Ellipse2D;

public class Ellipse extends Shape {

	protected double width;

	protected double height;

	public Ellipse( double x, double y, double width, double height ) {
		super(x, y);
		this.width = width;
		this.height = height;
		setAnchor(CENTER);
	}

	public Ellipse( Ellipse ellipse ) {
		this(ellipse.x, ellipse.y, ellipse.width, ellipse.height);
		copyFrom(ellipse);
	}

	public double getWidth() {
		return width;
	}

	public void setWidth( double width ) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight( double height ) {
		this.height = height;
	}

	@Override
	public void copyFrom( Shape shape ) {
		super.copyFrom(shape);
		if( shape instanceof Ellipse ) {
			Ellipse e = (Ellipse) shape;
			this.width = e.width;
			this.height = e.height;
		}
	}

	@Override
	public Ellipse copy() {
		return new Ellipse(this);
	}

	@Override
	public void scale( double factor ) {
		super.scale(factor);
		width *= factor;
		height *= factor;
	}

	@Override
	public void setAnchor( Options.Direction anchor ) {
		calculateAnchor(width, height, anchor);
	}

	@Override
	public java.awt.Shape getShape() {
		return new Ellipse2D.Double(0, 0, width, height);
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( o == null || getClass() != o.getClass() ) return false;
		Ellipse ellipse = (Ellipse) o;
		return super.equals(o) &&
			Double.compare(ellipse.width, width) == 0 &&
			Double.compare(ellipse.height, height) == 0;
	}

	@Override
	public String toString() {
		return getClass().getCanonicalName() + '[' +
			"width=" + width +
			",height=" + height +
			",x=" + x +
			",y=" + y +
			']';
	}

}
