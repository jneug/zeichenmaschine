
import schule.ngb.zm.Layer;

import java.awt.Graphics2D;
import java.awt.Image;

public class Background extends Layer {

	private static final int TILE_SIZE = 64;

	private int tile_width;

	private Image[] floor, plants;

	private Image water;

	public Background() {
		super();
	}

	public Background( int width, int height ) {
		super(width, height);
	}

	@Override
	public void setSize( int width, int height ) {
		super.setSize(width, height);
		generateBackground();
	}

	public void generateBackground() {
		tile_width = (int)(ceil(getWidth()/(double)TILE_SIZE));
		floor = new Image[tile_width];
		plants = new Image[tile_width];

		for ( int i = 0; i < tile_width; i++ ) {
			floor[i] = loadImage("tiles/floor"+random(1,8)+".png");
			if( random(1,10) < 4 ) {
				plants[i] = loadImage("tiles/plant"+random(1,14)+".png");
			}
		}

		water = loadImage("tiles/water.png");

		for ( int i = 0; i < getHeight(); i += TILE_SIZE ) {
			for ( int j = 0; j < getWidth(); j += TILE_SIZE ) {
				drawing.drawImage(water, j, i, null);
			}
		}

		for ( int i = 0; i < tile_width; i++ ) {
			if( plants[i] != null ) {
				drawing.drawImage(plants[i], i*TILE_SIZE, getHeight() - (2*TILE_SIZE) + 10,TILE_SIZE,TILE_SIZE, null);
			}
			drawing.drawImage(floor[i], i*TILE_SIZE, getHeight() - TILE_SIZE,TILE_SIZE,TILE_SIZE, null);
		}
	}

}
