package schule.ngb.zm.anim;

import schule.ngb.zm.util.Validator;

import java.util.function.DoubleUnaryOperator;

/**
 * Eine Wrapper Animation, um die Werte einer anderen Animation (Laufzeit, Easing) zu überschrieben,
 * ohne die Werte der Originalanimation zu verändern.
 *
 * @param <S> Art des Animierten Objektes.
 */
public class AnimationFacade<S> extends Animation<S> {

	private final Animation<S> anim;

	public AnimationFacade( Animation<S> anim, int runtime, DoubleUnaryOperator easing ) {
		super(runtime, easing);
		this.anim = Validator.requireNotNull(anim, "anim");
	}

	@Override
	public S getAnimationTarget() {
		return anim.getAnimationTarget();
	}

	@Override
	public void animate( double e ) {
		anim.animate(e);
	}

	@Override
	public void initialize() {
		anim.initialize();
	}

	@Override
	public void finish() {
		anim.finish();
	}

}
