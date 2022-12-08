package schule.ngb.zm;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Basisklasse für Ebenen der {@link Zeichenleinwand}.
 * <p>
 * Die {@code Zeichenleinwand} besteht aus einer Reihe von Ebenen, die
 * übereinandergelegt und von "unten" nach "oben" gezeichnet werden. Die Inhalte
 * der oberen Ebenen können also Inhalte der darunterliegenden verdecken.
 * <p>
 * Ebenen sind ein zentraler Bestandteil bei der Implementierung einer
 * {@link Zeichenmaschine}. Sie erben von {@code Constants}, da neue Ebenentypen
 * von Nutzern implementiert werden können.
 */
public abstract class Layer extends Constants implements Drawable, Updatable {

	/**
	 * Interner Puffer für die Ebene, der einmal pro Frame auf die
	 * Zeichenleinwand übertragen wird.
	 */
	protected BufferedImage buffer;

	/**
	 * Der Grafikkontext der Ebene, der zum Zeichnen der Inhalte verwendet
	 * wird.
	 */
	protected Graphics2D drawing;

	/**
	 * Ob die Ebene derzeit sichtbar ist.
	 */
	protected boolean visible = true;

	/**
	 * Ob die Ebene aktiv ist, also {@link #update(double) Updates} empfangen
	 * soll.
	 */
	protected boolean active = true;


	/**
	 * Erstellt eine neue Ebene mit den Standardmaßen.
	 */
	public Layer() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	/**
	 * Erstellt eine neue Ebene mit den angegebenen Maßen.
	 *
	 * @param width Die Breite der Ebene.
	 * @param height Die Höhe der Ebene.
	 */
	public Layer( int width, int height ) {
		createCanvas(width, height);
	}

	/**
	 * @return Die Breite der Ebene.
	 */
	public int getWidth() {
		return buffer.getWidth();
	}

	/**
	 * @return Die Höhe der Ebene.
	 */
	public int getHeight() {
		return buffer.getHeight();
	}

	/**
	 * Ändert die Größe der Ebene auf die angegebenen Maße.
	 *
	 * @param width Die neue Breite.
	 * @param height Die neue Höhe.
	 */
	public void setSize( int width, int height ) {
		if( buffer != null ) {
			if( buffer.getWidth() != width || buffer.getHeight() != height ) {
				recreateCanvas(width, height);
			}
		} else {
			createCanvas(width, height);
		}

	}

	/**
	 * Gibt die Ressourcen der Ebene frei.
	 */
	public void dispose() {
		drawing.dispose();
		drawing = null;
		buffer = null;
	}

	/**
	 * Erstellt einen neuen Puffer für die Ebene und konfiguriert diesen.
	 *
	 * @param width Breite des neuen Puffers.
	 * @param height Höhe des neuen Puffers.
	 */
	private void createCanvas( int width, int height ) {
		buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		drawing = buffer.createGraphics();

		// add antialiasing
		RenderingHints hints = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON
		);
		hints.put(
			RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_ON
		);
		hints.put(
			RenderingHints.KEY_RENDERING,
			RenderingHints.VALUE_RENDER_QUALITY
		);
		drawing.addRenderingHints(hints);
	}

	/**
	 * Erstellt einen neuen Puffer für die Ebene mit der angegebenen Größe und
	 * kopiert den Inhalt des alten Puffers in den Neuen.
	 *
	 * @param width Breite des neuen Puffers.
	 * @param height Höhe des neuen Puffers.
	 */
	private void recreateCanvas( int width, int height ) {
		BufferedImage oldCanvas = buffer;
		createCanvas(width, height);
		drawing.drawImage(oldCanvas, 0, 0, null);
	}

	/**
	 * Leert die Ebene und löscht alles bisher gezeichnete. Alle Pixel der Ebene
	 * werden transparent, damit unterliegende Ebenen durchscheinen können.
	 */
	public void clear() {
		// https://stackoverflow.com/questions/31149206/set-pixels-of-bufferedimage-as-transparent
		Color current = drawing.getColor();
		drawing.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		drawing.setColor(new Color(255, 255, 255, 255));
		drawing.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
		drawing.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		drawing.setColor(current);
	}

	/**
	 * Zeichnet den Puffer auf den Grafikkontext.
	 *
	 * @param graphics Der Grafikkontext, auf den gezeichnet wird.
	 */
	@Override
	public void draw( Graphics2D graphics ) {
		if( visible ) {
			graphics.drawImage(buffer, 0, 0, null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Versteckt die Ebene.
	 */
	public void hide() {
		visible = false;
	}

	/**
	 * Zeigt die Ebene an, falls sie versteckt war.
	 */
	@SuppressWarnings( "unused" )
	public void show() {
		visible = true;
	}

	/**
	 * Versteckt oder zeigt die Ebene, je nachdem, welchen Zustand sie derzeit
	 * hat.
	 */
	@SuppressWarnings( "unused" )
	public void toggle() {
		visible = !visible;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update( double delta ) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isActive() {
		return active;
	}

	/**
	 * Prüft, ob die angegebenen Koordinaten innerhalb der Ebene liegen, oder
	 * nicht.
	 * <p>
	 * Eine Koordinate liegt in der Ebene, wenn die {@code x}- und
	 * {@code y}-Koordinaten größer oder gleich Null und kleiner als die Breite
	 * bzw. Höhe der Ebene sind.
	 *
	 * @param x Die x-Koordinate.
	 * @param y Die y-Koordinate.
	 * @return {@code true}, wenn die Koordinaten innerhalb der Ebene liegen,
	 *    {@code false}, wenn sie außerhalb liegen.
	 */
	@SuppressWarnings( "unused" )
	public boolean isInBounds( int x, int y ) {
		return (x >= 0 && y >= 0 && x < getWidth() && y < getHeight());
	}

}
