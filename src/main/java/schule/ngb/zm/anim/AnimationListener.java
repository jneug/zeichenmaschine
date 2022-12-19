package schule.ngb.zm.anim;

import schule.ngb.zm.util.events.Listener;

public interface AnimationListener extends Listener<Animation> {

	void animationStarted( Animation anim );

	void animationStopped( Animation anim );

}
