package schule.ngb.zm.anim;

import schule.ngb.zm.Constants;
import schule.ngb.zm.Options;
import schule.ngb.zm.shapes.Shape;

import java.util.function.DoubleUnaryOperator;

public class WaveAnimation extends Animation<Shape> {

	private Shape object;

	private double strength, sinOffset, previousDelta = 0.0;

	private Options.Direction dir;

	public WaveAnimation( Shape target, double strength, Options.Direction dir, double sinOffset, int runtime, DoubleUnaryOperator easing ) {
		super(runtime, easing);
		this.object = target;
		this.dir = dir;
		this.strength = strength;
		this.sinOffset = sinOffset;
	}

	@Override
	public Shape getAnimationTarget() {
		return object;
	}

	@Override
	public void animate( double e ) {
		double delta = this.strength * Constants.sin(Constants.interpolate(0.0, Constants.TWO_PI, e) + sinOffset);
		object.move((delta - previousDelta) * dir.x, (delta - previousDelta) * dir.y);
		previousDelta = delta;
	}

}
