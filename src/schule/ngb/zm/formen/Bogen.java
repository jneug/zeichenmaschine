package schule.ngb.zm.formen;

import java.awt.*;
import java.awt.geom.Arc2D;

public class Bogen extends Form {

	protected double breite;

	protected double hoehe;

	protected double winkel;

	protected double startwinkel;

	protected int typ = OFFEN;

	public Bogen( double pX, double pY, double pRadius, double pWinkel ) {
		this(pX, pY, pRadius, pRadius, pWinkel, 0.0);
	}

	public Bogen( double pX, double pY, double pRadius, double pWinkel, double pStartwinkel ) {
		this(pX, pY, pRadius, pRadius, pWinkel, pStartwinkel);
	}

	public Bogen( double pX, double pY, double pBreite, double pHoehe, double pWinkel, double pStartwinkel ) {
		super(pX, pY);
		breite = pBreite;
		hoehe = pHoehe;
		winkel = pWinkel;
		startwinkel = pStartwinkel;
		setAnkerpunkt(ZENTRUM);

		keineFuellung();
	}

	public Bogen( Bogen pBogen ) {
		this(pBogen.x, pBogen.y, pBogen.breite, pBogen.hoehe, pBogen.winkel, pBogen.startwinkel);
		kopiere(pBogen);
	}

	public double getBreite() {
		return breite;
	}

	public void setBreite( double pBreite ) {
		this.breite = pBreite;
	}

	public double getHoehe() {
		return hoehe;
	}

	public void setHoehe( double pHoehe ) {
		this.hoehe = pHoehe;
	}

	public double getWinkel() {
		return winkel;
	}

	public void setWinkel( double pWinkel ) {
		this.winkel = pWinkel;
	}

	public double getStartwinkel() {
		return startwinkel;
	}

	public void setStartwinkel( double pStartwinkel ) {
		this.startwinkel = pStartwinkel;
	}

	public int getTyp() {
		return typ;
	}

	public void setTyp( int pTyp ) {
		this.typ = pTyp;
	}

	@Override
	public Form kopie() {
		return new Bogen(this);
	}

	@Override
	public void kopiere(Form pForm) {
		super.kopiere(pForm);
		if( pForm instanceof Bogen ) {
			Bogen b = (Bogen) pForm;
			breite = b.breite;
			hoehe = b.hoehe;
			winkel = b.winkel;
			startwinkel = b.startwinkel;
			typ = b.typ;
		}
	}

	@Override
	public void setAnkerpunkt( byte pAnker ) {
		ankerBerechnen(breite, hoehe, pAnker);
	}

	@Override
	public Shape getShape() {
		return new Arc2D.Double(0, 0, breite, hoehe, startwinkel, winkel, typ);
	}

}
