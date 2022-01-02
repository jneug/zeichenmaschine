package schule.ngb.zm.shapes;

import schule.ngb.zm.Options;

import java.awt.geom.Point2D;

public class Point extends Circle {

	private static final double POINT_RADIUS = 2.0;

	/**
	 * Erstellt ein Punktobjekt mit den angegebenen Koordinaten.
	 * @param x
	 * @param y
	 */
	public Point( double x, double y ) {
		super(x, y, POINT_RADIUS);
	}

	/**
	 * Erstellt ein Punktobjekt mit den Koordinaten des übergebenen
	 * {@link schule.ngb.zm.Vector Vektors}.
	 * @param point
	 */
	public Point( schule.ngb.zm.Vector vector ) {
		super(vector.x, vector.y, POINT_RADIUS);
	}

	/**
	 * Erstellt ein Punktobjekt mit den Koordinaten des übergebenen
	 * {@link java.awt.geom.Point2D Punktes}.
	 * @param point
	 */
	public Point( Point2D point ) {
		super(point.getX(), point.getY(), POINT_RADIUS);
	}

	/**
	 * Erstellt ein Punktobjekt mit den Koordinaten der angegeben Form.
	 * @param shape
	 */
	public Point( Shape shape ) {
		super(shape.getX(), shape.getY(), POINT_RADIUS);
	}

	@Override
	public void scale( double factor ) {
		// Skalierung ist für Punkte deaktiviert
		return;
	}

	@Override
	public void setAnchor( Options.Direction anchor ) {
		// Punkte sind immer im Zentrum verankert
		calculateAnchor(radius + radius, radius + radius, CENTER);
	}

}
