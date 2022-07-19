package schule.ngb.zm.ml;

import schule.ngb.zm.Constants;

import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;

// TODO: Move Math into Matrix class
// TODO: Implement support for optional sci libs
public class DoubleMatrix implements MLMatrix {

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
		this.coefficients = coefficients;
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
		coefficients = MLMath.matrixApply(coefficients, (d) -> Constants.randomGaussian());
		return this;
	}

	public MLMatrix initializeRandom( double lower, double upper ) {
		coefficients = MLMath.matrixApply(coefficients, (d) -> ((upper-lower) * (Constants.randomGaussian()+1) * .5) + lower);
		return this;
	}

	public MLMatrix initializeOne() {
		coefficients = MLMath.matrixApply(coefficients, (d) -> 1.0);
		return this;
	}

	public MLMatrix initializeZero() {
		coefficients = MLMath.matrixApply(coefficients, (d) -> 0.0);
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

	@Override
	public MLMatrix transpose() {
		coefficients = MLMath.matrixTranspose(coefficients);
		return this;
	}

	@Override
	public MLMatrix multiply( MLMatrix B ) {
		coefficients = MLMath.matrixMultiply(coefficients, B.coefficients());
		return this;
	}

	@Override
	public MLMatrix multiplyAddBias( MLMatrix B, MLMatrix C ) {
		double[] biases = Arrays.stream(C.coefficients()).mapToDouble(( arr) -> arr[0]).toArray();
		coefficients = MLMath.biasAdd(
			MLMath.matrixMultiply(coefficients, B.coefficients()),
			biases
		);
		return this;
	}

	@Override
	public MLMatrix multiplyLeft( MLMatrix B ) {
		coefficients = MLMath.matrixMultiply(B.coefficients(), coefficients);
		return this;
	}

	@Override
	public MLMatrix add( MLMatrix B ) {
		coefficients = MLMath.matrixAdd(coefficients, B.coefficients());
		return this;
	}

	@Override
	public MLMatrix sub( MLMatrix B ) {
		coefficients = MLMath.matrixSub(coefficients, B.coefficients());
		return this;
	}

	@Override
	public MLMatrix scale( double scalar ) {
		return this;
	}

	@Override
	public MLMatrix scale( MLMatrix S ) {
		coefficients = MLMath.matrixScale(coefficients, S.coefficients());
		return this;
	}

	@Override
	public MLMatrix apply( DoubleUnaryOperator op ) {
		this.coefficients = MLMath.matrixApply(coefficients, op);
		return this;
	}

	@Override
	public MLMatrix duplicate() {
		return new DoubleMatrix(MLMath.copyMatrix(coefficients));
	}

}
