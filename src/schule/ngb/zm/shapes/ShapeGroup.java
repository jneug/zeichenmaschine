package schule.ngb.zm.shapes;

import schule.ngb.zm.Options;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
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
 */
public class ShapeGroup extends Shape {

	private List<Shape> shapes;

	private double width = -1.0;

	private double height = -1.0;

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
			if( typeClass.getClass().isInstance(s) ) {
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
		if( width < 0 ) {
			calculateBounds();
		}
		return width;
	}

	@Override
	public double getHeight() {
		if( height < 0 ) {
			calculateBounds();
		}
		return height;
	}

	private void invalidateBounds() {
		width = -1.0;
		height = -1.0;
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

		width = maxx - minx;
		height = maxy - miny;
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
