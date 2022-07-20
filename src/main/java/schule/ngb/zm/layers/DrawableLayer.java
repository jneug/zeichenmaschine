package schule.ngb.zm.layers;

import schule.ngb.zm.Drawable;
import schule.ngb.zm.Layer;

import java.awt.Graphics2D;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DrawableLayer extends Layer {

	protected List<Drawable> drawables = new LinkedList<>();

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
			List<Drawable> it = Collections.unmodifiableList(drawables);
			for( Drawable d : it ) {
				if( d.isVisible() ) {
					d.draw(drawing);
				}
			}
		}

		super.draw(pGraphics);
	}

}
