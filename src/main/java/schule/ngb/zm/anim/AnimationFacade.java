package schule.ngb.zm.anim;

import schule.ngb.zm.util.Validator;

import java.util.function.DoubleUnaryOperator;

public class AnimationFacade<S> extends Animation<S> {

	private Animation<S> anim;

	public AnimationFacade( Animation<S> anim, int runtime, DoubleUnaryOperator easing ) {
		super(runtime, easing);
		this.anim = Validator.requireNotNull(anim);
	}

	@Override
	public S getAnimationTarget() {
		return anim.getAnimationTarget();
	}

	@Override
	public void interpolate( double e ) {
		anim.interpolate(e);
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
