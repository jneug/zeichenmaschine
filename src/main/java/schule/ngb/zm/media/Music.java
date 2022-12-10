package schule.ngb.zm.media;

import schule.ngb.zm.util.Log;
import schule.ngb.zm.util.Validator;
import schule.ngb.zm.util.events.EventDispatcher;
import schule.ngb.zm.util.io.ResourceStreamProvider;
import schule.ngb.zm.util.tasks.TaskRunner;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/**
 * Eine Musik, die abgespielt werden kann.
 * <p>
 * Im Gegensatz zu einem {@link Sound} wird {@code Music} für längere
 * Audiodateien benutzt, die zum Beispiel als Hintergrundmusik gespielt werden.
 * Die Audiodaten werden daher nicht vollständig in den Speicher geladen,
 * sondern direkt aus der Quelle gestreamt und direkt wiedergegeben.
 * <p>
 * Daher ist es nicht möglich, die länge der Musik im Vorfeld abzufragen oder zu
 * einer bestimmten Stelle im Stream zu springen.
 *
 * <h2>MP3-Dateien verwenden</h2>
 * Java kann nativ nur Waveform ({@code .wav}) Dateien wiedergeben. Um auch
 * MP3-Dateien zu nutzen, müssen die Bibliotheken <a href="#">jlayer</a>, <a
 * href="#">tritonus-share</a> und <a href="#">mp3spi</a> eingebunden werden.
 * Details zur Verwendung können in der <a
 * href="https://zeichenmaschine.xyz/installation/#unterstutzung-fur-mp3">Dokumentation
 * der Zeichenmaschine</a> nachgelesen werden.
 */
// TODO: Wann sollten Listener beim Loopen informiert werden? Nach jedem Loop oder erst ganz am Ende?
@SuppressWarnings( "unused" )
public class Music implements Audio {

	/**
	 * Größe des verwendeten Input-Puffers für die Audiodaten.
	 */
	private static final int BUFFER_SIZE = 4096;


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
	 * Der {@link AudioInputStream}, um die Audiosdaten zu lesen. {@code null},
	 * falls noch kein Stream geöffnet wurde.
	 */
	private AudioInputStream audioStream;

	/**
	 * Die {@link SourceDataLine} für die Ausgabe. {@code null}, falls die
	 * Audiodatei noch nicht geöffnet wurde.
	 */
	private SourceDataLine audioLine;

	/**
	 * Die aktuelle Lautstärke des Mediums.
	 */
	private float volume = 0.8f;

	/**
	 * Dispatcher für Audio-Events (start und stop).
	 */
	EventDispatcher<Audio, AudioListener> eventDispatcher;

	/**
	 * Erstellt eine Musik aus der angegebenen Audioquelle.
	 *
	 * @param audioSource Quelle der Audiodaten.
	 * @throws NullPointerException Falls die Quelle {@code null} ist.
	 * @see ResourceStreamProvider#getResourceURL(String)
	 */
	public Music( String audioSource ) {
		Validator.requireNotNull(audioSource);
		this.audioSource = audioSource;
	}

	@Override
	public String getSource() {
		return audioSource;
	}

	@Override
	public boolean isPlaying() {
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
		if( audioLine != null ) {
			applyVolume();
		}
	}

	@Override
	public double getVolume() {
		return volume;
	}

	/**
	 * Wendet die Lautstärke vor dem Abspielen auf den Audiostream an.
	 */
	private void applyVolume() {
		FloatControl gainControl =
			(FloatControl) audioLine.getControl(FloatControl.Type.MASTER_GAIN);

		float vol = 20f * (float) Math.log10(volume);
		// vol = (float) Constants.limit(vol, gainControl.getMinimum(), gainControl.getMaximum());
		gainControl.setValue(vol);
	}

	@Override
	public void play() {
		if( openLine() ) {
			TaskRunner.run(this::stream);
		}
	}

	@Override
	public void playAndWait() {
		if( openLine() ) {
			stream();
		}
	}

	@Override
	public void loop() {
		looping = true;
		play();
	}

	@Override
	public void stop() {
		playing = false;
		looping = false;
		dispose();
	}

	@Override
	public synchronized void dispose() {
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

	/**
	 * Startet den Stream der Audiodaten und damit die Wiedergabe.
	 * <p>
	 * Die {@link #audioLine} muss vorher mit {@link #openLine()} geöffnet
	 * werden, ansonsten passiert nichts.
	 */
	private synchronized void stream() {
		if( audioLine == null ) {
			return;
		}

		audioLine.start();
		playing = true;
		if( eventDispatcher != null ) {
			eventDispatcher.dispatchEvent("start", Music.this);
		}

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
		if( eventDispatcher != null ) {
			eventDispatcher.dispatchEvent("stop", Music.this);
		}
	}

	/**
	 * Öffnet eine {@link SourceDataLine} für die
	 * {@link #audioSource Audioquelle} und bereitet die Wiedergabe vor. Es wird
	 * noch nichts abgespielt.
	 *
	 * @return {@code true}, wenn die Line geöffnet werden konnte, {@code false}
	 * 	sonst.
	 */
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

	/**
	 * Wird aufgerufen, wenn die Wiedergabe beendet wurde. Entweder durch einen
	 * Aufruf von {@link #stop()} oder weil keine Audiodaten mehr vorhanden
	 * sind.
	 * <p>
	 * Nach dem Ende des Streams wird {@link #dispose()} aufgerufen und, falls
	 * das Musikstück in einer Schleife abgespielt wird, der Stream direkt
	 * wieder gestartet.
	 */
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

	private static final Log LOG = Log.getLogger(Music.class);

}
