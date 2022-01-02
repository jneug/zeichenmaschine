package schule.ngb.zm;

public class ColorLayer extends Layer {

	private Color background;

	public ColorLayer( Color color ) {
		this.background = color;
		clear();
	}

	public void setColor( Color color ) {
		background = color;
		clear();
	}

	@Override
	public void clear() {
		drawing.setColor(background.getColor());
		drawing.fillRect(0, 0, getWidth(), getHeight());
	}

}
