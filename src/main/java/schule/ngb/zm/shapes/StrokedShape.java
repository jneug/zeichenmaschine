package schule.ngb.zm.shapes;

import schule.ngb.zm.Color;
import schule.ngb.zm.Constants;
import schule.ngb.zm.Drawable;
import schule.ngb.zm.Options;
import schule.ngb.zm.util.Noise;

import java.awt.*;
import java.awt.Shape;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.Arrays;


public abstract class StrokedShape extends Constants implements Drawable {

	protected Color strokeColor = STD_STROKECOLOR;

	protected double strokeWeight = STD_STROKEWEIGHT;

	protected Options.StrokeType strokeType = SOLID;

	protected Stroke stroke = null;

	public Color getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor( Color color ) {
		this.strokeColor = color;
	}

	public void setStrokeColor( Color color, int alpha ) {
		setStrokeColor(new Color(color, alpha));
	}

	public void setStrokeColor( int gray ) {
		setStrokeColor(gray, gray, gray, 255);
	}

	public void noStroke() {
		setStrokeColor(null);
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

	public double getStrokeWeight() {
		return strokeWeight;
	}

	public void setStrokeWeight( double weight ) {
		this.strokeWeight = weight;
		this.stroke = null;
	}

	public Options.StrokeType getStrokeType() {
		return strokeType;
	}

	/**
	 * Setzt den Typ der Kontur. Erlaubte Werte sind {@link #DASHED},
	 * {@link #DOTTED} und {@link #SOLID}.
	 *
	 * @param type
	 */
	public void setStrokeType( Options.StrokeType type ) {
		this.strokeType = type;
		this.stroke = null;
	}

	@Override
	public abstract void draw( Graphics2D graphics );

	/**
	 * Erstellt ein {@link Stroke} Objekt für den Konturtyp.
	 *
	 * @return
	 */
	protected Stroke createStroke() {
		// TODO: Used global cached Stroke Objects?
		if( stroke == null ) {
			switch( strokeType ) {
				case DOTTED:
					stroke = new BasicStroke(
						(float) strokeWeight,
						BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_ROUND,
						10.0f, new float[]{1.0f, 5.0f}, 0.0f);
					break;
				case DASHED:
					stroke = new BasicStroke(
						(float) strokeWeight,
						BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_ROUND,
						10.0f, new float[]{5.0f}, 0.0f);
					break;
				case SOLID:
				default:
					stroke = new BasicStroke(
						(float) strokeWeight,
						BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_ROUND);
					break;
			}
		}
		return stroke;
	}

	public void resetStroke() {
		setStrokeColor(STD_STROKECOLOR);
		setStrokeWeight(STD_STROKEWEIGHT);
		setStrokeType(SOLID);
	}

}
