package schule.ngb.zm.anim;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

/**
 * Führt eine Liste von Animationen nacheinander aus. Jede Animation startet direkt nachdem die
 * davor geendet ist. Optional kann zwischen dem Ende einer und dem Start der nächsten Animation
 * ein
 * <var>lag</var> eingefügt werden.
 *
 * @param <T> Die Art des animierten Objektes.
 */
@SuppressWarnings( "unused" )
public class AnimationSequence<T> extends Animation<T> {

	private final List<Animation<T>> anims;

	private final int lag;

	private int currentAnimationIndex = -1, currentStart = -1, nextStart = -1;

	@SafeVarargs
	public AnimationSequence( Animation<T>... anims ) {
		this(0, Arrays.asList(anims));
	}

	public AnimationSequence( Collection<Animation<T>> anims ) {
		this(0, anims);
	}

	public AnimationSequence( int lag, Collection<Animation<T>> anims ) {
		super(Easing::linear);

		this.anims = List.copyOf(anims);
		this.lag = lag;

		this.runtime = (anims.size() - 1) * lag + anims.stream().mapToInt(Animation::getRuntime).sum();
	}

	@Override
	public T getAnimationTarget() {
		for( Animation<T> anim : anims ) {
			if( anim.isActive() ) {
				return anim.getAnimationTarget();
			}
		}
		if( this.finished ) {
			return anims.get(anims.size() - 1).getAnimationTarget();
		} else {
			return anims.get(0).getAnimationTarget();
		}
	}

	@Override
	public DoubleUnaryOperator getEasing() {
		for( Animation<T> anim : anims ) {
			if( anim.isActive() ) {
				return anim.getEasing();
			}
		}
		if( this.finished ) {
			return anims.get(anims.size() - 1).getEasing();
		} else {
			return anims.get(0).getEasing();
		}
	}

//	@Override
//	public void update( double delta ) {
//		elapsedTime += (int) (delta * 1000);
//
//		// Animation is done. Stop all Animations.
//		if( elapsedTime > runtime ) {
//			for( int i = 0; i < anims.size(); i++ ) {
//				if( anims.get(i).isActive() ) {
//					anims.get(i).elapsedTime = anims.get(i).runtime;
//					anims.get(i).stop();
//				}
//			}
//			elapsedTime = runtime;
//			running = false;
//			this.stop();
//		}
//
//		Animation<T> curAnim = null;
//		if( elapsedTime > nextStart ) {
//			currentAnimation += 1;
//			curAnim = anims.get(currentAnimation);
//			currentStart = nextStart;
//			nextStart += lag + curAnim.getRuntime();
//			curAnim.start();
//		} else {
//			curAnim = anims.get(currentAnimation);
//		}
//
//		// Calculate delta for current animation
//		double t = (double) (elapsedTime - currentStart) / (double) curAnim.getRuntime();
//		if( t >= 1.0 ) {
//			curAnim.elapsedTime = curAnim.runtime;
//			curAnim.stop();
//		} else {
//			curAnim.animate(curAnim.easing.applyAsDouble(t));
//		}
//	}


	@Override
	public void finish() {
		for( Animation<T> anim : anims ) {
			if( anim.isActive() ) {
				anim.elapsedTime = anim.runtime;
				anim.stop();
			}
		}
	}

	@Override
	public void animate( double e ) {
		Animation<T> curAnim = null;
		if( running && elapsedTime > nextStart ) {
			currentAnimationIndex += 1;
			curAnim = anims.get(currentAnimationIndex);
			currentStart = nextStart;
			nextStart += lag + curAnim.getRuntime();
			curAnim.start();
		} else {
			curAnim = anims.get(currentAnimationIndex);
		}

		// Calculate delta for current animation
		double t = (double) (elapsedTime - currentStart) / (double) curAnim.getRuntime();
		if( t >= 1.0 ) {
			curAnim.elapsedTime = curAnim.runtime;
			curAnim.stop();
		} else {
			curAnim.elapsedTime = (elapsedTime - currentStart);
			curAnim.animate(curAnim.easing.applyAsDouble(t));
		}
	}

}
