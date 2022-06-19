import schule.ngb.zm.DrawableLayer;
import schule.ngb.zm.Zeichenmaschine;
import schule.ngb.zm.partikel.PartikelGenerator;

public class PartikelBeispiel extends Zeichenmaschine {

	public static void main( String[] args ) {
		new PartikelBeispiel();
	}

	public PartikelBeispiel() {
		super(400, 400, "ZM: Partikel");
	}

	PartikelGenerator pgen;

	public void setup() {
		pgen = new PartikelGenerator(200, 200, 3, 20);
		DrawableLayer drawables = new DrawableLayer();
		addLayer(drawables);
		drawables.add(pgen);
		pgen.starten();
	}

	@Override
	public void update( double delta ) {
		pgen.update(delta);
	}

}
