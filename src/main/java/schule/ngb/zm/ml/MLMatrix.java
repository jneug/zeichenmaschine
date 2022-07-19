package schule.ngb.zm.ml;

import java.util.function.DoubleUnaryOperator;

public interface MLMatrix {

	int columns();

	int rows();

	double[][] coefficients();

	double get( int row, int col );

	MLMatrix set( int row, int col, double value );

	MLMatrix initializeRandom();

	MLMatrix initializeRandom( double lower, double upper );

	MLMatrix initializeOne();

	MLMatrix initializeZero();

	/**
	 * Erzeugt die transponierte Matrix zu dieser.
	 * @return
	 */
	MLMatrix transpose();

	MLMatrix multiply( MLMatrix B );

	MLMatrix multiplyAddBias( MLMatrix B, MLMatrix C );

	MLMatrix multiplyLeft( MLMatrix B );

	MLMatrix add( MLMatrix B );

	MLMatrix sub( MLMatrix B );


	MLMatrix scale( double scalar );

	MLMatrix scale( MLMatrix S );

	MLMatrix apply( DoubleUnaryOperator op );

	MLMatrix duplicate();

	String toString();

}
