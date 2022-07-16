package schule.ngb.zm.anim;

import schule.ngb.zm.Color;
import schule.ngb.zm.Constants;
import schule.ngb.zm.shapes.Shape;

import java.util.function.DoubleUnaryOperator;

public class FillAnimation extends Animation<Shape> {

	private Shape object;

	private Color oFill, tFill;

	public FillAnimation( Shape object, Color newFill, int runtime, DoubleUnaryOperator easing ) {
		super(runtime, easing);

		this.object = object;
		oFill = object.getFillColor();
		tFill = newFill;
	}

	@Override
	public Shape getAnimationTarget() {
		return object;
	}

	@Override
	public void interpolate( double e ) {
		object.setFillColor(Color.interpolate(oFill, tFill, e));
	}

}
