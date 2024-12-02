package schule.ngb.zm.anim;

import schule.ngb.zm.Color;
import schule.ngb.zm.Constants;
import schule.ngb.zm.shapes.Shape;

import java.util.function.DoubleUnaryOperator;

@SuppressWarnings( "unused" )
public class FadeAnimation extends Animation<Shape> {

	public static final int FADE_IN = 255;

	public static final int FADE_OUT = 0;

	private final Shape target;

	private final int targetAlpha;

	private Color fill, stroke;

	private int fillAlpha, strokeAlpha;

	public FadeAnimation( Shape target, int targetAlpha ) {
		this(target, targetAlpha, DEFAULT_ANIM_RUNTIME, DEFAULT_EASING);
	}

	public FadeAnimation( Shape target, int targetAlpha, int runtime ) {
		this(target, targetAlpha, runtime, DEFAULT_EASING);
	}

	public FadeAnimation( Shape target, int runtime, DoubleUnaryOperator easing ) {
		this(target, 0, runtime, easing);
	}

	public FadeAnimation( Shape target, int targetAlpha, int runtime, DoubleUnaryOperator easing ) {
		super(runtime, easing);

		this.target = target;
		this.targetAlpha = targetAlpha;
	}

	@Override
	public void initialize() {
		fill = target.getFillColor();
		fillAlpha = fill.getAlpha();
		stroke = target.getStrokeColor();
		strokeAlpha = stroke.getAlpha();

	}

	@Override
	public Shape getAnimationTarget() {
		return target;
	}

	@Override
	public void animate( double e ) {
		target.setFillColor(fill, (int) Constants.interpolate(fillAlpha, targetAlpha, e));
		target.setStrokeColor(stroke, (int) Constants.interpolate(strokeAlpha, targetAlpha, e));
	}

}
