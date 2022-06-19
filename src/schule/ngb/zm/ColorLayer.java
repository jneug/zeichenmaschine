package schule.ngb.zm;

public class ColorLayer extends Layer {

	private Color background;

	public ColorLayer( Color color ) {
		this.background = color;
		clear();
	}

	public ColorLayer( int width, int height, Color color ) {
		super(width, height);
		this.background = color;
		clear();
	}

	@Override
	public void setSize( int width, int height ) {
		super.setSize(width, height);
		clear();
	}

	public Color getColor() {
		return background;
	}

	public void setColor( Color color ) {
		background = color;
		clear();
	}

	public void setColor( int gray ) {
		setColor(gray, gray, gray, 255);
	}

	public void setColor( int gray, int alpha ) {
		setColor(gray, gray, gray, alpha);
	}

	public void setColor( int red, int green, int blue ) {
		setColor(red, green, blue, 255);
	}

	public void setColor( int red, int green, int blue, int alpha ) {
		setColor(new Color(red, green, blue, alpha));
	}

	@Override
	public void clear() {
		drawing.setColor(background.getJavaColor());
		drawing.fillRect(0, 0, getWidth(), getHeight());
	}

}
