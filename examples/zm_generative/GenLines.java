import schule.ngb.zm.Color;
import schule.ngb.zm.Zeichenmaschine;

public class GenLines extends Zeichenmaschine {

	public static final int BORDER = 16;

	public static final int SIZE = 400;

	public static Color[] colors = new Color[]{
		color(62, 156, 191),
		color(167, 236, 242),
		color(242, 196, 61),
		color(241, 124, 55),
		color(242, 109, 80)
	};


	public static void main( String[] args ) {
		new GenLines();
	}


	private int i = 0;

	public GenLines() {
		super(SIZE, SIZE, "Lines");
	}

	@Override
	public void setup() {
		setFramesPerSecond(10);
		drawing.clear(33);
	}

	@Override
	public void update( double delta ) {
	}

	@Override
	public void draw() {
		int a = random(BORDER, SIZE-BORDER);

		//drawing.setStrokeColor(random(50, 200));
		drawing.setStrokeColor(colors[random(colors.length-1)]);
		drawing.setStrokeWeight(random(4,12));

		int d;
		if( a > SIZE*0.5 ) {
			d = random(1, (SIZE - a) - BORDER);
		} else {
			d = random(1, a - BORDER);
		}
		drawing.line(a - d, a + d, a + d, a - d);

		i += 1;
		if( i == SIZE ) {
			stop();
		}
	}

}
