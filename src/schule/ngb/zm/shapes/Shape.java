package schule.ngb.zm.shapes;

import org.jetbrains.annotations.NotNull;
import schule.ngb.zm.Options;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public abstract class Shape extends FilledShape {

	protected double x;

	protected double y;

	protected double rotation = 0.0;

	protected double scale = 1.0;

	protected boolean visible = true;

	protected Point2D.Double anchor = new Point2D.Double();

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

	public Point2D.Double getAnchor() {
		return new Point2D.Double(anchor.x, anchor.y);
	}

	/**
	 * Setzt den Ankerpunkt der Form basierend auf der angegebenen
	 * {@link Options.Direction Richtung}.
	 *
	 * Für das Setzen des Ankers muss das {@link #getBounds() begrenzende
	 * Rechteck} berechnet werden. Unterklassen sollten die Methode
	 * überschreiben, wenn der Anker auch direkt gesetzt werden kann.
	 * @param anchor
	 */
	public void setAnchor( Options.Direction anchor ) {
		java.awt.Shape shape = getShape();
		if( shape != null ) {
			Rectangle2D bounds = shape.getBounds2D();
			calculateAnchor(bounds.getWidth(), bounds.getHeight(), anchor);
		} else {
			this.anchor.x = 0;
			this.anchor.y = 0;
		}
	}

	/**
	 * Setzt den Ankerpunkt explizit auf den angegebenen
	 * @param anchor
	 */
	public void setAnchor( Point2D.Double anchor ) {
		setAnchor(anchor, false);
	}

	public void setAnchor( Point2D.Double anchor, boolean isRelative ) {
		if( anchor != null ) {
			setAnchor(anchor.x, anchor.y, isRelative);
		} else {
			setAnchor(0, 0, true);
		}
	}

	public void setAnchor( double x, double y ) {
		setAnchor(x, y, false);
	}

	public void setAnchor( double x, double y, boolean isRelative ) {
		if( isRelative ) {
			this.anchor.x = x;
			this.anchor.y = y;
		} else {
			this.anchor.x = this.x-x;
			this.anchor.y = this.y-y;
		}
	}

	/**
	 * Hilfsmethode zur Berechnung eines Ankerpunktes relativ zu den angegebenen
	 * Begrenzungen basierend aus {@link #x}-, {@link #y}-Koordinate und
	 * <var>width</var> / <var>height</var> (Breite / Höhe).
	 * @param width
	 * @param height
	 * @param anchor
	 */
	protected void calculateAnchor( double width, double height, @NotNull Options.Direction anchor ) {
		double bHalf = width * .5, hHalf = height * .5;
		// pAnker == CENTER
		this.anchor.x = bHalf;
		this.anchor.y = hHalf;
		if( NORTH.is(anchor) ) {
			this.anchor.y -= hHalf;
		}
		if( SOUTH.is(anchor) ) {
			this.anchor.y += hHalf;
		}
		if( WEST.is(anchor) ) {
			this.anchor.x -= bHalf;
		}
		if( EAST.is(anchor) ) {
			this.anchor.x += bHalf;
		}
	}

	/**
	 * Kopiert die Eigenschaften der übergebenen Form in diese.
	 *
	 * Unterklassen sollten diese Methode überschreiben, um weitere Eigenschaften
	 * zu kopieren (zum Beispiel den Radius eines Kreises). Mit dem Aufruf
	 * <code>super.copyFrom(shape)</code> sollten die Basiseigenschaften
	 * kopiert werden.
	 * @param shape
	 */
	public void copyFrom( Shape shape ) {
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

	public abstract Shape copy();

	public abstract java.awt.Shape getShape();

	public Rectangle getBounds() {
		return new Rectangle(this);
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
		anchor.x *= factor;
		anchor.y *= factor;
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

    /*public void shear( double dx, double dy ) {
        verzerrung.shear(dx, dy);
    }*/

	public AffineTransform getTransform() {
		AffineTransform transform = new AffineTransform();
		transform.translate(x, y);
		transform.rotate(Math.toRadians(rotation));
		//transform.scale(scale, scale);
		transform.translate(-anchor.x, -anchor.y);
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
	 * @param pVerzerrung
	 */
	public void draw( Graphics2D graphics, AffineTransform pVerzerrung ) {
		if( !visible ) {
			return;
		}

		java.awt.Shape shape = getShape();
		if( shape != null ) {
			if( pVerzerrung != null ) {
				shape = pVerzerrung.createTransformedShape(shape);
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
