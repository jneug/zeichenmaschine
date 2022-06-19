import schule.ngb.zm.Vector;
import schule.ngb.zm.Zeichenmaschine;

import java.util.LinkedList;

/**
 * Hauptklasse der Simulation.
 */
public class Gravity extends Zeichenmaschine {

	/**
	 * Liste der beweglichen Objekte in der Welt.
	 */
	private LinkedList<Mover> movers = new LinkedList<>();

	private final Vector gravity = new Vector(0, 6.734);

	/**
	 * Erstellt die {@link Mover}-Objekte.
	 */
	public void setup() {
		for( int i = 0; i < 4; i++ ) {
			//Mover m = new Mover(random(10, width - 10), 30);
			Mover m = new Mover(10 + (i*30), 30, 5*(i+1));
			movers.add(m);
			shapes.add(m);
		}
	}

	/**
	 * Aktualisiert die Beschleunigung der {@link Mover}-Objekte durch Anwenden
	 * der einwirkenden Kräfte und aktualisiert dann die Position entsprechend
	 * der Beschleunigung.
	 * <p>
	 * Die Position des ersten {@link Attractor} wird auf die Mausposition
	 * gesetzt.
	 *
	 * @param delta
	 */
	public void update( double delta ) {
		// Kräfte anwenden
		for( Mover m : movers ) {
			if( m.isActive() ) {
				m.applyForce(gravity);
			}
		}

		// Position aktualisieren
		for( Mover m : movers ) {
			if( m.isActive() ) {
				m.update(delta);

				// Abprallen Boden
				if( m.getY() >= height ) {
					m.setY(height-1);

					m.getVelocity().y *= -1;
					double s = 9.0 / m.getMass();
					m.getVelocity().scale(limit(s, 0.0, 1.0));
				}
				// Abprallen rechter Rand
				if( m.getX() >= width ) {
					m.setX(width-1);
					m.getVelocity().x *= -1;
				}
				// Abprallen linker Rand
				if( m.getX() <= 0) {
					m.setX(1);
					m.getVelocity().x *= -1;
				}
			}
		}
	}

	/**
	 * Setzt die Position und Beschleunigung aller {@link Mover}-Objekte in der
	 * Welt zurück.
	 */
	public void mouseClicked() {
		background.setColor(randomNiceColor());
		for( Mover m : movers ) {
			m.moveTo(random(10, width - 10), 30);
			m.setVelocity(mouseX-m.getX(), mouseY-m.getY());
			m.getVelocity().setLength(4.0);
		}
	}

	public static void main( String[] args ) {
		new Gravity();
	}

}
