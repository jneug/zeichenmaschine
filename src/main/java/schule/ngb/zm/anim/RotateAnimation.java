package schule.ngb.zm.anim;

import schule.ngb.zm.Constants;
import schule.ngb.zm.shapes.Shape;

import java.util.function.DoubleUnaryOperator;

public class RotateAnimation extends Animation<Shape> {

	private Shape object;

	private double oA, tA;

	public RotateAnimation( Shape object, double angle, int runtime, DoubleUnaryOperator easing ) {
		super(runtime, easing);

		this.object = object;
		oA = object.getRotation();
		tA = angle;
	}

	@Override
	public Shape getAnimationTarget() {
		return object;
	}

	@Override
	public void animate( double e ) {
		object.rotateTo(Constants.interpolate(oA, tA, e));
	}

}
