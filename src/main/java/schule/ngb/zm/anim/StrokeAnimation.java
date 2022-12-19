package schule.ngb.zm.anim;

import schule.ngb.zm.Color;
import schule.ngb.zm.shapes.Shape;

import java.util.function.DoubleUnaryOperator;

public class StrokeAnimation extends Animation<Shape> {

	private Shape object;

	private Color oFill, tFill;

	public StrokeAnimation( Shape object, Color newStroke, int runtime, DoubleUnaryOperator easing ) {
		super(runtime, easing);

		this.object = object;
		oFill = object.getFillColor();
		tFill = newStroke;
	}

	@Override
	public Shape getAnimationTarget() {
		return object;
	}

	@Override
	public void animate( double e ) {
		object.setStrokeColor(Color.interpolate(oFill, tFill, e));
	}

}
