package schule.ngb.zm.shapes;

import org.junit.jupiter.api.Test;
import schule.ngb.zm.Options;

import static schule.ngb.zm.Options.Direction.*;

import static org.junit.jupiter.api.Assertions.*;



class ShapeTest {

	class TestShape extends Shape {

		public TestShape() {
			super(42, 131);
		}

		@Override
		public double getWidth() {
			return 182;
		}

		@Override
		public double getHeight() {
			return 235;
		}

		@Override
		public Shape copy() {
			return new TestShape();
		}

		@Override
		public java.awt.Shape getShape() {
			return null;
		}

	}

	@Test
	void testShape() {
		Shape s = new TestShape();

		assertEquals(42, s.getX());
		assertEquals(131, s.getY());
		assertEquals(182, s.getWidth());
		assertEquals(235, s.getHeight());

		assertEquals(Options.Direction.CENTER, s.getAnchor());
	}

	@Test
	void getAnchorPoint() {
		Shape s = new TestShape();

		assertEquals(0.0, s.getAnchorPoint(CENTER).getX(), 0.00001);
		assertEquals(0.0, s.getAnchorPoint(CENTER).getY(), 0.00001);

		assertEquals(0.0, s.getAnchorPoint(UP).getX(), 0.00001);
		assertEquals(-117.5, s.getAnchorPoint(UP).getY(), 0.00001);

		assertEquals(-91.0, s.getAnchorPoint(LEFT).getX(), 0.00001);
		assertEquals(0.0, s.getAnchorPoint(LEFT).getY(), 0.00001);

		assertEquals(91.0, s.getAnchorPoint(SOUTHEAST).getX(), 0.00001);
		assertEquals(117.5, s.getAnchorPoint(SOUTHEAST).getY(), 0.00001);

		s.setAnchor(DOWN);

		assertEquals(0.0, s.getAnchorPoint(DOWN).getX(), 0.00001);
		assertEquals(0.0, s.getAnchorPoint(DOWN).getY(), 0.00001);

		assertEquals(0.0, s.getAnchorPoint(CENTER).getX(), 0.00001);
		assertEquals(-117.5, s.getAnchorPoint(CENTER).getY(), 0.00001);

		assertEquals(0.0, s.getAnchorPoint(UP).getX(), 0.00001);
		assertEquals(-235.0, s.getAnchorPoint(UP).getY(), 0.00001);

		assertEquals(-91.0, s.getAnchorPoint(LEFT).getX(), 0.00001);
		assertEquals(-117.5, s.getAnchorPoint(LEFT).getY(), 0.00001);

		assertEquals(91.0, s.getAnchorPoint(SOUTHEAST).getX(), 0.00001);
		assertEquals(0-0, s.getAnchorPoint(SOUTHEAST).getY(), 0.00001);
	}

	@Test
	void getAbsAnchorPoint() {
		Shape s = new TestShape();

		assertEquals(42.0, s.getAbsAnchorPoint(CENTER).getX(), 0.00001);
		assertEquals(131.0, s.getAbsAnchorPoint(CENTER).getY(), 0.00001);

		assertEquals(42.0, s.getAbsAnchorPoint(UP).getX(), 0.00001);
		assertEquals(13.5, s.getAbsAnchorPoint(UP).getY(), 0.00001);

		assertEquals(-49.0, s.getAbsAnchorPoint(LEFT).getX(), 0.00001);
		assertEquals(131.0, s.getAbsAnchorPoint(LEFT).getY(), 0.00001);

		assertEquals(133.0, s.getAbsAnchorPoint(SOUTHEAST).getX(), 0.00001);
		assertEquals(248.5, s.getAbsAnchorPoint(SOUTHEAST).getY(), 0.00001);

		s.setAnchor(DOWN);

		assertEquals(42.0, s.getAbsAnchorPoint(CENTER).getX(), 0.00001);
		assertEquals(13.5, s.getAbsAnchorPoint(CENTER).getY(), 0.00001);

		assertEquals(42.0, s.getAbsAnchorPoint(UP).getX(), 0.00001);
		assertEquals(-104.0, s.getAbsAnchorPoint(UP).getY(), 0.00001);

		assertEquals(-49.0, s.getAbsAnchorPoint(LEFT).getX(), 0.00001);
		assertEquals(13.5, s.getAbsAnchorPoint(LEFT).getY(), 0.00001);

		assertEquals(133.0, s.getAbsAnchorPoint(SOUTHEAST).getX(), 0.00001);
		assertEquals(131.0, s.getAbsAnchorPoint(SOUTHEAST).getY(), 0.00001);
	}

	@Test
	void testMoveTo() {
		Shape s1 = new TestShape();
		Shape s2 = s1.copy();

		s1.moveTo( 100, 200);
		assertEquals(100.0, s1.x, 0.00001);
		assertEquals(200.0, s1.y, 0.00001);

		s2.moveTo(s1);
		assertEquals(100.0, s2.x, 0.00001);
		assertEquals(200.0, s2.y, 0.00001);

		s2.moveTo(s1, RIGHT);
		assertEquals(191.0, s2.x, 0.00001);
		assertEquals(200.0, s2.y, 0.00001);
		assertEquals(s2.getAbsAnchorPoint(CENTER), s1.getAbsAnchorPoint(RIGHT));
		assertEquals(s1.getAbsAnchorPoint(CENTER), s2.getAbsAnchorPoint(LEFT));

		s2.moveTo(s1, RIGHT, 10.0);
		assertEquals(201.0, s2.x, 0.00001);
		assertEquals(200.0, s2.y, 0.00001);

		s2.moveTo(s1, LEFT);
		assertEquals(9.0, s2.x, 0.00001);
		assertEquals(200.0, s2.y, 0.00001);

		s2.moveTo(s1, LEFT, 10.0);
		assertEquals(-1.0, s2.x, 0.00001);
		assertEquals(200.0, s2.y, 0.00001);
	}

	@Test
	void testAlignTo() {
		Shape s1 = new TestShape();
		Shape s2 = new TestShape();

		s1.moveTo(100, 200);

		s2.alignTo(s1, LEFT, 0.0);
		assertEquals(100.0, s2.getX());
		assertEquals(131.0, s2.getY());
		assertEquals(9.0, s2.getAbsAnchorPoint(LEFT).getX(), 0.00001);

		s2.moveTo(300, 400);
		s2.alignTo(s1, LEFT, 10.0);
		assertEquals(90.0, s2.getX());
		assertEquals(400.0, s2.getY());

		s2.alignTo(s1, SOUTHEAST, 0.0);
		assertEquals(100.0, s2.getX());
		assertEquals(200.0, s2.getY());

		s2.moveTo(0, 0);
		s2.alignTo(s1, DOWN, 24.0);
		assertEquals(0.0, s2.getX());
		assertEquals(224.0, s2.getY());

		s2.setAnchor(NORTHWEST);
		s2.moveTo(0, 0);
		s2.alignTo(s1, DOWN, 24.0);
		assertEquals(0.0, s2.getX());
		assertEquals(106.5, s2.getY());
	}

	@Test
	void testNextTo() {
		Shape s1 = new TestShape();
		Shape s2 = new TestShape();

		s2.nextTo(s1, RIGHT, 10.0);
		assertEquals(234.0, s2.getX());
		assertEquals(131.0, s2.getY());

		s2.nextTo(s1, LEFT, 10.0);
		assertEquals(-150.0, s2.getX());
		assertEquals(131.0, s2.getY());

		s2.nextTo(s1, SOUTHEAST, 10.0);
		assertEquals(234.0, s2.getX());
		assertEquals(376.0, s2.getY());
	}

}
