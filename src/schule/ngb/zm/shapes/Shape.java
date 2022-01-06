package schule.ngb.zm.shapes;

import org.jetbrains.annotations.NotNull;
import schule.ngb.zm.Options;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public abstract class Shape extends FilledShape {

	protected double x;

	protected double y;

	protected double rotation = 0.0;

	protected double scale = 1.0;

	protected boolean visible = true;

	protected Options.Direction anchor = Options.Direction.CENTER;

	public Shape( double x, double y ) {
		this.x = x;
		this.y = y;
	}

	public Shape() {
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

	/**
	 * Gibt die Breite dieser Form zurück.
	 * <p>
	 * Die Breite einer Form ist immer die Breite ihrer Begrenzung, <b>bevor</b>
	 * Drehungen und andere Transformationen auf sei angewandt wurden.
	 * <p>
	 * Die Begrenzungen der tatsächlich gezeichneten Form kann mit {@link #getBounds()}
	 * abgerufen werden.
	 *
	 * @return
	 */
	public abstract double getWidth();

	/**
	 * Gibt die Höhe dieser Form zurück.
	 * <p>
	 * Die Höhe einer Form ist immer die Höhe ihrer Begrenzung, <b>bevor</b>
	 * Drehungen und andere Transformationen auf sei angewandt wurden.
	 * <p>
	 * Die Begrenzungen der tatsächlich gezeichneten Form kann mit {@link #getBounds()}
	 * abgerufen werden.
	 *
	 * @return
	 */
	public abstract double getHeight();

	public double getRotation() {
		return rotation;
	}

	public double getScale() {
		return scale;
	}

	public boolean isVisible() {
		return visible;
	}

	public void hide() {
		visible = false;
	}

	public void show() {
		visible = true;
	}

	public void toggle() {
		visible = !visible;
	}

	public Options.Direction getAnchor() {
		return anchor;
	}

	/**
	 * Setzt den Ankerpunkt der Form basierend auf der angegebenen
	 * {@link Options.Direction Richtung}.
	 * <p>
	 * Für das Setzen des Ankers muss das {@link #getBounds() begrenzende
	 * Rechteck} berechnet werden. Unterklassen sollten die Methode
	 * überschreiben, wenn der Anker auch direkt gesetzt werden kann.
	 *
	 * @param anchor
	 */
	public void setAnchor( Options.Direction anchor ) {
		if( anchor != null ) {
			this.anchor = anchor;
		}
	}

	/**
	 * Bestimmt den Ankerpunkt der Form relativ zur oberen linken Ecke und
	 * abhängig vom gesetzten {@link #setAnchor(Options.Direction) Anker}.
	 */
	public Point2D.Double getAnchorPoint( Options.Direction anchor ) {
		Point2D.Double anchorpoint = new Point2D.Double(0, 0);

		double bHalf = getWidth() * .5, hHalf = getHeight() * .5;
		// anchor == CENTER
		anchorpoint.x = bHalf;
		anchorpoint.y = hHalf;

		if( NORTH.is(anchor) ) {
			anchorpoint.y -= hHalf;
		}
		if( SOUTH.is(anchor) ) {
			anchorpoint.y += hHalf;
		}
		if( WEST.is(anchor) ) {
			anchorpoint.x -= bHalf;
		}
		if( EAST.is(anchor) ) {
			anchorpoint.x += bHalf;
		}

		return anchorpoint;
	}

	/**
	 * Kopiert die Eigenschaften der übergebenen Form in diese.
	 * <p>
	 * Unterklassen sollten diese Methode überschreiben, um weitere Eigenschaften
	 * zu kopieren (zum Beispiel den Radius eines Kreises). Mit dem Aufruf
	 * <code>super.copyFrom(shape)</code> sollten die Basiseigenschaften
	 * kopiert werden.
	 *
	 * @param shape
	 */
	public void copyFrom( Shape shape ) {
		if( shape != null ) {
			moveTo(shape.x, shape.y);
			setFillColor(shape.getFillColor());
			setStrokeColor(shape.getStrokeColor());
			setStrokeWeight(shape.getStrokeWeight());
			setStrokeType(shape.getStrokeType());
			visible = shape.isVisible();
			rotation = shape.rotation;
			scale(shape.scale);
			setAnchor(shape.getAnchor());
		}
	}

	public abstract Shape copy();

	public abstract java.awt.Shape getShape();

	public Bounds getBounds() {
		return new Bounds(this);
	}

	public void move( double dx, double dy ) {
		x += dx;
		y += dy;
	}

	public void moveTo( double x, double y ) {
		this.x = x;
		this.y = y;
	}

	public void scale( double factor ) {
		scale = factor;
	}

	public void scaleBy( double factor ) {
		scale(scale * factor);
	}

	public void rotate( double angle ) {
		this.rotation += angle % 360;
	}

	public void rotateTo( double angle ) {
		this.rotation = angle % 360;
	}

	public void rotate( Point2D center, double angle ) {
		rotate(center.getX(), center.getY(), angle);
	}

	public void rotate( double x, double y, double angle ) {
		this.rotation += angle % 360;

		// Rotate x/y position
		double x1 = this.x-x, y1 = this.y-y;

		double rad = Math.toRadians(angle);
		double x2 = x1 * Math.cos(rad) - y1 * Math.sin(rad);
		double y2 = x1 * Math.sin(rad) + y1 * Math.cos(rad);

		this.x = x2 + x;
		this.y = y2 + y;
	}

    /*public void shear( double dx, double dy ) {
        verzerrung.shear(dx, dy);
    }*/

	public AffineTransform getTransform() {
		Point2D.Double anchorPoint = getAnchorPoint(this.anchor);

		AffineTransform transform = new AffineTransform();
		transform.translate(x, y);
		transform.rotate(Math.toRadians(rotation));
		//transform.scale(scale, scale);
		transform.translate(-anchorPoint.x, -anchorPoint.y);
		return transform;
	}

	/**
	 * Zeichnet die Form.
	 *
	 * @param graphics
	 */
	@Override
	public final void draw( Graphics2D graphics ) {
		draw(graphics, getTransform());
	}

	/**
	 * Zeichnet die Form, aber wendet zuvor noch eine zusätzliche Transformations-
	 * matrix an. Wird u.A. von der {@link ShapeGroup} verwendet.
	 *
	 * @param graphics
	 * @param transform
	 */
	public void draw( Graphics2D graphics, AffineTransform transform ) {
		if( !visible ) {
			return;
		}

		java.awt.Shape shape = getShape();
		if( shape != null ) {
			if( transform != null ) {
				shape = transform.createTransformedShape(shape);
			}

			Color currentColor = graphics.getColor();
			if( fillColor != null && fillColor.getAlpha() > 0 ) {
				graphics.setColor(fillColor.getColor());
				graphics.fill(shape);
			}
			if( strokeColor != null && strokeColor.getAlpha() > 0
				&& strokeWeight > 0.0 ) {
				graphics.setColor(strokeColor.getColor());
				graphics.setStroke(createStroke());
				graphics.draw(shape);
			}
			graphics.setColor(currentColor);
		}
	}

	/**
	 * Vergleicht die Form mit einem anderen Objekt. Handelt es sich bei dem
	 * Objekt um eine andere Form, werden Position, Drehwinkel und Skalierung
	 * verglichen. Unterklassen überschreiben die Methode, um weitere
	 * Eigenschaften zu berücksichtigen.
	 * <p>
	 * Die Eigenschaften von {@link FilledShape} und {@link StrokedShape} werden
	 * nicht verglichen.
	 *
	 * @param o Ein anderes Objekt.
	 * @return
	 */
	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( o == null || getClass() != o.getClass() ) return false;
		Shape pShape = (Shape) o;
		return Double.compare(pShape.x, x) == 0 &&
			Double.compare(pShape.y, y) == 0 &&
			Double.compare(pShape.rotation, rotation) == 0 &&
			Double.compare(pShape.scale, scale) == 0;
	}

}
