package schule.ngb.zm.shapes;

import schule.ngb.zm.BasicDrawable;
import schule.ngb.zm.Fillable;
import schule.ngb.zm.Options;
import schule.ngb.zm.Strokeable;
import schule.ngb.zm.util.Validator;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Dies ist die Basisklasse für alle Formen in der Zeichenmaschine.
 * <p>
 * Alle Formen sind als Unterklassen von {@code Shape} implementiert.
 * <p>
 * Neben den abstrakten Methoden implementieren Unterklassen mindestens zwei
 * Konstruktoren. Einen Konstruktor, der die Form mit vom Nutzer gegebenen
 * Parametern initialisiert und einen, der die Werte einer anderen Form
 * desselben Typs übernimmt. In der Klasse {@link Circle} sind die Konstruktoren
 * beispielsweise so implementiert:
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
	 * Speichert die x-Koordinate der Form.
	 */
	protected double x;

	/**
	 * Speichert die y-Koordinate der Form.
	 */
	protected double y;

	/**
	 * Speichert die Rotation in Grad um den Punkt (x, y).
	 */
	protected double rotation = 0.0;

	/**
	 * Speichert den Skalierungsfaktor.
	 */
	protected double scale = 1.0;

	/**
	 * Speichert den Ankerpunkt.
	 */
	protected Options.Direction anchor = Options.Direction.CENTER;

	/**
	 * Erstellt eine neue Form mit den Koordinaten {@code (0,0)}.
	 */
	public Shape() {
		this(0.0, 0.0);
	}

	/**
	 * Erstellt eine Form mit den angegebenen Koordinaten.
	 *
	 * @param x Die x-Koordinate.
	 * @param y Die y-Koordinate.
	 */
	public Shape( double x, double y ) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Liefert die aktuelle x-Koordinate der Form.
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
	 * Liefert die aktuelle y-Koordinate der Form.
	 *
	 * @return Die y-Koordinate.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Setzt die x-Koordinate der Form.
	 *
	 * @param y Die neue y-Koordinate der Form.
	 */
	public void setY( double y ) {
		this.y = y;
	}

	/**
	 * Liefert die aktuelle Breite dieser Form.
	 * <p>
	 * Die Breite einer Form ist immer die Breite ihrer Begrenzung,
	 * <strong>bevor</strong> Drehungen und andere Transformationen auf sie
	 * angewandt wurden.
	 * <p>
	 * Die Begrenzungen der tatsächlich gezeichneten Form wird mit
	 * {@link #getBounds()} abgerufen.
	 *
	 * @return Die Breite der Form.
	 */
	public abstract double getWidth();

	/**
	 * Liefert die aktuelle Höhe dieser Form.
	 * <p>
	 * Die Höhe einer Form ist immer die Höhe ihrer Begrenzung,
	 * <strong>bevor</strong> Drehungen und andere Transformationen auf sie
	 * angewandt wurden.
	 * <p>
	 * Die Begrenzungen der tatsächlich gezeichneten Form wird mit
	 * {@link #getBounds()} abgerufen.
	 *
	 * @return Die Höhe der Form.
	 */
	public abstract double getHeight();

	/**
	 * Liefert die Rotation in Grad.
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

	/**
	 * Dreht die Form um den angegebenen Winkel um das angegebene Drehzentrum.
	 *
	 * @param center Das Drehzentrum der Drehung.
	 * @param angle Der Drehwinkel.
	 */
	public void rotate( Point2D center, double angle ) {
		Validator.requireNotNull(center, "center");
		rotate(center.getX(), center.getY(), angle);
	}

	/**
	 * Dreht die Form um den angegebenen Drehwinkel um die angegbenen
	 * Koordinaten als Drehzentrum.
	 *
	 * @param x x-Koordiante des Drehzentrums.
	 * @param y y-Koordiante des Drehzentrums.
	 * @param angle Drehwinkel in Grad.
	 */
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
	 * Setzt die Drehung der Form auf den angegebenen Winkel.
	 *
	 * @param angle Drehwinkel in Grad.
	 */
	public void rotateTo( double angle ) {
		this.rotation = angle % 360;
	}

	/**
	 * Gibt den aktuellen Skalierungsfaktor zurück.
	 *
	 * @return Der Skalierungsfaktor.
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * Setzt den Skalierungsfaktor auf den angegebenen Faktor.
	 * <p>
	 * Bei einem Faktor größer 0 wird die Form vergrößert, bei einem Faktor
	 * kleiner 0 verkleinert. Bei negativen Werten wird die Form entlang der x-
	 * bzw. y-Achse gespiegelt.
	 * <p>
	 * Das Seitenverhältnis wird immer beibehalten.
	 *
	 * @param factor Der Skalierungsfaktor.
	 */
	public void scale( double factor ) {
		scale = factor;
	}

	/**
	 * Skaliert die Form um den angegebenen Faktor.
	 * <p>
	 * Bei einem Faktor größer 0 wird die Form vergrößert, bei einem Faktor
	 * kleiner 0 verkleinert. Bei negativen Werten wird die Form entlang der x-
	 * bzw. y-Achse gespiegelt.
	 * <p>
	 * Die Skalierung wird zusätzlich zur aktuellen Skalierung angewandt. Wurde
	 * die Form zuvor um den Faktor 0.5 verkleinert und wird dann um 1.5
	 * vergrößert, dann ist die Form im Anschluss ein Drittel kleiner als zu
	 * Beginn ({@code 0.5 * 1.5 = 0.75}).
	 *
	 * @param factor Der Skalierungsfaktor.
	 */
	public void scaleBy( double factor ) {
		scale(scale * factor);
	}

	/**
	 * Liefert den aktuellen Ankerpunkt der Form.
	 *
	 * @return Der Ankerpunkt.
	 */
	public Options.Direction getAnchor() {
		return anchor;
	}

	/**
	 * Setzt den Ankerpunkt der Form auf die angegebene Richtung.
	 * <p>
	 * Jede Form hat einen Ankerpunkt, von dem aus sie gezeichnet wird. Jede
	 * {@link schule.ngb.zm.Options.Direction Richtung} beschreibt einen der
	 * Neun Ankerpunkte:
	 * <pre>
	 * NW────N────NE
	 * │           │
	 * │           │
	 * W     C     E
	 * │           │
	 * │           │
	 * SW────S────SE
	 * </pre>
	 * <p>
	 * Für den Ankerpunkt {@link #CENTER} wird die Form also ausgehend von den
	 * Koordinaten {@link #x} und {@link #y} um die Hälfte der Breite nach links
	 * und rechts, sowie um die Hälfte der Höhe nach oben und unten gezeichnet.
	 * Fpr den Ankerpunkt {@link #NORTHWEST} dagegen um die gesamte Breite nach
	 * rechts und die Höhe nach unten.
	 * <pre>
	 * setAnchor(CENTER) │   setAnchor(NORTHWEST)
	 *   ┌───────────┐   │
	 *   │           │   │
	 *   │           │   │
	 *   │   (x,y)   │   │       (x,y)─────────┐
	 *   │           │   │         │           │
	 *   │           │   │         │           │
	 *   └───────────┘   │         │           │
	 *                   │         │           │
	 *                   │         │           │
	 *                   │         └───────────┘
	 * </pre>
	 * <p>
	 * Der Ankerpunkt der Form bestimmt bei Transformationen auch die Position
	 * des Drehzentrums und anderer relativer Koordinaten bezüglich der Form.
	 *
	 * @param anchor Der Ankerpunkt.
	 */
	public void setAnchor( Options.Direction anchor ) {
		Validator.requireNotNull(anchor, "anchor");
		this.anchor = anchor;
	}

	/**
	 * Bestimmt die relativen Koordinaten des angegebenen Ankerpunkts basierend
	 * auf der angegebenen Breite und Höhe des umschließenden Rechtecks.
	 * <p>
	 * Die Koordinaten des Ankerpunktes werden relativ zur oberen linken Ecke
	 * des Rechtecks mit der Breite {@code width} und der Höhe {@code height}
	 * bestimmt. Der Ankerpunkt {@link #NORTHWEST} hat daher immer das Ergebnis
	 * {@code (0,0)} und {@link #SOUTHEAST} {@code (width, height)}.
	 * <pre>
	 *  (0,0)───(w/2,0)───(w,0)
	 *    │                 │
	 *    │                 │
	 *    │                 │
	 * (0,h/2) (w/2,h/2) (w,h/2)
	 *    │                 │
	 *    │                 │
	 *    │                 │
	 *  (0,h)───(w/2,h)───(w,h)
	 * </pre>
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
	 * Bestimmt die Koordinaten des angegebenen Ankers der Form relativ zum
	 * aktuellen {@link #setAnchor(Options.Direction) Ankerpunkt}.
	 *
	 * @param anchor Die Richtung des Ankers.
	 * @return Der relative Ankerpunkt.
	 * @see #getAnchorPoint(double, double, Options.Direction)
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
	 * Ermittelt die absoluten Koordinaten des angegebenen
	 * {@link #setAnchor(Options.Direction) Ankers}.
	 * <p>
	 * Die absoluten Koordinaten werden bestimmt durch die Position der Form
	 * {@code (x,y)} plus die
	 * {@link #getAnchorPoint(Options.Direction) relativen Koordinaten} des
	 * Ankers.
	 *
	 * <b>Wichtig:</b> Die Berechnung berücksichtigt derzeit keine Rotationen
	 * und Transformationen der Form.
	 *
	 * @param anchor Die Richtung des Ankers.
	 * @return Der absolute Ankerpunkt.
	 * @see #getAnchorPoint(double, double, Options.Direction)
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
	 * Unterklassen überschreiben diese Methode, um weitere Eigenschaften zu
	 * kopieren (zum Beispiel den Radius eines Kreises). Überschreibende
	 * Methoden sollten immer mit dem Aufruf {@code super.copyFrom(shape)} die
	 * Basiseigenschaften kopieren.
	 * <p>
	 * Die Methode kopiert so viele Eigenschaften wie möglich von der
	 * angegebenen Form in diese. Wenn die andere Form einen anderen Typ hat,
	 * dann werden trotzdem die Basiseigenschaften (Konturlinie, Füllung,
	 * Position, Rotation, Skalierung, Sichtbarkeit und Ankerpunkt) in diese
	 * Form kopiert. Soweit sinnvoll übernehmen implementierende Unterklassen
	 * auch andere Werte. Eine {@link Ellipse} kopiert beispielsweise auch die
	 * Breite und Höhe eines {@link Rectangle}.
	 * <p>
	 * Wird {@code null} übergeben, dann passiert nichts.
	 *
	 * @param shape Die Originalform, von der kopiert wird.
	 */
	public void copyFrom( Shape shape ) {
		if( shape != null ) {
			moveTo(shape.x, shape.y);
			setFillColor(shape.getFillColor());
			setStrokeColor(shape.getStrokeColor());
			setStrokeWeight(shape.getStrokeWeight());
			setStrokeType(shape.getStrokeType());
			setStrokeJoin(shape.getStrokeJoin());
			visible = shape.isVisible();
			rotation = shape.getRotation();
			scale(shape.getScale());
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
	 * Form zu erhalten. In der Regel besitzt aber jede Form einen Konstruktor,
	 * der alle Werte einer andern Form übernimmt. Die gezeigte Implementierung
	 * ist daher im Regelfall ausreichend.
	 *
	 * @return Eine genaue Kopie dieser Form.
	 */
	public abstract Shape copy();

	/**
	 * Gibt eine {@link java.awt.Shape Java-AWT Shape} Version dieser Form
	 * zurück. Intern werden die AWT Shapes benutzt, um sie auf den
	 * {@link Graphics2D Grafikkontext} zu zeichnen.
	 * <p>
	 * Wenn diese Form nicht durch eine AWT-Shape dargestellt wird, liefert die
	 * Methode {@code null}.
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

	/**
	 * Verschiebt die Form um die angegebenen Werte entlang der
	 * Koordinatenachsen.
	 *
	 * @param dx Verschiebung entlang der x-Achse.
	 * @param dy Verschiebung entlang der y-Achse.
	 */
	public void move( double dx, double dy ) {
		x += dx;
		y += dy;
	}

	/**
	 * Bewegt die Form an die angegebenen Koordinaten.
	 *
	 * @param x Die neue x-Koordinate.
	 * @param y Die neue y-Koordinate.
	 */
	public void moveTo( double x, double y ) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Bewegt die Form an dieselben Koordinaten wie die angegebene Form.
	 *
	 * @param shape Eine andere Form.
	 */
	public void moveTo( Shape shape ) {
		moveTo(shape.getX(), shape.getY());
	}

	/**
	 * Bewegt die Form zum angegebenen Ankerpunkt der angegebenen Form.
	 *
	 * @param shape Die andere Form.
	 * @param dir Die Richtung des Ankerpunktes.
	 * @see #moveTo(Shape, Options.Direction, double)
	 */
	public void moveTo( Shape shape, Options.Direction dir ) {
		moveTo(shape, dir, 0.0);
	}

	/**
	 * Bewegt den Ankerpunkt dieser Form zu einem Ankerpunkt einer anderen
	 * Form.
	 * <p>
	 * Mit {@code buff} wird ein zusätzlicher Abstand angegeben, um den die Form
	 * entlang des Ankerpunktes {@code anchor} verschoben wird.
	 * <p>
	 * Ist der Anker zum Beispiel {@code NORTH}, dann wird die Form um
	 * {@code buff} oberhalb der oberen Kante der zweiten Form verschoben.
	 * <p>
	 * Befinden sich die Formen zuvor in folgender Ausrichtung:
	 * <pre>
	 *                  ┌─────────┐
	 *                  │         │
	 *                  W    B    │
	 *      ┌─────┐     │         │
	 *      │     │     └─────────┘
	 *      W  A  │
	 *      │     │
	 *      └─────┘
	 * </pre>
	 * <p>
	 * bringt sie der Aufruf {@code B.moveTo(A, DOWN, 0)} in diese Ausrichtung:
	 * <pre>
	 *  B.moveTo(A, WEST, 0) │ B.moveTo(A, WEST, 10)
	 *                       │
	 *       ┌─────┬───┐     │    ┌┬────┬────┐
	 *       │     │   │     │    ││    │    │
	 *       │  A B│   │     │    ││ A  B    │
	 *       │     │   │     │    ││    │    │
	 *       └─────┴───┘     │    └┴────┴────┘
	 * </pre>
	 *
	 * @param shape Die andere Form.
	 * @param dir Die Richtung des Ankerpunktes.
	 * @param buff Der Abstand zum angegebenen Ankerpunkt.
	 */
	public void moveTo( Shape shape, Options.Direction dir, double buff ) {
		Point2D ap = shape.getAbsAnchorPoint(dir);

		this.x = ap.getX() + dir.x * buff;
		this.y = ap.getY() + dir.y * buff;
	}

	/**
	 * Bewegt die Form an den Rand der Zeichenfläche in der angegebenen
	 * Richtung.
	 *
	 * @param dir Die Richtung.
	 */
	public void alignTo( Options.Direction dir ) {
		alignTo(dir, 0.0);
	}

	/**
	 * Bewegt die Form mit dem angegebenen Abstand an den Rand der Zeichenfläche
	 * in der angegebenen Richtung aus.
	 *
	 * @param dir Die Richtung.
	 * @param buff Der Abstand zum Rand.
	 */
	public void alignTo( Options.Direction dir, double buff ) {
		Point2D anchorShape = Shape.getAnchorPoint(canvasWidth, canvasHeight, dir);
		Point2D anchorThis = this.getAbsAnchorPoint(dir);

		this.x += Math.abs(dir.x) * (anchorShape.getX() - anchorThis.getX()) + dir.x * buff;
		this.y += Math.abs(dir.y) * (anchorShape.getY() - anchorThis.getY()) + dir.y * buff;
	}

	/**
	 * Bewegt den Ankerpunkt dieser Form in der angegebenen Richtung zum
	 * Gleichen Ankerpunkt der anderen Form.
	 *
	 * @param shape Die andere Form.
	 * @param dir Die Richtung des Ankerpunktes.
	 * @see #alignTo(Shape, Options.Direction, double)
	 */
	public void alignTo( Shape shape, Options.Direction dir ) {
		alignTo(shape, dir, 0.0);
	}

	/**
	 * Richtet die Form entlang der angegebenen Richtung an einer anderen Form
	 * aus.
	 * <p>
	 * {@code buff} gibt einen Abstand ab, um den diese From versetzt
	 * ausgerichtet wird.
	 * <p>
	 * Für {@link #DOWN} wird beispielsweise die y-Koordinate der unteren Kante
	 * dieser Form an der unteren Kante von {@code shape} ausgerichtet. Die
	 * x-Koordinate wird in dem Fall nicht verändert.
	 * <p>
	 * Befinden sich die Formen beispielsweise in folgender Position:
	 * <pre>
	 *              ┌─────┐
	 *              │     │
	 *              │  B  │
	 *   ┌─────┐    │     │
	 *   │     │    └──D──┘
	 *   │  A  │
	 *   │     │
	 *   └──D──┘
	 * </pre>
	 * <p>
	 * <p>
	 * werden sie durch {@code alignTo} so positioniert:
	 * <pre>
	 * B.alignTo(A, EAST, 0)  │  B.alignTo(A, EAST, 10)
	 *                        │
	 *   ┌─────┐    ┌─────┐   │    ┌─────┐
	 *   │     │    │     │   │    │     │    ┌─────┐
	 *   │  A  │    │  B  │   │    │  A  │    │     │
	 *   │     │    │     │   │    │     │    │  B  │
	 *   └──D──┘    └──D──┘   │    └──D──┘    │     │
	 *                        │               └──D──┘
	 *                        │
	 * </pre>
	 *
	 * @param shape Die andere Form.
	 * @param dir Die Richtung.
	 * @param buff Der Abstand.
	 */
	public void alignTo( Shape shape, Options.Direction dir, double buff ) {
		Point2D anchorShape = shape.getAbsAnchorPoint(dir);
		Point2D anchorThis = this.getAbsAnchorPoint(dir);

		this.x += Math.abs(dir.x) * (anchorShape.getX() - anchorThis.getX()) + dir.x * buff;
		this.y += Math.abs(dir.y) * (anchorShape.getY() - anchorThis.getY()) + dir.y * buff;
	}

	/**
	 * @param shape
	 * @param dir
	 * @see #nextTo(Shape, Options.Direction, double)
	 */
	public void nextTo( Shape shape, Options.Direction dir ) {
		nextTo(shape, dir, DEFAULT_BUFFER);
	}

	/**
	 * Bewegt die Form neben eine andere in Richtung des angegebenen
	 * Ankerpunktes.
	 * <p>
	 * Im Gegensatz zu {@link #moveTo(Shape, Options.Direction, double)} wird
	 * die Breite bzw. Höhe der Formen berücksichtigt und die Formen so
	 * platziert, dass keine Überlappungen vorhanden sind.
	 * <p>
	 * Befinden sich die Formen zuvor in folgender Ausrichtung:
	 * <pre>
	 *                 ┌─────┐
	 *                 │     │
	 *                 W  B  │
	 * ┌──────┐        │     │
	 * │      │        └─────┘
	 * │  A   E
	 * │      │
	 * └──────┘
	 * </pre>
	 * <p>
	 * bringt sie der Aufruf {@code B.nextTo(A, EAST, 0)} in diese Ausrichtung:
	 * <pre>
	 * B.nextTo(A, EAST, 0) │  B.nextTo(A, EAST, 10)
	 *                      │
	 *    ┌─────┬─────┐     │     ┌─────┐ ┌─────┐
	 *    │     │     │     │     │     │ │     │
	 *    │  A  │  B  │     │     │  A  │ │  B  │
	 *    │     │     │     │     │     │ │     │
	 *    └─────┴─────┘     │     └─────┘ └─────┘
	 *                      │
	 * </pre>
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

	@Override
	public void setGradient( schule.ngb.zm.Color from, schule.ngb.zm.Color to, Options.Direction dir ) {
		Point2D apDir = getAbsAnchorPoint(dir);
		Point2D apInv = getAbsAnchorPoint(dir.inverse());
		setGradient(apInv.getX(), apInv.getY(), from, apDir.getX(), apDir.getY(), to);
	}

	@Override
	public void setGradient( schule.ngb.zm.Color from, schule.ngb.zm.Color to ) {
		Point2D ap = getAbsAnchorPoint(CENTER);
		setGradient(ap.getX(), ap.getY(), Math.min(ap.getX(), ap.getY()), from, to);
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
