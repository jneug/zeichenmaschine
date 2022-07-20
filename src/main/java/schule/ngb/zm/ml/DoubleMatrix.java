package schule.ngb.zm.ml;

import schule.ngb.zm.Constants;

import java.util.function.DoubleUnaryOperator;

/**
 * Eine einfache Implementierung der {@link MLMatrix} zur Verwendung in
 * {@link NeuralNetwork}s.
 * <p>
 * Diese Klasse stellt die interne Implementierung der Matrixoperationen dar,
 * die zur Berechnung der Gewichte in einem {@link NeuronLayer} notwendig sind.
 * <p>
 * Die Klasse ist nur minimal optimiert und sollte nur für kleine Netze
 * verwendet werden. Für größere Netze sollte auf eine der optionalen
 * Bibliotheken wie
 * <a href="">Colt</a> zurückgegriffen werden.
 */
public final class DoubleMatrix implements MLMatrix {

	/**
	 * Anzahl Zeilen der Matrix.
	 */
	private int rows;

	/**
	 * Anzahl Spalten der Matrix.
	 */
	private int columns;

	/**
	 * Die Koeffizienten der Matrix.
	 * <p>
	 * Um den Overhead bei Speicher und Zugriffszeiten von zweidimensionalen
	 * Arrays zu vermeiden wird ein eindimensionales Array verwendet und die
	 * Indizes mit Spaltenpriorität berechnet. Der Index i des Koeffizienten
	 * {@code r,c} in Zeile {@code r} und Spalte {@code c} wird bestimmt durch
	 * <pre>
	 * i = c * rows + r
	 * </pre>
	 * <p>
	 * Die Werte einer Spalte liegen also hintereinander im Array. Dies sollte
	 * einen leichten Vorteil bei der {@link #colSums() Spaltensummen} geben.
	 * Generell sollte eine Iteration über die Matrix der Form
	 * <pre><code>
	 * for( int j = 0; j < columns; j++ ) {
	 *     for( int i = 0; i < rows; i++ ) {
	 *         // ...
	 *     }
	 * }
	 * </code></pre>
	 * etwas schneller sein als
	 * <pre><code>
	 * for( int i = 0; i < rows; i++ ) {
	 *     for( int j = 0; j < columns; j++ ) {
	 *         // ...
	 *     }
	 * }
	 * </code></pre>
	 */
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

	/**
	 * Initialisiert diese Matrix als Kopie der angegebenen Matrix.
	 *
	 * @param other Die zu kopierende Matrix.
	 */
	public DoubleMatrix( DoubleMatrix other ) {
		this.rows = other.rows();
		this.columns = other.columns();
		this.coefficients = new double[rows * columns];
		System.arraycopy(
			other.coefficients, 0,
			this.coefficients, 0,
			rows * columns);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int columns() {
		return columns;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int rows() {
		return rows;
	}

	/**
	 * {@inheritDoc}
	 */
	int idx( int r, int c ) {
		return c * rows + r;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double get( int row, int col ) {
		try {
			return coefficients[idx(row, col)];
		} catch( ArrayIndexOutOfBoundsException ex ) {
			throw new IllegalArgumentException("No element at row=" + row + ", column=" + col, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MLMatrix set( int row, int col, double value ) {
		try {
			coefficients[idx(row, col)] = value;
		} catch( ArrayIndexOutOfBoundsException ex ) {
			throw new IllegalArgumentException("No element at row=" + row + ", column=" + col, ex);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MLMatrix initializeRandom() {
		return initializeRandom(-1.0, 1.0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MLMatrix initializeRandom( double lower, double upper ) {
		applyInPlace(( d ) -> ((upper - lower) * Constants.random()) + lower);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MLMatrix initializeOne() {
		applyInPlace(( d ) -> 1.0);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MLMatrix initializeZero() {
		applyInPlace(( d ) -> 0.0);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MLMatrix duplicate() {
		return new DoubleMatrix(this);
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MLMatrix addInPlace( MLMatrix B ) {
		for( int j = 0; j < columns; j++ ) {
			for( int i = 0; i < rows; i++ ) {
				coefficients[idx(i, j)] += B.get(i, j);
			}
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MLMatrix scaleInPlace( final double scalar ) {
		for( int i = 0; i < coefficients.length; i++ ) {
			coefficients[i] *= scalar;
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MLMatrix scaleInPlace( final MLMatrix S ) {
		for( int j = 0; j < columns; j++ ) {
			for( int i = 0; i < rows; i++ ) {
				coefficients[idx(i, j)] *= S.get(i, j);
			}
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MLMatrix apply( DoubleUnaryOperator op ) {
		DoubleMatrix result = new DoubleMatrix(rows, columns);
		for( int i = 0; i < coefficients.length; i++ ) {
			result.coefficients[i] = op.applyAsDouble(coefficients[i]);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
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
