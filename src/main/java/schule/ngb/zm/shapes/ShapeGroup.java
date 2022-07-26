package schule.ngb.zm.shapes;

import schule.ngb.zm.Options;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Eine {@code ShapwGroup} ist eine Sammlung von {@link Shape}s, die gemeinsam
 * eine Gruppe bilden. Transformationen der Gruppe werden nach den
 * Transformationen der einzelnen Formen auf die gesammte Gruppe angewandt. So
 * kann ein Rechteck in der Gruppe zunächst um 45 Grad gedreht werden und dann
 * die Gruppe um -45 Grad. Das Rechteck wird dann wieder waagerecht dargestellt,
 * während alle anderen Formen der Gruppe nun gedreht erscheinen. Da die Gruppe
 * selbst ein eigenes Drehzentrum hat, können so komplexe Strukturen als eine
 * eigene, zusammenhängende Form verwendet werden. (Im Gegensatz zu einer
 * {@link CustomShape} haben Formgruppen den Vorteil, dass die einzelnen Formen
 * individuelle Farben und Konturen bekommen können.)
 * <p>
 * Da die Größe der Gruppe durch seine zugewiesenen Formen fefstgelegt wird,
 * sollten Modifikation wie {@link #setAnchor(Options.Direction)},
 * {@link #nextTo(Shape, Options.Direction)} oder
 * {@link #alignTo(Shape, Options.Direction, double)}, die die Größe der Gruppe
 * benötigen, erst nach Hinzufügen der Gruppenelemente ausgeführt werden.
 * Nachdem sich die Zusammensetzung der Gruppe geändert hat, muss die Gruppe
 * ggf. neu ausgerichtet werden.
 * <p>
 * Für die Ausrichtung der Elemente in einer Gruppe können
 * {@link #arrange(Options.Direction, double)},
 * {@link #arrangeInGrid(int, Options.Direction, double, int)} und
 * {@link #align(Options.Direction)} verwendet werden, die jeweils die Position
 * der Formen in der Gruppe ändern und nicht die Position der Gruppe selbst (so
 * wie z.B. {@link #alignTo(Shape, Options.Direction)}.
 */
public class ShapeGroup extends Shape {

	public static final int ARRANGE_ROWS = 0;

	public static final int ARRANGE_COLS = 1;

	private List<Shape> shapes;

	private double groupWidth = -1.0;

	private double groupHeight = -1.0;

	public ShapeGroup() {
		super();
		shapes = new ArrayList<>(10);
	}

	public ShapeGroup( double x, double y ) {
		super(x, y);
		shapes = new ArrayList<>(10);
	}

	public ShapeGroup( double x, double y, Shape... shapes ) {
		super(x, y);
		this.shapes = new ArrayList<>(shapes.length);
		for( Shape pShape : shapes ) {
			this.shapes.add(pShape);
		}
	}

	public Shape copy() {
		// TODO: implement?
		return null;
	}

	public void add( Shape... shapes ) {
		for( Shape shape : shapes ) {
			add(shape, false);
		}
		invalidateBounds();
	}

	public void add( Shape pShape, boolean relative ) {
		if( relative ) {
			pShape.x = pShape.x - x;
			pShape.y = pShape.y - y;
		}
		shapes.add(pShape);
		invalidateBounds();
	}

	public void removeAll() {
		shapes.clear();
		invalidateBounds();
	}

	public List<Shape> getShapes() {
		return shapes;
	}

	public <ShapeType extends Shape> List<ShapeType> getShapes( Class<ShapeType> typeClass ) {
		LinkedList<ShapeType> list = new LinkedList<>();
		for( Shape s : shapes ) {
			if( typeClass.isInstance(s) ) {
				list.add((ShapeType) s);
			}
		}
		return list;
	}

	public void remove( Shape shape ) {
		shapes.remove(shape);
		invalidateBounds();
	}

	public Shape get( int index ) {
		if( index < shapes.size() ) {
			return shapes.get(index);
		} else {
			return null;
		}
	}

	public boolean contains( Shape shape ) {
		return shapes.contains(shape);
	}

	public int size() {
		return shapes.size();
	}

	@Override
	public double getWidth() {
		if( groupWidth < 0 ) {
			calculateBounds();
		}
		return groupWidth;
	}

	@Override
	public double getHeight() {
		if( groupHeight < 0 ) {
			calculateBounds();
		}
		return groupHeight;
	}

	public void arrange( Options.Direction dir, double buffer ) {
		Shape last = null;
		for( Shape s : shapes ) {
			if( last != null ) {
				s.nextTo(last, dir, buffer);
			} else {
				s.moveTo(0, 0);
			}
			last = s;
		}
		invalidateBounds();
	}

	public void arrangeInRows( int n, Options.Direction dir, double buffer ) {
		arrangeInGrid(n, dir, buffer, ARRANGE_ROWS);
	}

	public void arrangeInColumns( int n, Options.Direction dir, double buffer ) {
		arrangeInGrid(n, dir, buffer, ARRANGE_COLS);
	}

	public void arrangeInGrid( int n, Options.Direction dir, double buffer, int mode ) {
		// Calculate grid size
		int rows, cols;
		if( mode == ARRANGE_ROWS ) {
			rows = n;
			cols = (int) ceil(shapes.size() / n);
		} else {
			cols = n;
			rows = (int) ceil(shapes.size() / n);
		}

		// Calculate grid cell size
		double maxHeight = shapes.stream().mapToDouble(
			( s ) -> s.getHeight()
		).reduce(0.0, Double::max);
		double maxWidth = shapes.stream().mapToDouble(
			( s ) -> s.getWidth()
		).reduce(0.0, Double::max);
		double halfHeight = maxHeight * .5;
		double halfWidth = maxWidth * .5;

		// Layout shapes
		for( int i = 0; i < shapes.size(); i++ ) {
			// Calculate center of grid cell
			int row, col;
			switch( dir ) {
				case UP:
				case NORTH:
					row = rows - i % rows;
					col = cols - (i / rows);
					break;
				case LEFT:
				case WEST:
					row = rows - (i / cols);
					col = cols - i % cols;
					break;
				case RIGHT:
				case EAST:
					row = i / cols;
					col = i % cols;
					break;
				case DOWN:
				case SOUTH:
				default:
					row = i % rows;
					col = i / rows;
					break;
			}

			double centerX = halfWidth + col * (maxWidth + buffer);
			double centerY = halfHeight + row * (maxHeight + buffer);

			// Move shape to proper anchor location in cell
			Shape s = shapes.get(i);
			Point2D ap = Shape.getAnchorPoint(maxWidth, maxHeight, s.getAnchor());
			s.moveTo(centerX + ap.getX(), centerY + ap.getY());
		}

		invalidateBounds();
	}

	public void align( Options.Direction dir ) {
		Shape target = shapes.stream().reduce(null,
			( t, s ) -> {
				if( t == null ) return s;
				Point2D apt = t.getAbsAnchorPoint(dir);
				Point2D aps = s.getAbsAnchorPoint(dir);
				if( apt.getX() * dir.x >= aps.getX() * dir.x && apt.getY() * dir.y >= aps.getY() * dir.y ) {
					return t;
				} else {
					return s;
				}
			}
		);

		for( Shape s : shapes ) {
			if( s != target ) {
				s.alignTo(target, dir);
			}
		}

		invalidateBounds();
	}

	private void invalidateBounds() {
		groupWidth = -1.0;
		groupHeight = -1.0;
	}

	private void calculateBounds() {
		double minx = Double.MAX_VALUE, miny = Double.MAX_VALUE,
			maxx = Double.MIN_VALUE, maxy = Double.MIN_VALUE;
		for( Shape pShape : shapes ) {
			Bounds bounds = pShape.getBounds();
			minx = Math.min(minx, bounds.x);
			maxx = Math.max(maxx, bounds.x + bounds.width);
			miny = Math.min(miny, bounds.y);
			maxy = Math.max(maxy, bounds.y + bounds.height);
		}

		//groupWidth = maxx - minx;
		//groupHeight = maxy - miny;
		groupWidth = maxx;
		groupHeight = maxy;
	}

	public void pack() {
		double minx = Double.MAX_VALUE, miny = Double.MAX_VALUE,
			maxx = Double.MIN_VALUE, maxy = Double.MIN_VALUE;
		for( Shape pShape : shapes ) {
			Bounds bounds = pShape.getBounds();
			minx = Math.min(minx, bounds.x);
			maxx = Math.max(maxx, bounds.x + bounds.width);
			miny = Math.min(miny, bounds.y);
			maxy = Math.max(maxy, bounds.y + bounds.height);
		}

		x = minx;
		y = miny;
		groupWidth = maxx - minx;
		groupHeight = maxy - miny;
	}

	@Override
	public java.awt.Shape getShape() {
		Path2D.Double gruppe = new Path2D.Double();
		for( Shape pShape : shapes ) {
			gruppe.append(pShape.getShape(), false);
		}
		return gruppe;
	}

	@Override
	public void draw( Graphics2D graphics, AffineTransform transform ) {
		if( !visible ) {
			return;
		}

		/*
		AffineTransform verzerrung = new AffineTransform();
		verzerrung.translate(x, y);
		verzerrung.rotate(Math.toRadians(rotation));
		//verzerrung.scale(skalierung, skalierung);
		verzerrung.translate(-anchor.x, -anchor.y);
		*/

		for( Shape f : shapes ) {
			AffineTransform af = f.getTransform();
			af.preConcatenate(transform);
			f.draw(graphics, af);
		}
	}

}
