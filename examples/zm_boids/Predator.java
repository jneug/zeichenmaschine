import schule.ngb.zm.DrawingLayer;
import schule.ngb.zm.Vector;

import java.util.List;

public class Predator extends Creature {

	public static final int SENSE_RADIUS = 180;

	public static final double AVOIDANCE_FACTOR = .65;

	public static final double HUNTING_FACTOR = .65;

	public static final double VELOCITY_LIMIT = 5.0;

	public static final double FORCE_LIMIT = .1;

	public static final int PREDATOR_WIDTH = 12;

	public static final int PREDATOR_HEIGHT = 26;

	public static final boolean SHOW_RADIUS = true;

	public Predator() {
		// Generate random Predator
		super(
			Vector.random(PREDATOR_WIDTH, width - PREDATOR_WIDTH, PREDATOR_HEIGHT, height - PREDATOR_HEIGHT),
			Vector.random(-1, 1).scale(random(VELOCITY_LIMIT))
		);
		senseRange = SENSE_RADIUS;
	}

	public Predator( Vector pos, Vector vel ) {
		super(pos, vel);
		senseRange = SENSE_RADIUS;
	}

	public void draw( DrawingLayer drawing ) {
		drawing.setFillColor(152, 61, 83);
		drawing.setStrokeColor(225, 33, 32);
		drawing.setStrokeWeight(2);

		drawing.pushMatrix();
		drawing.translate(position.x, position.y);
		drawing.rotate(90 + velocity.angle());
		drawing.triangle(PREDATOR_WIDTH / -2.0, PREDATOR_HEIGHT / 2.0, 0, PREDATOR_HEIGHT / -2.0, PREDATOR_WIDTH / 2.0, PREDATOR_HEIGHT / 2.0);

		if( SHOW_RADIUS ) {
			drawing.setFillColor(152, 61, 83, 33);
			drawing.noStroke();
			drawing.circle(0, 0, SENSE_RADIUS);
		}
		drawing.popMatrix();
	}

	public void update( List<Creature> creatures ) {
		Vector hunting = new Vector();
		Vector separation = new Vector();
		double boids = 0, predators = 0;

		for( Creature c : creatures ) {
			if( isInRange(c) ) {
				if( Boid.class.isInstance(c) ) {
					hunting.add(c.getPosition());
					boids += 1;
				} else {
					double distSq = position.distanceSq(c.getPosition());
					separation.add(Vector.sub(position, c.getPosition()).div(distSq));

					predators += 1;
				}
			}
		}
		if( boids > 0 ) {
			hunting.div(boids).sub(position);
			hunting.setLength(VELOCITY_LIMIT).sub(velocity);
			hunting.limit(FORCE_LIMIT);
			hunting.scale(HUNTING_FACTOR);
		}
		if( predators > 0 ) {
			separation.div(predators);
			separation.setLength(VELOCITY_LIMIT).sub(velocity);
			separation.limit(FORCE_LIMIT);
			separation.scale(AVOIDANCE_FACTOR);
		}

		acceleration
			.scale(0.0)
			.add(hunting)
			.add(separation);

		position.add(velocity);
		limitPosition();
		velocity.add(acceleration).limit(VELOCITY_LIMIT);
	}

}
