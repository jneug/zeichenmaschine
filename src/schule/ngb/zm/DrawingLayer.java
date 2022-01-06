package schule.ngb.zm;

import schule.ngb.zm.util.ImageLoader;

import java.awt.*;
import java.awt.geom.*;
import java.util.Stack;

public class DrawingLayer extends Layer {

	protected Color fillColor = STD_FILLCOLOR;

	protected Color strokeColor = STD_STROKECOLOR;

	protected double strokeWeight = STD_STROKEWEIGHT;

	protected Options.StrokeType strokeType = SOLID;

	private Options.Direction default_anchor = CENTER;

	protected Line2D.Double line = new Line2D.Double();
	protected Ellipse2D.Double ellipse = new Ellipse2D.Double();
	protected Rectangle2D.Double rect = new Rectangle2D.Double();
	protected Arc2D.Double arc = new Arc2D.Double();

	private Stack<AffineTransform> transformStack = new Stack<>();

	private FontMetrics fontMetrics = null;

	public DrawingLayer() {
		super();
		transformStack.push(new AffineTransform());
		fontMetrics = drawing.getFontMetrics();
	}

	public DrawingLayer( int width, int height ) {
		super(width, height);
	}

	public Color getColor() {
		return fillColor;
	}

	public void setFillColor( int gray ) {
		setFillColor(gray, gray, gray, 255);
	}

	public void setFillColor( Color color ) {
		fillColor = color;
		drawing.setColor(color.getColor());
	}

	public void noFill() {
		fillColor = null;
	}

	public void setFillColor( int gray, int alpha ) {
		setFillColor(gray, gray, gray, alpha);
	}

	public void setFillColor( int red, int green, int blue ) {
		setFillColor(red, green, blue, 255);
	}

	public void setFillColor( int red, int green, int blue, int alpha ) {
		setFillColor(new Color(red, green, blue, alpha));
	}

	public Color getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor( int gray ) {
		setStrokeColor(gray, gray, gray, 255);
	}

	public void setStrokeColor( Color color ) {
		strokeColor = color;
		drawing.setColor(color.getColor());
	}

	public void noStroke() {
		strokeColor = null;
	}

	public void setStrokeColor( int gray, int alpha ) {
		setStrokeColor(gray, gray, gray, alpha);
	}

	public void setStrokeColor( int red, int green, int blue ) {
		setStrokeColor(red, green, blue, 255);
	}

	public void setStrokeColor( int red, int green, int blue, int alpha ) {
		setStrokeColor(new Color(red, green, blue, alpha));
	}

	public void setStrokeWeight( double pWeight ) {
		strokeWeight = pWeight;
		drawing.setStroke(createStroke());
	}

	protected Stroke createStroke() {
		switch( strokeType ) {
			case DOTTED:
				return new BasicStroke(
					(float) strokeWeight,
					BasicStroke.CAP_ROUND,
					BasicStroke.JOIN_ROUND,
					10.0f, new float[]{1.0f, 5.0f}, 0.0f);
			case DASHED:
				return new BasicStroke(
					(float) strokeWeight,
					BasicStroke.CAP_ROUND,
					BasicStroke.JOIN_ROUND,
					10.0f, new float[]{5.0f}, 0.0f);
			default:
				return new BasicStroke(
					(float) strokeWeight,
					BasicStroke.CAP_ROUND,
					BasicStroke.JOIN_ROUND);
		}
	}

	public Options.StrokeType getStrokeType() {
		return strokeType;
	}

	public void setStrokeType( Options.StrokeType type ) {
		switch( type ) {
			case DASHED:
				this.strokeType = DASHED;
				break;
			case DOTTED:
				this.strokeType = DOTTED;
				break;
			default:
				this.strokeType = SOLID;
				break;
		}
	}

	public void resetStroke() {
		setStrokeColor(STD_STROKECOLOR);
		setStrokeWeight(STD_STROKEWEIGHT);
		setStrokeType(SOLID);
	}

	public void setAnchor( Options.Direction anchor ) {
		default_anchor = anchor;
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
		clear(new Color(red, green, blue, alpha));
	}

	public void clear( Color pColor ) {
        /*graphics.setBackground(pColor);
        graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());*/
		java.awt.Color currentColor = drawing.getColor();
		pushMatrix();
		resetMatrix();
		drawing.setColor(pColor.getColor());
		drawing.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
		drawing.setColor(currentColor);
		popMatrix();
	}

	public void line( double x1, double y1, double x2, double y2 ) {
		//Shape line = new Line2D.Double(x1, y1, x2, y2);
		line.setLine(x1, y1, x2, y2);
		drawShape(line);
	}

	public void pixel( double x, double y ) {
		square(x, y, 1);
	}

	public void square( double x, double y, double w ) {
		rect(x, y, w, w);
	}

	public void square( double x, double y, double w, Options.Direction anchor ) {
		rect(x, y, w, w, anchor);
	}

	public void rect( double x, double y, double w, double h ) {
		rect(x, y, w, h, default_anchor);
	}

	public void rect( double x, double y, double w, double h, Options.Direction anchor ) {
		Point2D.Double anchorPoint = getAnchorPoint(x, y, w, h, anchor);
		// Shape rect = new Rectangle2D.Double(anchorPoint.getX(), anchorPoint.getY(), w, h);
		rect.setRect(anchorPoint.getX(), anchorPoint.getY(), w, h);
		fillShape(rect);
		drawShape(rect);
	}

	public void point( double x, double y ) {
		circle(x - 1, y - 1, 2);
	}

	public void circle( double x, double y, double r ) {
		ellipse(x, y, r+r, r+r, default_anchor);
	}

	public void circle( double x, double y, double r, Options.Direction anchor ) {
		ellipse(x, y, r+r, r+r, anchor);
	}

	public void ellipse( double x, double y, double w, double h ) {
		ellipse(x, y, w, h, default_anchor);
	}

	public void ellipse( double x, double y, double w, double h, Options.Direction anchor ) {
		Point2D.Double anchorPoint = getAnchorPoint(x, y, w, h, anchor);
		// Shape ellipse = new Ellipse2D.Double(anchorPoint.x, anchorPoint.y, w, h);
		ellipse.setFrame(anchorPoint.x, anchorPoint.y, w, h);
		fillShape(ellipse);
		drawShape(ellipse);
	}

	public void arc( double x, double y, double r, double angle1, double angle2 ) {
		arc(x, y, r+r, r+r, angle1, angle2);
	}

	public void arc( double x, double y, double w, double h, double angle1, double angle2 ) {
		while( angle2 < angle1 ) angle2 += 360.0;

		Point2D.Double anchorPoint = getAnchorPoint(x, y, w, h, CENTER);
		/*Shape arc = new Arc2D.Double(
			anchorPoint.x,
			anchorPoint.y,
			d, d,
			angle1, angle2 - angle1,
			Arc2D.OPEN
		);*/
		arc.setArc(
			anchorPoint.x, anchorPoint.y,
			w, h,
			//Math.toRadians(angle1), Math.toRadians(angle2 - angle1),
			angle1, angle2-angle1,
			Arc2D.OPEN
		);

		drawShape(arc);
	}

	public void pie( double x, double y, double r, double angle1, double angle2 ) {
		while( angle2 < angle1 ) angle2 += 360.0;

		double d = r+r;

		Point2D.Double anchorPoint = getAnchorPoint(x, y, d, d, CENTER);
		/*Shape arc = new Arc2D.Double(
			anchorPoint.x,
			anchorPoint.y,
			d, d,
			angle1, angle2 - angle1,
			Arc2D.PIE
		);*/
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
		rhombus(x, y, width, height, default_anchor);
	}

	public void rhombus( double x, double y, double width, double height, Options.Direction anchor ) {
		double whalf = width / 2.0, hhalf = height / 2.0;
		Point2D.Double anchorPoint = getAnchorPoint(x, y, width, height, anchor);
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

	public void image( String imageSource, double x, double y ) {
		image(ImageLoader.loadImage(imageSource), x, y, 1.0, default_anchor);
	}

	public void image( String imageSource, double x, double y, Options.Direction anchor ) {
		image(ImageLoader.loadImage(imageSource), x, y, 1.0, anchor);
	}

	public void image( String imageSource, double x, double y, double scale ) {
		image(ImageLoader.loadImage(imageSource), x, y, scale, default_anchor);
	}

	public void image( String imageSource, double x, double y, double scale, Options.Direction anchor ) {
		image(ImageLoader.loadImage(imageSource), x, y, scale, anchor);
	}

	public void image( Image image, double x, double y ) {
		image(image, x, y, 1.0, default_anchor);
	}

	public void image( Image image, double x, double y, double scale ) {
		image(image, x, y, scale, default_anchor);
	}

	public void image( Image image, double x, double y, double scale, Options.Direction anchor ) {
		if( image != null ) {
			double neww = image.getWidth(null) * scale;
			double newh = image.getHeight(null) * scale;
			Point2D.Double anchorPoint = getAnchorPoint(x, y, neww, newh, anchor);
			drawing.drawImage(image, (int) anchorPoint.x, (int) anchorPoint.y, (int) neww, (int) newh, null);
		}
	}

	public void text( String text, double x, double y ) {
		text(text, x, y, default_anchor);
	}

	public void text( String text, double x, double y, Options.Direction anchor ) {
		FontMetrics fm = drawing.getFontMetrics();
		Point2D.Double anchorPoint = getAnchorPoint(x, y + fm.getAscent(), fm.stringWidth(text), fm.getHeight(), anchor);

		drawing.drawString(text, (float) anchorPoint.x, (float) anchorPoint.y);
	}

	public void setFontSize( int size ) {
		setFont(drawing.getFont().deriveFont((float)size));
	}

	public void setFont( String fontName ) {
		Font font = new Font(fontName, drawing.getFont().getStyle(), drawing.getFont().getSize());
		setFont(font);
	}

	public void setFont( String fontName, int size ) {
		Font font = new Font(fontName, drawing.getFont().getStyle(), size);
		setFont(font);
	}

	public void setFont( String fontName, int size, int style ) {
		Font font = new Font(fontName, style, size);
		setFont(font);
	}

	public void setFont( Font font ) {
		drawing.setFont(font);
		fontMetrics = drawing.getFontMetrics();
	}


	private Point2D.Double transformToCanvas( double x, double y ) {
		return transformToCanvas(new Point2D.Double(x,y));
	}

	private Point2D.Double transformToCanvas( Point2D.Double pPoint ) {
		AffineTransform matrix = getMatrix();
		matrix.transform(pPoint, pPoint);
		return pPoint;
	}

	private Point2D.Double transformToUser( double x, double y ) {
		return transformToUser(new Point2D.Double(x,y));
	}

	private Point2D.Double transformToUser( Point2D.Double pPoint ) {
		AffineTransform matrix = getMatrix();

		try {
			matrix.inverseTransform(pPoint, pPoint);
		} catch( NoninvertibleTransformException e ) {
			e.printStackTrace();
		}

		return pPoint;
	}

	private Point2D.Double getAnchorPoint( double x, double y, double w, double h, Options.Direction anchor ) {
		double whalf = w * .5, hhalf = h * .5;

		// anchor == CENTER
		Point2D.Double anchorPoint = new Point2D.Double(x - whalf, y - hhalf);

		if( NORTH.is(anchor) ) {
			anchorPoint.y += hhalf;
		}
		if( SOUTH.is(anchor) ) {
			anchorPoint.y -= hhalf;
		}
		if( WEST.is(anchor) ) {
			anchorPoint.x += whalf;
		}
		if( EAST.is(anchor) ) {
			anchorPoint.x -= whalf;
		}

		return anchorPoint;
	}

	private void fillShape( Shape pShape ) {
		if( fillColor != null && fillColor.getAlpha() > 0.0 ) {
			drawing.setColor(fillColor.getColor());
			drawing.fill(pShape);
		}
	}

	private void drawShape( Shape pShape ) {
		if( strokeColor != null && strokeColor.getAlpha() > 0.0
			&& strokeWeight > 0.0 ) {
			drawing.setColor(strokeColor.getColor());
			drawing.setStroke(createStroke());
			drawing.draw(pShape);
		}
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
