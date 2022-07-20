package schule.ngb.zm.util.tasks;

public abstract class RateLimitedTask extends Task {

	public abstract int getRate();

	@Override
	public final void run() {
		if( running || done ) {
			return;
		}

		initialize();

		// current time in ns
		long beforeTime = System.nanoTime();
		// store for deltas
		long overslept = 0L;
		// delta in ms
		double delta = 0;

		running = true;
		while( running ) {
			// delta in seconds
			delta = (System.nanoTime() - beforeTime) / 1000000000.0;
			beforeTime = System.nanoTime();

			this.update(delta);

			// delta time in ns
			long afterTime = System.nanoTime();
			long dt = afterTime - beforeTime;
			long sleep = 0;
			if( getRate() > 0 ) {
				sleep = ((1000000000L / getRate()) - dt) - overslept;
			}

			if( sleep > 0 ) {
				try {
					Thread.sleep(sleep / 1000000L, (int) (sleep % 1000000L));
				} catch( InterruptedException e ) {
					// Interrupt not relevant
				}
			} else {
				Thread.yield();
			}
			// Did we sleep to long?
			overslept = (System.nanoTime() - afterTime) - sleep;
		}

		running = false;
		done = true;

		finish();
	}

}
