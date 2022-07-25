package schule.ngb.zm.anim;


import schule.ngb.zm.Constants;
import schule.ngb.zm.Vector;
import schule.ngb.zm.shapes.Shape;

import java.util.function.DoubleUnaryOperator;

public class CircleAnimation extends Animation<Shape> {

	private Shape object;

	private double centerx, centery, radius, startangle;

	public CircleAnimation( Shape target, double cx, double cy, int runtime, DoubleUnaryOperator easing ) {
		super(runtime, easing);
		object = target;
		centerx = cx;
		centery = cy;
		Vector vec = new Vector(target.getX(), target.getY()).sub(cx, cy);
		startangle = vec.heading();
		radius = vec.length();
	}

	@Override
	public Shape getAnimationTarget() {
		return object;
	}

	@Override
	public void animate( double e ) {
		double angle = startangle + Constants.radians(Constants.interpolate(0, 360, e));
		double x = centerx + radius * Constants.cos(angle);
		double y = centery + radius * Constants.sin(angle);
		object.moveTo(x, y);
	}

}
