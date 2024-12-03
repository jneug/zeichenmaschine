package schule.ngb.zm.particles;

import schule.ngb.zm.Drawable;
import schule.ngb.zm.Updatable;
import schule.ngb.zm.Vector;


public abstract class Particle extends PhysicsObject implements Updatable, Drawable {

	protected double maxLifetime = 0;

	protected double lifetime = 0;


	public Particle() {
		super();
	}

	public Particle( int pLifetime ) {
		super();
		maxLifetime = pLifetime;
	}

	public void spawn( Vector pPosition, Vector pVelocity ) {
		lifetime = maxLifetime;
		position = pPosition.copy();
		velocity = pVelocity.copy();
		acceleration = new Vector();
	}

	@Override
	public boolean isActive() {
		return lifetime > 0;
	}

	@Override
	public boolean isVisible() {
		return isActive();
	}

	public double getLifetime() {
		return lifetime;
	}

	public void setLifetime( double pLifetime ) {
		this.lifetime = pLifetime;
	}

	public double getMaxLifetime() {
		return maxLifetime;
	}

	public void setMaxLifetime( double pMaxLifetime ) {
		this.maxLifetime = pMaxLifetime;
	}

	@Override
	public void update( double delta ) {
		super.update(delta);
		// lifetime -= delta;
		lifetime -= 1;
	}

}
