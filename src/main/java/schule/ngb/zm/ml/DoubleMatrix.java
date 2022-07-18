package schule.ngb.zm.ml;

import schule.ngb.zm.Constants;

import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;

// TODO: Move Math into Matrix class
// TODO: Implement support for optional sci libs
public class DoubleMatrix implements Matrix {

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

	public double[][] getCoefficients() {
		return coefficients;
	}

	public double get( int row, int col ) {
		return coefficients[row][col];
	}

	public Matrix set( int row, int col, double value ) {
		coefficients[row][col] = value;
		return this;
	}

	public Matrix initializeRandom() {
		coefficients = MLMath.matrixApply(coefficients, (d) -> Constants.randomGaussian());
		return this;
	}

	public Matrix initializeRandom( double lower, double upper ) {
		coefficients = MLMath.matrixApply(coefficients, (d) -> ((upper-lower) * (Constants.randomGaussian()+1) * .5) + lower);
		return this;
	}

	public Matrix initializeOne() {
		coefficients = MLMath.matrixApply(coefficients, (d) -> 1.0);
		return this;
	}

	public Matrix initializeZero() {
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
	public Matrix transpose() {
		coefficients = MLMath.matrixTranspose(coefficients);
		return this;
	}

	@Override
	public Matrix multiply( Matrix B ) {
		coefficients = MLMath.matrixMultiply(coefficients, B.getCoefficients());
		return this;
	}

	@Override
	public Matrix multiplyAddBias( Matrix B, Matrix C ) {
		double[] biases = Arrays.stream(C.getCoefficients()).mapToDouble((arr) -> arr[0]).toArray();
		coefficients = MLMath.biasAdd(
			MLMath.matrixMultiply(coefficients, B.getCoefficients()),
			biases
		);
		return this;
	}

	@Override
	public Matrix multiplyLeft( Matrix B ) {
		coefficients = MLMath.matrixMultiply(B.getCoefficients(), coefficients);
		return this;
	}

	@Override
	public Matrix add( Matrix B ) {
		coefficients = MLMath.matrixAdd(coefficients, B.getCoefficients());
		return this;
	}

	@Override
	public Matrix sub( Matrix B ) {
		coefficients = MLMath.matrixSub(coefficients, B.getCoefficients());
		return this;
	}

	@Override
	public Matrix scale( double scalar ) {
		return this;
	}

	@Override
	public Matrix scale( Matrix S ) {
		coefficients = MLMath.matrixScale(coefficients, S.getCoefficients());
		return this;
	}

	@Override
	public Matrix apply( DoubleUnaryOperator op ) {
		this.coefficients = MLMath.matrixApply(coefficients, op);
		return this;
	}

	@Override
	public Matrix duplicate() {
		return new DoubleMatrix(MLMath.copyMatrix(coefficients));
	}

}
