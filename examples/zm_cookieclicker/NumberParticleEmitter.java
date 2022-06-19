import schule.ngb.zm.Drawable;
import schule.ngb.zm.Updatable;
import schule.ngb.zm.shapes.Text;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class NumberParticleEmitter implements Updatable, Drawable {

	public class NumberParticle extends Text implements Updatable {

		double velX = 0, velY = 0, accX = 0, accY = 0;

		double life = 0;

		public NumberParticle() {
			super(0, 0, "0");
			// life = particleLifespan;
		}

		@Override
		public boolean isActive() {
			return (life <= 0);
		}

		public void activate() {
			x = emitterX;
			y = emitterY;
			life = particleLifespan;
		}

		@Override
		public void update( double delta ) {
			if( isActive() ) {
				velX += accX;
				velY += accY;
				x += velX;
				y += velY;

				life -= delta;
				setFillColor(fillColor, (int) interpolate(0, 255, life / particleLifespan));
			}
		}

	}

	private int particlesPerSecond, maxParticles;

	private double particleLifespan;

	private ArrayList<NumberParticle> particles;

	private int activeParticles = 0;

	private double lastEmit = 0.0;

	private double emitterX, emitterY;

	public NumberParticleEmitter( double x, double y, int particlesPerSecond, double particleLifespan ) {
		emitterX = x;
		emitterY = y;

		this.particlesPerSecond = particlesPerSecond;
		this.particleLifespan = particleLifespan;

		maxParticles = (int) Math.ceil(particlesPerSecond * particleLifespan);
		particles = new ArrayList<>(maxParticles);

		for( int i = 0; i < maxParticles; i++ ) {
			particles.add(new NumberParticle());
		}
	}

	@Override
	public boolean isVisible() {
		return activeParticles > 0;
	}

	@Override
	public void draw( Graphics2D graphics ) {
		for( NumberParticle p : particles ) {
			if( p.isActive() ) {
				p.draw(graphics);
			}
		}
	}

	@Override
	public boolean isActive() {
		return isVisible();
	}

	public void emitParticle( int number ) {
		particles.add(new NumberParticle());
	}

	@Override
	public void update( double delta ) {
		//lastEmit -= delta;

		for( NumberParticle p : particles ) {
			if( p.isActive() ) {
				p.update(delta);
			} /*else if( lastEmit <= 0 ) {
				p.activate();
				lastEmit += 1/(double)particlesPerSecond;
			}*/
		}
	}

}
