package schule.ngb.zm.formen;

import schule.ngb.zm.Konstanten;
import schule.ngb.zm.Zeichenbar;

import java.awt.*;

public abstract class Konturform extends Konstanten implements Zeichenbar {

    public static final int DURCHGEZOGEN = 16;
    public static final int GESTRICHELT = 17;
    public static final int GEPUNKTET = 18;

    public static final Color STD_KONTURFABRE = Color.BLACK;
    public static final double STD_KONTURDICKE = 1.0;


    protected Color konturFarbe = STD_KONTURFABRE;

    protected double konturDicke = STD_KONTURDICKE;

    protected int konturArt = DURCHGEZOGEN;


    public Color getKonturFarbe() {
        return konturFarbe;
    }

    public void setKonturFarbe(Color pKonturFarbe) {
        this.konturFarbe = pKonturFarbe;
    }

    public void keineKontur() {
        konturFarbe = null;
    }

    public void setKonturFarbe(int gray) {
        setKonturFarbe(gray, gray, gray, 255);
    }

    public void setKonturFarbe(int gray, int alpha) {
        setKonturFarbe(gray, gray, gray, alpha);
    }

    public void setKonturFarbe(int red, int green, int blue) {
        setKonturFarbe(red, green, blue, 255);
    }

    public void setKonturFarbe(int red, int green, int blue, int alpha) {
        if (red < 0 || red >= 256)
            throw new IllegalArgumentException("red must be between 0 and 255");
        if (green < 0 || green >= 256)
            throw new IllegalArgumentException("green must be between 0 and 255");
        if (blue < 0 || blue >= 256)
            throw new IllegalArgumentException("blue must be between 0 and 255");
        if (alpha < 0 || alpha >= 256)
            throw new IllegalArgumentException("alpha must be between 0 and 255");

        setKonturFarbe(new Color(red, green, blue, alpha));
    }

    public double getKonturDicke() {
        return konturDicke;
    }

    public void setKonturDicke(double pKonturDicke) {
        this.konturDicke = pKonturDicke;
    }

    public int getKonturArt() {
        return konturArt;
    }

    public void setKonturArt(int konturArt) {
        switch (konturArt) {
            case GESTRICHELT:
                this.konturArt = GESTRICHELT;
                break;
            case GEPUNKTET:
                this.konturArt = GEPUNKTET;
                break;
            default:
                this.konturArt = DURCHGEZOGEN;
                break;
        }

    }

    public abstract void zeichnen(Graphics2D graphics);

    protected Stroke createStroke() {
        switch(konturArt) {
            case GEPUNKTET:
                return new BasicStroke(
                    (float) konturDicke,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND,
                    10.0f, new float[]{1.0f,5.0f}, 0.0f);
            case GESTRICHELT:
                return new BasicStroke(
                    (float) konturDicke,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND,
                    10.0f, new float[]{5.0f}, 0.0f);
            case DURCHGEZOGEN:
            default:
                return new BasicStroke(
                    (float) konturDicke,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND);
        }
    }

}
