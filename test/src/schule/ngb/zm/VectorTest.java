package schule.ngb.zm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorTest {

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
