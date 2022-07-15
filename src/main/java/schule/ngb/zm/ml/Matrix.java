package schule.ngb.zm.ml;

import schule.ngb.zm.Constants;

import java.util.Arrays;

// TODO: Move Math into Matrix class
// TODO: Implement support for optional sci libs
public class Matrix {

	private int columns, rows;

	double[][] coefficients;

	public Matrix( int rows, int cols )  {
		this.rows = rows;
		this.columns = cols;
		coefficients = new double[rows][cols];
	}

	public Matrix( double[][] coefficients ) {
		this.coefficients = coefficients;
		this.rows = coefficients.length;
		this.columns = coefficients[0].length;
	}

	public int getColumns() {
		return columns;
	}

	public int getRows() {
		return rows;
	}

	public double[][] getCoefficients() {
		return coefficients;
	}

	public double get( int row, int col ) {
		return coefficients[row][col];
	}

	public void initializeRandom() {
		coefficients = MLMath.matrixApply(coefficients, (d) -> Constants.randomGaussian());
	}

	public void initializeRandom( double lower, double upper ) {
		coefficients = MLMath.matrixApply(coefficients, (d) -> ((upper-lower) * (Constants.randomGaussian()+1) * .5) + lower);
	}

	public void initializeIdentity() {
		initializeZero();
		for( int i = 0; i < Math.min(rows, columns); i++ ) {
			this.coefficients[i][i] = 1.0;
		}
	}

	public void initializeOne() {
		coefficients = MLMath.matrixApply(coefficients, (d) -> 1.0);
	}

	public void initializeZero() {
		coefficients = MLMath.matrixApply(coefficients, (d) -> 0.0);
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
