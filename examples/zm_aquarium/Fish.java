import schule.ngb.zm.*;
import schule.ngb.zm.shapes.Picture;
import schule.ngb.zm.shapes.Shape;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Fish extends Shape implements Updatable {

	private int speed;

	private Picture img;

	public Fish() {
		randomize();
	}

	public void randomize() {
		int i = random(1, 7);
		speed = random(-2, 2);
		img = new Picture("fish" + i);
		img.setAnchor(NORTHWEST);
		img.scale(random(0.8, 1.0));
		img.tint(randomNiceColor());
		//img.scale(0.5);
		if( speed < 0 ) {
			img.flip(LEFT);
		}
		img.moveTo(random(10, width-img.getWidth()), random(30, height-120));
	}

	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public void update( double delta ) {
		img.move(speed, .5 * sin(tick / (speed * 10.0)));

		if( img.getX() <= 0 || img.getX()+img.getWidth() >= 800 ) {
			speed *= -1;
			img.flip(LEFT);
		}
	}

	@Override
	public void draw( Graphics2D graphics, AffineTransform transform ) {
		img.draw(graphics, transform);
	}

	@Override
	public double getWidth() {
		return img.getWidth();
	}

	@Override
	public double getHeight() {
		return img.getHeight();
	}

	@Override
	public Shape copy() {
		return img.copy();
	}

	@Override
	public java.awt.Shape getShape() {
		return img.getShape();
	}

}
