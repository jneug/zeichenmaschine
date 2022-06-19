import schule.ngb.zm.*;

// TODO: Build Interface (like Drawable) that uses DrawingLayer instead of Graphics2D ?

public class Walker extends Constants {

	public static final int SIZE = 8;

	public static final int STEP = 6;

	private static final Color[] colors = new Color[]{
		Color.parseHexcode("#b13254"),
		Color.parseHexcode("#ff5349"),
		Color.parseHexcode("#ff7249"),
		Color.parseHexcode("#ff9248")
	};

	private int x;

	private int y;

	private Color color;

	private DrawingLayer drawing;

	public Walker(DrawingLayer drawing) {
		this(0, 0, drawing);
	}

	public Walker( int x, int y, DrawingLayer drawing) {
		this.x = x;
		this.y = y;
		this.color = choice(colors);
		this.drawing = drawing;
	}

	public void update() {
		x += random(-STEP, STEP);
		y += random(-STEP, STEP);
		x = limit(x, 10, drawing.getWidth() - 10);
		y = limit(y, 10, drawing.getHeight() - 10);
	}

	public void draw() {
		drawing.setFillColor(color);
		drawing.noStroke();
		drawing.square(x, y, SIZE, MIDDLE);
	}

}
