
import java.util.LinkedList;

import schule.ngb.zm.*;

public class Gravity extends Zeichenmaschine {
    
    
    public static final int G = 1;
    
    
    private LinkedList<Mover> movers = new LinkedList<>();
    
    private LinkedList<Attractor> attractors = new LinkedList<>();
    
    public void setup(){
         for( int i = 0; i < 10; i++ ) {
             Mover m = new Mover(random(10, width-10), random(10, height-10));
             movers.add(m);
             shapes.add(m);
         }
         
         attractors.add(new Attractor(width/2, height/2, 10));
         shapes.add(attractors.get(0));
    }
    
    public void update( double delta ) {
        Attractor mouseFollow = attractors.get(0);
        mouseFollow.moveTo(mouseX, mouseY);
        
        for( Attractor a: attractors ) {
            for( Mover m: movers ) {
                a.attract(m);
            }
        }
        
        for( Mover m: movers ) {
            m.update(delta);
        }
        shapes.clear();
    }
    
    public void mouseClicked() {
        for( Mover m: movers ) {
            m.moveTo(random(10, width-10), random(10, height-10));
            m.setVelocity(0, 0);
        }
    }
    
}
