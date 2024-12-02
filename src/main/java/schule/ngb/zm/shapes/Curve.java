package schule.ngb.zm.shapes;

import schule.ngb.zm.Options;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.util.Arrays;

public class Curve extends Shape {

	protected double[] coordinates;

	public Curve( double x, double y, double cx, double cy, double x2, double y2 ) {
		super(x, y);

		coordinates = new double[]{
			cx, cy, x2, y2
		};

		noFill();
	}

	public Curve( double x, double y, double cx1, double cy1, double cx2, double cy2, double x2, double y2 ) {
		super(x, y);

		coordinates = new double[]{
			cx1, cy1, cx2, cy2, x2, y2
		};

		noFill();
	}

	public Curve( Curve curve ) {
		super(curve.x, curve.y);
		coordinates = Arrays.copyOf(curve.coordinates, curve.coordinates.length);
	}

	@Override
	public double getWidth() {
		return Math.abs(x-coordinates[coordinates.length-2]);
	}

	@Override
	public double getHeight() {
		return Math.abs(y-coordinates[coordinates.length-1]);
	}

	public Point2D getStart() {
		return new Point2D.Double(x, y);
	}

	public void setStart( double x, double y ) {
		this.x = x;
		this.y = y;
	}

	public Point2D getEnd() {
		return new Point2D.Double(
			coordinates[coordinates.length - 2],
			coordinates[coordinates.length - 1]
		);
	}

	public void setEnd( double x, double y ) {
		coordinates[coordinates.length - 2] = x;
		coordinates[coordinates.length - 1] = y;
	}

	public Point2D getControl1() {
		return new Point2D.Double(
			coordinates[0],
			coordinates[1]
		);
	}

	public void setControl1( double x, double y ) {
		coordinates[0] = x;
		coordinates[1] = y;
	}

	public Point2D getControl2() {
		return new Point2D.Double(
			coordinates[coordinates.length - 4],
			coordinates[coordinates.length - 3]
		);
	}

	public void setControl2( double x, double y ) {
		coordinates[coordinates.length - 4] = x;
		coordinates[coordinates.length - 3] = y;
	}

	public void setPoints( double x, double y, double cx, double cy, double x2, double y2 ) {
		setStart(x, y);
		coordinates = coordinates = new double[]{
			cx, cy, x2, y2
		};
	}

	public void setPoints( double x, double y, double cx1, double cy1, double cx2, double cy2, double x2, double y2 ) {
		setStart(x, y);
		coordinates = new double[]{
			cx1, cy1, cx2, cy2, x2, y2
		};
	}

	public boolean isCubic() {
		return coordinates.length == 6;
	}

	public boolean isQuad() {
		return coordinates.length == 4;
	}

	@Override
	public Shape copy() {
		return new Curve(this);
	}

	@Override
	public void copyFrom( Shape shape ) {
		super.copyFrom(shape);
		if( shape instanceof Curve ) {
			Curve k = (Curve) shape;
			coordinates = Arrays.copyOf(k.coordinates, k.coordinates.length);
		}
	}

	@Override
	public java.awt.Shape getShape() {
		if( isCubic() ) {
			return new CubicCurve2D.Double(
				0, 0,
				coordinates[0] - x, coordinates[1] - y,
				coordinates[2] - x, coordinates[3] - y,
				coordinates[4] - x, coordinates[5] - y
			);
		} else {
			return new QuadCurve2D.Double(
				0, 0,
				coordinates[0] - x, coordinates[1] - y,
				coordinates[2] - x, coordinates[3] - y
			);
		}
	}

	@Override
	public void scale( double factor ) {
		super.scale(factor);
		/*for( int i = 0; i < coordinates.length; i++ ) {
			coordinates[i] *= factor;
		}*/
		coordinates[coordinates.length - 4] *= factor;
		coordinates[coordinates.length - 3] *= factor;
		coordinates[coordinates.length - 2] *= factor;
		coordinates[coordinates.length - 1] *= factor;
	}

	@Override
	public void move( double dx, double dy ) {
		super.move(dx, dy);
		for( int i = 0; i < coordinates.length; i += 2 ) {
			coordinates[i] = coordinates[i] + dx;
			coordinates[i + 1] = coordinates[i + 1] + dy;
		}
	}

	@Override
	public void moveTo( double x, double y ) {
		double dx = x - x, dy = y - y;
		move(dx, dy);
	}

	@Override
	public void draw( Graphics2D graphics, AffineTransform transform ) {
		if( !visible ) {
			return;
		}

		AffineTransform orig = graphics.getTransform();
		if( transform != null ) {
			//graphics.transform(transform);
		}

		graphics.translate(x, y);
		graphics.rotate(Math.toRadians(rotation));

		java.awt.Shape shape = getShape();

		java.awt.Color currentColor = graphics.getColor();
		fillShape(shape, graphics);
		strokeShape(shape, graphics);
		graphics.setColor(currentColor);

		graphics.setTransform(orig);
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( o == null || getClass() != o.getClass() ) return false;
		Curve pCurve = (Curve) o;
		return super.equals(o) &&
			getStart().equals(pCurve.getStart()) &&
			getControl1().equals(pCurve.getControl1()) &&
			getControl2().equals(pCurve.getControl2()) &&
			getEnd().equals(pCurve.getEnd());
	}

	@Override
	public String toString() {
		return getClass().getCanonicalName() + "[" +
			"x=" + x +
			",y=" + y +
			"x2=" + coordinates[coordinates.length - 2] +
			",y2=" + coordinates[coordinates.length - 1] +
			']';
	}

}
