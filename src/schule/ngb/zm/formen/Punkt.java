package schule.ngb.zm.formen;

import java.awt.geom.Point2D;

public class Punkt extends Kreis {

	private static final double PUNKT_RADIUS = 2.0;


	public Punkt( double pX, double pY ) {
		super(pX, pY, PUNKT_RADIUS);
	}

	public Punkt( Point2D pPunkt ) {
		super(pPunkt.getX(), pPunkt.getY(), PUNKT_RADIUS);
	}

	public Punkt( Form pForm ) {
		super(pForm.getX(), pForm.getY(), PUNKT_RADIUS);
	}

	@Override
	public void skalieren( double pFaktor ) {
		// Skalierung ist für Punkte deaktiviert
		return;
	}

	@Override
	public void setAnkerpunkt( byte pAnker ) {
		// Punkte sind immer im Zentrum verankert
		ankerBerechnen(radius + radius, radius + radius, ZENTRUM);
	}

}
