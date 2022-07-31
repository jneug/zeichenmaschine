package schule.ngb.zm.layers;

import schule.ngb.zm.*;
import schule.ngb.zm.shapes.Fillable;
import schule.ngb.zm.shapes.Rectangle;
import schule.ngb.zm.shapes.Shape;
import schule.ngb.zm.shapes.Strokeable;

import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@SuppressWarnings( "unused" )
public class TurtleLayer extends Layer implements Strokeable, Fillable {

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

	private final static Stack<Color> turtleColors;

	static {
		turtleColors = new Stack<>();
		turtleColors.add(Color.ORANGE);
		turtleColors.add(Color.YELLOW);
		turtleColors.add(Color.MAGENTA);
		turtleColors.add(Color.GREEN);
		turtleColors.add(Color.RED);
		turtleColors.add(Color.BLUE);
	}

	private final Turtle mainTurtle;

	private final List<Turtle> turtles = new ArrayList<>(6);

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

	// begin of delegate methods (auto-generated)

	@Override
	public boolean isVisible() {
		return mainTurtle.isVisible();
	}

	public void beginPath() {
		mainTurtle.beginPath();
	}

	public void closePath() {
		mainTurtle.closePath();
	}

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

	@Override
	public void setFill( Paint fill ) {
		mainTurtle.setFill(fill);
	}

	@Override
	public Paint getFill() {
		return mainTurtle.getFill();
	}

	@Override
	public void setFillColor( Color color ) {
		mainTurtle.setFillColor(color);
	}

	@Override
	public Color getFillColor() {
		return mainTurtle.getFillColor();
	}

	@Override
	public void setStrokeColor( Color color ) {
		mainTurtle.setStrokeColor(color);
	}

	@Override
	public void setStrokeWeight( double weight ) {
		mainTurtle.setStrokeWeight(weight);
	}

	@Override
	public Options.StrokeType getStrokeType() {
		return mainTurtle.getStrokeType();
	}

	@Override
	public void setStrokeType( Options.StrokeType type ) {
		mainTurtle.setStrokeType(type);
	}

	@Override
	public void setGradient( Color from, Color to, Options.Direction dir ) {
		mainTurtle.setGradient(from, to, dir);
	}

	@Override
	public void setGradient( Color from, Color to ) {
		mainTurtle.setGradient(from, to);
	}

	@Override
	public boolean hasFill() {
		return mainTurtle.hasFill();
	}

	@Override
	public boolean hasFillColor() {
		return mainTurtle.hasFillColor();
	}

	@Override
	public boolean hasGradient() {
		return mainTurtle.hasGradient();
	}

	@Override
	public void setFillColor( Color color, int alpha ) {
		mainTurtle.setFillColor(color, alpha);
	}

	@Override
	public void setFillColor( int gray ) {
		mainTurtle.setFillColor(gray);
	}

	@Override
	public void setFillColor( int gray, int alpha ) {
		mainTurtle.setFillColor(gray, alpha);
	}

	@Override
	public void setFillColor( int red, int green, int blue ) {
		mainTurtle.setFillColor(red, green, blue);
	}

	@Override
	public void setFillColor( int red, int green, int blue, int alpha ) {
		mainTurtle.setFillColor(red, green, blue, alpha);
	}

	@Override
	public void noFill() {
		mainTurtle.noFill();
	}

	@Override
	public void resetFill() {
		mainTurtle.resetFill();
	}

	@Override
	public GradientPaint getGradient() {
		return mainTurtle.getGradient();
	}

	@Override
	public void setGradient( double fromX, double fromY, Color from, double toX, double toY, Color to ) {
		mainTurtle.setGradient(fromX, fromY, from, toX, toY, to);
	}

	@Override
	public void setGradient( double centerX, double centerY, double radius, Color from, Color to ) {
		mainTurtle.setGradient(centerX, centerY, radius, from, to);
	}

	@Override
	public void noGradient() {
		mainTurtle.noGradient();
	}

	@Override
	public void setStroke( Stroke stroke ) {
		mainTurtle.setStroke(stroke);
	}

	@Override
	public Stroke getStroke() {
		return mainTurtle.getStroke();
	}

	@Override
	public boolean hasStroke() {
		return mainTurtle.hasStroke();
	}

	@Override
	public Color getStrokeColor() {
		return mainTurtle.getStrokeColor();
	}

	@Override
	public void setStrokeColor( Color color, int alpha ) {
		mainTurtle.setStrokeColor(color, alpha);
	}

	@Override
	public void setStrokeColor( int gray ) {
		mainTurtle.setStrokeColor(gray);
	}

	@Override
	public void setStrokeColor( int gray, int alpha ) {
		mainTurtle.setStrokeColor(gray, alpha);
	}

	@Override
	public void setStrokeColor( int red, int green, int blue ) {
		mainTurtle.setStrokeColor(red, green, blue);
	}

	@Override
	public void setStrokeColor( int red, int green, int blue, int alpha ) {
		mainTurtle.setStrokeColor(red, green, blue, alpha);
	}

	@Override
	public void noStroke() {
		mainTurtle.noStroke();
	}

	@Override
	public void resetStroke() {
		mainTurtle.resetStroke();
	}

	@Override
	public double getStrokeWeight() {
		return mainTurtle.getStrokeWeight();
	}


	// end of delegate methods (auto-generated)


	/**
	 * Die Turtle der Zeichenmaschine.
	 */
	public class Turtle extends BasicDrawable {

		private static final int DEFAULT_SIZE = 12;

		boolean penDown = true;

		boolean visible = true;

		Vector position = new Vector(0, 0);

		Vector direction = new Vector(0, -1);

		Path2D.Double path = new Path2D.Double();

		boolean pathOpen = false;

		/**
		 * Path-Objekt für die Darstellung der Turtle.
		 */
		Path2D.Double turtlePath;

		Turtle() {
		}

		public boolean isVisible() {
			return visible;
		}

		public void beginPath() {
			pathOpen = false;
			addPosToPath();
		}

		public void closePath() {
			if( pathOpen ) {
				addPosToPath();
				path.closePath();
				path.trimToSize();
				pathOpen = false;
			}
		}

		private void addPosToPath() {
			if( !pathOpen ) {
				path.reset();
				path.moveTo(position.x, position.y);
				pathOpen = true;
			} else {
				path.lineTo(position.x, position.y);
			}
		}

		public void fill() {
			closePath();

			if( hasFill() ) {
				drawing.setPaint(getFill());
				drawing.fill(path);
			}
		}

		@Override
		public void draw( Graphics2D graphics ) {
			if( turtlePath == null ) {
				turtlePath = new Path2D.Double();
				path.moveTo(DEFAULT_SIZE, 0);
				path.lineTo(-DEFAULT_SIZE, -DEFAULT_SIZE / 2.0);
				path.lineTo(-DEFAULT_SIZE, DEFAULT_SIZE / 2.0);
				path.lineTo(DEFAULT_SIZE, 0);
			}

			AffineTransform verzerrung = new AffineTransform();
			verzerrung.translate(position.x, position.y);
			verzerrung.rotate(Math.toRadians(direction.angle()));

			java.awt.Shape shape = verzerrung.createTransformedShape(turtlePath);

			if( hasStroke() ) {
				graphics.setColor(strokeColor.getJavaColor());
			} else {
				graphics.setColor(DEFAULT_STROKECOLOR.getJavaColor());
			}
			graphics.fill(shape);
			graphics.setColor(Color.BLACK.getJavaColor());
			graphics.setStroke(getStroke());
			graphics.draw(shape);
		}

		public void fd( double dist ) {
			this.forward(dist);
		}

		public void forward( double dist ) {
			addPosToPath();

			Vector positionStart = position.copy();
			position.add(Vector.setLength(direction, dist));

			if( penDown && hasStroke() ) {
				drawing.setColor(strokeColor.getJavaColor());
				drawing.setStroke(getStroke());
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

			if( penDown && hasStroke() ) {
				drawing.setColor(strokeColor.getJavaColor());
				drawing.setStroke(getStroke());
				drawing.drawLine((int) x, (int) y, (int) position.x, (int) position.y);
			}
		}

	}

}
