package schule.ngb.zm.formen;

import java.awt.geom.Path2D;

public class CustomShape extends Shape {

	protected Path2D.Double path;

	public CustomShape( double x, double y ) {
		super(x, y);
		path = new Path2D.Double();
	}

	public CustomShape( CustomShape custom ) {
		super(custom.x, custom.y);
		path = custom.path;
	}

	public void lineTo( double x, double y ) {
		path.lineTo(x - x, y - y);
	}

	public void arcTo( double x1, double y1, double x2, double y2 ) {
		path.quadTo(x1 - x, y1 - y, x2 - x, y2 - y);
	}

	public void curveTo( double x1, double y1, double x2, double y2, double x3, double y3 ) {
		path.curveTo(x1 - x, y1 - y, x2 - x, y2 - y, x3 - x, y3 - y);
	}

	public void close() {
		path.lineTo(0, 0);
	}

	@Override
	public Shape copy() {
		return new CustomShape(this);
	}

	@Override
	public void copyFrom( Shape shape ) {
		super.copyFrom(shape);
		if( shape instanceof CustomShape ) {
			path = new Path2D.Double(path);
		}
	}

	@Override
	public java.awt.Shape getShape() {
		return new Path2D.Double(path);
	}

}
