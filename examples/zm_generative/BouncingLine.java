import schule.ngb.zm.Vector;
import schule.ngb.zm.Zeichenmaschine;

public class BouncingLine extends Zeichenmaschine {

	public static void main( String[] args ) {
		new BouncingLine();
	}

	private Vector start, end, dStart, dEnd;

	private int r, g, b;

	private boolean mouseFollow = false;

	public BouncingLine() {
		super(800, 800, "Bouncing Lines");
	}

	@Override
	public void setup() {
		start = Vector.random(0, width, 0, height);
		end = Vector.random(0, width, 0, height);
		dStart = Vector.random(-5, 5);
		dEnd = Vector.random(-5, 5);

		r = random(255);
		g = random(255);
		b = random(255);
	}

	@Override
	public void update( double delta ) {
		start.add(dStart);
		end.add(dEnd);

		if( start.x > width || start.x < 0 ) {
			dStart.x *= -1;
		}
		if( end.x > width || end.x < 0 ) {
			dEnd.x *= -1;
		}
		if( start.y > width || start.y < 0 ) {
			dStart.y *= -1;
		}
		if( end.y > width || end.y < 0 ) {
			dEnd.y *= -1;
		}

		r += limit(random(-5, 5), 0, 255);
		g += limit(random(-5, 5), 0, 255);
		b += limit(random(-5, 5), 0, 255);
	}

	@Override
	public void draw() {
		drawing.setStrokeColor(r, g, b);
		drawing.line(start.x, start.y, end.x, end.y);
		// bezier(0, 0, startX, startY, endX, endY, width, height);
	}

	@Override
	public void mouseClicked() {
		mouseFollow = !mouseFollow;
	}

	@Override
	public void mouseMoved() {
		if( mouseFollow ) {
			dStart = new Vector(mouseX - start.x, mouseY - start.y);
			dStart.setLength(5);
			dEnd = new Vector(mouseX - end.x, mouseY - end.y);
			dEnd.setLength(5);
		}
	}

}
