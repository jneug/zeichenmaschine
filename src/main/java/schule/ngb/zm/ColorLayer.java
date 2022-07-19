package schule.ngb.zm;

import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

/**
 * Eine Ebene, die nur aus einer Farbe (oder einem Farbverlauf) besteht.
 * <p>
 * Die Farbe der Ebene kann beliebig gesetzt werden und kann gut als
 * Hintergundfarbe für eine Szene dienen, oder als halbtransparente "Abdeckung",
 * wenn ein {@code ColorLayer} über den anderen Ebenen eingefügt wird.
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
	 * Gibt die Hintergrundfarbe der Ebene zurück.
	 *
	 * @return Die aktuelle Hintergrundfarbe.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Setzt die Farbe der Ebene neu.
	 *
	 * @param color Die neue Hintergrundfarbe.
	 */
	public void setColor( Color color ) {
		this.color = color;
		this.background = color.getJavaColor();
		clear();
	}

	public void setColor( int gray ) {
		setColor(gray, gray, gray, 255);
	}

	public void setColor( int gray, int alpha ) {
		setColor(gray, gray, gray, alpha);
	}

	public void setColor( int red, int green, int blue ) {
		setColor(red, green, blue, 255);
	}

	public void setColor( int red, int green, int blue, int alpha ) {
		setColor(new Color(red, green, blue, alpha));
	}

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

	public void setGradient( double fromX, double fromY, Color from, double toX, double toY, Color to ) {
		this.color = from;
		background = new GradientPaint(
			(float) fromX, (float) fromY, from.getJavaColor(),
			(float) toX, (float) toY, to.getJavaColor()
		);
		clear();
	}

	public void setGradient( Color from, Color to ) {
		setGradient(getWidth() * .5, getHeight() * .5, Math.min(getWidth() * .5, getHeight() * .5), from, to);
	}

	public void setGradient( double centerX, double centerY, double radius, Color from, Color to ) {
		this.color = from;
		background = new RadialGradientPaint(
			(float) centerX, (float) centerY, (float) radius,
			new float[]{0f, 1f},
			new java.awt.Color[]{from.getJavaColor(), to.getJavaColor()});
		clear();
	}

	@Override
	public void clear() {
		drawing.setPaint(background);
		drawing.fillRect(0, 0, getWidth(), getHeight());
	}

}
