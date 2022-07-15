package schule.ngb.zm.media;

import schule.ngb.zm.util.Log;
import schule.ngb.zm.util.ResourceStreamProvider;
import schule.ngb.zm.util.Validator;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Wiedergabe kurzer Soundclips, die mehrmals wiederverwendet werden.
 * <p>
 * In Spielen und anderen Projekten gibt es oftmals eine Reihe kurzer Sounds,
 * die zusammen mit bestimmten Aktionen wiedergegeben werden (zum Beispiel, wenn
 * die Spielfigur springt, wenn zwei Objekte kollidieren, usw.). Sounds werden
 * komplett in den Speicher geladen und können dadurch immer wieder, als
 * Schleife oder auch nur Abschnittsweise abgespielt werden.
 * <p>
 * Für längre Musikstücke (beispielsweise Hintergrundmusik) bietet sich eher die
 * KLasse {@link Music} an.
 */
@SuppressWarnings( "unused" )
public class Sound implements Audio {

	/**
	 * Ob der Sound gerade abgespielt wird.
	 */
	private boolean playing = false;

	/**
	 * Ob der Sound gerade in einer Schleife abgespielt wird.
	 */
	private boolean looping = false;

	/**
	 * Die Quelle des Musikstücks.
	 */
	private String audioSource;

	/**
	 * Der Clip, falls er schon geladen wurde, sonst {@code null}.
	 */
	private Clip audioClip;

	/**
	 * Ob die Resourcen des Clips im Speicher nach dem nächsten Abspielen
	 * freigegeben werden sollen.
	 */
	private boolean disposeAfterPlay = false;

	/**
	 * Die Lautstärke des Clips.
	 */
	private float volume = 0.8f;

	/**
	 * Erstellt einen Sound aus der angegebene Quelle.
	 *
	 * @param source Ein Dateipfad oder eine Webadresse.
	 * @throws NullPointerException Falls die Quelle {@code null} ist.
	 */
	public Sound( String source ) {
		Validator.requireNotNull(source);
		this.audioSource = source;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPlaying() {
		// return audioClip != null && audioClip.isRunning();
		return playing;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLooping() {
		if( !playing ) {
			looping = false;
		}
		return looping;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVolume( double volume ) {
		this.volume = (float) volume;
		if( audioClip != null ) {
			applyVolume();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getVolume() {
		return volume;
	}

	/**
	 * Interne Methode, um die gesetzte Lautstärke vor dem Abspielen
	 * anzuwenden.
	 */
	private void applyVolume() {
		FloatControl gainControl =
			(FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);

		float vol = 20f * (float) Math.log10(volume);
		// vol = (float) Constants.limit(vol, gainControl.getMinimum(), gainControl.getMaximum());
		gainControl.setValue(vol);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop() {
		looping = false;
		if( audioClip.isRunning() ) {
			audioClip.stop();
		}
		playing = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void play() {
		if( this.openClip() ) {
			audioClip.start();
			playing = true;
		}
	}

	/**
	 * {@inheritDoc}
	 */
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
	 * Spielt den Sound genau einmal ab und gibt danach alle Resourcen des Clips
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
	 * Spielt den Sound genau einmal ab und gibt danach alle Resourcen des Clips
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loop() {
		loop(Clip.LOOP_CONTINUOUSLY);
	}

	/**
	 * Wiederholt den Sound die angegebene Anzahl an Wiederholungen ab und stoppt
	 * die Wiedergabe dann.
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		if( audioClip != null ) {
			if( audioClip.isRunning() ) {
				audioClip.stop();
			}
			audioClip.close();
			audioClip = null;
		}
	}

	private boolean openClip() {
		if( audioClip != null ) {
			audioClip.setFramePosition(0);
			return true;
		}

		try {
			InputStream in = ResourceStreamProvider.getInputStream(audioSource);
			if( in != null ) {
				AudioInputStream audioStream = AudioSystem.getAudioInputStream(in);
				AudioFormat format = audioStream.getFormat();
				DataLine.Info info = new DataLine.Info(Clip.class, format);

				audioClip = (Clip) AudioSystem.getLine(info);
				audioClip.addLineListener(new LineListener() {
					@Override
					public void update( LineEvent event ) {
						if( event.getType() == LineEvent.Type.STOP ) {
							playbackStopped();
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
	 * Interne Methode, die aufgerufen wird, wenn die Wiedergabe gestoppt wird.
	 * Entweder durch einen Aufruf von {@link #stop()} oder, weil die Wiedergabe
	 * nach {@link #playOnce()} beendet wurde.
	 */
	private void playbackStopped() {
		playing = false;
		if( disposeAfterPlay ) {
			this.dispose();
			disposeAfterPlay = false;
		}
	}

	private static final Log LOG = Log.getLogger(Sound.class);

}
