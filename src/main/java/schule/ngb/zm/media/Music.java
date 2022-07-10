package schule.ngb.zm.media;

import schule.ngb.zm.tasks.TaskRunner;
import schule.ngb.zm.util.ResourceStreamProvider;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Music implements Audio {

	// size of the byte buffer used to read/write the audio stream
	private static final int BUFFER_SIZE = 4096;


	private boolean playing = false;

	private boolean looping = false;

	private String audioSource;

	private AudioInputStream audioStream;

	private SourceDataLine audioLine;

	private float volume = 0.8f;

	public Music( String source ) {
		this.audioSource = source;
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
		this.volume = (float) volume;
		if( audioLine != null ) {
			applyVolume();
		}
	}

	private void applyVolume() {
		FloatControl gainControl =
			(FloatControl) audioLine.getControl(FloatControl.Type.MASTER_GAIN);

		float vol = 20f * (float) Math.log10(volume);
		// vol = (float) Constants.limit(vol, gainControl.getMinimum(), gainControl.getMaximum());
		gainControl.setValue(vol);
	}

	@Override
	public void playOnce() {
		if( openLine() ) {
			TaskRunner.run(new Runnable() {
				@Override
				public void run() {
					stream();
				}
			});
		}
	}

	@Override
	public void playOnceAndWait() {
		if( openLine() ) {
			stream();
		}
	}

	@Override
	public void loop() {
		looping = true;
		playOnce();
	}

	@Override
	public void stop() {
		playing = false;
		looping = false;
		dispose();
	}

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
		} catch( IOException ex ) {}

		audioLine = null;
		audioStream = null;
	}

	private void stream() {
		audioLine.start();
		playing = true;

		byte[] bytesBuffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;

		try {
			while (playing && (bytesRead = audioStream.read(bytesBuffer)) != -1) {
				audioLine.write(bytesBuffer, 0, bytesRead);
			}

			audioLine.drain();
			audioLine.stop();
		} catch( IOException ex ) {
			LOGGER.warning("Error while playing Music source <" + audioSource + ">");
			LOGGER.throwing("Music", "stream", ex);
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
				AudioFormat outFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, ch, ch*2, rate, false);

				DataLine.Info info = new DataLine.Info(SourceDataLine.class, outFormat);

				audioLine = (SourceDataLine) AudioSystem.getLine(info);
				audioLine.open(outFormat);
				applyVolume();

				audioStream = AudioSystem.getAudioInputStream(outFormat, inStream);
				return true;
			} else {
				LOGGER.warning("Sound source <" + audioSource + "> could not be played: No audio source found.");
			}
		} catch( UnsupportedAudioFileException ex ) {
			LOGGER.log(Level.WARNING, "Sound source <" + audioSource + "> could not be played: The specified audio file is not supported.", ex);
		} catch( LineUnavailableException ex ) {
			LOGGER.log(Level.WARNING, "Sound source <" + audioSource + "> could not be played: Audio line for playing back is unavailable.", ex);
		} catch( IOException ex ) {
			LOGGER.log(Level.WARNING, "Sound source <" + audioSource + "> could not be played: Error playing the audio file.", ex);
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

	//private static final Logger LOGGER = Logger.getLogger("schule.ngb.zm.media.Music");
	private static final Logger LOGGER = Logger.getLogger(Music.class.getName());

}
