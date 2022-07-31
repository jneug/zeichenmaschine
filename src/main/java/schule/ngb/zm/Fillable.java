package schule.ngb.zm;

import schule.ngb.zm.shapes.Shape;

import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

/**
 * {@link Drawable} Klassen, die mit einer Füllung versehen werden können.
 * <p>
 * Das {@code Fillable} Interface dient hauptsächlich zur Vereinheitlichung der
 * API für Füllungen. Durch Implementation wird sichergestellt, dass alle
 * Objekte, die eine Füllung haben können, dieselben Methoden zur Verfügung
 * stellen. Wenn eine {@link Shape} eine
 * {@link Fillable#setFillColor(Color, int)} Methode hat, dann sollte auch eine
 * {@link schule.ngb.zm.layers.TurtleLayer.Turtle} dieselbe Methode anbieten. Im
 * Einzelfall kann es sinnvoll sein, weitere Methoden für Füllungen zur
 * Verfügung zu stellen. Allerdings sollte davon nach Möglichkeit zugunsten
 * einer einheitlichen API abgesehen werden.
 * <p>
 * Das Äquivalent für Konturlinien stellt {@link Strokeable} dar.
 * <p>
 * Im einfachsten Fall reicht es {@link #setFill(Paint)} und {@link #getFill()}
 * zu implementieren. Die anderen Methoden besitzen Standardimplementierungen,
 * die sich auf die beiden Methoden beziehen. Allerdings ist es in vielen Fällen
 * sinnvoll, einige der Methoden gezielt zu überschreiben, um sie an spezifische
 * Situationen anzupassen.
 */
public interface Fillable extends Drawable {

	/**
	 * Setzt die Füllung direkt auf das angegebene {@code Paint}-Objekt.
	 *
	 * @param fill Die neue Füllung.
	 */
	void setFill( Paint fill );

	/**
	 * Gibt die aktuell gesetzte Füllung zurück.
	 * <p>
	 * Die Art der Füllung kann anhand der Abfragen {@link #hasFillColor()} und
	 * {@link #hasGradient()} ermittelt werden.
	 *
	 * @return Die aktuelle Füllung.
	 */
	Paint getFill();

	/**
	 * Gibt an, ob aktuell eine sichtbare Füllung konfiguriert ist.
	 * <p>
	 * Eine Füllung gilt als sichtbar, wenn eine nciht transparente Füllfarbe
	 * oder ein Farbverlauf eingestellt ist.
	 *
	 * @return {@code true}, wenn die Füllung sichtbar ist, {@code false} sonst.
	 */
	default boolean hasFill() {
		return (hasFillColor() && getFillColor().getAlpha() > 0) || hasGradient();
	}

	/**
	 * Gibt an, ob eine Füllfarbe konfiguriert ist.
	 * <p>
	 * Im Gegensatz zu {@link #hasFill()} prüft die Methode <em>nicht</em>, ob
	 * die Füllfarbe transparent ist.
	 *
	 * @return {@code true}, wenn eine Füllfarbe gesetzt ist.
	 */
	default boolean hasFillColor() {
		Paint fill = getFill();
		return fill instanceof Color;
	}

	/**
	 * Gibt an, ob ein Farbverlauf konfiguriert ist.
	 *
	 * @return {@code true}, wenn ein Farbverlauf gesetzt ist.
	 */
	default boolean hasGradient() {
		Paint fill = getFill();
		return fill instanceof GradientPaint;
	}

	/**
	 * Gibt die aktuelle Füllfarbe der Form zurück.
	 *
	 * @return Die aktuelle Füllfarbe oder {@code null}.
	 */
	default Color getFillColor() {
		if( hasFillColor() ) {
			return (Color) getFill();
		} else {
			return null;
		}
	}

	/**
	 * Setzt die Füllfarbe auf die angegebene Farbe.
	 *
	 * @param color Die neue Füllfarbe oder {@code null}.
	 * @see Color
	 */
	default void setFillColor( Color color ) {
		setFill(color);
	}

	/**
	 * Setzt die Füllfarbe auf die angegebene Farbe und setzt die Transparenz
	 * auf den angegebenen Wert. 0 is komplett durchsichtig und 255 komplett
	 * deckend.
	 *
	 * @param color Die neue Füllfarbe oder {@code null}.
	 * @param alpha Ein Transparenzwert zwischen 0 und 255.
	 * @see Color#Color(Color, int)
	 */
	default void setFillColor( Color color, int alpha ) {
		setFillColor(new Color(color, alpha));
	}

	/**
	 * Setzt die Füllfarbe auf einen Grauwert mit der angegebenen Intensität. 0
	 * entspricht schwarz, 255 entspricht weiß.
	 *
	 * @param gray Ein Grauwert zwischen 0 und 255.
	 * @see Color#Color(int)
	 */
	default void setFillColor( int gray ) {
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
	default void setFillColor( int gray, int alpha ) {
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
	default void setFillColor( int red, int green, int blue ) {
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
	default void setFillColor( int red, int green, int blue, int alpha ) {
		setFillColor(new Color(red, green, blue, alpha));
	}

	/**
	 * Entfernt die Füllung der Form.
	 */
	default void noFill() {
		setFillColor(null);
		noGradient();
	}

	/**
	 * Setzt die Füllfarbe auf den Standardwert zurück.
	 *
	 * @see schule.ngb.zm.Constants#DEFAULT_FILLCOLOR
	 */
	default void resetFill() {
		setFillColor(Constants.DEFAULT_FILLCOLOR);
		noGradient();
	}

	/**
	 * Gibt den aktuellen Farbverlauf der Form zurück.
	 *
	 * @return Der aktuelle Farbverlauf oder {@code null}.
	 */
	default GradientPaint getGradient() {
		if( hasGradient() ) {
			return (GradientPaint) getFill();
		} else {
			return null;
		}
	}

	/**
	 * Setzt die Füllung auf einen linearen Farbverlauf, der am Punkt
	 * ({@code fromX}, {@code fromY}) mit der Farbe {@code from} startet und am
	 * Punkt (({@code toX}, {@code toY}) mit der Farbe {@code to} endet.
	 *
	 * @param fromX x-Koordinate des Startpunktes.
	 * @param fromY y-Koordinate des Startpunktes.
	 * @param from Farbe am Startpunkt.
	 * @param toX x-Koordinate des Endpunktes.
	 * @param toY y-Koordinate des Endpunktes.
	 * @param to Farbe am Endpunkt.
	 */
	default void setGradient( double fromX, double fromY, Color from, double toX, double toY, Color to ) {
		setFill(new GradientPaint(
			(float) fromX, (float) fromY, from.getJavaColor(),
			(float) toX, (float) toY, to.getJavaColor()
		));
	}

	/**
	 * Setzt die Füllung auf einen linearen Farbverlauf, der in die angegebene
	 * Richtung verläuft.
	 *
	 * @param from Farbe am Startpunkt.
	 * @param to Farbe am Endpunkt.
	 * @param dir Richtung des Farbverlaufs.
	 */
	default void setGradient( Color from, Color to, Options.Direction dir ) {
		int whalf = (Constants.canvasWidth / 2);
		int hhalf = (Constants.canvasHeight / 2);
		setGradient(whalf - dir.x * whalf, hhalf - dir.y * hhalf, from, whalf + dir.x * whalf, hhalf + dir.y * hhalf, to);
	}

	/**
	 * Setzt die Füllung auf einen kreisförmigen (radialen) Farbverlauf, mit dem
	 * Zentrum im Punkt ({@code centerX}, {@code centerY}) und dem angegebenen
	 * Radius. Der Verlauf starte im Zentrum mit der Farbe {@code from} und
	 * endet am Rand des durch den Radius beschriebenen Kreises mit der Farbe
	 * {@code to}.
	 *
	 * @param centerX x-Koordinate des Kreismittelpunktes.
	 * @param centerY y-Koordinate des Kreismittelpunktes.
	 * @param radius Radius des Kreises.
	 * @param from Farbe im Zentrum des Kreises.
	 * @param to Farbe am Rand des Kreises.
	 */
	default void setGradient( double centerX, double centerY, double radius, Color from, Color to ) {
		setFill(new RadialGradientPaint(
			(float) centerX, (float) centerY, (float) radius,
			new float[]{0f, 1f},
			new java.awt.Color[]{from.getJavaColor(), to.getJavaColor()}));
	}

	/**
	 * Setzt die Füllung auf einen kreisförmigen (radialen) Farbverlauf, der im
	 * Zentrum beginnt.
	 *
	 * @param from Farbe im Zentrum.
	 * @param to Farbe am Rand.
	 */
	default void setGradient( Color from, Color to ) {
		int whalf = (Constants.canvasWidth / 2);
		int hhalf = (Constants.canvasHeight / 2);
		setGradient(whalf, hhalf, Math.min(whalf, hhalf), from, to);
	}

	/**
	 * Entfernt den Farbverlauf von der Form.
	 */
	default void noGradient() {
		setFill(null);
	}

}
