package schule.ngb.zm.shapes;

import java.awt.geom.Rectangle2D;

public class Bounds extends Rectangle2D.Double {

	public Bounds( double x, double y, double w, double h ) {
		super(x,y,w,h);
	}

	public Bounds( Shape shape ) {
		if( shape != null ) {
			java.awt.Shape s = shape.getShape();
			if( s != null ) {
				s = shape.getTransform().createTransformedShape(s);
				Rectangle2D bounds = s.getBounds2D();
				x = bounds.getX();
				y = bounds.getY();
				width = bounds.getWidth();
				height = bounds.getHeight();
			} else {
				x = shape.getX();
				y = shape.getY();
				width = 0;
				height = 0;
			}
		}
	}

}
