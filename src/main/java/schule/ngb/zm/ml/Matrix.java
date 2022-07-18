package schule.ngb.zm.ml;

import java.util.function.DoubleUnaryOperator;

public interface Matrix {

	int columns();

	int rows();

	double[][] getCoefficients();

	double get( int row, int col );

	Matrix set( int row, int col, double value );

	Matrix initializeRandom();

	Matrix initializeRandom( double lower, double upper );

	Matrix initializeOne();

	Matrix initializeZero();

	Matrix transpose();

	Matrix multiply( Matrix B );

	Matrix multiplyAddBias( Matrix B, Matrix C );

	Matrix multiplyLeft( Matrix B );

	Matrix add( Matrix B );

	Matrix sub( Matrix B );


	Matrix scale( double scalar );

	Matrix scale( Matrix S );

	Matrix apply( DoubleUnaryOperator op );

	Matrix duplicate();

	String toString();

}
