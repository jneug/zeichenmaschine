import schule.ngb.zm.Drawable;
import schule.ngb.zm.Updatable;
import schule.ngb.zm.shapes.Picture;

public class Hehomon {

	private String name, typ, bild;

	protected int angr, vert;

	protected int lp, lpMax;

	protected boolean paralysiert = false, vergiftet = false;

	private String angr1, angr2, vert1, vert2;

	public Hehomon( String pName, String pTyp, int pLp, int pAngr, int pVert, String pAngr1, String pAngr2, String pVert1, String pVert2, String pBild ) {
		name = pName;
		lp = pLp;
		lpMax = pLp;
		angr = pAngr;
		vert = pVert;
		angr1 = pAngr1;
		angr2 = pAngr2;
		vert1 = pVert1;
		vert2 = pVert2;
		bild = pBild;
	}

	public String getName() {
		return name;
	}

	public String getTyp() {
		return typ;
	}

	public String getBild() {
		return bild;
	}

	public int getLp() {
		return lp;
	}

	public int getLpMax() {
		return lpMax;
	}

	public int getAngr() {
		return angr;
	}

	public int getVert() {
		return vert;
	}

	public boolean isParalysiert() {
		return paralysiert;
	}

	public boolean isVergiftet() {
		return vergiftet;
	}

	public String getNameAngr1() {
		return angr1;
	}

	public String getNameAngr2() {
		return angr2;
	}

	public String getNameVert1() {
		return vert1;
	}

	public String getNameVert2() {
		return vert2;
	}

	public void setAngr( int pAngr ) {
		angr = pAngr;
	}

	public void setVert( int pVert ) {
		vert = pVert;
	}

	public void setParalysiert( boolean pParalysiert ) {
		paralysiert = pParalysiert;
	}

	public void setVergiftet( boolean pVergiftet ) {
		vergiftet = pVergiftet;
	}

	public void nimmSchaden( int pSchaden ) {
		if( lp-pSchaden < 0 ) {
			lp = 0;
		} else {
			lp -= pSchaden;
		}
	}

	public void heilen( int pHeilung ) {
		if( lp+pHeilung > lpMax ) {
			lp = lpMax;
		} else {
			lp += pHeilung;
		}
	}

	public void angriff1( Hehomon gegner ) {

	}

	public void angriff2( Hehomon gegner ) {

	}

	public void verteidigung1( Hehomon gegner ) {

	}

	public void verteidigung2( Hehomon gegner ) {

	}

}
