package schule.ngb.zm.layers;

import schule.ngb.zm.Layer;
import schule.ngb.zm.util.io.ImageLoader;

import java.awt.Graphics2D;
import java.awt.Image;

/**
 * Eine Ebene, die ein statisches Bild anzeigt.
 * <p>
 * Die Ebene wird mit einem Bild initialisiert und zeigt dieses Bild als
 * einzigen Inhalt an. Optional kann die Position des Bildes verändert werden,
 * sodass es nicht im Ursprung der Ebene gezeichnet wird.
 */
@SuppressWarnings( "unused" )
public class ImageLayer extends Layer {

	/**
	 * Das Bild, das angezeigt wird.
	 */
	protected Image image;

	/**
	 * x-Koordinate der oberen linken Ecke auf der Ebene.
	 */
	protected double x = 0;

	/**
	 * y-Koordinate der oberen linken Ecke auf der Ebene.
	 */
	protected double y = 0;

	/**
	 * Interner Schalter, ob die Ebene neu gezeichnet werden muss.
	 */
	protected boolean redraw = true;

	/**
	 * Erstellt eine Bildebene aus der angegebenen Bildquelle.
	 *
	 * @param source Eine Bildquelle.
	 */
	public ImageLayer( String source ) {
		image = ImageLoader.loadImage(source);
	}

	public ImageLayer( Image image ) {
		this.image = image;
	}

	public ImageLayer( int width, int height, Image image ) {
		super(width, height);
		this.image = image;
	}

	public void setImage( Image image ) {
		this.image = image;
		redraw = true;
	}

	public double getX() {
		return x;
	}

	public void setX( double pX ) {
		this.x = pX;
		redraw = true;
	}

	public double getY() {
		return y;
	}

	public void setY( double pY ) {
		this.y = pY;
		redraw = true;
	}

	@Override
	public void clear() {
		super.clear();
		redraw = true;
	}

	@Override
	public void draw( Graphics2D graphics ) {
		if( redraw && visible ) {
			drawing.drawImage(image, (int) x, (int) y, null);
			redraw = false;
		}
		super.draw(graphics);
	}

}
