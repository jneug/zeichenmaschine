package schule.ngb.zm.formen;

import schule.ngb.zm.Options;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Text extends Shape {

	protected String text;

	protected Font font;

	protected int width = 0, height = 0, ascent = 0;

	public Text( double x, double y, String text ) {
		super(x, y);
		font = new Font(Font.SANS_SERIF, Font.PLAIN, STD_FONTSIZE);
		setText(text);
	}

	public Text( Text text ) {
		super(text.getX(), text.getY());
		copyFrom(text);
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
		}
	}

	@Override
	public void scale( double factor ) {
		super.scale(factor);
		setFontsize(font.getSize2D() * factor);
	}

	public Font getFont() {
		return font;
	}

	public void setFontsize( double size ) {
		font = font.deriveFont((float) size);
		setText(text);
	}

	public String getText() {
		return text;
	}

	public void setText( String pText ) {
		text = pText;

		Canvas metricsCanvas = new Canvas();
		FontMetrics metrics = metricsCanvas.getFontMetrics(font);
		width = metrics.stringWidth(text);
		height = metrics.getDescent() + metrics.getAscent();
		ascent = metrics.getAscent();

		setAnchor(CENTER);
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public void setAnchor( Options.Direction anchor ) {
		calculateAnchor(width, ascent - height, anchor);
	}

	@Override
	public java.awt.Shape getShape() {
		return new Rectangle2D.Double(0, 0, width, height);
	}

	@Override
	public void draw( Graphics2D graphics, AffineTransform pVerzerrung ) {
		if( !visible ) {
			return;
		}

		// Aktuelle Werte speichern
		Font currentFont = graphics.getFont();
		Color currentColor = graphics.getColor();
		AffineTransform af = graphics.getTransform();

		// Neue Werte setzen
		graphics.setFont(font);
		graphics.setColor(strokeColor.getColor());
		graphics.transform(pVerzerrung);

		// Draw text
		FontMetrics fm = graphics.getFontMetrics();
		//graphics.drawString(text, (float) (x - fm.stringWidth(text)/2.0), (float) (y + fm.getDescent()));
		graphics.drawString(text, 0, 0);

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
