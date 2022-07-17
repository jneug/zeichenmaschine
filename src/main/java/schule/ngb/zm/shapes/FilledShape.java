package schule.ngb.zm.shapes;

import schule.ngb.zm.Color;

import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

/**
 * Basisklasse für Formen, die eine Füllung besitzen können.
 * <p>
 * Formen mit einer Füllung können auch immer eine Konturlinie besitzen.
 */
public abstract class FilledShape extends StrokedShape {

	/**
	 * Die aktuelle Füllfarbe der Form oder {@code null}, wenn die Form nicht
	 * gefüllt werden soll.
	 */
	protected Color fillColor = DEFAULT_FILLCOLOR;

	/**
	 * Der aktuelle Farbverlauf der Form oder {@code null}, wenn die Form keinen
	 * Farbverlauf besitzt.
	 */
	protected Paint fill = null;

	/**
	 * Gibt die aktuelle Füllfarbe der Form zurück.
	 *
	 * @return Die aktuelle Füllfarbe oder {@code null}.
	 */
	public Color getFillColor() {
		return fillColor;
	}

	/**
	 * Setzt die Füllfarbe auf die angegebene Farbe.
	 *
	 * @param color Die neue Füllfarbe oder {@code null}.
	 * @see Color#Color(Color)
	 */
	public void setFillColor( Color color ) {
		fillColor = color;
	}

	/**
	 * Setzt die Füllfarbe auf die angegebene Farbe und setzt die Transparenz
	 * auf den angegebenen Wert. 0 is komplett durchsichtig und 255 komplett
	 * deckend.
	 *
	 * @param color Die neue Füllfarbe oder {@null}.
	 * @param alpha Ein Transparenzwert zwischen 0 und 255.
	 * @see Color#Color(Color, int)
	 */
	public void setFillColor( Color color, int alpha ) {
		setFillColor(new Color(color, alpha));
	}

	/**
	 * Setzt die Füllfarbe auf einen Grauwert mit der angegebenen Intensität. 0
	 * entspricht schwarz, 255 entspricht weiß.
	 *
	 * @param gray Ein Grauwert zwischen 0 und 255.
	 * @see Color#Color(int)
	 */
	public void setFillColor( int gray ) {
		setFillColor(gray, gray, gray, 255);
	}

	/**
	 * Setzt die Füllfarbe auf einen Grauwert mit der angegebenen Intensität und
	 * dem angegebenen Transparenzwert. Der Grauwert 0 entspricht schwarz, 255
	 * entspricht weiß.
	 *
	 * @param gray Ein Grauwert zwischen 0 und 255.
	 * @param alpha Ein Transparenzwert zwischen 0 und 255.
	 * @see Color#Color(int, int)
	 */
	public void setFillColor( int gray, int alpha ) {
		setFillColor(gray, gray, gray, alpha);
	}

	/**
	 * Setzt die Füllfarbe auf die Farbe mit den angegebenen Rot-, Grün- und
	 * Blauanteilen.
	 *
	 * @param red Der Rotanteil der Farbe zwischen 0 und 255.
	 * @param green Der Grünanteil der Farbe zwischen 0 und 255.
	 * @param blue Der Blauanteil der Farbe zwischen 0 und 255.
	 * @see Color#Color(int, int, int)
	 * @see <a
	 * 	href="https://de.wikipedia.org/wiki/RGB-Farbraum">https://de.wikipedia.org/wiki/RGB-Farbraum</a>
	 */
	public void setFillColor( int red, int green, int blue ) {
		setFillColor(red, green, blue, 255);
	}

	/**
	 * Setzt die Füllfarbe auf die Farbe mit den angegebenen Rot-, Grün- und
	 * Blauanteilen und dem angegebenen Transparenzwert.
	 *
	 * @param red Der Rotanteil der Farbe zwischen 0 und 255.
	 * @param green Der Grünanteil der Farbe zwischen 0 und 255.
	 * @param blue Der Blauanteil der Farbe zwischen 0 und 255.
	 * @param alpha Ein Transparenzwert zwischen 0 und 25
	 * @see Color#Color(int, int, int, int)
	 * @see <a
	 * 	href="https://de.wikipedia.org/wiki/RGB-Farbraum">https://de.wikipedia.org/wiki/RGB-Farbraum</a>
	 */
	public void setFillColor( int red, int green, int blue, int alpha ) {
		setFillColor(new Color(red, green, blue, alpha));
	}

	/**
	 * Entfernt die Füllung der Form.
	 */
	public void noFill() {
		setFillColor(null);
		noGradient();
	}

	/**
	 * Setzt die Füllfarbe auf den Standardwert zurück.
	 *
	 * @see schule.ngb.zm.Constants#DEFAULT_FILLCOLOR
	 */
	public void resetFill() {
		setFillColor(DEFAULT_FILLCOLOR);
		noGradient();
	}

	/**
	 * Setzt die Füllung auf einen linearen Farbverlauf, der am Punkt
	 * ({@code fromX}, {@code fromY}) mit der Farbe {@code from} startet und am
	 * Punkt (({@code toX}, {@code toY}) mit der Farbe {@code to} endet.
	 *
	 * @param fromX
	 * @param fromY
	 * @param from
	 * @param toX
	 * @param toY
	 * @param to
	 */
	public void setGradient( double fromX, double fromY, Color from, double toX, double toY, Color to ) {
		setFillColor(from);
		fill = new GradientPaint(
			(float) fromX, (float) fromY, from.getJavaColor(),
			(float) toX, (float) toY, to.getJavaColor()
		);
	}

	/**
	 * Setzt die Füllung auf einen kreisförmigen (radialen) Farbverlauf, mit dem
	 * Zentrum im Punkt ({@code centerX}, {@code centerY}) und dem angegebenen
	 * Radius. Der Verlauf starte im Zentrum mit der Farbe {@code from} und
	 * endet am Rand des durch den Radius beschriebenen Kreises mit der Farbe
	 * {@code to}.
	 *
	 * @param centerX
	 * @param centerY
	 * @param radius
	 * @param from
	 * @param to
	 */
	public void setGradient( double centerX, double centerY, double radius, Color from, Color to ) {
		setFillColor(from);
		fill = new RadialGradientPaint(
			(float) centerX, (float) centerY, (float) radius,
			new float[]{0f, 1f},
			new java.awt.Color[]{from.getJavaColor(), to.getJavaColor()});
	}

	/**
	 * Entfernt den Farbverlauf von der Form.
	 */
	public void noGradient() {
		fill = null;
	}

	/**
	 * Hilfsmethode für Unterklassen, um die angegebene Form mit der gesetzten
	 * Füllung auf den Grafik-Kontext zu zeichnen. Die Methode verändert
	 * gegebenenfalls die aktuelle Farbe des Grafikobjekts und setzt sie nicht
	 * auf den Ursprungswert zurück. Dies sollte die aufrufende Unterklasse
	 * übernehmen.
	 *
	 * @param shape Die zu zeichnende Java-AWT Form
	 * @param graphics Das Grafikobjekt.
	 */
	protected void fillShape( java.awt.Shape shape, Graphics2D graphics ) {
		if( fill != null ) {
			graphics.setPaint(fill);
			graphics.fill(shape);
		} else if( fillColor != null && fillColor.getAlpha() > 0 ) {
			graphics.setColor(fillColor.getJavaColor());
			graphics.fill(shape);
		}
	}

}
