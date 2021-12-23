package schule.ngb.zm;

import schule.ngb.zm.formen.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

public class TestFormen extends Zeichenfenster {
	public static void main( String[] args ) {
		new TestFormen();
	}


	Kurve k;

	@Override
	public void vorbereiten() {
		setSize(400, 400);

		k = new Kurve(50, 50, 100, 50, 100, 100,150, 100);
		//formen.anzeigen(new Kurve(50, 50, 100, 50, 150, 100));
		formen.anzeigen(k);

		k.verschiebeNach(200, 200);
		k.skalieren(1.1);

		showDot(k.getStartpunkt());
		showDot(k.getKontrollpunkt1());
		showDot(k.getKontrollpunkt2());
		showDot(k.getEndpunkt());
	}

	@Override
	public void aktualisieren( double delta ) {

	}

	@Override
	public void zeichnen() {
		formen.leeren();
	}

	private void showDot( Point2D p ) {
		showDot(p.getX(), p.getY(), randomColor());
	}
	private void showDot( double x, double y, Color clr ) {
		Punkt p = new Punkt(x, y);
		p.setFuellfarbe(clr);
		p.setKonturFarbe(clr);
		formen.anzeigen(p);
	}

	Random rand = new Random();

	private Color randomColor() {
		return new Color(
			rand.nextInt(256),
			rand.nextInt(256),
			rand.nextInt(256),
			128
		);
	}

}
