package schule.ngb.zm;

import schule.ngb.zm.formen.Form;
import schule.ngb.zm.formen.Rechteck;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class TestAttraction extends Zeichenfenster {

    public static void main(String[] args) {
        new TestAttraction();
    }

    @Override
    public void einstellungen() {
        setSize(800, 600);
        setTitel("My test Window");
        //setFramesPerSecond(5);

        s2dl = new Shape2DEbene();
        hinzu(s2dl);
    }

    Shape2DEbene s2dl;

    Vektor posA, posB, velB, posC, velC;

    double massA = 500, massB = 1, massC = 4.3, G = 5.0;

    Rechteck recht;

    @Override
    public void vorbereiten() {
        //zeichnung.hide();
        zeichnung.clear(200);
        posA = new Vektor(0, 0);
        posB = new Vektor(-100, -100);
        velB = new Vektor(10, -10);
        posC = new Vektor(200, 100);
        velC = new Vektor(1, 14);

        zeichnung.translate(breite /2, hoehe /2);
        zeichnung.shear(0.1, 0.5);

        recht = new Rechteck(50, 50, 150, 75);
        recht.setFuellfarbe(200);
        recht.setKonturFarbe(255, 0, 64);
        recht.setKonturDicke(2.5);
        recht.setKonturArt(Form.GESTRICHELT);
        formen.anzeigen(recht);

        zeichnung.verstecken();
        //schule.ngb.zm.formen.verstecken();

        s2dl.setColor(64,200,128);
        s2dl.add(new Rectangle2D.Double(100, 100, 120, 80));
    }

    public void zeichnen() {
        zeichnung.setStrokeColor(255);
        zeichnung.setStrokeWeight(4.0);
        zeichnung.setKonturArt(GESTRICHELT);
        zeichnung.clear(33, 33, 33, 100);

        zeichnung.setColor(Color.ORANGE);
        zeichnung.pie(posA.x, posA.y, 80, 30, 60);
        zeichnung.setColor(Color.YELLOW);
        zeichnung.circle(posA.x, posA.y, 60);

        Vektor acc = acceleration(posA, posB, massA, massB);
        velB.addieren(acc);
        posB.addieren(velB);

        zeichnung.setColor(Color.BLUE);
        zeichnung.circle(posB.x, posB.y, 20);

        acc = acceleration(posA, posC, massA, massC);
        velC.addieren(acc);
        posC.addieren(velC);

        zeichnung.setColor(Color.GREEN);
        zeichnung.circle(posC.x, posC.y, 20);

        zeichnung.rotate(1);

        formen.leeren();
				double x = recht.getX();
        x = (x+100*delta)% breite;
				recht.setX(x);
    }

    Vektor acceleration(Vektor a, Vektor b, double ma, double mb ) {
        Vektor acc = Vektor.subtrahieren(a, b);
        double draw = (G*ma*mb)/acc.laengeQuad();
        acc.setLaenge(draw*delta);
        acc.beschraenken(3, 30);
        return acc;
    }

    public void mouseDragged() {
        zeichnung.translate(mausX - lmausX, mausY - lmausY);
    }

    boolean zoom = true;
    public void mouseClicked() {
        //canvas.translateToCanvas(mouseX-width/2.0, mouseY-height/2.0);
        if( zoom ) {
            zeichnung.scale(2.0);
        } else {
            zeichnung.scale(.5);
        }
        zoom = !zoom;
    }

}
