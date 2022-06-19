import schule.ngb.zm.GraphicsLayer;
import schule.ngb.zm.Zeichenmaschine;

public class ParticleTest extends Zeichenmaschine {

	public static void main( String[] args ) {
		new ParticleTest();
	}

	private NumberParticleEmitter emitter;

	private GraphicsLayer graphics;

	public ParticleTest() {
		super(800,800, "Particles");
	}

	@Override
	public void setup() {
		graphics = new GraphicsLayer();
		canvas.addLayer(graphics);

		emitter = new NumberParticleEmitter(400, 400, 1, 10);
	}

	@Override
	public void update( double delta ) {
		emitter.update(delta);
	}

	@Override
	public void draw() {
		emitter.draw(graphics.getGraphics());
	}

}
