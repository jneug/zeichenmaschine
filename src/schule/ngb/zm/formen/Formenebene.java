package schule.ngb.zm.formen;

import schule.ngb.zm.Ebene;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Formenebene extends Ebene {

	private LinkedList<Form> formen;

	public Formenebene() {
		super();
		formen = new LinkedList<Form>();
	}

	public Formenebene( int pWidth, int pHeight ) {
		super(pWidth, pHeight);
		formen = new LinkedList<Form>();
	}

	public void anzeigen( Form... pFormen ) {
		synchronized( formen ) {
			for( Form f: pFormen ) {
				formen.add(f);
			}
		}
	}

	public void alleVerstecken() {
		synchronized( formen ) {
			for( Form form : formen ) {
				form.verstecken();
			}
		}
	}

	public void alleZeigen() {
		synchronized( formen ) {
			for( Form form : formen ) {
				form.zeigen();
			}
		}
	}

	public void leeren() {
		Color currentColor = zeichnung.getBackground();
		zeichnung.setBackground(new Color(0, 0, 0, 0));
		zeichnung.clearRect(0, 0, puffer.getWidth(), puffer.getHeight());
		zeichnung.setBackground(currentColor);
	}

	public java.util.List<Form> getShapes() {
		return formen;
	}

	@Override
	public void zeichnen( Graphics2D pGraphics ) {
		synchronized( formen ) {
			for( Form form : formen ) {
				if( form.istSichtbar() ) {
					form.zeichnen(zeichnung);
				}
			}
		}

		super.zeichnen(pGraphics);
	}
}
