import schule.ngb.zm.DrawableLayer;
import schule.ngb.zm.Zeichenmaschine;

public class TIXY  extends Zeichenmaschine {

	public static final int FIELD_SIZE = 16;

	public static final int DOT_SIZE = 16;

	public static final int DOT_GAP = 4;


	private Dot[][] dots;

	private MyTIXY tixy;

	public TIXY() {
		super(
			DOT_GAP + FIELD_SIZE * (DOT_SIZE + DOT_GAP),
			DOT_GAP + FIELD_SIZE * (DOT_SIZE + DOT_GAP),
			"tixy"
		);
	}

	@Override
	public void setup() {
		background.setColor(BLACK);

		addLayer(new DrawableLayer());
		DrawableLayer dl = canvas.getLayer(DrawableLayer.class);

		tixy = new MyTIXY();

		dots = new Dot[FIELD_SIZE][FIELD_SIZE];
		for( int i = 0; i < FIELD_SIZE; i++ ) {
			for( int j = 0; j < FIELD_SIZE; j++ ) {
				dots[i][j] = new Dot(i, j, i*FIELD_SIZE + j);
				dl.add(dots[i][j]);
			}
		}
	}

	@Override
	public void draw() {
	}

	@Override
	public void update( double delta ) {
		for( int i = 0; i < FIELD_SIZE; i++ ) {
			for( int j = 0; j < FIELD_SIZE; j++ ) {
				Dot d = dots[i][j];
				double value = tixy.update(runtime/1000.0, d.i, d.x, d.y);
				dots[i][j].setValue(value);

				tixy.update(runtime/1000.0, dots[i][j]);
			}
		}
	}

	public static void main( String[] args ) {
		new TIXY();
	}

}
