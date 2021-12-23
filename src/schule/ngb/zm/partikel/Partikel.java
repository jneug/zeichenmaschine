package schule.ngb.zm.partikel;

import schule.ngb.zm.Aktualisierbar;
import schule.ngb.zm.Farbe;
import schule.ngb.zm.Vektor;
import schule.ngb.zm.Zeichenbar;

import java.awt.*;



public class Partikel implements Aktualisierbar, Zeichenbar {

	int maxLeben = 0;

	int leben = 0;

	Vektor position, geschwindigkeit, beschleunigung;

	Color farbe, farbeStart, farbeEnde;

	Partikel naechster = null;

	double masse = 1.0;

	public Partikel( Vektor pPosition ) {
		position = pPosition.kopie();
		geschwindigkeit = new Vektor();
		beschleunigung = new Vektor();
	}

	public Partikel( Vektor pPosition, Vektor pGeschwindigkeit ) {
		position = pPosition.kopie();
		geschwindigkeit = pGeschwindigkeit.kopie();
		beschleunigung = new Vektor();
	}

	@Override
	public boolean istAktiv() {
		return leben > 0;
	}

	@Override
	public boolean istSichtbar() {
		return istAktiv();
	}

	public void beschleunigen( Vektor pBeschleunigung ) {
		beschleunigung.addieren(Vektor.dividieren(pBeschleunigung, masse));
	}

	@Override
	public void aktualisieren( double delta ) {
		if( istAktiv() ) {
			geschwindigkeit.addieren(beschleunigung);
			position.addieren(Vektor.skalieren(geschwindigkeit, delta));
			beschleunigung.skalieren(0.0);

			leben -= 1;

			if( farbeStart != null ) {
				double t = 1.0 - (double) leben / (double) maxLeben;
				farbe = Farbe.morphen(farbeStart, farbeEnde, t);
			}
		}
	}

	@Override
	public void zeichnen( Graphics2D graphics ) {
		if( istSichtbar() ) {
			graphics.setColor(farbe);
			graphics.fillOval((int) position.x, (int) position.y, 4, 4);
		}
	}
}
