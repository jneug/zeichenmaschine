package schule.ngb.zm.formen;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public abstract class Form extends Fuellform {


	protected double x;

	protected double y;

	protected double drehwinkel = 0.0;

	protected double skalierung = 1.0;

	protected boolean sichtbar = true;

	protected Point2D.Double anker = new Point2D.Double();

	FormGruppe gruppe = null;

	public Form( double pX, double pY ) {
		x = pX;
		y = pY;
	}

	public Form() {
		this(0.0, 0.0);
	}

	public double getX() {
		return x;
	}

	public void setX( double x ) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY( double y ) {
		this.y = y;
	}

	public double getDrehwinkel() {
		return drehwinkel;
	}

	public double getSkalierung() {
		return skalierung;
	}

	public boolean istSichtbar() {
		return sichtbar;
	}

	public void verstecken() {
		sichtbar = false;
	}

	public void zeigen() {
		sichtbar = true;
	}

	public void umschalten() {
		sichtbar = !sichtbar;
	}

	public Point2D.Double getAnkerpunkt() {
		return new Point2D.Double(anker.x, anker.y);
	}

	public void setAnkerpunkt( byte pAnker ) {
		Shape shape = getShape();
		if( shape != null ) {
			Rectangle2D bounds = shape.getBounds2D();
			ankerBerechnen(bounds.getWidth(), bounds.getHeight(), pAnker);
		} else {
			anker.x = 0;
			anker.y = 0;
		}
	}

	public void setAnkerpunkt( Point2D.Double pAnker ) {
		anker.x = pAnker.x;
		anker.y = pAnker.y;
	}

	protected void ankerBerechnen( double pBreite, double pHoehe, byte pAnker ) {
		double bHalb = pBreite * .5, hHalb = pHoehe * .5;
		// pAnker == CENTER
		anker.x = bHalb;
		anker.y = hHalb;
		if( (pAnker & NORDEN) == NORDEN ) {
			anker.y -= hHalb;
		}
		if( (pAnker & SUEDEN) == SUEDEN ) {
			anker.y += hHalb;
		}
		if( (pAnker & WESTEN) == WESTEN ) {
			anker.x -= bHalb;
		}
		if( (pAnker & OSTEN) == OSTEN ) {
			anker.x += bHalb;
		}
	}

	public void kopiere( Form pForm ) {
		verschiebeNach(pForm);
		setFuellfarbe(pForm.getFuellfarbe());
		setKonturFarbe(pForm.getKonturFarbe());
		setKonturDicke(pForm.getKonturDicke());
		setKonturArt(pForm.getKonturArt());
		sichtbar = pForm.istSichtbar();
		drehwinkel = pForm.drehwinkel;
		skalieren(pForm.skalierung);
		setAnkerpunkt(pForm.getAnkerpunkt());
	}

	public abstract Form kopie();

	public Rechteck getBegrenzung() {
		return new Rechteck(this);
	}

	public void verschieben( double dx, double dy ) {
		x += dx;
		y += dy;
	}

	public void verschiebeNach( double pX, double pY ) {
		x = pX;
		y = pY;
	}

	public void verschiebeNach( Form pForm ) {
		verschiebeNach(pForm.x, pForm.y);
	}

	public void skalieren( double pFaktor ) {
		skalierung = pFaktor;
		anker.x *= pFaktor;
		anker.y *= pFaktor;
	}

	public void skalierenUm( double pFaktor ) {
		skalieren(skalierung * pFaktor);
	}

	public void drehen( double pWinkel ) {
		drehwinkel += pWinkel % 360;
	}

	public void drehenNach( double pWinkel ) {
		drehwinkel = pWinkel % 360;
	}

    /*public void scheren( double dx, double dy ) {
        verzerrung.shear(dx, dy);
    }*/

	public AffineTransform getVerzerrung() {
		AffineTransform verzerrung = new AffineTransform();
		verzerrung.translate(x, y);
		verzerrung.rotate(Math.toRadians(drehwinkel));
		//verzerrung.scale(skalierung, skalierung);
		verzerrung.translate(-anker.x, -anker.y);
		return verzerrung;
	}

	public final void zeichnen( Graphics2D graphics ) {
		zeichnen(graphics, getVerzerrung());
	}

	/**
	 * Zeichnet die Form, aber wendet zuvor noch eine zusätzliche Transformations-
	 * matrix an. Wird u.A. von der {@link FormGruppe} verwendet.
	 * @param graphics
	 * @param pVerzerrung
	 */
	public void zeichnen( Graphics2D graphics, AffineTransform pVerzerrung ) {
		if( !sichtbar ) {
			return;
		}

		Shape shape = getShape();
		if( shape != null ) {
			if( pVerzerrung != null ) {
				shape = pVerzerrung.createTransformedShape(shape);
			}

			Color currentColor = graphics.getColor();
			if( fuellFarbe != null && fuellFarbe.getAlpha() > 0 ) {
				graphics.setColor(fuellFarbe);
				graphics.fill(shape);
			}
			if( konturFarbe != null && konturFarbe.getAlpha() > 0
				&& konturDicke > 0.0 ) {
				graphics.setColor(konturFarbe);
				graphics.setStroke(createStroke());
				graphics.draw(shape);
			}
			graphics.setColor(currentColor);
		}
	}

	public abstract Shape getShape();

	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( o == null || getClass() != o.getClass() ) return false;
		Form form = (Form) o;
		return Double.compare(form.x, x) == 0 &&
			Double.compare(form.y, y) == 0 &&
			Double.compare(form.drehwinkel, drehwinkel) == 0 &&
			Double.compare(form.skalierung, skalierung) == 0;
	}

}
