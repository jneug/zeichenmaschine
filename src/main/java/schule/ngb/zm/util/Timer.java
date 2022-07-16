package schule.ngb.zm.util;

import java.util.concurrent.TimeUnit;

@SuppressWarnings( "unused" )
public final class Timer {

	private final TimeUnit baseUnit;

	private boolean running = false;

	private long starttime = -1;

	private long elapsed = 0;

	public Timer() {
		this(TimeUnit.MILLISECONDS);
	}

	public Timer( TimeUnit baseUnit ) {
		this.baseUnit = baseUnit;
	}

	public boolean isRunning() {
		return running;
	}

	@SuppressWarnings( "UnusedReturnValue" )
	public Timer start() {
		starttime = System.nanoTime();
		running = true;

		return this;
	}

	@SuppressWarnings( "UnusedReturnValue" )
	public Timer stop() {
		running = false;
		elapsed += System.nanoTime() - starttime;

		return this;
	}

	@SuppressWarnings( "UnusedReturnValue" )
	public Timer reset() {
		running = false;
		starttime = -1;
		elapsed = 0;

		return this;
	}

	public long getTime() {
		return getTime(baseUnit);
	}

	public long getTime( TimeUnit unit ) {
		if( running ) {
			return unit.convert(System.nanoTime() - starttime + elapsed, TimeUnit.NANOSECONDS);
		} else {
			return unit.convert(elapsed, TimeUnit.NANOSECONDS);
		}
	}

	public int getMillis() {
		if( running ) {
			return (int) ((System.nanoTime() - starttime + elapsed) / 1000000);
		} else {
			return (int) (elapsed / 1000000);
		}
	}

	public double getSeconds() {
		if( running ) {
			return (System.nanoTime() - starttime + elapsed) / 1000000000.0;
		} else {
			return elapsed / 1000000000.0;
		}
	}

}
