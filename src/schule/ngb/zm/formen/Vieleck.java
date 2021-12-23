package schule.ngb.zm.formen;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Arrays;

public class Vieleck extends Form {

	protected Point2D[] ecken;

	public Vieleck( double pX, double pY, Point2D... pEcken ) {
		super(pX, pY);

		ecken = new Point2D[pEcken.length];
		for( int i = 0; i < pEcken.length; i++ ) {
			ecken[i] = new Point2D.Double(pEcken[i].getX()-pX, pEcken[i].getY()-pY);
		}
	}

	public Vieleck( Point2D... pEcken ) {
		super();

		ecken = new Point2D[pEcken.length];
		for( int i = 0; i < pEcken.length; i++ ) {
			if( i == 0 ) {
				x = pEcken[i].getX();
				y = pEcken[i].getY();
			}
			ecken[i] = new Point2D.Double(pEcken[i].getX()-x, pEcken[i].getY()-y);
		}
	}

	public Vieleck( Vieleck pVieleck ) {
		this(pVieleck.x, pVieleck.y);
		kopiere(pVieleck);
	}

	public Point2D[] getEcken() {
		return ecken;
	}

	@Override
	public void kopiere( Form pForm ) {
		super.kopiere(pForm);
		if( pForm instanceof Vieleck ) {
			Vieleck v = (Vieleck) pForm;

			ecken = new Point2D[v.ecken.length];
			for( int i = 0; i < v.ecken.length; i++ ) {
				ecken[i] = new Point2D.Double(v.ecken[i].getX(), v.ecken[i].getY());
			}
		}
	}

	@Override
	public Form kopie() {
		return new Vieleck(this);
	}

	@Override
	public Shape getShape() {
		Path2D shape = new Path2D.Double();
		shape.moveTo(ecken[0].getX(), ecken[0].getY());
		for( int i = 1; i < ecken.length; i++ ) {
			shape.lineTo(ecken[i].getX(), ecken[i].getY());
		}
		shape.closePath();
		return shape;
	}

}
