package schule.ngb.zm.ml;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DoubleMatrixTest {

	@Test
	void initializeOne() {
		DoubleMatrix m = new DoubleMatrix(4, 4);
		m.initializeOne();

		double[] ones = new double[]{1.0, 1.0, 1.0, 1.0};
		assertArrayEquals(ones, m.coefficients[0]);
		assertArrayEquals(ones, m.coefficients[1]);
		assertArrayEquals(ones, m.coefficients[2]);
		assertArrayEquals(ones, m.coefficients[3]);
	}

	@Test
	void initializeZero() {
		DoubleMatrix m = new DoubleMatrix(4, 4);
		m.initializeZero();

		double[] zeros = new double[]{0.0, 0.0, 0.0, 0.0};
		assertArrayEquals(zeros, m.coefficients[0]);
		assertArrayEquals(zeros, m.coefficients[1]);
		assertArrayEquals(zeros, m.coefficients[2]);
		assertArrayEquals(zeros, m.coefficients[3]);
	}

	@Test
	void initializeRandom() {
		DoubleMatrix m = new DoubleMatrix(4, 4);
		m.initializeRandom(-1, 1);

		assertTrue(Arrays.stream(m.coefficients[0]).allMatch((d) -> -1.0 <= d && d < 1.0));
		assertTrue(Arrays.stream(m.coefficients[1]).allMatch((d) -> -1.0 <= d && d < 1.0));
		assertTrue(Arrays.stream(m.coefficients[2]).allMatch((d) -> -1.0 <= d && d < 1.0));
		assertTrue(Arrays.stream(m.coefficients[3]).allMatch((d) -> -1.0 <= d && d < 1.0));
	}

}
