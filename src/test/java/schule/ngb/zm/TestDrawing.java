package schule.ngb.zm;

public class TestDrawing extends Zeichenmaschine {

	private final double w = 100, h = 50;

	private double xa = 50, ya = 50;

	private double x2 = 200, y2 = 50;

	int frames = 0;
	long start = 0;

	public static void main( String[] args ) {
		new TestDrawing();
	}

	@Override
	public void setup() {
		drawing.setFont("AvenirNext-Medium", 14);
		drawing.setAnchor(NORTHWEST);

		start = System.currentTimeMillis();
	}

	@Override
	public void update( double delta ) {
		ya = (ya+1)%350;
	}

	@Override
	public void draw() {
		long ms = System.currentTimeMillis();

		drawing.clear();

		// x2 = mouseX;
		// y2 = mouseY;

		drawing.resetStroke();
		drawing.rect(xa, ya, w, h);

		//drawing.image("WitchCraftIcons_122_t.PNG", width/2, height/2, .5, CENTER);

		for( int i = 0; i < 4; i++ ) {
			//delay(1000);
			drawing.resetStroke();
			drawing.rect(x2 + i*10, y2 + i * 75, w, h);
			connect(xa + w, ya + h / 2.0, x2 + i*10, y2 + i * 75 + h / 2.0);
			drawing.text("Rect " + i, x2 + i*10 + w / 2.0, y2 + i * 75 + h / 2.0, CENTER);
		}


		if( (System.currentTimeMillis()-start) >= 1000 ) {
			System.out.printf("FPS: %d\n", frames);
			frames = 0;
			start = System.currentTimeMillis();
		} else {
			frames += 1;
		}
	}

	private void connect( double x1, double y1, double x2, double y2 ) {
		double midx = (x1 + x2) / 2.0;
		drawing.setStrokeType(DASHED);
		drawing.setStrokeWeight(2.4);
		drawing.curve(x1, y1, midx, y1, midx, y2, x2, y2);
		//drawing.curve(x1, y1, x2, y1, x1, y2, x2, y2);
	}

}
