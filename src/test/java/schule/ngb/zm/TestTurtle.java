package schule.ngb.zm;

import schule.ngb.zm.layers.TurtleLayer;
import schule.ngb.zm.layers.TurtleLayer.Turtle;

public class TestTurtle extends Zeichenmaschine {

	public static void main( String[] args ) {
		new TestTurtle();
	}

	TurtleLayer turtle;

	TurtleLayer.Turtle t1, t2;

	public void setup() {
		turtle = new TurtleLayer();
		addLayer(turtle);

		t1 = turtle.getTurtle();
		t2 = turtle.newTurtle();

		t1.setStrokeWeight(2.0);
		t2.setStrokeWeight(1.4);

		t1.setFillColor(244, 64, 22);
		t2.setFillColor(33, 64, 255);
	}

	@Override
	public void update( double delta ) {
		//t1.right(1);
		t1.penUp();
		t1.moveTo(100, 100);
		t1.penDown();

		t2.penUp();
		t2.moveTo(200, 200);
		t2.penDown();
	}

	public void draw() {
		turtle.clear();
		star(5, 80, 40, t1);
		star(5, 80, 40, t2);
	}

	void star( int corners, int size ) {
		star(5, size, 40, t1);
	}

	void star( int corners, int size, int angle, Turtle t ) {
		int angle2 = (180 - (360/corners) - angle) / 2;

		t.penUp();
		t.left();
		t.fd(size*1.25);
		t.right();
		t.penDown();

		for( int i = 0; i < corners; i++ ) {
			t.right(360/corners);
			t.right(angle2);
			t.fd(size);
			t.left(2*angle2);
			t.fd(size);
			t.right(angle2);
		}
		t.fill();

		t.penUp();
		t.right();
		t.fd(size*1.25);
		t.left();
		t.penDown();
	}

}
