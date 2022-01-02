import schule.ngb.zm.Color;
import schule.ngb.zm.Zeichenmaschine;
import schule.ngb.zm.turtle.TurtleLayer;

public class Skyline extends Zeichenmaschine {

	TurtleLayer turtle;

	public void setup() {
		setSize(1000, 600);
		setTitle("Zeichenmaschine: Skyline");

		// Turtle-Ebene erstellen
		turtle = new TurtleLayer();
		addLayer(turtle);

		// Hintergrund schwarz
		background.setColor(BLACK);
		turtle.noStroke();

		turtle.penUp();
		turtle.lt(90);
		turtle.fd(300);
		turtle.rt(90);
		turtle.penDown();
	}

	public void draw() {
		for( int i = 0; i < 6; i++ ) {
			haus1(randomColor());
		}
		turtle.setStrokeColor(BLUE);
	}

	public void haus1( Color clr ) {
		turtle.setFillColor(clr);
		quad(100, 80);
		turtle.fill();
		turtle.fd(100);
		turtle.rt(45);
		turtle.fd(80/sqrt(2));
		turtle.rt(90);
		turtle.fd(80/sqrt(2));
		turtle.rt(45);
		turtle.fill();
		turtle.fd(100);
		turtle.rt(180);
	}

	public void quad( int a, int b ) {
		turtle.fd(a);
		turtle.rt(90);
		turtle.fd(b);
		turtle.rt(90);
		turtle.fd(a);
		turtle.rt(90);
		turtle.fd(b);
		turtle.rt(90);
	}

	public static void main( String[] args ) {
		new Skyline();
	}

}
