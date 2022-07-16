package schule.ngb.zm.anim;

import schule.ngb.zm.Color;
import schule.ngb.zm.Constants;
import schule.ngb.zm.shapes.Circle;
import schule.ngb.zm.shapes.Ellipse;
import schule.ngb.zm.shapes.Rectangle;
import schule.ngb.zm.shapes.Shape;

import java.util.function.DoubleUnaryOperator;

public class MorphAnimation extends Animation<Shape> {

	private Shape object, original, target;

	public MorphAnimation( Shape object, Shape target, int runtime, DoubleUnaryOperator easing ) {
		super(runtime, easing);

		this.original = object.copy();
		this.object = object;
		this.target = target;
	}

	@Override
	public Shape getAnimationTarget() {
		return object;
	}

	@Override
	public void interpolate( double e ) {
		object.setX(Constants.interpolate(original.getX(), target.getX(), e));
		object.setY(Constants.interpolate(original.getY(), target.getY(), e));
		object.setFillColor(Color.interpolate(original.getFillColor(), target.getFillColor(), e));
		object.setStrokeColor(Color.interpolate(original.getStrokeColor(), target.getStrokeColor(), e));
		object.rotateTo(Constants.interpolate(original.getRotation(), target.getRotation(), e));
		object.scale(Constants.interpolate(original.getScale(), target.getScale(), e));
		object.setStrokeWeight(Constants.interpolate(original.getStrokeWeight(), target.getStrokeWeight(), e));

		if( object instanceof Rectangle ) {
			Rectangle r = (Rectangle)object;
			r.setWidth(Constants.interpolate(original.getWidth(), target.getWidth(), e));
			r.setHeight(Constants.interpolate(original.getHeight(), target.getHeight(), e));
		} else if( object instanceof Circle ) {
			Circle r = (Circle)object;
			r.setRadius(Constants.interpolate(original.getWidth()*.5, target.getWidth()*.5, e));
		} else if( object instanceof Ellipse ) {
			Ellipse r = (Ellipse)object;
			r.setWidth(Constants.interpolate(original.getWidth(), target.getWidth(), e));
			r.setHeight(Constants.interpolate(original.getHeight(), target.getHeight(), e));
		}
	}

}
