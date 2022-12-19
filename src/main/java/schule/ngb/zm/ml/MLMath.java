package schule.ngb.zm.ml;

import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.IntStream;

// See https://github.com/wheresvic/neuralnet
public final class MLMath {

	public static double sigmoid( double x ) {
		return 1 / (1 + Math.exp(-x));
	}

	public static double sigmoidDerivative( double x ) {
		return x * (1 - x);
	}

	public static double tanh( double x ) {
		return Math.tanh(x);
	}

	public static double tanhDerivative( double x ) {
		return 1 - Math.tanh(x) * Math.tanh(x);
	}


	public static double[] normalize( double[] vector ) {
		final double sum = Arrays.stream(vector).sum();
		return Arrays.stream(vector).map(( d ) -> d / sum).toArray();
	}

	public static double[][] matrixMultiply( double[][] A, double[][] B ) {
		int a = A.length, b = A[0].length, c = B[0].length;
		if( B.length != b ) {
			throw new IllegalArgumentException(
				String.format("Matrix A needs equal columns to matrix B rows. (Currently <%d> vs <%d>)", a, B.length)
			);
		}

		return IntStream.range(0, a).parallel().mapToObj(
			( i ) -> IntStream.range(0, c).mapToDouble(
				( j ) -> IntStream.range(0, b).mapToDouble(
					( k ) -> A[i][k] * B[k][j]
				).sum()
			).toArray()
		).toArray(double[][]::new);
	}

	public static double[][] matrixScale( final double[][] A, final double[][] S ) {
		if( A.length != S.length || A[0].length != S[0].length ) {
			throw new IllegalArgumentException("Matrices need to be same size.");
		}

		return IntStream.range(0, A.length).parallel().mapToObj(
			( i ) -> IntStream.range(0, A[i].length).mapToDouble(
				( j ) -> A[i][j] * S[i][j]
			).toArray()
		).toArray(double[][]::new);
	}

	public static double[][] matrixSub( double[][] A, double[][] B ) {
		if( A.length != B.length || A[0].length != B[0].length ) {
			throw new IllegalArgumentException("Cannot subtract unequal matrices");
		}

		return IntStream.range(0, A.length).parallel().mapToObj(
			( i ) -> IntStream.range(0, A[i].length).mapToDouble(
				( j ) -> A[i][j] - B[i][j]
			).toArray()
		).toArray(double[][]::new);
	}

	public static double[][] matrixAdd( double[][] A, double[][] B ) {
		if( A.length != B.length || A[0].length != B[0].length ) {
			throw new IllegalArgumentException("Cannot add unequal matrices");
		}

		return IntStream.range(0, A.length).parallel().mapToObj(
			( i ) -> IntStream.range(0, A[i].length).mapToDouble(
				( j ) -> A[i][j] + B[i][j]
			).toArray()
		).toArray(double[][]::new);
	}

	public static double[][] matrixTranspose( double[][] matrix ) {
		int a = matrix.length, b = matrix[0].length;

		double[][] result = new double[matrix[0].length][matrix.length];
		for( int i = 0; i < a; i++ ) {
			for( int j = 0; j < b; ++j ) {
				result[j][i] = matrix[i][j];
			}
		}

		return result;
	}

	public static double[][] matrixApply( double[][] A, DoubleUnaryOperator op ) {
		return Arrays.stream(A).parallel().map(
			( arr ) -> Arrays.stream(arr).map(op).toArray()
		).toArray(double[][]::new);
	}

	public static double[][] copyMatrix( double[][] matrix ) {
		/*return Arrays.stream(matrix).map(
			(arr) -> Arrays.copyOf(arr, arr.length)
		).toArray(double[][]::new);*/

		double[][] result = new double[matrix.length][matrix[0].length];
		for( int i = 0; i < matrix.length; i++ ) {
			result[i] = Arrays.copyOf(matrix[i], matrix[i].length);
		}
		return result;
	}

	public static double[] toVector( double[][] matrix ) {
		return Arrays.stream(matrix).mapToDouble(
			( arr ) -> arr[0]
		).toArray();
	}

	public static double[][] toMatrix( double[] vector ) {
		return Arrays.stream(vector).mapToObj(
			( d ) -> new double[]{d}
		).toArray(double[][]::new);
	}

	public static double entropy(double[][] A, double[][] Y, int batch_size) {
		int m = A.length;
		int n = A[0].length;
		double[][] z = new double[m][n];

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				z[i][j] = (Y[i][j] * Math.log(A[i][j])) + ((1 - Y[i][j]) * Math.log(1 - A[i][j]));
			}
		}

		double sum = 0;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				sum += z[i][j];
			}
		}
		return -sum / batch_size;
	}

	public static double[][] biasAdd( double[][] A, double[] V ) {
		if( A[0].length != V.length ) {
			throw new IllegalArgumentException("Can't add bias vector to matrix with wrong column count");
		}

		double[][] result = new double[A.length][A[0].length];
		for( int j = 0; j < A[0].length; j++ ) {
			for( int i = 0; i < A.length; i++ ) {
				result[i][j] = A[i][j] + V[j];
			}
		}

		return result;
	}

	public static double[] biasAdjust( double[] biases, double[][] delta ) {
		if( biases.length != delta[0].length ) {
			throw new IllegalArgumentException("Can't add adjust bias vector by delta with wrong column count");
		}

		double[] result = new double[biases.length];
		for( int j = 0; j < delta[0].length; j++ ) {
			for( int i = 0; i < delta.length; i++ ) {
				result[j] += biases[j] - delta[i][j];
			}
			result[j] /= delta.length;
		}

		return result;
	}

	private MLMath() {

	}

}
