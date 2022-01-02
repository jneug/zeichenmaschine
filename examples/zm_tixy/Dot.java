import schule.ngb.zm.Color;
import schule.ngb.zm.Constants;
import schule.ngb.zm.Drawable;

import java.awt.Graphics2D;


public class Dot implements Drawable {

	public static final Color DOT_RED = new Color(244, 32, 65);

	public static final Color DOT_WHITE = new Color(255);

	public static final int SIZE = TIXY.DOT_SIZE;

	public static final int RADIUS = TIXY.DOT_SIZE / 2;

	public static final int GAP = TIXY.DOT_GAP;

	public final int x, y, i;

	public int size = SIZE;

	public Color color;


	public Dot( int x, int y, int i ) {
		this.x = x;
		this.y = y;
		this.i = i;

		color = DOT_WHITE;
	}

	public void setValue( double value ) {
		if( value < 0 ) {
			color = DOT_RED;
			value = value * -1;
		} else {
			color = DOT_WHITE;
		}

		value = Constants.limit(value, 1.0);
		size = (int) (SIZE * value);
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void draw( Graphics2D graphics ) {
		graphics.setColor(color.getColor());
		graphics.fillOval(GAP + x * (SIZE + GAP) + RADIUS - (size / 2), GAP + y * (SIZE + GAP) + RADIUS - (size / 2), size, size);
	}

}
