package schule.ngb.zm.media;

/**
 * Interface für Audio-Medien.
 */
public interface Audio {

	/**
	 * Prüft, ob das Medium gerade abgespielt wird.
	 *
	 * @return {@code true}, wenn das Medium abgespielt wird, {@code false}
	 * 	sonst.
	 */
	boolean isPlaying();

	/**
	 * Prüft, ob das Medium gerade in einer Schleife abgespielt wird. Wenn
	 * {@code isLooping() == true}, dann muss auch immer
	 * {@code isPlaying() == true} gelten.
	 *
	 * @return @return {@code true}, wenn das Medium in einer Schleife
	 * 	abgespielt wird, {@code false} sonst.
	 */
	boolean isLooping();

	/**
	 * Legt die Lautstärke des Mediums beim Abspielen fest.
	 * <p>
	 * Die Lautstärke wird auf einer linearen Skale festgelegt, wobei 0 kein Ton
	 * und 1 volle Lautstärke bedeutet. Werte über 1 verstärken den Ton des
	 * Mediums.
	 *
	 * @param volume Die neue Lautstärke zwischen 0 und 1.
	 * @see <a
	 * 	href="https://stackoverflow.com/a/40698149">https://stackoverflow.com/a/40698149</a>
	 */
	void setVolume( double volume );

	/**
	 * Startet die Wiedergabe des Mediums und beendet die Methode. Das
	 * Audio-Medium wird einmal abgespielt und stoppt dann.
	 */
	void playOnce();

	/**
	 * Startet die Wiedergabe des Mediums und blockiert das Programm, bis die
	 * Wiedergabe beendet ist.
	 */
	void playOnceAndWait();

	/**
	 * Spielt das Medium in einer kontinuierlichen Schleife ab. Die Methode
	 * startet die Wiedergabe und beendet dann direkt die Methode. Um die
	 * Wiedergabe zu stoppen muss {@link #stop()} aufgerufen werden.
	 */
	void loop();

	/**
	 * Stoppt die Wiedergabe. Wird das Medium gerade nicht abgespielt
	 * {@code isPlaying() == false}, dann passiert nichts.
	 */
	void stop();

	/**
	 * Stoppt die Wiedergabe und gibt alle Resourcen, die für das Medium
	 * verwendet werden, frei.
	 */
	void dispose();

}
