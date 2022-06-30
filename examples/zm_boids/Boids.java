import schule.ngb.zm.*;
import schule.ngb.zm.util.ImageLoader;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Boids extends Zeichenmaschine {

	public static void main( String[] args ) {
		new Boids();
	}

	public static final boolean BORDER_WRAP = true;

	public static final int N_BOIDS = 200;

	public static final int N_PREDATORS = 0;

	private List<Creature> creatures;

	public Boids() {
		super(1280, 720, "ZM: Boids");
	}

	@Override
	public void setup() {
		setFullscreen(true);
		setCursor(ImageLoader.loadImage("pointer.png"), 0, 0);

		creatures = new ArrayList<Creature>();

		synchronized( creatures ) {
			for( int i = 0; i < N_BOIDS; i++ ) {
				creatures.add(new Boid());
			}
			for( int i = 0; i < N_PREDATORS; i++ ) {
				creatures.add(new Predator());
			}
		}
	}

	@Override
	public void update( double delta ) {
		synchronized( creatures ) {
			for( Creature c : creatures ) {
				c.update(creatures);
				if( Double.isNaN(c.getPosition().x) ) {
					pause();
					break;
				}
			}
		}
	}

	@Override
	public void draw() {
		drawing.clear(0, 125, 182);
		synchronized( creatures ) {
			for( Creature c : creatures ) {
				c.draw(drawing);
			}
		}
	}

	@Override
	public void mouseClicked() {
		synchronized( creatures ) {
			creatures.add(new Predator(
				Vector.mouse(), Vector.ZERO
			));
		}
	}

	@Override
	public void keyPressed() {
		if( keyCode == KEY_G ) {
			setSize(800, 800);
		} else if( keyCode == KEY_C ) {
			int cnt = 0;
			for( Creature c : creatures ) {
				Vector pos = c.getPosition();
				if(  pos.x >= 0 && pos.x <= width && pos.y >= 0 && pos.y <= height ) {
					cnt++;
				} else {
					System.out.printf("Missing: %s\n", c.getPosition().toString());
				}
			}
			System.out.printf("Screen: %dx%d\nCreatures: %d of %d\n", width, height, cnt, creatures.size());
		} else if( keyCode == KEY_F ) {
			setFullscreen(true);
		} else if( keyCode == KEY_Q ) {
			for( Creature c : creatures ) {
				c.getPosition().set(width/2, height/2);
			}
		}
	}

	/*@Override
	public void fullscreenChanged() {
		if( creatures != null ) {
			synchronized( creatures ) {
				for( Creature c : creatures ) {
					c.limitPosition();
				}
			}
		}
	}
	*/
}
