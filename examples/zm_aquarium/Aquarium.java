import schule.ngb.zm.Zeichenmaschine;
import schule.ngb.zm.util.ImageLoader;

public class Aquarium extends Zeichenmaschine {

	public static final int N_FISHES = 25;

	public static void main( String[] args ) {
		new Aquarium();
	}

	private Fish[] fish;

	public Aquarium() {
		super(800, 600, "Aquarium");
	}

	@Override
	public void setup() {
		canvas.addLayer(1, new Background());

		fish = new Fish[N_FISHES];

		for( int i = 1; i <= 7; i++ ) {
			ImageLoader.preloadImage("fish"+i, "tiles/fish"+i+"gs.png");
		}

		for( int i = 0; i < N_FISHES; i++ ) {
			fish[i] = new Fish();
			shapes.add(fish[i]);
		}
	}

	@Override
	public void update( double delta ) {
		for( int i = 0; i < fish.length; i++ ) {
			fish[i].update(delta);
		}
	}

	@Override
	public void draw() {

	}

	@Override
	public void mouseClicked() {
		for( int i = 0; i < fish.length; i++ ) {
			fish[i].randomize();
		}
	}

}
