package schule.ngb.zm.anim;

import schule.ngb.zm.Color;
import schule.ngb.zm.shapes.Shape;

import java.util.function.DoubleUnaryOperator;

public class FillAnimation extends Animation<Shape> {

	private final Shape object;

	private Color originFill;

	private final Color targetFill;

	public FillAnimation( Shape target, Color newFill, int runtime, DoubleUnaryOperator easing ) {
		super(runtime, easing);

		this.object = target;
		targetFill = newFill;
	}

	@Override
	public void initialize() {
		originFill = object.getFillColor();
	}

	@Override
	public Shape getAnimationTarget() {
		return object;
	}

	@Override
	public void animate( double e ) {
		object.setFillColor(Color.interpolate(originFill, targetFill, e));
	}

}
