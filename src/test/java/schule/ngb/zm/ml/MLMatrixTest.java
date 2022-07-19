package schule.ngb.zm.ml;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class MLMatrixTest {

	private TestInfo info;

	@BeforeEach
	void saveTestInfo( TestInfo info ) {
		this.info = info;
	}

	@ParameterizedTest
	@ValueSource( classes = {DoubleMatrix.class, MatrixFactory.ColtMatrix.class} )
	void get( Class<? extends MLMatrix> mType ) {
		MatrixFactory.matrixType = mType;

		MLMatrix M = MatrixFactory.create(new double[][]{
			{1, 2, 3},
			{4, 5, 6}
		});

		assertEquals(mType, M.getClass());

		assertEquals(1.0, M.get(0,0));
		assertEquals(4.0, M.get(1,0));
		assertEquals(6.0, M.get(1,2));
	}

	@ParameterizedTest
	@ValueSource( classes = {DoubleMatrix.class, MatrixFactory.ColtMatrix.class} )
	void initializeOne( Class<? extends MLMatrix> mType ) {
		MatrixFactory.matrixType = mType;

		MLMatrix m = MatrixFactory.create(4, 4);
		m.initializeOne();

		assertEquals(mType, m.getClass());

		for( int i = 0; i < m.rows(); i++ ) {
			for( int j = 0; j < m.columns(); j++ ) {
				assertEquals(1.0, m.get(i, j));
			}
		}
	}

	@ParameterizedTest
	@ValueSource( classes = {DoubleMatrix.class, MatrixFactory.ColtMatrix.class} )
	void initializeZero( Class<? extends MLMatrix> mType ) {
		MatrixFactory.matrixType = mType;

		MLMatrix m = MatrixFactory.create(4, 4);
		m.initializeZero();

		assertEquals(mType, m.getClass());

		for( int i = 0; i < m.rows(); i++ ) {
			for( int j = 0; j < m.columns(); j++ ) {
				assertEquals(0.0, m.get(i, j));
			}
		}
	}

	@ParameterizedTest
	@ValueSource( classes = {DoubleMatrix.class, MatrixFactory.ColtMatrix.class} )
	void initializeRandom( Class<? extends MLMatrix> mType ) {
		MatrixFactory.matrixType = mType;

		MLMatrix m = MatrixFactory.create(4, 4);
		m.initializeRandom();

		assertEquals(mType, m.getClass());

		for( int i = 0; i < m.rows(); i++ ) {
			for( int j = 0; j < m.columns(); j++ ) {
				double d = m.get(i, j);
				assertTrue(-1.0 <= d && d < 1.0);
			}
		}
	}

	@ParameterizedTest
	@ValueSource( classes = {DoubleMatrix.class, MatrixFactory.ColtMatrix.class} )
	void multiplyTransposed( Class<? extends MLMatrix> mType ) {
		MatrixFactory.matrixType = mType;

		MLMatrix A = MatrixFactory.create(new double[][]{
			{1.0, 2.0, 3.0, 4.0},
			{1.0, 2.0, 3.0, 4.0},
			{1.0, 2.0, 3.0, 4.0}
		});
		MLMatrix B = MatrixFactory.create(new double[][]{
			{1, 3, 5, 7},
			{2, 4, 6, 8}
		});

		MLMatrix C = A.multiplyTransposed(B);

		assertEquals(mType, A.getClass());
		assertEquals(mType, B.getClass());
		assertEquals(mType, C.getClass());

		assertEquals(3, C.rows());
		assertEquals(2, C.columns());
		for( int i = 0; i < C.rows(); i++ ) {
			assertEquals(50.0, C.get(i, 0));
			assertEquals(60.0, C.get(i, 1));
		}
	}

	@ParameterizedTest
	@ValueSource( classes = {DoubleMatrix.class, MatrixFactory.ColtMatrix.class} )
	void multiplyAddBias( Class<? extends MLMatrix> mType ) {
		MatrixFactory.matrixType = mType;

		MLMatrix A = MatrixFactory.create(new double[][]{
			{1.0, 2.0, 3.0, 4.0},
			{1.0, 2.0, 3.0, 4.0},
			{1.0, 2.0, 3.0, 4.0}
		});
		MLMatrix B = MatrixFactory.create(new double[][]{
			{1.0, 2.0},
			{3.0, 4.0},
			{5.0, 6.0},
			{7.0, 8.0}
		});
		MLMatrix V = MatrixFactory.create(new double[][]{
			{1000.0, 2000.0}
		});

		MLMatrix C = A.multiplyAddBias(B, V);

		assertEquals(mType, A.getClass());
		assertEquals(mType, B.getClass());
		assertEquals(mType, C.getClass());
		assertEquals(mType, V.getClass());

		assertEquals(3, C.rows());
		assertEquals(2, C.columns());
		for( int i = 0; i < C.rows(); i++ ) {
			assertEquals(1050.0, C.get(i, 0));
			assertEquals(2060.0, C.get(i, 1));
		}
	}

	@ParameterizedTest
	@ValueSource( classes = {DoubleMatrix.class, MatrixFactory.ColtMatrix.class} )
	void transposedMultiplyAndScale( Class<? extends MLMatrix> mType ) {
		MatrixFactory.matrixType = mType;

		MLMatrix A = MatrixFactory.create(new double[][]{
			{1, 1, 1},
			{2, 2, 2},
			{3, 3, 3},
			{4, 4, 4}
		});
		MLMatrix B = MatrixFactory.create(new double[][]{
			{1.0, 2.0},
			{3.0, 4.0},
			{5.0, 6.0},
			{7.0, 8.0}
		});

		MLMatrix C = A.transposedMultiplyAndScale(B, 2.0);

		assertEquals(mType, A.getClass());
		assertEquals(mType, B.getClass());
		assertEquals(mType, C.getClass());

		assertEquals(3, C.rows());
		assertEquals(2, C.columns());
		for( int i = 0; i < C.rows(); i++ ) {
			assertEquals(100.0, C.get(i, 0));
			assertEquals(120.0, C.get(i, 1));
		}
	}

	@ParameterizedTest
	@ValueSource( classes = {DoubleMatrix.class, MatrixFactory.ColtMatrix.class} )
	void apply( Class<? extends MLMatrix> mType ) {
		MatrixFactory.matrixType = mType;

		MLMatrix M = MatrixFactory.create(new double[][]{
			{1, 1, 1},
			{2, 2, 2},
			{3, 3, 3},
			{4, 4, 4}
		});

		MLMatrix R = M.apply(( d ) -> d * d);

		assertEquals(mType, M.getClass());
		assertEquals(mType, R.getClass());
		assertNotSame(M, R);

		for( int i = 0; i < M.rows(); i++ ) {
			for( int j = 0; j < M.columns(); j++ ) {
				assertEquals(
					(i + 1) * (i + 1), R.get(i, j),
					msg("(%d,%d)", "apply", i, j)
				);
			}
		}

		MLMatrix M2 = M.applyInPlace(( d ) -> d * d * d);
		assertSame(M, M2);
		for( int i = 0; i < M.rows(); i++ ) {
			for( int j = 0; j < M.columns(); j++ ) {
				assertEquals(
					(i + 1) * (i + 1) * (i + 1), M.get(i, j),
					msg("(%d,%d)", "applyInPlace", i, j)
				);
			}
		}
	}

	@ParameterizedTest
	@ValueSource( classes = {DoubleMatrix.class, MatrixFactory.ColtMatrix.class} )
	void add( Class<? extends MLMatrix> mType ) {
		MatrixFactory.matrixType = mType;

		MLMatrix M = MatrixFactory.create(new double[][]{
			{1, 1, 1},
			{2, 2, 2},
			{3, 3, 3},
			{4, 4, 4}
		});

		MLMatrix R = M.add(M);

		assertEquals(mType, M.getClass());
		assertEquals(mType, R.getClass());
		assertNotSame(M, R);

		for( int i = 0; i < M.rows(); i++ ) {
			for( int j = 0; j < M.columns(); j++ ) {
				assertEquals(
					(i + 1) + (i + 1), R.get(i, j),
					msg("(%d,%d)", "add", i, j)
				);
			}
		}

		MLMatrix M2 = M.addInPlace(R);
		assertSame(M, M2);
		for( int i = 0; i < M.rows(); i++ ) {
			for( int j = 0; j < M.columns(); j++ ) {
				assertEquals(
					(i + 1) + (i + 1) + (i + 1), M.get(i, j),
					msg("(%d,%d)", "addInPlace", i, j)
				);
			}
		}
	}

	@ParameterizedTest
	@ValueSource( classes = {DoubleMatrix.class, MatrixFactory.ColtMatrix.class} )
	void sub( Class<? extends MLMatrix> mType ) {
		MatrixFactory.matrixType = mType;

		MLMatrix M = MatrixFactory.create(new double[][]{
			{1, 1, 1},
			{2, 2, 2},
			{3, 3, 3},
			{4, 4, 4}
		});

		MLMatrix R = M.sub(M);

		assertEquals(mType, M.getClass());
		assertEquals(mType, R.getClass());
		assertNotSame(M, R);

		for( int i = 0; i < M.rows(); i++ ) {
			for( int j = 0; j < M.columns(); j++ ) {
				assertEquals(
					0.0, R.get(i, j),
					msg("(%d,%d)", "sub", i, j)
				);
			}
		}
	}

	@ParameterizedTest
	@ValueSource( classes = {DoubleMatrix.class, MatrixFactory.ColtMatrix.class} )
	void colSums( Class<? extends MLMatrix> mType ) {
		MatrixFactory.matrixType = mType;

		MLMatrix M = MatrixFactory.create(new double[][]{
			{1, 2, 3},
			{1, 2, 3},
			{1, 2, 3},
			{1, 2, 3}
		});

		MLMatrix R = M.colSums();

		assertEquals(mType, M.getClass());
		assertEquals(mType, R.getClass());
		assertNotSame(M, R);

		assertEquals(1, R.rows());
		assertEquals(3, R.columns());
		for( int j = 0; j < M.columns(); j++ ) {
			assertEquals(
				(j+1)*4, R.get(0, j),
				msg("(%d,%d)", "colSums", 0, j)
			);
		}
	}


	@ParameterizedTest
	@ValueSource( classes = {DoubleMatrix.class, MatrixFactory.ColtMatrix.class} )
	void duplicate( Class<? extends MLMatrix> mType ) {
		MatrixFactory.matrixType = mType;

		MLMatrix M = MatrixFactory.create(new double[][]{
			{1, 2, 3},
			{1, 2, 3},
			{1, 2, 3},
			{1, 2, 3}
		});

		MLMatrix R = M.duplicate();

		assertEquals(mType, M.getClass());
		assertEquals(mType, R.getClass());
		assertNotSame(M, R);

		for( int i = 0; i < M.rows(); i++ ) {
			for( int j = 0; j < M.columns(); j++ ) {
				assertEquals(
					M.get(i, j), R.get(i, j),
					msg("(%d,%d)", "duplicate", i, j)
				);
			}
		}
	}

	@ParameterizedTest
	@ValueSource( classes = {DoubleMatrix.class, MatrixFactory.ColtMatrix.class} )
	void scale( Class<? extends MLMatrix> mType ) {
		MatrixFactory.matrixType = mType;

		MLMatrix M = MatrixFactory.create(new double[][]{
			{1, 1, 1},
			{2, 2, 2},
			{3, 3, 3},
			{4, 4, 4}
		});

		MLMatrix M2 = M.scaleInPlace(2.0);

		assertEquals(mType, M.getClass());
		assertEquals(mType, M2.getClass());
		assertSame(M, M2);

		for( int i = 0; i < M.rows(); i++ ) {
			for( int j = 0; j < M.columns(); j++ ) {
				assertEquals(
					(i+1)*2.0, M2.get(i, j),
					msg("(%d,%d)", "scaleInPlace", i, j)
				);
			}
		}

		MLMatrix M3 = M.scaleInPlace(M);
		assertSame(M, M3);
		for( int i = 0; i < M.rows(); i++ ) {
			for( int j = 0; j < M.columns(); j++ ) {
				assertEquals(
					((i+1)*2.0)*((i+1)*2.0), M.get(i, j),
					msg("(%d,%d)", "addInPlace", i, j)
				);
			}
		}
	}

	private String msg( String msg, String methodName, Object... args ) {
		String testName = this.info.getTestMethod().get().getName();
		String className = MatrixFactory.matrixType.getSimpleName();
		return String.format("[" + testName + "(" + className + ") " + methodName + "()] " + msg, args);
	}

}
