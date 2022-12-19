package schule.ngb.zm.ml;

import cern.colt.matrix.DoubleFactory2D;
import schule.ngb.zm.Constants;
import schule.ngb.zm.util.Log;

import java.util.function.DoubleUnaryOperator;

/**
 * Zentrale Klasse zur Erstellung neuer Matrizen. Generell sollten neue Matrizen
 * nicht direkt erstellt werden, sondern durch den Aufruf von
 * {@link #create(int, int)} oder {@link #create(double[][])}. Die Fabrik
 * ermittelt automatisch die beste verfügbare Implementierung und initialisiert
 * eine entsprechende Implementierung von {@link MLMatrix}.
 * <p>
 * Derzeit werden die optionale Bibliothek <a
 * href="https://dst.lbl.gov/ACSSoftware/colt/">Colt</a> und die interne
 * Implementierung {@link DoubleMatrix} unterstützt.
 */
public class MatrixFactory {

	/**
	 * Erstellt eine neue Matrix mit den angegebenen Dimensionen und
	 * initialisiert alle Werte mit 0.
	 *
	 * @param rows Anzahl der Zeilen.
	 * @param cols Anzahl der Spalten.
	 * @return Eine {@code rows} x {@code cols} Matrix.
	 */
	public static final MLMatrix create( int rows, int cols ) {
		try {
			return getMatrixType().getDeclaredConstructor(int.class, int.class).newInstance(rows, cols);
		} catch( Exception ex ) {
			LOG.error(ex, "Could not initialize matrix implementation for class <%s>. Using internal implementation.", matrixType);
		}
		return new DoubleMatrix(rows, cols);
	}

	/**
	 * Erstellt eine neue Matrix mit den Dimensionen des angegebenen Arrays und
	 * initialisiert die Werte mit den entsprechenden Werten des Arrays.
	 *
	 * @param values Die Werte der Matrix.
	 * @return Eine {@code values.length} x {@code values[0].length} Matrix mit
	 * 	den Werten des Arrays.
	 */
	public static final MLMatrix create( double[][] values ) {
		try {
			return getMatrixType().getDeclaredConstructor(double[][].class).newInstance((Object) values);
		} catch( Exception ex ) {
			LOG.error(ex, "Could not initialize matrix implementation for class <%s>. Using internal implementation.", matrixType);
		}
		return new DoubleMatrix(values);
	}

	/**
	 * Die verwendete {@link MLMatrix} Implementierung, aus der Matrizen erzeugt
	 * werden.
	 */
	static Class<? extends MLMatrix> matrixType = null;

	/**
	 * Ermittelt die beste verfügbare Implementierung von {@link MLMatrix}.
	 *
	 * @return Die verwendete {@link MLMatrix} Implementierung.
	 */
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

	/**
	 * Interner Wrapper der DoubleMatrix2D Klasse aus der Colt Bibliothek, um
	 * das {@link MLMatrix} Interface zu implementieren.
	 */
	static class ColtMatrix implements MLMatrix {

		cern.colt.matrix.DoubleMatrix2D matrix;

		public ColtMatrix( double[][] doubles ) {
			matrix = new cern.colt.matrix.impl.DenseDoubleMatrix2D(doubles);
		}

		public ColtMatrix( int rows, int cols ) {
			matrix = new cern.colt.matrix.impl.DenseDoubleMatrix2D(rows, cols);
		}

		public ColtMatrix( ColtMatrix matrix ) {
			this.matrix = matrix.matrix.copy();
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
		public MLMatrix initializeRandom() {
			return initializeRandom(-1.0, 1.0);
		}

		@Override
		public MLMatrix initializeRandom( double lower, double upper ) {
			matrix.assign(( d ) -> ((upper - lower) * Constants.random()) + lower);
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
		public MLMatrix duplicate() {
			ColtMatrix newMatrix = new ColtMatrix(matrix.rows(), matrix.columns());
			newMatrix.matrix.assign(this.matrix);
			return newMatrix;
		}

		@Override
		public MLMatrix multiplyTransposed( MLMatrix B ) {
			ColtMatrix CB = (ColtMatrix) B;
			ColtMatrix newMatrix = new ColtMatrix(0, 0);
			newMatrix.matrix = matrix.zMult(CB.matrix, null, 1.0, 0.0, false, true);
			return newMatrix;
		}

		@Override
		public MLMatrix multiplyAddBias( MLMatrix B, MLMatrix C ) {
			ColtMatrix CB = (ColtMatrix) B;
			ColtMatrix newMatrix = new ColtMatrix(0, 0);
			newMatrix.matrix = DoubleFactory2D.dense.repeat(((ColtMatrix) C).matrix, rows(), 1);
			matrix.zMult(CB.matrix, newMatrix.matrix, 1.0, 1.0, false, false);
			return newMatrix;
		}

		@Override
		public MLMatrix transposedMultiplyAndScale( final MLMatrix B, final double scalar ) {
			ColtMatrix CB = (ColtMatrix) B;
			ColtMatrix newMatrix = new ColtMatrix(0, 0);
			newMatrix.matrix = matrix.zMult(CB.matrix, null, scalar, 0.0, true, false);
			return newMatrix;
		}

		@Override
		public MLMatrix add( MLMatrix B ) {
			ColtMatrix CB = (ColtMatrix) B;
			ColtMatrix newMatrix = new ColtMatrix(this);
			newMatrix.matrix.assign(CB.matrix, ( d1, d2 ) -> d1 + d2);
			return newMatrix;
		}

		@Override
		public MLMatrix addInPlace( MLMatrix B ) {
			ColtMatrix CB = (ColtMatrix) B;
			matrix.assign(CB.matrix, ( d1, d2 ) -> d1 + d2);
			return this;
		}

		@Override
		public MLMatrix sub( MLMatrix B ) {
			ColtMatrix CB = (ColtMatrix) B;
			ColtMatrix newMatrix = new ColtMatrix(this);
			newMatrix.matrix.assign(CB.matrix, ( d1, d2 ) -> d1 - d2);
			return newMatrix;
		}

		@Override
		public MLMatrix colSums() {
			double[][] sums = new double[1][matrix.columns()];
			for( int c = 0; c < matrix.columns(); c++ ) {
				for( int r = 0; r < matrix.rows(); r++ ) {
					sums[0][c] += matrix.getQuick(r, c);
				}
			}
			return new ColtMatrix(sums);
		}

		@Override
		public MLMatrix scaleInPlace( double scalar ) {
			this.matrix.assign(( d ) -> d * scalar);
			return this;
		}

		@Override
		public MLMatrix scaleInPlace( MLMatrix S ) {
			this.matrix.forEachNonZero(( r, c, d ) -> d * S.get(r, c));
			return this;
		}

		@Override
		public MLMatrix apply( DoubleUnaryOperator op ) {
			ColtMatrix newMatrix = new ColtMatrix(matrix.rows(), matrix.columns());
			newMatrix.matrix.assign(matrix);
			newMatrix.matrix.assign(( d ) -> op.applyAsDouble(d));
			return newMatrix;
		}

		@Override
		public MLMatrix applyInPlace( DoubleUnaryOperator op ) {
			this.matrix.assign(( d ) -> op.applyAsDouble(d));
			return this;
		}

		@Override
		public String toString() {
			return matrix.toString();
		}

	}

}
