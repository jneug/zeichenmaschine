import schule.ngb.zm.Color;
import schule.ngb.zm.Zeichenmaschine;

import java.awt.event.KeyEvent;

/**
 * Eine Bildergallerie von Bildern eines Informatikkurses des 10. Jahrgangs.
 */
public class Gallery extends Zeichenmaschine {

	public static void main( String[] args ) {
		new Gallery();
	}

	public void setup() {
		setSize(600, 600);
		setTitle("ZM: Gallery");
	}

	/**
	 * Wählt ein zufälliges Bild aus und zeigt es an.
	 */
	public void draw() {
		drawRandom();
	}

	/**
	 * Zeigt ein zufälliges Bild an.
	 */
	public void drawRandom() {
		switch( random(0, 4) ) {
			case 0:
				rainbow();
				break;
			case 1:
				snowman();
				break;
			case 2:
				deathstar();
				break;
			case 3:
				prideflag();
				break;
		}
	}

	/**
	 * Bei Betätigen der Leertaste ein Zufallsbild anzeigen
	 */
	public void keyPressed() {
		if( keyCode == 32 ) {
			drawRandom();
		}
	}

	/**
	 * Startet eine fortlaufende Präsentation aller Bilder.
	 *
	 * @param pWartezeit Die Wartezeit zum nächsten Bildwechsel in Millisekunden.
	 */
	public void slideshow( int pWartezeit ) {
		int i = 0;
		while( true ) {
			switch( i ) {
				case 0:
					rainbow();
					break;
				case 1:
					snowman();
					break;
				case 2:
					deathstar();
					break;
				case 3:
					prideflag();
					break;
			}

			i = (i + 1) % 4;
			delay(pWartezeit);
		}
	}

	/**
	 * Regenbogen
	 * <p>
	 * von
	 */
	public void rainbow() {
		// Blauer Hintergrund
		drawing.clear(60, 155, 217);

		// Einige Werte berechnen, um Bild an Bildgröße anzupassen
		double size = (width+height)*0.03333;
		double r = width / 2.0;

		// Kleiner werdende Kreise in den Farben des Bogens zeichnen
		drawing.noStroke();
		drawing.setFillColor(207, 71, 67);
		drawing.circle(width / 2.0, height, r - 1 * size);
		drawing.setFillColor(235, 134, 42);
		drawing.circle(width / 2.0, height, r - 2 * size);
		drawing.setFillColor(234, 181, 58);
		drawing.circle(width / 2.0, height, r - 3 * size);
		// Mitte mit "himmelfarbe" übermalen, um Ringe zu erzeugen
		drawing.setFillColor(60, 155, 217);
		drawing.circle(width / 2.0, height, r - 4 * size);

		// Sonne zeichnen
		drawing.setFillColor(232, 200, 52);
		drawing.circle(width*0.8333, size*2.6666, size*2);

		// Bild auf Leinwand übertragen
		redraw();
	}

	/**
	 * LGBTQ-Flagge
	 * <p>
	 * von
	 */
	public void prideflag() {
		// Schwarzer Hintergrund
		drawing.clear(0);
		drawing.setStrokeColor(0);

		// Farben der Streifen festlegen
		Color[] colors = new Color[]{
			RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE
		};

		// Breite der Streifen an Bildgröße anpassen
		double size = height*0.06666;
		double borderX = width/6.0, borderY = height/6.0;


		// Flaggenstab zeichnen
		drawing.setFillColor(BROWN);
		drawing.rect(borderX, borderY, size, height - 2*borderY, NORTHWEST);
		// Streifen zeichnen
		for( int i = 0; i < colors.length; i++ ) {
			drawing.setFillColor(colors[i]);
			drawing.rect(borderX, borderY + i * size, width - 2*borderX, size, NORTHWEST);
		}

		// Bild auf Leinwand übertragen
		redraw();
	}

	/**
	 * Todesstern
	 * <p>
	 * von Niklas, Henry und Ben
	 */
	public void deathstar() {
		// Sternenhimmel zeichnen
		drawing.clear(0);
		drawing.noStroke();
		drawing.setFillColor(WHITE);

		// Zufällig erzeugte Sterne
		for( int i = 0; i < (width+height)/4; i++ ) {
			drawing.circle(random(0.0, width), random(0.0, height), random(1, 3));
		}

		// Einige Werte berechnen, die später verwendet werden, um
		// die Zeichnung an die Bildschirmgröße anzupassen.
		double radius = limit(width*0.6666, height*0.6666) / 2.0;
		double w2 = width / 2.0, h2 = height / 2.0;
		double r2 = radius / 2.0,
			r3 = radius / 3.0, r4 = radius / 4.0,
			r5 = radius / 5.0, r10 = radius / 10.0;

		// Korpus zeichnen
		drawing.setFillColor(128);
		drawing.circle(width / 2.0, height / 2.0, radius);
		// Graben am Äquator zeichnen
		drawing.setStrokeColor(0);
		drawing.setStrokeWeight(1.4);
		drawing.arc(width / 2.0, height / 2.0, radius + radius, r2, 180, 360);

		// Schüssel zeichnen
		drawing.setFillColor(96);
		drawing.circle(w2 + r2, h2 - r3, r4);
		drawing.setFillColor(84);
		drawing.circle(w2 + r2 + 2, h2 - r3 - 2, r10);

		// Strahlen des Lasers zeichnen
		int beams = 8; // Anzahl Strahlen
		for( int i = 0; i < beams; i++ ) {
			drawing.setStrokeColor(GREEN);
			drawing.setStrokeType(SOLID);
			drawing.setStrokeWeight(1.6);
			drawing.line(
				w2 + r2 - r4 * cos(radians(360 / beams * i)),
				h2 - r3 + r4 * sin(radians(360 / beams * i)),
				w2 + r2 + 2 + r5,
				h2 - r3 - 2 - r5
			);

			drawing.setStrokeType(DASHED);
			drawing.setStrokeWeight(3.0);
			drawing.line(
				w2 + r2 - r4 * cos(radians(360 / beams * i)),
				h2 - r3 + r4 * sin(radians(360 / beams * i)),
				w2 + r2 + 2 + r5,
				h2 - r3 - 2 - r5
			);

			drawing.setStrokeColor(WHITE);
			drawing.setStrokeType(SOLID);
			drawing.setStrokeWeight(1.0);
			drawing.line(
				w2 + r2 - r4 * cos(radians(360 / beams * i)),
				h2 - r3 + r4 * sin(radians(360 / beams * i)),
				w2 + r2 + 2 + r5,
				h2 - r3 - 2 - r5
			);
		}

		// Hauptstrahl
		drawing.setStrokeColor(GREEN);
		drawing.setStrokeType(SOLID);
		drawing.setStrokeWeight(4);
		drawing.line(w2 + r2 + 2 + r5, h2 - r3 - 2 - r5, width, h2 - w2 + radius / 6.0);
		drawing.setStrokeWeight(1);
		drawing.setStrokeColor(255);
		drawing.line(w2 + r2 + 2 + r5, h2 - r3 - 2 - r5, width, h2 - w2 + radius / 6.0);

		// Bild auf Leinwand übertragen
		redraw();
	}

	/**
	 * Schneemann
	 * <p>
	 * von
	 */
	public void snowman() {
		// Hellblauer Hintergrund
		drawing.clear(219, 253, 255);
		drawing.noStroke();
		drawing.setFillColor(255);

		// Drei Schneekugeln
		drawing.circle(width / 2.0, height - 100, 100);
		drawing.circle(width / 2.0, height - 180 - 60, 60);
		drawing.circle(width / 2.0, height - 180 - 110 - 40, 40);

		// Zwei Augen
		drawing.setFillColor(0);
		drawing.circle(width / 2.0 - 15, height - 345, 4);
		drawing.circle(width / 2.0 + 15, height - 345, 4);

		// Nase
		drawing.setFillColor(255, 147, 0);
		drawing.triangle(width / 2.0, height - 335, width / 2.0 - 5, height - 330, width / 2.0 + 5, height - 330);

		// Bild auf Leinwand übertragen
		redraw();
	}

}
