package schule.ngb.zm.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoiseTest {

	@Test
	void noise() {
		int N = 1000;

		Noise perlin = new Noise();
		Noise perlin1 = new Noise(1001);
		Noise perlin2 = new Noise(2002);
		Noise perlin3 = new Noise(2002);

		int pp1 = 0, p1p2 = 0;
		for( int i = 1; i < N; i++ ) {
			double x = i * 0.005;

			assertInRange(perlin.noise(x));
			assertInRange(perlin1.noise(x));
			assertInRange(perlin2.noise(x));
			if( perlin.noise(x) == perlin1.noise(x) ) {
				pp1++;
			}
			if( perlin1.noise(x) == perlin2.noise(x) ) {
				p1p2++;
			}
			assertEquals(perlin2.noise(x), perlin3.noise(x), "perlin2 and perlin3 should be equal for input " + x);
		}
		assertTrue(pp1 < N * 0.75, "perlin and perlin1 should not be equal more than 75% (was " + pp1 / 1000.0 + ")");
		assertTrue(p1p2 < N * 0.75, "perlin1 and perlin2 should not be equal more than 75% (was " + p1p2 / 1000.0 + ")");
	}

	private void assertInRange( double d ) {
		assertTrue(0.0 <= d && 1.0 >= d);
	}

	private void assertInRange( double d, double min, double max ) {
		assertTrue(min <= d && max >= d);
	}

	@Test
	void noise2d() {
		int N = 100;

		Noise perlin = new Noise();
		Noise perlin1 = new Noise(1001);
		Noise perlin2 = new Noise(2002);
		Noise perlin3 = new Noise(2002);

		int pp1 = 0, p1p2 = 0;
		for( int i = 1; i < N; i++ ) {
			for( int j = 0; j < N; j++ ) {
				double x = i * 0.005;
				double y = j * 0.001;

				assertInRange(perlin.noise(x, y));
				assertInRange(perlin1.noise(x, y));
				assertInRange(perlin2.noise(x, y));
				if( perlin.noise(x, y) == perlin1.noise(x, y) ) {
					pp1++;
				}
				if( perlin1.noise(x, y) == perlin2.noise(x, y) ) {
					p1p2++;
				}
				assertEquals(perlin2.noise(x, y), perlin3.noise(x, y), "perlin2 and perlin3 should be equal for input " + x + "," + y);
			}
		}
		assertTrue(pp1 < N * N * 0.75, "perlin and perlin1 should not be equal more than 75% (was " + (pp1 / 1.0 * N * N) + ")");
		assertTrue(p1p2 < N * N * 0.75, "perlin1 and perlin2 should not be equal more than 75% (was " + (p1p2 / 1.0 * N * N) + ")");
	}

	@Test
	void noise3d() {
		int N = 100;

		Noise perlin = new Noise();
		Noise perlin1 = new Noise(1001);
		Noise perlin2 = new Noise(2002);
		Noise perlin3 = new Noise(2002);

		int pp1 = 0, p1p2 = 0;
		for( int i = 1; i < N; i++ ) {
			for( int j = 0; j < N; j++ ) {
				for( int k = 0; k < N; k++ ) {
					double x = i * 0.005;
					double y = j * 0.001;
					double z = k * 0.004;

					assertInRange(perlin.noise(x, y, z));
					assertInRange(perlin1.noise(x, y, z));
					assertInRange(perlin2.noise(x, y, z));
					if( perlin.noise(x, y, z) == perlin1.noise(x, y, z) ) {
						pp1++;
					}
					if( perlin1.noise(x, y, z) == perlin2.noise(x, y, z) ) {
						p1p2++;
					}
					assertEquals(perlin2.noise(x, y, z), perlin3.noise(x, y, z), "perlin2 and perlin3 should be equal for input " + x + "," + y);
				}
			}
		}
		assertTrue(pp1 < N * N * N* 0.75, "perlin and perlin1 should not be equal more than 75% (was " + (pp1 / 1.0 * N * N * N) + ")");
		assertTrue(p1p2 < N * N * N * 0.75, "perlin1 and perlin2 should not be equal more than 75% (was " + (p1p2 / 1.0 * N * N * N) + ")");
	}

	@Test
	void range() {
		Noise perlin = new Noise(1001);
		perlin.setRange(100, 255);

		for( int i = 0; i < 1000; i++ ) {
			assertInRange(perlin.noise(i * 0.005), 100, 255);
		}

		perlin.setRange(-100, 100);

		for( int i = 0; i < 1000; i++ ) {
			assertInRange(perlin.noise(i * 0.005), -100, 100);
		}
	}

}
