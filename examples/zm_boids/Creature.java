import schule.ngb.zm.Constants;
import schule.ngb.zm.DrawingLayer;
import schule.ngb.zm.Vector;

import java.util.List;

public abstract class Creature extends Constants {

	protected Vector position, velocity, acceleration;

	protected int senseRange = 100;

	public Creature( Vector pos, Vector vel ) {
		position = pos.copy();
		velocity = vel.copy();
		acceleration = new Vector();
	}

	public Vector getPosition() {
		return position;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public abstract void draw( DrawingLayer drawing );

	public abstract void update( List<Creature> creatures );

	public void limitPosition() {
		if( position.x < 0 ) {
			if( Boids.BORDER_WRAP ) {
				position.x = width;
			} else {
				position.x = 0;
				//velocity.mult(-1);
				acceleration.add(Vector.scale(velocity, -2));
			}
		} else if( position.x > width ) {
			if( Boids.BORDER_WRAP ) {
				position.x = 0;
			} else {
				position.x = width;
				//velocity.mult(-1);
				acceleration.add(Vector.scale(velocity, -2));
			}
		}
		if( position.y < 0 ) {
			if( Boids.BORDER_WRAP ) {
				position.y = height;
			} else {
				position.y = 0;
				//velocity.mult(-1);
				acceleration.add(Vector.scale(velocity, -2));
			}
		} else if( position.y > height ) {
			if( Boids.BORDER_WRAP ) {
				position.y = 0.0;
			} else {
				position.y = height;
				//velocity.mult(-1);
				acceleration.add(Vector.scale(velocity, -2));
			}
		}
	}

	protected boolean isInRange( Creature otherCreature ) {
		if( otherCreature == this || otherCreature == null ) {
			return false;
		}
		if( abs(position.x-otherCreature.getPosition().x) > senseRange  ) {
			return false;
		}
		return position.distanceSq(otherCreature.getPosition()) < senseRange*senseRange;
	}
}
