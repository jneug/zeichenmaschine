package schule.ngb.zm.media;

import schule.ngb.zm.anim.Animation;
import schule.ngb.zm.anim.AnimationListener;
import schule.ngb.zm.events.EventDispatcher;
import schule.ngb.zm.tasks.TaskRunner;
import schule.ngb.zm.util.Log;
import schule.ngb.zm.util.ResourceStreamProvider;
import schule.ngb.zm.util.Validator;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/**
 * Ein Musikstück, dass im Projekt abgespielt werden soll.
 * <p>
 * Im gegensatz zu einem {@link Sound} sind Musikstücke längere Audiodateien,
 * die zum Beispiel als Hintergrundmusik ablaufen sollen. Die Musik wird daher
 * nicht komplett in den Speicher geladen, sondern direkt aus der Audioquelle
 * gestreamt und wiedergegeben.
 */
public class Music implements Audio {

	// size of the byte buffer used to read/write the audio stream
	private static final int BUFFER_SIZE = 4096;


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
	 * Der AudioStream, um die AUdiosdaten zulsen, falls dieser schon geöffnet
	 * wurde. Sonst {@code null}.
	 */
	private AudioInputStream audioStream;

	/**
	 * Die Line für die Ausgabe, falls diese schon geöffnet wurde. Sonst
	 * {@code null}.
	 */
	private SourceDataLine audioLine;

	/**
	 * Die Lautstärke der Musik.
	 */
	private float volume = 0.8f;

	EventDispatcher<Audio, AudioListener> eventDispatcher;

	public Music( String source ) {
		Validator.requireNotNull(source);
		this.audioSource = source;
	}

	public String getSource() {
		return audioSource;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPlaying() {
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
		if( audioLine != null ) {
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
			(FloatControl) audioLine.getControl(FloatControl.Type.MASTER_GAIN);

		float vol = 20f * (float) Math.log10(volume);
		// vol = (float) Constants.limit(vol, gainControl.getMinimum(), gainControl.getMaximum());
		gainControl.setValue(vol);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void play() {
		if( openLine() ) {
			TaskRunner.run(new Runnable() {
				@Override
				public void run() {
					stream();
				}
			});
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void playAndWait() {
		if( openLine() ) {
			stream();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loop() {
		looping = true;
		play();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop() {
		playing = false;
		looping = false;
		dispose();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		if( audioLine != null ) {
			if( audioLine.isRunning() ) {
				playing = false;
				audioLine.stop();
			}
			if( audioLine.isOpen() ) {
				audioLine.drain();
				audioLine.close();

			}
		}
		try {
			if( audioStream != null ) {
				audioStream.close();
			}
		} catch( IOException ex ) {
		}

		audioLine = null;
		audioStream = null;
	}

	private void stream() {
		audioLine.start();
		playing = true;
		eventDispatcher.dispatchEvent("start", Music.this);

		byte[] bytesBuffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;

		try {
			while( playing && (bytesRead = audioStream.read(bytesBuffer)) != -1 ) {
				audioLine.write(bytesBuffer, 0, bytesRead);
			}

			audioLine.drain();
			audioLine.stop();
		} catch( IOException ex ) {
			LOG.warn(ex, "Error while playing Music source <%s>", audioSource);
		}

		// Wait for the remaining audio to play
		/*while( audioLine.isRunning() ) {
			try {
				Thread.sleep(10);
			} catch( InterruptedException ex ) {
				// Just keep waiting ...
			}
		}*/

		playing = false;
		streamingStopped();
		eventDispatcher.dispatchEvent("stop", Music.this);
	}

	private boolean openLine() {
		if( audioLine != null ) {
			return true;
		}

		try {
			URL url = ResourceStreamProvider.getResourceURL(audioSource);
			if( url != null ) {
				final AudioInputStream inStream = AudioSystem.getAudioInputStream(url);
				AudioFormat format = inStream.getFormat();

				final int ch = format.getChannels();
				final float rate = format.getSampleRate();
				AudioFormat outFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);

				DataLine.Info info = new DataLine.Info(SourceDataLine.class, outFormat);

				audioLine = (SourceDataLine) AudioSystem.getLine(info);
				audioLine.open(outFormat);
				applyVolume();

				audioStream = AudioSystem.getAudioInputStream(outFormat, inStream);
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

	private void streamingStopped() {
		dispose();

		if( looping ) {
			if( openLine() ) {
				stream();
			} else {
				playing = false;
				looping = false;
			}
		} else {
			playing = false;
		}
	}

	public void addListener( AudioListener listener ) {
		initializeEventDispatcher().addListener(listener);
	}

	public void removeListener( AudioListener listener ) {
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
			eventDispatcher.registerEventType("start", (a,l) -> l.start(a));
			eventDispatcher.registerEventType("stop", (a,l) -> l.stop(a));
		}
		return eventDispatcher;
	}

	private static final Log LOG = Log.getLogger(Music.class);

}
