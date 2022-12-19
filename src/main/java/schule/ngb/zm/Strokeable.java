package schule.ngb.zm;

import schule.ngb.zm.shapes.Shape;

import java.awt.BasicStroke;
import java.awt.Stroke;

/**
 * {@link Drawable} Klassen, die mit einer Konturlinie versehen werden können.
 * <p>
 * Das {@code Strokeable} Interface dient hauptsächlich zur Vereinheitlichung
 * der API für Konturlinien. Durch Implementation wird sichergestellt, dass alle
 * Objekte, die eine Konturlinie haben können, dieselben Methoden zur Verfügung
 * stellen. Wenn eine {@link Shape} eine
 * {@link Strokeable#setStrokeColor(Color, int)} Methode hat, dann sollte auch
 * eine {@link schule.ngb.zm.layers.TurtleLayer.Turtle} dieselbe Methode
 * anbieten. Im Einzelfall kann es sinnvoll sein, weitere Methoden für
 * Konturlinien zur verfügung zu stellen. Allerdings sollte davon nach
 * Möglichkeit zugunsten einer einheitlichen API abgesehen werden.
 * <p>
 * Das Äquivalent für Füllungen stellt {@link Fillable} dar.
 */
public interface Strokeable extends Drawable {

	/**
	 * Setzt den {@code Stroke} für die Konturlinie direkt.
	 *
	 * @param stroke Ein {@code Stroke}-Objekt.
	 */
	void setStroke( Stroke stroke );

	/**
	 * Gibt ein {@code Stroke}-Objekt mit den aktuell gesetzten Eigenschaften
	 * zurück.
	 *
	 * @return Ein {@code Stroke} mit den passenden Kontureigenschaften.
	 */
	Stroke getStroke();

	/**
	 * Gibt an, ob die aktuell gesetzten Eigenschaften eine sichtbare
	 * Konturlinie erzeugen.
	 * <p>
	 * Die Konturlinie gilt als sichtbar, wenn sie eine nicht transparente Farbe
	 * und eine Dicke größer 0 besitzt.
	 * <p>
	 * Das bedeutet, falls die Methode {@code false} zurückgibt, dann kann
	 * {@link #getStroke()} trotzdem ein gültiges {@link Stroke}-Objekt
	 * zurückgeben, beispielsweise wenn keine Farbe gesetzt wurde.
	 *
	 * @return {@code true}, wenn die Konturlinie sichtbar ist, {@code false}
	 * 	sonst.
	 */
	default boolean hasStroke() {
		Color strokeColor = getStrokeColor();
		double strokeWeight = getStrokeWeight();
		return strokeColor != null && strokeColor.getAlpha() > 0 && strokeWeight > 0;
	}

	/**
	 * Gibt die aktuelle Farbe der Konturlinie zurück.
	 *
	 * @return Die Konturfarbe oder {@code null}.
	 */
	Color getStrokeColor();

	/**
	 * Setzt die Farbe der Konturlinie auf die angegebene Farbe.
	 *
	 * @param color Die neue Farbe der Konturlinie.
	 * @see Color
	 */
	void setStrokeColor( Color color );

	/**
	 * Setzt die Farbe der Konturlinie auf die angegebene Farbe und setzt die
	 * Transparenz auf den angegebenen Wert. 0 is komplett durchsichtig und 255
	 * komplett deckend.
	 *
	 * @param color Die neue Farbe der Konturlinie oder {@code null}.
	 * @param alpha Ein Transparenzwert zwischen 0 und 255.
	 * @see Color#Color(Color, int)
	 */
	default void setStrokeColor( Color color, int alpha ) {
		setStrokeColor(new Color(color, alpha));
	}

	/**
	 * Setzt die Farbe der Konturlinie auf einen Grauwert mit der angegebenen
	 * Intensität. 0 entspricht schwarz, 255 entspricht weiß.
	 *
	 * @param gray Ein Grauwert zwischen 0 und 255.
	 * @see Color#Color(int)
	 */
	default void setStrokeColor( int gray ) {
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
	default void setStrokeColor( int gray, int alpha ) {
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
	default void setStrokeColor( int red, int green, int blue ) {
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
	default void setStrokeColor( int red, int green, int blue, int alpha ) {
		setStrokeColor(new Color(red, green, blue, alpha));
	}

	/**
	 * Entfernt die Kontur der Form.
	 */
	default void noStroke() {
		setStrokeColor(null);
	}

	/**
	 * Setzt die Farbe der Konturlinie auf die Standardwerte zurück.
	 *
	 * @see schule.ngb.zm.Constants#DEFAULT_STROKECOLOR
	 * @see schule.ngb.zm.Constants#DEFAULT_STROKEWEIGHT
	 * @see schule.ngb.zm.Constants#SOLID
	 */
	default void resetStroke() {
		setStrokeColor(Constants.DEFAULT_STROKECOLOR);
		setStrokeWeight(Constants.DEFAULT_STROKEWEIGHT);
		setStrokeType(Constants.SOLID);
	}

	/**
	 * Gibt die Dicke der Konturlinie zurück.
	 *
	 * @return Die aktuelle Dicke der Linie.
	 */
	double getStrokeWeight();

	/**
	 * Setzt die Dicke der Konturlinie. Die Dicke muss größer 0 sein. Wird 0
	 * übergeben, dann wird keine Kontur mehr angezeigt.
	 *
	 * @param weight Die Dicke der Konturlinie.
	 */
	default void setStrokeWeight( double weight ) {
		setStroke(createStroke(getStrokeType(), weight));
	}

	/**
	 * Gibt die Art der Konturlinie zurück.
	 *
	 * @return Die aktuelle Art der Konturlinie.
	 * @see Options.StrokeType
	 */
	Options.StrokeType getStrokeType();

	/**
	 * Setzt den Typ der Kontur. Erlaubte Werte sind {@link Constants#DASHED},
	 * {@link Constants#DOTTED} und {@link Constants#SOLID}.
	 *
	 * @param type Eine der möglichen Konturarten.
	 * @see Options.StrokeType
	 */
	default void setStrokeType( Options.StrokeType type ) {
		setStroke(createStroke(type, getStrokeWeight()));
	}

	/**
	 * Hilfsmethode, um ein {@link Stroke} Objekt mit den aktuellen
	 * Kontureigenschaften zu erstellen. Der aktuelle {@code Stroke} wird
	 * zwischengespeichert.
	 *
	 * @param strokeType
	 * @param strokeWeight
	 * @return Ein {@code Stroke} mit den passenden Kontureigenschaften.
	 */
	static Stroke createStroke( Options.StrokeType strokeType, double strokeWeight ) {
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

}
