import schule.ngb.zm.*;

import java.util.List;

public class Boid extends Creature {

	public static final double COHESION_FACTOR = .5;

	public static final double SEPARATION_FACTOR = .5;

	public static final double ALIGNMENT_FACTOR = .5;

	public static final double FLIGHT_FACTOR = .5;

	public static final double VELOCITY_LIMIT = 3.0;

	public static final double FORCE_LIMIT = .1;

	public static final int FLOCK_RADIUS = 100;

	public static final boolean SHOW_RADIUS = false;

	public static final int BOID_WIDTH = 8;

	public static final int BOID_HEIGHT = 16;

	private boolean highlight = false;

	public Boid() {
		// Generate random Boid
		super(
			Vector.random(BOID_WIDTH, width-BOID_WIDTH, BOID_HEIGHT, height-BOID_HEIGHT),
			Vector.random(-1, 1).scale(random(VELOCITY_LIMIT))
		);
		senseRange = FLOCK_RADIUS;
	}

	public Boid( Vector pos, Vector vel ) {
		super(pos, vel);
		senseRange = FLOCK_RADIUS;
	}

	public void toggleHighlight() {
		highlight = !highlight;
	}

	public void draw( DrawingLayer drawing ) {
		drawing.setFillColor(251, 241, 195);
		drawing.setStrokeColor(239, 191, 77);
		drawing.setStrokeWeight(2);

		drawing.pushMatrix();
		drawing.translate(position.x, position.y);
		drawing.rotate(90 + velocity.angle());
		drawing.triangle(BOID_WIDTH / -2.0, BOID_HEIGHT / 2.0, 0, BOID_HEIGHT / -2.0, BOID_WIDTH / 2.0, BOID_HEIGHT / 2.0);

		if( SHOW_RADIUS || highlight ) {
			drawing.setFillColor(251, 241, 195, 33);
			drawing.noStroke();
			drawing.circle(0, 0, FLOCK_RADIUS);
		}
		drawing.popMatrix();
	}


	public void update( List<Creature> creatures ) {
		Vector cohesion = new Vector();
		Vector separation = new Vector();
		Vector alignment = new Vector();
		Vector flight = new Vector();
		int boids = 0, predators = 0;

		for( Creature c: creatures ) {
			if( isInRange(c) ) {
				if( Predator.class.isInstance(c) ) {
					double distSq = position.distanceSq(c.getPosition());
					flight.add(Vector.sub(position, c.getPosition()).div(distSq));

					predators += 1;
				} else {
					cohesion.add(c.getPosition());
					alignment.add(c.getVelocity());

					double distSq = position.distanceSq(c.getPosition());
					if( distSq > 0 ) {
						separation.add(Vector.sub(position, c.getPosition()).div(distSq));
					}

					boids += 1;
				}
			}
		}

		if( boids > 0 ) {
			// Cohesion
			cohesion.div(boids).sub(position);
			cohesion.setLength(VELOCITY_LIMIT).sub(velocity);
			cohesion.limit(FORCE_LIMIT);
			cohesion.scale(COHESION_FACTOR);
			// Separation
			separation.div(boids);
			separation.setLength(VELOCITY_LIMIT).sub(velocity);
			separation.limit(FORCE_LIMIT);
			separation.scale(SEPARATION_FACTOR);
			// Alignment
			alignment.div(boids);
			alignment.setLength(VELOCITY_LIMIT).sub(velocity);
			alignment.limit(FORCE_LIMIT);
			alignment.scale(ALIGNMENT_FACTOR);
		}
		if( predators > 0 ) {
			flight.div(predators);
			flight.setLength(VELOCITY_LIMIT).sub(velocity);
			flight.limit(FORCE_LIMIT*4.0);
			flight.scale(FLIGHT_FACTOR);
		}

		acceleration
			.scale(0.0)
			.add(separation)
			.add(cohesion)
			.add(alignment)
			.add(flight);

		position.add(velocity);
		limitPosition();
		velocity.add(acceleration).limit(VELOCITY_LIMIT);
	}

}
