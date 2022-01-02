package schule.ngb.zm.shapes;

import schule.ngb.zm.Layer;

import java.awt.*;
import java.util.LinkedList;

public class ShapesLayer extends Layer {

	protected boolean clearBeforeDraw = true;

	private LinkedList<Shape> shapes;

	public ShapesLayer() {
		super();
		shapes = new LinkedList<Shape>();
	}

	public ShapesLayer( int width, int height ) {
		super(width, height);
		shapes = new LinkedList<Shape>();
	}

	public void add( Shape... pFormen ) {
		synchronized( shapes ) {
			for( Shape f : pFormen ) {
				shapes.add(f);
			}
		}
	}

	public void showAll() {
		synchronized( shapes ) {
			for( Shape pShape : shapes ) {
				pShape.hide();
			}
		}
	}

	public void hideAll() {
		synchronized( shapes ) {
			for( Shape pShape : shapes ) {
				pShape.show();
			}
		}
	}

	public java.util.List<Shape> getShapes() {
		return shapes;
	}

	@Override
	public void draw( Graphics2D pGraphics ) {
		if( clearBeforeDraw ) {
			clear();
		}

		synchronized( shapes ) {
			for( Shape pShape : shapes ) {
				if( pShape.isVisible() ) {
					pShape.draw(drawing);
				}
			}
		}

		super.draw(pGraphics);
	}

}
