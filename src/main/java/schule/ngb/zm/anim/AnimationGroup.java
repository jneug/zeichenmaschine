package schule.ngb.zm.anim;

import schule.ngb.zm.shapes.Shape;

import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;

public class AnimationGroup extends Animation<Shape> {

	Animation<? extends Shape>[] anims;

	private boolean overrideRuntime = false;


	public AnimationGroup( DoubleUnaryOperator easing, Animation<? extends Shape>... anims ) {
		super(easing);
		this.anims = anims;

		int maxRuntime = Arrays.stream(this.anims).mapToInt((a) -> a.getRuntime()).reduce(0, Integer::max);
		setRuntime(maxRuntime);
	}

	public AnimationGroup( int runtime, DoubleUnaryOperator easing, Animation<? extends Shape>... anims ) {
		super(runtime, easing);
		this.anims = anims;
		overrideRuntime = true;
	}

	@Override
	public Shape getAnimationTarget() {
		return null;
	}

	@Override
	public void update( double delta ) {
		if( overrideRuntime ) {
			synchronized( anims ) {
				for( Animation<? extends Shape> anim: anims ) {
					if( anim.isActive() ) {
						anim.update(delta);
					}
				}
			}
		} else {
			super.update(delta);
		}
	}

	@Override
	public void interpolate( double e ) {
		synchronized( anims ) {
			for( Animation<? extends Shape> anim: anims ) {
				anim.interpolate(e);
			}
		}
	}

	@Override
	public void initialize() {
		synchronized( anims ) {
			for( Animation<? extends Shape> anim: anims ) {
				anim.initialize();
			}
		}
	}

	@Override
	public void finish() {
		synchronized( anims ) {
			for( Animation<? extends Shape> anim: anims ) {
				anim.finish();
			}
		}
	}

}
