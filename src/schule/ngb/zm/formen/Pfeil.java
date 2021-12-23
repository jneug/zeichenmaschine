package schule.ngb.zm.formen;

import schule.ngb.zm.Vektor;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class Pfeil extends Linie {

	public static final int OFFEN = 101;
	public static final int GESCHLOSSEN = 102;
	private static final double BASIS_PFEILGROESSE = 5.0;
	protected int pfeilspitze = OFFEN;

	protected double pfeilgroesse = 1.0;

	public Pfeil( double pX1, double pY1, double pX2, double pY2 ) {
		super(pX1, pY1, pX2, pY2);
	}

	public Pfeil( Vektor pVektor ) {
		this(0, 0, pVektor.x, pVektor.y);
	}

	public Pfeil( double pX, double pY, Vektor pVektor ) {
		this(pX, pY, pX + pVektor.x, pY + pVektor.y);
	}

	public Pfeil( Linie pLinie ) {
		this(pLinie.x, pLinie.y, pLinie.x2, pLinie.y2);
		kopiere(pLinie);
	}

	public double getPfeilgroesse() {
		return pfeilgroesse;
	}

	public void setPfeilgroesse( double pPfeilgroesse ) {
		pfeilgroesse = pPfeilgroesse;
	}

	public int getPfeilspitze() {
		return pfeilspitze;
	}

	public void setPfeilspitze( int pPfeilspitze ) {
		this.pfeilspitze = pPfeilspitze;
	}

	public void abbilden( Vektor pVektor ) {
		x2 = x + pVektor.x;
		y2 = y + pVektor.y;
	}

	@Override
	public void kopiere( Form pForm ) {
		super.kopiere(pForm);
		if( pForm instanceof Pfeil ) {
			Pfeil pfeil = (Pfeil) pForm;
			pfeilgroesse = pfeil.pfeilgroesse;
			pfeilspitze = pfeil.pfeilspitze;
		}
	}

	@Override
	public Pfeil kopie() {
		return new Pfeil(this);
	}

	@Override
	public Shape getShape() {
		/*Path2D.Double gruppe = new Path2D.Double();
		gruppe.append(super.getShape(), false);
		gruppe.append(getPfeilspitze(), false);

		return gruppe;*/
		return super.getShape();
	}

	protected Shape getSpitze() {
		AffineTransform af;
		switch( pfeilspitze ) {
			case OFFEN:
				double len = BASIS_PFEILGROESSE * pfeilgroesse;
				Path2D.Double sOffen = new Path2D.Double();
				sOffen.moveTo(-len, -len);
				sOffen.lineTo(0, 0);
				sOffen.lineTo(-len, len);

				af = new AffineTransform();
				af.translate(x2, y2);
				af.rotate(Math.atan2(y2 - y, x2 - x));
				return af.createTransformedShape(sOffen);
			case GESCHLOSSEN:
			default:
				int ix = (int) x2, iy = (int) y2, pg = (int) (BASIS_PFEILGROESSE * pfeilgroesse);
				Polygon sGeschlossen = new Polygon(
					new int[]{0, -pg, -pg},
					new int[]{0, -pg, pg},
					3
				);

				af = new AffineTransform();
				af.translate(x2, y2);
				af.rotate(Math.atan2(y2 - y, x2 - x));
				return af.createTransformedShape(sGeschlossen);
		}
	}

	@Override
	public void zeichnen( Graphics2D graphics, AffineTransform pVerzerrung ) {
		if( !sichtbar ) {
			return;
		}

		super.zeichnen(graphics);

		Shape spitze = getSpitze();
		if( pVerzerrung != null ) {
			spitze = pVerzerrung.createTransformedShape(spitze);
		}

		Color currentColor = graphics.getColor();
		if( konturFarbe != null && konturFarbe.getAlpha() > 0.0 ) {
			graphics.setColor(konturFarbe);
			graphics.setStroke(new BasicStroke((float) konturDicke));
			if( pfeilspitze == GESCHLOSSEN ) {
				graphics.fill(spitze);
			} else {
				graphics.draw(spitze);
			}
		}
		graphics.setColor(currentColor);

	}
}
