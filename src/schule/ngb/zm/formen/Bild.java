package schule.ngb.zm.formen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Bild extends Form {

	private double breite;

	private double hoehe;

	private BufferedImage bild;

	public Bild( String pQuelle ) {
		this(0, 0, pQuelle);
	}

	public Bild( double pX, double pY, String pQuelle ) {
		super(pX, pY);

		if( pQuelle == null || pQuelle.length() == 0 )
			throw new IllegalArgumentException("Bildquelle darf nicht null oder leer sein.");

		try {
			// Bilddatei aus dem Arbeitsverzeichnis laden
			File file = new File(pQuelle);
			if( file.isFile() ) {
				bild = ImageIO.read(file);
			} else {
				// relativ zur .class-Datei
				URL url = getClass().getResource(pQuelle);

				// relativ zum ClassLoader
				if( url == null ) {
					url = getClass().getClassLoader().getResource(pQuelle);
				}

				// aWebadresse oder JAR-Datei
				if( url == null ) {
					url = new URL(pQuelle);
				}

				bild = ImageIO.read(url);
			}

			if( bild == null ) {
				throw new IllegalArgumentException("Bild konnte nicht aus " + pQuelle + " geladen werden!");
			}

			breite = bild.getWidth(null);
			hoehe = bild.getHeight(null);
			setAnkerpunkt(ZENTRUM);
		} catch( IOException ioe ) {
			throw new IllegalArgumentException("Bild konnte nicht aus " + pQuelle + " geladen werden!", ioe);
		}
	}

	public Bild( Bild pBild ) {
		super(pBild.getX(), pBild.getY());
		kopiere(pBild);
	}

	@Override
	public Form kopie() {
		return new Bild(this);
	}

	@Override
	public void kopiere( Form pForm ) {
		super.kopiere(pForm);
		if( pForm instanceof Bild ) {
			Bild pBild = (Bild)pForm;
			bild = new BufferedImage(pBild.bild.getWidth(), pBild.bild.getHeight(), pBild.bild.getType());
			Graphics2D g = bild.createGraphics();
			g.drawImage(pBild.bild, 0, 0, null);
			g.dispose();

			breite = bild.getWidth(null);
			hoehe = bild.getHeight(null);
			setAnkerpunkt(pForm.getAnkerpunkt());
		}
	}

	public double getBreite() {
		return breite;
	}

	public void setBreite( double pBreite ) {
		skalieren(pBreite / breite);
	}

	public double getHoehe() {
		return hoehe;
	}

	public void setHoehe( double pHoehe ) {
		skalieren(pHoehe / hoehe);
	}

	@Override
	public void setAnkerpunkt( byte pAnker ) {
		ankerBerechnen(breite, hoehe, pAnker);
	}

	@Override
	public void skalieren( double pFaktor ) {
		super.skalieren(pFaktor);
		breite *= pFaktor;
		hoehe *= pFaktor;
	}

	@Override
	public Shape getShape() {
		return new Rectangle2D.Double(0, 0, breite, hoehe);
	}

	/*
	@Override
	public AffineTransform getVerzerrung() {
		AffineTransform verzerrung = new AffineTransform();
		verzerrung.translate(x, y);
		verzerrung.scale(skalierung, skalierung);
		verzerrung.rotate(Math.toRadians(drehwinkel));
		verzerrung.translate(-anker.x, -anker.y);
		return verzerrung;
	}
	*/

	@Override
	public void zeichnen( Graphics2D graphics, AffineTransform pVerzerrung ) {
		if( !sichtbar ) {
			return;
		}

		AffineTransform current = graphics.getTransform();
		graphics.transform(getVerzerrung());
		graphics.drawImage(bild, 0, 0, (int) breite, (int) hoehe, null);
		graphics.setTransform(current);
	}

}
