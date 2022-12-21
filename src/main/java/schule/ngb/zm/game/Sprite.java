package schule.ngb.zm.game;

import schule.ngb.zm.util.Cache;
import schule.ngb.zm.util.io.ImageLoader;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class Sprite {

	public Image[] sprites;

	public Sprite( String spriteFile, int cols, int rows ) {
		BufferedImage sprite = ImageLoader.loadImage(spriteFile, false);

		int w = sprite.getWidth() / cols;
		int h = sprite.getHeight() / rows;

		sprites = new Image[cols*rows];
		for( int i = 0; i < cols; i++ ) {
			for( int j = 0; j < rows; j++ ) {
				sprites[j*cols + i] = ImageLoader.copyImage(
					sprite.getSubimage(i*w, j*h, w, h)
				);
			}
		}
	}


}
