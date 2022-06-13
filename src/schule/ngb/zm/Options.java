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
		CENTER(0),
		NORTH(1 << 0),
		EAST(1 << 1),
		SOUTH(1 << 2),
		WEST(1 << 3),

		NORTHEAST(NORTH.mask | EAST.mask),
		SOUTHEAST(SOUTH.mask | EAST.mask),
		NORTHWEST(NORTH.mask | WEST.mask),
		SOUTHWEST(SOUTH.mask | WEST.mask),

		MIDDLE(CENTER.mask),
		UP(NORTH.mask),
		DOWN(SOUTH.mask),
		LEFT(WEST.mask),
		RIGHT(EAST.mask);

		public final byte mask;

		Direction( int mask ) {
			this.mask = (byte) mask;
		}

		public boolean in( int mask ) {
			return (mask & this.mask) == this.mask;
		}

		public boolean in( Direction dir ) {
			return in(dir.mask);
		}

		public boolean contains( int mask ) {
			return (mask & this.mask) == mask;
		}

		public boolean contains( Direction dir ) {
			return contains(dir.mask);
		}
	}

}
