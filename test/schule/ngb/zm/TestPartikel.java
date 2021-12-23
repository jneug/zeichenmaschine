package schule.ngb.zm;

import schule.ngb.zm.partikel.PartikelGenerator;

import java.awt.*;

public class TestPartikel extends Zeichenfenster {

	public static void main( String[] args ) {
		new TestPartikel();
	}

	PartikelGenerator[] pg = new PartikelGenerator[3];

	public void vorbereiten() {
		setSize(400,400);

		int w = 4, pl = 360;
		Farbe f1 = new Farbe(Color.GREEN, 128), f2 = new Farbe(Color.YELLOW, 0);
		Vektor r = new Vektor(-32, 10);
		int ppf = 2;

		pg[0] = new PartikelGenerator(100,200, pl, ppf);
		pg[0].zufallsfaktor = 0.0;

		pg[1] = new PartikelGenerator(200,200, pl, ppf);
		pg[1].zufallsfaktor = 0.5;

		pg[2] = new PartikelGenerator(300,200, pl, ppf);
		pg[2].zufallsfaktor = 1.0;

		/*for(PartikelGenerator p: pg) {
			p.winkel = w;
			p.farbeStart = f1;
			p.farbeEnde = f2;
			p.richtung = r;

			drawing.hinzu(p);
			p.starten();
		}*/
	}

	public void zeichnen() {
		for(PartikelGenerator p: pg) {
			p.aktualisieren(delta);
		}
	}

}
