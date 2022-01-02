package schule.ngb.zm.shapes;

import schule.ngb.zm.Color;

public abstract class FilledShape extends StrokedShape {

	protected Color fillColor = STD_FILLCOLOR;

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor( Color color ) {
		fillColor = color;
	}

	public void setFillColor( int gray ) {
		setFillColor(gray, gray, gray, 255);
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

	public void resetFill() {
		setFillColor(STD_FILLCOLOR);
	}

}
