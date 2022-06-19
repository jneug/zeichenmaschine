import schule.ngb.zm.Color;
import schule.ngb.zm.DrawingLayer;
import schule.ngb.zm.Vector;
import schule.ngb.zm.Zeichenobjekt;

public class Eye extends Zeichenobjekt {

	private Vector position;

	private double size;

	private Color color;

	public Eye() {
		position = Vector.random(0, width, 0, height);
		size = random(10.0, 25.0);
		color = null;
	}

	public Eye( float x, float y ) {
		position = new Vector(x, y);
		size = random(10.0, 25.0);
		color = null;
	}

	public Eye( float x, float y, Color color ) {
		position = new Vector(x, y);
		size = random(10.0, 25.0);
		this.color = color;
	}

	@Override
	public void draw( DrawingLayer drawing ) {
		Vector dir = Vector.sub(new Vector(mouseX, mouseY), position);
		double len = dir.length();

		drawing.setStrokeColor(0);
		if( color == null ) {
			drawing.setFillColor(colorHsb(354, 100.0 - limit(len, 0.0, 100.0), 100));
		} else {
			drawing.setFillColor(color);
		}
		drawing.circle(position.x, position.y, size);


		Vector pupil = Vector.add(position, dir.limit(size*.4));

		drawing.setFillColor(0);
		drawing.circle(pupil.x, pupil.y, size*.4);
	}
}
