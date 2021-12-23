package schule.ngb.zm;

import schule.ngb.zm.turtle.Turtleebene;

public class TestTurtle extends Zeichenfenster {

    public static void main(String[] args) {
        new TestTurtle();
    }

    Turtleebene turtle;

    public void einstellungen() {
        turtle = new Turtleebene();
        hinzu(turtle);
    }

	public void neck( int n, int s, Farbe f ) {
		for( int i = 0; i < n; i++ ) {
			turtle.vor(s);
			turtle.rechts( 360.0 / n );
		}
		turtle.setFuellfarbe(f);
		turtle.fuellen();
	}

    public void vorbereiten() {
		turtle.setKonturDicke(2.5);
		neck(7, 50, Farbe.HGGRUEN);

        /*
        turtle.vor(100);
        turtle.links(135);
        turtle.stiftHoch();
        turtle.vor(100);
        turtle.rechts();
        turtle.stiftRunter();
        turtle.setKonturFarbe(50, 190, 33);
        turtle.vor(100);
        turtle.links();
        turtle.setKonturDicke(4);
        //turtle.setKonturArt(Turtleebene.Turtle.GEPUNKTET);
        turtle.vor(50);
		*/

        /*Turtleebene.Turtle t = turtle.neueTurtle();
        t.setKonturDicke(1.5);
        for( int i = 0; i < 360; i++ ) {
            t.vor(1);
            t.rechts(1);
        }*/
    }

}
