import schule.ngb.zm.Vector;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * Gravitationsquelle in der Simulation.
 * <p>
 * Eine Gravitationsquelle zieht mit einer Anziehungskraft proportional zu
 * seiner Masse alle {@link Mover}-Objekte an. Dabei kommt die Newtonsche
 * Gravitationsformel zur Anwendung.
 * <p>
 * Ein <code>Attractor</code> ist auch ein {@link Mover} und wird von anderen
 * Gravitationsquellen beeinflusst. Dieses Verhalten kann durch Setzen von
 * <code>setMovable(false)</code> abgeschaltet werden.
 */
public class Attractor extends Mover {

	/**
	 * Gravitationskonstante
	 * <p>
	 * Beeinflusst die Stärke der Anziehungskraft der {@link Attractor}en.
	 */
	public static final int G = 25;

	/**
	 * Ob dieser <code>Attractor</code> auch von anderen Kräften beeinflusst wird.
	 */
	private boolean movable = true;

	/**
	 * Erstellt einen <code>Attractor</code> an der angegebenen Position mit der angegebenen
	 * Masse.
	 *
	 * @param pX    x-Koordinate des Objektes.
	 * @param pY    y-Koordinate des Objektes.
	 * @param pMass Masse des Objektes.
	 */
	public Attractor( int pX, int pY, int pMass ) {
		this(pX, pY, pMass, new Vector());
	}

	/**
	 * Erstellt einen <code>Attractor</code> an der angegebenen Position
	 *
	 * @param pX        x-Koordinate des Objektes.
	 * @param pY        y-Koordinate des Objektes.
	 * @param pMass     Masse des Objektes.
	 * @param pVelocity Initialgeschwindigkeit des Objektes.
	 */
	public Attractor( int pX, int pY, int pMass, Vector pVelocity ) {
		super(pX, pY, pVelocity);
		mass = pMass;

		setFillColor(randomColor());
	}

	/**
	 * Stellt ein, ob dieser <code>Attractor</code> auch von anderen Kräften
	 * beeinflusst wird, oder ob er starr an einer Position bleibt.
	 *
	 * @param pMovable <code>true</code> oder <code>false</code>.
	 */
	public void setMovable( boolean pMovable ) {
		this.movable = pMovable;
	}

	/**
	 * Wendet die Anziehungskraft des <code>Attractor</code> auf einen
	 * <code>Mover</code> an.
	 *
	 * @param pMover Das Objekt, das angezogen wird.
	 */
	public void attract( Mover pMover ) {
		if( pMover != this && isActive() ) {
			Vector force = new Vector(this.x, this.y);
			force.sub(pMover.getX(), pMover.getY());
			double v = G * mass / force.lenSq();
			force.setLen(v).limit(1.0, 4 * G);
			pMover.applyForce(force);
		}
	}

	/**
	 * Aktualisiert die momentante Geschwindigkeit und Position des Objektes und
	 * setzt die Beschleunigung zurück.
	 *
	 * @param delta Zeitintervall seit dem letzten Aufruf (in Sekunden).
	 */
	@Override
	public void update( double delta ) {
		if( movable ) {
			super.update(delta);
		}
	}

	@Override
	public void draw( Graphics2D graphics, AffineTransform pVerzerrung ) {
		double m = 2.0*mass;

		AffineTransform at = graphics.getTransform();
		graphics.transform(pVerzerrung);
		graphics.setColor(new java.awt.Color(255,193,64,66));
		graphics.fillOval((int)(-.5*m), (int)(-.5*m), (int)(2*getRadius()+m), (int)(2*getRadius()+m));
		graphics.setTransform(at);

		super.draw(graphics, pVerzerrung);
	}

}
