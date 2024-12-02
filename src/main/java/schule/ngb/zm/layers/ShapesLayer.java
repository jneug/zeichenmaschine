package schule.ngb.zm.layers;

import schule.ngb.zm.Layer;
import schule.ngb.zm.Updatable;
import schule.ngb.zm.anim.Animation;
import schule.ngb.zm.anim.AnimationFacade;
import schule.ngb.zm.anim.Easing;
import schule.ngb.zm.shapes.Shape;

import java.awt.Graphics2D;
import java.util.*;
import java.util.function.DoubleUnaryOperator;

/**
 * Eine Ebene um {@link Shape} Objekte zu zeichnen.
 * <p>
 * Ein {@code ShapesLayer} ist eine der drei Standardebenen der
 * {@link schule.ngb.zm.Zeichenmaschine}.
 */
@SuppressWarnings( "unused" )
public class ShapesLayer extends Layer {

	/**
	 *
	 */
	protected boolean clearBeforeDraw = true;

	protected boolean updateShapes = true;

	protected final List<Shape> shapes;

	private final List<Animation<? extends Shape>> animations;

	private final List<Updatable> updatables;

	public ShapesLayer() {
		super();
		shapes = new LinkedList<>();
		animations = new LinkedList<>();
		updatables = new LinkedList<>();
	}

	public ShapesLayer( int width, int height ) {
		super(width, height);
		shapes = new LinkedList<>();
		animations = new LinkedList<>();
		updatables = new LinkedList<>();
	}

	public Shape getShape( int index ) {
		return shapes.get(index);
	}

	public <ST extends Shape> ST getShape( Class<ST> shapeClass ) {
		for( Shape s : shapes ) {
			if( shapeClass.isInstance(s) ) {
				return shapeClass.cast(s);
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
				result.add(shapeClass.cast(s));
			}
		}
		return result;
	}

	public void add( Shape... shapes ) {
		synchronized( this.shapes ) {
			Collections.addAll(this.shapes, shapes);

			for( Shape s : shapes ) {
				if( Updatable.class.isInstance(s) ) {
					updatables.add((Updatable) s);
				}
			}
		}
	}

	public void add( Collection<Shape> shapes ) {
		synchronized( this.shapes ) {
			this.shapes.addAll(shapes);

			for( Shape s : shapes ) {
				if( Updatable.class.isInstance(s) ) {
					updatables.add((Updatable) s);
				}
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
			this.shapes.removeAll(shapes);
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

	@SafeVarargs
	public final void play( Animation<? extends Shape>... anims ) {
		for( Animation<? extends Shape> anim : anims ) {
			this.animations.add(anim);
			anim.start();
		}
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
		if( updateShapes ) {
			synchronized( shapes ) {
				List<Updatable> uit = List.copyOf(updatables);
				for( Updatable u : uit ) {
					if( u.isActive() ) {
						u.update(delta);
					}
				}
			}

			/*
			Iterator<Updatable> uit = updatables.iterator();
			while( uit.hasNext() ) {
				Updatable u = uit.next();
				if( u.isActive() ) {
					u.update(delta);
				}
			}
			*/
		}

		Iterator<Animation<? extends Shape>> it = animations.iterator();
		while( it.hasNext() ) {
			Animation<? extends Shape> anim = it.next();
			anim.update(delta);

			if( !anim.isActive() ) {
				it.remove();
			}
		}
	}

	@Override
	public void draw( Graphics2D graphics ) {
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

		super.draw(graphics);
	}

}
