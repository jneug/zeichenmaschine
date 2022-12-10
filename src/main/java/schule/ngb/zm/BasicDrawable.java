package schule.ngb.zm;

import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.Stroke;

/**
 * Basisimplementierung der {@link Strokeable} und {@link Fillable} APIs.
 *
 * Die Klasse bietet eine Grundlage zur Implementierung eigener Zeichenobjekte,
 * die eine Füllung und eine Konturlinie haben können.
 */
public abstract class BasicDrawable extends Constants implements Strokeable, Fillable {

	/**
	 * Ob das Objekt gezeichnet werden soll.
	 */
	protected boolean visible = true;

	/**
	 * Aktuelle Farbe der Konturlinie oder {@code null}, wenn das Objekt ohne
	 * Kontur dargestellt werden soll.
	 */
	protected schule.ngb.zm.Color strokeColor = DEFAULT_STROKECOLOR;

	/**
	 * Die Dicke der Konturlinie. Wird nicht kleiner als 0.
	 */
	protected double strokeWeight = DEFAULT_STROKEWEIGHT;

	/**
	 * Die Art der Konturlinie.
	 */
	protected Options.StrokeType strokeType = SOLID;

	/**
	 * Cache für den aktuellen {@code Stroke} der Kontur. Wird nach Änderung
	 * einer der Kontureigenschaften auf {@code null} gesetzt und beim nächsten
	 * Zeichnen neu erstellt.
	 */
	protected Stroke stroke = null;

	/**
	 * Die aktuelle Füllfarbe der Form oder {@code null}, wenn das Objekt nicht
	 * gefüllt werden soll.
	 */
	protected Color fillColor = DEFAULT_FILLCOLOR;

	/**
	 * Der aktuelle Farbverlauf des Objektes oder {@code null}, wenn es keinen
	 * Farbverlauf besitzt.
	 */
	protected MultipleGradientPaint fill = null;


	// Implementierung Drawable Interface

	/**
	 * Ob das Objekt angezeigt bzw. gezeichnet werden soll.
	 *
	 * @return {@code true}, wenn das Objekt angezeigt werden soll,
	 *    {@code false} sonst.
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Versteckt das Objekt.
	 */
	public void hide() {
		visible = false;
	}

	/**
	 * Zeigt das Objekt an.
	 */
	public void show() {
		visible = true;
	}

	/**
	 * Versteckt da Objekt, wenn es derzeit angezeigt wird und zeigt es
	 * andernfalls an.
	 */
	public void toggle() {
		visible = !visible;
	}

	public abstract void draw( Graphics2D graphics );


	// Implementierung Fillable Interface

	@Override
	public void setFill( Paint fill ) {
		if( fill == null ) {
			this.fill = null;
		} else if( fill instanceof Color ) {
			this.fillColor = ((Color) fill);
			this.fill = null;
		} else if( fill instanceof MultipleGradientPaint ) {
			this.fillColor = null;
			this.fill = (MultipleGradientPaint) fill;
		}
	}

	@Override
	public Paint getFill() {
		if( fill != null ) {
			return fill;
		} else if( fillColor != null && fillColor.getAlpha() > 0 ) {
			return fillColor;
		} else {
			return null;
		}
	}

	@Override
	public boolean hasFillColor() {
		return fillColor != null;
	}

	@Override
	public boolean hasGradient() {
		return fill != null;
	}

	@Override
	public Color getFillColor() {
		return fillColor;
	}

	@Override
	public void setFillColor( Color color ) {
		fillColor = color;
		fill = null;
	}

	@Override
	public MultipleGradientPaint getGradient() {
		return fill;
	}


	// Implementierung Strokeable Interface

	@Override
	public void setStroke( Stroke stroke ) {
		this.stroke = stroke;
	}

	@Override
	public Stroke getStroke() {
		if( stroke == null ) {
			stroke = Strokeable.createStroke(strokeType, strokeWeight);
		}
		return stroke;
	}

	@Override
	public Color getStrokeColor() {
		return strokeColor;
	}

	@Override
	public void setStrokeColor( Color color ) {
		strokeColor = color;
	}

	@Override
	public double getStrokeWeight() {
		return strokeWeight;
	}

	@Override
	public void setStrokeWeight( double weight ) {
		strokeWeight = weight;
		this.stroke = null;
	}

	@Override
	public Options.StrokeType getStrokeType() {
		return strokeType;
	}

	@Override
	public void setStrokeType( Options.StrokeType type ) {
		strokeType = type;
		this.stroke = null;
	}

}
