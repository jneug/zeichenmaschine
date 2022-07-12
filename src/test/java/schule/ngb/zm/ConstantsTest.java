package schule.ngb.zm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstantsTest {

	@Test
	void min() {
	}

	@Test
	void max() {
		assertEquals(5.0, Constants.max(5.0, 0.0), 0.000001);
		assertEquals(5.0, Constants.max(5.0, 4.0), 0.000001);
		assertEquals(5.0, Constants.max(5.0, 5.0), 0.000001);
		assertEquals(100.0, Constants.max(100.0, -100.0), 0.000001);
		assertEquals(0.0, Constants.max(0.0, 0.0), 0.000001);

		assertEquals(5.0, Constants.max(new double[]{ 0.0, 1.0, 2.0, 3.0, 4.0, 5.0 }), 0.000001);
		assertEquals(5.0, Constants.max(new double[]{ 5.0, 5.0, 5.0, 5.0, 5.0 }), 0.000001);
		assertEquals(5.0, Constants.max(new double[]{ 5.0 }), 0.000001);

		assertThrows(IllegalArgumentException.class, () -> Constants.max(null));
		assertThrows(IllegalArgumentException.class, () -> Constants.max(new double[]{}));
	}

	@Test
	void abs() {
		assertEquals(5.0, Constants.abs(5.0), 0.000001);
		assertEquals(0.0, Constants.abs(0.0), 0.000001);
		assertEquals(105.0, Constants.abs(-105.0), 0.000001);
	}

	@Test
	void sign() {
		assertEquals(1.0, Constants.sign(5.0), 0.000001);
		assertEquals(0.0, Constants.sign(0.0), 0.000001);
		assertEquals(-1.0, Constants.sign(-105.0), 0.000001);
	}

	@Test
	void morph() {
		assertEquals(0.0, Constants.interpolate(0.0, 100.0, 0.0), 0.000001);
		assertEquals(10.0, Constants.interpolate(0.0, 100.0, 0.1), 0.000001);
		assertEquals(50.0, Constants.interpolate(0.0, 100.0, 0.5), 0.000001);
		assertEquals(100.0, Constants.interpolate(0.0, 100.0, 1.0), 0.000001);
		assertEquals(-100.0, Constants.interpolate(-100.0, 100.0, 0.0), 0.000001);
		assertEquals(0.0, Constants.interpolate(-100.0, 100.0, 0.5), 0.000001);
		assertEquals(100.0, Constants.interpolate(-100.0, 100.0, 1.0), 0.000001);
	}

	@Test
	void limit() {
		assertEquals(4, Constants.limit(4, 1, 5));
		assertEquals(1, Constants.limit(0, 1, 5));
		assertEquals(5, Constants.limit(6, 1, 5));
		assertEquals(1, Constants.limit(-6, 1, 5));
		assertEquals(-6, Constants.limit(-6, -10, 5));
		assertEquals(-10, Constants.limit(-15, -10, 5));
		assertEquals(-10, Constants.limit(-10, -10, 5));

		assertEquals(4.0, Constants.limit(4, 1, 5), 0.000001);
		assertEquals(1.3, Constants.limit(0, 1.3, 5), 0.000001);
		assertEquals(5.0, Constants.limit(6, 1, 5), 0.000001);
		assertEquals(1.2, Constants.limit(-6, 1.2, 5), 0.000001);
		assertEquals(-6.0, Constants.limit(-6, -10, 5), 0.000001);
		assertEquals(-10.0, Constants.limit(-15, -10, 5), 0.000001);
		assertEquals(-10.3, Constants.limit(-10.3, -10.3, 5), 0.000001);
	}

	@Test
	void b() {
		assertTrue(Constants.asBool(true));
		assertFalse(Constants.asBool(false));
		assertTrue(Constants.asBool(1));
		assertFalse(Constants.asBool(0));
		assertTrue(Constants.asBool(4.0));
		assertFalse(Constants.asBool(0.0));
		assertTrue(Constants.asBool(4.0f));
		assertFalse(Constants.asBool(0.0f));
		assertTrue(Constants.asBool(4L));
		assertFalse(Constants.asBool(0L));
		assertTrue(Constants.asBool("true"));
		assertTrue(Constants.asBool("True"));
		assertFalse(Constants.asBool("1"));
		assertFalse(Constants.asBool("false"));
		assertFalse(Constants.asBool("yes"));
		assertFalse(Constants.asBool("no"));
	}

	@Test
	void randomBool() {
		float t = 0f, f = 0f;
		for( int i = 0; i < 100000; i++ ) {
			if( Constants.randomBool() ) {
				t++;
			} else {
				f++;
			}
		}

		assertEquals(.5f, Math.abs(t/(t+f)), .01f);

		t = 0f;
		f = 0f;
		for( int i = 0; i < 100000; i++ ) {
			if( Constants.randomBool(.2) ) {
				t++;
			} else {
				f++;
			}
		}

		assertEquals(.2f, Math.abs(t/(t+f)), .01f);

		t = 0f;
		f = 0f;
		for( int i = 0; i < 100000; i++ ) {
			if( Constants.randomBool(80) ) {
				t++;
			} else {
				f++;
			}
		}

		assertEquals(.8f, Math.abs(t/(t+f)), .01f);
	}

}
