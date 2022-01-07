import schule.ngb.zm.Zeichenmaschine;

public class Raindrops extends Zeichenmaschine {

	/**
	 * Zähler für die gefangenen und fallen gelassenen Tropfen.
	 */
	int catched = 0, dropped = 0;

	/**
	 * Startgeschwindigkeit, in der die Tropfen fallen.
	 *
	 * Die Tropfen fallen pro Frame (also ungefähr 60-mal pro Sekunde).
	 */
    double drop_speed = 1.0;

	/**
	 * Objektvariablen für die Positionen der fünf Tropfen.
	 */
    double x1 = random(10, 390), y1 = 10;
    double x2 = random(10, 390), y2 = -60;
    double x3 = random(10, 390), y3 = -100;
    double x4 = random(10, 390), y4 = -170;
    double x5 = random(10, 390), y5 = -230;

	/**
	 * Wird zu Beginn aufgerufen, um das Spiel zu initialisieren.
	 */
    public void setup() {
        setSize(400, 600);
        setTitle("ZM: Raindrops");

		drawing.setFontSize(64);
    }

	/**
	 * Aktualisiert die Position der Tropfen und prüft, ob die Tropfen mit dem
	 * Eimer oder dem Boden kollidieren. Ist dies der Fall, wird der Tropfen
	 * wieder "nach oben" befördert und der entsprechende Zähler wird um eins
	 * erhöht. Anschließend wird geprüft, ob die Geschwindigkeit erhöht werden
	 * muss, oder ob das Spiel verloren wurde.
	 */
	public void update( double delta ) {
		// Tropfen bewegen
		y1 += drop_speed;
		y2 += drop_speed;
		y3 += drop_speed;
		y4 += drop_speed;
		y5 += drop_speed;

		// Kollision mit Eimer prüfen (Tropfen 1)
		if( x1 > mouseX-20 && x1 < mouseX+20 && y1 > 520 && y1 < 560 ) {
			x1 = random(10, 390);
			y1 = -10;
			catched += 1;

			// Nächster Level?
			if( catched > 0 && catched%10 == 0 ) {
				drop_speed += 2;
			}
		} else if( y1 > 560 ) {
			x1 = random(10, 390);
			y1 = -10;
			dropped += 1;
		}
		// Kollision mit Eimer prüfen (Tropfen 2)
		if( x2 > mouseX-20 && x2 < mouseX+20 && y2 > 520 && y2 < 560 ) {
			x2 = random(10, 390);
			y2 = -10;
			catched += 1;

			// Nächster Level?
			if( catched > 0 && catched%10 == 0 ) {
				drop_speed += 2;
			}
		} else if( y2 > 560 ) {
			x2 = random(10, 390);
			y2 = -10;
			dropped += 1;
		}
		// Kollision mit Eimer prüfen (Tropfen 3)
		if( x3 > mouseX-20 && x3 < mouseX+20 && y3 > 520 && y3 < 560 ) {
			x3 = random(10, 390);
			y3 = -10;
			catched += 1;

			// Nächster Level?
			if( catched > 0 && catched%10 == 0 ) {
				drop_speed += 2;
			}
		} else if( y3 > 560 ) {
			x3 = random(10, 390);
			y3 = -10;
			dropped += 1;
		}
		// Kollision mit Eimer prüfen (Tropfen 4)
		if( x4 > mouseX-20 && x4 < mouseX+20 && y4 > 520 && y4 < 560 ) {
			x4 = random(10, 390);
			y4 = -10;
			catched += 1;

			// Nächster Level?
			if( catched > 0 && catched%10 == 0 ) {
				drop_speed += 2;
			}
		} else if( y4 > 560 ) {
			x4 = random(10, 390);
			y4 = -10;
			dropped += 1;
		}
		// Kollision mit Eimer prüfen (Tropfen 5)
		if( x5 > mouseX-20 && x5 < mouseX+20 && y5 > 520 && y5< 560 ) {
			x5 = random(10, 390);
			y5 = -10;
			catched += 1;

			// Nächster Level?
			if( catched > 0 && catched%10 == 0 ) {
				drop_speed += 2;
			}
		} else if( y5 > 560 ) {
			x5 = random(10, 390);
			y5 = -10;
			dropped += 1;
		}

		// Game over?
		if( dropped >= 13 ) {
			stop(); // Stoppt das Spiel und ruft teardown() auf
		}
	}

	/**
	 * Zeichnet die Spielszene (hintergrund, Eimer, Tropfen, Zähler)
	 */
	public void draw() {
		// Hintergrund blaugrau
		drawing.clear(129, 174, 206);
		// Boden zeichnen
		drawing.setFillColor(0, 144, 81);
		drawing.noStroke();
		drawing.rect(0, height - 40, width, 40, NORTHWEST);

		// Eimer zeichnen
		drawing.image("bucket.png", mouseX, 540, 0.25);

		// Tropfen zeichnen
		drawing.image("raindrop.png", x1, y1, 0.1);
		drawing.image("raindrop.png", x2, y2, 0.1);
		drawing.image("raindrop.png", x3, y3, 0.1);
		drawing.image("raindrop.png", x4, y4, 0.1);
		drawing.image("raindrop.png", x5, y5, 0.1);

		// Punktezähler
		drawing.setFillColor(0);
		drawing.text(""+catched, 10, 10, NORTHWEST);
		drawing.text(""+dropped, 390, 10, NORTHEAST);
	}

	/**
	 * Wird nach dem Aufruf von {@code stop()} aufgerufen.
	 */
	public void teardown() {
		// Alles löschen
		drawing.clear(129, 174, 206);

		// Text anzeigen (Punkte und Zeit)
		drawing.setFillColor(33);
		drawing.setFontSize(64);
		drawing.text("Game Over!", 200, 300);
		drawing.setFontSize(32);
		drawing.text("Punkte: " + catched, 200, 364);
		drawing.text("Zeit: " + (runtime/1000.0), 200, 396);

		redraw();
	}

	public static void main( String[] args ) {
        new Raindrops();
    }

}
