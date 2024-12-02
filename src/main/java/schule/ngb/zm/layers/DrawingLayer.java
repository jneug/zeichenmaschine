package schule.ngb.zm.layers;

import schule.ngb.zm.Color;
import schule.ngb.zm.*;
import schule.ngb.zm.shapes.Text;
import schule.ngb.zm.util.io.ImageLoader;

import java.awt.*;
import java.awt.geom.*;
import java.util.Stack;

/**
 * Eine Ebene auf der direkt gezeichnet werden kann.
 * <p>
 * Ein {@code DrawingLayer} ist eine der drei Standardebenen der
 * {@link schule.ngb.zm.Zeichenmaschine}.
 */
@SuppressWarnings( "unused" )
public class DrawingLayer extends Layer implements Strokeable, Fillable {

	/**
	 * Wiederverwendbarer Speicher für eine Linie.
	 */
	protected Line2D.Double line = new Line2D.Double();

	/**
	 * Wiederverwendbarer Speicher für eine Ellipse.
	 */
	protected Ellipse2D.Double ellipse = new Ellipse2D.Double();

	/**
	 * Wiederverwendbarer Speicher für ein Rechteck.
	 */
	protected Rectangle2D.Double rect = new Rectangle2D.Double();

	/**
	 * Wiederverwendbarer Speicher für einen Kreisbogen.
	 */
	protected Arc2D.Double arc = new Arc2D.Double();

	/**
	 * Wiederverwendbarer Speicher für einen Pfad.
	 */
	protected Path2D.Double path = new Path2D.Double();

	/**
	 * Ob ein individueller Pfad gestartet wurde.
	 */
	private boolean pathStarted = false;

	/**
	 * Stapel für zwischengespeicherte Transformationsmatrizen.
	 */
	private final Stack<AffineTransform> transformStack;
	// private FontMetrics fontMetrics;

	/**
	 * Eine Shape, an die die Methoden für Farben und Konturlinien delegiert
	 * werden.
	 */
	private schule.ngb.zm.shapes.Text shapeDelegate;

	private final Stack<schule.ngb.zm.shapes.Text> styleStack;

	/**
	 * Erstellt eine Zeichenebene in der Standardgröße.
	 */
	public DrawingLayer() {
		super();
		transformStack = new Stack<>();
		transformStack.push(new AffineTransform());
		// fontMetrics = drawing.getFontMetrics();

		styleStack = new Stack<>();
		resetStyle();
	}

	/**
	 * Erstellt eine Zeichenebene mit der angegebenen Größe.
	 *
	 * @param width Die Breite der Ebene.
	 * @param height Die Höhe der Ebene.
	 */
	public DrawingLayer( int width, int height ) {
		super(width, height);
		transformStack = new Stack<>();
		transformStack.push(new AffineTransform());
		// fontMetrics = drawing.getFontMetrics();

		styleStack = new Stack<>();
		resetStyle();
	}

	/**
	 * Gibt die aktuelle Füllfarbe zurück.
	 *
	 * @return Die aktuelle Füllfarbe.
	 */
	public Color getFillColor() {
		return shapeDelegate.getFillColor();
	}

	/**
	 * Setzt die Füllfarbe auf die angegebene Farbe.
	 *
	 * @param color Die neue Füllfarbe oder {@code null}.
	 * @see Color
	 */
	public void setFillColor( Color color ) {
		shapeDelegate.setFillColor(color);
		drawing.setPaint(color);
	}

	/**
	 * Setzt die Füllfarbe auf die angegebene Farbe und setzt die Transparenz
	 * auf den angegebenen Wert. 0 is komplett durchsichtig und 255 komplett
	 * deckend.
	 *
	 * @param color Die neue Füllfarbe oder {@code null}.
	 * @param alpha Ein Transparenzwert zwischen 0 und 255.
	 * @see Color#Color(Color, int)
	 */
	public void setFillColor( Color color, int alpha ) {
		setFillColor(new Color(color, alpha));
	}

	/**
	 * Setzt die Füllfarbe auf einen Grauwert mit der angegebenen Intensität. 0
	 * entspricht schwarz, 255 entspricht weiß.
	 *
	 * @param gray Ein Grauwert zwischen 0 und 255.
	 * @see Color#Color(int)
	 */
	public void setFillColor( int gray ) {
		setFillColor(gray, gray, gray, 255);
	}

	/**
	 * Setzt die Füllfarbe auf einen Grauwert mit der angegebenen Intensität und
	 * dem angegebenen Transparenzwert. Der Grauwert 0 entspricht schwarz, 255
	 * entspricht weiß.
	 *
	 * @param gray Ein Grauwert zwischen 0 und 255.
	 * @param alpha Ein Transparenzwert zwischen 0 und 255.
	 * @see Color#Color(int, int)
	 */
	public void setFillColor( int gray, int alpha ) {
		setFillColor(gray, gray, gray, alpha);
	}

	/**
	 * Setzt die Füllfarbe auf die Farbe mit den angegebenen Rot-, Grün- und
	 * Blauanteilen.
	 *
	 * @param red Der Rotanteil der Farbe zwischen 0 und 255.
	 * @param green Der Grünanteil der Farbe zwischen 0 und 255.
	 * @param blue Der Blauanteil der Farbe zwischen 0 und 255.
	 * @see Color#Color(int, int, int)
	 * @see <a
	 * 	href="https://de.wikipedia.org/wiki/RGB-Farbraum">https://de.wikipedia.org/wiki/RGB-Farbraum</a>
	 */
	public void setFillColor( int red, int green, int blue ) {
		setFillColor(red, green, blue, 255);
	}

	/**
	 * Setzt die Füllfarbe auf die Farbe mit den angegebenen Rot-, Grün- und
	 * Blauanteilen und dem angegebenen Transparenzwert.
	 *
	 * @param red Der Rotanteil der Farbe zwischen 0 und 255.
	 * @param green Der Grünanteil der Farbe zwischen 0 und 255.
	 * @param blue Der Blauanteil der Farbe zwischen 0 und 255.
	 * @param alpha Ein Transparenzwert zwischen 0 und 25
	 * @see Color#Color(int, int, int, int)
	 * @see <a
	 * 	href="https://de.wikipedia.org/wiki/RGB-Farbraum">https://de.wikipedia.org/wiki/RGB-Farbraum</a>
	 */
	public void setFillColor( int red, int green, int blue, int alpha ) {
		setFillColor(new schule.ngb.zm.Color(red, green, blue, alpha));
	}

	/**
	 * Entfernt die Füllung der Form.
	 */
	public void noFill() {
		shapeDelegate.noFill();
	}

	/**
	 * Setzt die Füllfarbe auf den Standardwert zurück.
	 *
	 * @see schule.ngb.zm.Constants#DEFAULT_FILLCOLOR
	 */
	public void resetFill() {
		shapeDelegate.resetFill();
	}

	@Override
	public void setFill( Paint fill ) {
		shapeDelegate.setFill(fill);
	}

	@Override
	public Paint getFill() {
		return shapeDelegate.getFill();
	}

	/**
	 * Setzt die Füllung auf einen linearen Farbverlauf, der am Punkt
	 * ({@code fromX}, {@code fromY}) mit der Farbe {@code from} startet und am
	 * Punkt (({@code toX}, {@code toY}) mit der Farbe {@code to} endet.
	 *
	 * @param fromX x-Koordinate des Startpunktes.
	 * @param fromY y-Koordinate des Startpunktes.
	 * @param from Farbe am Startpunkt.
	 * @param toX x-Koordinate des Endpunktes.
	 * @param toY y-Koordinate des Endpunktes.
	 * @param to Farbe am Endpunkt.
	 */
	public void setGradient( double fromX, double fromY, Color from, double toX, double toY, Color to ) {
		shapeDelegate.setGradient(fromX, fromY, from, toX, toY, to);
	}

	/**
	 * Setzt die Füllung auf einen kreisförmigen (radialen) Farbverlauf, mit dem
	 * Zentrum im Punkt ({@code centerX}, {@code centerY}) und dem angegebenen
	 * Radius. Der Verlauf starte im Zentrum mit der Farbe {@code from} und
	 * endet am Rand des durch den Radius beschriebenen Kreises mit der Farbe
	 * {@code to}.
	 *
	 * @param centerX x-Koordinate des Kreismittelpunktes.
	 * @param centerY y-Koordinate des Kreismittelpunktes.
	 * @param radius Radius des Kreises.
	 * @param from Farbe im Zentrum des Kreises.
	 * @param to Farbe am Rand des Kreises.
	 */
	public void setGradient( double centerX, double centerY, double radius, Color from, Color to ) {
		shapeDelegate.setGradient(centerX, centerY, radius, from, to);
	}

	@Override
	public void setGradient( Color from, Color to, Options.Direction dir ) {
		Point2D.Double anchorPoint = schule.ngb.zm.shapes.Shape.getAnchorPoint(getWidth(), getHeight(), dir);
		Point2D.Double inversePoint = schule.ngb.zm.shapes.Shape.getAnchorPoint(getWidth(), getHeight(), dir.inverse());
		shapeDelegate.setGradient(inversePoint.x, inversePoint.y, from, anchorPoint.x, anchorPoint.y, to);
	}

	@Override
	public void setGradient( Color from, Color to ) {
		shapeDelegate.setGradient(getWidth() / 2.0, getHeight() / 2.0, min(getWidth(), getHeight()) / 2.0, from, to);
	}

	/**
	 * Entfernt einen Farbverlauf von der Füllung.
	 */
	public void noGradient() {
		shapeDelegate.noGradient();
	}

	/**
	 * Setzt den Linienstil für Konturlinien direkt auf das angegebene
	 * {@code Stroke}-Objekt.
	 *
	 * @param stroke Ein {@code Stroke}-Objekt.
	 */
	@Override
	public void setStroke( Stroke stroke ) {
		shapeDelegate.setStroke(stroke);
	}

	/**
	 * Gibt die aktuelle Farbe der Konturlinie zurück.
	 *
	 * @return Die Konturfarbe oder {@code null}.
	 */
	public Color getStrokeColor() {
		return shapeDelegate.getStrokeColor();
	}

	/**
	 * Setzt die Farbe der Konturlinie auf die angegebene Farbe.
	 *
	 * @param color Die neue Farbe der Konturlinie.
	 * @see Color
	 */
	public void setStrokeColor( Color color ) {
		shapeDelegate.setStrokeColor(color);
		drawing.setPaint(color);
	}

	/**
	 * Setzt die Farbe der Konturlinie auf die angegebene Farbe und setzt die
	 * Transparenz auf den angegebenen Wert. 0 is komplett durchsichtig und 255
	 * komplett deckend.
	 *
	 * @param color Die neue Farbe der Konturlinie oder {@code null}.
	 * @param alpha Ein Transparenzwert zwischen 0 und 255.
	 * @see Color#Color(Color, int)
	 */
	public void setStrokeColor( Color color, int alpha ) {
		setStrokeColor(new Color(color, alpha));
	}

	/**
	 * Setzt die Farbe der Konturlinie auf einen Grauwert mit der angegebenen
	 * Intensität. 0 entspricht schwarz, 255 entspricht weiß.
	 *
	 * @param gray Ein Grauwert zwischen 0 und 255.
	 * @see Color#Color(int)
	 */
	public void setStrokeColor( int gray ) {
		setStrokeColor(gray, gray, gray, 255);
	}

	/**
	 * Setzt die Farbe der Konturlinie auf einen Grauwert mit der angegebenen
	 * Intensität und dem angegebenen Transparenzwert. Der Grauwert 0 entspricht
	 * schwarz, 255 entspricht weiß.
	 *
	 * @param gray Ein Grauwert zwischen 0 und 255.
	 * @param alpha Ein Transparenzwert zwischen 0 und 255.
	 * @see Color#Color(int, int)
	 */
	public void setStrokeColor( int gray, int alpha ) {
		setStrokeColor(gray, gray, gray, alpha);
	}

	/**
	 * Setzt die Farbe der Konturlinie auf die Farbe mit den angegebenen Rot-,
	 * Grün- und Blauanteilen.
	 *
	 * @param red Der Rotanteil der Farbe zwischen 0 und 255.
	 * @param green Der Grünanteil der Farbe zwischen 0 und 255.
	 * @param blue Der Blauanteil der Farbe zwischen 0 und 255.
	 * @see Color#Color(int, int, int)
	 * @see <a
	 * 	href="https://de.wikipedia.org/wiki/RGB-Farbraum">https://de.wikipedia.org/wiki/RGB-Farbraum</a>
	 */
	public void setStrokeColor( int red, int green, int blue ) {
		setStrokeColor(red, green, blue, 255);
	}

	/**
	 * Setzt die Farbe der Konturlinie auf die Farbe mit den angegebenen Rot-,
	 * Grün- und Blauanteilen und dem angegebenen Transparenzwert.
	 *
	 * @param red Der Rotanteil der Farbe zwischen 0 und 255.
	 * @param green Der Grünanteil der Farbe zwischen 0 und 255.
	 * @param blue Der Blauanteil der Farbe zwischen 0 und 255.
	 * @param alpha Ein Transparenzwert zwischen 0 und 25
	 * @see Color#Color(int, int, int, int)
	 * @see <a
	 * 	href="https://de.wikipedia.org/wiki/RGB-Farbraum">https://de.wikipedia.org/wiki/RGB-Farbraum</a>
	 */
	public void setStrokeColor( int red, int green, int blue, int alpha ) {
		setStrokeColor(new schule.ngb.zm.Color(red, green, blue, alpha));
	}

	/**
	 * Entfernt die Kontur der Form.
	 */
	public void noStroke() {
		shapeDelegate.noStroke();
	}

	/**
	 * Setzt die Farbe der Konturlinie auf die Standardwerte zurück.
	 *
	 * @see schule.ngb.zm.Constants#DEFAULT_STROKECOLOR
	 * @see schule.ngb.zm.Constants#DEFAULT_STROKEWEIGHT
	 * @see schule.ngb.zm.Constants#SOLID
	 */
	public void resetStroke() {
		shapeDelegate.resetStroke();
	}

	/**
	 * Gibt die Dicke der Konturlinie zurück.
	 *
	 * @return Die aktuelle Dicke der Linie.
	 */
	public double getStrokeWeight() {
		return shapeDelegate.getStrokeWeight();
	}

	/**
	 * Setzt die Dicke der Konturlinie. Die Dicke muss größer 0 sein. Wird 0
	 * übergeben, dann wird keine Kontur mehr angezeigt.
	 *
	 * @param weight Die Dicke der Konturlinie.
	 */
	public void setStrokeWeight( double weight ) {
		shapeDelegate.setStrokeWeight(weight);
		drawing.setStroke(shapeDelegate.getStroke());
	}

	/**
	 * Gibt die Art der Konturlinie zurück.
	 *
	 * @return Die aktuelle Art der Konturlinie.
	 * @see Options.StrokeType
	 */
	public Options.StrokeType getStrokeType() {
		return shapeDelegate.getStrokeType();
	}

	@Override
	public Options.StrokeJoin getStrokeJoin() {
		return shapeDelegate.getStrokeJoin();
	}

	/**
	 * Setzt den Typ der Kontur. Erlaubte Werte sind {@link #DASHED},
	 * {@link #DOTTED} und {@link #SOLID}.
	 *
	 * @param type Eine der möglichen Konturarten.
	 * @see Options.StrokeType
	 */
	public void setStrokeType( Options.StrokeType type ) {
		shapeDelegate.setStrokeType(type);
	}

	@Override
	public Stroke getStroke() {
		return shapeDelegate.getStroke();
	}

	/**
	 * Setzt den Standard-ANker für die Zeichenebene auf die angegebene
	 * Richtung.
	 * <p>
	 * Zu Beginn ist der Standardanker immer auf
	 * {@link Options.Direction#CENTER CENTER} gesetzt. Alle Formen werden von
	 * der Mitte aus gezeichnet. Wir der Anker mit {@code setAnchor(NORTHWEST)}
	 * beispielsweise auf {@link Options.Direction#NORTHWEST NORTHWEST} gesetzt,
	 * werden alle Formen von der linken oberen Ecke aus gezeichnet.
	 *
	 * @param anchor Der neue Anker.
	 */
	public void setAnchor( Options.Direction anchor ) {
		shapeDelegate.setAnchor(anchor);
	}

	/**
	 * Übermalt die komplette Ebene mit einem Grauwert mit der angegebenen
	 * Intensität. 0 entspricht schwarz, 255 entspricht weiß.
	 *
	 * @param gray Ein Grauwert zwischen 0 und 255.
	 * @see Color#Color(int)
	 */
	public void clear( int gray ) {
		clear(gray, gray, gray, 255);
	}

	/**
	 * Übermalt die komplette Ebene mit einem Grauwert mit der angegebenen
	 * Intensität und dem angegebenen Transparenzwert. Der Grauwert 0 entspricht
	 * schwarz, 255 entspricht weiß.
	 *
	 * @param gray Ein Grauwert zwischen 0 und 255.
	 * @param alpha Ein Transparenzwert zwischen 0 und 255.
	 * @see Color#Color(int, int)
	 */
	public void clear( int gray, int alpha ) {
		clear(gray, gray, gray, alpha);
	}

	/**
	 * Übermalt die komplette Ebene mit der Farbe mit den angegebenen Rot-,
	 * Grün- und Blauanteilen.
	 *
	 * @param red Der Rotanteil der Farbe zwischen 0 und 255.
	 * @param green Der Grünanteil der Farbe zwischen 0 und 255.
	 * @param blue Der Blauanteil der Farbe zwischen 0 und 255.
	 * @see Color#Color(int, int, int)
	 * @see <a
	 * 	href="https://de.wikipedia.org/wiki/RGB-Farbraum">https://de.wikipedia.org/wiki/RGB-Farbraum</a>
	 */
	public void clear( int red, int green, int blue ) {
		clear(red, green, blue, 255);
	}

	/**
	 * Übermalt die komplette Ebene mit der Farbe mit den angegebenen Rot-,
	 * Grün- und Blauanteilen und dem angegebenen Transparenzwert.
	 *
	 * @param red Der Rotanteil der Farbe zwischen 0 und 255.
	 * @param green Der Grünanteil der Farbe zwischen 0 und 255.
	 * @param blue Der Blauanteil der Farbe zwischen 0 und 255.
	 * @param alpha Ein Transparenzwert zwischen 0 und 25
	 * @see Color#Color(int, int, int, int)
	 * @see <a
	 * 	href="https://de.wikipedia.org/wiki/RGB-Farbraum">https://de.wikipedia.org/wiki/RGB-Farbraum</a>
	 */
	public void clear( int red, int green, int blue, int alpha ) {
		clear(new schule.ngb.zm.Color(red, green, blue, alpha));
	}

	/**
	 * Übermalt die komplette Ebene mit der angegebenen Farbe.
	 *
	 * @param color Die neue Füllfarbe oder {@code null}.
	 * @see Color
	 */
	public void clear( schule.ngb.zm.Color color ) {
        /*graphics.setBackground(pColor);
        graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());*/
		java.awt.Color currentColor = drawing.getColor();
		pushMatrix();
		resetMatrix();
		drawing.setColor(color.getJavaColor());
		drawing.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
		drawing.setColor(currentColor);
		popMatrix();
	}

	/**
	 * Zeichnet eine gerade Line zwischen den angegebenen Koordinaten.
	 *
	 * @param x1 Die x-Koordinate des Startpunktes.
	 * @param y1 Die y-Koordinate des Startpunktes.
	 * @param x2 Die x-Koordinate des Endpunktes.
	 * @param y2 Die y-Koordinate des Endpunktes.
	 */
	public void line( double x1, double y1, double x2, double y2 ) {
		line.setLine(x1, y1, x2, y2);
		drawShape(line);
	}

	/**
	 * Färbt den Pixel an der angegebenen Koordinate ein.
	 *
	 * @param x Die x-Koordinate.
	 * @param y Die y-Koordinate.
	 */
	public void pixel( double x, double y ) {
		buffer.setRGB((int) x, (int) y, shapeDelegate.getFontColor().getRGBA());
	}

	/**
	 * Zeichnet ein Quadrat an den angegebenen Koordinaten mit der angegebenen
	 * Kantenlänge in die Zeichenebene.
	 * <p>
	 * Als Ankerpunkt wird der Standardanker verwendet.
	 *
	 * @param x Die x-Koordinate des Ankerpunktes.
	 * @param y Die y-Koordinate des Ankerpunktes.
	 * @param w Die Kantenlänge des Quadrats.
	 * @see #square(double, double, double, Options.Direction)
	 */
	public void square( double x, double y, double w ) {
		rect(x, y, w, w, shapeDelegate.getAnchor());
	}

	/**
	 * Zeichnet ein Quadrat an den angegebenen Koordinaten mit der angegebenen
	 * Kantenlänge und dem angegebenen Ankerpunkt in die Zeichenebene.
	 *
	 * @param x Die x-Koordinate des Ankerpunktes.
	 * @param y Die y-Koordinate des Ankerpunktes.
	 * @param w Die Kantenlänge des Quadrats.
	 * @see #square(double, double, double, Options.Direction)
	 */
	public void square( double x, double y, double w, Options.Direction anchor ) {
		rect(x, y, w, w, anchor);
	}

	/**
	 * Zeichnet ein Rechteck an den angegebenen Koordinaten mit der angegebenen
	 * Breite und Höhe in die Zeichenebene.
	 * <p>
	 * Als Ankerpunkt wird der Standardanker verwendet.
	 *
	 * @param x x-Koordinate des Rechtecks.
	 * @param y y-Koordinate des Rechtecks.
	 * @param w Breite des Rechtecks.
	 * @param h Höhe des Rechtecks.
	 */
	public void rect( double x, double y, double w, double h ) {
		rect(x, y, w, h, shapeDelegate.getAnchor());
	}

	/**
	 * Zeichnet ein Rechteck an den angegebenen Koordinaten mit der angegebenen
	 * Breite und Höhe und dem angegebenen Ankerpunkt in die Zeichenebene.
	 *
	 * @param x x-Koordinate des Rechtecks.
	 * @param y y-Koordinate des Rechtecks.
	 * @param w Breite des Rechtecks.
	 * @param h Höhe des Rechtecks.
	 * @param anchor Ankerpunkt de Rechtecks.
	 */
	public void rect( double x, double y, double w, double h, Options.Direction anchor ) {
		Point2D.Double anchorPoint = this.getOriginPoint(x, y, w, h, anchor);
		rect.setRect(anchorPoint.getX(), anchorPoint.getY(), w, h);
		fillShape(rect);
		drawShape(rect);
	}

	/**
	 * Zeichnet ein Rechteck mit abgerundeten Ecken an den angegebenen
	 * Koordinaten mit der angegebenen Breite und Höhe in die Zeichenebene.
	 * <p>
	 * Jede Ecke wird als Viertelkreis eines Kreises mit dem Radius
	 * {@code radius} gezeichnet.
	 *
	 * @param x x-Koordinate des Rechtecks.
	 * @param y y-Koordinate des Rechtecks.
	 * @param w Breite des Rechtecks.
	 * @param h Höhe des Rechtecks.
	 * @param radius Radius der Eckenkreise.
	 */
	public void roundedRect( double x, double y, double w, double h, double radius ) {
		roundedRect(x, y, w, h, radius, shapeDelegate.getAnchor());
	}

	/**
	 * Zeichnet ein Rechteck mit abgerundeten Ecken an den angegebenen
	 * Koordinaten mit der angegebenen Breite und Höhe und dem angegebenen
	 * Ankerpunkt in die Zeichenebene.
	 * <p>
	 * Jede Ecke wird als Viertelkreis eines Kreises mit dem Radius
	 * {@code radius} gezeichnet.
	 *
	 * @param x x-Koordinate des Rechtecks.
	 * @param y y-Koordinate des Rechtecks.
	 * @param w Breite des Rechtecks.
	 * @param h Höhe des Rechtecks.
	 * @param radius Radius der Eckenkreise.
	 * @param anchor Ankerpunkt de Rechtecks.
	 */
	public void roundedRect( double x, double y, double w, double h, double radius, Options.Direction anchor ) {
		Point2D.Double anchorPoint = this.getOriginPoint(x, y, w, h, anchor);
		Shape rect = new RoundRectangle2D.Double(anchorPoint.getX(), anchorPoint.getY(), w, h, radius, radius);
		fillShape(rect);
		drawShape(rect);
	}

	/**
	 * Zeichnet einen Punkt in die Zeichenebene.
	 * <p>
	 * Im Gegensatz zu einem {@link #pixel(double, double) Pixel}, besitzt ein
	 * Punkt einen Radius und nimmt mehr als ein Pixel auf der Ebene ein.
	 *
	 * @param x x-Koordinate des Punktes.
	 * @param y y-Koordinate des Punktes.
	 */
	public void point( double x, double y ) {
		ellipse(x - 1, y - 1, 2, 2, shapeDelegate.getAnchor());
	}

	/**
	 * Zeichnet einen Kreis an den angegebenen Koordinaten mit dem angegebenen
	 * Radius.
	 *
	 * @param x
	 * @param y
	 * @param r
	 */
	public void circle( double x, double y, double r ) {
		ellipse(x, y, r + r, r + r, shapeDelegate.getAnchor());
	}

	/**
	 * Zeichnet einen Kreis an den angegebenen Koordinaten mit dem angegebenen
	 * Radius und dem angegebenen Ankerpunkt.
	 *
	 * @param x
	 * @param y
	 * @param r
	 */
	public void circle( double x, double y, double r, Options.Direction anchor ) {
		ellipse(x, y, r + r, r + r, anchor);
	}

	/**
	 * Zeichnet eine Ellipse in die Zeichenebene.
	 *
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public void ellipse( double x, double y, double w, double h ) {
		ellipse(x, y, w, h, shapeDelegate.getAnchor());
	}

	/**
	 * Zeichnet eine Ellipse in die Zeichenebene.
	 *
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param anchor
	 */
	public void ellipse( double x, double y, double w, double h, Options.Direction anchor ) {
		Point2D.Double anchorPoint = getOriginPoint(x, y, w, h, anchor);
		ellipse.setFrame(anchorPoint.x, anchorPoint.y, w, h);
		fillShape(ellipse);
		drawShape(ellipse);
	}

	/**
	 * Zeichnet einen Kreisbogen in die Zeichenebene.
	 * <p>
	 * Der Kreisbogen liegt auf einem Kreis mit dem angegebenen Radius, beginnt
	 * beim angegebenen Winkel und läuft bis zum zweiten Winkel.
	 *
	 * @param x
	 * @param y
	 * @param r
	 * @param angle1
	 * @param angle2
	 */
	public void arc( double x, double y, double r, double angle1, double angle2 ) {
		arc(x, y, r + r, r + r, angle1, angle2);
	}

	/**
	 * Zeichnet einen Ellipsenbogen in die Zeichenebene.
	 * <p>
	 * Der Ellipsenbogen liegt auf einer Ellipse mit der angegebenen Breite und
	 * Höhe, beginnt beim angegebenen Winkel und läuft bis zum zweiten Winkel.
	 *
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param angle1
	 * @param angle2
	 */
	public void arc( double x, double y, double w, double h, double angle1, double angle2 ) {
		while( angle2 < angle1 ) angle2 += 360.0;

		Point2D.Double anchorPoint = getOriginPoint(x, y, w, h, CENTER);
		arc.setArc(
			anchorPoint.x, anchorPoint.y,
			w, h,
			//Math.toRadians(angle1), Math.toRadians(angle2 - angle1),
			angle1, angle2 - angle1,
			Arc2D.OPEN
		);

		drawShape(arc);
	}

	/**
	 * Zeichnet einen Kreisausschnitt in die Zeichenebene.
	 *
	 * @param x
	 * @param y
	 * @param r
	 * @param angle1
	 * @param angle2
	 */
	public void pie( double x, double y, double r, double angle1, double angle2 ) {
		while( angle2 < angle1 ) angle2 += 360.0;

		double d = r + r;

		Point2D.Double anchorPoint = getOriginPoint(x, y, d, d, CENTER);
		arc.setArc(
			anchorPoint.x, anchorPoint.y,
			d, d,
			angle1, angle2 - angle1,
			Arc2D.PIE
		);

		fillShape(arc);
		drawShape(arc);
	}

	/**
	 * Zeichnet eine quadratische Bézierkurve mit den angegebenen Start- und
	 * Endkoordinaten, sowie dem angegebenen Kontrollpunkt.
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 */
	public void curve( double x1, double y1, double x2, double y2, double x3, double y3 ) {
		QuadCurve2D curve = new QuadCurve2D.Double(x1, y1, x2, y2, x3, y3);
		drawShape(curve);
	}

	/**
	 * Zeichnet eine kubische Bézierkurve mit den angegebenen Start- und
	 * Endkoordinaten, sowie den angegebenen Kontrollpunkten.
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @param x4
	 * @param y4
	 */
	public void curve( double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4 ) {
		CubicCurve2D curve = new CubicCurve2D.Double(x1, y1, x2, y2, x3, y3, x4, y4);
		drawShape(curve);
	}

	/**
	 * Zeichnet ein Dreieck mit den angegebenen Eckpunkten in die Zeichenebene.
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 */
	public void triangle( double x1, double y1, double x2, double y2, double x3, double y3 ) {
		Path2D path = new Path2D.Double();
		path.moveTo(x1, y1);
		path.lineTo(x2, y2);
		path.lineTo(x3, y3);
		path.lineTo(x1, y1);

		fillShape(path);
		drawShape(path);
	}

	/**
	 * Zeichnet einen Rhombus in die Zeichenebene.
	 *
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void rhombus( double x, double y, double width, double height ) {
		rhombus(x, y, width, height, shapeDelegate.getAnchor());
	}

	public void rhombus( double x, double y, double width, double height, Options.Direction anchor ) {
		double whalf = width / 2.0, hhalf = height / 2.0;
		Point2D.Double anchorPoint = getOriginPoint(x, y, width, height, anchor);
		polygon(anchorPoint.x + whalf, anchorPoint.y, anchorPoint.x + width, anchorPoint.y + hhalf, anchorPoint.x + whalf, anchorPoint.y + height, anchorPoint.x, anchorPoint.y + hhalf);
	}

	public void polygon( double... coordinates ) {
		if( coordinates.length < 4 ) {
			return;
		}

		Path2D path = new Path2D.Double();
		path.moveTo(coordinates[0], coordinates[1]);
		for( int i = 2; i < coordinates.length; i += 2 ) {
			if( i + 1 < coordinates.length ) {
				path.lineTo(coordinates[i], coordinates[i + 1]);
			}
		}

		int len = coordinates.length;
		if( coordinates[len - 2] != coordinates[0] || coordinates[len - 1] != coordinates[1] ) {
			path.lineTo(coordinates[0], coordinates[1]);
		}

		fillShape(path);
		drawShape(path);
	}

	public void polygon( Point2D... points ) {
		if( points.length < 2 ) {
			return;
		}

		Path2D path = new Path2D.Double();
		path.moveTo(points[0].getX(), points[0].getY());
		for( int i = 1; i < points.length; i += 1 ) {
			path.lineTo(points[i].getX(), points[i].getY());
		}

		int len = points.length;
		if( points[len - 1].equals(points[0]) ) {
			path.moveTo(points[0].getX(), points[0].getY());
		}

		fillShape(path);
		drawShape(path);
	}

	protected void fillShape( Shape shape ) {
		if( shapeDelegate.getFill() != null ) {
			drawing.setPaint(shapeDelegate.getFill());
			drawing.fill(shape);
		}
	}

	protected void drawShape( Shape shape ) {
		if( shapeDelegate.hasStroke() ) {
			drawing.setColor(shapeDelegate.getStrokeColor().getJavaColor());
			drawing.setStroke(shapeDelegate.getStroke());
			drawing.draw(shape);
		}
	}

	/**
	 * Startet eine neue Freihand-Form.
	 */
	public void beginShape() {
		path.reset();
		pathStarted = false;
	}

	/**
	 * Fügt einer zuvor {@link #beginShape() begonnenen} Freihand-Form eine
	 * Linie zu den angegebenen Koordinaten hinzu.
	 * <p>
	 * Wurde seit dem Start der Form noch keine Linie hinzugefügt, wird zunächst
	 * nur die angegebene Koordinate als Startpunkt der Freihand-Form gesetzt.
	 *
	 * @param x Die x-Koordinate.
	 * @param y Die y-Koordinate.
	 */
	public void lineTo( double x, double y ) {
		if( !pathStarted ) {
			path.moveTo(x, y);
			pathStarted = true;
		} else {
			path.lineTo(x, y);
		}
	}

	public void curveTo( double ctrlX, double ctrlY, double x, double y ) {
		if( !pathStarted ) {
			path.moveTo(x, y);
			pathStarted = true;
		} else {
			path.quadTo(
				ctrlX, ctrlY,
				x, y
			);
		}
	}

	public void curveTo( double ctrlX1, double ctrlY1, double ctrlX2, double ctrlY2, double x, double y ) {
		if( !pathStarted ) {
			path.moveTo(x, y);
			pathStarted = true;
		} else {
			path.curveTo(
				ctrlX1, ctrlY1,
				ctrlX2, ctrlY2,
				x, y
			);
		}
	}

	/**
	 * Beendet eine zuvor {@link #beginShape() begonnene} Freihand-Form und
	 * zeichent sie auf die Zeichenebene.
	 */
	public void endShape() {
		endShape(CLOSED);
	}

	public void endShape( Options.PathType closingType ) {
		if( closingType == Options.PathType.CLOSED ) {
			path.closePath();
		}
		path.trimToSize();
		pathStarted = false;

		fillShape(path);
		drawShape(path);
	}

	/**
	 * Zeichnet das Bild von der angegebenen Quelle an der angegebenen Position
	 * auf die Zeichenebene.
	 * <p>
	 * Die Bildquelle wird mithilfe von {@link ImageLoader#loadImage(String)}
	 * geladen. Schlägt dies fehl, wird nichts gezeichnet.
	 *
	 * @param imageSource Die Bildquelle.
	 * @param x x-Koordinate des Ankerpunktes.
	 * @param y y-Koordinate des Ankerpunktes.
	 * @see ImageLoader#loadImage(String)
	 */
	public void image( String imageSource, double x, double y ) {
		imageScale(ImageLoader.loadImage(imageSource), x, y, 1.0, shapeDelegate.getAnchor());
	}

	/**
	 * Zeichnet das Bild von der angegebenen Quelle an der angegebenen Position
	 * auf die Zeichenebene.
	 * <p>
	 * Die Bildquelle wird mithilfe von {@link ImageLoader#loadImage(String)}
	 * geladen. Schlägt dies fehl, wird nichts gezeichnet.
	 *
	 * @param imageSource Die Bildquelle.
	 * @param x x-Koordinate des Ankerpunktes.
	 * @param y y-Koordinate des Ankerpunktes.
	 * @param anchor Der Ankerpunkt.
	 * @see ImageLoader#loadImage(String)
	 */
	public void image( String imageSource, double x, double y, Options.Direction anchor ) {
		imageScale(ImageLoader.loadImage(imageSource), x, y, 1.0, anchor);
	}

	/**
	 * Zeichnet das Bild von der angegebenen Quelle an den angegebenen
	 * Koordinaten auf die Zeichenebene. Das Bild wird um den angegebenen Faktor
	 * skaliert.
	 * <p>
	 * Siehe
	 * {@link #imageScale(Image, double, double, double, Options.Direction)} für
	 * mehr Details.
	 *
	 * @param imageSource Die Bildquelle.
	 * @param x x-Koordinate des Ankerpunktes.
	 * @param y y-Koordinate des Ankerpunktes.
	 * @param scale Der Skalierungsfaktor des Bildes.
	 * @see ImageLoader#loadImage(String)
	 */
	public void imageScale( String imageSource, double x, double y, double scale ) {
		imageScale(ImageLoader.loadImage(imageSource), x, y, scale, shapeDelegate.getAnchor());
	}

	/**
	 * Zeichnet das Bild von der angegebenen Quelle an den angegebenen
	 * Koordinaten auf die Zeichenebene. Das Bild wird um den angegebenen Faktor
	 * skaliert und der angegebene Ankerpunkt verwendet.
	 * <p>
	 * Siehe
	 * {@link #imageScale(Image, double, double, double, Options.Direction)} für
	 * mehr Details.
	 *
	 * @param imageSource Die Bildquelle.
	 * @param x x-Koordinate des Ankerpunktes.
	 * @param y y-Koordinate des Ankerpunktes.
	 * @param scale Der Skalierungsfaktor des Bildes.
	 * @param anchor Der Ankerpunkt.
	 * @see ImageLoader#loadImage(String)
	 */
	public void imageScale( String imageSource, double x, double y, double scale, Options.Direction anchor ) {
		imageScale(ImageLoader.loadImage(imageSource), x, y, scale, anchor);
	}

	/**
	 * Zeichnet das angegebene Bild an den angegebenen Koordinaten auf die
	 * Zeichenebene.
	 *
	 * @param image Das vorher geladene Bild.
	 * @param x x-Koordinate des Ankerpunktes.
	 * @param y y-Koordinate des Ankerpunktes.
	 */
	public void image( Image image, double x, double y ) {
		imageScale(image, x, y, 1.0, shapeDelegate.getAnchor());
	}

	/**
	 * Zeichnet das angegebene Bild an den angegebenen Koordinaten auf die
	 * Zeichenebene. Das Bild wird um den angegebenen Faktor skaliert.
	 * <p>
	 * Siehe
	 * {@link #imageScale(Image, double, double, double, Options.Direction)} für
	 * mehr Details.
	 *
	 * @param image Das vorher geladene Bild.
	 * @param x x-Koordinate des Ankerpunktes.
	 * @param y y-Koordinate des Ankerpunktes.
	 * @param scale Der Skalierungsfaktor des Bildes.
	 */
	public void imageScale( Image image, double x, double y, double scale ) {
		imageScale(image, x, y, scale, shapeDelegate.getAnchor());
	}

	/**
	 * Zeichnet das angegebene Bild an den angegebenen Koordinaten auf die
	 * Zeichenebene. Das Bild wird um den angegebenen Faktor skaliert und der
	 * angegebene Ankerpunkt verwendet.
	 * <p>
	 * Bei einem Faktor größer 0 wird das Bild vergrößert, bei einem Faktor
	 * kleiner 0 verkleinert. Bei negativen Werten wird das Bild entlang der x-
	 * bzw. y-Achse gespiegelt.
	 * <p>
	 * Das Seitenverhältnis wird immer beibehalten.
	 * <p>
	 * Soll das Bild innerhalb eines vorgegebenen Rechtecks liegen, sollte
	 * {@link #imageScale(Image, double, double, double, double,
	 * Options.Direction)} verwendet werden.
	 *
	 * @param image Das vorher geladene Bild.
	 * @param x x-Koordinate des Ankerpunktes.
	 * @param y y-Koordinate des Ankerpunktes.
	 * @param scale Der Skalierungsfaktor des Bildes.
	 * @param anchor Der Ankerpunkt.
	 */
	public void imageScale( Image image, double x, double y, double scale, Options.Direction anchor ) {
		/*if( image != null ) {
			double neww = image.getWidth(null) * scale;
			double newh = image.getHeight(null) * scale;
			Point2D.Double anchorPoint = getOriginPoint(x, y, neww, newh, anchor);
			drawing.drawImage(image, (int) anchorPoint.x, (int) anchorPoint.y, (int) neww, (int) newh, null);
		}*/
		double neww = image.getWidth(null) * scale;
		double newh = image.getHeight(null) * scale;
		imageScale(image, x, y, neww, newh, anchor);
	}

	/**
	 * Zeichnet das Bild von der angegebenen Quelle an den angegebenen
	 * Koordinaten in der angegebenen Größe auf die Zeichenebene.
	 * <p>
	 * Siehe
	 * {@link #imageScale(Image, double, double, double, double,
	 * Options.Direction)} für mehr Details.
	 *
	 * @param imageSource Die Bildquelle.
	 * @param x x-Koordinate des Ankerpunktes.
	 * @param y y-Koordinate des Ankerpunktes.
	 * @param width Breite des Bildes auf der Zeichenebene oder 0.
	 * @param height Höhe des Bildes auf der Zeichenebene oder 0.
	 * @see ImageLoader#loadImage(String)
	 */
	public void imageScale( String imageSource, double x, double y, double width, double height ) {
		imageScale(ImageLoader.loadImage(imageSource), x, y, width, height, shapeDelegate.getAnchor());
	}

	/**
	 * Zeichnet das Bild von der angegebenen Quelle an den angegebenen
	 * Koordinaten in der angegebenen Größe auf die Zeichenebene. Es wird der
	 * angegebene Ankerpunkt verwendet.
	 * <p>
	 * Siehe
	 * {@link #imageScale(Image, double, double, double, double,
	 * Options.Direction)} für mehr Details.
	 *
	 * @param imageSource Die Bildquelle.
	 * @param x x-Koordinate des Ankerpunktes.
	 * @param y y-Koordinate des Ankerpunktes.
	 * @param width Breite des Bildes auf der Zeichenebene oder 0.
	 * @param height Höhe des Bildes auf der Zeichenebene oder 0.
	 * @param anchor Der Ankerpunkt.
	 * @see ImageLoader#loadImage(String)
	 */
	public void imageScale( String imageSource, double x, double y, double width, double height, Options.Direction anchor ) {
		imageScale(ImageLoader.loadImage(imageSource), x, y, width, height, anchor);
	}

	/**
	 * Zeichnet das angegebene Bild an den angegebenen Koordinaten in der
	 * angegebenen Größe auf die Zeichenebene.
	 * <p>
	 * Siehe
	 * {@link #imageScale(Image, double, double, double, double,
	 * Options.Direction)} für mehr Details.
	 *
	 * @param image Ein Bild-Objekt.
	 * @param x x-Koordinate des Ankerpunktes.
	 * @param y y-Koordinate des Ankerpunktes.
	 * @param width Breite des Bildes auf der Zeichenebene oder 0.
	 * @param height Höhe des Bildes auf der Zeichenebene oder 0.
	 */
	public void imageScale( Image image, double x, double y, double width, double height ) {
		imageScale(image, x, y, width, height, shapeDelegate.getAnchor());
	}

	/**
	 * Zeichnet das angegebene Bild an den angegebenen Koordinaten in der
	 * angegebenen Größe auf die Zeichenebene. Der angegebene Ankerpunkt wird
	 * verwendet.
	 * <p>
	 * Das Bild wird innerhalb eines Rechtecks mit der angegebenen Breite und
	 * Höhe gezeichnet. Dabei wird das Abbild verzerrt, wenn beim Aufruf nicht
	 * auf ein passendes Seitenverhältnis der Werte zueinander geachtet wird.
	 * <p>
	 * Um das Bild auf eine bestimmte Breite oder Höhe festzulegen und die
	 * andere Größe passend zu skalieren, kann einer der Parameter auf 0 gesetzt
	 * werden.
	 * <p>
	 * Soll die Bildgröße unter Beachtung der Abmessungen um einen Faktor
	 * verändert werden, sollte
	 * {@link #imageScale(Image, double, double, double, Options.Direction)}
	 * verwendet werden.
	 *
	 * @param image Ein Bild-Objekt.
	 * @param x x-Koordinate des Ankerpunktes.
	 * @param y y-Koordinate des Ankerpunktes.
	 * @param width Breite des Bildes auf der Zeichenebene oder 0.
	 * @param height Höhe des Bildes auf der Zeichenebene oder 0.
	 * @param anchor Der Ankerpunkt.
	 */
	public void imageScale( Image image, double x, double y, double width, double height, Options.Direction anchor ) {
		imageRotateAndScale(image, x, y, 0, width, height, anchor);
	}

	/**
	 * Zeichnet das Bild von der angegebenen Bildquelle an den angegebenen
	 * Koordinaten mit der angegebenen Drehung auf die Zeichenebene.
	 * <p>
	 * Das Bild wird um seinen Mittelpunkt als Rotationszentrum gedreht.
	 *
	 * @param imageSource Die Bildquelle.
	 * @param x x-Koordinate des Ankerpunktes.
	 * @param y y-Koordinate des Ankerpunktes.
	 * @param angle Winkel in Grad.
	 */
	public void imageRotate( String imageSource, double x, double y, double angle ) {
		imageRotate(ImageLoader.loadImage(imageSource), x, y, angle, shapeDelegate.getAnchor());
	}

	/**
	 * Zeichnet das Bild von der angegebenen Bildquelle an den angegebenen
	 * Koordinaten mit der angegebenen Drehung auf die Zeichenebene. Der
	 * angegebene Ankerpunkt wird verwendet.
	 * <p>
	 * Das Bild wird um seinen Mittelpunkt als Rotationszentrum gedreht.
	 *
	 * @param imageSource Die Bildquelle.
	 * @param x x-Koordinate des Ankerpunktes.
	 * @param y y-Koordinate des Ankerpunktes.
	 * @param angle Winkel in Grad.
	 * @param anchor Der Ankerpunkt.
	 */
	public void imageRotate( String imageSource, double x, double y, double angle, Options.Direction anchor ) {
		imageRotate(ImageLoader.loadImage(imageSource), x, y, angle, anchor);
	}

	/**
	 * Zeichnet das angegebene Bild an den angegebenen Koordinaten mit der
	 * angegebenen Drehung auf die Zeichenebene.
	 * <p>
	 * Das Bild wird um seinen Mittelpunkt als Rotationszentrum gedreht.
	 *
	 * @param image Ein Bild-Objekt.
	 * @param x x-Koordinate des Ankerpunktes.
	 * @param y y-Koordinate des Ankerpunktes.
	 * @param angle Winkel in Grad.
	 */
	public void imageRotate( Image image, double x, double y, double angle ) {
		imageRotateAndScale(image, x, y, angle, image.getWidth(null), image.getHeight(null), shapeDelegate.getAnchor());
	}

	/**
	 * Zeichnet das angegebene Bild an den angegebenen Koordinaten mit der
	 * angegebenen Drehung auf die Zeichenebene. Der angegebene Ankerpunkt wird
	 * verwendet.
	 * <p>
	 * Das Bild wird um seinen Mittelpunkt als Rotationszentrum gedreht.
	 *
	 * @param image Ein Bild-Objekt.
	 * @param x x-Koordinate des Ankerpunktes.
	 * @param y y-Koordinate des Ankerpunktes.
	 * @param angle Winkel in Grad.
	 * @param anchor Der Ankerpunkt.
	 */
	public void imageRotate( Image image, double x, double y, double angle, Options.Direction anchor ) {
		imageRotateAndScale(image, x, y, angle, image.getWidth(null), image.getHeight(null), anchor);
	}

	/**
	 * Zeichnet das Bild von der angegebenen Bildquelle an den angegebenen
	 * Koordinaten mit der angegebenen Drehung in der angegebenen Größe auf die
	 * Zeichenebene.
	 *
	 * @param imageSource Die Bildquelle.
	 * @param x x-Koordinate des Ankerpunktes.
	 * @param y y-Koordinate des Ankerpunktes.
	 * @param angle Winkel in Grad.
	 * @param width Breite des Bildes auf der Zeichenebene oder 0.
	 * @param height Höhe des Bildes auf der Zeichenebene oder 0.
	 * @see #imageRotate(String, double, double, double)
	 * @see #imageScale(Image, double, double, double)
	 */
	public void imageRotateAndScale( String imageSource, double x, double y, double angle, double width, double height ) {
		imageRotateAndScale(ImageLoader.loadImage(imageSource), x, y, angle, width, height, shapeDelegate.getAnchor());
	}

	/**
	 * Zeichnet das Bild von der angegebenen Bildquelle an den angegebenen
	 * Koordinaten mit der angegebenen Drehung in der angegebenen Größe auf die
	 * Zeichenebene. Der angegebene Ankerpunkt wird verwendet.
	 *
	 * @param imageSource Die Bildquelle.
	 * @param x x-Koordinate des Ankerpunktes.
	 * @param y y-Koordinate des Ankerpunktes.
	 * @param angle Winkel in Grad.
	 * @param width Breite des Bildes auf der Zeichenebene oder 0.
	 * @param height Höhe des Bildes auf der Zeichenebene oder 0.
	 * @param anchor Der Ankerpunkt.
	 * @see #imageRotate(String, double, double, double)
	 * @see #imageScale(Image, double, double, double)
	 */
	public void imageRotateAndScale( String imageSource, double x, double y, double angle, double width, double height, Options.Direction anchor ) {
		imageRotateAndScale(ImageLoader.loadImage(imageSource), x, y, angle, width, height, anchor);
	}

	/**
	 * Zeichnet das angegebene Bild an den angegebenen Koordinaten mit der
	 * angegebenen Drehung in der angegebenen Größe auf die Zeichenebene. Der
	 * angegebene Ankerpunkt wird verwendet.
	 *
	 * @param image Ein Bild-Objekt.
	 * @param x x-Koordinate des Ankerpunktes.
	 * @param y y-Koordinate des Ankerpunktes.
	 * @param angle Winkel in Grad.
	 * @param width Breite des Bildes auf der Zeichenebene oder 0.
	 * @param height Höhe des Bildes auf der Zeichenebene oder 0.
	 * @see #imageRotate(String, double, double, double)
	 * @see #imageScale(Image, double, double, double)
	 */
	public void imageRotateAndScale( Image image, double x, double y, double angle, double width, double height ) {
		imageRotateAndScale(image, x, y, angle, width, height, shapeDelegate.getAnchor());
	}

	/**
	 * Zeichnet das angegebene Bild an den angegebenen Koordinaten mit der
	 * angegebenen Drehung in der angegebenen Größe auf die Zeichenebene. Der
	 * angegebene Ankerpunkt wird verwendet.
	 *
	 * @param image Ein Bild-Objekt.
	 * @param x x-Koordinate des Ankerpunktes.
	 * @param y y-Koordinate des Ankerpunktes.
	 * @param angle Winkel in Grad.
	 * @param width Breite des Bildes auf der Zeichenebene oder 0.
	 * @param height Höhe des Bildes auf der Zeichenebene oder 0.
	 * @param anchor Der Ankerpunkt.
	 * @see #imageRotate(String, double, double, double)
	 * @see #imageScale(Image, double, double, double)
	 */
	public void imageRotateAndScale( Image image, double x, double y, double angle, double width, double height, Options.Direction anchor ) {
		// TODO: Use Validator or at least LOG a message if image == null?
		if( image != null ) {
			AffineTransform orig = drawing.getTransform();

			int imgWidth = image.getWidth(null);
			int imgHeight = image.getHeight(null);

			if( width == 0 ) {
				width = (height / imgHeight) * imgWidth;
			} else if( height == 0 ) {
				height = (width / imgWidth) * imgHeight;
			}

			Point2D.Double anchorPoint = getOriginPoint(x, y, width, height, anchor);
			drawing.rotate(Math.toRadians(angle), anchorPoint.x + width / 2, anchorPoint.y + height / 2);
			drawing.drawImage(image, (int) anchorPoint.x, (int) anchorPoint.y, (int) width, (int) height, null);

			drawing.setTransform(orig);
		}
	}

	/**
	 * @return Die aktuell verwendete Schriftart.
	 */
	public Font getFont() {
		return shapeDelegate.getFont();
	}

	/**
	 * Setzt die verwendete Schriftart für Texte direkt auf das angegebene
	 * {@code Font}-Objekt.
	 *
	 * @param newFont Ein {@code Font}-Objekt.
	 */
	public void setFont( Font newFont ) {
		shapeDelegate.setFont(newFont);
		// fontMetrics = drawing.getFontMetrics();
	}

	/**
	 * Setzt die verwendete Schriftart für Texte auf eine Schriftart mit dem
	 * angegebenen Namen.
	 * <p>
	 * Kann keine Schriftart mit einem passenden Namen geladen werden, wird die
	 * Schriftart nicht geändert.
	 *
	 * @param fontName Der Name der Schriftart.
	 * @see schule.ngb.zm.util.io.FontLoader#loadFont(String)
	 */
	public void setFont( String fontName ) {
		shapeDelegate.setFont(fontName);
	}

	/**
	 * Setzt die verwendete Schriftart für Texte auf eine Schriftart mit dem
	 * angegebenen Namen und der angegebenen Schriftgröße.
	 * <p>
	 * Kann keine Schriftart mit einem passenden Namen geladen werden, wird die
	 * Schriftart nicht geändert.
	 *
	 * @param fontName Der Name der Schriftart.
	 * @param size Die Schriftgröße.
	 * @see schule.ngb.zm.util.io.FontLoader#loadFont(String)
	 */
	public void setFont( String fontName, int size ) {
		shapeDelegate.setFont(fontName, size);
	}

	/**
	 * Setzt die verwendete Schriftart für Texte auf eine Schriftart mit dem
	 * angegebenen Namen, der angegebenen Schriftgröße und dem angegebenen
	 * Schriftstil.
	 * <p>
	 * Der Schriftstil kann über die Stil-Konstanten
	 * {@link schule.ngb.zm.Constants#PLAIN PLAIN},
	 * {@link schule.ngb.zm.Constants#BOLD BOLD} und
	 * {@link schule.ngb.zm.Constants#ITALIC ITALIC} gesetzt werden.
	 * <p>
	 * Kann keine Schriftart mit einem passenden Namen geladen werden, wird die
	 * Schriftart nicht geändert.
	 *
	 * @param fontName Der Name der Schriftart.
	 * @param size Die Schriftgröße.
	 * @param style Der Schriftstil.
	 * @see schule.ngb.zm.util.io.FontLoader#loadFont(String)
	 */
	public void setFont( String fontName, int size, int style ) {
		shapeDelegate.setFont(fontName, size, style);
	}

	/**
	 * @return Die aktuelle Schriftgröße für Texte.
	 */
	public double getFontSize() {
		return shapeDelegate.getFontSize();
	}

	/**
	 * Setzt die Schriftgörße für Texte auf den angegebenen Wert.
	 *
	 * @param size Die neue Schriftgröße.
	 */
	public void setFontSize( double size ) {
		shapeDelegate.setFontSize(size);
	}

	/**
	 * Schreibt einen Text an der angegebenen Position auf die Zeichenebene.
	 *
	 * @param text Der zu schreibende Text.
	 * @param x Die x-Koordinate des Ankerpunktes.
	 * @param y Die y-Koordinate des Ankerpunktes.
	 */
	public void text( String text, double x, double y ) {
		text(text, x, y, shapeDelegate.getAnchor());
	}

	/**
	 * Schreibt einen Text an der angegebenen Position auf die Zeichenebene und
	 * nutzt den angegebenen Ankerpunkt.
	 *
	 * @param text Der zu schreibende Text.
	 * @param x Die x-Koordinate des Ankerpunktes.
	 * @param y Die y-Koordinate des Ankerpunktes.
	 * @param anchor Position des Ankerpunktes.
	 */
	public void text( String text, double x, double y, Options.Direction anchor ) {
		drawing.setFont(shapeDelegate.getFont());
		FontMetrics fm = drawing.getFontMetrics();
		Point2D.Double anchorPoint = getOriginPoint(x, y + fm.getAscent(), fm.stringWidth(text), fm.getHeight(), anchor);

		drawing.drawString(text, (float) anchorPoint.x, (float) anchorPoint.y);
	}

	/**
	 * Berechnet den Ursprung einer zu zeichnenden Form, wenn der angegebene
	 * Ankerpunkt zugrunde gelegt wird.
	 * <p>
	 * Der Ursprung einer Form liegt immer oben links. Eine Form an den
	 * Koordinaten {@code (300, 300)} und den Abmessungen {@code (100, 50)}
	 * würde für den Anker {@link Options.Direction#NORTHWEST} also den Ursprung
	 * {@code (300, 300)} haben. Für den Anker {@link Options.Direction#CENTER}
	 * wäre der Ursprung zu den Koordinaten um die Hälfte der Abmessungen nach
	 * links und oben verschoben bei {@code (250, 275)}. Beim Zeichnen liegen
	 * die Koordinaten {@code (300, 300)} dann in der Mitte der Form.
	 *
	 * @param x x-Koordinate des umgebenden Rechtecks.
	 * @param y y-Koordinate des umgebenden Rechtecks.
	 * @param w Breite des umgebenden Rechtecks.
	 * @param h Höhe des umgebenden Rechtecks.
	 * @param anchor Zu verendender Anker.
	 * @return Der Ursprung des umgebenden Rechtecks.
	 */
	protected Point2D.Double getOriginPoint( double x, double y, double w, double h, Options.Direction anchor ) {
		Point2D.Double ap = schule.ngb.zm.shapes.Shape.getAnchorPoint(w, h, anchor);
		ap.x = x - ap.x;
		ap.y = y - ap.y;
		return ap;
	}

	/**
	 * Speichert die aktuellen Einstellungen für Farben, Konturlinie und
	 * Schriftarten ab, sodass sie zu einem späteren Zeitpunkt mit
	 * {@link #popStyle()} wiederhergestellt werden können.
	 */
	public void pushStyle() {
		styleStack.push(shapeDelegate);
		shapeDelegate = new Text(shapeDelegate);
	}

	/**
	 * Stellt die zuletzt mit {@link #pushStyle()} gespeicherten Einstellungen
	 * für Farben, Konturlinien und Texte wieder her.
	 */
	public void popStyle() {
		if( styleStack.isEmpty() ) {
			resetStyle();
		} else {
			shapeDelegate = styleStack.pop();
		}
	}

	/**
	 * Setzt alle Einstellungen für Farben, Konturlinien und Texte auf die
	 * Standardwerte.
	 */
	public void resetStyle() {
		Text newDelegate = new Text(0, 0, "");
		newDelegate.setFillColor(DEFAULT_FILLCOLOR);
		newDelegate.setStrokeColor(DEFAULT_STROKECOLOR);
		if( shapeDelegate != null ) {
			newDelegate.setAnchor(shapeDelegate.getAnchor());
		}
		shapeDelegate = newDelegate;
	}

	/**
	 * Verschiebt den Ursprung der Zeichenebene um die angegebenen Werte entlang
	 * der x- und y-Achse.
	 *
	 * @param dx Verschiebung entlang der x-Achse.
	 * @param dy Verschiebung entlang der y-Achse.
	 */
	public void translate( double dx, double dy ) {
		drawing.translate(dx, dy);
	}

	/**
	 * Skaliert die Zeichenebene um den angegebenen Faktor.
	 * <p>
	 * Ein Abstand von zehn Einheiten wird bei einem Faktor von 2.0 zu einem
	 * Abstand von 20 Einheiten. Bei einem Faktor von 0.5 zu fünf Einheiten.
	 * <p>
	 * Der Skalierungsfaktor wird entlang der x- und y-Achse gleich angewandt.
	 *
	 * @param factor Der Skalierungsfaktor.
	 */
	public void scale( double factor ) {
		drawing.scale(factor, factor);
	}

	/**
	 * Rotiert die Zeichenebene um den angegebenen Winkel um den Ursprung.
	 *
	 * @param pAngle Rotationswinkel in Grad.
	 */
	public void rotate( double pAngle ) {
		drawing.rotate(Math.toRadians(pAngle));
	}

	/**
	 * Rotiert die Zeichenebene um den angegebenen Winkel um das angegebene
	 * Drehzentrum.
	 *
	 * @param pAngle Rotationswinkel in Grad.
	 * @param centerX x-Koordinate des Rotationszentrums.
	 * @param centerY y-Koordinate des Rotationszentrums.
	 */
	public void rotate( double pAngle, double centerX, double centerY ) {
		drawing.rotate(Math.toRadians(pAngle), centerX, centerY);
	}

	/**
	 * Wendet eine Scherung auf die Zeichenebene an.
	 * <p>
	 * Bei einer Scherung werden die x-Koordinaten parallel zur x-Achse abhängig
	 * von ihrer y-Koordinate verschoben. Ebenso die y-Koordinaten parallel zur
	 * y-Achse abhängig von ihrer x-Koordinate.
	 *
	 * @param dx Scherungsfaktor entlang der x-Achse.
	 * @param dy Scherungsfaktor entlang der y-Achse.
	 */
	public void shear( double dx, double dy ) {
		drawing.shear(dx, dy);
	}

	/**
	 * @return Die aktuelle <a
	 * 	href="https://de.wikibrief.org/wiki/Transformation_matrix">Transformationsmatrix</a>
	 * 	der Zeichenebene.
	 */
	public AffineTransform getMatrix() {
		return drawing.getTransform();
	}

	/**
	 * Speichert eine Kopie der aktuellen Transformationen der Zeichenebene ab.
	 * <p>
	 * Die zuletzt gespeicherten Transformationen können mit
	 * {@link #popMatrix()} wiederhergestellt werden.
	 */
	public void pushMatrix() {
		transformStack.push(drawing.getTransform());
	}

	/**
	 * Ersetzt die aktuellen Transformationen der Zeichenebene durch die zuletzt
	 * mit {@link #pushMatrix()} gespeicherten.
	 * <p>
	 * Wurden keine Transformationen gespeichert, werden alle wird der
	 * Ursprungszustand wiederhergestellt.
	 */
	public void popMatrix() {
		if( transformStack.isEmpty() ) {
			resetMatrix();
		} else {
			drawing.setTransform(transformStack.pop());
		}
	}

	/**
	 * Setzt alle Transformationen der Zeichenebene zurück.
	 */
	public void resetMatrix() {
		drawing.setTransform(new AffineTransform());
	}

}
