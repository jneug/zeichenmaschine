package schule.ngb.zm;

import java.awt.*;

/**
 * {@code Drawable} Objekte können auf eine Zeichenfläche gezeichnet werden. In
 * der Regel werden sie einmal pro Frame gezeichnet.
 */
public interface Drawable {

	/**
	 * Gibt an, ob das Objekt derzeit sichtbar ist (also gezeichnet werden
	 * muss).
	 * <p>
	 * Wie mit dieser Information umgegangen wird, ist nicht weiter festgelegt.
	 * In der Regel sollte eine aufrufende Instanz zunächst prüfen, ob das
	 * Objekt aktiv ist, und nur dann{@link #draw(Graphics2D)} aufrufen. Für
	 * implementierende Klassen ist es aber gegebenenfalls auch sinnvoll, bei
	 * Inaktivität den Aufruf von {@code draw(Graphics2D)} schnell abzubrechen:
	 * <pre><code>
	 * void draw( Graphics2D graphics ) {
	 *     if( !isVisible() ) {
	 *         return;
	 *     }
	 *
	 *     // Objekt zeichnen..
	 * }
	 * </code></pre>
	 *
	 * @return {@code true}, wenn das Objekt sichtbar ist.
	 */
	boolean isVisible();

	/**
	 * Wird aufgerufen, um das Objekt auf die Zeichenfläche <var>graphics</var>
	 * zu zeichnen.
	 * <p>
	 * Das Objekt muss dafür Sorge tragen, dass der Zustand der Zeichenfläche
	 * (Transformationsmatrix, Farbe, ...) erhalten bleibt. Das Objekt sollte
	 * also etwaige Änderungen am Ende des Aufrufs wieder rückgängig machen.
	 *
	 * @param graphics Die Zeichenfläche.
	 */
	void draw( Graphics2D graphics );

}
