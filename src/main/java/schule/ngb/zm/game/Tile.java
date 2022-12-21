package schule.ngb.zm.game;

import schule.ngb.zm.Color;
import schule.ngb.zm.Constants;

import java.awt.Graphics2D;
public class Tile {

	int c, r;

	Color clr;

	public Tile( int c, int r ) {
		this.c = c;
		this.r = r;

		clr = Color.getHSBColor(((20-c)*(20-c)+(15-r)*(15-r))/625.0, .7, .8);
	}

	public void render( Graphics2D g, int x, int y, int size ) {
		g.setColor(clr.getJavaColor());
		g.fillRect(x, y, size, size);
		g.setColor(Color.DARKGRAY.getJavaColor());
		g.drawRect(x, y, size, size);
		g.drawString("(" + c + "," + r + ")", x+3, y+13);
	}

}
