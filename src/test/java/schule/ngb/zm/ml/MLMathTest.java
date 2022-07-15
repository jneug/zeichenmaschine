package schule.ngb.zm.ml;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MLMathTest {

	@Test
	void matrixMultiply() {
		double[][] A = new double[][]{
			{1.0, 2.0, 3.0},
			{-5.0, -4.0, -3.0},
			{0.0, -10.0, 10.0}
		};
		double[][] B = new double[][]{
			{0.0, 1.0},
			{2.0, -2.0},
			{5.0, -10.0}
		};
		double[][] result = MLMath.matrixMultiply(A, B);

		assertNotNull(result);
		assertEquals(A.length, result.length);
		assertEquals(B[0].length, result[0].length);
		assertArrayEquals(new double[]{19.0, -33.0}, result[0]);
		assertArrayEquals(new double[]{-23.0, 33.0}, result[1]);
		assertArrayEquals(new double[]{30.0, -80.0}, result[2]);

		assertThrowsExactly(IllegalArgumentException.class, () -> MLMath.matrixMultiply(B, A));
	}

	@Test
	void matrixScale() {
		double[][] matrix = new double[][]{
			{1.0, 2.0, 3.0},
			{-5.0, -4.0, -3.0},
			{0.0, -10.0, 10.0}
		};
		double[][] scalars = new double[][]{
			{0.0, 1.0, -1.0},
			{2.0, -2.0, 10.0},
			{5.0, -10.0, 10.0}
		};
		double[][] result = MLMath.matrixScale(matrix, scalars);

		assertNotNull(result);
		assertNotSame(matrix, result);
		assertArrayEquals(new double[]{0.0, 2.0, -3.0}, result[0]);
		assertArrayEquals(new double[]{-10.0, 8.0, -30.0}, result[1]);
		assertArrayEquals(new double[]{0.0, 100.0, 100.0}, result[2]);
	}

	@Test
	void matrixApply() {
		double[][] matrix = new double[][]{
			{1.0, 2.0, 3.0},
			{-5.0, -4.0, -3.0},
			{0.0, -10.0, 10.0}
		};
		double[][] result = MLMath.matrixApply(matrix, (d) -> -1*d);

		assertNotNull(result);
		assertNotSame(matrix, result);
		assertArrayEquals(new double[]{-1.0, -2.0, -3.0}, result[0]);
		assertArrayEquals(new double[]{5.0, 4.0, 3.0}, result[1]);
		assertArrayEquals(new double[]{-0.0, 10.0, -10.0}, result[2]);
	}

	@Test
	void matrixSubtract() {
	}

	@Test
	void matrixAdd() {
	}

	@Test
	void matrixTranspose() {
		double[][] matrix = new double[][]{
			{1.0, 2.0, 3.0, 4.5},
			{-5.0, -4.0, -3.0, 2.1},
			{0.0, -10.0, 10.0, 0.9}
		};
		double[][] result = MLMath.matrixTranspose(matrix);

		assertNotNull(result);
		assertEquals(4, result.length);
		assertEquals(3, result[0].length);

		assertArrayEquals(new double[]{1.0, -5.0, 0.0}, result[0]);
		assertArrayEquals(new double[]{2.0, -4.0, -10.0}, result[1]);
		assertArrayEquals(new double[]{3.0, -3.0, 10.0}, result[2]);
		assertArrayEquals(new double[]{4.5, 2.1, 0.9}, result[3]);
	}

	@Test
	void normalize() {
	}

}
