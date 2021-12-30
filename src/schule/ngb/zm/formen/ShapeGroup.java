package schule.ngb.zm.formen;

import schule.ngb.zm.Options;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ShapeGroup extends Shape {

	private List<Shape> shapes;

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
		setAnchor(CENTER);
	}

	public Shape copy() {
		// TODO: implement?
		return null;
	}

	public void add( Shape... shapes ) {
		for( Shape shape : shapes ) {
			add(shape, false);
		}
	}

	public void add( Shape pShape, boolean relative ) {
		if( relative ) {
			pShape.x = pShape.x - x;
			pShape.y = pShape.y - y;
		}
		shapes.add(pShape);
	}

	public void removeAll() {
		shapes.clear();
	}

	public List<Shape> getShapes() {
		return shapes;
	}
	public <ShapeType extends Shape> List<ShapeType> getShapes( Class<ShapeType> typeClass ) {
		LinkedList<ShapeType> list = new LinkedList<>();
		for( Shape s: shapes ) {
			if( typeClass.getClass().isInstance(s) ) {
				list.add((ShapeType)s);
			}
		}
		return list;
	}

	public void remove( Shape shape ) {
		shapes.remove(shape);
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
	public void setAnchor( Options.Direction anchor ) {
		double minx = Double.MAX_VALUE, miny = Double.MAX_VALUE,
			maxx = Double.MIN_VALUE, maxy = Double.MIN_VALUE;
		for( Shape pShape : shapes ) {
			Rectangle bounds = pShape.getBounds();
			if( bounds.x < minx )
				minx = bounds.x;
			if( bounds.y < miny )
				miny = bounds.y;
			if( bounds.x+bounds.width > maxx )
				maxx = bounds.x+bounds.width;
			if( bounds.y+bounds.height > maxy )
				maxy = bounds.y+bounds.height;
		}

		calculateAnchor(maxx-minx, maxy-miny, anchor);
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
	public void draw( Graphics2D graphics, AffineTransform pVerzerrung ) {
		if( !visible ) {
			return;
		}

		AffineTransform verzerrung = new AffineTransform();
		verzerrung.translate(x, y);
		verzerrung.rotate(Math.toRadians(rotation));
		//verzerrung.scale(skalierung, skalierung);
		verzerrung.translate(-anchor.x, -anchor.y);

		for( Shape f: shapes ) {
			AffineTransform af = f.getTransform();
			af.preConcatenate(verzerrung);
			f.draw(graphics, af);
		}
	}

}
