package schule.ngb.zm.layers;

import schule.ngb.zm.Color;
import schule.ngb.zm.Layer;
import schule.ngb.zm.Options;
import schule.ngb.zm.shapes.Text;
import schule.ngb.zm.util.io.ImageLoader;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.*;
import java.util.Stack;

/**
 * Eine Ebene auf der direkt gezeichnet werden kann.
 * <p>
 * Ein {@code DrawingLayer} ist eine der drei Standardebenen der
 * {@link schule.ngb.zm.Zeichenmaschine}.
 */
@SuppressWarnings( "unused" )
public class DrawingLayer extends Layer {

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
	 * Erstellt eine Ebene in der Standardgröße.
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
	 * Erstellt eine Ebene mit der angegebenen Größe.
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

	/**
	 * Entfernt den Farbverlauf von der Form.
	 */
	public void noGradient() {
		shapeDelegate.noGradient();
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

	public void setAnchor( Options.Direction anchor ) {
		shapeDelegate.setAnchor(anchor);
	}

	public void clear( int gray ) {
		clear(gray, gray, gray, 255);
	}

	public void clear( int gray, int alpha ) {
		clear(gray, gray, gray, alpha);
	}

	public void clear( int red, int green, int blue ) {
		clear(red, green, blue, 255);
	}

	public void clear( int red, int green, int blue, int alpha ) {
		clear(new schule.ngb.zm.Color(red, green, blue, alpha));
	}

	public void clear( schule.ngb.zm.Color pColor ) {
        /*graphics.setBackground(pColor);
        graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());*/
		java.awt.Color currentColor = drawing.getColor();
		pushMatrix();
		resetMatrix();
		drawing.setColor(pColor.getJavaColor());
		drawing.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
		drawing.setColor(currentColor);
		popMatrix();
	}

	public void line( double x1, double y1, double x2, double y2 ) {
		line.setLine(x1, y1, x2, y2);
		drawShape(line);
	}

	public void pixel( double x, double y ) {
		buffer.setRGB((int) x, (int) y, shapeDelegate.getFontColor().getRGBA());
	}

	public void square( double x, double y, double w ) {
		rect(x, y, w, w, shapeDelegate.getAnchor());
	}

	public void square( double x, double y, double w, Options.Direction anchor ) {
		rect(x, y, w, w, anchor);
	}

	public void rect( double x, double y, double w, double h ) {
		rect(x, y, w, h, shapeDelegate.getAnchor());
	}

	public void rect( double x, double y, double w, double h, Options.Direction anchor ) {
		Point2D.Double anchorPoint = this.getOriginPoint(x, y, w, h, anchor);
		rect.setRect(anchorPoint.getX(), anchorPoint.getY(), w, h);
		fillShape(rect);
		drawShape(rect);
	}

	public void point( double x, double y ) {
		ellipse(x - 1, y - 1, 2, 2, shapeDelegate.getAnchor());
	}

	public void circle( double x, double y, double r ) {
		ellipse(x, y, r + r, r + r, shapeDelegate.getAnchor());
	}

	public void circle( double x, double y, double r, Options.Direction anchor ) {
		ellipse(x, y, r + r, r + r, anchor);
	}

	public void ellipse( double x, double y, double w, double h ) {
		ellipse(x, y, w, h, shapeDelegate.getAnchor());
	}

	public void ellipse( double x, double y, double w, double h, Options.Direction anchor ) {
		Point2D.Double anchorPoint = getOriginPoint(x, y, w, h, anchor);
		ellipse.setFrame(anchorPoint.x, anchorPoint.y, w, h);
		fillShape(ellipse);
		drawShape(ellipse);
	}

	public void arc( double x, double y, double r, double angle1, double angle2 ) {
		arc(x, y, r + r, r + r, angle1, angle2);
	}

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

	public void curve( double x1, double y1, double x2, double y2, double x3, double y3 ) {
		QuadCurve2D curve = new QuadCurve2D.Double(x1, y1, x2, y2, x3, y3);
		drawShape(curve);
	}

	public void curve( double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4 ) {
		CubicCurve2D curve = new CubicCurve2D.Double(x1, y1, x2, y2, x3, y3, x4, y4);
		drawShape(curve);
	}

	public void triangle( double x1, double y1, double x2, double y2, double x3, double y3 ) {
		Path2D path = new Path2D.Double();
		path.moveTo(x1, y1);
		path.lineTo(x2, y2);
		path.lineTo(x3, y3);
		path.lineTo(x1, y1);

		fillShape(path);
		drawShape(path);
	}

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
		if( shapeDelegate.getPaint() != null ) {
			drawing.setPaint(shapeDelegate.getPaint());
			drawing.fill(shape);
		}
	}

	protected void drawShape( Shape shape ) {
		if( shapeDelegate.getStrokeColor() != null && shapeDelegate.getStrokeColor().getAlpha() > 0
			&& shapeDelegate.getStrokeWeight() > 0.0 ) {
			drawing.setColor(shapeDelegate.getStrokeColor().getJavaColor());
			drawing.setStroke(shapeDelegate.getStroke());
			drawing.draw(shape);
		}
	}

	public void beginShape() {
		path.reset();
		pathStarted = false;
	}

	public void lineTo( double x, double y ) {
		if( !pathStarted ) {
			path.moveTo(x, y);
			pathStarted = true;
		} else {
			path.lineTo(x, y);
		}
	}

	public void endShape() {
		path.closePath();
		path.trimToSize();
		pathStarted = false;

		fillShape(path);
		drawShape(path);
	}

	public void image( String imageSource, double x, double y ) {
		image(ImageLoader.loadImage(imageSource), x, y, 1.0, shapeDelegate.getAnchor());
	}

	public void image( String imageSource, double x, double y, Options.Direction anchor ) {
		image(ImageLoader.loadImage(imageSource), x, y, 1.0, anchor);
	}

	public void image( String imageSource, double x, double y, double scale ) {
		image(ImageLoader.loadImage(imageSource), x, y, scale, shapeDelegate.getAnchor());
	}

	public void image( String imageSource, double x, double y, double scale, Options.Direction anchor ) {
		image(ImageLoader.loadImage(imageSource), x, y, scale, anchor);
	}

	public void image( Image image, double x, double y ) {
		image(image, x, y, 1.0, shapeDelegate.getAnchor());
	}

	public void image( Image image, double x, double y, double scale ) {
		image(image, x, y, scale, shapeDelegate.getAnchor());
	}

	public void image( Image image, double x, double y, double scale, Options.Direction anchor ) {
		if( image != null ) {
			double neww = image.getWidth(null) * scale;
			double newh = image.getHeight(null) * scale;
			Point2D.Double anchorPoint = getOriginPoint(x, y, neww, newh, anchor);
			drawing.drawImage(image, (int) anchorPoint.x, (int) anchorPoint.y, (int) neww, (int) newh, null);
		}
	}

	public Font getFont() {
		return shapeDelegate.getFont();
	}

	public void setFont( Font newFont ) {
		shapeDelegate.setFont(newFont);
		// fontMetrics = drawing.getFontMetrics();
	}

	public void setFont( String fontName ) {
		shapeDelegate.setFont(fontName);
	}

	public void setFont( String fontName, int size ) {
		shapeDelegate.setFont(fontName, size);
	}

	public void setFont( String fontName, int size, int style ) {
		shapeDelegate.setFont(fontName, size, style);
	}

	public double getFontSize() {
		return shapeDelegate.getFontSize();
	}

	public void setFontSize( double size ) {
		shapeDelegate.setFontSize(size);
	}

	public void text( String text, double x, double y ) {
		text(text, x, y, shapeDelegate.getAnchor());
	}

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
	 * wäre der Ursprung zu den Koordinaten um die hälfte der Abmessungen nach
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

	public void pushStyle() {
		styleStack.push(shapeDelegate);
		shapeDelegate = new Text(shapeDelegate);
	}

	public void popStyle() {
		if( styleStack.isEmpty() ) {
			resetStyle();
		} else {
			shapeDelegate = styleStack.pop();
		}
	}

	public void resetStyle() {
		Text newDelegate = new Text(0, 0, "");
		if( shapeDelegate != null ) {
			newDelegate.setAnchor(shapeDelegate.getAnchor());
		}
		shapeDelegate = newDelegate;
	}

	public void translate( double dx, double dy ) {
		drawing.translate(dx, dy);
	}

	public void scale( double factor ) {
		drawing.scale(factor, factor);
	}

	public void rotate( double pAngle ) {
		drawing.rotate(Math.toRadians(pAngle));
	}

	public void shear( double dx, double dy ) {
		drawing.shear(dx, dy);
	}

	public AffineTransform getMatrix() {
		return drawing.getTransform();
	}

	public void pushMatrix() {
		transformStack.push(drawing.getTransform());
	}

	public void popMatrix() {
		if( transformStack.isEmpty() ) {
			resetMatrix();
		} else {
			drawing.setTransform(transformStack.pop());
		}
	}

	public void resetMatrix() {
		drawing.setTransform(new AffineTransform());
	}

}
