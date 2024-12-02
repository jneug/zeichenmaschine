package schule.ngb.zm.anim;

import schule.ngb.zm.Constants;
import schule.ngb.zm.shapes.Shape;

import java.util.function.DoubleUnaryOperator;

public class MoveAnimation extends Animation<Shape> {

	private final Shape object;

	private final double targetX, targetY;

	private double originX, originY;


	public MoveAnimation( Shape target, double targetX, double targetY, int runtime, DoubleUnaryOperator easing ) {
		super(runtime, easing);

		this.object = target;
		this.targetX = targetX;
		this.targetY = targetY;
	}

	@Override
	public void initialize() {
		originX = object.getX();
		originY = object.getY();
	}

	@Override
	public Shape getAnimationTarget() {
		return object;
	}

	@Override
	public void animate( double e ) {
		object.setX(Constants.interpolate(originX, targetX, e));
		object.setY(Constants.interpolate(originY, targetY, e));
	}

}
