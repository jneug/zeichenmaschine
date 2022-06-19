import schule.ngb.zm.Zeichenmaschine;

public class Eyes extends Zeichenmaschine {

	public static final int N_EYES = 30;

	public static void main( String[] args ) {
		new Eyes();
	}

	Eye[] eyes;

	public Eyes() {
		super(600, 600, "Eyes");
	}

	@Override
	public void setup() {
		eyes = new Eye[N_EYES];
		for( int i = 0; i < eyes.length; i++ ) {
			eyes[i] = new Eye();
		}
	}

	@Override
	public void update( double delta ) {

	}

	@Override
	public void draw() {
		drawing.clear(200);

		for( int i = 0; i < eyes.length; i++ ) {
			eyes[i].draw(drawing);
		}
	}
}
