import schule.ngb.zm.Color;
import schule.ngb.zm.Zeichenmaschine;

public class GenSpiral extends Zeichenmaschine {

	public static final int SIZE = 800;

	public static final double D_SCALE = .33;
	public static final double A_SCALE = 6.0;

	public static Color[] colors = new Color[]{
		color(62, 156, 191, 44),
		color(167, 236, 242, 44),
		color(242, 196, 61, 44),
		color(241, 124, 55, 44),
		color(242, 109, 80, 44)
	};

	public static void main( String[] args ) {
		new GenSpiral();
	}


	private int i = 0;

	public GenSpiral() {
		super(SIZE, SIZE, "Lines");
	}

	@Override
	public void setup() {
		setFramesPerSecond(60);
		drawing.clear(33);
		drawing.translate(SIZE/2, SIZE/2);
	}

	@Override
	public void update( double delta ) {
	}

	@Override
	public void draw() {
		double d = (tick * D_SCALE); // + random(0,3);
		double a = radians(tick * tick / A_SCALE);
		// a = radians(tick * A_SCALE / D_SCALE);
		// a = radians(d * A_SCALE);

		int x = (int)(d * cos(a));
		int y = (int)(d * sin(a));

		drawing.setFillColor(colors[random(colors.length - 1)]);
		drawing.noStroke();
		drawing.circle(x, y, 10);

		if( d > SIZE+SIZE ) {
			stop();
		}
	}

}
