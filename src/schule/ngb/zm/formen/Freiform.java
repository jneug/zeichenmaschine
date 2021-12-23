package schule.ngb.zm.formen;

import java.awt.*;
import java.awt.geom.Path2D;

public class Freiform extends Form {

	private Path2D.Double pfad;

	public Freiform(double pX, double pY) {
		super(pX, pY);
		pfad = new Path2D.Double();
	}

	public void linieNach( double pX, double pY ) {
		pfad.lineTo(pX-x, pY-y);
	}

	public void bogenNach( double pX1, double pY1, double pX2, double pY2 ) {
		pfad.quadTo(pX1-x, pY1-y, pX2-x, pY2-y);
	}

	public void kurveNach( double pX1, double pY1, double pX2, double pY2, double pX3, double pY3 ) {
		pfad.curveTo(pX1-x, pY1-y, pX2-x, pY2-y, pX3-x, pY3-y);
	}

	public void schliessen() {
		pfad.lineTo(0,0);
	}

	@Override
	public Form kopie() {
		return null;
	}

	@Override
	public Shape getShape() {
		return new Path2D.Double(pfad);
	}
}
