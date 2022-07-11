package schule.ngb.zm.shapes;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class Polygon extends Shape {

	protected Point2D[] points;

	private double width = 0.0, height = 0.0;

	public Polygon( double x, double y, Point2D... points ) {
		super(x, y);

		this.points = new Point2D[points.length];
		for( int i = 0; i < points.length; i++ ) {
			this.points[i] = new Point2D.Double(points[i].getX() - x, points[i].getY() - y);
		}

		calculateBounds();
	}

	public Polygon( Point2D... points ) {
		this.points = new Point2D[points.length];
		for( int i = 0; i < points.length; i++ ) {
			if( i == 0 ) {
				x = points[i].getX();
				y = points[i].getY();
			}
			this.points[i] = new Point2D.Double(points[i].getX() - x, points[i].getY() - y);
		}

		calculateBounds();
	}

	public Polygon( double x1, double y1, double x2, double y2, double... coordinates ) {
		super(x1, y1);

		int numPoints = coordinates.length / 2 + 2;

		points = new Point2D[numPoints];
		this.points[0] = new Point2D.Double(x1, y1);
		this.points[1] = new Point2D.Double(x2 - x1, y2 - y1);
		for( int i = 0; i < numPoints - 2; i += 1 ) {
			this.points[i + 2] = new Point2D.Double(coordinates[i * 2] - x1, coordinates[i * 2 + 1] - y1);
		}

		calculateBounds();
	}

	public Polygon( Polygon polygon ) {
		super(polygon.x, polygon.y);
		copyFrom(polygon);
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
		java.awt.Rectangle s = getShape().getBounds();
		width = s.width;
		height = s.height;
	}

	public Point2D[] getPoints() {
		return points;
	}

	@Override
	public void copyFrom( Shape shape ) {
		super.copyFrom(shape);
		if( shape instanceof Polygon ) {
			Polygon v = (Polygon) shape;

			points = new Point2D[v.points.length];
			for( int i = 0; i < v.points.length; i++ ) {
				points[i] = new Point2D.Double(v.points[i].getX(), v.points[i].getY());
			}
		}
		calculateBounds();
	}

	@Override
	public Shape copy() {
		return new Polygon(this);
	}

	@Override
	public java.awt.Shape getShape() {
		Path2D shape = new Path2D.Double();
		//shape.moveTo(points[0].getX(), points[0].getY());
		shape.moveTo(0, 0);
		for( int i = 1; i < points.length; i++ ) {
			shape.lineTo(points[i].getX(), points[i].getY());
		}
		shape.closePath();
		return shape;
	}

}
