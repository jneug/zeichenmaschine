package schule.ngb.zm.formen;


import schule.ngb.zm.Color;
import schule.ngb.zm.Options;
import schule.ngb.zm.util.ImageLoader;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Picture extends Shape {

	// https://stackoverflow.com/questions/14225518/tinting-image-in-java-improvement
	protected Color tint;

	private BufferedImage image;

	private double width;

	private double height;

	public Picture( String source ) {
		this(0, 0, source);
	}

	public Picture( double x, double y, String source ) {
		super(x, y);
		image = ImageLoader.loadImage(source);

		if( image == null ) {
			throw new IllegalArgumentException("Could not initialize image from source " + source);
		}

		width = image.getWidth();
		height = image.getHeight();
		setAnchor(CENTER);
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

			width = image.getWidth();
			height = image.getHeight();
			setAnchor(shape.getAnchor());
		}
	}

	public double getWidth() {
		return width;
	}

	public void setWidth( double width ) {
		scale(width / width);
	}

	public double getHeight() {
		return height;
	}

	public void setHeight( double height ) {
		scale(height / height);
	}

	public BufferedImage getImage() {
		return image;
	}

	@Override
	public void setAnchor( Options.Direction anchor ) {
		calculateAnchor(width, height, anchor);
	}

	@Override
	public void scale( double factor ) {
		super.scale(factor);
		width *= factor;
		height *= factor;
	}

	@Override
	public java.awt.Shape getShape() {
		return new Rectangle2D.Double(0, 0, width, height);
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

	@Override
	public void draw( Graphics2D graphics, AffineTransform pVerzerrung ) {
		if( !visible ) {
			return;
		}

		AffineTransform current = graphics.getTransform();
		graphics.transform(getTransform());
		graphics.drawImage(image, 0, 0, (int) width, (int) height, null);
		graphics.setTransform(current);
	}

}
