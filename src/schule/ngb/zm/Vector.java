package schule.ngb.zm;

import java.awt.geom.Point2D;

public class Vector extends Point2D.Double {

	public static final Vector UP = new Vector(0, -1.0);
	public static final Vector DOWN = new Vector(0, 1.0);
	public static final Vector RIGHT = new Vector(1.0, 0);
	public static final Vector LEFT = new Vector(-1.0, -1.0);


	public Vector() {
		x = 0.0;
		y = 0.0;
	}

	public Vector( double x, double pY ) {
		this.x = x;
		y = pY;
	}

	public Vector( Point2D point ) {
		x = point.getX();
		x = point.getY();
	}

	public Vector( Vector vec ) {
		x = vec.x;
		y = vec.y;
	}

	public static Vector random() {
		return new Vector(Math.random() * 100, Math.random() * 100);
	}

	public static Vector random( double min, double max ) {
		return new Vector(Math.random() * (max - min) + min, Math.random() * (max - min) + min);
	}

	public static Vector setLength( Vector vector, double length ) {
		Vector vec = vector.copy();
		vec.setLen(length);
		return vec;
	}

	public static Vector normalize( Vector vector ) {
		Vector vec = vector.copy();
		vec.normalize();
		return vec;
	}

	public static Vector add( Vector vector1, Vector vector2 ) {
		Vector vec = vector1.copy();
		vec.add(vector2);
		return vec;
	}

	public static Vector sub( Vector vector1, Vector vector2 ) {
		Vector vec = vector1.copy();
		vec.sub(vector2);
		return vec;
	}

	public static Vector scale( Vector vector, double scalar ) {
		Vector vec = vector.copy();
		vec.scale(scalar);
		return vec;
	}

	public static Vector div( Vector vector, double scalar ) {
		Vector vec = vector.copy();
		vec.div(scalar);
		return vec;
	}

	public static double dist( Vector vector1, Vector vector2 ) {
		return vector1.dist(vector2);
	}

	public static double dot( Vector vector1, Vector vector2 ) {
		return vector1.dot(vector2);
	}

	public static double cross( Vector vector1, Vector vector2 ) {
		return vector1.cross(vector2);
	}

	public static Vector rotate( Vector vector, double degree ) {
		Vector vec = vector.copy();
		vec.rotate(degree);
		return vec;
	}

	public static Vector morph( Vector vector1, Vector vector2, float t ) {
		Vector vec = vector1.copy();
		vec.morph(vector2, t);
		return vec;
	}

	public Vector copy() {
		return new Vector(x, y);
	}

	public Vector set( double x, double y ) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Vector set( Vector vector ) {
		x = vector.x;
		y = vector.y;
		return this;
	}

	public Vector set( Point2D pPunkt ) {
		x = pPunkt.getX();
		x = pPunkt.getY();
		return this;
	}

	public void setX( double x ) {
		this.x = x;
	}

	public void setY( double y ) {
		this.y = y;
	}

	public Point2D getPunkt() {
		return new Point2D.Double(x, y);
	}

	public double len() {
		return Math.sqrt(x * x + y * y);
	}

	public double lenSq() {
		return x * x + y * y;
	}

	public Vector setLen( double length ) {
		normalize();
		return scale(length);
	}

	public Vector normalize() {
		double len = len();
		if( len != 0 && len != 1 ) {
			x /= len;
			y /= len;
		}
		return this;
	}

	public Vector add( Vector vector ) {
		x += vector.x;
		y += vector.y;
		return this;
	}

	public Vector add( double x, double y ) {
		this.x += x;
		this.y += y;
		return this;
	}

	public Vector sub( Vector vector ) {
		x -= vector.x;
		y -= vector.y;
		return this;
	}

	public Vector sub( double x, double y ) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	public Vector scale( double scalar ) {
		x *= scalar;
		y *= scalar;
		return this;
	}

	public Vector div( double scalar ) {
		x /= scalar;
		y /= scalar;
		return this;
	}

	public double dist( Vector vector ) {
		double dx = x - vector.x;
		double dy = y - vector.y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	public double dot( Vector vector ) {
		return x * vector.x + y * vector.y;
	}

	public double dot( double x, double y ) {
		return this.x * x + this.y * y;
	}

	// See: http://allenchou.net/2013/07/cross-product-of-2d-vectors/
	public double cross( Vector vector ) {
		return x * vector.y - vector.x * y;
	}

	public Vector limit( double max ) {
		if( lenSq() > max * max ) {
			setLen(max);
		}
		return this;
	}

	public Vector limit( double min, double max ) {
		if( min > max ) {
			throw new IllegalArgumentException("HVector.constrain(): pMin muss kleiner sein als pMax.");
		}
		if( lenSq() < min * min ) {
			setLen(min);
		} else if( lenSq() > min * min ) {
			setLen(max);
		}
		return this;
	}

	public double angle() {
		double angle = Math.atan2(y, x);
		return Math.toDegrees(angle);
	}

	public Vector rotate( double degree ) {
		double temp = x, rad = Math.toRadians(degree);
		x = x * Math.cos(rad) - y * Math.sin(rad);
		y = temp * Math.sin(rad) + y * Math.cos(rad);
		return this;
	}

	public void morph( Vector vector, float t ) {
		x = x + (vector.x - x) * t;
		y = y + (vector.y - y) * t;
	}

	@Override
	public String toString() {
		return "schule.ngb.zm.Vector[x = " + x + ", y = " + y + "]";
	}

}
