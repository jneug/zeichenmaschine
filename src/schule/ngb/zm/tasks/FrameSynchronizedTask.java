package schule.ngb.zm.tasks;

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
		running = true;
		int lastTick = 0;
		Object lock = Zeichenmaschine.globalSyncLock;

		while( running ) {
			lastTick = Constants.tick;
			this.update(lastTick);

			synchronized( lock ) {
				while( lastTick >= Constants.tick ) {
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
	}

	@Override
	public boolean isActive() {
		return false;
	}

}
