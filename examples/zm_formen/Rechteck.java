import schule.ngb.zm.Color;
import schule.ngb.zm.Drawable;

import java.awt.Graphics2D;

public class Rechteck implements Drawable {

    private int x;

    private int y;

    private int breite;

    private int hoehe;

    private Color farbe;

    private Color linienfarbe;

    private boolean sichtbar;

    public Rechteck() {
        x = 320;
        y = 240;

        breite = 100;
        hoehe = 60;

        farbe = Color.RED;
        linienfarbe = Color.BLACK;

        sichtbar = true;
    }

	public void anzeigen() {
		sichtbar = true;
	}

	public void verstecken() {
		sichtbar = false;
	}

	public void setPosition( int pX, int pY ) {
		x = pX;
		y = pY;
	}

	public void links() {
		x -= 10;
	}

	public void rechts() {
		x += 10;
	}

	public void hoch() {
		y -= 10;
	}

	public void runter() {
		y += 10;
	}

	public void setBreite( int pBreite ) {
		breite = pBreite;
	}

	public void setHoehe( int pHoehe ) {
		hoehe = pHoehe;
	}

	public void setFarbe( String pFarbe ) {
		farbe = Color.parseString(pFarbe);
	}

	public void setLinienfarbe( String pLinienfarbe ) {
		linienfarbe = Color.parseString(pLinienfarbe);
	}

	@Override
	public boolean isVisible() {
		return sichtbar;
	}

	@Override
	public void draw( Graphics2D graphics ) {
		if( x-(breite/2.0) > 0 && x+(breite/2.0) < 800 && y-(hoehe/2.0) > 0 && y+(hoehe/2.0) < 800 ) {
			graphics.setColor(farbe.getJavaColor());
			graphics.fillRect(x, y, breite, hoehe);
			graphics.setColor(linienfarbe.getJavaColor());
			graphics.drawRect(x, y, breite, hoehe);
		}
	}

}
