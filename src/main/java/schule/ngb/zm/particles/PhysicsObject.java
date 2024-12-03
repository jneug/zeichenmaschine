package schule.ngb.zm.particles;

import schule.ngb.zm.Updatable;
import schule.ngb.zm.Vector;


public abstract class PhysicsObject implements Updatable {

	protected Vector position, velocity, acceleration;

	protected double mass = 1.0;


	public PhysicsObject() {
		position = new Vector();
		velocity = new Vector();
		acceleration = new Vector();
	}

	public PhysicsObject( Vector pPosition ) {
		position = pPosition.copy();
		velocity = new Vector();
		acceleration = new Vector();
	}

	public Vector getAcceleration() {
		return acceleration;
	}

	public void setAcceleration( Vector pAcceleration ) {
		this.acceleration = pAcceleration;
	}

	public double getMass() {
		return mass;
	}

	public void setMass( double pMass ) {
		this.mass = pMass;
	}

	public Vector getPosition() {
		return position;
	}

	public void setPosition( Vector pPosition ) {
		this.position = pPosition;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public void setVelocity( Vector pVelocity ) {
		this.velocity = pVelocity;
	}

	public void accelerate( Vector pAcceleration ) {
		acceleration.add(Vector.div(pAcceleration, mass));
	}

	@Override
	public void update( double delta ) {
		velocity.add(acceleration);
		position.add(Vector.scale(velocity, delta));
		acceleration.scale(0.0);
	}

}
