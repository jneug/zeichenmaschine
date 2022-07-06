package schule.ngb.zm.shapes;

import schule.ngb.zm.Layer;

import java.awt.Graphics2D;
import java.util.Collection;
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

	public Shape getShape( int index ) {
		return shapes.get(index);
	}

	public <ST extends Shape> ST getShape( Class<ST> shapeClass ) {
		for( Shape s : shapes ) {
			if( shapeClass.isInstance(s) ) {
				return (ST) s;
			}
		}
		return null;
	}

	public java.util.List<Shape> getShapes() {
		return shapes;
	}

	public <ST extends Shape> java.util.List<ST> getShapes( Class<ST> shapeClass ) {
		java.util.List<ST> result = new LinkedList<>();
		for( Shape s : shapes ) {
			if( shapeClass.isInstance(s) ) {
				result.add((ST) s);
			}
		}
		return result;
	}

	public void add( Shape... shapes ) {
		synchronized( shapes ) {
			for( Shape s : shapes ) {
				this.shapes.add(s);
			}
		}
	}

	public void add( Collection<Shape> shapes ) {
		synchronized( shapes ) {
			for( Shape s : shapes ) {
				this.shapes.add(s);
			}
		}
	}

	public void remove( Shape... shapes ) {
		synchronized( shapes ) {
			for( Shape s: shapes ) {
				this.shapes.remove(s);
			}
		}
	}

	public void remove( Collection<Shape> shapes ) {
		synchronized( shapes ) {
			for( Shape s: shapes ) {
				this.shapes.remove(s);
			}
		}
	}

	public void removeAll() {
		synchronized( shapes ) {
			shapes.clear();
		}
	}

	public void showAll() {
		synchronized( shapes ) {
			for( Shape pShape : shapes ) {
				pShape.show();
			}
		}
	}

	public void hideAll() {
		synchronized( shapes ) {
			for( Shape pShape : shapes ) {
				pShape.hide();
			}
		}
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
