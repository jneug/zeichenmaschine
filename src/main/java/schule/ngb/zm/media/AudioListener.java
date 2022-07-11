package schule.ngb.zm.media;

import schule.ngb.zm.events.Listener;

public interface AudioListener extends Listener<Audio> {

	void start( Audio source );

	void stop( Audio source );

}
