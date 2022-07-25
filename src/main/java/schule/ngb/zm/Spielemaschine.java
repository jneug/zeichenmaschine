package schule.ngb.zm;

import schule.ngb.zm.layers.DrawableLayer;

import java.awt.Graphics2D;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings( "unused" )
public class Spielemaschine extends Zeichenmaschine {

	private LinkedList<Drawable> drawables;

	private LinkedList<Updatable> updatables;

	private GameLayer mainLayer;

	public Spielemaschine( String title ) {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT, title);
	}


	public Spielemaschine( int width, int height, String title ) {
		super(width, height, title, false);

		drawables = new LinkedList<>();
		updatables = new LinkedList<>();

		mainLayer = new GameLayer();
		canvas.addLayer(mainLayer);
	}

	public final void add( Object... pObjects ) {
		for( Object obj : pObjects ) {
			if( obj instanceof Drawable ) {
				addDrawable((Drawable) obj);
			}
			if( obj instanceof Updatable ) {
				addUpdatable((Updatable) obj);
			}
		}
	}

	public final void addDrawable( Drawable pDrawable ) {
		synchronized( drawables ) {
			drawables.add(pDrawable);
		}
	}

	public final void addUpdatable( Updatable pUpdatable ) {
		synchronized( updatables ) {
			updatables.add(pUpdatable);
		}
	}

	public final void remove( Object... pObjects ) {
		for( Object obj : pObjects ) {
			if( obj instanceof Drawable ) {
				removeDrawable((Drawable) obj);
			}
			if( obj instanceof Updatable ) {
				removeUpdatable((Updatable) obj);
			}
		}
	}

	public final void removeDrawable( Drawable pDrawable ) {
		synchronized( drawables ) {
			drawables.remove(pDrawable);
		}
	}

	public final void removeUpdatable( Updatable pUpdatable ) {
		synchronized( updatables ) {
			updatables.remove(pUpdatable);
		}
	}

	protected void updateGame( double delta ) {

	}

	@Override
	public final void update( double delta ) {
		synchronized( updatables ) {
			List<Updatable> it = List.copyOf(updatables);
			for( Updatable u: it ) {
				if( u.isActive() ) {
					u.update(delta);
				}
			}
		}

		updateGame(delta);
	}

	@Override
	public final void draw() {

	}

	private class GameLayer extends Layer {

		public Graphics2D getGraphics() {
			return drawing;
		}

		@Override
		public void draw( Graphics2D pGraphics ) {
			clear();
			List<Drawable> it = List.copyOf(drawables);
			for( Drawable d: it ) {
				if( d.isVisible() ) {
					d.draw(drawing);
				}
			}
			super.draw(pGraphics);
		}

	}

}
