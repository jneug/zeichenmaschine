package schule.ngb.zm.anim;

import schule.ngb.zm.Color;
import schule.ngb.zm.Constants;
import schule.ngb.zm.shapes.Circle;
import schule.ngb.zm.shapes.Ellipse;
import schule.ngb.zm.shapes.Rectangle;
import schule.ngb.zm.shapes.Shape;

import java.util.function.DoubleUnaryOperator;

public class MoveAnimation extends Animation<Shape> {

	private Shape object;

	private double oX, oY, tX, tY;

	public MoveAnimation( Shape object, double x, double y, int runtime, DoubleUnaryOperator easing ) {
		super(runtime, easing);

		this.object = object;
		oX = object.getX();
		oY = object.getY();
		tX = x;
		tY = y;
	}

	@Override
	public Shape getAnimationTarget() {
		return object;
	}

	@Override
	public void animate( double e ) {
		object.setX(Constants.interpolate(oX, tX, e));
		object.setY(Constants.interpolate(oY, tY, e));
	}

}
