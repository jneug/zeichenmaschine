package schule.ngb.zm.shapes;

import schule.ngb.zm.BasicDrawable;
import schule.ngb.zm.Color;
import schule.ngb.zm.Constants;
import schule.ngb.zm.Options;

import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Basisklasse für alle Formen in der Zeichenmaschine.
 * <p>
 * Alle Formen sind als Unterklassen von {@code Shape} implementiert.
 * <p>
 * Neben den abstrakten Methoden implementieren Unterklassen mindestens zwei
 * Konstruktoren. Einen Konstruktor, der die Form mit vom Nutzer gegebenen
 * Parametern initialisiert und einen, der die Werte einer anderen Form
 * desselben Typs übernimmt. In der Klasse {@link Circle} sind die Konstruktoren
 * beispielsweise so implementiert:
 *
 * <pre><code>
 * public Circle( double x, double y, double radius ) {
 *     super(x, y);
 *     this.radius = radius;
 * }
 *
 * public Circle( Circle circle ) {
 *     super(circle.x, circle.y);
 *     copyFrom(circle);
 * }
 * </code></pre>
 * <p>
 * Außerdem implementieren Unterklassen eine passende {@link #toString()} und
 * eine {@link #equals(Object)} Methode.
 */
@SuppressWarnings( "unused" )
public abstract class Shape extends BasicDrawable {

	/**
	 * x-Koordinate der Form.
	 */
	protected double x;

	/**
	 * y-Koordinate der Form.
	 */
	protected double y;

	/**
	 * Rotation in Grad um den Punkt (x, y).
	 */
	protected double rotation = 0.0;

	/**
	 * Skalierungsfaktor.
	 */
	protected double scale = 1.0;

	/**
	 * Ankerpunkt der Form.
	 */
	protected Options.Direction anchor = Options.Direction.CENTER;

	/**
	 * Setzt die x- und y-Koordinate der Form auf 0.
	 */
	public Shape() {
		this(0.0, 0.0);
	}

	/**
	 * Setzt die x- und y-Koordinate der Form.
	 *
	 * @param x
	 * @param y
	 */
	public Shape( double x, double y ) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gibt die x-Koordinate der Form zurück.
	 *
	 * @return Die x-Koordinate.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Setzt die x-Koordinate der Form.
	 *
	 * @param x Die neue x-Koordinate.
	 */
	public void setX( double x ) {
		this.x = x;
	}

	/**
	 * Gibt die y-Koordinate der Form zurück.
	 *
	 * @return Die y-Koordinate.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Setzt die y-Koordinate der Form.
	 *
	 * @param y Die neue y-Koordinate.
	 */
	public void setY( double y ) {
		this.y = y;
	}

	/**
	 * Gibt die Breite dieser Form zurück.
	 * <p>
	 * Die Breite einer Form ist immer die Breite ihrer Begrenzung,
	 * <strong>bevor</strong> Drehungen und andere Transformationen auf sie
	 * angewandt wurden.
	 * <p>
	 * Die Begrenzungen der tatsächlich gezeichneten Form kann mit
	 * {@link #getBounds()} abgerufen werden.
	 *
	 * @return
	 */
	public abstract double getWidth();

	/**
	 * Gibt die Höhe dieser Form zurück.
	 * <p>
	 * Die Höhe einer Form ist immer die Höhe ihrer Begrenzung,
	 * <strong>bevor</strong> Drehungen und andere Transformationen auf sie
	 * angewandt wurden.
	 * <p>
	 * Die Begrenzungen der tatsächlich gezeichneten Form kann mit
	 * {@link #getBounds()} abgerufen werden.
	 *
	 * @return
	 */
	public abstract double getHeight();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGradient( schule.ngb.zm.Color from, schule.ngb.zm.Color to, Options.Direction dir ) {
		Point2D apDir = getAbsAnchorPoint(dir);
		Point2D apInv = getAbsAnchorPoint(dir.inverse());
		setGradient(apInv.getX(), apInv.getY(), from, apDir.getX(), apDir.getY(), to);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGradient( schule.ngb.zm.Color from, schule.ngb.zm.Color to ) {
		Point2D ap = getAbsAnchorPoint(CENTER);
		setGradient(ap.getX(), ap.getY(), Math.min(ap.getX(), ap.getY()), from, to);
	}

	/**
	 * Gibt die Rotation in Grad zurück.
	 *
	 * @return Rotation in Grad.
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * Dreht die Form um den angegebenen Winkel um ihren Ankerpunkt.
	 *
	 * @param angle Drehwinkel in Grad.
	 */
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
		double x1 = this.x - x, y1 = this.y - y;

		double rad = Math.toRadians(angle);
		double x2 = x1 * Math.cos(rad) - y1 * Math.sin(rad);
		double y2 = x1 * Math.sin(rad) + y1 * Math.cos(rad);

		this.x = x2 + x;
		this.y = y2 + y;
	}

	/**
	 * Gibt den aktuellen Skalierungsfaktor zurück.
	 *
	 * @return Der Skalierungsfaktor.
	 */
	public double getScale() {
		return scale;
	}

	public void scale( double factor ) {
		scale = factor;
	}

	public void scaleBy( double factor ) {
		scale(scale * factor);
	}

	public Options.Direction getAnchor() {
		return anchor;
	}

	/**
	 * Setzt den Ankerpunkt der Form basierend auf der angegebenen
	 * {@link Options.Direction Richtung}.
	 * <p>
	 * Für das Setzen des Ankers muss das
	 * {@link #getBounds() begrenzende Rechteck} berechnet werden. Unterklassen
	 * sollten die Methode überschreiben, wenn der Anker auch direkt gesetzt
	 * werden kann.
	 *
	 * @param anchor
	 */
	public void setAnchor( Options.Direction anchor ) {
		if( anchor != null ) {
			this.anchor = anchor;
		}
	}

	/**
	 * Bestimmt die relativen Koordinaten des angegebenen Ankerpunkts basierend
	 * auf der angegebenen Breite und Höhe des umschließenden Rechtecks.
	 * <p>
	 * Die Koordinaten des Ankerpunktes werden relativ zur oberen linken Ecke
	 * des Rechtecks mit der Breite {@code width} und der Höhe {@code height}
	 * bestimmt.
	 *
	 * @param width Breite des umschließenden Rechtecks.
	 * @param height Höhe des umschließenden Rechtecks.
	 * @param anchor Gesuchter Ankerpunkt.
	 * @return Ein {@link Point2D} mit den relativen Koordinaten.
	 */
	public static Point2D.Double getAnchorPoint( double width, double height, Options.Direction anchor ) {
		double wHalf = width * .5, hHalf = height * .5;

		return new Point2D.Double(
			wHalf + wHalf * anchor.x,
			hHalf + hHalf * anchor.y
		);
	}

	/**
	 * Bestimmt den Ankerpunkt der Form relativ zum gesetzten
	 * {@link #setAnchor(Options.Direction) Ankerpunkt}.
	 *
	 * @param anchor Die Richtung des Ankerpunktes.
	 * @return Der relative Ankerpunkt.
	 */
	public Point2D.Double getAnchorPoint( Options.Direction anchor ) {
		double wHalf = getWidth() * .5, hHalf = getHeight() * .5;

		// anchor == CENTER
		return new Point2D.Double(
			wHalf * (anchor.x - this.anchor.x),
			hHalf * (anchor.y - this.anchor.y)
		);
	}

	/**
	 * Ermittelt die absoluten Koordinaten eines angegebenen
	 * {@link #setAnchor(Options.Direction) Ankers}.
	 *
	 * @param anchor Die Richtung des Ankerpunktes.
	 * @return Der absolute Ankerpunkt.
	 */
	public Point2D.Double getAbsAnchorPoint( Options.Direction anchor ) {
		// TODO: Die absoluten Anker müssten eigentlich die Rotation berücksichtigen.
		Point2D.Double ap = getAnchorPoint(anchor);
		ap.x += getX();
		ap.y += getY();
		return ap;
	}

	/**
	 * Kopiert die Eigenschaften der angegebenen Form in diese.
	 * <p>
	 * Unterklassen sollten diese Methode überschreiben, um weitere
	 * Eigenschaften zu kopieren (zum Beispiel den Radius eines Kreises).
	 * Unterklassen sollten immer mit dem Aufruf {@code super.copyFrom(shape)}
	 * die Basiseigenschaften kopieren.
	 * <p>
	 * Die Methode sollte so viele Eigenschaften wie möglich von der anderen
	 * Form in diese kopieren. Wenn die andere Form einen anderen Typ hat, dann
	 * werden trotzdem die Basiseigenschaften (Konturlinie, Füllung, Position,
	 * Rotation, Skalierung, Sichtbarkeit und Ankerpunkt) in diese Form kopiert.
	 * Implementierende Unterklassen können soweit sinnvoll auch andere Werte
	 * übernehmen. Eine {@link Ellipse} kann beispielsweise auch die Breite und
	 * Höhe eines {@link Rectangle} übernehmen.
	 *
	 * @param shape Die Originalform, von der kopiert werden soll.
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

	/**
	 * Erzeugt eine Kopie dieser Form mit denselben Eigenschaften.
	 * <p>
	 * Unterklassen implementieren diese Methode mit dem genauen Typ der
	 * Unterklasse. In {@link Rectangle} sieht die Umsetzung beispielsweise so
	 * aus:
	 * <pre><code>
	 * public Rectangle copy() {
	 *     return new Rectangle(this);
	 * }
	 * </code></pre>
	 * <p>
	 * Die Methode kann beliebig umgesetzt werden, um eine 1-zu-1-Kopie dieser
	 * Form zu erhalten. In der Regel sollte aber jede Form einen Konstruktor
	 * besitzen, die alle Werte einer andern Form übernimmt. Die gezeigte
	 * Implementierung dürfte daher im Regelfall ausreichend sein.
	 *
	 * @return Eine genaue Kopie dieser Form.
	 */
	public abstract Shape copy();

	/**
	 * Gibt eine {@link java.awt.Shape Java-AWT Shape} Version dieser Form
	 * zurück. Intern werden die AWT Shapes benutzt, um sie auf den
	 * {@link Graphics2D Grafikkontext} zu zeichnen.
	 * <p>
	 * Wenn diese Form nicht durch eine AWT-Shape dargestellt wird, kann die
	 * Methode {@code null} zurückgeben.
	 *
	 * @return Eine Java-AWT {@code Shape} die diese Form repräsentiert oder
	 *    {@code null}.
	 */
	public abstract java.awt.Shape getShape();

	/**
	 * Gibt die Begrenzungen der Form zurück.
	 * <p>
	 * Ein {@code Bounds}-Objekt beschreibt eine "<a
	 * href="https://gdbooks.gitbooks.io/3dcollisions/content/Chapter1/aabb.html">Axis
	 * Aligned Bounding Box</a>" (AABB).
	 *
	 * @return Die Abgrenzungen der Form nach Anwendung der Transformationen.
	 */
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

	public void moveTo( Shape shape ) {
		moveTo(shape.getX(), shape.getY());
	}

	public void moveTo( Shape shape, Options.Direction dir ) {
		moveTo(shape, dir, 0.0);
	}

	/**
	 * Bewegt den Ankerpunkt dieser Form zu einem Ankerpunkt einer anderen Form.
	 * Mit {@code buff} kann ein zusätzlicher Abstand angegeben werden, um den
	 * die Form entlang des Ankerpunktes {@code anchor} verschoben werden soll.
	 * Ist der Anker zum Beispiel {@code NORTH}, dann wird die Form um
	 * {@code buff} nach oben verschoben.
	 *
	 * @param shape
	 * @param dir
	 * @param buff
	 */
	public void moveTo( Shape shape, Options.Direction dir, double buff ) {
		Point2D ap = shape.getAbsAnchorPoint(dir);

		this.x = ap.getX() + dir.x * buff;
		this.y = ap.getY() + dir.y * buff;
	}

	/**
	 * Richtet die Form entlang der angegebenen Richtung am Rand der
	 * Zeichenfläche aus.
	 *
	 * @param dir Die Richtung der Ausrichtung.
	 */
	public void alignTo( Options.Direction dir ) {
		alignTo(dir, 0.0);
	}

	public void alignTo( Options.Direction dir, double buff ) {
		Point2D anchorShape = Shape.getAnchorPoint(canvasWidth, canvasHeight, dir);
		Point2D anchorThis = this.getAbsAnchorPoint(dir);

		this.x += Math.abs(dir.x) * (anchorShape.getX() - anchorThis.getX()) + dir.x * buff;
		this.y += Math.abs(dir.y) * (anchorShape.getY() - anchorThis.getY()) + dir.y * buff;
	}

	public void alignTo( Shape shape, Options.Direction dir ) {
		alignTo(shape, dir, 0.0);
	}

	/**
	 * Richtet die Form entlang der angegebenen Richtung an einer anderen Form
	 * aus. Für {@code DOWN} wird beispielsweise die y-Koordinate der unteren
	 * Kante dieser Form an der unteren Kante der angegebenen Form {@code shape}
	 * ausgerichtet. Die x-Koordinate wird nicht verändert. {@code buff} gibt
	 * einen Abstand ab, um den diese From versetzt ausgerichtet werden soll.
	 *
	 * @param shape
	 * @param dir
	 * @param buff
	 */
	public void alignTo( Shape shape, Options.Direction dir, double buff ) {
		Point2D anchorShape = shape.getAbsAnchorPoint(dir);
		Point2D anchorThis = this.getAbsAnchorPoint(dir);

		this.x += Math.abs(dir.x) * (anchorShape.getX() - anchorThis.getX()) + dir.x * buff;
		this.y += Math.abs(dir.y) * (anchorShape.getY() - anchorThis.getY()) + dir.y * buff;
	}

	public void nextTo( Shape shape, Options.Direction dir ) {
		nextTo(shape, dir, DEFAULT_BUFFER);
	}

	/**
	 * Bewegt die Form neben eine andere in Richtung des angegebenen
	 * Ankerpunktes. Im Gegensatz zu
	 * {@link #moveTo(Shape, Options.Direction, double)} wird die Breite bzw.
	 * Höhe der Formen berücksichtigt und die Formen so platziert, dass keine
	 * Überlappungen vorhanden sind.
	 *
	 * @param shape
	 * @param dir
	 * @param buff
	 */
	public void nextTo( Shape shape, Options.Direction dir, double buff ) {
		Point2D anchorShape = shape.getAbsAnchorPoint(dir);
		Point2D anchorThis = this.getAbsAnchorPoint(dir.inverse());

		this.x += (anchorShape.getX() - anchorThis.getX()) + dir.x * buff;
		this.y += (anchorShape.getY() - anchorThis.getY()) + dir.y * buff;
	}

    /*public void shear( double dx, double dy ) {
        verzerrung.shear(dx, dy);
    }*/

	public AffineTransform getTransform() {
		// Point2D.Double anchorPoint = getAnchorPoint();
		Point2D.Double basePoint = getAnchorPoint(Options.Direction.NORTHWEST);

		AffineTransform transform = new AffineTransform();
		transform.translate(x, y);
		transform.rotate(Math.toRadians(rotation));
		//transform.scale(scale, scale);
		//transform.translate(-anchorPoint.x, -anchorPoint.y);
		transform.translate(basePoint.x, basePoint.y);
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
	 * Zeichnet die Form, aber wendet zuvor noch eine zusätzliche
	 * Transformations- matrix an. Wird u.A. von der {@link ShapeGroup}
	 * verwendet.
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

			java.awt.Color currentColor = graphics.getColor();
			fillShape(shape, graphics);
			strokeShape(shape, graphics);
			graphics.setColor(currentColor);
		}
	}

	/**
	 * Vergleicht die Form mit einem anderen Objekt. Handelt es sich bei dem
	 * Objekt um eine andere Form, werden Position, Drehwinkel und Skalierung
	 * verglichen. Unterklassen überschreiben die Methode, um weitere
	 * Eigenschaften zu berücksichtigen.
	 * <p>
	 * Die Eigenschaften, die durch {@link Fillable} und {@link Strokeable}
	 * impliziert werden, werden nicht verglichen.
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

	/**
	 * Hilfsmethode für Unterklassen, um die angegebene Form mit den aktuellen
	 * Kontureigenschaften auf den Grafik-Kontext zu zeichnen. Die Methode
	 * verändert gegebenenfalls die aktuelle Farbe des Grafikobjekts und setzt
	 * sie nicht auf den Ursprungswert zurück, wie von {@link #draw(Graphics2D)}
	 * gefordert. Dies sollte die aufrufende Unterklasse übernehmen.
	 *
	 * @param shape Die zu zeichnende Java-AWT Form
	 * @param graphics Das Grafikobjekt.
	 */
	protected void strokeShape( java.awt.Shape shape, Graphics2D graphics ) {
		if( strokeColor != null && strokeColor.getAlpha() > 0
			&& strokeWeight > 0.0 ) {
			graphics.setColor(strokeColor.getJavaColor());
			graphics.setStroke(getStroke());
			graphics.draw(shape);
		}
	}

	/**
	 * Hilfsmethode für Unterklassen, um die angegebene Form mit der aktuellen
	 * Füllung auf den Grafik-Kontext zu zeichnen. Die Methode verändert
	 * gegebenenfalls die aktuelle Farbe des Grafikobjekts und setzt sie nicht
	 * auf den Ursprungswert zurück, wie von {@link #draw(Graphics2D)}
	 * gefordert. Dies sollte die aufrufende Unterklasse übernehmen.
	 *
	 * @param shape Die zu zeichnende Java-AWT Form
	 * @param graphics Das Grafikobjekt.
	 */
	protected void fillShape( java.awt.Shape shape, Graphics2D graphics ) {
		if( fill != null ) {
			graphics.setPaint(fill);
			graphics.fill(shape);
		} else if( fillColor != null && fillColor.getAlpha() > 0 ) {
			graphics.setColor(fillColor.getJavaColor());
			graphics.fill(shape);
		}
	}

}
