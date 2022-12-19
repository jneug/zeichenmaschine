package schule.ngb.zm.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

	@Test
	void requireNotNull() {
		StringBuilder sb = new StringBuilder("Message");

		Object o1 = new Object();
		assertEquals(o1, Validator.requireNotNull(o1, "content"));
		assertEquals(o1, Validator.requireNotNull(o1, "content", "Message"));
		assertEquals(o1, Validator.requireNotNull(o1, "content", sb));
		assertEquals(o1, Validator.requireNotNull(o1, ()->"Message"));

		String o2 = null;
		assertThrowsExactly(NullPointerException.class, () -> Validator.requireNotNull(o2, "content"));
		assertThrowsExactly(NullPointerException.class, () -> Validator.requireNotNull(o2, "content", "Message"));
		assertThrowsExactly(NullPointerException.class, () -> Validator.requireNotNull(o2, ()->"Message"));
	}

	@Test
	void requireNotEmpty() {
		StringBuilder sb = new StringBuilder("Message");

		String s1 = "Content";
		assertEquals(s1, Validator.requireNotEmpty(s1, "content"));
		assertEquals(s1, Validator.requireNotEmpty(s1, "content", "Message"));
		assertEquals(s1, Validator.requireNotEmpty(s1, "content", sb));
		assertEquals(s1, Validator.requireNotEmpty(s1, ()->"Message"));

		String s2 = "";
		assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requireNotEmpty(s2, "content"));
		assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requireNotEmpty(s2, "content", "Message"));
		assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requireNotEmpty(s2, ()->"Message"));

	}

	@Test
	void requirePositive() {
		int i = 1;
		assertEquals(i, Validator.requirePositive(i));
		assertEquals(i, Validator.requirePositive(i, "Message"));
		assertEquals(i, Validator.requirePositive(i, ()->"Message"));

		assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requirePositive(i-1));
		assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requirePositive(i-1, "Message"));
		assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requirePositive(i-1, ()->"Message"));

		double k = 1.0;
		assertEquals(k, Validator.requirePositive(i));
		assertEquals(k, Validator.requirePositive(i, "Message"));
		assertEquals(k, Validator.requirePositive(i, ()->"Message"));

		assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requirePositive(k-1.0));
		assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requirePositive(k-1.0, "Message"));
		assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requirePositive(k-1.0, ()->"Message"));
	}

	@Test
	void requireNotNegative() {
		int i = 0;
		assertEquals(i, Validator.requireNotNegative(i));
		assertEquals(i, Validator.requireNotNegative(i, "Message"));
		assertEquals(i, Validator.requireNotNegative(i, ()->"Message"));

		assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requireNotNegative(i-1));
		assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requireNotNegative(i-1, "Message"));
		assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requireNotNegative(i-1, ()->"Message"));

		double k = 0.0;
		assertEquals(k, Validator.requireNotNegative(i));
		assertEquals(k, Validator.requireNotNegative(i, "Message"));
		assertEquals(k, Validator.requireNotNegative(i, ()->"Message"));

		assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requireNotNegative(k-1.0));
		assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requireNotNegative(k-1.0, "Message"));
		assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requireNotNegative(k-1.0, ()->"Message"));
	}

	@Test
	void requireInRange() {
		int min = 50, max = 100;
		for( int ii = min; ii < max; ii++ ) {
			final int i = ii;
			assertEquals(i, Validator.requireInRange(i, min, max));
			assertEquals(i, Validator.requireInRange(i, min, max, "Message"));
			assertEquals(i, Validator.requireInRange(i, min, max, () -> "Message"));
		}
		for( int ii = 0; ii < min; ii++ ) {
			final int i = ii;
			assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requireInRange(i, min, max));
			assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requireInRange(i, min, max));
			assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requireInRange(i, min, max));
		}
		for( int ii = max+1; ii < 2*max; ii++ ) {
			final int i = ii;
			assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requireInRange(i, min, max));
			assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requireInRange(i, min, max));
			assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requireInRange(i, min, max));
		}

		double dmin = 50, dmax = 60;
		for( double dd = dmin; dd < dmax; dd+=0.1 ) {
			final double d = dd;
			assertEquals(d, Validator.requireInRange(d, dmin, dmax));
			assertEquals(d, Validator.requireInRange(d, dmin, dmax, "Message"));
			assertEquals(d, Validator.requireInRange(d, dmin, dmax, () -> "Message"));
		}
		for( double dd = dmin-10.0; dd < dmin; dd+=0.1 ) {
			final double d = dd;
			assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requireInRange(d, dmin, dmax));
			assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requireInRange(d, dmin, dmax));
			assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requireInRange(d, dmin, dmax));
		}
		for( double dd = dmax+0.1; dd < dmax+10.0; dd+=0.1 ) {
			final double d = dd;
			assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requireInRange(d, dmin, dmax));
			assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requireInRange(d, dmin, dmax));
			assertThrowsExactly(IllegalArgumentException.class, () -> Validator.requireInRange(d, dmin, dmax));
		}
	}

}
