import schule.ngb.zm.Zeichenmaschine;

import java.util.LinkedList;

public class Atoms extends Zeichenmaschine {

	/**
	 * Liste der beweglichen Objekte in der Welt.
	 */
	private LinkedList<Mover> movers = new LinkedList<>();

	/**
	 * Liste der Gravitationsquellen in der Welt.
	 */
	private LinkedList<Attractor> attractors = new LinkedList<>();

	/**
	 * Erstellt die {@link Mover}- und {@link Attractor}-Objekte.
	 */
	public void setup() {
		addAttractor( width / 2, height / 2, 20 );
		attractors.getFirst().setMovable(false);
		attractors.getFirst().setActive(false);
		attractors.getFirst().hide();

		addAttractor( width*.4, height*.4, 8 );
		addAttractor( width*.6, height*.6, 8 );

		for( int i = 0; i < 10; i++ ) {
			Mover m = new Mover(random(10, width - 10), random(10, height - 10));
			movers.add(m);
			shapes.add(m);
		}
	}

	private void addAttractor( double pX, double pY, int pMass ) {
		Attractor a = new Attractor((int)pX, (int)pY, pMass);
		attractors.add(a);
		movers.add(a);
		shapes.add(a);
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
		// Erste Gravitationsquelle auf Mausposition setzen.
		Attractor mouseFollow = attractors.get(0);
		if( mouseFollow.isActive() ) {
			mouseFollow.moveTo(mouseX, mouseY);
		}

		// Kräfte anwenden
		for( Attractor a : attractors ) {
			if( a.isActive() ) {
				for( Mover m : movers ) {
					if( m.isActive() ) {
						a.attract(m);
					}
				}
			}
		}

		// Position aktualisieren
		for( Mover m : movers ) {
			if( m.isActive() ) {
				m.update(delta);
			}
		}
	}

	/**
	 * Setzt die Position und Beschleunigung aller {@link Mover}-Objekte in der
	 * Welt zurück.
	 */
	public void mouseClicked() {
		for( Mover m : movers ) {
			m.moveTo(random(10, width - 10), random(10, height - 10));
			m.setVelocity(0, 0);
		}
	}

	public static void main( String[] args ) {
		new Atoms();
	}

}
