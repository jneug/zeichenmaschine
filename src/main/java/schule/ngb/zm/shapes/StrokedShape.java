package schule.ngb.zm.shapes;

import schule.ngb.zm.Color;
import schule.ngb.zm.Constants;
import schule.ngb.zm.Drawable;
import schule.ngb.zm.Options;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;


/**
 * Basisklasse für Formen, die eine Konturlinie besitzen.
 */
public abstract class StrokedShape extends Constants implements Drawable {

	/**
	 * Aktuelle Farbe der Konturlinie oder {@code null}, wenn die Form ohne
	 * kontur dargestellt werden soll.
	 */
	protected Color strokeColor = DEFAULT_STROKECOLOR;

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
	 * Gibt die aktuelle Farbe der Konturlinie zurück.
	 *
	 * @return Die Konturfarbe oder {@code null}.
	 */
	public Color getStrokeColor() {
		return strokeColor;
	}

	/**
	 * Setzt die Farbe der Konturlinie auf die angegebene Farbe.
	 *
	 * @param color Die neue Farbe der Konturlinie.
	 * @see Color
	 */
	public void setStrokeColor( Color color ) {
		this.strokeColor = color;
	}

	/**
	 * Setzt die Farbe der Konturlinie auf die angegebene Farbe und setzt die
	 * Transparenz auf den angegebenen Wert. 0 is komplett durchsichtig und 255
	 * komplett deckend.
	 *
	 * @param color Die neue Farbe der Konturlinie oder {@code null}.
	 * @param alpha Ein Transparenzwert zwischen 0 und 255.
	 * @see Color#Color(Color, int)
	 */
	public void setStrokeColor( Color color, int alpha ) {
		setStrokeColor(new Color(color, alpha));
	}

	/**
	 * Setzt die Farbe der Konturlinie auf einen Grauwert mit der angegebenen
	 * Intensität. 0 entspricht schwarz, 255 entspricht weiß.
	 *
	 * @param gray Ein Grauwert zwischen 0 und 255.
	 * @see Color#Color(int)
	 */
	public void setStrokeColor( int gray ) {
		setStrokeColor(gray, gray, gray, 255);
	}

	/**
	 * Setzt die Farbe der Konturlinie auf einen Grauwert mit der angegebenen
	 * Intensität und dem angegebenen Transparenzwert. Der Grauwert 0 entspricht
	 * schwarz, 255 entspricht weiß.
	 *
	 * @param gray Ein Grauwert zwischen 0 und 255.
	 * @param alpha Ein Transparenzwert zwischen 0 und 255.
	 * @see Color#Color(int, int)
	 */
	public void setStrokeColor( int gray, int alpha ) {
		setStrokeColor(gray, gray, gray, alpha);
	}

	/**
	 * Setzt die Farbe der Konturlinie auf die Farbe mit den angegebenen Rot-,
	 * Grün- und Blauanteilen.
	 *
	 * @param red Der Rotanteil der Farbe zwischen 0 und 255.
	 * @param green Der Grünanteil der Farbe zwischen 0 und 255.
	 * @param blue Der Blauanteil der Farbe zwischen 0 und 255.
	 * @see Color#Color(int, int, int)
	 * @see <a
	 * 	href="https://de.wikipedia.org/wiki/RGB-Farbraum">https://de.wikipedia.org/wiki/RGB-Farbraum</a>
	 */
	public void setStrokeColor( int red, int green, int blue ) {
		setStrokeColor(red, green, blue, 255);
	}

	/**
	 * Setzt die Farbe der Konturlinie auf die Farbe mit den angegebenen Rot-,
	 * Grün- und Blauanteilen und dem angegebenen Transparenzwert.
	 *
	 * @param red Der Rotanteil der Farbe zwischen 0 und 255.
	 * @param green Der Grünanteil der Farbe zwischen 0 und 255.
	 * @param blue Der Blauanteil der Farbe zwischen 0 und 255.
	 * @param alpha Ein Transparenzwert zwischen 0 und 25
	 * @see Color#Color(int, int, int, int)
	 * @see <a
	 * 	href="https://de.wikipedia.org/wiki/RGB-Farbraum">https://de.wikipedia.org/wiki/RGB-Farbraum</a>
	 */
	public void setStrokeColor( int red, int green, int blue, int alpha ) {
		setStrokeColor(new Color(red, green, blue, alpha));
	}

	/**
	 * Entfernt die Kontur der Form.
	 */
	public void noStroke() {
		setStrokeColor(null);
	}

	/**
	 * Setzt die Farbe der Konturlinie auf die Standardwerte zurück.
	 *
	 * @see schule.ngb.zm.Constants#DEFAULT_STROKECOLOR
	 * @see schule.ngb.zm.Constants#DEFAULT_STROKEWEIGHT
	 * @see schule.ngb.zm.Constants#SOLID
	 */
	public void resetStroke() {
		setStrokeColor(DEFAULT_STROKECOLOR);
		setStrokeWeight(DEFAULT_STROKEWEIGHT);
		setStrokeType(SOLID);
	}

	/**
	 * Gibt die Dicke der Konturlinie zurück.
	 *
	 * @return Die aktuelle Dicke der Linie.
	 */
	public double getStrokeWeight() {
		return strokeWeight;
	}

	/**
	 * Setzt die Dicke der Konturlinie. Die Dicke muss größer 0 sein. Wird 0
	 * übergeben, dann wird keine Kontur mehr angezeigt.
	 *
	 * @param weight Die Dicke der Konturlinie.
	 */
	public void setStrokeWeight( double weight ) {
		this.strokeWeight = max(0.0, weight);
		this.stroke = null;
	}

	/**
	 * Gibt die Art der Konturlinie zurück.
	 *
	 * @return Die aktuelle Art der Konturlinie.
	 * @see Options.StrokeType
	 */
	public Options.StrokeType getStrokeType() {
		return strokeType;
	}

	/**
	 * Setzt den Typ der Kontur. Erlaubte Werte sind {@link #DASHED},
	 * {@link #DOTTED} und {@link #SOLID}.
	 *
	 * @param type Eine der möglichen Konturarten.
	 * @see Options.StrokeType
	 */
	public void setStrokeType( Options.StrokeType type ) {
		this.strokeType = type;
		this.stroke = null;
	}

	@Override
	public abstract void draw( Graphics2D graphics );

	/**
	 * Hilfsmethode, um ein {@link Stroke} Objekt mit den aktuellen
	 * Kontureigenschaften zu erstellen. Der aktuelle {@code Stroke} wird
	 * zwischengespeichert.
	 *
	 * @return Ein {@code Stroke} mit den passenden Kontureigenschaften.
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

	/**
	 * Hilfsmethode für Unterklassen, um die angegebene Form mit den aktuellen
	 * Kontureigenschaften auf den Grafik-Kontext zu zeichnen. Die Methode
	 * verändert gegebenenfalls die aktuelle Farbe des Grafikobjekts und setzt
	 * sie nicht auf den Ursprungswert zurück, wie von {@link #draw(Graphics2D)}
	 * gefordert. Dies sollte die aufrufende Unterklasse übernehmen.
	 *
	 * @param shape Die zu zeichnende Java-AWT Form
	 * @param graphics Das Grafikobjekt.
	 */
	protected void strokeShape( java.awt.Shape shape, Graphics2D graphics ) {
		if( strokeColor != null && strokeColor.getAlpha() > 0
			&& strokeWeight > 0.0 ) {
			graphics.setColor(strokeColor.getJavaColor());
			graphics.setStroke(createStroke());
			graphics.draw(shape);
		}
	}

}
