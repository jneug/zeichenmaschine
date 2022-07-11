package schule.ngb.zm.shapes;


import schule.ngb.zm.Color;
import schule.ngb.zm.Options;
import schule.ngb.zm.util.ImageLoader;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Picture extends Shape {

	private BufferedImage image;

	private double imgWidth;

	private double imgHeight;

	public Picture( String source ) {
		this(0, 0, source);
	}

	public Picture( double x, double y, String source ) {
		super(x, y);
		image = ImageLoader.loadImage(source);

		if( image == null ) {
			throw new IllegalArgumentException("Could not initialize image from source " + source);
		}

		imgWidth = image.getWidth();
		imgHeight = image.getHeight();
		this.anchor = Options.Direction.CENTER;
	}

	public Picture( Picture picture ) {
		super(picture.getX(), picture.getY());
		copyFrom(picture);
	}

	@Override
	public Shape copy() {
		return new Picture(this);
	}

	@Override
	public void copyFrom( Shape shape ) {
		super.copyFrom(shape);
		if( shape instanceof Picture ) {
			Picture pic = (Picture) shape;
			image = new BufferedImage(pic.image.getWidth(), pic.image.getHeight(), pic.image.getType());
			Graphics2D g = image.createGraphics();
			g.drawImage(pic.image, 0, 0, null);
			g.dispose();

			imgWidth = image.getWidth();
			imgHeight = image.getHeight();
		}
	}

	public double getWidth() {
		return imgWidth;
	}

	public void setWidth( double width ) {
		scale(width / this.imgWidth);
	}

	public double getHeight() {
		return imgHeight;
	}

	public void setHeight( double height ) {
		scale(height / this.imgHeight);
	}

	public BufferedImage getImage() {
		return image;
	}

	@Override
	public void scale( double factor ) {
		super.scale(factor);
		imgWidth *= factor;
		imgHeight *= factor;
	}

	@Override
	public java.awt.Shape getShape() {
		return new Rectangle2D.Double(0, 0, imgWidth, imgHeight);
	}

	/*
	@Override
	public AffineTransform getVerzerrung() {
		AffineTransform verzerrung = new AffineTransform();
		verzerrung.translate(x, y);
		verzerrung.scale(skalierung, skalierung);
		verzerrung.rotate(Math.toRadians(drehwinkel));
		verzerrung.translate(-anker.x, -anker.y);
		return verzerrung;
	}
	*/

	public void flip( Options.Direction dir ) {
		int w = image.getWidth();
		int h = image.getHeight();

		BufferedImage new_image = ImageLoader.createImage(w, h, image.getColorModel().getTransparency());
		Graphics2D g = new_image.createGraphics();
		if( dir.contains(LEFT) || dir.contains(RIGHT) ) {
			g.drawImage(image, 0, 0, w, h, w, 0, 0, h, null);
		} else {
			g.drawImage(image, 0, 0, w, h, 0, h, w, 0, null);
		}
		g.dispose();
		image = new_image;
	}

	public void tint( Color tintColor ) {
		BufferedImage mask = generateMask(tintColor, 0.5);

		int imgWidth = image.getWidth();
		int imgHeight = image.getHeight();

		BufferedImage new_image = ImageLoader.createImage(imgWidth, imgHeight, image.getColorModel().getTransparency());
		Graphics2D g2 = new_image.createGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.drawImage(mask, 0, 0, null);
		g2.dispose();

		image = new_image;
	}

	private BufferedImage generateMask( Color color, double alpha ) {
		int imgWidth = image.getWidth();
		int imgHeight = image.getHeight();

		BufferedImage imgMask = ImageLoader.createImage(imgWidth, imgHeight, Transparency.TRANSLUCENT);
		imgMask.coerceData(true);

		Graphics2D g2 = imgMask.createGraphics();

		g2.drawImage(image, 0, 0, null);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, (float)alpha));
		g2.setColor(color.getJavaColor());

		g2.fillRect(0, 0, imgWidth, imgHeight);
		g2.dispose();

		return imgMask;
	}


	@Override
	public void draw( Graphics2D graphics, AffineTransform transform ) {
		if( !visible ) {
			return;
		}

		AffineTransform current = graphics.getTransform();
		graphics.transform(transform);
		graphics.drawImage(image, 0, 0, (int) imgWidth, (int) imgHeight, null);
		graphics.setTransform(current);
	}

}
