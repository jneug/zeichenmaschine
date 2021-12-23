package schule.ngb.zm;

import schule.ngb.zm.formen.Formenebene;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Eine Leinwand ist die Hauptkomponente einer Zeichenmaschine. Sie besteht aus
 * mehreren Ebenen, auf denen auf verschiedene Arten gezeichnet werden kann. Die
 * Ebenen lassen sich beliebig übereinander anordenen, ausblenden oder wieder
 * löschen.
 *
 * Jede Ebene besitzt eine Zeichenfläche, auf der ihre Zeichnung liegt. Diese
 * zeichenflächen werden pro Frame einmal von "unten" nach "oben" auf diese
 * Leinwand gezeichnet.
 */
public class Leinwand extends JComponent {


	private LinkedList<Ebene> ebenen;

	public Leinwand( int pBreite, int pHoehe ) {
		Dimension dim = new Dimension(pBreite, pHoehe);
		super.setSize(pBreite, pHoehe);
		this.setPreferredSize(dim);
		this.setMinimumSize(dim);

		ebenen = new LinkedList<>();
		ebenen.add(new Zeichenebene());
		ebenen.add(new Formenebene());
		setBackground(Konstanten.STD_HINTERGRUND);
	}

	@Override
	public void setSize( int pBreite, int pHoehe ) {
		Dimension dim = new Dimension(pBreite, pHoehe);
		super.setSize(pBreite, pHoehe);
		this.setPreferredSize(dim);
		this.setMinimumSize(dim);

		for( Ebene ebene : ebenen ) {
			ebene.setGroesse(getWidth(), getHeight());
		}
	}

	public void hinzu( Ebene pEbene ) {
		if( pEbene != null ) {
			pEbene.setGroesse(getWidth(), getHeight());
			ebenen.add(pEbene);
		}
	}

	public void hinzu( int pIndex, Ebene pEbene ) {
		if( pEbene != null ) {
			pEbene.setGroesse(getWidth(), getHeight());
			ebenen.add(pIndex, pEbene);
		}
	}

	public java.util.List<Ebene> getEbenen() {
		return ebenen;
	}

	/**
	 * Holt die {@link Ebene} am Index <var>i</var> (beginnend bei <code>0</code>).
	 *
	 * @param i Index der Ebene (beginnend bei <code>0</code>).
	 * @return Die Ebene am Index <var>i</var> oder <code>null</code>.
	 * @throws IndexOutOfBoundsException Falls der Index nicht existiert.
	 */
	public Ebene getEbene( int i ) {
		if( ebenen.size() > i ) {
			return ebenen.get(i);
		} else {
			throw new IndexOutOfBoundsException("Keine Ebene mit dem Index " + i + " vorhanden (maximum: " + (ebenen.size() - 1) + ").");
		}
	}

	/**
	 * Holt die erste Ebene des angegebenen Typs aus der Liste der Ebenen.
	 * Existiert keine solche Ebene, wird <code>null</code> zurückgegeben.
	 *
	 * @param pClazz Typ der Ebene.
	 * @param <L>
	 * @return Erste Ebene vom angegeben Typ.
	 */
	public <L extends Ebene> L getEbene( Class<L> pClazz ) {
		for( Ebene ebene : ebenen ) {
			if( ebene.getClass().equals(pClazz) ) {
				return pClazz.cast(ebene);
			}
		}
		return null;
	}

	public <L extends Ebene> java.util.List<L> getEbenen( Class<L> pClazz ) {
		ArrayList<L> result = new ArrayList<>(ebenen.size());
		for( Ebene ebene : ebenen ) {
			if( ebene.getClass().equals(pClazz) ) {
				result.add(pClazz.cast(ebene));
			}
		}
		return result;
	}

	public void paintComponent( Graphics g ) {
		Graphics2D g2d = (Graphics2D) g.create();

		for( Ebene ebene : ebenen ) {
			ebene.zeichnen(g2d);
		}

		g2d.dispose();
	}
}
