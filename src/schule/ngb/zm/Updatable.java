package schule.ngb.zm;

/**
 * Aktualisierbare Objekte können in regelmäßigen Intervallen (meist einmal
 * pro Frame) ihren Zustand update. Diese Änderung kann abhängig vom
 * Zeitintervall (in Sekunden) zum letzten Aufruf passieren.
 */
public interface Updatable {

	/**
	 * Gibt an, ob das Objekt gerade auf Aktualisierungen reagiert.
	 * @return <code>true</code>, wenn das Objekt aktiv ist.
	 */
	public boolean isActive();

	/**
	 * Änderung des Zustandes des Objekts abhängig vom Zeitintervall
	 * <var>delta</var> in Sekunden.
	 * @param delta Zeitintervall seit dem letzten Aufruf (in Sekunden).
	 */
	public void update( double delta );

}
