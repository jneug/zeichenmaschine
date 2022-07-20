package schule.ngb.zm.util.tasks;

import schule.ngb.zm.Constants;
import schule.ngb.zm.Zeichenmaschine;

public abstract class FrameSynchronizedTask extends Task {

	private static Thread mainThread;

	private static final Thread getMainThread() {
		if( mainThread == null ) {
			mainThread = Thread.currentThread();
			if( !mainThread.getName().equals(Constants.APP_NAME) ) {
				// Need to search for main Zeichenthread ...
			}
		}
		return mainThread;
	}

	@Override
	public void run() {
		initialize();
		running = true;

		Object lock = Zeichenmaschine.globalSyncLock;

		// start of thread in ms
		final long start = System.currentTimeMillis();
		// current time in ns
		long beforeTime = System.nanoTime();
		// store for deltas
		long overslept = 0L;
		// internal counters for tick and runtime
		int _tick = 0;

		double delta = 0.0;

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

		running = false;
		done = true;

		finish();
	}


}
