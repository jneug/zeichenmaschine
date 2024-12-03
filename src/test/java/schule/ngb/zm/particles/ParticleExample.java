package schule.ngb.zm.particles;

import schule.ngb.zm.*;
import schule.ngb.zm.layers.DrawableLayer;
import schule.ngb.zm.layers.DrawingLayer;

import java.awt.Graphics2D;

public class ParticleExample extends Testmaschine {

	public static void main( String[] args ) {
		new ParticleExample();
	}

	public ParticleExample() {
		super();
	}

	ParticleEmitter pe1, pe2, pe3;

	Rocket r;

	public void setup() {
		getLayer(DrawingLayer.class).hide();
		background.setColor(0);
		drawing.noStroke();
		drawing.setFillColor(WHITE);
		for( int i = 0; i < 1000; i++ ) {
			drawing.point(random(0, canvasWidth), random(0, canvasHeight));
		}

		pe1 = new ParticleEmitter(
			100, 100, 50, 5,
			// new BasicParticleFactory(PINK, BLUE)
			new GenericParticleFactory<Particle>(() -> {
				return new Particle() {
					@Override
					public void draw( Graphics2D graphics ) {
						graphics.setColor(Color.MAGENTA.getJavaColor());
						graphics.rotate(Constants.radians(45), (int) position.x, (int) position.y);
						graphics.drawRect((int) position.x - 3, (int) position.y - 3, 6, 6);
						graphics.rotate(Constants.radians(-45), (int) position.x, (int) position.y);
					}
				};
			})
		);
		pe1.randomness = .2;
		pe1.angle = 45;
		pe1.strength = 200;


		pe2 = new ParticleEmitter(
			300, 300, 50, 10,
			new GenericParticleFactory(() -> new StarParticle(RED, new Color(BLUE, 55)))
			//new GenericParticleFactory(StarParticle.class, RED, new Color(BLUE, 55))
		);
		pe2.direction = NORTH.asVector().scale(100);
		pe2.randomness = .8;
		pe2.angle = 90;
		pe2.strength = 200;


		pe3 = new ParticleEmitter(
			100, 400, 20, 8,
			new BasicParticleFactory(YELLOW, RED)
		);
		pe3.direction = SOUTH.asVector();
		pe3.randomness = .33;
		pe3.angle = 30;

		DrawableLayer drawables = new DrawableLayer();
		addLayer(drawables);
		drawables.add(pe1, pe2, pe3);

		pe1.start();
		pe2.start();
		pe3.start();

		r = new Rocket(200, 400);
		drawables.add(r);
		r.start();
	}

	@Override
	public void update( double delta ) {
		pe1.update(delta);
		pe2.update(delta);
		pe3.update(delta);
		pe3.position.add(NORTH.asVector().scale(Constants.map(runtime, 0, 1000, 0, 10) * delta));

		if( r.isActive() ) {
			r.update(delta);
		}
	}

	class Rocket extends PhysicsObject implements Drawable {

		ParticleEmitter trail;

		private boolean starting = false;

		private double acc = 4;

		public Rocket( double x, double y ) {
			super(new Vector(x, y));
			trail = new ParticleEmitter(
				x, y, 30, 6,
				new BasicParticleFactory(YELLOW, RED)
			);
			trail.direction = SOUTH.asVector();
			trail.randomness = .33;
			trail.angle = 30;
			trail.position = this.position;
		}

		public void start() {
			starting = true;
			trail.start();
		}

		@Override
		public boolean isActive() {
			return starting;
		}

		@Override
		public boolean isVisible() {
			return true;
		}

		@Override
		public void update( double delta ) {
			super.update(delta);
			if( this.acceleration.lengthSq() < acc * acc ) {
				this.accelerate(NORTHWEST.asVector().scale(acc));
			}

			trail.update(delta);
		}

		@Override
		public void draw( Graphics2D graphics ) {
			graphics.rotate(-Constants.radians(velocity.angle() + 180), position.getIntX(), position.getIntY());
			trail.draw(graphics);

			graphics.setColor(WHITE.getJavaColor());
			graphics.fillRect(position.getIntX() - 6, position.getIntY() - 32, 12, 32);
			graphics.fillPolygon(
				new int[]{position.getIntX() - 6, position.getIntX(), position.getIntX() + 6},
				new int[]{position.getIntY() - 32, position.getIntY() - 40, position.getIntY() - 32},
				3
			);
			graphics.rotate(Constants.radians(velocity.angle() + 180), position.getIntX(), position.getIntY());
		}

	}

}
