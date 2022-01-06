package schule.ngb.zm;

import java.awt.Color;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Layer extends Constants implements Drawable, Updatable {

	protected BufferedImage buffer;

	protected Graphics2D drawing;

	protected boolean visible = true;

	protected boolean active = true;


	public Layer() {
		this(STD_WIDTH, STD_HEIGHT);
	}

	public Layer( int width, int height ) {
		createCanvas(width, height);
	}

	public int getWidth() {
		return buffer.getWidth();
	}

	public int getHeight() {
		return buffer.getHeight();
	}

	public void setSize( int width, int height ) {
		if( buffer != null ) {
			if( buffer.getWidth() != width || buffer.getHeight() != height ) {
				recreateCanvas(width, height);
			}
		} else {
			createCanvas(width, height);
		}

	}

	public void dispose() {
		drawing.dispose();
	}

	/**
	 * Erstellt einen neuen Puffer für die Ebene und konfiguriert diesen.
	 *
	 * @param width  Width des neuen Puffers.
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
	 * Erstellt einen neuen Puffer für dei Ebene mit der angegebenen Größe
	 * und kopiert den Inhalt des alten Puffers in den Neuen.
	 *
	 * @param width  Width des neuen Puffers.
	 * @param height Höhe des neuen Puffers.
	 */
	private void recreateCanvas( int width, int height ) {
		BufferedImage oldCanvas = buffer;
		createCanvas(width, height);
		drawing.drawImage(oldCanvas, 0, 0, null);
	}

	/**
	 * Leert die Ebene und löscht alles bisher gezeichnete. Alle Pixel der
	 * Ebene werden transparent, damit unterliegende Ebenen durchscheinen können.
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
	 * Zeichnet den Puffer auf die Grafik-Instanz.
	 *
	 * @param pGraphics
	 */
	@Override
	public void draw( Graphics2D pGraphics ) {
		if( visible ) {
			pGraphics.drawImage(buffer, 0, 0, null);
		}
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	public void hide() {
		visible = false;
	}

	public void view() {
		visible = true;
	}

	public void toggle() {
		visible = !visible;
	}

	@Override
	public void update( double delta ) {
	}

	@Override
	public boolean isActive() {
		return active;
	}

}
