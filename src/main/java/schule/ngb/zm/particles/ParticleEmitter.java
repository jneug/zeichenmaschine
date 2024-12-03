package schule.ngb.zm.particles;


import schule.ngb.zm.Drawable;
import schule.ngb.zm.Updatable;
import schule.ngb.zm.Vector;

import java.awt.Graphics2D;

public class ParticleEmitter implements Updatable, Drawable {

	protected ParticleFactory particleFactory;

	private int particlesPerFrame;

	private int particleLifetime = 180;

	private Particle[] particles;

	private boolean active = false;

	private Particle nextParticle;

	public Vector position;

	public Vector direction = new Vector();

	public int angle = 0;

	public double randomness = 0.0;

	// private Vortex vortex = null;

	public ParticleEmitter( double pX, double pY, int pParticlesPerFrame, ParticleFactory pFactory ) {
		this.position = new Vector(pX, pY);
		this.particlesPerFrame = pParticlesPerFrame;
		this.particleFactory = pFactory;

		// Create particle pool
		this.particles = new Particle[particlesPerFrame * pFactory.getMaxLifetime()];
		this.direction = Vector.random(8, 16).setLength(100);

		// vortex = new Vortex(position.copy().add(-10, -10), -.2, 8);
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public boolean isVisible() {
		return active;
	}

	public void start() {
		// Partikel initialisieren
		for( int i = 0; i < particles.length; i++ ) {
			particles[i] = particleFactory.createParticle();
		}

		active = true;
	}

	public void stop() {
		for( int i = 0; i < particles.length; i++ ) {
			particles[i].setLifetime(0);
			particles[i] = null;
		}
		active = false;
	}

	private Particle getNextParticle() {
		// TODO: improve by caching next particle
		for( Particle p : particles ) {
			if( p != null && !p.isActive() ) {
				return p;
			}
		}
		return null;
	}

	public void emitParticle() {
		int ppf = particlesPerFrame;
		Particle nextParticle = getNextParticle();
		while( ppf > 0 && nextParticle != null ) {
			// TODO: (ngb) randomize lifetime of particles in Factory?
//			int pLeben = (int) random(particleLifetime);
//			p.lifetime = pLeben;
//			p.maxLifetime = pLeben;

			double rotation = (angle / 2.0) - (int) (Math.random() * angle);
			Vector velocity = direction.copy().rotate(rotation);
			velocity.scale(random());

			nextParticle.spawn(this.position, velocity);
			nextParticle = getNextParticle();
			ppf -= 1;
		}
	}

	@Override
	public void update( double delta ) {
		emitParticle();

		boolean _active = false;
		for( Particle particle : particles ) {
			if( particle != null ) {
				if( particle.isActive() ) {
//					if( vortex != null ) {
//						vortex.attract(particle);
//					}
					particle.update(delta);
					_active = true;
				}
			}
		}
		this.active = _active;
	}

	private double random() {
		return 1.0 - (Math.random() * randomness);
	}

	private double random( double pZahl ) {
		return pZahl * random();
	}

	@Override
	public void draw( Graphics2D graphics ) {
		java.awt.Color current = graphics.getColor();
		for( Particle particle : particles ) {
			if( particle != null ) {
				particle.draw(graphics);
			}
		}

//		if( vortex != null ) {
//			graphics.setColor(java.awt.Color.BLACK);
//			double vscale = (4 * vortex.scale);
//			graphics.fillOval((int) (vortex.position.x - vscale * .5), (int) (vortex.position.y - vscale * .5), (int) vscale, (int) vscale);
//		}
		graphics.setColor(current);
	}


	class Vortex {

		Vector position;

		double speed = 1.0, scale = 1.0;

		public Vortex( Vector pPosition, double pSpeed, double pScale ) {
			this.position = pPosition.copy();
			this.scale = pScale;
			this.speed = pSpeed;
		}

		public void attract( Particle pPartikel ) {
			Vector diff = Vector.sub(pPartikel.position, this.position);
			double dx = -diff.y * this.speed;
			double dy = diff.x * this.speed;
			double f = 1.0 / (1.0 + (dx * dx + dy * dy) / scale);

			pPartikel.position.x += (diff.x - pPartikel.velocity.x) * f;
			pPartikel.position.y += (diff.y - pPartikel.velocity.y) * f;

		}

	}

}
