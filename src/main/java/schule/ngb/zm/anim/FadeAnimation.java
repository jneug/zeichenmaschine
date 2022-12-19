package schule.ngb.zm.anim;

import schule.ngb.zm.Color;
import schule.ngb.zm.Constants;
import schule.ngb.zm.shapes.Shape;

import java.util.function.DoubleUnaryOperator;

@SuppressWarnings( "unused" )
public class FadeAnimation extends Animation<Shape> {

	public static final int FADE_IN = 255;

	public static final int FADE_OUT = 0;

	private Shape object;

	private Color fill, stroke;

	private int fillAlpha, strokeAlpha, tAlpha;

	public FadeAnimation( Shape object, int alpha, int runtime, DoubleUnaryOperator easing ) {
		super(runtime, easing);

		this.object = object;
		fill = object.getFillColor();
		fillAlpha = fill.getAlpha();
		stroke = object.getStrokeColor();
		strokeAlpha = stroke.getAlpha();
		tAlpha = alpha;
	}

	@Override
	public Shape getAnimationTarget() {
		return object;
	}

	@Override
	public void animate( double e ) {
		object.setFillColor(new Color(fill, (int) Constants.interpolate(fillAlpha, tAlpha, e)));
		object.setStrokeColor(new Color(stroke, (int) Constants.interpolate(strokeAlpha, tAlpha, e)));
	}

}
