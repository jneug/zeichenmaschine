package schule.ngb.zm;

import schule.ngb.zm.shapes.Rectangle;

import java.awt.geom.Rectangle2D;

public class TestAttraction extends Zeichenmaschine {

    public static void main(String[] args) {
        new TestAttraction();
    }

    @Override
    public void settings() {
        setSize(800, 600);
        setTitle("My test Window");
        //setFramesPerSecond(5);

        s2dl = new Shape2DLayer();
        addLayer(s2dl);
    }

    Shape2DLayer s2dl;

    Vector posA, posB, velB, posC, velC;

    double massA = 500, massB = 1, massC = 4.3, G = 5.0;

    Rectangle recht;

    @Override
    public void setup() {
        //zeichnung.hide();
        drawing.clear(200);
        posA = new Vector(0, 0);
        posB = new Vector(-100, -100);
        velB = new Vector(10, -10);
        posC = new Vector(200, 100);
        velC = new Vector(1, 14);

        drawing.translate(width /2, height /2);
        drawing.shear(0.1, 0.5);

        recht = new Rectangle(50, 50, 150, 75);
        recht.setFillColor(200);
        recht.setStrokeColor(255, 0, 64);
        recht.setStrokeWeight(2.5);
        recht.setStrokeType(DASHED);
        shapes.add(recht);

        drawing.hide();
        //schule.ngb.zm.formen.verstecken();

        s2dl.setFillColor(64,200,128);
        s2dl.add(new Rectangle2D.Double(100, 100, 120, 80));
    }

    public void draw() {
        drawing.setStrokeColor(255);
        drawing.setStrokeWeight(4.0);
        drawing.setStrokeType(DASHED);
        drawing.clear(33, 33, 33, 100);

        drawing.setFillColor(Color.ORANGE);
        drawing.pie(posA.x, posA.y, 80, 30, 60);
        drawing.setFillColor(Color.YELLOW);
        drawing.circle(posA.x, posA.y, 60);

        Vector acc = acceleration(posA, posB, massA, massB);
        velB.add(acc);
        posB.add(velB);

        drawing.setFillColor(Color.BLUE);
        drawing.circle(posB.x, posB.y, 20);

        acc = acceleration(posA, posC, massA, massC);
        velC.add(acc);
        posC.add(velC);

        drawing.setFillColor(Color.GREEN);
        drawing.circle(posC.x, posC.y, 20);

        drawing.rotate(1);

        shapes.clear();
				double x = recht.getX();
        x = (x+100*delta)% width;
				recht.setX(x);
    }

    Vector acceleration( Vector a, Vector b, double ma, double mb ) {
        Vector acc = Vector.sub(a, b);
        double draw = (G*ma*mb)/acc.lengthSq();
        acc.setLength(draw*delta);
        acc.limit(3, 30);
        return acc;
    }

    public void mouseDragged() {
        drawing.translate(mouseX - pmouseX, mouseY - pmouseY);
    }

    boolean zoom = true;
    public void mouseClicked() {
        //canvas.translateToCanvas(mouseX-width/2.0, mouseY-height/2.0);
        if( zoom ) {
            drawing.scale(2.0);
        } else {
            drawing.scale(.5);
        }
        zoom = !zoom;
    }

}
