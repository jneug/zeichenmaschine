
import schule.ngb.zm.*;
import schule.ngb.zm.formen.*;

public class Mover extends Circle implements Updatable {

    private Vector velocity;
    private Vector acceleration = new Vector();

    public Mover( int x, int pY ) {
        this(x, pY, new Vector());
    }

    public Mover( int x, int pY, Vector pVelocity ) {
        super(x, pY, 10);
        this.velocity = pVelocity.copy();
    }

    public void applyForce( Vector force ) {
        acceleration.add(force);
    }

    public boolean isActive() {
        return true;
    }

    public void update( double delta ) {
        acceleration.scale(delta);
        velocity.add(acceleration);
        acceleration.scale(0.0);

        this.x += velocity.x;
        this.y += velocity.y;


    }

}
