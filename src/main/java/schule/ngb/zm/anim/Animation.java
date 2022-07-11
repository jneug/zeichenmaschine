package schule.ngb.zm.anim;

import schule.ngb.zm.events.EventDispatcher;

public class Animation {

	EventDispatcher<Animation, AnimationListener> eventDispatcher;

	private EventDispatcher<Animation, AnimationListener> initializeEventDispatcher() {
		if( eventDispatcher == null ) {
			eventDispatcher = new EventDispatcher<>();
			eventDispatcher.registerEventType("start", ( a, l ) -> l.animationStarted(a));
			eventDispatcher.registerEventType("stop", ( a, l ) -> l.animationStopped(a));
		}
		return eventDispatcher;
	}

	public void addListener( AnimationListener listener ) {
		initializeEventDispatcher().addListener(listener);
	}

	public void removeListener( AnimationListener listener ) {
		initializeEventDispatcher().removeListener(listener);
	}

}
