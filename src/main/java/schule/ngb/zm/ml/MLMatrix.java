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

	//MLMatrix transpose();

	//MLMatrix multiply( MLMatrix B );

	/**
	 * Erzeugt eine neue Matrix <em>C</em> mit dem Ergebnis der Matrixoperation
	 * <pre>
	 * C = A.B + V
	 * </pre>
	 * wobei <em>A</em> dieses Matrixobjekt ist und {@code .} für die
	 * Matrixmultiplikation steht.
	 *
	 * @param B
	 * @param V
	 * @return
	 */
	MLMatrix multiplyAddBias( MLMatrix B, MLMatrix V );

	/**
	 * Erzeugt eine neue Matrix <em>C</em> mit dem Ergebnis der Matrixoperation
	 * <pre>
	 * C = A.t(B)
	 * </pre>
	 * wobei <em>A</em> dieses Matrixobjekt ist und {@code t(B)} für die
	 * Transposition der Matrix <em>B</em>> steht.
	 *
	 * @param B
	 * @return
	 */
	MLMatrix multiplyTransposed( MLMatrix B );

	MLMatrix transposedMultiplyAndScale( MLMatrix B, double scalar );

	/**
	 * Erzeugt eine neue Matrix <em>C</em> mit dem Ergebnis der
	 * komponentenweisen Matrix-Addition
	 * <pre>
	 * C = A+B
	 * </pre>
	 * wobei <em>A</em> dieses Matrixobjekt ist. Für ein Element
	 * <em>C_ij</em> in <em>C</em> gilt
	 * <pre>
	 * C_ij = A_ij + B_ij
	 * </pre>
	 *
	 * @param B Die zweite Matrix.
	 * @return Ein neues Matrixobjekt mit dem Ergebnis.
	 */
	MLMatrix add( MLMatrix B );

	/**
	 * Setzt dies Matrix auf das Ergebnis der
	 * komponentenweisen Matrix-Addition
	 * <pre>
	 * A = A+B
	 * </pre>
	 * wobei <em>A</em> dieses Matrixobjekt ist. Für ein Element
	 * <em>A_ij</em> in <em>A</em> gilt
	 * <pre>
	 * A_ij = A_ij + B_ij
	 * </pre>
	 *
	 * @param B Die zweite Matrix.
	 * @return Diese Matrix selbst (method chaining).
	 */
	MLMatrix addInPlace( MLMatrix B );

	/**
	 * Erzeugt eine neue Matrix <em>C</em> mit dem Ergebnis der
	 * komponentenweisen Matrix-Subtraktion
	 * <pre>
	 * C = A-B
	 * </pre>
	 * wobei <em>A</em> dieses Matrixobjekt ist. Für ein Element
	 * <em>C_ij</em> in <em>C</em> gilt
	 * <pre>
	 * C_ij = A_ij - B_ij
	 * </pre>
	 *
	 * @param B
	 * @return
	 */
	MLMatrix sub( MLMatrix B );

	MLMatrix scaleInPlace( double scalar );

	MLMatrix scaleInPlace( MLMatrix S );

	/**
	 * Berechnet eine neue Matrix mit nur einer Zeile, die die Spaltensummen
	 * dieser Matrix enthalten.
	 * @return
	 */
	MLMatrix colSums();

	/**
	 * Endet die gegebene Funktion auf jeden Wert der Matrix an.
	 *
	 * @param op
	 * @return
	 */
	MLMatrix apply( DoubleUnaryOperator op );

	/**
	 * Endet die gegebene Funktion auf jeden Wert der Matrix an.
	 *
	 * @param op
	 * @return
	 */
	MLMatrix applyInPlace( DoubleUnaryOperator op );

	/**
	 * Erzeugt eine neue Matrix mit denselben Dimenstionen und Koeffizienten wie
	 * diese Matrix.
	 *
	 * @return
	 */
	MLMatrix duplicate();

	String toString();

}
