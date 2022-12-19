package schule.ngb.zm.layers;

import schule.ngb.zm.Drawable;
import schule.ngb.zm.Layer;

import java.awt.Graphics2D;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Ein Layer um {@link Drawable} Objekte zu zeichnen.
 * <p>
 * Objekte, die das {@code Drawable} Interface implementieren, können der Ebene
 * hinzugefügt werden. Die Ebene sorgt dafür, dass alle {@code Drawable}s einmal
 * pro Frame über ihre {@link Drawable#draw(Graphics2D)} Methode gezeichnet.
 */
@SuppressWarnings( "unused" )
public class DrawableLayer extends Layer {

	/**
	 * Liste der {@link Drawable}s.
	 */
	protected final List<Drawable> drawables;

	/**
	 * Ob die Ebene bei jedem Aufruf von {@link #draw(Graphics2D)} geleert
	 * werden soll.
	 */
	protected boolean clearBeforeDraw = true;

	/**
	 * Erstellt eine Ebene in der Standardgröße.
	 */
	public DrawableLayer() {
		drawables = new LinkedList<>();
	}

	/**
	 * Erstellt eine Ebene mit der angegebenen Größe.
	 *
	 * @param width Die Breite der Ebene.
	 * @param height Die Höhe der Ebene.
	 */
	public DrawableLayer( int width, int height ) {
		super(width, height);
		drawables = new LinkedList<>();
	}

	/**
	 * Fügt alle angegebenen {@code Drawable}s der Ebene hinzu.
	 *
	 * @param drawables Die {@code Drawable} Objekte.
	 */
	public void add( Drawable... drawables ) {
		synchronized( this.drawables ) {
			Collections.addAll(this.drawables, drawables);
		}
	}

	/**
	 * Gibt eine Liste aller {@code Drawable} Objekte dieser Ebene zurück.
	 *
	 * @return Die Liste der {@code Drawable} Objekte.
	 */
	public java.util.List<Drawable> getDrawables() {
		return drawables;
	}

	/**
	 * Ob die Ebene bei jedem Frame automatisch gelöscht wird.
	 *
	 * @return {@code true}, wenn die Ebene vorm Zeichnen gelöscht wird,
	 *    {@code false} sonst.
	 */
	public boolean isClearBeforeDraw() {
		return clearBeforeDraw;
	}

	/**
	 * Stellt ein, ob die Ebene vorm Zeichnen gelöscht werden soll.
	 *
	 * @param pClearBeforeDraw Ob die Ebene vorm Zeichnen gelöscht werden
	 * 	soll.
	 */
	public void setClearBeforeDraw( boolean pClearBeforeDraw ) {
		this.clearBeforeDraw = pClearBeforeDraw;
	}

	@Override
	public void draw( Graphics2D graphics ) {
		if( clearBeforeDraw ) {
			clear();
		}

		List<Drawable> it = List.copyOf(drawables);
		for( Drawable d : it ) {
			if( d.isVisible() ) {
				d.draw(drawing);
			}
		}

		super.draw(graphics);
	}

}
