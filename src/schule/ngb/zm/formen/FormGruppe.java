package schule.ngb.zm.formen;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class FormGruppe extends Form {

	private List<Form> formen;

	public FormGruppe() {
		super();
		formen = new ArrayList<>(10);
	}

	public FormGruppe( double pX, double pY ) {
		super(pX, pY);
		formen = new ArrayList<>(10);
	}

	public FormGruppe( double pX, double pY, Form... pFormen ) {
		super(pX, pY);
		formen = new ArrayList<>(pFormen.length);
		for( Form form : pFormen ) {
			formen.add(form);
			form.gruppe = this;
		}
		setAnkerpunkt(ZENTRUM);
	}

	public Form kopie() {
		return null;
	}

	public void hinzu( Form... pFormen ) {
		for( Form form : pFormen ) {
			hinzu(form, false);
		}
	}

	public void hinzu( Form pForm ) {
		hinzu(pForm, false);
	}

	public void hinzu( Form pForm, boolean pKoordinatenAnpassen ) {
		if( pKoordinatenAnpassen ) {
			pForm.x = pForm.x - x;
			pForm.y = pForm.y - y;
		}
		formen.add(pForm);
		pForm.gruppe = this;
	}

	@Override
	public void setAnkerpunkt( byte pAnker ) {
		double minx = Double.MAX_VALUE, miny = Double.MAX_VALUE,
			maxx = Double.MIN_VALUE, maxy = Double.MIN_VALUE;
		for( Form form : formen ) {
			Rechteck bounds = form.getBegrenzung();
			if( bounds.x < minx )
				minx = bounds.x;
			if( bounds.y < miny )
				miny = bounds.y;
			if( bounds.x+bounds.breite > maxx )
				maxx = bounds.x+bounds.breite;
			if( bounds.y+bounds.hoehe > maxy )
				maxy = bounds.y+bounds.hoehe;
		}

		ankerBerechnen(maxx-minx, maxy-miny, pAnker);
	}

	@Override
	public Shape getShape() {
		Path2D.Double gruppe = new Path2D.Double();
		for( Form form : formen ) {
			gruppe.append(form.getShape(), false);
		}
		return gruppe;
	}

	@Override
	public void zeichnen( Graphics2D graphics, AffineTransform pVerzerrung ) {
		if( !sichtbar ) {
			return;
		}

		AffineTransform verzerrung = new AffineTransform();
		verzerrung.translate(x, y);
		verzerrung.rotate(Math.toRadians(drehwinkel));
		//verzerrung.scale(skalierung, skalierung);
		verzerrung.translate(-anker.x, -anker.y);

		for( Form f: formen ) {
			AffineTransform af = f.getVerzerrung();
			af.preConcatenate(verzerrung);
			f.zeichnen(graphics, af);
		}
	}

}
