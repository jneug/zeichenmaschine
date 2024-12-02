package schule.ngb.zm.anim;


import schule.ngb.zm.Constants;
import schule.ngb.zm.Vector;
import schule.ngb.zm.shapes.Shape;

import java.util.function.DoubleUnaryOperator;

/**
 * Animates the {@code target} in a circular motion centered at (<var>cx</var>, <var>cy</var>).
 */
public class CircleAnimation extends Animation<Shape> {

	private final Shape target;

	private final double centerX, centerY, rotateTo;

	private double rotationRadius, startAngle;

	private final boolean rotateRight;

	public CircleAnimation( Shape target, double cx, double cy ) {
		this(target, cx, cy, 360, true, DEFAULT_ANIM_RUNTIME, DEFAULT_EASING);
	}

	public CircleAnimation( Shape target, double cx, double cy, double rotateTo ) {
		this(target, cx, cy, rotateTo, true, DEFAULT_ANIM_RUNTIME, DEFAULT_EASING);
	}

	public CircleAnimation( Shape target, double cx, double cy, boolean rotateRight ) {
		this(target, cx, cy, 360, rotateRight, DEFAULT_ANIM_RUNTIME, DEFAULT_EASING);
	}

	public CircleAnimation( Shape target, double cx, double cy, double rotateTo, boolean rotateRight ) {
		this(target, cx, cy, rotateTo, rotateRight, DEFAULT_ANIM_RUNTIME, DEFAULT_EASING);
	}

	public CircleAnimation( Shape target, double cx, double cy, int runtime ) {
		this(target, cx, cy, 360, true, runtime, DEFAULT_EASING);
	}

	public CircleAnimation( Shape target, double cx, double cy, boolean rotateRight, int runtime ) {
		this(target, cx, cy, 360, rotateRight, runtime, DEFAULT_EASING);
	}

	public CircleAnimation( Shape target, double cx, double cy, DoubleUnaryOperator easing ) {
		this(target, cx, cy, 360, true, DEFAULT_ANIM_RUNTIME, easing);
	}

	public CircleAnimation( Shape target, double cx, double cy, boolean rotateRight, DoubleUnaryOperator easing ) {
		this(target, cx, cy, 360, rotateRight, DEFAULT_ANIM_RUNTIME, easing);
	}

	public CircleAnimation( Shape target, double cx, double cy, double rotateTo, int runtime, DoubleUnaryOperator easing ) {
		this(target, cx, cy, rotateTo, true, runtime, easing);
	}

	public CircleAnimation( Shape target, double cx, double cy, double rotateTo, boolean rotateRight, int runtime, DoubleUnaryOperator easing ) {
		super(runtime, easing);
		this.target = target;
		this.centerX = cx;
		this.centerY = cy;
		this.rotateTo = Constants.radians(Constants.limit(rotateTo, 0, 360));
		this.rotateRight = rotateRight;
	}

	@Override
	public void initialize() {
		Vector vec = new Vector(target.getX(), target.getY()).sub(centerX, centerY);
		startAngle = vec.heading();
		rotationRadius = vec.length();
	}

	@Override
	public Shape getAnimationTarget() {
		return target;
	}

	@Override
	public void animate( double e ) {
		double angle = startAngle;
		if( rotateRight ) {
			angle += Constants.interpolate(0, rotateTo, e);
		} else {
			angle -= Constants.interpolate(0, rotateTo, e);
		}
		double x = centerX + rotationRadius * Constants.cos(angle);
		double y = centerY + rotationRadius * Constants.sin(angle);
		target.moveTo(x, y);
	}

}
