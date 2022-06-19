package schule.ngb.zm.partikel;

import schule.ngb.zm.Updatable;
import schule.ngb.zm.Color;
import schule.ngb.zm.Vector;
import schule.ngb.zm.Drawable;

import java.awt.*;

public class PartikelGenerator implements Updatable, Drawable {

	private int partikelProFrame;

	private int partikelLeben = 180;

	private Partikel[] partikel;

	private boolean aktiv = false, _aktiv = false;

	private Partikel naechsterPartikel;

	public Vector position;

	public Vector richtung = new Vector();

	public int winkel = 0;

	public Color farbeStart = new Color(128,128,129);

	public Color farbeEnde = new Color(128,128,129, 0);

	public double zufallsfaktor = 0.0;

	private Vortex vortex = null;

	public PartikelGenerator( double pX, double pY, int pPartikelLeben, int pPartikelProFrame ) {
		position = new Vector(pX, pY);
		partikelProFrame = pPartikelProFrame;
		partikelLeben = pPartikelLeben;
		partikel = new Partikel[partikelProFrame*partikelLeben];

		//vortex = new Vortex(position.kopie().addieren(-10, -10), -.2, 8);
	}

	@Override
	public boolean isActive() {
		return aktiv;
	}

	@Override
	public boolean isVisible() {
		return aktiv;
	}

	public void starten() {
		// Partikel initialisieren
		for( int i = 0; i < partikel.length; i++ ) {
			partikel[i] = new Partikel(position.copy());
			if( i > 0 ) {
				partikel[i-1].naechster = partikel[i];
			}
		}
		naechsterPartikel = partikel[0];

		aktiv = true;
	}

	public void stoppen() {
		aktiv = false;
	}

	public void partikelGenerieren() {
		int i = partikelProFrame;
		while( i > 0 && naechsterPartikel != null ) {
			Partikel p = naechsterPartikel;
			naechsterPartikel = p.naechster;

			double rotation = Math.toRadians(
				(winkel / 2) - (int) (Math.random() * winkel)
			);
			p.position.set(position);

			p.geschwindigkeit.set(richtung.copy().rotate(rotation));
			p.geschwindigkeit.scale(zufall());

			p.farbeStart = farbeStart;
			p.farbeEnde = farbeEnde;

			int pLeben = (int) zufall(partikelLeben);
			p.leben = pLeben;
			p.maxLeben = pLeben;

			i -= 1;
		}
	}

	@Override
	public void update( double delta ) {
		if( isActive() ) {
			partikelGenerieren();

			_aktiv = false;
			for( int i = 0; i < partikel.length; i++ ) {
				if( partikel[i] != null ) {
					if( partikel[i].isActive() ) {
						if( vortex != null ) {
							vortex.attract(partikel[i]);
						}
						partikel[i].update(delta);
						_aktiv = true;
					} else {
						partikel[i].naechster = naechsterPartikel;
						naechsterPartikel = partikel[i];
					}
				}
			}
		}
	}

	private double zufall() {
		return 1.0-(Math.random()*zufallsfaktor);
	}

	private double zufall( double pZahl ) {
		return pZahl*zufall();
	}

	@Override
	public void draw( Graphics2D graphics ) {
		if( isActive() ) {
			java.awt.Color current = graphics.getColor();
			for( int i = 0; i < partikel.length; i++ ) {
				if( partikel[i] != null ) {
					partikel[i].draw(graphics);
				}
			}

			if( vortex != null ) {
				graphics.setColor(java.awt.Color.BLACK);
				double vscale = (4*vortex.scale);
				graphics.fillOval((int)(vortex.position.x-vscale*.5), (int)(vortex.position.y-vscale*.5), (int)vscale, (int)vscale);
			}
			graphics.setColor(current);
		}
	}




	class Vortex {
		Vector position;
		double speed = 1.0, scale = 1.0;

		public Vortex( Vector pPosition ,double pSpeed, double pScale ) {
			this.position = pPosition.copy();
			this.scale = pScale;
			this.speed = pSpeed;
		}

		public void attract( Partikel pPartikel ) {
			Vector diff = Vector.sub(pPartikel.position, this.position);
			double dx = -diff.y * this.speed;
			double dy = diff.x * this.speed;
			double f = 1.0 / (1.0 + (dx*dx+dy*dy)/scale);

			pPartikel.position.x += (diff.x - pPartikel.geschwindigkeit.x) * f;
			pPartikel.position.y += (diff.y - pPartikel.geschwindigkeit.y) * f;

		}
	}
}
