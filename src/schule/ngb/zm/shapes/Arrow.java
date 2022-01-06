package schule.ngb.zm.shapes;

import org.jetbrains.annotations.NotNull;
import schule.ngb.zm.Options;
import schule.ngb.zm.Vector;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class Arrow extends Line {

	// Spitzenarten
	public static final int OPEN = 101;

	public static final int CLOSED = 102;

	private static final double BASE_HEADSIZE = 5.0;

	protected Options.ArrowHead arrowhead = LINES;

	protected double headsize = 1.0;

	public Arrow( double x1, double y1, double x2, double y2 ) {
		super(x1, y1, x2, y2);
	}

	/**
	 * Erstellt einen Pfeil, der den übergebenen Vektor darstellt.
	 *
	 * @param vector
	 */
	public Arrow( @NotNull Vector vector ) {
		this(0, 0, vector.x, vector.y);
	}

	/**
	 * Erstellt einen Pfeil, der den Differenzvektor zwischen den übergebenen
	 * Vektoren darstellt.
	 *
	 * @param vector1
	 * @param vector2
	 */
	public Arrow( @NotNull Vector vector1, @NotNull Vector vector2 ) {
		this(vector1.x, vector1.y, vector2.x, vector2.y);
	}

	/**
	 * Erstellt einen Pfeil, der den übergebenen Vektor um die angegeben
	 * Koordinaten verschoben darstellt.
	 *
	 * @param x
	 * @param y
	 * @param vector
	 */
	public Arrow( double x, double y, @NotNull Vector vector ) {
		this(x, y, x + vector.x, y + vector.y);
	}

	/**
	 * Erstellt einen Pfeil als Kopie einer vorgegebenen Linie.
	 *
	 * @param line
	 */
	public Arrow( @NotNull Line line ) {
		this(line.x, line.y, line.x2, line.y2);
		this.copyFrom(line);
	}

	public double getHeadsize() {
		return headsize;
	}

	public void setHeadsize( double headsize ) {
		this.headsize = headsize;
	}

	public Options.ArrowHead getArrowhead() {
		return arrowhead;
	}

	public void setArrowhead( Options.ArrowHead arrowhead ) {
		this.arrowhead = arrowhead;
	}

	/**
	 * Kopiert die Werte des angegebenen Vektors.
	 *
	 * @param vector
	 */
	public void copyFrom( Vector vector ) {
		if( vector != null ) {
			x2 = x + vector.x;
			y2 = y + vector.y;
		}
	}

	@Override
	public void copyFrom( Shape shape ) {
		super.copyFrom(shape);
		if( shape instanceof Arrow ) {
			Arrow pArrow = (Arrow) shape;
			headsize = pArrow.headsize;
			arrowhead = pArrow.arrowhead;
		}
	}

	@Override
	public Arrow copy() {
		return new Arrow(this);
	}

	@Override
	public java.awt.Shape getShape() {
		/*Path2D.Double gruppe = new Path2D.Double();
		gruppe.append(super.getShape(), false);
		gruppe.append(getPfeilspitze(), false);

		return gruppe;*/
		return super.getShape();
	}

	protected java.awt.Shape getHeadShape() {
		AffineTransform af;
		switch( arrowhead ) {
			case LINES:
				double len = BASE_HEADSIZE * headsize;
				Path2D.Double sOPEN = new Path2D.Double();
				sOPEN.moveTo(-len, -len);
				sOPEN.lineTo(0, 0);
				sOPEN.lineTo(-len, len);

				af = new AffineTransform();
				af.translate(x2-x, y2-y);
				af.rotate(Math.atan2(y2 - y, x2 - x));
				return af.createTransformedShape(sOPEN);
			case FILLED:
			default:
				int ix = (int) x2, iy = (int) y2, pg = (int) (BASE_HEADSIZE * headsize);
				java.awt.Polygon sCLOSED = new java.awt.Polygon(
					new int[]{0, -pg, -pg},
					new int[]{0, -pg, pg},
					3
				);

				af = new AffineTransform();
				af.translate(x2-x, y2-y);
				af.rotate(Math.atan2(y2 - y, x2 - x));
				return af.createTransformedShape(sCLOSED);
		}
	}

	@Override
	public void draw( Graphics2D graphics, AffineTransform transform ) {
		if( !visible ) {
			return;
		}

		super.draw(graphics, transform);

		java.awt.Shape head = getHeadShape();
		if( transform != null ) {
			head = transform.createTransformedShape(head);
		}

		Color currentColor = graphics.getColor();
		if( strokeColor != null && strokeColor.getAlpha() > 0.0 ) {
			graphics.setColor(strokeColor.getColor());
			graphics.setStroke(new BasicStroke((float) strokeWeight));
			if( arrowhead == FILLED ) {
				graphics.fill(head);
			} else {
				graphics.draw(head);
			}
		}
		graphics.setColor(currentColor);
	}

}
