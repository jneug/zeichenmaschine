import schule.ngb.zm.Color;
import schule.ngb.zm.Constants;
import schule.ngb.zm.Options;
import schule.ngb.zm.Zeichenmaschine;

public class GenHitomezashi extends Zeichenmaschine {

	public static final int TILE_SIZE = 20;

	public static final int COLUMNS = 30;

	public static final int ROWS = 30;

	public static final double P = 0.5;

	public static final Color[] colors = new Color[]{
		randomNiceColor(),
		randomNiceColor(),
		color(241, 124, 55),
		color(62, 156, 191)
	};

	private int[] stitchesX, stitchesY;

	public static void main( String[] args ) {
		new GenHitomezashi();
	}


	public GenHitomezashi() {
		super(TILE_SIZE*COLUMNS, TILE_SIZE*ROWS, "Hitomezashi Pattern");
	}

	@Override
	public void setup() {
		drawing.setStrokeColor(0);
		drawing.setFillColor(0);

		generatePattern();

		pause();
	}

	public void generatePattern() {
		stitchesX = new int[COLUMNS];
		for ( int i = 0; i < COLUMNS; i++ ) {
			stitchesX[i] = random(0, 1) < P ? 1 : 0;
		}
		stitchesY = new int[COLUMNS];
		for ( int j = 0; j < ROWS; j++ ) {
			stitchesY[j] = random(0, 1) < P ? 1 : 0;
		}
	}

	public boolean hasStitch( int i, int j, Options.Direction dir ) {
		switch( dir ) {
			case UP:
				return (j > 0 && (stitchesY[j-1]+i)%2 == 1);
			case DOWN:
				return (j >= 0 && (stitchesY[j]+i)%2 == 1);
			case LEFT:
				return (i > 0 && (stitchesX[i-1]+j)%2 == 1);
			case RIGHT:
				return (i >= 0 && (stitchesX[i]+j)%2 == 1);
			default:
				return false;
		}
	}

	@Override
	public void update( double delta ) {

	}

	@Override
	public void draw() {
		drawing.clear(200);

		int clr = random(1), clr2 = clr;
		drawing.noStroke();
		for ( int i = 0; i < COLUMNS; i++ ) {
			if ( hasStitch(i, 0, LEFT) ) {
				clr = (clr2+1)%2;
				clr2 = clr;
			} else {
				clr = clr2;
			}

			for ( int j = 0; j < ROWS; j++ ) {
				drawing.setFillColor(colors[clr]);
				drawing.rect(i*TILE_SIZE, j*TILE_SIZE, TILE_SIZE, TILE_SIZE, NORTHWEST);

				if ( hasStitch(i, j, DOWN) ) {
					clr = (clr+1)%2;
				}
			}
		}

		drawing.setStrokeColor(0);
		drawing.setStrokeWeight(2);
		for ( int i = 0; i < COLUMNS; i++ ) {
			for ( int j = 0; j < ROWS; j++ ) {
				boolean stitchX = (stitchesX[i]+j)%2 == 1;
				if ( i < COLUMNS-1 && stitchX ) {
					drawing.line((i+1)*TILE_SIZE, j*TILE_SIZE, (i+1)*TILE_SIZE, (j+1)*TILE_SIZE);
				}

				boolean stitchY = (stitchesY[j]+i)%2 == 1;
				if ( j < ROWS-1 && stitchY ) {
					drawing.line(i*TILE_SIZE, (j+1)*TILE_SIZE, (i+1)*TILE_SIZE, (j+1)*TILE_SIZE);
				}
			}
		}
	}

	@Override
	public void mousePressed() {
		generatePattern();
		redraw();
	}

}
