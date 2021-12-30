package schule.ngb.zm.formen;

import schule.ngb.zm.Layer;

import java.awt.*;
import java.util.LinkedList;

public class ShapesLayer extends Layer {

	protected boolean clearBeforeDraw = true;

	private LinkedList<Shape> formen;

	public ShapesLayer() {
		super();
		formen = new LinkedList<Shape>();
	}

	public ShapesLayer( int width, int height ) {
		super(width, height);
		formen = new LinkedList<Shape>();
	}

	public void add( Shape... pFormen ) {
		synchronized( formen ) {
			for( Shape f : pFormen ) {
				formen.add(f);
			}
		}
	}

	public void showAll() {
		synchronized( formen ) {
			for( Shape pShape : formen ) {
				pShape.hide();
			}
		}
	}

	public void hideAll() {
		synchronized( formen ) {
			for( Shape pShape : formen ) {
				pShape.show();
			}
		}
	}

	public java.util.List<Shape> getShapes() {
		return formen;
	}

	@Override
	public void draw( Graphics2D pGraphics ) {
		if( clearBeforeDraw ) {
			clear();
		}

		synchronized( formen ) {
			for( Shape pShape : formen ) {
				if( pShape.isVisible() ) {
					pShape.draw(drawing);
				}
			}
		}

		super.draw(pGraphics);
	}

}
