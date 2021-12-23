package schule.ngb.zm;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Ebene extends Konstanten implements Zeichenbar, Aktualisierbar {

	protected BufferedImage puffer;

	protected Graphics2D zeichnung;

	protected boolean sichtbar = true;

	protected boolean aktiv = true;


	public Ebene() {
		this(STD_BREITE, STD_HOEHE);
	}

	public Ebene( int pBreite, int pHoehe ) {
		zeichnungErstellen(pBreite, pHoehe);
	}

	public int getBreite() {
		return puffer.getWidth();
	}

	public int getHoehe() {
		return puffer.getHeight();
	}

	public void setGroesse( int pBreite, int pHoehe ) {
		if( puffer != null ) {
			zeichnenWiederherstellen(pBreite, pHoehe);
		} else {
			zeichnungErstellen(pBreite, pHoehe);
		}
	}

	private void zeichnungErstellen( int pBreite, int pHoehe ) {
		puffer = new BufferedImage(pBreite, pHoehe, BufferedImage.TYPE_INT_ARGB);
		zeichnung = puffer.createGraphics();

		// add antialiasing
		RenderingHints hints = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON
		);
		hints.put(
			RenderingHints.KEY_RENDERING,
			RenderingHints.VALUE_RENDER_QUALITY
		);
		zeichnung.addRenderingHints(hints);
	}

	private void zeichnenWiederherstellen( int pBreite, int pHoehe ) {
		BufferedImage oldCanvas = puffer;
		zeichnungErstellen(pBreite, pHoehe);
		zeichnung.drawImage(oldCanvas, 0, 0, null);
	}

	/**
	 * Leert die Ebene und löscht alles bisher gezeichnete.
	 */
	public abstract void leeren();

	/**
	 * Zeichnet den Puffer auf die Grafik-Instanz.
	 *
	 * @param pGraphics
	 */
	@Override
	public void zeichnen( Graphics2D pGraphics ) {
		if( sichtbar ) {
			pGraphics.drawImage(puffer, 0, 0, null);
		}
	}

	@Override
	public boolean istSichtbar() {
		return sichtbar;
	}

	public void verstecken() {
		sichtbar = false;
	}

	public void zeigen() {
		sichtbar = true;
	}

	public void umschalten() {
		sichtbar = !sichtbar;
	}

	@Override
	public void aktualisieren( double delta ) {
	}

	@Override
	public boolean istAktiv() {
		return aktiv;
	}

}
