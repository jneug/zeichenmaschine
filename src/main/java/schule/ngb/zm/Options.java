package schule.ngb.zm;

import java.awt.geom.Arc2D;

/**
 * Diese Klasse sammelt Enumerationen, die verschiedene Eigenschaften der zu
 * zeichnenden Formen darstellen.
 */
public final class Options {

	private Options() {
	}

	public enum StrokeType {
		SOLID, DASHED, DOTTED
	}

	public enum ArrowHead {
		LINES, FILLED
	}

	public enum PathType {
		OPEN(Arc2D.OPEN), CLOSED(Arc2D.CHORD), PIE(Arc2D.PIE);

		public final int awt_type;

		PathType( int type ) {
			awt_type = type;
		}
	}

	public enum AppState {
		INITIALIZING,
		INITIALIZED,
		RUNNING,
		UPDATING,
		DRAWING,
		PAUSED,
		STOPPED,
		TERMINATED
	}

	public enum Direction {
		CENTER(0, 0),
		NORTH(0, -1),
		EAST(1, 0),
		SOUTH(0, 1),
		WEST(-1, 0),

		NORTHEAST(1, -1),
		SOUTHEAST(1, 1),
		NORTHWEST(-1, -1),
		SOUTHWEST(-1, 1),

		MIDDLE(CENTER),
		UP(NORTH),
		DOWN(SOUTH),
		LEFT(WEST),
		RIGHT(EAST);

		public final byte x, y;

		Direction( int x, int y ) {
			this.x = (byte)x;
			this.y = (byte)y;
		}

		Direction( Direction original ) {
			this.x = original.x;
			this.y = original.y;
		}

		public boolean in( Direction dir ) {
			return (this.x == dir.x && this.y == 0) || (this.y == dir.y && this.x == 0) || (this.x == dir.x && this.y == dir.y);
		}

		public boolean contains( Direction dir ) {
			return dir.in(this);
		}

		public Vector asVector() {
			return new Vector(x, y);
		}

		/**
		 * Gibt die entgegengesetzte Richtung zu dieser zurück.
		 * @return
		 */
		public Direction inverse() {
			for( Direction dir: Direction.values() ) {
				if( dir.x == -this.x && dir.y == -this.y ) {
					return dir;
				}
			}
			return CENTER;
		}
	}

}
