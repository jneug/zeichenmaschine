package schule.ngb.zm;

import java.awt.BasicStroke;
import java.awt.geom.Arc2D;

/**
 * Diese Klasse sammelt Enumerationen, die verschiedene Eigenschaften der zu
 * zeichnenden Formen darstellen.
 */
public final class Options {

	private Options() {
	}

	/**
	 * Linienstile für Konturlinien.
	 */
	public enum StrokeType {
		/**
		 * Durchgezogene Linien.
		 */
		SOLID,

		/**
		 * Getrichelte Linien.
		 */
		DASHED,

		/**
		 * Gepunktete Linien.
		 */
		DOTTED
	}

	/**
	 * Linienstile für Konturlinien.
	 */
	public enum StrokeJoin {

		/**
		 * Abgerundete Verbindungen.
		 */
		ROUND(BasicStroke.JOIN_ROUND),

		/**
		 * Abgeschnittene Verbindungen.
		 */
		BEVEL(BasicStroke.JOIN_BEVEL),

		/**
		 * Eckige Verbindungen.
		 */
		MITER(BasicStroke.JOIN_MITER);

		/**
		 * Der entsprechende Wert der Konstanten in {@link java.awt}
		 */
		public final int awt_type;

		StrokeJoin( int type ) {
			awt_type = type;
		}
	}

	/**
	 * Stile für Pfeilspitzen.
	 */
	public enum ArrowHead {
		/**
		 * Einfache Pfeilspitze aus zwei Linien.
		 */
		LINES,

		/**
		 * Gefülltes Dreieck.
		 */
		FILLED
	}

	/**
	 * Arten von Bögen.
	 * <p>
	 * Die Werte legen fest, wie Bögen geschlossen werden sollen, wenn sie
	 * beispielsweise gefüllt werden.
	 * <p>
	 * Wrapper für die AWT-Konstanten in {@link Arc2D}.
	 */
	public enum PathType {
		/**
		 * Offener Pfad, bei dem die Pfadenden direkt miteinander verbunden
		 * werden ohne eine Linie zu ziehen.
		 */
		OPEN(Arc2D.OPEN),

		/**
		 * Geschlossener Pfad, bei dem die Pfadenden direkt miteinander
		 * verbunden werden, indem eine Linie gezogen wird.
		 */
		CLOSED(Arc2D.CHORD),

		/**
		 * Geschlossener Pfad, bei dem Linien von den Pfadenden zum Mittelpunkt
		 * des Kreises, der den Kreisbogen festlegt, gezogen werden.
		 */
		PIE(Arc2D.PIE);

		/**
		 * Der entsprechende Wert der Konstanten in {@link Arc2D}
		 */
		public final int awt_type;

		PathType( int type ) {
			awt_type = type;
		}
	}

	/**
	 * Zustände in denen sich die Zeichenmaschine befinden kann.
	 */
	public enum AppState {
		/**
		 * Die Zeichenmaschine befindet sich in der Initialisierung. Die
		 * Laufzeitumgebung wird konfiguriert und alle nötigen Komonenten
		 * ({@link Zeichenfenster}, {@link Zeichenleinwand}, ...) werden
		 * erstellt.
		 */
		INITIALIZING,

		/**
		 * Die Initialisierung der Zeichenmaschine ist beendet, aber der
		 * {@link schule.ngb.zm.Zeichenmaschine.Zeichenthread Zeichenthread}
		 * wurde noch nicht gestartet.
		 */
		INITIALIZED,

		/**
		 * Die Zeichenmaschine wurde gestartet und der
		 * {@link schule.ngb.zm.Zeichenmaschine.Zeichenthread Zeichenthread}
		 * arbeitet.
		 */
		RUNNING,

		/**
		 * Der {@link schule.ngb.zm.Zeichenmaschine.Zeichenthread Zeichenthread}
		 * wurde pausiert.
		 */
		PAUSED,

		/**
		 * Der {@link schule.ngb.zm.Zeichenmaschine.Zeichenthread Zeichenthread}
		 * wurde gestoppt, die Zeichenmaschine ist aber noch nicht vollständig
		 * heruntergefahren und hat noch nicht alle Ressourcen freigegeben.
		 */
		STOPPED,

		/**
		 * Der {@link schule.ngb.zm.Zeichenmaschine.Zeichenthread Zeichenthread}
		 * ist beendet.
		 */
		TERMINATED,

		/**
		 * Die Zeichenmaschine ist dabei, vollständig herunterzufahren und alle
		 * Ressourcen freizugeben.
		 */
		QUITING,

		/**
		 * Der {@link schule.ngb.zm.Zeichenmaschine.Zeichenthread Zeichenthread}
		 * wartet gerade auf den nächsten Frame.
		 */
		IDLE,

		/**
		 * Die Zeichenmaschine führt gerade
		 * {@link Zeichenmaschine#update(double)} aus.
		 */
		UPDATING,

		/**
		 * Die Zeichenmaschine führt gerade {@link Zeichenmaschine#draw()} aus.
		 */
		DRAWING,

		/**
		 * Die Ausführung der Zeichenmaschine wurde mit
		 * {@link Zeichenmaschine#delay(int)} verzögert und wartet auf
		 * Fortsetzung.
		 */
		DELAYED,

		/**
		 * Die Zeichenmaschine sendet gereade gesammelte Events und führt Tasks
		 * aus.
		 */
		DISPATCHING
	}

	/**
	 * Richtungen für die Ausrichtung von Formen. Richtungen sind durch
	 * Einheitsvektoren bzw. deren Kombination im Koordinatensystem der
	 * Zeichenfläche repräsentiert, wodurch mit ihnen gerechnet werden kann. Die
	 * Richtung {@link #DOWN} ist beispielsweise gleich {@code (1, 0)}.
	 * <p>
	 * Jede Richtung ist zusätzlich als Himmelsrichtung definiert. {@link #EAST}
	 * ist äquivalent zu {@link #RIGHT} als {@code (0, 1)} definiert. Auch wenn
	 * beide Werte dieselbe Richtung beschreiben sind sie nicht "gleich"
	 * ({@code EAST != RIGHT}). Um verschiedene Richtungen zuverlässig zu
	 * vergleichen, sollte daher {@link #equals(Direction)} verwendet werden.
	 * <p>
	 * Für zusammengesetzten Richtungen wie {@link #NORTHEAST} bzw
	 * {@link #UPRIGHT} lassen sich mit {@link #in(Direction)} und
	 * {@link #contains(Direction)} Beziehungen zu den anderen Richtungen
	 * prüfen. Beispielsweise ist {@code NORTHEAST.contains(NORTH)} wahr.
	 */
	public enum Direction {
		CENTER(0, 0),

		NORTH(0, -1),
		EAST(1, 0),
		SOUTH(0, 1),
		WEST(-1, 0),

		NORTHEAST(1, -1),
		SOUTHEAST(1, 1),
		SOUTHWEST(-1, 1),
		NORTHWEST(-1, -1),

		MIDDLE(CENTER),
		UP(NORTH),
		RIGHT(EAST),
		DOWN(SOUTH),
		LEFT(WEST),

		UPLEFT(NORTHWEST),
		DOWNLEFT(SOUTHWEST),
		DOWNRIGHT(SOUTHEAST),
		UPRIGHT(NORTHEAST);

		public final byte x, y;

		Direction( int x, int y ) {
			this.x = (byte) x;
			this.y = (byte) y;
		}

		Direction( Direction original ) {
			this.x = original.x;
			this.y = original.y;
		}

		/**
		 * Prüft, ob die angegebene Richtung gleich dieser ist. Dabei werden die
		 * Komponenten des Richtungsvektors geprüft. Daher sind für die Methode
		 * beispielsweise {@link #NORTH} und {@link #UP} gleich.
		 *
		 * @param dir Eine andere Richtung.
		 * @return {@code true}, wenn die Richtungen dieselben Komponenten
		 * 	haben, {@code false} sonst.
		 */
		public boolean equals( Direction dir ) {
			return this.x == dir.x && this.y == dir.y;
		}

		/**
		 * Prüft, ob diese Richtung Tile der angegebenen Richtung ist.
		 * <p>
		 * Beispielsweise ist {@link #NORTH} Teil von {@link #NORTHWEST}, aber
		 * nicht von {@link #SOUTHWEST}. Dabei wird doe Art der Richtung nicht
		 * beachtet. {@link #UP} ist daher auch Teil von {@link #NORTHWEST}.
		 *
		 * <pre>
		 * NORTH.in(NORTHWEST) // true
		 * NORTH.in(SOUTHWEST) // false
		 * UP.in(NORTHWEST) // true
		 * </pre>
		 *
		 * @param dir Eine andere Richtung.
		 * @return {@code true}, wenn diese Richtungen Teil der anderen ist,
		 *    {@code false} sonst.
		 */
		public boolean in( Direction dir ) {
			return (this.x == dir.x && this.y == 0) || (this.y == dir.y && this.x == 0) || (this.x == dir.x && this.y == dir.y);
		}

		/**
		 * Prüft, ob die angegebene Richtung Teil dieser Richtung ist.
		 * <p>
		 * Beispielsweise ist {@link #NORTH} Teil von {@link #NORTHWEST}, aber
		 * nicht von {@link #SOUTHWEST}. Dabei wird die Art der Richtung nicht
		 * beachtet. {@link #UP} ist daher auch Teil von {@link #NORTHWEST}.
		 *
		 * <pre>
		 * NORTHWEST.contains(NORTH) // true
		 * SOUTHWEST.in(NORTH) // false
		 * NORTHWEST.in(UP) // true
		 * </pre>
		 *
		 * @param dir Eine andere Richtung.
		 * @return {@code true}, wenn diese Richtungen Teil der anderen ist,
		 *    {@code false} sonst.
		 */
		public boolean contains( Direction dir ) {
			return dir.in(this);
		}

		/**
		 * @return Diese Richtung als Vektor-Objekt.
		 */
		public Vector asVector() {
			return new Vector(x, y);
		}

		/**
		 * Liefert die entgegengesetzte Richtung zu dieser.
		 * <p>
		 * Es wird die Art der Richtung berücksichtigt. Das bedeutet, das
		 * Inverse von {@link #UP} ist {@link #DOWN}, während das Inverse von
		 * {@link #NORTH} zu {@link #SOUTH} wird.
		 *
		 * @return Die entgegengesetzte Richtung zu dieser.
		 */
		public Direction inverse() {
			switch( this ) {
				case UP:
					return DOWN;
				case DOWN:
					return UP;
				case LEFT:
					return RIGHT;
				case RIGHT:
					return LEFT;
				case UPLEFT:
					return DOWNRIGHT;
				case UPRIGHT:
					return DOWNLEFT;
				case DOWNLEFT:
					return UPRIGHT;
				case DOWNRIGHT:
					return UPLEFT;
				case MIDDLE:
					return MIDDLE;
				case NORTH:
					return SOUTH;
				case SOUTH:
					return NORTH;
				case EAST:
					return WEST;
				case WEST:
					return EAST;
				case SOUTHWEST:
					return NORTHEAST;
				case SOUTHEAST:
					return NORTHWEST;
				case NORTHEAST:
					return SOUTHWEST;
				case NORTHWEST:
					return SOUTHEAST;
				default:
					return CENTER;
			}
		}
	}

}
