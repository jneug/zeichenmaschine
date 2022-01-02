
import schule.ngb.zm.*;

public class Attractor extends Mover {

    private int mass = 0;

    public Attractor( int x, int pY, int pMass ) {
        this(x, pY, pMass, new Vector());
    }

    public Attractor( int x, int pY, int pMass, Vector pVelocity ) {
        super(x, pY, pVelocity);
        mass = pMass;

        setFillColor(YELLOW);
    }

    public void attract( Mover pMover ) {
        if( pMover != this ) {
            Vector force = new Vector(this.x, this.y);
            force.sub(pMover.getX(), pMover.getY()).scale(mass*Gravity.G).limit(0, 50*Gravity.G);
            pMover.applyForce(force);
        }
    }

}
