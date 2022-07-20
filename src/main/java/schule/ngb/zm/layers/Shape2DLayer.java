package schule.ngb.zm.layers;

import schule.ngb.zm.Color;
import schule.ngb.zm.Layer;
import schule.ngb.zm.Options;

import java.awt.*;
import java.util.LinkedList;

public final class Shape2DLayer extends Layer {

	protected schule.ngb.zm.Color strokeColor = DEFAULT_STROKECOLOR;

	protected schule.ngb.zm.Color fillColor = DEFAULT_FILLCOLOR;

	protected double strokeWeight = DEFAULT_STROKEWEIGHT;

	protected Options.StrokeType strokeType = SOLID;

	private LinkedList<java.awt.Shape> shapes;

	private boolean instantDraw = false;

	public Shape2DLayer() {
		super();
		shapes = new LinkedList<java.awt.Shape>();
	}

	public Shape2DLayer( boolean instantDraw ) {
		super();
		shapes = new LinkedList<java.awt.Shape>();
		this.instantDraw = instantDraw;
	}

	public Shape2DLayer( int width, int height ) {
		super(width, height);
		shapes = new LinkedList<java.awt.Shape>();
	}

	public Shape2DLayer( int width, int height, boolean instantDraw ) {
		super(width, height);
		shapes = new LinkedList<java.awt.Shape>();
		this.instantDraw = instantDraw;
	}

	public schule.ngb.zm.Color getFillColor() {
		return fillColor;
	}

	public void setFillColor( int gray ) {
		setFillColor(gray, gray, gray, 255);
	}

	public void setFillColor( schule.ngb.zm.Color pColor ) {
		fillColor = pColor;
		drawing.setColor(pColor.getJavaColor());
	}

	public void noFill() {
		fillColor = null;
	}

	public void setFillColor( int gray, int alpha ) {
		setFillColor(gray, gray, gray, alpha);
	}

	public void setFillColor( int red, int green, int blue ) {
		setFillColor(red, green, blue, 255);
	}

	public void setFillColor( int red, int green, int blue, int alpha ) {
		setFillColor(new schule.ngb.zm.Color(red, green, blue, alpha));
	}

	public schule.ngb.zm.Color getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor( int gray ) {
		setStrokeColor(gray, gray, gray, 255);
	}

	public void setStrokeColor( schule.ngb.zm.Color pColor ) {
		strokeColor = pColor;
		drawing.setColor(pColor.getJavaColor());
	}

	public void noStroke() {
		strokeColor = null;
	}

	public void setStrokeColor( int gray, int alpha ) {
		setStrokeColor(gray, gray, gray, alpha);
	}

	public void setStrokeColor( int red, int green, int blue ) {
		setStrokeColor(red, green, blue, 255);
	}

	public void setStrokeColor( int red, int green, int blue, int alpha ) {
		setStrokeColor(new Color(red, green, blue, alpha));
	}

	public void setStrokeWeight( double pWeight ) {
		strokeWeight = pWeight;
		drawing.setStroke(createStroke());
	}

	protected Stroke createStroke() {
		switch( strokeType ) {
			case DOTTED:
				return new BasicStroke(
					(float) strokeWeight,
					BasicStroke.CAP_ROUND,
					BasicStroke.JOIN_ROUND,
					10.0f, new float[]{1.0f, 5.0f}, 0.0f);
			case DASHED:
				return new BasicStroke(
					(float) strokeWeight,
					BasicStroke.CAP_ROUND,
					BasicStroke.JOIN_ROUND,
					10.0f, new float[]{5.0f}, 0.0f);
			default:
				return new BasicStroke(
					(float) strokeWeight,
					BasicStroke.CAP_ROUND,
					BasicStroke.JOIN_ROUND);
		}
	}

	public Options.StrokeType getStrokeType() {
		return strokeType;
	}

	public void setStrokeType( Options.StrokeType strokeType ) {
		this.strokeType = strokeType;
	}

	public java.util.List<java.awt.Shape> getShapes() {
		return shapes;
	}

	public void add( java.awt.Shape s ) {
		shapes.add(s);

		if( instantDraw ) {
			drawing.setColor(fillColor.getJavaColor());
			drawing.fill(s);

			drawing.setColor(strokeColor.getJavaColor());
			drawing.draw(s);
		}
	}

	@Override
	public void draw( Graphics2D pGraphics ) {
		if( !instantDraw ) {
			for( Shape shape : shapes ) {
				drawing.setColor(fillColor.getJavaColor());
				drawing.fill(shape);

				drawing.setColor(strokeColor.getJavaColor());
				drawing.draw(shape);
			}
		}

		super.draw(pGraphics);
	}

}
