package schule.ngb.zm.media;

import schule.ngb.zm.util.ResourceStreamProvider;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class Sound implements Audio {

	private boolean playing = false;

	private boolean looping = false;

	private String audioSource;

	private Clip audioClip;

	private boolean disposeAfterPlay = false;

	private float volume = 0.8f;

	public Sound( String source ) {
		this.audioSource = source;
	}

	public String getSource() {
		return audioSource;
	}

	public boolean isPlaying() {
		// return audioClip != null && audioClip.isRunning();
		return playing;
	}

	public boolean isLooping() {
		if( !playing ) {
			looping = false;
		}
		return looping;
	}

	public void setVolume( double volume ) {
		this.volume = (float) volume;
		if( audioClip != null ) {
			applyVolume();
		}
	}

	private void applyVolume() {
		FloatControl gainControl =
			(FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);

		float vol = 20f * (float) Math.log10(volume);
		// vol = (float) Constants.limit(vol, gainControl.getMinimum(), gainControl.getMaximum());
		gainControl.setValue(vol);
	}

	public void stop() {
		looping = false;
		if( audioClip.isRunning() ) {
			audioClip.stop();
		}
		playing = false;
	}

	public void play() {
		if( this.openClip() ) {
			audioClip.start();
			playing = true;
		}
	}

	public void playOnce() {
		disposeAfterPlay = true;
		play();
	}

	public void playOnceAndWait() {
		disposeAfterPlay = true;
		playAndWait();
	}

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

	public void loop() {
		loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void loop( int count ) {
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
			InputStream in = ResourceStreamProvider.getResourceStream(audioSource);
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
				LOGGER.warning("Sound source " + audioSource + " could not be played: No audio source found.");
			}
		} catch( UnsupportedAudioFileException ex ) {
			LOGGER.warning("Sound source " + audioSource + " could not be played: The specified audio file is not supported.");
			LOGGER.throwing("Sound", "openClip", ex);
		} catch( LineUnavailableException ex ) {
			LOGGER.warning("Sound source " + audioSource + " could not be played: Audio line for playing back is unavailable.");
			LOGGER.throwing("Sound", "openClip", ex);
		} catch( IOException ex ) {
			LOGGER.warning("Sound source " + audioSource + " could not be played: Error playing the audio file.");
			LOGGER.throwing("Sound", "openClip", ex);
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

	private void playbackStopped() {
		playing = false;
		if( disposeAfterPlay ) {
			this.dispose();
			disposeAfterPlay = false;
		}
	}

	/*
	public void addLineListener( LineListener listener ) {
		if( audioClip == null ) {
			openClip();
		}
		if( audioClip != null ) {
			audioClip.addLineListener(listener);
		}
	}
	*/

	private static final Logger LOGGER = Logger.getLogger(Sound.class.getName());

}
