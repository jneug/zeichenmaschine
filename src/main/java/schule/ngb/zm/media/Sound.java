package schule.ngb.zm.media;

import schule.ngb.zm.util.Log;
import schule.ngb.zm.util.Validator;
import schule.ngb.zm.util.events.EventDispatcher;
import schule.ngb.zm.util.io.ResourceStreamProvider;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/**
 * Ein kurzer Soundclip, der mehrmals wiederverwendet werden kann.
 * <p>
 * In Spielen und anderen Projekten gibt es oftmals eine Reihe kurzer
 * Soundclips, die zusammen mit bestimmten Aktionen wiedergegeben werden (zum
 * Beispiel, wenn die Spielfigur springt, wenn zwei Objekte kollidieren, usw.).
 * Sounds werden vollständig in den Speicher geladen und können immer wieder,
 * als Schleife oder auch nur Abschnittsweise, abgespielt werden.
 * <p>
 * Für längere Musikstücke (beispielsweise Hintergrundmusik) bietet sich eher
 * die Klasse {@link Music} an.
 *
 * <h4>MP3-Dateien verwenden</h4>
 * Java kann nativ nur Waveform ({@code .wav}) Dateien wiedergeben. Um auch
 * MP3-Dateien zu nutzen, müssen die Bibliotheken <a href="#">jlayer</a>, <a
 * href="#">tritonus-share</a> und <a href="#">mp3spi</a> eingebunden werden.
 * Details zur Verwendung können in der <a
 * href="https://zeichenmaschine.xyz/installation/#unterstutzung-fur-mp3">Dokumentation
 * der Zeichenmaschine</a> nachgelesen werden.
 */
@SuppressWarnings( "unused" )
public class Sound implements Audio {

	/**
	 * Ob der Sound aktuell abgespielt wird.
	 */
	private boolean playing = false;

	/**
	 * Ob der Sound aktuell in einer Schleife abgespielt wird.
	 */
	private boolean looping = false;

	/**
	 * Die Quelle der Audiodaten.
	 */
	private String audioSource;

	/**
	 * Der Clip, falls er schon geladen wurde. Ansonsten {@code null}.
	 */
	private Clip audioClip;

	/**
	 * Ob die Ressourcen des Clips im Speicher nach dem nächsten Abspielen
	 * freigegeben werden sollen.
	 */
	private boolean disposeAfterPlay = false;

	/**
	 * Die aktuelle Lautstärke des Clips.
	 */
	private float volume = 0.8f;

	/**
	 * Dispatcher für Audio-Events (start und stop).
	 */
	EventDispatcher<Audio, AudioListener> eventDispatcher;

	/**
	 * Erstellt einen Sound aus der angegebene Quelle.
	 *
	 * @param source Quelle der Audiodaten.
	 * @throws NullPointerException Falls die Quelle {@code null} ist.
	 * @see ResourceStreamProvider#getResourceURL(String)
	 */
	public Sound( String source ) {
		Validator.requireNotNull(source);
		this.audioSource = source;
	}

	@Override
	public String getSource() {
		return audioSource;
	}

	@Override
	public boolean isPlaying() {
		// return audioClip != null && audioClip.isRunning();
		return playing;
	}

	@Override
	public boolean isLooping() {
		if( !playing ) {
			looping = false;
		}
		return looping;
	}

	@Override
	public void setVolume( double volume ) {
		this.volume = volume < 0 ? 0f : (float) volume;
		if( audioClip != null ) {
			applyVolume();
		}
	}

	@Override
	public double getVolume() {
		return volume;
	}

	/**
	 * Wendet die Lautstärke vor dem Abspielen auf den Clip an.
	 */
	private void applyVolume() {
		FloatControl gainControl =
			(FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);

		float vol = 20f * (float) Math.log10(volume);
		// vol = (float) Constants.limit(vol, gainControl.getMinimum(), gainControl.getMaximum());
		gainControl.setValue(vol);
	}

	@Override
	public void stop() {
		looping = false;
		if( audioClip.isRunning() ) {
			audioClip.stop();
		}
		playing = false;
	}

	@Override
	public void play() {
		if( this.openClip() ) {
			audioClip.start();
			playing = true;
		}
	}

	@Override
	public void playAndWait() {
		this.play();

		long audioLen = audioClip.getMicrosecondLength();
		while( playing ) {
			try {
				long ms = (audioLen - audioClip.getMicrosecondPosition()) / 1000L;
				Thread.sleep(ms);
			} catch( InterruptedException ex ) {
				// Ignore
			}
		}

		audioClip.close();
	}

	/**
	 * Spielt den Sound einmal ab und gibt danach alle Ressourcen des Clips
	 * frei.
	 * <p>
	 * Der Aufruf ist effektiv gleich zu
	 * <pre><code>
	 * clip.playAndWait();
	 * clip.dispose();
	 * </code></pre>
	 * allerdings wird der aufrufende Thread nicht blockiert und
	 * {@link #dispose()} automatisch am Ende aufgerufen.
	 */
	public void playOnce() {
		disposeAfterPlay = true;
		play();
	}

	/**
	 * Spielt den Sound einmal ab und gibt danach alle Ressourcen des Clips
	 * frei.
	 * <p>
	 * Der Aufruf entspricht
	 * <pre><code>
	 * clip.playAndWait();
	 * clip.dispose();
	 * </code></pre>
	 */
	public void playOnceAndWait() {
		disposeAfterPlay = true;
		playAndWait();
	}

	@Override
	public void loop() {
		loop(Clip.LOOP_CONTINUOUSLY);
	}

	/**
	 * Wiederholt den Sound die angegebene Anzahl an Wiederholungen und stoppt
	 * die Wiedergabe dann.
	 * <p>
	 * Wird {@code count} auf {@link Clip#LOOP_CONTINUOUSLY} gesetzt (-1), wird
	 * der Clip unendlich oft wiederholt. Der Aufruf entspricht dann
	 * {@link #loop()}.
	 *
	 * @param count Anzahl der Wiederholungen.
	 */
	public void loop( int count ) {
		if( count > 0 ) {
			int loopCount = count;
			if( loopCount != Clip.LOOP_CONTINUOUSLY ) {
				if( loopCount <= 0 ) {
					return;
				}
				// Adjust Number of loops
				loopCount -= 1;
			}
			if( openClip() ) {
				looping = true;
				audioClip.loop(loopCount);
				playing = true;
			}
		}
	}

	@Override
	public synchronized void dispose() {
		if( audioClip != null ) {
			if( audioClip.isRunning() ) {
				stop();
			}
			audioClip.close();
			audioClip = null;
		}
	}

	/**
	 * Lädt falls nötig den {@link Clip} für die
	 * {@link #audioSource Audioquelle} und startet die Wiedergabe.
	 *
	 * @return {@code true}, wenn der Clip geöffnet werden konnte, {@code false}
	 * 	sonst.
	 */
	private synchronized boolean openClip() {
		if( audioClip != null ) {
			audioClip.setFramePosition(0);
			return true;
		}

		try {
			URL url = ResourceStreamProvider.getResourceURL(audioSource);
			if( url != null ) {
				final AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
				AudioFormat format = audioStream.getFormat();
				DataLine.Info info = new DataLine.Info(Clip.class, format);

				audioClip = (Clip) AudioSystem.getLine(info);
				audioClip.addLineListener(new LineListener() {
					@Override
					public void update( LineEvent event ) {
						if( event.getType() == LineEvent.Type.START ) {
							if( eventDispatcher != null ) {
								eventDispatcher.dispatchEvent("start", Sound.this);
							}
						} else if( event.getType() == LineEvent.Type.STOP ) {
							playbackStopped();

							if( eventDispatcher != null ) {
								eventDispatcher.dispatchEvent("stop", Sound.this);
							}
						}
					}
				});
				audioClip.open(audioStream);
				applyVolume();
				return true;
			} else {
				LOG.warn("Sound source <%s> could not be played: No audio source found.", audioSource);
			}
		} catch( UnsupportedAudioFileException ex ) {
			LOG.warn(ex, "Sound source <%s> could not be played: The specified audio file is not supported.", audioSource);
		} catch( LineUnavailableException ex ) {
			LOG.warn(ex, "Sound source <%s> could not be played: Audio line for playing back is unavailable.", audioSource);
		} catch( IOException ex ) {
			LOG.warn(ex, "Sound source <%s> could not be played: Error playing the audio file.", audioSource);
		}
		return false;
	}

	/*@Override
	public void update( LineEvent event ) {
		LineEvent.Type type = event.getType();

		if( type == LineEvent.Type.START ) {
			playing = true;
		} else if( type == LineEvent.Type.STOP ) {
			playing = false;
			if( disposeAfterPlay ) {
				this.dispose();
				disposeAfterPlay = false;
			}
		}
	}*/

	/**
	 * Wird aufgerufen, wenn die Wiedergabe beendet wurde. Entweder durch einen
	 * Aufruf von {@link #stop()} oder, weil die Wiedergabe nach
	 * {@link #playOnce()} beendet wurde.
	 * <p>
	 * Falls {@link #disposeAfterPlay} gesetzt ist, wird nach dem Ende der
	 * Wiedergabe {@link #dispose()} aufgerufen.
	 */
	private void playbackStopped() {
		playing = false;

		if( disposeAfterPlay ) {
			this.dispose();
			disposeAfterPlay = false;
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
	 * aufgerufen, sobald sich der erste Listener anmelden möchte.
	 *
	 * @return Der {@code EventDispatcher} für dieses Objekt.
	 */
	private EventDispatcher<Audio, AudioListener> initializeEventDispatcher() {
		if( eventDispatcher == null ) {
			eventDispatcher = new EventDispatcher<>();
			eventDispatcher.registerEventType("start", ( a, l ) -> l.playbackStarted(a));
			eventDispatcher.registerEventType("stop", ( a, l ) -> l.playbackStopped(a));
		}
		return eventDispatcher;
	}

	private static final Log LOG = Log.getLogger(Sound.class);

}
