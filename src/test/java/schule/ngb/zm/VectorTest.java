package schule.ngb.zm;

import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.*;

class VectorTest {

	@Test
	public void init() {
		Vector vec;

		vec = new Vector();
		assertEquals(0.0, vec.x, 0.0);
		assertEquals(0.0, vec.y, 0.0);

		vec = new Vector(4.5, 5.1);
		assertEquals(4.5, vec.x, 0.0);
		assertEquals(5.1, vec.y, 0.0);

		vec = new Vector(vec);
		assertEquals(4.5, vec.x, 0.0);
		assertEquals(5.1, vec.y, 0.0);

		vec = new Vector(Double.MAX_VALUE, Double.MIN_VALUE);
		assertEquals(Double.MAX_VALUE, vec.x, 0.0);
		assertEquals(Double.MIN_VALUE, vec.y, 0.0);

		vec = new Vector(new Point2D.Double(7.2, 3.5677));
		assertEquals(7.2, vec.x, 0.0);
		assertEquals(3.5677, vec.y, 0.0);

		vec = new Vector(new Point2D.Float(7.2f, 3.5677f));
		assertEquals(7.2, vec.x, 0.0001);
		assertEquals(3.5677, vec.y, 0.0001);

		vec.set(new Vector(5.1, 8.9));
		assertEquals(5.1, vec.x, 0.0001);
		assertEquals(8.9, vec.y, 0.0001);

		Vector vec2 = vec.copy();
		assertNotSame(vec, vec2);
		assertEquals(vec, vec2);
		assertEquals(5.1, vec2.x, 0.0001);
		assertEquals(8.9, vec2.y, 0.0001);

		assertEquals(vec, vec.set(100, 100));
		assertEquals(200, vec.set(200, 200).x, 0.0001);
		assertEquals(8.9, vec.set(vec2).y, 0.0001);

		for( int i = 0; i < 50; i++ ) {
			vec = Vector.random();
			assertTrue(vec.x >= 0);
			assertTrue(vec.y >= 0);
			assertTrue(vec.x < 100);
			assertTrue(vec.y < 100);
		}

		for( int i = 1; i < 50; i++ ) {
			vec = Vector.random(0, i*10);
			assertTrue(vec.x >= 0);
			assertTrue(vec.y >= 0);
			assertTrue(vec.x < i*10);
			assertTrue(vec.y < i*10);
		}

		for( int i = 1; i < 50; i++ ) {
			vec = Vector.random(0, i*10, (i+1)*10, (i+2)*10);
			assertTrue(vec.x >= 0);
			assertTrue(vec.y >= (i+1)*10);
			assertTrue(vec.x < i*10);
			assertTrue(vec.y < (i+2)*10);
		}
	}

	@Test
	void setLength() {
		Vector vec = new Vector(2.0, 5.4);
		Vector vec2 = Vector.setLength(vec, 10);
		assertEquals(10.0, vec2.length(), 0.00001);
		Vector vec3 = Vector.setLength(vec, -10);
		assertEquals(10.0, vec3.length(), 0.00001);
	}

	@Test
	void normalize() {
		Vector vec = new Vector(4.0, 3.0);
		assertEquals(5.0, vec.length(), 0.00001);
		Vector vec2 = vec.normalize();
		assertSame(vec, vec2);
		assertEquals(1.0, vec.length(), 0.00001);
	}

	@Test
	void add() {
	}

	@Test
	void sub() {
	}

	@Test
	void scale() {
		Vector vec = new Vector(4.0, 3.0);
		assertEquals(5.0, vec.length(), 0.00001);
		Vector vec2 = vec.scale(10.0);
		assertSame(vec, vec2);
		assertEquals(50.0, vec.length(), 0.00001);
		vec2.scale(-10.0);
		assertEquals(500.0, vec.length(), 0.00001);
		assertEquals(-400.0, vec.x, 0.00001);
		assertEquals(-300.0, vec.y, 0.00001);
	}

	@Test
	void div() {

	}

	@Test
	void dist() {
	}

	@Test
	void dot() {
	}

	@Test
	void cross() {
	}

	@Test
	void rotate() {
	}

	@Test
	void morph() {
	}

	@Test
	void copy() {
	}

	@Test
	void set() {
	}

	@Test
	void testSet() {
	}

	@Test
	void testSet1() {
	}

	@Test
	void setX() {
	}

	@Test
	void setY() {
	}

	@Test
	void getPunkt() {
	}

	@Test
	void len() {
		Vector vec = new Vector(4.0, 3.0);
		assertEquals(5.0, vec.length(), 0.00001);
		vec = new Vector(1.0, 0.0);
		assertEquals(1.0, vec.length(), 0.00001);
	}

	@Test
	void lenSq() {
		Vector vec = new Vector(4.0, 3.0);
		assertEquals(25.0, vec.lengthSq(), 0.00001);
		vec = new Vector(1.0, 0.0);
		assertEquals(1.0, vec.lengthSq(), 0.00001);
	}

	@Test
	void setLen() {
		Vector vec = new Vector(2.0, 5.4);
		Vector vec2 = vec.setLength(10);
		assertEquals(10.0, vec.length(), 0.00001);
		assertSame(vec, vec2);
		vec.setLength(-10);
		assertEquals(10.0, vec.length(), 0.00001);
	}

	@Test
	void testNormalize() {

	}

	@Test
	void testAdd() {
	}

	@Test
	void testAdd1() {
	}

	@Test
	void testSub() {
	}

	@Test
	void testSub1() {
	}

	@Test
	void testScale() {
	}

	@Test
	void testDiv() {
	}

	@Test
	void testDist() {
	}

	@Test
	void testDot() {
	}

	@Test
	void testDot1() {
	}

	@Test
	void testCross() {
	}

	@Test
	void limit() {
	}

	@Test
	void testLimit() {
	}

	@Test
	void angle() {
	}

	@Test
	void testRotate() {
	}

	@Test
	void testMorph() {
	}

}
