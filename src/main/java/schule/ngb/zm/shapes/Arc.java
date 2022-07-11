package schule.ngb.zm.shapes;

import schule.ngb.zm.Options;

import java.awt.geom.Arc2D;

public class Arc extends Shape {

	protected double width;

	protected double height;

	protected double angle;

	protected double startingangle;

	protected Options.PathType type = OPEN;

	public Arc( double x, double y, double radius, double angle ) {
		this(x, y, radius, radius, angle, 0.0);
	}

	public Arc( double x, double y, double radius, double angle, double startingangle ) {
		this(x, y, radius, radius, angle, startingangle);
	}

	public Arc( double x, double y, double width, double height, double angle, double startingangle ) {
		super(x, y);
		this.width = width;
		this.height = height;
		this.angle = angle;
		this.startingangle = startingangle;
		setAnchor(CENTER);

		noFill();
	}

	public Arc( Arc arc ) {
		this(arc.x, arc.y, arc.width, arc.height, arc.angle, arc.startingangle);
		copyFrom(arc);
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

	public double getAngle() {
		return angle;
	}

	public void setAngle( double angle ) {
		this.angle = angle;
	}

	public double getStartingangle() {
		return startingangle;
	}

	public void setStartingangle( double startingangle ) {
		this.startingangle = startingangle;
	}

	public Options.PathType getType() {
		return type;
	}

	public void setType( Options.PathType type ) {
		this.type = type;
	}

	@Override
	public Shape copy() {
		return new Arc(this);
	}

	@Override
	public void copyFrom( Shape shape ) {
		super.copyFrom(shape);
		if( shape instanceof Arc ) {
			Arc arc = (Arc) shape;
			width = arc.width;
			height = arc.height;
			angle = arc.angle;
			startingangle = arc.startingangle;
			type = arc.type;
		}
	}

	@Override
	public java.awt.Shape getShape() {
		return new Arc2D.Double(0, 0, width, height, startingangle, angle, type.awt_type);
	}

}
