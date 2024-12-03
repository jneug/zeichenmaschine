package schule.ngb.zm.particles;

import schule.ngb.zm.Zeichenmaschine;
import schule.ngb.zm.layers.DrawableLayer;

public class ParticleExample extends Zeichenmaschine {

	public static void main( String[] args ) {
		new ParticleExample();
	}

	public ParticleExample() {
		super(400, 400, "ZM: Particles");
	}

	ParticleEmitter pgen;

	public void setup() {
		pgen = new ParticleEmitter(
			200, 200, 1,
			new BasicParticleFactory()
		);
		pgen.randomness = .5;
		pgen.angle = 45;
		DrawableLayer drawables = new DrawableLayer();
		addLayer(drawables);
		drawables.add(pgen);
		pgen.start();
	}

	@Override
	public void update( double delta ) {
		pgen.update(delta);
	}

}
