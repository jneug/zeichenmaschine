package schule.ngb.zm.shapes;

import java.awt.geom.Point2D;
import java.util.Arrays;

public class Quad extends Polygon {

	public Quad( double x, double y, Point2D... points ) {
		super(x, y, Arrays.copyOf(points, 4));
		if( points.length < 4 ) {
			throw new IllegalArgumentException("A quadrilateral requires exactly four points.");
		}
	}

	public Quad( Point2D... points ) {
		super(Arrays.copyOf(points, 4));
		if( points.length < 4 ) {
			throw new IllegalArgumentException("A quadrilateral requires exactly four points.");
		}
	}

	public Quad( double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4 ) {
		super(x1, y1, x2, y2, x3, y3, x4, y4);
	}

	public Quad( Quad pViereck ) {
		super(pViereck.x, pViereck.y);
		copyFrom(pViereck);
	}

	@Override
	public Shape copy() {
		return new Quad(this);
	}

}
