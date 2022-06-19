package schule.ngb.zm.partikel;

import schule.ngb.zm.Updatable;
import schule.ngb.zm.Color;
import schule.ngb.zm.Vector;
import schule.ngb.zm.Drawable;

import java.awt.*;



public class Partikel implements Updatable, Drawable {

	int maxLeben = 0;

	int leben = 0;

	Vector position, geschwindigkeit, beschleunigung;

	Color farbe, farbeStart, farbeEnde;

	Partikel naechster = null;

	double masse = 1.0;

	public Partikel( Vector pPosition ) {
		position = pPosition.copy();
		geschwindigkeit = new Vector();
		beschleunigung = new Vector();
	}

	public Partikel( Vector pPosition, Vector pGeschwindigkeit ) {
		position = pPosition.copy();
		geschwindigkeit = pGeschwindigkeit.copy();
		beschleunigung = new Vector();
	}

	@Override
	public boolean isActive() {
		return leben > 0;
	}

	@Override
	public boolean isVisible() {
		return isActive();
	}

	public void beschleunigen( Vector pBeschleunigung ) {
		beschleunigung.add(Vector.div(pBeschleunigung, masse));
	}

	@Override
	public void update( double delta ) {
		if( isActive() ) {
			geschwindigkeit.add(beschleunigung);
			position.add(Vector.scale(geschwindigkeit, delta));
			beschleunigung.scale(0.0);

			leben -= 1;

			if( farbeStart != null ) {
				double t = 1.0 - (double) leben / (double) maxLeben;
				farbe = Color.interpolate(farbeStart, farbeEnde, t);
			}
		}
	}

	@Override
	public void draw( Graphics2D graphics ) {
		if( isVisible() ) {
			graphics.setColor(farbe.getJavaColor());
			graphics.fillOval((int) position.x, (int) position.y, 4, 4);
		}
	}
}
