package schule.ngb.zm;

import org.junit.Test;

import java.awt.geom.Point2D;

import static org.junit.Assert.*;

public class TestVector {

	@Test
	public void  testInit() {
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
	public void testMath() {
		Vector vec1, vec2;

		vec1 = new Vector(100, 0);
		vec1.scale(0.0);
		vec2 = new Vector(0, 0);
		assertEquals(vec2, vec1);

		vec1.add(100, 50);
		assertEquals(100, vec1.x, 0.0);
		assertEquals(50, vec1.y, 0.0);

		vec2.sub(50, 100);
		assertEquals(-50, vec2.x, 0.0);
		assertEquals(-100, vec2.y, 0.0);

		vec2.scale(1.5);
		assertEquals(-75, vec2.x, 0.0001);
		assertEquals(-150, vec2.y, 0.0001);

		assertEquals(100, vec1.set(100,0).length(), 0.0001);
		assertEquals(10000, vec1.lengthSq(), 0.0001);
		assertEquals(Math.sqrt(50*50 + 60*60), vec1.set(50,60).length(), 0.0001);
		assertEquals(1.0, vec1.set(100,0).normalize().length(), 0.0001);
		assertEquals(1.0, Vector.random().normalize().length(), 0.0001);
	}


	@Test
	public void testStaticMath() {

	}

}
