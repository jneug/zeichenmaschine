package schule.ngb.zm.shapes;

import schule.ngb.zm.Color;
import schule.ngb.zm.Constants;
import schule.ngb.zm.Drawable;
import schule.ngb.zm.Options;

import java.awt.*;


public abstract class StrokedShape extends Constants implements Drawable {

	protected Color strokeColor = STD_STROKECOLOR;

	protected double strokeWeight = STD_STROKEWEIGHT;

	protected Options.StrokeType strokeType = SOLID;

	public Color getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor( Color color ) {
		this.strokeColor = color;
	}

	public void setStrokeColor( Color color , int alpha ) {
		this.strokeColor = new Color(color, alpha);
	}

	public void setStrokeColor( int gray ) {
		setStrokeColor(gray, gray, gray, 255);
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

	public double getStrokeWeight() {
		return strokeWeight;
	}

	public void setStrokeWeight( double weight ) {
		this.strokeWeight = weight;
	}

	public Options.StrokeType getStrokeType() {
		return strokeType;
	}

	/**
	 * Setzt den Typ der Kontur. Erlaubte Werte sind {@link #DASHED},
	 * {@link #DOTTED} und {@link #SOLID}.
	 * @param type
	 */
	public void setStrokeType( Options.StrokeType type ) {
		this.strokeType = DASHED;
	}

	@Override
	public abstract void draw( Graphics2D graphics );

	/**
	 * Erstellt ein {@link Stroke} Objekt für den Konturtyp.
	 * @return
	 */
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
			case SOLID:
			default:
				return new BasicStroke(
					(float) strokeWeight,
					BasicStroke.CAP_ROUND,
					BasicStroke.JOIN_ROUND);
		}
	}

	public void resetStroke() {
		setStrokeColor(STD_STROKECOLOR);
		setStrokeWeight(STD_STROKEWEIGHT);
		setStrokeType(SOLID);
	}

}
