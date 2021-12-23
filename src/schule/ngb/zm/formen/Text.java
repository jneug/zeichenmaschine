package schule.ngb.zm.formen;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Text extends Form {

	private static final int DFT_FONT_SIZE = 14;

	protected String text;

	protected Font schriftart;

	protected int breite = 0, hoehe = 0, ascent = 0;

	public Text( double pX, double pY, String pText ) {
		super(pX, pY);
		schriftart = new Font(Font.SANS_SERIF, Font.PLAIN, DFT_FONT_SIZE);
		setText(pText);
	}

	public Text( Text pText ) {
		super(pText.getX(), pText.getY());
		kopiere(pText);
	}

	public Form kopie() {
		return new Text(this);
	}

	@Override
	public void kopiere( Form pForm ) {
		super.kopiere(pForm);
		if( pForm instanceof Text ) {
			Text pText = (Text)pForm;
			this.text = pText.getText();
			this.schriftart = pText.getSchriftart();
		}
	}

	@Override
	public void skalieren( double pFaktor ) {
		super.skalieren(pFaktor);
		setSchriftgroesse(schriftart.getSize2D()*pFaktor);
	}

	public Font getSchriftart() {
		return schriftart;
	}

	public void setSchriftgroesse( double pGroesse ) {
		schriftart = schriftart.deriveFont((float)pGroesse);
		setText(text);
	}

	public String getText() {
		return text;
	}

	public void setText( String pText ) {
		text = pText;

		Canvas metricsCanvas = new Canvas();
		FontMetrics metrics = metricsCanvas.getFontMetrics(schriftart);
		breite = metrics.stringWidth(text);
		hoehe = metrics.getDescent() + metrics.getAscent();
		ascent = metrics.getAscent();

		setAnkerpunkt(ZENTRUM);
	}

	public double getBreite() {
		return breite;
	}

	public double getHoehe() {
		return hoehe;
	}

	public void setAnkerpunkt( byte pAnker ) {
		ankerBerechnen(breite, ascent - hoehe, pAnker);
	}

	@Override
	public Shape getShape() {
		return new Rectangle2D.Double(0, 0, breite, hoehe);
	}

	@Override
	public void zeichnen( Graphics2D graphics, AffineTransform pVerzerrung ) {
		if( !sichtbar ) {
			return;
		}

		// Aktuelle Werte speichern
		Font currentFont = graphics.getFont();
		Color currentColor = graphics.getColor();
		AffineTransform af = graphics.getTransform();

		// Neue Werte setzen
		graphics.setFont(schriftart);
		graphics.setColor(konturFarbe);
		graphics.transform(getVerzerrung());

		// Text zeichnen
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
			schriftart.equals(text.schriftart);
	}

	@Override
	public String toString() {
		return getClass().getCanonicalName() + "[" +
			"text=" + text +
			']';
	}

}
