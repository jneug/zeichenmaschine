import schule.ngb.zm.Color;
import schule.ngb.zm.Drawable;

import java.awt.Graphics2D;

public class Kreis implements Drawable {

    private int x;

    private int y;

    private int radius;

    private Color farbe;

    private Color linienfarbe;

    private boolean sichtbar;

    public Kreis() {
        x = 420;
        y = 420;

        radius = 80;

        farbe = Color.BLUE;
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

    public void setRadius( int pRadius ) {
        radius = pRadius;
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
        if( x-(radius/2.0) > 0 && x+(radius/2.0) < 800 && y-(radius/2.0) > 0 && y+(radius/2.0) < 800 ) {
            graphics.setColor(farbe.getJavaColor());
            graphics.fillOval(x, y, radius, radius);
            graphics.setColor(linienfarbe.getJavaColor());
            graphics.drawOval(x, y, radius, radius);
        }
    }

}
