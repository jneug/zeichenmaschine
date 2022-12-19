package schule.ngb.zm.util.tasks;

import schule.ngb.zm.Constants;
import schule.ngb.zm.Zeichenmaschine;

public abstract class FrameSynchronizedTask extends Task {

	@Override
	public void run() {
		initialize();
		running = true;

		final Object lock = Zeichenmaschine.globalSyncLock;

		// current time in ns
		long beforeTime = System.nanoTime();
		// internal counters for tick and delta time
		int _tick;
		double delta;

		while( running ) {
			// delta in seconds
			delta = (System.nanoTime() - beforeTime) / 1000000000.0;
			beforeTime = System.nanoTime();

			_tick = Constants.tick;
			this.update(delta);

			synchronized( lock ) {
				while( _tick >= Constants.tick ) {
					try {
						lock.wait();
					} catch( InterruptedException e ) {
						// We got interrupted ...
					}
				}
			}
		}
		done = true;
		finish();
	}


}
