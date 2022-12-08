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
	 * Erstellt eine Bildebene in der Standardgröße aus der angegebenen
	 * Bildquelle.
	 *
	 * @param source Eine Bildquelle.
	 * @see ImageLoader#loadImage(String)
	 */
	public ImageLayer( String source ) {
		image = ImageLoader.loadImage(source);
	}

	/**
	 * Erstellt eine Bildebene in der Standardgröße aus dem angegebenen Bild.
	 *
	 * @param image Ein Bild-Objekt.
	 */
	public ImageLayer( Image image ) {
		this.image = image;
	}

	/**
	 * Erstellt eine Bildebene in der angegebenen Größe aus dem angegebenen
	 * Bild.
	 *
	 * @param width Breite der Bildebene.
	 * @param height Höhe der Bildebene.
	 * @param image Ein Bild-Objekt.
	 */
	public ImageLayer( int width, int height, Image image ) {
		super(width, height);
		this.image = image;
	}

	/**
	 * Setzt das Bild der Ebene auf das angegebene Bild-Objekt.
	 *
	 * @param image Ein Bild-Objekt.
	 */
	public void setImage( Image image ) {
		this.image = image;
		redraw = true;
	}

	/**
	 * @return Die x-Koordinate des Bildes in der Ebene.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Setzt die {@code x}-Koordinate des BIldes in der Ebene auf den
	 * angegebenen Wert.
	 *
	 * @param pX Die x-Koordinate des Bildes.
	 */
	public void setX( double pX ) {
		this.x = pX;
		redraw = true;
	}

	/**
	 * @return Die y-Koordinate des Bildes in der Ebene.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Setzt die {@code y}-Koordinate des BIldes in der Ebene auf den
	 * angegebenen Wert.
	 *
	 * @param pY Die y-Koordinate des Bildes.
	 */
	public void setY( double pY ) {
		this.y = pY;
		redraw = true;
	}

	/**
	 * Löscht die Ebene und zeichnet das Bild neu.
	 * <p>
	 * In der Regel muss die Ebene nicht gelöscht werden, da sie automatisch neu
	 * gezeichnet wird, sobald sich das zugrundeliegende Bild ändert.
	 */
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
