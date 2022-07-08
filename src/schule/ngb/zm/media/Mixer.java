package schule.ngb.zm.media;

import schule.ngb.zm.Constants;
import schule.ngb.zm.tasks.TaskRunner;

import java.util.ArrayList;
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
public class Mixer implements Audio {

	private List<AudioWrapper> audios;

	private float volume = 0.8f;

	class AudioWrapper {

		Audio audio;

		float volumeFactor;

		public AudioWrapper( Audio audio, float volumeFactor ) {
			this.audio = audio;
			this.volumeFactor = volumeFactor;
		}

	}

	public Mixer() {
		this.audios = new ArrayList<>(4);
	}

	public String getSource() {
		return "";
	}

	public void add( Audio pAudio ) {
		add(pAudio, 1f);
	}

	public void add( Audio pAudio, double pVolumeFactor ) {
		audios.add(new AudioWrapper(pAudio, (float) pVolumeFactor));
		pAudio.setVolume(pVolumeFactor * volume);
	}

	public void remove( Audio pAudio ) {
	}

	public void removeAll() {
		audios.clear();
	}

	@Override
	public boolean isPlaying() {
		return audios.stream().anyMatch(aw -> aw.audio.isPlaying());
	}

	@Override
	public boolean isLooping() {
		return audios.stream().anyMatch(aw -> aw.audio.isLooping());
	}

	@Override
	public void setVolume( double pVolume ) {
		volume = (float) pVolume;
		audios.stream().forEach(aw -> aw.audio.setVolume(aw.volumeFactor * pVolume));
	}

	@Override
	public void playOnce() {
		audios.stream().forEach(aw -> aw.audio.playOnce());
	}

	@Override
	public void playOnceAndWait() {
		audios.stream().forEach(aw -> aw.audio.playOnce());
		while( audios.stream().anyMatch(aw -> aw.audio.isPlaying()) ) {
			try {
				Thread.sleep(10);
			} catch( InterruptedException e ) {
				// Just keep waiting
			}
		}
	}

	@Override
	public void loop() {
		audios.stream().forEach(aw -> aw.audio.loop());
	}

	@Override
	public void stop() {
		audios.stream().forEach(aw -> aw.audio.stop());
	}

	@Override
	public void dispose() {
		if( isPlaying() ) {
			stop();
		}
		audios.stream().forEach(aw -> aw.audio.dispose());
	}

	public void fade( final double to, final int time ) {
		TaskRunner.run(new Runnable() {
			@Override
			public void run() {
				final long start = System.currentTimeMillis();
				double t = 0.0;
				double from = volume;
				if( !isPlaying() ) {
					playOnce();
				}
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

}
