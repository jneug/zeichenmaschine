package schule.ngb.zm.game;

import schule.ngb.zm.Constants;
import schule.ngb.zm.shapes.Bounds;

public class Camera {

	double x, y, z = .0;

	int width, height;

	double zoom = 1.0;

	public Camera() {
		x = Constants.canvasWidth / 2.0;
		y = Constants.canvasHeight / 2.0;
		width = Constants.canvasWidth;
		height = Constants.canvasHeight;
	}

	public Bounds getBounds() {
		return new Bounds(x - width/2.0, y - height/2.0, width, height);
	}

}
