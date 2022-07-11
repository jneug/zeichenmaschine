package schule.ngb.zm.tasks;

import schule.ngb.zm.Constants;

public abstract class FramerateLimitedTask extends RateLimitedTask {

	public int getRate() {
		return Constants.framesPerSecond;
	}

}
