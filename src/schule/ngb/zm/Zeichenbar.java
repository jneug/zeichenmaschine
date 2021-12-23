package schule.ngb.zm;

import java.awt.*;

/**
 * Zeichenbare Objekte können auf eine Zeichenfläche gezeichnet werden.
 * In der Regel werden sie einmal pro Frame gezeichnet.
 */
public interface Zeichenbar {

	/**
	 * Gibt an, ob das Objekt derzeit sichtbar ist (also gezeichnet werden
	 * muss).
	 *
	 * @return <code>true</code>, wenn das Objekt sichtbar ist.
	 */
	public boolean istSichtbar();

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
	public void zeichnen( Graphics2D graphics );

}
