package schule.ngb.zm.media;

import schule.ngb.zm.util.events.Listener;

/**
 * Interface für Klassen, die auf das starten und stoppen der Wiedergabe von
 * {@link Audio}-Objekten reagieren möchten.
 * <p>
 * Implementierende Klassen können sich bei einem Auido-Objekt mittels
 * {@link Audio#addAudioListener(AudioListener)} anmelden und werden über die
 * jeweilige Methode informiert, sobald die Wiedergabe gestartet oder gestoppt
 * wird.
 */
public interface AudioListener extends Listener<Audio> {

	/**
	 * Wird aufgerufen, sobald die Wiedergabe eines Audio-Objektes startet, dem
	 * dieses Objekt mittels {@link Audio#addAudioListener(AudioListener)}
	 * hinzugefügt wurde.
	 *
	 * @param source Das Audio-Objekt, dessen Wiedergabe gestartet wurde.
	 */
	void playbackStarted( Audio source );

	/**
	 * Wird aufgerufen, sobald die Wiedergabe eines Audio-Objektes stoppt, dem
	 * dieses Objekt mittels {@link Audio#addAudioListener(AudioListener)}
	 * hinzugefügt wurde.
	 *
	 * @param source Das Audio-Objekt, dessen Wiedergabe gestoppt wurde.
	 */
	void playbackStopped( Audio source );

}
