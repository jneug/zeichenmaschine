package schule.ngb.zm.util.tasks;

import schule.ngb.zm.Constants;

public abstract class FramerateLimitedTask extends RateLimitedTask {

	public int getRate() {
		return Constants.framesPerSecond;
	}

}
