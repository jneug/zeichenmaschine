import schule.ngb.zm.Zeichenmaschine;
import schule.ngb.zm.shapes.Text;

import java.awt.Font;

public class Raindrops extends Zeichenmaschine {

	//@formatter:off
	// Einstellungen für das Spiel

	// Breite / Höhe des Spielfensters.
	public static final int GAME_WIDTH = 400;
	public static final int GAME_HEIGHT = 600;
	// Breite des Randes (dort werden keine Tropfen erstellt).
	public static final int GAME_BORDER = 10;

	// Anzahl Tropfen zu Beginn des Spiels.
	public static final int GAME_START_DROPS = 5;
	// Maximale Anzahl Tropfen im Spiel.
	public static final int GAME_MAX_DROPS = 12;
	// Anzahl gefangenere Tropfen, ab der ein neuer Level anfängt.
	public static final int GAME_NEXT_LEVEL_AT = 10;
	// Anzahl Tropfen, die fallen gelassen werden dürfen,
	// bevor das Spiel vorbei ist.
	public static final int GAME_MAX_DROPPED = 13;
	//@formatter:on

	public static void main( String[] args ) {
		new Raindrops();
	}

	/**
	 * Array der Tropfen-Objekte.
	 */
	private Drop[] drops;

	/**
	 * Das Eimer-Objekt.
	 */
	private Bucket bucket;

	/**
	 * Anzahl Tropfen, die aktuell im Spiel sind.
	 */
	private int noOfDrops = GAME_START_DROPS;

	/**
	 * Anzahl gefangener / fallen gelassener Tropfen.
	 */
	private int dropped = 0, catched = 0;

	/**
	 * Text-Objekte für die Anzeige der gefangenen / fallen gelassenen Tropfen.
	 */
	private Text textDropped, textCatched;

	/**
	 * Initialisierung des Spiels und der benötigten Objekte.
	 */
	public void setup() {
		setSize(GAME_WIDTH, GAME_HEIGHT);
		setTitle("ZM: Raindrops");

		// Hintergrund blaugrau
		background.setColor(129, 174, 206);

		// Hintergrundbild zeichnen (DrawingLayer)
		drawing.setFillColor(0, 144, 81);
		drawing.noStroke();
		drawing.rect(0, height - 40, width, 40, NORTHWEST);

		// Eimer-Objekt erstellen
		bucket = new Bucket(width / 2, height - 60);
		bucket.scale(.25);
		shapes.add(bucket);

		// Tropfen-Objekte erstellen
		// Die maximale Anzahl Tropfen kann durch GAME_MAX_DROPS eingestellt werden
		// Es werden alle Tropfen erstellt, anber nur die ersten noOfDrops
		// Objekt im Spiel angezeigt.
		drops = new Drop[GAME_MAX_DROPS];
		for( int i = 0; i < drops.length; i++ ) {
			drops[i] = new Drop();
			drops[i].scale(.1);
			shapes.add(drops[i]);
		}

		// Die ersten noOfDrops Tropfen zu Beginn anzeigen.
		// Die Tropfen werden mit zufälliger x-Koordinate etwas versetzt
		// übereinander positioniert, damit sie nicht alle gleichzeitig fallen.
		for( int i = 0; i < noOfDrops; i++ ) {
			drops[i].moveTo(
				random(GAME_BORDER, GAME_WIDTH - GAME_BORDER),
				Drop.START_Y - i * (2 * Drop.START_Y) - random(-10, 10)
			);
			drops[i].activate();
			drops[i].show();
		}

		// Erstellen der Text-Objekte für dei Zähler.
		textCatched = new Text(10, 60, "0");
		textCatched.setFontsize(64);
		textCatched.setAnchor(NORTHWEST); //linksbündig
		shapes.add(textCatched);
		textDropped = new Text(width - 10, 60, "0");
		textDropped.setFontsize(64);
		textDropped.setAnchor(NORTHEAST); // rechtsbündig
		shapes.add(textDropped);
	}

	/**
	 * Hier passiert die Spiel-Logik. Die Position des Eimers und der Tropfen
	 * wird aktualisiert. Es wird geprüft, ob ein Tropfen mit dem Eimer
	 * kollidiert oder den Boden erreicht hat.
	 *
	 * @param delta
	 */
	public void update( double delta ) {
		// Position des Eimers auf Mausposition setzen.
		if( mouseX < 0 ) {
			// Stopp am linken Rand
			bucket.setX(0);
		} else if( mouseX > width ) {
			// Stopp am rechten Rand
			bucket.setX(width);
		} else {
			bucket.setX(mouseX);
		}

		// Die Position der Tropfen aktualisieren und Kollisionen prüfen.
		for( int i = 0; i < noOfDrops; i++ ) {
			drops[i].update(delta);

			if( drops[i].getY() >= height - 40 ) {
				// Tropfen hat Boden erreicht!
				dropped += 1;
				textDropped.setText("" + dropped);
				drops[i].reset();
			} else if( bucket.contains(drops[i]) ) {
				// Tropfen wurde gefangen!
				catched += 1;
				textCatched.setText("" + catched);
				drops[i].reset();

				// Wurde ein Tropfen gefangen (und nur dann) wird geprüft, ob
				// das nächste Level erreicht wurde.
				if( catched > 0 && (catched % GAME_NEXT_LEVEL_AT) == 0 ) {
					// Falls noch nicht alle Tropfen im Spiel, aktiviere einen
					// weiteren.
					if( noOfDrops < drops.length ) {
						noOfDrops += 1;
						drops[noOfDrops - 1].activate();
						drops[noOfDrops - 1].reset();
					}

					// Geschwindigkeit aller Tropfen erhöhen.
					for( int j = 0; j < drops.length; j++ ) {
						drops[j].increaseSpeed();
					}
				}
			}
		}

		// Prüfen, ob das Spiel verloren wurde.
		if( dropped >= GAME_MAX_DROPPED ) {
			stop(); // Stoppt den Game-Loop und ruft dann teardown() auf.
		}
	}

	/**
	 * Wird aufgerufen, nachdem das Spiel gestoppt wurde und zeigt den Game Over
	 * Bildschirm an.
	 */
	public void teardown() {
		// Alle Formen (Eimer, Tropfen, Texte) entfernen.
		shapes.removeAll();

		// "Game Over" Text
		Text gameOver = new Text(width / 2, 300, "Game Over");
		gameOver.setFontsize(64);
		shapes.add(gameOver);

		// Ergebnis anzeigen (Anzahl gefangener Tropfen, Spielzeit in Sekunden).
		Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 32);
		shapes.add(new Text(width / 2, 360, "Catched: " + catched, f));
		shapes.add(new Text(width / 2, 360 + f.getSize(), "Time: " + (runtime / 1000.0), f));

		redraw();
	}

}
