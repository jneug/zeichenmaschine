package schule.ngb.zm.ml;

import schule.ngb.zm.Constants;

import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.IntStream;

// TODO: Move Math into Matrix class
// TODO: Implement support for optional sci libs
public final class DoubleMatrix implements MLMatrix {

	private int columns, rows;

	double[][] coefficients;

	public DoubleMatrix( int rows, int cols )  {
		this.rows = rows;
		this.columns = cols;
		coefficients = new double[rows][cols];
	}

	public DoubleMatrix( double[][] coefficients ) {
		this.rows = coefficients.length;
		this.columns = coefficients[0].length;
		this.coefficients = Arrays.stream(coefficients)
				.map(double[]::clone)
				.toArray(double[][]::new);
	}

	public int columns() {
		return columns;
	}

	public int rows() {
		return rows;
	}

	public double[][] coefficients() {
		return coefficients;
	}

	public double get( int row, int col ) {
		return coefficients[row][col];
	}

	public MLMatrix set( int row, int col, double value ) {
		coefficients[row][col] = value;
		return this;
	}

	public MLMatrix initializeRandom() {
		return initializeRandom(-1.0, 1.0);
	}

	public MLMatrix initializeRandom( double lower, double upper ) {
		applyInPlace((d) -> ((upper-lower) * Constants.random()) + lower);
		return this;
	}

	public MLMatrix initializeOne() {
		applyInPlace((d) -> 1.0);
		return this;
	}

	public MLMatrix initializeZero() {
		applyInPlace((d) -> 0.0);
		return this;
	}

	@Override
	public MLMatrix duplicate() {
		return new DoubleMatrix(coefficients);
	}

	@Override
	public MLMatrix multiplyTransposed( MLMatrix B ) {
		return new DoubleMatrix(IntStream.range(0, rows).parallel().mapToObj(
			( i ) -> IntStream.range(0, B.rows()).mapToDouble(
				( j ) ->  IntStream.range(0, columns).mapToDouble(
					(k) -> coefficients[i][k]*B.get(j,k)
				).sum()
			).toArray()
		).toArray(double[][]::new));
	}

	@Override
	public MLMatrix multiplyAddBias( final MLMatrix B, final MLMatrix C ) {
		return new DoubleMatrix(IntStream.range(0, rows).parallel().mapToObj(
			( i ) -> IntStream.range(0, B.columns()).mapToDouble(
				( j ) ->  IntStream.range(0, columns).mapToDouble(
					(k) -> coefficients[i][k]*B.get(k,j)
				).sum() + C.get(0, j)
			).toArray()
		).toArray(double[][]::new));
	}

	@Override
	public MLMatrix transposedMultiplyAndScale( final MLMatrix B, final double scalar ) {
		return new DoubleMatrix(IntStream.range(0, columns).parallel().mapToObj(
			( i ) -> IntStream.range(0, B.columns()).mapToDouble(
				( j ) -> IntStream.range(0, rows).mapToDouble(
					(k) -> coefficients[k][i]*B.get(k,j)*scalar
				).sum()
			).toArray()
		).toArray(double[][]::new));
	}

	@Override
	public MLMatrix add( MLMatrix B ) {
		return new DoubleMatrix(IntStream.range(0, rows).parallel().mapToObj(
			( i ) -> IntStream.range(0, columns).mapToDouble(
				( j ) -> coefficients[i][j] + B.get(i, j)
			).toArray()
		).toArray(double[][]::new));
	}

	@Override
	public MLMatrix addInPlace( MLMatrix B ) {
		coefficients = IntStream.range(0, rows).parallel().mapToObj(
			( i ) -> IntStream.range(0, columns).mapToDouble(
				( j ) -> coefficients[i][j] + B.get(i, j)
			).toArray()
		).toArray(double[][]::new);
		return this;
	}

	@Override
	public MLMatrix sub( MLMatrix B ) {
		return new DoubleMatrix(IntStream.range(0, rows).parallel().mapToObj(
			( i ) -> IntStream.range(0, columns).mapToDouble(
				( j ) -> coefficients[i][j] - B.get(i, j)
			).toArray()
		).toArray(double[][]::new));
	}

	@Override
	public MLMatrix colSums() {
		double[][] sums = new double[1][columns];
		for( int c = 0; c < columns; c++ ) {
			for( int r = 0; r < rows; r++ ) {
				sums[0][c] += coefficients[r][c];
			}
		}
		return new DoubleMatrix(sums);
	}

	@Override
	public MLMatrix scaleInPlace( final double scalar ) {
		coefficients = Arrays.stream(coefficients).parallel().map(
			( arr ) -> Arrays.stream(arr).map(
				(d) -> d * scalar
			).toArray()
		).toArray(double[][]::new);
		return this;
	}

	@Override
	public MLMatrix scaleInPlace( final MLMatrix S ) {
		coefficients = IntStream.range(0, coefficients.length).parallel().mapToObj(
			( i ) -> IntStream.range(0, coefficients[i].length).mapToDouble(
				( j ) -> coefficients[i][j] * S.get(i, j)
			).toArray()
		).toArray(double[][]::new);
		return this;
	}

	@Override
	public MLMatrix apply( DoubleUnaryOperator op ) {
		return new DoubleMatrix(Arrays.stream(coefficients).parallel().map(
			( arr ) -> Arrays.stream(arr).map(op).toArray()
		).toArray(double[][]::new));
	}

	@Override
	public MLMatrix applyInPlace( DoubleUnaryOperator op ) {
		this.coefficients = Arrays.stream(coefficients).parallel().map(
			( arr ) -> Arrays.stream(arr).map(op).toArray()
		).toArray(double[][]::new);
		return this;
	}

	@Override
	public String toString() {
		//return Arrays.deepToString(coefficients);
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		sb.append('\n');
		for( int i = 0; i < coefficients.length; i++ ) {
			sb.append('\t');
			sb.append(Arrays.toString(coefficients[i]));
			sb.append('\n');
		}
		sb.append(']');

		return sb.toString();
	}

}
