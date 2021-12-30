package schule.ngb.zm;

import schule.ngb.zm.formen.*;
import schule.ngb.zm.formen.Point;
import schule.ngb.zm.formen.Polygon;
import schule.ngb.zm.formen.Rectangle;
import schule.ngb.zm.formen.Shape;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

public class TestFormen extends Zeichenmaschine {

	Random rand = new Random();

	public static void main( String[] args ) {
		new TestFormen();
	}

	@Override
	public void setup() {
		setSize(400, 400);

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

	@Override
	public void update( double delta ) {
	}

	@Override
	public void mouseClicked() {
		for( Shape s : shapes.getShapes() ) {
			randomizeShape(s);
			s.move(10, 10);
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

	private Color randomColor() {
		return new Color(
			rand.nextInt(256),
			rand.nextInt(256),
			rand.nextInt(256),
			128
		);
	}

}
