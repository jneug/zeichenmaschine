package schule.ngb.zm.turtle;

import schule.ngb.zm.Ebene;
import schule.ngb.zm.Farbe;
import schule.ngb.zm.Vektor;
import schule.ngb.zm.formen.Fuellform;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Stack;

public class Turtleebene extends Ebene {

	private static Stack<Farbe> turtleFarben;

	static {
		turtleFarben = new Stack<>();
		turtleFarben.add(Farbe.ORANGE);
		turtleFarben.add(Farbe.GELB);
		turtleFarben.add(Farbe.MAGENTA);
		turtleFarben.add(Farbe.GRUEN);
		turtleFarben.add(Farbe.ROT);
		turtleFarben.add(Farbe.BLAU);
	}

	private Turtle ersterTurtle = null;

	private ArrayList<Turtle> turtles = new ArrayList<Turtle>(6);

	public Turtleebene() {
		super();
	}

	public Turtleebene( int pWidth, int pHeight ) {
		super(pWidth, pHeight);
	}

	public Turtle neueTurtle() {
		Turtle newTurtle = new Turtle();
		newTurtle.position.x = getBreite() / 2;
		newTurtle.position.y = getHoehe() / 2;
		newTurtle.richtung.x = 0.0;
		newTurtle.richtung.y = -1.0;
		synchronized( turtles ) {
			if( !turtleFarben.isEmpty() ) {
				newTurtle.setKonturFarbe(turtleFarben.pop());
			}
			turtles.add(newTurtle);
		}
		return newTurtle;
	}

	private Turtle t() {
		if( ersterTurtle == null ) {
			ersterTurtle = neueTurtle();
		}
		return ersterTurtle;
	}

	@Override
	public void leeren() {
		zeichnung.setBackground(STD_HINTERGRUND);
		zeichnung.clearRect(0, 0, puffer.getWidth(), puffer.getHeight());
	}

	public void vor( double pStrecke ) {
		t().vor(pStrecke);
	}

	public void links( double pWinkel ) {
		t().links(pWinkel);
	}

	public void rechts( double pWinkel ) {
		t().rechts(pWinkel);
	}

	public void rechts() {
		t().rechts(90);
	}

	public void links() {
		t().links(90);
	}

	public void stiftRunter() {
		t().stiftRunter();
	}

	public void stiftHoch() {
		t().stiftHoch();
	}

	public Color getFuellfarbe() {
		return t().getFuellfarbe();
	}

	public void setFuellfarbe( Color pFarbe ) {
		t().setFuellfarbe(pFarbe);
	}

	public void setFuellfarbe( int gray ) {
		t().setFuellfarbe(gray);
	}

	public void keineFuellung() {
		t().keineFuellung();
	}

	public void setFuellfarbe( int gray, int alpha ) {
		t().setFuellfarbe(gray, alpha);
	}

	public void setFuellfarbe( int red, int green, int blue ) {
		t().setFuellfarbe(red, green, blue);
	}

	public void setFuellfarbe( int red, int green, int blue, int alpha ) {
		t().setFuellfarbe(red, green, blue, alpha);
	}

	public Color getKonturFarbe() {
		return t().getKonturFarbe();
	}

	public void setKonturFarbe( Color pKonturFarbe ) {
		t().setKonturFarbe(pKonturFarbe);
	}

	public void setKonturFarbe( int gray ) {
		t().setKonturFarbe(gray);
	}

	public void keineKontur() {
		t().keineKontur();
	}

	public void setKonturFarbe( int gray, int alpha ) {
		t().setKonturFarbe(gray, alpha);
	}

	public void setKonturFarbe( int red, int green, int blue ) {
		t().setKonturFarbe(red, green, blue);
	}

	public void setKonturFarbe( int red, int green, int blue, int alpha ) {
		t().setKonturFarbe(red, green, blue, alpha);
	}

	public double getKonturDicke() {
		return t().getKonturDicke();
	}

	public void setKonturDicke( double pDicke ) {
		t().setKonturDicke(pDicke);
	}

	public int getKonturArt() {
		return t().getKonturArt();
	}

	public void setKonturArt( int konturArt ) {
		t().setKonturArt(konturArt);
	}

	public void bewegeNach( double x, double y ) {
		t().bewegeNach(x, y);
	}

	public void fuellen() {
		t().fuellen();
	}

	@Override
	public void zeichnen( Graphics2D graphics ) {
		super.zeichnen(graphics);

		synchronized( turtles ) {
			for( Turtle t : turtles ) {
				if( t.sichtbar ) {
					t.zeichnen(graphics);
				}
			}
		}
	}

	class Turtle extends Fuellform {
		boolean stiftUnten = true;

		boolean sichtbar = true;

		Vektor position = new Vektor(0, 0);

		Vektor richtung = new Vektor(0, -1);

		Path2D.Double path = new Path2D.Double();

		boolean pathOpen = false;

		private void addPosToPath() {
			if( !pathOpen ) {
				path.reset();
				path.moveTo(position.x, position.y);
				pathOpen = true;
			} else {
				path.lineTo(position.x, position.y);
			}
		}

		private void closePath() {
			if( pathOpen ) {
				addPosToPath();
				path.closePath();
				pathOpen = false;
			}
		}

		public void fuellen() {
			closePath();

			if( fuellFarbe != null && fuellFarbe.getAlpha() > 0 ) {
				zeichnung.setColor(fuellFarbe);
				zeichnung.fill(path);
			}
		}

		public void vor( double pStrecke ) {
			addPosToPath();

			Vektor positionStart = position.kopie();
			position.addieren(Vektor.setLaenge(richtung, pStrecke));

			if( stiftUnten ) {
				zeichnung.setColor(konturFarbe);
				zeichnung.setStroke(createStroke());
				zeichnung.drawLine((int) positionStart.x, (int) positionStart.y, (int) position.x, (int) position.y);
			}
		}

		public boolean istSichtbar() {
			return sichtbar;
		}

		@Override
		public void zeichnen( Graphics2D graphics ) {
			Shape shape = new RoundRectangle2D.Double(
				-12, -5, 16, 10, 5, 3
			);

			AffineTransform verzerrung = new AffineTransform();
			verzerrung.translate(position.x, position.y);
			verzerrung.rotate(-1 * richtung.winkel());

			shape = verzerrung.createTransformedShape(shape);

			graphics.setColor(konturFarbe);
			graphics.fill(shape);
			graphics.setColor(Color.BLACK);
			graphics.setStroke(createStroke());
			graphics.draw(shape);
		}

		public void links() {
			links(90);
		}

		public void links( double pWinkel ) {
			richtung.drehen(Math.toRadians(-1 * pWinkel));
		}

		public void rechts() {
			rechts(90);
		}

		public void rechts( double pWinkel ) {
			richtung.drehen(Math.toRadians(pWinkel));
		}

		public void stiftHoch() {
			stiftUnten = false;
		}

		public void stiftRunter() {
			stiftUnten = true;
		}

		public void bewegeNach( double x, double y ) {
			addPosToPath();

			position.x = x;
			position.y = y;

			if( stiftUnten ) {
				zeichnung.setColor(konturFarbe);
				zeichnung.setStroke(createStroke());
				zeichnung.drawLine((int) x, (int) y, (int) position.x, (int) position.y);
			}
		}
	}

}
