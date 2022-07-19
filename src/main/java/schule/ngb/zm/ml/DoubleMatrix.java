package schule.ngb.zm.ml;

import schule.ngb.zm.Constants;

import java.util.function.DoubleUnaryOperator;

// TODO: Move Math into Matrix class
// TODO: Implement support for optional sci libs
public final class DoubleMatrix implements MLMatrix {

	private int columns, rows;

	double[] coefficients;

	public DoubleMatrix( int rows, int cols ) {
		this.rows = rows;
		this.columns = cols;
		coefficients = new double[rows * cols];
	}

	public DoubleMatrix( double[][] coefficients ) {
		this.rows = coefficients.length;
		this.columns = coefficients[0].length;
		this.coefficients = new double[rows * columns];
		for( int j = 0; j < columns; j++ ) {
			for( int i = 0; i < rows; i++ ) {
				this.coefficients[idx(i, j)] = coefficients[i][j];
			}
		}
	}

	public DoubleMatrix( DoubleMatrix other ) {
		this.rows = other.rows();
		this.columns = other.columns();
		this.coefficients = new double[rows * columns];
		System.arraycopy(
			other.coefficients, 0,
			this.coefficients, 0,
			rows * columns);
	}

	public int columns() {
		return columns;
	}

	public int rows() {
		return rows;
	}

	public double[][] coefficients() {
		return new double[rows][columns];
	}

	int idx( int r, int c ) {
		return c * rows + r;
	}

	public double get( int row, int col ) {
		return coefficients[idx(row, col)];
	}

	public MLMatrix set( int row, int col, double value ) {
		coefficients[idx(row, col)] = value;
		return this;
	}

	public MLMatrix initializeRandom() {
		return initializeRandom(-1.0, 1.0);
	}

	public MLMatrix initializeRandom( double lower, double upper ) {
		applyInPlace(( d ) -> ((upper - lower) * Constants.random()) + lower);
		return this;
	}

	public MLMatrix initializeOne() {
		applyInPlace(( d ) -> 1.0);
		return this;
	}

	public MLMatrix initializeZero() {
		applyInPlace(( d ) -> 0.0);
		return this;
	}

	@Override
	public MLMatrix duplicate() {
		return new DoubleMatrix(this);
	}

	@Override
	public MLMatrix multiplyTransposed( MLMatrix B ) {
		/*return new DoubleMatrix(IntStream.range(0, rows).parallel().mapToObj(
			( i ) -> IntStream.range(0, B.rows()).mapToDouble(
				( j ) -> IntStream.range(0, columns).mapToDouble(
					( k ) -> get(i, k) * B.get(j, k)
				).sum()
			).toArray()
		).toArray(double[][]::new));*/
		DoubleMatrix result = new DoubleMatrix(rows, B.rows());
		for( int i = 0; i < rows; i++ ) {
			for( int j = 0; j < B.rows(); j++ ) {
				result.coefficients[result.idx(i, j)] = 0.0;
				for( int k = 0; k < columns; k++ ) {
					result.coefficients[result.idx(i, j)] += get(i, k) * B.get(j, k);
				}
			}
		}
		return result;
	}

	@Override
	public MLMatrix multiplyAddBias( final MLMatrix B, final MLMatrix C ) {
		/*return new DoubleMatrix(IntStream.range(0, rows).parallel().mapToObj(
			( i ) -> IntStream.range(0, B.columns()).mapToDouble(
				( j ) -> IntStream.range(0, columns).mapToDouble(
					( k ) -> get(i, k) * B.get(k, j)
				).sum() + C.get(0, j)
			).toArray()
		).toArray(double[][]::new));*/
		DoubleMatrix result = new DoubleMatrix(rows, B.columns());
		for( int i = 0; i < rows; i++ ) {
			for( int j = 0; j < B.columns(); j++ ) {
				result.coefficients[result.idx(i, j)] = 0.0;
				for( int k = 0; k < columns; k++ ) {
					result.coefficients[result.idx(i, j)] += get(i, k) * B.get(k, j);
				}
				result.coefficients[result.idx(i, j)] += C.get(0, j);
			}
		}
		return result;
	}

	@Override
	public MLMatrix transposedMultiplyAndScale( final MLMatrix B, final double scalar ) {
		/*return new DoubleMatrix(IntStream.range(0, columns).parallel().mapToObj(
			( i ) -> IntStream.range(0, B.columns()).mapToDouble(
				( j ) -> IntStream.range(0, rows).mapToDouble(
					( k ) -> get(k, i) * B.get(k, j) * scalar
				).sum()
			).toArray()
		).toArray(double[][]::new));*/
		DoubleMatrix result = new DoubleMatrix(columns, B.columns());
		for( int i = 0; i < columns; i++ ) {
			for( int j = 0; j < B.columns(); j++ ) {
				result.coefficients[result.idx(i, j)] = 0.0;
				for( int k = 0; k < rows; k++ ) {
					result.coefficients[result.idx(i, j)] += get(k, i) * B.get(k, j);
				}
				result.coefficients[result.idx(i, j)] *= scalar;
			}
		}
		return result;
	}

	@Override
	public MLMatrix add( MLMatrix B ) {
		/*return new DoubleMatrix(IntStream.range(0, rows).parallel().mapToObj(
			( i ) -> IntStream.range(0, columns).mapToDouble(
				( j ) -> get(i, j) + B.get(i, j)
			).toArray()
		).toArray(double[][]::new));*/
		DoubleMatrix sum = new DoubleMatrix(rows, columns);
		for( int j = 0; j < columns; j++ ) {
			for( int i = 0; i < rows; i++ ) {
				sum.coefficients[idx(i, j)] = coefficients[idx(i, j)] + B.get(i, j);
			}
		}
		return sum;
	}

	@Override
	public MLMatrix addInPlace( MLMatrix B ) {
		for( int j = 0; j < columns; j++ ) {
			for( int i = 0; i < rows; i++ ) {
				coefficients[idx(i, j)] += B.get(i, j);
			}
		}
		return this;
	}

	@Override
	public MLMatrix sub( MLMatrix B ) {
		/*return new DoubleMatrix(IntStream.range(0, rows).parallel().mapToObj(
			( i ) -> IntStream.range(0, columns).mapToDouble(
				( j ) -> get(i, j) - B.get(i, j)
			).toArray()
		).toArray(double[][]::new));*/
		DoubleMatrix diff = new DoubleMatrix(rows, columns);
		for( int j = 0; j < columns; j++ ) {
			for( int i = 0; i < rows; i++ ) {
				diff.coefficients[idx(i, j)] = coefficients[idx(i, j)] - B.get(i, j);
			}
		}
		return diff;
	}

	@Override
	public MLMatrix colSums() {
		/*DoubleMatrix colSums = new DoubleMatrix(1, columns);
		colSums.coefficients = IntStream.range(0, columns).parallel().mapToDouble(
			( j ) -> IntStream.range(0, rows).mapToDouble(
				( i ) -> get(i, j)
			).sum()
		).toArray();
		return colSums;*/
		DoubleMatrix colSums = new DoubleMatrix(1, columns);
		for( int j = 0; j < columns; j++ ) {
			colSums.coefficients[j] = 0.0;
			for( int i = 0; i < rows; i++ ) {
				colSums.coefficients[j] += coefficients[idx(i, j)];
			}
		}
		return colSums;
	}

	@Override
	public MLMatrix scaleInPlace( final double scalar ) {
		for( int i = 0; i < coefficients.length; i++ ) {
			coefficients[i] *= scalar;
		}
		return this;
	}

	@Override
	public MLMatrix scaleInPlace( final MLMatrix S ) {
		for( int j = 0; j < columns; j++ ) {
			for( int i = 0; i < rows; i++ ) {
				coefficients[idx(i, j)] *= S.get(i, j);
			}
		}
		return this;
	}

	@Override
	public MLMatrix apply( DoubleUnaryOperator op ) {
		DoubleMatrix result = new DoubleMatrix(rows, columns);
		for( int i = 0; i < coefficients.length; i++ ) {
			result.coefficients[i] = op.applyAsDouble(coefficients[i]);
		}
		return result;
	}

	@Override
	public MLMatrix applyInPlace( DoubleUnaryOperator op ) {
		for( int i = 0; i < coefficients.length; i++ ) {
			coefficients[i] = op.applyAsDouble(coefficients[i]);
		}
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(rows);
		sb.append(" x ");
		sb.append(columns);
		sb.append(" Matrix");
		sb.append('\n');
		for( int i = 0; i < rows; i++ ) {
			for( int j = 0; j < columns; j++ ) {
				sb.append(get(i, j));
				if( j < columns - 1 )
					sb.append(' ');
			}
			sb.append('\n');
		}
		return sb.toString();
	}

}
