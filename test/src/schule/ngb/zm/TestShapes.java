package schule.ngb.zm;

import schule.ngb.zm.shapes.*;
import schule.ngb.zm.shapes.Point;
import schule.ngb.zm.shapes.Polygon;
import schule.ngb.zm.shapes.Rectangle;
import schule.ngb.zm.shapes.Shape;

import java.awt.geom.Point2D;
import java.util.Random;

public class TestShapes extends Zeichenmaschine {

	public static void main( String[] args ) {
		new TestShapes();
	}

	@Override
	public void setup() {
		setSize(400, 400);

		//createShapes();

		//add(new Triangle(200, 150, 150, 250, 250, 250));
		//add(new Rectangle(200, 200, 200, 100));
		//shapes.getShape(0).setAnchor(CENTER);

		shapePositions();
	}

	public void createShapes() {
		add(new Rectangle(20, 10, 40, 20));
		add(new Ellipse(40, 50, 40, 20));
		add(new Circle(40, 80, 10));

		add(new Line(40, 100, 100, 40));
		add(new Arrow(200, 200, 105, 45));

		shapes.add(new Point(200, 200));

		add(new Rhombus(40, 200, 50, 120));
		add(new Kite(40, 200, 50, 120, .75));
		add(new Kite(40, 200, 50, 120, .25));

		add(new RoundedRectangle(20, 300, 100, 60, 30));

		add(new Arc(200, 200, 60, 90, 120));

		add(new Polygon(250, 40, 300, 55, 321, 83, 200, 300));
		shapes.add(new Point(250, 40));
		shapes.add(new Point(300, 55));
		shapes.add(new Point(321, 83));
		shapes.add(new Point(200, 300));

		add(new Triangle(300, 55, 355, 75, 345, 25));
		add(new Quad(300, 55, 355, 75, 345, 25, 300, 10));

		add(new Curve(50, 50, 350, 50, 350, 350));
		add(new Curve(50, 50, 50, 350, 350, 50, 350, 350));

		shapes.add(new Picture(300, 300, "WitchCraftIcons_122_t.PNG"));

		add(new Text(200, 40, "Shapes 😊"));
	}

	public void shapePositions() {
		int pad = 24;
		Rectangle bounds = new Rectangle(pad, pad, width-pad, height-pad);

		Rectangle[] rects = new Rectangle[5];
		for( int i = 0; i < rects.length; i++ ) {
			rects[i] = new Rectangle(40 + i*15, 40 + i*15, 10, 10+i*4);
		}
		shapes.add(rects);

		for( Rectangle r: rects ) {
			r.alignTo(bounds, LEFT);
		}

		//Text t = new TextBox(width/2, height/2, 200, 200, "Hello,\nWorld!");
		//shapes.add(t);
	}

	@Override
	public void update( double delta ) {
		//shapes.getShape(Triangle.class).rotate(2);
		//shapes.getShape(0).rotate(200, 250,2);
	}

	@Override
	public void mouseClicked() {
		for( Shape s : shapes.getShapes() ) {
			randomizeShape(s);
			//s.move(10, 10);
		}
	}

	private void add( Shape s ) {
		shapes.add(randomizeShape(s));
	}

	private Shape randomizeShape( Shape s ) {
		if( !(s instanceof Arc) && !(s instanceof Curve) && !(s instanceof Line) && !(s instanceof Arrow) ) {
			s.setFillColor(randomColor());
		}
		s.setStrokeColor(randomColor());
		s.setStrokeWeight(random(.75, 2.25));
		if( randomBool(20) ) {
			s.setStrokeType(DASHED);
		} else {
			s.setStrokeType(SOLID);
		}

		//s.moveTo(random(20, 380), random(20, 380));

		return s;
	}

	private void showDot( Point2D p ) {
		showDot(p.getX(), p.getY(), randomColor());
	}

	private void showDot( double x, double y, Color clr ) {
		Point p = new Point(x, y);
		p.setFillColor(clr);
		p.setStrokeColor(clr);
		shapes.add(p);
	}

}
