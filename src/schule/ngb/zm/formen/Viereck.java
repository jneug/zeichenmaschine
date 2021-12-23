package schule.ngb.zm.formen;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Arrays;

public class Viereck extends Vieleck {

	public Viereck( double pX, double pY, Point2D... pEcken ) {
		super(pX, pY, Arrays.copyOf(pEcken, 4));
		if( pEcken.length < 4 ) {
			throw new IllegalArgumentException("Ein Viereck muss genau vier Eckpunkte besitzen.");
		}
	}

	public Viereck( Point2D... pEcken ) {
		super(Arrays.copyOf(pEcken, 4));
		if( pEcken.length < 4 ) {
			throw new IllegalArgumentException("Ein Viereck muss genau vier Eckpunkte besitzen.");
		}
	}

	public Viereck( Viereck pViereck ) {
		super(pViereck.x, pViereck.y);
		kopiere(pViereck);
	}

	@Override
	public Form kopie() {
		return new Viereck(this);
	}

}
