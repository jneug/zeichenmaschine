import schule.ngb.zm.Updatable;
import schule.ngb.zm.Vector;
import schule.ngb.zm.shapes.Arrow;
import schule.ngb.zm.shapes.Circle;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * Ein bewegliches Objekt in der Simulation.
 *
 * <code>Mover</code> werden durch Kräfte beeinflusst. Diese Kräfte können von
 * unterschiedlichen Quellen ausgehen. Die {@link Attractor}en wirken zum
 * Beispiel mit ihrer Anziehungskraft auf die <code>Mover</code> ein.
 */
public class Mover extends Circle implements Updatable {

	/**
	 * Größe der Objekte in der Simulation.
	 */
	public static final int SIZE = 10;

	/**
	 * Ob die momentane Geschwindigkeit der Objekte als Pfeil dargestellt werden soll.
	 */
	public static final boolean SHOW_VELOCITY = false;


	/**
	 * Masse des Objektes.
	 */
	protected double mass = 10.0;

	/**
	 * Ob dieses Objekt an der Simulation beteiligt ist.
	 */
	protected boolean active = true;

	/**
	 * Momentane Geschwindigkeit des Objektes.
	 */
	private Vector velocity;

	/**
	 * Momentane Beschleunigung des Objektes.
	 */
	private Vector acceleration = new Vector();

	/**
	 * Erstellt einen Mover an der angegebenen Position mit der momentanen
	 * Geschwindigkeit <code>(0, 0)</code>.
	 *
	 * @param pX x-Position des Objektes.
	 * @param pY y-Position des Objektes.
	 */
	public Mover( int pX, int pY ) {
		this(pX, pY, new Vector());
	}


	/**
	 * Erstellt einen <code>Attractor</code> an der angegebenen Position mit der angegebenen
	 * Masse.
	 *
	 * @param pX    x-Koordinate des Objektes.
	 * @param pY    y-Koordinate des Objektes.
	 * @param pMass Masse des Objektes.
	 */
	public Mover( int pX, int pY, int pMass ) {
		this(pX, pY, pMass, new Vector());
	}

	/**
	 * Erstellt einen Mover an der angegebenen Position mit der angegebenen
	 * Initialgeschwindigkeit.
	 *
	 * @param pX        x-Position des Objektes.
	 * @param pY        y-Position des Objektes.
	 * @param pVelocity Momentane Geschwindigkeit des Objektes.
	 */
	public Mover( int pX, int pY, Vector pVelocity ) {
		super(pX, pY, SIZE);
		velocity = pVelocity.copy();
	}

	/**
	 * Erstellt einen <code>Attractor</code> an der angegebenen Position
	 *
	 * @param pX        x-Koordinate des Objektes.
	 * @param pY        y-Koordinate des Objektes.
	 * @param pMass     Masse des Objektes.
	 * @param pVelocity Initialgeschwindigkeit des Objektes.
	 */
	public Mover( int pX, int pY, int pMass, Vector pVelocity ) {
		super(pX, pY, SIZE);
		mass = pMass;
		velocity = pVelocity.copy();
	}

	/**
	 * Gibt die Masse des Objektes zurück.
	 *
	 * @return Die Masse des Objektes.
	 */
	public double getMass() {
		return mass;
	}

	/**
	 * Gibt die momentane Geschwindigkeit zurück.
	 * @return Die momentane Geschwindigkeit als Vektor.
	 */
	public Vector getVelocity() {
		return velocity;
	}

	/**
	 * Setzt die momentane Geschwindigkeit des Objektes.
	 *
	 * @param pDx Momentane Geschwindigkeit in x-Richtung.
	 * @param pDy Momentane Geschwindigkeit in y-Richtung.
	 */
	public void setVelocity( double pDx, double pDy ) {
		velocity.set(pDx, pDy);
	}

	/**
	 * Addiert eine Kraft zur momentanen Beschleunigung des Objektes. Die
	 * Beschleunigung wird einmal pro Update auf die Geschwindigkeit angewandt
	 * und dann wieder auf <code>(0, 0)</code> gesetzt.
	 *
	 * @param force Ein Vektor, der die Kraft darstellt.
	 */
	public void applyForce( Vector force ) {
		acceleration.add(force);
	}

	/**
	 * Ob dieses Objekt aktiv ist und Updates erhalten soll.
	 *
	 * @return Ob das Objekt simuliert wird.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Aktiviert / Deaktiviert das Objekt.
	 * @param pActive
	 */
	public void setActive( boolean pActive ) {
		this.active = pActive;
	}

	/**
	 * Aktualisiert die momentante Geschwindigkeit und Position des Objektes und
	 * setzt die Beschleunigung zurück.
	 *
	 * @param delta Zeitintervall seit dem letzten Aufruf (in Sekunden).
	 */
	public void update( double delta ) {
		acceleration.scale(delta);
		velocity.add(acceleration);
		acceleration.scale(0.0);

		this.x += velocity.x;
		this.y += velocity.y;
	}

	@Override
	public void draw( Graphics2D graphics, AffineTransform pVerzerrung ) {
		if( SHOW_VELOCITY ) {
			Vector v = velocity.copy().scale(10);
			Arrow ar = new Arrow(v);

			AffineTransform af = new AffineTransform(pVerzerrung);
			af.translate(radius, radius);
			ar.draw(graphics, af);
		}

		super.draw(graphics, pVerzerrung);
	}

}
