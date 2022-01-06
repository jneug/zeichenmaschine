package schule.ngb.zm.shapes;

import org.jetbrains.annotations.NotNull;

import java.awt.geom.Path2D;

public class CustomShape extends Shape {

	protected Path2D.Double path;

	private double width = 0.0, height = 0.0;

	public CustomShape( double x, double y ) {
		super(x, y);
		path = new Path2D.Double();
	}

	public CustomShape( @NotNull CustomShape custom ) {
		super(custom.x, custom.y);
		path = custom.path;
		calculateBounds();
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getHeight() {
		return height;
	}

	private void calculateBounds() {
		java.awt.Rectangle s = path.getBounds();
		width = s.width;
		height = s.height;
	}

	public void lineTo( double x, double y ) {
		path.lineTo(x - x, y - y);
		calculateBounds();
	}

	public void arcTo( double x1, double y1, double x2, double y2 ) {
		path.quadTo(x1 - x, y1 - y, x2 - x, y2 - y);
		calculateBounds();
	}

	public void curveTo( double x1, double y1, double x2, double y2, double x3, double y3 ) {
		path.curveTo(x1 - x, y1 - y, x2 - x, y2 - y, x3 - x, y3 - y);
		calculateBounds();
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
