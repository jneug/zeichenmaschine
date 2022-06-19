import schule.ngb.zm.DrawingLayer;
import schule.ngb.zm.Zeichenmaschine;

public class GenWalk extends Zeichenmaschine {

	public static final int N_WALKERS = 8;

	private Walker[] walkers;

	public static void main( String[] args ) {
		new GenWalk();
	}

	public GenWalk() {
		super(800, 800, "Randomwalk");
	}

	@Override
	public void setup() {
		this.walkers = new Walker[N_WALKERS];
		for( int i = 0; i < walkers.length; i++ ) {
			this.walkers[i] = new Walker(width/2, height/2, drawing);
		}

		drawing.clear(0);
	}

	@Override
	public void update( double delta ) {
		for( Walker walker: walkers ) {
			walker.update();
		}
	}

	@Override
	public void draw() {
		drawing.clear(0, 5);
		for( Walker walker: walkers ) {
			walker.draw();
		}
	}

}
