package schule.ngb.zm;

import java.awt.Graphics;
import java.util.LinkedList;

public class Spielemaschine extends Zeichenmaschine {

	private LinkedList<Drawable> drawables;

	private LinkedList<Updatable> updatables;

	private GraphicsLayer mainLayer;

	public Spielemaschine( String title ) {
		this(STD_WIDTH, STD_HEIGHT, title);
	}


	public Spielemaschine( int width, int height, String title ) {
		super(width, height, title, false);

		drawables = new LinkedList<>();
		updatables = new LinkedList<>();

		mainLayer = new GraphicsLayer();
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
			for( Updatable updatable : updatables ) {
				if( updatable.isActive() ) {
					updatable.update(delta);
				}
			}
		}

		updateGame(delta);
	}

	@Override
	public final void draw() {
		mainLayer.clear();
		synchronized( drawables ) {
			for( Drawable drawable : drawables ) {
				if( drawable.isVisible() ) {
					drawable.draw(mainLayer.getGraphics());
				}
			}
		}
	}

}
