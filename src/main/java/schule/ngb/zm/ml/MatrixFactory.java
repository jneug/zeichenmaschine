package schule.ngb.zm.ml;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import schule.ngb.zm.Constants;
import schule.ngb.zm.util.Log;

import java.util.function.DoubleUnaryOperator;

public class MatrixFactory {

	public static void main( String[] args ) {
		System.out.println(
			MatrixFactory.create(new double[][]{ {1.0, 0.0}, {0.0, 1.0} }).toString()
		);
	}

	public static final MLMatrix create( int rows, int cols ) {
		try {
			return getMatrixType().getDeclaredConstructor(int.class, int.class).newInstance(rows, cols);
		} catch( Exception ex ) {
			LOG.error(ex, "Could not initialize matrix implementation for class <%s>. Using internal implementation.", matrixType);
		}
		return new DoubleMatrix(rows, cols);
	}

	public static final MLMatrix create( double[][] values ) {
		try {
			return getMatrixType().getDeclaredConstructor(double[][].class).newInstance((Object)values);
		} catch( Exception ex ) {
			LOG.error(ex, "Could not initialize matrix implementation for class <%s>. Using internal implementation.", matrixType);
		}
		return new DoubleMatrix(values);
	}

	private static Class<? extends MLMatrix> matrixType = null;

	private static final Class<? extends MLMatrix> getMatrixType() {
		if( matrixType == null ) {
			try {
				Class<?> clazz = Class.forName("cern.colt.matrix.impl.DenseDoubleMatrix2D", false, MatrixFactory.class.getClassLoader());
				matrixType = ColtMatrix.class;
				LOG.info("Colt library found. Using <cern.colt.matrix.impl.DenseDoubleMatrix2D> as matrix implementation.");
			} catch( ClassNotFoundException e ) {
				LOG.info("Colt library not found. Falling back on internal implementation.");
				matrixType = DoubleMatrix.class;
			}
		}
		return matrixType;
	}

	private static final Log LOG = Log.getLogger(MatrixFactory.class);

	static class ColtMatrix implements MLMatrix {

		cern.colt.matrix.DoubleMatrix2D matrix;

		public ColtMatrix( double[][] doubles ) {
			matrix = new cern.colt.matrix.impl.DenseDoubleMatrix2D(doubles);
		}

		public ColtMatrix( int rows, int cols ) {
			matrix = new cern.colt.matrix.impl.DenseDoubleMatrix2D(rows, cols);
		}

		@Override
		public int columns() {
			return matrix.columns();
		}

		@Override
		public int rows() {
			return matrix.rows();
		}

		@Override
		public double get( int row, int col ) {
			return matrix.get(row, col);
		}

		@Override
		public MLMatrix set( int row, int col, double value ) {
			matrix.set(row, col, value);
			return this;
		}

		@Override
		public double[][] coefficients() {
			return this.matrix.toArray();
		}

		@Override
		public MLMatrix initializeRandom() {
			matrix.assign((d) -> Constants.randomGaussian());
			return this;
		}

		@Override
		public MLMatrix initializeRandom( double lower, double upper ) {
			matrix.assign((d) -> ((upper-lower) * (Constants.randomGaussian()+1) * .5) + lower);
			return this;
		}

		@Override
		public MLMatrix initializeOne() {
			this.matrix.assign(1.0);
			return this;
		}

		@Override
		public MLMatrix initializeZero() {
			this.matrix.assign(0.0);
			return this;
		}

		@Override
		public MLMatrix apply( DoubleUnaryOperator op ) {
			this.matrix.assign((d) -> op.applyAsDouble(d));
			return this;
		}

		@Override
		public MLMatrix transpose() {
			this.matrix = cern.colt.matrix.linalg.Algebra.DEFAULT.transpose(this.matrix);
			return this;
		}

		@Override
		public MLMatrix multiply( MLMatrix B ) {
			ColtMatrix CB = (ColtMatrix)B;
			this.matrix = cern.colt.matrix.linalg.Algebra.DEFAULT.mult(matrix, CB.matrix);
			return this;
		}

		@Override
		public MLMatrix multiplyLeft( MLMatrix B ) {
			ColtMatrix CB = (ColtMatrix)B;
			this.matrix = cern.colt.matrix.linalg.Algebra.DEFAULT.mult(CB.matrix, matrix);
			return this;
		}

		@Override
		public MLMatrix multiplyAddBias( MLMatrix B, MLMatrix C ) {
			ColtMatrix CB = (ColtMatrix)B;
			this.matrix = cern.colt.matrix.linalg.Algebra.DEFAULT.mult(matrix, CB.matrix);
			// TODO: add bias
			return this;
		}

		@Override
		public MLMatrix add( MLMatrix B ) {
			ColtMatrix CB = (ColtMatrix)B;
			matrix.assign(CB.matrix, (d1,d2) -> d1+d2);
			return this;
		}

		@Override
		public MLMatrix sub( MLMatrix B ) {
			ColtMatrix CB = (ColtMatrix)B;
			matrix.assign(CB.matrix, (d1,d2) -> d1-d2);
			return this;
		}

		@Override
		public MLMatrix scale( double scalar ) {
			this.matrix.assign((d) -> d*scalar);
			return this;
		}

		@Override
		public MLMatrix scale( MLMatrix S ) {
			this.matrix.forEachNonZero((r, c, d) -> d * S.get(r, c));
			return this;
		}

		@Override
		public MLMatrix duplicate() {
			ColtMatrix newMatrix = new ColtMatrix(matrix.rows(), matrix.columns());
			newMatrix.matrix.assign(this.matrix);
			return newMatrix;
		}

		@Override
		public String toString() {
			return matrix.toString();
		}

	}

}
