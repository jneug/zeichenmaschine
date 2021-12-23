package schule.ngb.zm.formen;

import java.awt.*;

public abstract class Fuellform extends Konturform {

    public static final Color STD_FUELLFARBE = Color.WHITE;

    protected Color fuellFarbe = STD_FUELLFARBE;


    public Color getFuellfarbe() {
        return fuellFarbe;
    }

    public void setFuellfarbe( Color pFuellFarbe) {
        fuellFarbe = pFuellFarbe;
    }

    public void keineFuellung() {
        fuellFarbe = null;
    }

    public void setFuellfarbe( int gray) {
        setFuellfarbe(gray, gray, gray, 255);
    }

    public void setFuellfarbe( int gray, int alpha) {
        setFuellfarbe(gray, gray, gray, alpha);
    }

    public void setFuellfarbe( int red, int green, int blue) {
        setFuellfarbe(red, green, blue, 255);
    }

    public void setFuellfarbe( int red, int green, int blue, int alpha) {
        if (red < 0 || red >= 256)
            throw new IllegalArgumentException("red must be between 0 and 255");
        if (green < 0 || green >= 256)
            throw new IllegalArgumentException("green must be between 0 and 255");
        if (blue < 0 || blue >= 256)
            throw new IllegalArgumentException("blue must be between 0 and 255");
        if (alpha < 0 || alpha >= 256)
            throw new IllegalArgumentException("alpha must be between 0 and 255");

        setFuellfarbe(new Color(red, green, blue, alpha));
    }

}
