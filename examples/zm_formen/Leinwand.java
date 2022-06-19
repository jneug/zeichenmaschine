import schule.ngb.zm.Drawable;
import schule.ngb.zm.DrawableLayer;
import schule.ngb.zm.Zeichenmaschine;

public class Leinwand {

	private final Zeichenmaschine zm;

    private final DrawableLayer zeichenflaeche;

    public Leinwand() {
		zm = new Zeichenmaschine(800, 800, "ZM: Shapes", false);
        zeichenflaeche = new DrawableLayer();
        zm.addLayer(zeichenflaeche);
    }

    public void anzeigen( Drawable pForm ) {
        zeichenflaeche.add(pForm);
    }

	public void beenden() {
		zm.exit();
	}

	public void zeichnen() {
		zm.redraw();
	}

}
