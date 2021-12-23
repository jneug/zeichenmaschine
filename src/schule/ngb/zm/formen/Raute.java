package schule.ngb.zm.formen;

import java.awt.*;
import java.awt.geom.Path2D;

public class Raute extends Drache {

	private double breite;

	private double hoehe;

	public Raute( double pX, double pY, double pBreite, double pHoehe ) {
		super(pX, pY, pBreite, pHoehe, 0.5);
		setAnkerpunkt(ZENTRUM);
	}

	public Raute( Raute pRaute ) {
		this(pRaute.x, pRaute.y, pRaute.breite, pRaute.hoehe);
		kopiere(pRaute);
	}

	@Override
	public void setVerhaeltnis( double pVerhaeltnis ) {
		super.setVerhaeltnis(0.5);
	}

	@Override
	public void kopiere( Form pForm ) {
		super.kopiere(pForm);
		super.setVerhaeltnis(0.5);
	}

	@Override
	public Drache kopie() {
		return new Drache(this);
	}

}
