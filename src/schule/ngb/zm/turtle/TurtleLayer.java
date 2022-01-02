package schule.ngb.zm.turtle;

import schule.ngb.zm.Color;
import schule.ngb.zm.Layer;
import schule.ngb.zm.Options;
import schule.ngb.zm.Vector;
import schule.ngb.zm.shapes.FilledShape;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Stack;

public class TurtleLayer extends Layer {

	// Rotating by the clock
	public static final int H1 = 30;

	public static final int H2 = 60;

	public static final int H3 = 90;

	public static final int H4 = 120;

	public static final int H5 = 150;

	public static final int H6 = 180;

	public static final int H7 = 210;

	public static final int H8 = 240;

	public static final int H9 = 270;

	public static final int H10 = 300;

	public static final int H11 = 330;

	public static final int H12 = 360;

	private static Stack<Color> turtleColors;

	static {
		turtleColors = new Stack<>();
		turtleColors.add(Color.ORANGE);
		turtleColors.add(Color.YELLOW);
		turtleColors.add(Color.MAGENTA);
		turtleColors.add(Color.GREEN);
		turtleColors.add(Color.RED);
		turtleColors.add(Color.BLUE);
	}

	private Turtle mainTurtle = null;

	private ArrayList<Turtle> turtles = new ArrayList<Turtle>(6);

	public TurtleLayer() {
		super();
		mainTurtle = newTurtle();
	}

	public TurtleLayer( int width, int height ) {
		super(width, height);
		mainTurtle = newTurtle();
	}

	public Turtle newTurtle() {
		Turtle newTurtle = new Turtle();

		newTurtle.position.x = getWidth() / 2.0;
		newTurtle.position.y = getHeight() / 2.0;
		newTurtle.direction.x = 0.0;
		newTurtle.direction.y = -1.0;
		synchronized( turtles ) {
			if( !turtleColors.isEmpty() ) {
				newTurtle.setStrokeColor(turtleColors.pop());
			}
			turtles.add(newTurtle);
		}
		return newTurtle;
	}

	public Turtle getTurtle() {
		return mainTurtle;
	}

	public Turtle getTurtle( int i ) {
		return turtles.get(i);
	}

	@Override
	public void setSize( int width, int height ) {
		super.setSize(width, height);
		mainTurtle.position.x = getWidth() / 2.0;
		mainTurtle.position.y = getHeight() / 2.0;
	}

	/*
	@Override
	public void clear() {
		drawing.setBackground(STD_BACKGROUND.getColor());
		drawing.clearRect(0, 0, buffer.getWidth(), buffer.getHeight());
	}
	*/

	@Override
	public void draw( Graphics2D graphics ) {
		super.draw(graphics);

		synchronized( turtles ) {
			for( Turtle t : turtles ) {
				if( t.visible ) {
					t.draw(graphics);
				}
			}
		}
	}

	// Begin of delegate methods (auto-generated)

	public void fill() {
		mainTurtle.fill();
	}

	public void fd( double dist ) {
		mainTurtle.fd(dist);
	}

	public void forward( double dist ) {
		mainTurtle.forward(dist);
	}

	public void bk( double dist ) {
		mainTurtle.bk(dist);
	}

	public void back( double dist ) {
		mainTurtle.back(dist);
	}

	public void left() {
		mainTurtle.left();
	}

	public void lt( double angle ) {
		mainTurtle.lt(angle);
	}

	public void left( double angle ) {
		mainTurtle.left(angle);
	}

	public void right() {
		mainTurtle.right();
	}

	public void rt( double angle ) {
		mainTurtle.rt(angle);
	}

	public void right( double angle ) {
		mainTurtle.right(angle);
	}

	public void penUp() {
		mainTurtle.penUp();
	}

	public void penDown() {
		mainTurtle.penDown();
	}

	public void moveTo( double x, double y ) {
		mainTurtle.moveTo(x, y);
	}

	// End of delegate methods (auto-generated)


	public class Turtle extends FilledShape {

		boolean penDown = true;

		boolean visible = true;

		Vector position = new Vector(0, 0);

		Vector direction = new Vector(0, -1);

		Path2D.Double path = new Path2D.Double();

		boolean pathOpen = false;

		Turtle() {}

		private void addPosToPath() {
			if( !pathOpen ) {
				path.reset();
				path.moveTo(position.x, position.y);
				pathOpen = true;
			} else {
				path.lineTo(position.x, position.y);
			}
		}

		private void closePath() {
			if( pathOpen ) {
				addPosToPath();
				path.closePath();
				pathOpen = false;
			}
		}

		public void fill() {
			closePath();

			if( fillColor != null && fillColor.getAlpha() > 0 ) {
				drawing.setColor(fillColor.getColor());
				drawing.fill(path);
			}
		}

		public boolean isVisible() {
			return visible;
		}

		@Override
		public void draw( Graphics2D graphics ) {
			/*Shape shape = new RoundRectangle2D.Double(
				-12, -5, 16, 10, 5, 3
			);*/
			Path2D path = new Path2D.Double();
			path.moveTo(STD_SIZE, 0);
			path.lineTo(-STD_SIZE, -STD_SIZE/2);
			path.lineTo(-STD_SIZE, STD_SIZE/2);
			path.lineTo(STD_SIZE, 0);

			AffineTransform verzerrung = new AffineTransform();
			verzerrung.translate(position.x, position.y);
			verzerrung.rotate(Math.toRadians(direction.angle()));

			Shape shape = verzerrung.createTransformedShape(path);

			if( strokeColor != null ) {
				graphics.setColor(strokeColor.getColor());
			} else {
				graphics.setColor(STD_STROKECOLOR.getColor());
			}
			graphics.fill(shape);
			graphics.setColor(Color.BLACK.getColor());
			graphics.setStroke(createStroke());
			graphics.draw(shape);
		}

		public void fd( double dist ) {
			this.forward(dist);
		}

		public void forward( double dist ) {
			addPosToPath();

			Vector positionStart = position.copy();
			position.add(Vector.setLength(direction, dist));

			if( penDown && strokeColor != null ) {
				drawing.setColor(strokeColor.getColor());
				drawing.setStroke(createStroke());
				drawing.drawLine((int) positionStart.x, (int) positionStart.y, (int) position.x, (int) position.y);
			}
		}

		public void bk( double dist ) {
			this.forward(-1 * dist);
		}

		public void back( double dist ) {
			this.forward(-1 * dist);
		}

		public void left() {
			this.left(90);
		}

		public void lt( double angle ) {
			this.left(angle);
		}

		public void left( double angle ) {
			direction.rotate(-angle);
		}

		public void right() {
			this.right(90);
		}

		public void rt( double angle ) {
			this.right(angle);
		}

		public void right( double angle ) {
			direction.rotate(angle);
		}

		public void penUp() {
			penDown = false;
		}

		public void penDown() {
			penDown = true;
		}

		public void moveTo( double x, double y ) {
			addPosToPath();

			position.x = x;
			position.y = y;

			if( penDown && strokeColor != null ) {
				drawing.setColor(strokeColor.getColor());
				drawing.setStroke(createStroke());
				drawing.drawLine((int) x, (int) y, (int) position.x, (int) position.y);
			}
		}

	}

}
