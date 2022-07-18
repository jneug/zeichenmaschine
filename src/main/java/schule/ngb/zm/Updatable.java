package schule.ngb.zm;

/**
 * {@code Updatable} Objekte können in regelmäßigen Intervallen (meist einmal
 * pro Frame) ihren Zustand aktualisieren. Diese Änderung kann abhängig vom
 * Zeitintervall (in Sekunden) zum letzten Aufruf passieren.
 */
public interface Updatable {

	/**
	 * Gibt an, ob das Objekt gerade auf Aktualisierungen reagiert.
	 * <p>
	 * Wie mit dieser Information umgegangen wird, ist nicht weiter festgelegt.
	 * In der Regel sollte eine aufrufende Instanz zunächst prüfen, ob das
	 * Objekt aktiv ist, und nur dann{@link #update(double)} aufrufen. Für
	 * implementierende Klassen ist es aber gegebenenfalls auch sinnvoll, bei
	 * Inaktivität den Aufruf von {@code update(double)} schnell abzubrechen:
	 * <pre><code>
	 * void update( double delta ) {
	 *     if( !isActive() ) {
	 *         return;
	 *     }
	 *
	 *     // Aktualisierung ausführen..
	 * }
	 * </code></pre>
	 *
	 * @return {@code true}, wenn das Objekt aktiv ist, {@code false}
	 * 	andernfalls.
	 */
	public boolean isActive();

	/**
	 * Änderung des Zustandes des Objekts abhängig vom Zeitintervall
	 * {@code delta} in Sekunden.
	 * <p>
	 * Die kann, muss aber nicht, die Rückgabe von {@link #isActive()}
	 * berücksichtigen.
	 *
	 * @param delta Zeitintervall seit dem letzten Aufruf (in Sekunden).
	 */
	public void update( double delta );

}
