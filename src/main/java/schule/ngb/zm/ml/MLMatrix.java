package schule.ngb.zm.ml;

import java.util.function.DoubleUnaryOperator;

/**
 * Interface für Matrizen, die in {@link NeuralNetwork} Klassen verwendet
 * werden.
 * <p>
 * Eine implementierende Klasse muss generell zwei Konstruktoren bereitstellen:
 * <ol>
 * <li> {@code MLMatrix(int rows, int columns)} erstellt eine Matrix mit den
 * 		angegebenen Dimensionen und setzt alle Koeffizienten auf 0.
 * <li> {@code MLMatrix(double[][] coefficients} erstellt eine Matrix mit der
 * 		durch das Array gegebenen Dimensionen und setzt die Werte auf die
 * 		jeweiligen Werte des Arrays.
 * </ol>
 * <p>
 * Das Interface ist nicht dazu gedacht eine allgemeine Umsetzung für
 * Matrizen-Algebra abzubilden, sondern soll gezielt die im Neuralen Netzwerk
 * verwendeten Algorithmen umsetzen. Einerseits würde eine ganz allgemeine
 * Matrizen-Klasse nicht im Rahmen der Zeichenmaschine liegen und auf der
 * anderen Seite bietet eine Konzentration auf die verwendeten Algorithmen mehr
 * Spielraum zur Optimierung.
 * <p>
 * Intern wird das Interface von {@link DoubleMatrix} implementiert. Die Klasse
 * ist eine weitestgehend naive Implementierung der Algorithmen mit kleineren
 * Optimierungen. Die Verwendung eines generalisierten Interfaces erlaubt aber
 * zukünftig die optionale Integration spezialisierterer Algebra-Bibliotheken
 * wie
 * <a href="https://dst.lbl.gov/ACSSoftware/colt/">Colt</a>, um auch große
 * Netze effizient berechnen zu können.
 */
public interface MLMatrix {

	/**
	 * Die Anzahl der Spalten der Matrix.
	 *
	 * @return Spaltenzahl.
	 */
	int columns();

	/**
	 * Die Anzahl der Zeilen der Matrix.
	 *
	 * @return Zeilenzahl.
	 */
	int rows();

	/**
	 * Gibt den Wert an der angegebenen Stelle der Matrix zurück.
	 *
	 * @param row Die Spaltennummer zwischen 0 und {@code rows()-1}.
	 * @param col Die Zeilennummer zwischen 0 und {@code columns()-1}
	 * @return Den Koeffizienten in der Zeile {@code row} und der Spalte
	 *    {@code col}.
	 * @throws IllegalArgumentException Falls {@code row >= rows()} oder
	 *                                  {@code col >= columns()}.
	 */
	double get( int row, int col ) throws IllegalArgumentException;

	/**
	 * Setzt den Wert an der angegebenen Stelle der Matrix.
	 *
	 * @param row Die Spaltennummer zwischen 0 und {@code rows()-1}.
	 * @param col Die Zeilennummer zwischen 0 und {@code columns()-1}
	 * @param value Der neue Wert.
	 * @return Diese Matrix selbst (method chaining).
	 * @throws IllegalArgumentException Falls {@code row >= rows()} oder
	 *                                  {@code col >= columns()}.
	 */
	MLMatrix set( int row, int col, double value ) throws IllegalArgumentException;

	/**
	 * Setzt jeden Wert in der Matrix auf eine Zufallszahl zwischen -1 und 1.
	 * <p>
	 * Nach Möglichkeit sollte der
	 * {@link schule.ngb.zm.Constants#random(int, int) Zufallsgenerator der
	 * Zeichenmaschine} verwendet werden.
	 *
	 * @return Diese Matrix selbst (method chaining).
	 */
	MLMatrix initializeRandom();

	/**
	 * Setzt jeden Wert in der Matrix auf eine Zufallszahl innerhalb der
	 * angegebenen Grenzen.
	 * <p>
	 * Nach Möglichkeit sollte der
	 * {@link schule.ngb.zm.Constants#random(int, int) Zufallsgenerator der
	 * Zeichenmaschine} verwendet werden.
	 *
	 * @param lower Untere Grenze der Zufallszahlen.
	 * @param upper Obere Grenze der Zufallszahlen.
	 * @return Diese Matrix selbst (method chaining).
	 */
	MLMatrix initializeRandom( double lower, double upper );

	/**
	 * Setzt alle Werte der Matrix auf 1.
	 *
	 * @return Diese Matrix selbst (method chaining).
	 */
	MLMatrix initializeOne();

	/**
	 * Setzt alle Werte der Matrix auf 0.
	 *
	 * @return Diese Matrix selbst (method chaining).
	 */
	MLMatrix initializeZero();

	/**
	 * Erzeugt eine neue Matrix {@code C} mit dem Ergebnis der Matrixoperation
	 * <pre>
	 * C = this . B + V'
	 * </pre>
	 * wobei {@code this} dieses Matrixobjekt ist und {@code .} für die
	 * Matrixmultiplikation steht. {@vode V'} ist die Matrix {@code V}
	 * {@code rows()}-mal untereinander wiederholt.
	 * <p>
	 * Wenn diese Matrix die Dimension r x c hat, dann muss die Matrix {@code B}
	 * die Dimension c x m haben und {@code V} eine 1 x m Matrix sein. Die
	 * Matrix {@code V'} hat also die Dimension r x m, ebenso wie das Ergebnis
	 * der Operation.
	 *
	 * @param B Eine {@code columns()} x m Matrix mit der Multipliziert wird.
	 * @param V Eine 1 x {@code B.columns()} Matrix mit den Bias-Werten.
	 * @return Eine {@code rows()} x m Matrix.
	 * @throws IllegalArgumentException Falls die Dimensionen der Matrizen nicht
	 *                                  zur Operation passen. Also
	 *                                  {@code this.columns() != B.rows()} oder
	 *                                  {@code B.columns() != V.columns()} oder
	 *                                  {@code V.rows() != 1}.
	 */
	MLMatrix multiplyAddBias( MLMatrix B, MLMatrix V ) throws IllegalArgumentException;

	/**
	 * Erzeugt eine neue Matrix {@code C} mit dem Ergebnis der Matrixoperation
	 * <pre>
	 * C = this . t(B)
	 * </pre>
	 * wobei {@code this} dieses Matrixobjekt ist, {@code t(B)} die
	 * Transposition der Matrix {@code B} ist und {@code .} für die
	 * Matrixmultiplikation steht.
	 * <p>
	 * Wenn diese Matrix die Dimension r x c hat, dann muss die Matrix {@code B}
	 * die Dimension m x c haben und das Ergebnis ist eine r x m Matrix.
	 *
	 * @param B Eine m x {@code columns()} Matrix.
	 * @return Eine {@code rows()} x m Matrix.
	 * @throws IllegalArgumentException Falls die Dimensionen der Matrizen nicht
	 *                                  zur Operation passen. Also
	 *                                  {@code this.columns() != B.columns()}.
	 */
	MLMatrix multiplyTransposed( MLMatrix B ) throws IllegalArgumentException;

	/**
	 * Erzeugt eine neue Matrix {@code C} mit dem Ergebnis der Matrixoperation
	 * <pre>
	 * C = t(this) . B * scalar
	 * </pre>
	 * wobei {@code this} dieses Matrixobjekt ist, {@code t(this)} die
	 * Transposition dieser Matrix ist und {@code .} für die
	 * Matrixmultiplikation steht. {@code *} bezeichnet die
	 * Skalarmultiplikation, bei der jeder Wert der Matrix mit {@code scalar}
	 * multipliziert wird.
	 * <p>
	 * Wenn diese Matrix die Dimension r x c hat, dann muss die Matrix {@code B}
	 * die Dimension r x m haben und das Ergebnis ist eine c x m Matrix.
	 *
	 * @param B Eine m x {@code columns()} Matrix.
	 * @return Eine {@code rows()} x m Matrix.
	 * @throws IllegalArgumentException Falls die Dimensionen der Matrizen nicht
	 *                                  zur Operation passen. Also
	 *                                  {@code this.rows() != B.rows()}.
	 */
	MLMatrix transposedMultiplyAndScale( MLMatrix B, double scalar ) throws IllegalArgumentException;

	/**
	 * Erzeugt eine neue Matrix {@code C} mit dem Ergebnis der komponentenweisen
	 * Matrix-Addition
	 * <pre>
	 * C = this + B
	 * </pre>
	 * wobei {@code this} dieses Matrixobjekt ist. Für ein Element {@code C_ij}
	 * in {@code C} gilt
	 * <pre>
	 * C_ij = A_ij + B_ij
	 * </pre>
	 * <p>
	 * Die Matrix {@code B} muss dieselbe Dimension wie diese Matrix haben.
	 *
	 * @param B Eine {@code rows()} x {@code columns()} Matrix.
	 * @return Eine {@code rows()} x {@code columns()} Matrix.
	 * @throws IllegalArgumentException Falls die Dimensionen der Matrizen nicht
	 *                                  zur Operation passen. Also
	 *                                  {@code this.rows() != B.rows()} oder
	 *                                  {@code this.columns() != B.columns()}.
	 */
	MLMatrix add( MLMatrix B ) throws IllegalArgumentException;

	/**
	 * Setzt diese Matrix auf das Ergebnis der komponentenweisen
	 * Matrix-Addition
	 * <pre>
	 * A' = A + B
	 * </pre>
	 * wobei {@code A} dieses Matrixobjekt ist und {@code A'} diese Matrix nach
	 * der Operation. Für ein Element {@code A'_ij} in {@code A'} gilt
	 * <pre>
	 * A'_ij = A_ij + B_ij
	 * </pre>
	 * <p>
	 * Die Matrix {@code B} muss dieselbe Dimension wie diese Matrix haben.
	 *
	 * @param B Eine {@code rows()} x {@code columns()} Matrix.
	 * @return Eine {@code rows()} x {@code columns()} Matrix.
	 * @throws IllegalArgumentException Falls die Dimensionen der Matrizen nicht
	 *                                  zur Operation passen. Also
	 *                                  {@code this.rows() != B.rows()} oder
	 *                                  {@code this.columns() != B.columns()}.
	 */
	MLMatrix addInPlace( MLMatrix B ) throws IllegalArgumentException;

	/**
	 * Erzeugt eine neue Matrix {@code C} mit dem Ergebnis der komponentenweisen
	 * Matrix-Subtraktion
	 * <pre>
	 * C = A - B
	 * </pre>
	 * wobei {@code A} dieses Matrixobjekt ist. Für ein Element {@code C_ij} in
	 * {@code C} gilt
	 * <pre>
	 * C_ij = A_ij - B_ij
	 * </pre>
	 * <p>
	 * Die Matrix {@code B} muss dieselbe Dimension wie diese Matrix haben.
	 *
	 * @param B Eine {@code rows()} x {@code columns()} Matrix.
	 * @return Eine {@code rows()} x {@code columns()} Matrix.
	 * @throws IllegalArgumentException Falls die Dimensionen der Matrizen nicht
	 *                                  zur Operation passen. Also
	 *                                  {@code this.rows() != B.rows()} oder
	 *                                  {@code this.columns() != B.columns()}.
	 */
	MLMatrix sub( MLMatrix B ) throws IllegalArgumentException;

	/**
	 * Multipliziert jeden Wert dieser Matrix mit dem angegebenen Skalar.
	 * <p>
	 * Ist {@code A} dieses Matrixobjekt und {@code A'} diese Matrix nach der
	 * Operation, dann gilt für ein Element {@code A'_ij} in {@code A'}
	 * <pre>
	 * A'_ij = A_ij * scalar
	 * </pre>
	 *
	 * @param scalar Ein Skalar.
	 * @return Diese Matrix selbst (method chaining)
	 */
	MLMatrix scaleInPlace( double scalar );

	/**
	 * Multipliziert jeden Wert dieser Matrix mit dem entsprechenden Wert in der
	 * Matrix {@code S}.
	 * <p>
	 * Ist {@code A} dieses Matrixobjekt und {@code A'} diese Matrix nach der
	 * Operation, dann gilt für ein Element {@code A'_ij} in {@code A'}
	 * <pre>
	 * A'_ij = A_ij * S_ij
	 * </pre>
	 *
	 * @param S Eine {@code rows()} x {@code columns()} Matrix.
	 * @return Diese Matrix selbst (method chaining)
	 * @throws IllegalArgumentException Falls die Dimensionen der Matrizen nicht
	 *                                  zur Operation passen. Also
	 *                                  {@code this.rows() != B.rows()} oder
	 *                                  {@code this.columns() != B.columns()}.
	 */
	MLMatrix scaleInPlace( MLMatrix S ) throws IllegalArgumentException;

	/**
	 * Berechnet eine neue Matrix mit nur einer Zeile, die die Spaltensummen
	 * dieser Matrix enthalten.
	 *
	 * @return Eine 1 x {@code columns()} Matrix.
	 */
	MLMatrix colSums();

	/**
	 * Erzeugt eine neue Matrix, deren Werte gleich den Werten dieser Matrix
	 * nach der Anwendung der angegebenen Funktion sind.
	 *
	 * @param op Eine Operation {@code (double) -> double}.
	 * @return Eine {@code rows()} x {@code columns()} Matrix.
	 */
	MLMatrix apply( DoubleUnaryOperator op );

	/**
	 * Endet die gegebene Funktion auf jeden Wert der Matrix an.
	 *
	 * @param op Eine Operation {@code (double) -> double}.
	 * @return Diese Matrix selbst (method chaining)
	 */
	MLMatrix applyInPlace( DoubleUnaryOperator op );

	/**
	 * Erzeugt eine neue Matrix mit denselben Dimensionen und Koeffizienten wie
	 * diese Matrix.
	 *
	 * @return Eine Kopie dieser Matrix.
	 */
	MLMatrix duplicate();

	String toString();

}
