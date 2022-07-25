package schule.ngb.zm.anim;

import java.util.Collection;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

@SuppressWarnings( "unused" )
public class AnimationGroup<T> extends Animation<T> {

	List<Animation<T>> anims;


	private boolean overrideEasing = false;

	private int overrideRuntime = -1;

	private int lag = 0;

	private int active = 0;

	public AnimationGroup( Collection<Animation<T>> anims ) {
		this(0, -1, null, anims);
	}

	public AnimationGroup( int lag, Collection<Animation<T>> anims ) {
		this(lag, -1, null, anims);
	}

	public AnimationGroup( DoubleUnaryOperator easing, Collection<Animation<T>> anims ) {
		this(0, -1, easing, anims);
	}

	public AnimationGroup( int lag, DoubleUnaryOperator easing, Collection<Animation<T>> anims ) {
		this(lag, -1, easing, anims);
	}

	public AnimationGroup( int lag, int runtime, DoubleUnaryOperator easing, Collection<Animation<T>> anims ) {
		super();

		this.anims = List.copyOf(anims);
		this.lag = lag;

		if( easing != null ) {
			this.easing = easing;
			overrideEasing = true;
		}

		if( runtime > 0 ) {
			this.runtime = anims.size() * lag + runtime;
			this.overrideRuntime = runtime;
		} else {
			this.runtime = 0;
			for( int i = 0; i < this.anims.size(); i++ ) {
				if( i * lag + this.anims.get(i).getRuntime() > this.runtime ) {
					this.runtime = i * lag + this.anims.get(i).getRuntime();
				}
			}
		}
	}

	@Override
	public T getAnimationTarget() {
		for( Animation<T> anim : anims ) {
			if( anim.isActive() ) {
				return anim.getAnimationTarget();
			}
		}
		return anims.get(anims.size() - 1).getAnimationTarget();
	}

	@Override
	public void update( double delta ) {
		elapsedTime += (int) (delta * 1000);
		// Animation is done. Stop all Animations.
		if( elapsedTime > runtime ) {
			for( int i = 0; i < anims.size(); i++ ) {
				if( anims.get(i).isActive() ) {
					anims.get(i).elapsedTime = anims.get(i).runtime;
					anims.get(i).stop();
				}
			}
			running = false;
			this.stop();
		}

		while( active < anims.size() && elapsedTime >= active * lag ) {
			anims.get(active).start();
			active += 1;
		}

		for( int i = 0; i < active; i++ ) {
			double t = 0.0;
			if( overrideRuntime > 0 ) {
				t = (double) (elapsedTime - i*lag) / (double) overrideRuntime;
			} else {
				t = (double) (elapsedTime - i*lag) / (double) anims.get(i).getRuntime();
			}

			if( t >= 1.0 ) {
				anims.get(i).elapsedTime = anims.get(i).runtime;
				anims.get(i).stop();
			} else {
				double e = overrideEasing ?
					easing.applyAsDouble(t) :
					anims.get(i).easing.applyAsDouble(t);

				anims.get(i).animate(e);
			}
		}
	}

	@Override
	public void animate( double e ) {
	}

}
