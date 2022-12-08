package schule.ngb.zm.layers;

import schule.ngb.zm.Color;
import schule.ngb.zm.Layer;
import schule.ngb.zm.Options;

import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

/**
 * Eine Ebene, die nur aus einer Farbe (oder einem Farbverlauf) besteht.
 * <p>
 * Ein {@code ColorLayer} ist eine der drei Standardebenen der
 * {@link schule.ngb.zm.Zeichenmaschine}.
 * <p>
 * Die Farbe der Ebene kann beliebig gesetzt werden und kann gut als
 * Hintergrundfarbe für eine Szene dienen, oder als halbtransparente
 * "Abdeckung", wenn ein {@code ColorLayer} über den anderen Ebenen eingefügt
 * wird.
 */
@SuppressWarnings( "unused" )
public class ColorLayer extends Layer {

	/**
	 * Farbe der Ebene.
	 */
	private Color color;

	/**
	 * Verlauf der Ebene, falls verwendet.
	 */
	private Paint background;

	/**
	 * Erstellt eine neue Farbebene mit der angegebenen Farbe.
	 *
	 * @param color Die Hintergrundfarbe.
	 */
	public ColorLayer( Color color ) {
		this.color = color;
		this.background = color.getJavaColor();
		clear();
	}

	/**
	 * Erstellt eine neue Farbebene mit der angegebenen Größe und Farbe.
	 *
	 * @param width Breite der Ebene.
	 * @param height Höhe der Ebene.
	 * @param color Die Hintergrundfarbe.
	 */
	public ColorLayer( int width, int height, Color color ) {
		super(width, height);
		this.color = color;
		this.background = color.getJavaColor();
		clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSize( int width, int height ) {
		super.setSize(width, height);
		clear();
	}

	/**
	 * @return Die aktuelle Hintergrundfarbe der Ebene.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Setzt die Farbe der Ebene auf die angegebene Farbe.
	 *
	 * @param color Die neue Hintergrundfarbe.
	 */
	public void setColor( Color color ) {
		this.color = color;
		this.background = color.getJavaColor();
		clear();
	}

	/**
	 * Setzt die Farbe der Ebene auf einen Grauwert mit der angegebenen
	 * Intensität. 0 entspricht schwarz, 255 entspricht weiß.
	 *
	 * @param gray Ein Grauwert zwischen 0 und 255.
	 * @see Color#Color(int)
	 */
	public void setColor( int gray ) {
		setColor(gray, gray, gray, 255);
	}

	/**
	 * Setzt die Farbe der Ebene auf einen Grauwert mit der angegebenen
	 * Intensität und dem angegebenen Transparenzwert. Der Grauwert 0 entspricht
	 * schwarz, 255 entspricht weiß.
	 *
	 * @param gray Ein Grauwert zwischen 0 und 255.
	 * @param alpha Ein Transparenzwert zwischen 0 und 255.
	 * @see Color#Color(int, int)
	 */
	public void setColor( int gray, int alpha ) {
		setColor(gray, gray, gray, alpha);
	}

	/**
	 * Setzt die Farbe der Ebene auf die Farbe mit den angegebenen Rot-, Grün-
	 * und Blauanteilen.
	 *
	 * @param red Der Rotanteil der Farbe zwischen 0 und 255.
	 * @param green Der Grünanteil der Farbe zwischen 0 und 255.
	 * @param blue Der Blauanteil der Farbe zwischen 0 und 255.
	 * @see Color#Color(int, int, int)
	 * @see <a
	 * 	href="https://de.wikipedia.org/wiki/RGB-Farbraum">https://de.wikipedia.org/wiki/RGB-Farbraum</a>
	 */
	public void setColor( int red, int green, int blue ) {
		setColor(red, green, blue, 255);
	}

	/**
	 * Setzt die Farbe der Ebene auf die Farbe mit den angegebenen Rot-, Grün-
	 * und Blauanteilen und dem angegebenen Transparenzwert.
	 *
	 * @param red Der Rotanteil der Farbe zwischen 0 und 255.
	 * @param green Der Grünanteil der Farbe zwischen 0 und 255.
	 * @param blue Der Blauanteil der Farbe zwischen 0 und 255.
	 * @param alpha Ein Transparenzwert zwischen 0 und 25
	 * @see Color#Color(int, int, int, int)
	 * @see <a
	 * 	href="https://de.wikipedia.org/wiki/RGB-Farbraum">https://de.wikipedia.org/wiki/RGB-Farbraum</a>
	 */
	public void setColor( int red, int green, int blue, int alpha ) {
		setColor(new Color(red, green, blue, alpha));
	}

	/**
	 * Setzt die Füllung der Ebene auf einen linearen Farbverlauf, der in die
	 * angegebene Richtung verläuft.
	 *
	 * @param from Farbe am Startpunkt.
	 * @param to Farbe am Endpunkt.
	 * @param dir Richtung des Farbverlaufs.
	 */
	public void setGradient( Color from, Color to, Options.Direction dir ) {
		double halfW = getWidth() * .5;
		double halfH = getHeight() * .5;

		Options.Direction inv = dir.inverse();
		int fromX = (int) (halfW + inv.x * halfW);
		int fromY = (int) (halfH + inv.y * halfH);

		int toX = (int) (halfW + dir.x * halfW);
		int toY = (int) (halfH + dir.y * halfH);

		setGradient(fromX, fromY, from, toX, toY, to);
	}

	/**
	 * Setzt die Füllung der Ebene auf einen linearen Farbverlauf, der am Punkt
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
	public void setGradient( double fromX, double fromY, Color from, double toX, double toY, Color to ) {
		this.color = from;
		background = new GradientPaint(
			(float) fromX, (float) fromY, from.getJavaColor(),
			(float) toX, (float) toY, to.getJavaColor()
		);
		clear();
	}

	/**
	 * Setzt die Füllung der Ebene auf einen kreisförmigen (radialen)
	 * Farbverlauf, der im Zentrum beginnt.
	 *
	 * @param from Farbe im Zentrum.
	 * @param to Farbe am Rand.
	 */
	public void setGradient( Color from, Color to ) {
		setGradient(getWidth() * .5, getHeight() * .5, Math.min(getWidth() * .5, getHeight() * .5), from, to);
	}

	/**
	 * Setzt die Füllung der Ebene auf einen kreisförmigen (radialen)
	 * Farbverlauf, mit dem Zentrum im Punkt ({@code centerX}, {@code centerY})
	 * und dem angegebenen Radius. Der Verlauf starte im Zentrum mit der Farbe
	 * {@code from} und endet am Rand des durch den Radius beschriebenen Kreises
	 * mit der Farbe {@code to}.
	 *
	 * @param centerX x-Koordinate des Kreismittelpunktes.
	 * @param centerY y-Koordinate des Kreismittelpunktes.
	 * @param radius Radius des Kreises.
	 * @param from Farbe im Zentrum des Kreises.
	 * @param to Farbe am Rand des Kreises.
	 */
	public void setGradient( double centerX, double centerY, double radius, Color from, Color to ) {
		this.color = from;
		background = new RadialGradientPaint(
			(float) centerX, (float) centerY, (float) radius,
			new float[]{0f, 1f},
			new java.awt.Color[]{from.getJavaColor(), to.getJavaColor()});
		clear();
	}

	/**
	 * Zeichnet den Hintergrund der Ebene mit der gesetzten Füllung neu.
	 */
	@Override
	public void clear() {
		drawing.setPaint(background);
		drawing.fillRect(0, 0, getWidth(), getHeight());
	}

}
