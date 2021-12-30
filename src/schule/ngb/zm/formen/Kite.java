package schule.ngb.zm.formen;

import schule.ngb.zm.Options;

import java.awt.geom.Path2D;

public class Kite extends Shape {

	protected double width;

	protected double height;

	protected double ratio;

	public Kite( double x, double y, double width, double height ) {
		this(x, y, width, height, 0.5);
	}

	public Kite( double x, double y, double width, double height, double ratio ) {
		super(x, y);
		this.width = width;
		this.height = height;
		this.ratio = ratio;
		setAnchor(CENTER);
	}

	public Kite( Kite pKite ) {
		this(pKite.x, pKite.y, pKite.width, pKite.height, pKite.ratio);
		copyFrom(pKite);
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

	public double getRatio() {
		return ratio;
	}

	public void setRatio( double ratio ) {
		this.ratio = ratio;
	}

	@Override
	public void copyFrom( Shape shape ) {
		super.copyFrom(shape);
		if( shape instanceof Kite ) {
			Kite d = (Kite) shape;
			width = d.width;
			height = d.height;
			ratio = d.ratio;
		}
	}

	@Override
	public Kite copy() {
		return new Kite(this);
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
		double hHalf = width * .5, hRatio = ratio * height;
		Path2D shape = new Path2D.Double();
		shape.moveTo(hHalf, 0);
		shape.lineTo(width, hRatio);
		shape.lineTo(hHalf, height);
		shape.lineTo(0, hRatio);
		shape.closePath();
		return shape;
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( o == null || getClass() != o.getClass() ) return false;
		Kite d = (Kite) o;
		return super.equals(o) &&
			Double.compare(d.width, width) == 0 &&
			Double.compare(d.height, height) == 0 &&
			Double.compare(d.ratio, ratio) == 0;
	}

	@Override
	public String toString() {
		return getClass().getCanonicalName() + '[' +
			"width=" + width +
			",height=" + height +
			",verhaeltnis=" + ratio +
			",x=" + x +
			",y=" + y +
			']';
	}

}
