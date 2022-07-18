package schule.ngb.zm.shapes;

import schule.ngb.zm.Layer;
import schule.ngb.zm.anim.Animation;
import schule.ngb.zm.anim.AnimationFacade;
import schule.ngb.zm.anim.Easing;

import java.awt.Graphics2D;
import java.util.*;
import java.util.function.DoubleUnaryOperator;

public class ShapesLayer extends Layer {

	protected boolean clearBeforeDraw = true;

	private List<Shape> shapes;

	private List<Animation<? extends Shape>> animations;

	public ShapesLayer() {
		super();
		shapes = new LinkedList<>();
		animations = new LinkedList<>();
	}

	public ShapesLayer( int width, int height ) {
		super(width, height);
		shapes = new LinkedList<>();
		animations = new LinkedList<>();
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

	public List<Shape> getShapes() {
		return shapes;
	}

	public <ST extends Shape> List<ST> getShapes( Class<ST> shapeClass ) {
		List<ST> result = new LinkedList<>();
		for( Shape s : shapes ) {
			if( shapeClass.isInstance(s) ) {
				result.add((ST) s);
			}
		}
		return result;
	}

	public void add( Shape... shapes ) {
		synchronized( this.shapes ) {
			for( Shape s : shapes ) {
				this.shapes.add(s);
			}
		}
	}

	public void add( Collection<Shape> shapes ) {
		synchronized( this.shapes ) {
			for( Shape s : shapes ) {
				this.shapes.add(s);
			}
		}
	}

	public void remove( Shape... shapes ) {
		synchronized( this.shapes ) {
			for( Shape s : shapes ) {
				this.shapes.remove(s);
			}
		}
	}

	public void remove( Collection<Shape> shapes ) {
		synchronized( this.shapes ) {
			for( Shape s : shapes ) {
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
			for( Shape s : shapes ) {
				s.show();
			}
		}
	}

	public void hideAll() {
		synchronized( shapes ) {
			for( Shape s : shapes ) {
				s.hide();
			}
		}
	}

	public <S extends Shape> void play( Animation<S> anim ) {
		this.animations.add(anim);
		anim.start();
	}

	public <S extends Shape> void play( Animation<S> anim, int runtime ) {
		play(anim, runtime, Easing.DEFAULT_EASING);
	}

	public <S extends Shape> void play( Animation<S> anim, int runtime, DoubleUnaryOperator easing ) {
		AnimationFacade<S> facade = new AnimationFacade<>(anim, runtime, easing);
		play(facade);
	}

	@Override
	public void update( double delta ) {
		Iterator<Animation<? extends Shape>> it = animations.iterator();
		while( it.hasNext() ) {
			Animation<? extends Shape> anim = it.next();
			anim.update(delta);

			if( !anim.isActive() ) {
				animations.remove(anim);
			}
		}
	}

	@Override
	public void draw( Graphics2D pGraphics ) {
		if( clearBeforeDraw ) {
			clear();
		}

		synchronized( shapes ) {
			List<Shape> it = List.copyOf(shapes);
			for( Shape s : it ) {
				if( s.isVisible() ) {
					s.draw(drawing);
				}
			}
		}

		super.draw(pGraphics);
	}

}
