package schule.ngb.zm.layers;

import schule.ngb.zm.Layer;
import schule.ngb.zm.util.io.ImageLoader;

import java.awt.Graphics2D;
import java.awt.Image;

@SuppressWarnings( "unused" )
public class ImageLayer extends Layer {

	protected Image image;

	protected double x = 0;

	protected double y = 0;

	protected boolean redraw = true;

	public ImageLayer( String source ) {
		image = ImageLoader.loadImage(source);
	}

	public ImageLayer( Image image ) {
		this.image = image;
	}

	public ImageLayer( int width, int height, Image image ) {
		super(width, height);
		this.image = image;
	}

	public void setImage( Image image ) {
		this.image = image;
		redraw = true;
	}

	public double getX() {
		return x;
	}

	public void setX( double pX ) {
		this.x = pX;
		redraw = true;
	}

	public double getY() {
		return y;
	}

	public void setY( double pY ) {
		this.y = pY;
		redraw = true;
	}

	@Override
	public void clear() {
		super.clear();
		redraw = true;
	}

	@Override
	public void draw( Graphics2D graphics ) {
		if( redraw && visible ) {
			drawing.drawImage(image, (int) x, (int) y, null);
			redraw = false;
		}
		super.draw(graphics);
	}

}
