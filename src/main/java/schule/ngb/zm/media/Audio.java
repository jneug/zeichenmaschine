package schule.ngb.zm.media;

/**
 * Schnittstelle für Audio-Medien.
 *
 * <h2>MP3-Dateien verwenden</h2>
 * Java kann nativ nur Waveform ({@code .wav}) Dateien wiedergeben. Um auch
 * MP3-Dateien zu nutzen, müssen die Bibliotheken <a href="#">jlayer</a>, <a
 * href="#">tritonus-share</a> und <a href="#">mp3spi</a> eingebunden werden.
 * Details zur Verwendung können in der <a
 * href="https://zeichenmaschine.xyz/installation/#unterstutzung-fur-mp3">Dokumentation
 * der Zeichenmaschine</a> nachgelesen werden.
 */
public interface Audio {

	/**
	 * @return Die Quelle, aus der das Medium geladen wurde.
	 */
	String getSource();

	/**
	 * Prüft, ob das Medium gerade abgespielt wird.
	 *
	 * @return {@code true}, wenn das Medium abgespielt wird, {@code false}
	 * 	sonst.
	 */
	boolean isPlaying();

	/**
	 * Prüft, ob das Medium gerade in einer Schleife abgespielt wird. Wenn
	 * {@code isLooping() == true} gilt, dann muss auch immer
	 * {@code isPlaying() == true} gelten.
	 *
	 * @return {@code true}, wenn das Medium in einer Schleife abgespielt wird,
	 *    {@code false} sonst.
	 */
	boolean isLooping();

	/**
	 * Legt die Lautstärke des Mediums beim Abspielen fest.
	 * <p>
	 * Die Lautstärke wird auf einer linearen Skale festgelegt, wobei 0 kein Ton
	 * und 1 volle Lautstärke bedeutet. Werte über 1 verstärken den Ton des
	 * Mediums. Negative Werte setzen die Lautstärke aud 0.
	 *
	 * @param volume Die neue Lautstärke zwischen 0 und 1.
	 * @see <a
	 * 	href="https://stackoverflow.com/a/40698149">https://stackoverflow.com/a/40698149</a>
	 */
	void setVolume( double volume );

	/**
	 * Liefert die aktuelle Lautstärke dieses Mediums.
	 * <p>
	 * Die Lautstärke wird auf einer linearen Skale angegeben, wobei 0 kein Ton
	 * und 1 volle Lautstärke bedeutet. Werte über 1 verstärken den Ton des
	 * Mediums.
	 *
	 * @return Die Lautstärke als linear skalierter Wert.
	 */
	double getVolume();

	/**
	 * Startet die Wiedergabe des Mediums. Das Audio-Medium wird einmal
	 * abgespielt und stoppt dann.
	 * <p>
	 * Die Methode beendet sofort und die Wiedergabe erfolgt im Hintergrund.
	 * Soll die Programmausführung erst nach Wiedergabe des Mediums fortgesetzt
	 * werden, sollte {@link #playAndWait()} verwendet werden.
	 * <p>
	 * Soll die Wiedergabe im Hintergrund ablaufen, aber dennoch auf das Ende
	 * reagiert werden, kann ein
	 * {@link #addAudioListener(AudioListener) AudioListener} verwendet werden.
	 */
	void play();

	/**
	 * Startet die Wiedergabe des Mediums und blockiert das Programm, bis die
	 * Wiedergabe beendet ist.
	 */
	void playAndWait();

	/**
	 * Spielt das Medium in einer kontinuierlichen Schleife ab. Die Methode
	 * startet die Wiedergabe im Hintergrund und beendet dann sofort. Um die
	 * Wiedergabe zu stoppen, muss {@link #stop()} aufgerufen werden.
	 */
	void loop();

	/**
	 * Stoppt die Wiedergabe. Wird das Medium gerade nicht abgespielt
	 * ({@code isPlaying() == false}), dann passiert nichts.
	 */
	void stop();

	/**
	 * Stoppt die Wiedergabe und gibt alle Ressourcen, die für das Medium
	 * verwendet werden, frei.
	 */
	void dispose();

	/**
	 * Fügt dem Medium das angegebene Objekt als {@code AudioListener} hinzu,
	 * der bei Start und Stopp der Wiedergabe informiert wird.
	 *
	 * @param listener Das Listener-Objekt.
	 */
	void addAudioListener( AudioListener listener );

	/**
	 * Entfernt den angegebenen {@code AudioListener} vom Medium.
	 *
	 * @param listener Das Listener-Objekt.
	 */
	void removeAudioListener( AudioListener listener );

}
