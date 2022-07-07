package schule.ngb.zm.tasks;

import schule.ngb.zm.Updatable;

public abstract class Task implements Runnable, Updatable {

	protected boolean running = false;

	protected boolean done = false;

	@Override
	public boolean isActive() {
		return running;
	}

	public boolean isDone() {
		return !running & done;
	}

	public void stop() {
		running = false;
	}

}
