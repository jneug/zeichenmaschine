package schule.ngb.zm;

import java.awt.*;
import java.util.LinkedList;

public class DrawableLayer extends Layer {

	protected LinkedList<Drawable> drawables = new LinkedList<>();

	protected boolean clearBeforeDraw = true;

	public DrawableLayer() {
	}

	public DrawableLayer( int width, int height ) {
		super(width, height);
	}

	public void add( Drawable... drawables ) {
		synchronized( drawables ) {
			for( Drawable d : drawables ) {
				this.drawables.add(d);
			}
		}
	}

	public java.util.List<Drawable> getDrawables() {
		return drawables;
	}

	public boolean isClearBeforeDraw() {
		return clearBeforeDraw;
	}

	public void setClearBeforeDraw( boolean pClearBeforeDraw ) {
		this.clearBeforeDraw = pClearBeforeDraw;
	}

	@Override
	public void draw( Graphics2D pGraphics ) {
		if( clearBeforeDraw ) {
			clear();
		}

		synchronized( drawables ) {
			for( Drawable d : drawables ) {
				if( d.isVisible() ) {
					d.draw(drawing);
				}
			}
		}

		super.draw(pGraphics);
	}

}
