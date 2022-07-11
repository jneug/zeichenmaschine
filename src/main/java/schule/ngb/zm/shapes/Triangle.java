package schule.ngb.zm.shapes;

import java.awt.geom.Point2D;
import java.util.Arrays;

public class Triangle extends Polygon {

	public Triangle( double x, double y, Point2D... points ) {
		super(x, y, Arrays.copyOf(points, 3));
		if( points.length < 3 ) {
			throw new IllegalArgumentException("A triangle requires exactly three points.");
		}
	}

	public Triangle( Point2D... points ) {
		super(Arrays.copyOf(points, 3));
		if( points.length < 3 ) {
			throw new IllegalArgumentException("A triangle requires exactly three points.");
		}
	}

	public Triangle( double x1, double y1, double x2, double y2, double x3, double y3 ) {
		super(x1, y1, x2, y2, x3, y3);
	}

	public Triangle( Triangle triangle ) {
		super(triangle.x, triangle.y);
		copyFrom(triangle);
	}

	@Override
	public Shape copy() {
		return new Triangle(this);
	}

}
