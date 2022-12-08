package schule.ngb.zm.media;

import schule.ngb.zm.Constants;
import schule.ngb.zm.util.events.EventDispatcher;
import schule.ngb.zm.util.tasks.TaskRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Ein Mixer ist eine Sammlung mehrerer {@link Audio Audio-Medien}, die
 * gemeinsam kontrolliert werden können.
 * <p>
 * Im einfachsten Fall kann die Audio-Gruppe gemeinsam gestartet und gestoppt
 * werden. Ein Mixer kann die Lautstärke der Medien in Relation zueinander
 * setzen. Dazu wird jedem Medium ein Faktor mitgegeben. Ein Medium mit dem
 * Faktor 0.5 ist dann halb so laut wie eines, mit dem Faktor 1.0.
 * <p>
 * Darüber hinaus kann ein Mixer Effekte wie einen
 * {@link #fade(double, int) fadeIn} auf die Medien anwenden.
 */
@SuppressWarnings( "unused" )
public class Mixer implements Audio, AudioListener {

	private List<AudioWrapper> audios;

	private float volume = 0.8f;

	EventDispatcher<Audio, AudioListener> eventDispatcher;

	public Mixer() {
		this.audios = new ArrayList<>(4);
	}

	public String getSource() {
		return "";
	}

	private AudioWrapper findWrapper( Audio pAudio ) {
		for( AudioWrapper aw: audios ) {
			if( aw.audio == pAudio ) {
				return aw;
			}
		}
		return null;
	}

	public boolean contains( Audio pAudio ) {
		return findWrapper(pAudio) != null;
	}

	public void add( Audio pAudio ) {
		add(pAudio, 1);
	}

	/**
	 * Fügt ein Audio-Objekt dem Mixer mit dem angegebenen Lautstärke-Faktor
	 * hinzu.
	 * <p>
	 * Der Lautstärke-Faktor setzt die Lautstärke des Audio-Objektes relativ zur
	 * Lautstärke des Mixers. Bei einem Faktor von 1.0 wird die Lautstärke des
	 * Mixers übernommen. Bei einem Wert von 0.5 wird das Objekt halb so laut
	 * abgespielt. Auf diese Weise lässt sich die Lautstärke aller Audio-Objekte
	 * des Mixers gleichzeitig anpassen, während ihre relative Lautstärke
	 * zueinander gleich bleibt.
	 *
	 * @param pAudio Ein Audio-Objekt.
	 * @param pVolumeFactor Der Lautstärke-Faktor.
	 */
	public void add( Audio pAudio, double pVolumeFactor ) {
		if( !contains(pAudio) ) {
			audios.add(new AudioWrapper(pAudio, (float) pVolumeFactor));
		} else {
			findWrapper(pAudio).volumeFactor = (float) pVolumeFactor;
		}
		pAudio.setVolume(pVolumeFactor * volume);
	}

	/**
	 * Entfernt die das angegebene Audio-Objekt aus dem Mixer. Ist das Objekt
	 * nicht Teil des Mixers, passiert nichts.
	 *
	 * @param pAudio Ein Audio-Objekt.
	 */
	public void remove( Audio pAudio ) {
		Iterator<AudioWrapper> it = audios.listIterator();
		while( it.hasNext() ) {
			AudioWrapper aw = it.next();
			if( aw.audio == pAudio ) {
				it.remove();
				break;
			}
		}
	}

	public void removeAll() {
		audios.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPlaying() {
		return audios.stream().anyMatch(aw -> aw.audio.isPlaying());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLooping() {
		return audios.stream().anyMatch(aw -> aw.audio.isLooping());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVolume( double pVolume ) {
		volume = (float) pVolume;
		audios.stream().forEach(aw -> aw.audio.setVolume(aw.volumeFactor * pVolume));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getVolume() {
		return volume;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void play() {
		audios.stream().forEach(aw -> aw.audio.play());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void playAndWait() {
		audios.stream().forEach(aw -> aw.audio.play());
		while( audios.stream().anyMatch(aw -> aw.audio.isPlaying()) ) {
			try {
				Thread.sleep(10);
			} catch( InterruptedException e ) {
				// Just keep waiting
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loop() {
		audios.stream().forEach(aw -> aw.audio.loop());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop() {
		audios.stream().forEach(aw -> aw.audio.stop());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		if( isPlaying() ) {
			stop();
		}
		audios.stream().forEach(aw -> aw.audio.dispose());
	}

	/**
	 * Ändert die Lautstärke aller hinzugefügten Audiomedien in der angegebenen
	 * Zeit schrittweise, bis die angegebene Lautstärke erreicht ist.
	 * <p>
	 * Zu beachten ist, dass die Lautstärke des Mixers angepasst wird. Das
	 * bedeutet, dass die Lautstärke der hinzugefügten Medien mit ihrem
	 * Lautstärkefaktor multipliziert werden. Die Medien haben am Ende also
	 * nicht unbedingt die Lautstärke {@code to}.
	 *
	 * @param to Der Zielwert für die Lautstärke.
	 * @param time Die Zeit, nach der die Änderung abgeschlossen sein soll.
	 */
	public void fade( final double to, final int time ) {
		TaskRunner.run(new Runnable() {
			@Override
			public void run() {
				final long start = System.currentTimeMillis();
				double t = 0.0;
				double from = volume;
				do {
					setVolume(Constants.interpolate(from, to, t));
					t = (double) (System.currentTimeMillis() - start) / (double) time;

					try {
						Thread.sleep(1000 / Constants.framesPerSecond);
					} catch( InterruptedException e ) {
						// Keep waiting
					}
				} while( t < 1.0 );
				setVolume(to);
			}
		});
	}

	@Override
	public void playbackStarted( Audio source ) {
		if( eventDispatcher != null ) {
			eventDispatcher.dispatchEvent("start", Mixer.this);
		}
	}

	@Override
	public void playbackStopped( Audio source ) {
		if( !isPlaying() ) {
			if( eventDispatcher != null ) {
				eventDispatcher.dispatchEvent("stop", Mixer.this);
			}
		}
	}

	@Override
	public void addAudioListener( AudioListener listener ) {
		initializeEventDispatcher().addListener(listener);
	}

	@Override
	public void removeAudioListener( AudioListener listener ) {
		initializeEventDispatcher().removeListener(listener);
	}

	/**
	 * Interne Methode, um den Listener-Mechanismus zu initialisieren. Wird erst
	 * aufgerufen, soblad sich auch ein Listener registrieren möchte.
	 * @return
	 */
	private EventDispatcher<Audio, AudioListener> initializeEventDispatcher() {
		if( eventDispatcher == null ) {
			eventDispatcher = new EventDispatcher<>();
			eventDispatcher.registerEventType("start", (a,l) -> l.playbackStarted(a));
			eventDispatcher.registerEventType("stop", (a,l) -> l.playbackStopped(a));
		}
		return eventDispatcher;
	}

	class AudioWrapper {

		Audio audio;

		float volumeFactor;

		public AudioWrapper( Audio audio, float volumeFactor ) {
			this.audio = audio;
			this.volumeFactor = volumeFactor;
		}

	}

}
