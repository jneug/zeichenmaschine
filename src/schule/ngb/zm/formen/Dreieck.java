package schule.ngb.zm.formen;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Arrays;

public class Dreieck extends Vieleck {

	public Dreieck( double pX, double pY, Point2D... pEcken ) {
		super(pX, pY, Arrays.copyOf(pEcken, 3));
		if( pEcken.length < 3 ) {
			throw new IllegalArgumentException("Ein Dreieck muss genau drei Eckpunkte besitzen.");
		}
	}

	public Dreieck( Point2D... pEcken ) {
		super(Arrays.copyOf(pEcken, 3));
		if( pEcken.length < 3 ) {
			throw new IllegalArgumentException("Ein Dreieck muss genau drei Eckpunkte besitzen.");
		}
	}

	public Dreieck( Dreieck pDreieck ) {
		super(pDreieck.x, pDreieck.y);
		kopiere(pDreieck);
	}

	@Override
	public Form kopie() {
		return new Dreieck(this);
	}
}
