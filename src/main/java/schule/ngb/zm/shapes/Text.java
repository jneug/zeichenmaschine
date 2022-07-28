package schule.ngb.zm.shapes;

import schule.ngb.zm.Color;
import schule.ngb.zm.Options;
import schule.ngb.zm.util.io.FontLoader;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Text extends Shape {

	protected String text;

	protected Font font;

	protected Color fontColor = BLACK;

	protected int width = 0, height = 0, ascent = 0;

	public Text( double x, double y, String text ) {
		this(x, y, text, new Font(Font.SANS_SERIF, Font.PLAIN, DEFAULT_FONTSIZE));
	}

	public Text( double x, double y, String text, String fontname ) {
		super(x, y);
		Font userfont = FontLoader.loadFont(fontname);
		if( userfont != null ) {
			font = userfont;
		} else {
			font = new Font(Font.SANS_SERIF, Font.PLAIN, DEFAULT_FONTSIZE);
		}
		setText(text);
		fillColor = null;
		strokeColor = null;
		anchor = Options.Direction.CENTER;
	}

	public Text( double x, double y, String text, Font font ) {
		super(x, y);
		this.font = font;
		setText(text);
		fillColor = null;
		strokeColor = null;
		anchor = Options.Direction.CENTER;
	}

	public Text( Text text ) {
		super(text.getX(), text.getY());
		copyFrom(text);
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public Font getFont() {
		return font;
	}

	public void setFont( Font newFont ) {
		//font = newFont.deriveFont(font.getStyle(), font.getSize2D());
		font = new Font(newFont.getFontName(), newFont.getStyle(), newFont.getSize());
		//font = newFont;
		calculateBounds();
	}

	public void setFont( String fontName ) {
		Font newFont = FontLoader.loadFont(fontName);
		if( newFont != null ) {
			setFont(newFont);
		}
	}

	public void setFont( String fontName, double fontSize ) {
		Font newFont = FontLoader.loadFont(fontName);
		if( newFont != null ) {
			setFont(newFont);
		}
		font = newFont.deriveFont((float) fontSize);
		calculateBounds();
	}

	public void setFont( String fontName, double fontSize, int style ) {
		Font newFont = FontLoader.loadFont(fontName);
		if( newFont != null ) {
			setFont(newFont);
		}
		font = newFont.deriveFont(style, (float) fontSize);
		calculateBounds();
	}

	public double getFontSize() {
		return font.getSize2D();
	}

	public void setFontSize( double size ) {
		font = font.deriveFont((float) size);
		calculateBounds();
	}

	public int getFontStyle() {
		return font.getStyle();
	}

	public void setFontStyle( int fontStyle ) {
		font = font.deriveFont(fontStyle);
		calculateBounds();
	}

	public String getText() {
		return text;
	}

	public void setText( String pText ) {
		text = pText;
		calculateBounds();
	}

	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor( Color color ) {
		if( color != null ) {
			fontColor = color;
		} else {
			fontColor = BLACK;
		}
	}

	public void setFontColor( Color color, int alpha ) {
		if( color != null ) {
			fontColor = new Color(color, alpha);
		} else {
			fontColor = BLACK;
		}
	}

	public void setFontColor( int gray ) {
		setFontColor(gray, gray, gray, 255);
	}

	public void setFontColor( int gray, int alpha ) {
		setFontColor(gray, gray, gray, alpha);
	}

	public void setFontColor( int red, int green, int blue ) {
		setFontColor(red, green, blue, 255);
	}

	public void setFontColor( int red, int green, int blue, int alpha ) {
		setFontColor(new Color(red, green, blue, alpha));
	}

	public void resetFontColor() {
		setFontColor(BLACK);
	}

	private void calculateBounds() {
		//GraphicsDevice gd;
		//gd.getDefaultConfiguration().createCompatibleImage(1,1);
		Canvas metricsCanvas = new Canvas();
		FontMetrics metrics = metricsCanvas.getFontMetrics(font);
		width = metrics.stringWidth(text);
		//height = metrics.getHeight();
		height = metrics.getDescent() + metrics.getAscent();
		ascent = metrics.getMaxAscent();
	}

	public Shape copy() {
		return new Text(this);
	}

	@Override
	public void copyFrom( Shape shape ) {
		super.copyFrom(shape);
		if( shape instanceof Text ) {
			Text pText = (Text) shape;
			this.text = pText.getText();
			this.font = pText.getFont();
			calculateBounds();
		}
	}

	@Override
	public void scale( double factor ) {
		super.scale(factor);
		setFontSize(font.getSize2D() * factor);
	}

	@Override
	public java.awt.Shape getShape() {
		return new Rectangle2D.Double(0, 0, width, height);
	}

	@Override
	public void draw( Graphics2D graphics, AffineTransform transform ) {
		if( !visible ) {
			return;
		}

		// Aktuelle Werte speichern
		Font currentFont = graphics.getFont();
		java.awt.Color currentColor = graphics.getColor();
		AffineTransform af = graphics.getTransform();
		graphics.transform(transform);

		// Hintergrund
		if( fillColor != null && fillColor.getAlpha() > 0 ) {
			graphics.setColor(fillColor.getJavaColor());
			graphics.fillRect(0, 0, width, height);
		}
		if( strokeColor != null && strokeColor.getAlpha() > 0
			&& strokeWeight > 0.0 ) {
			graphics.setColor(strokeColor.getJavaColor());
			graphics.setStroke(getStroke());
			graphics.drawRect(0, 0, width, height);
		}

		// Neue Werte setzen
		if( font != null && fontColor != null && fontColor.getAlpha() > 0 ) {
			graphics.setFont(font);
			graphics.setColor(fontColor.getJavaColor());

			// Draw text
			//FontMetrics fm = graphics.getFontMetrics();
			//graphics.drawString(text, (float) (x - fm.stringWidth(text)/2.0), (float) (y + fm.getDescent()));
			graphics.drawString(text, 0, ascent);
		}

		// Alte Werte wiederherstellen
		graphics.setTransform(af);
		graphics.setColor(currentColor);
		graphics.setFont(currentFont);
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( o == null || getClass() != o.getClass() ) return false;
		Text text = (Text) o;
		return super.equals(o) &&
			text.equals(text.text) &&
			font.equals(text.font);
	}

	@Override
	public String toString() {
		return getClass().getCanonicalName() + "[" +
			"text=" + text + ',' +
			"font=" + font.getName() + ',' +
			"size=" + font.getSize() +
			']';
	}

}
