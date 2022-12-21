package schule.ngb.zm.game;

import schule.ngb.zm.Zeichenmaschine;

import java.awt.event.MouseWheelEvent;

class TiledMapTest extends Zeichenmaschine {

	public static final int COLS = 40;

	public static final int ROWS = 30;

	public static final int TILE_SIZE = 40;

	private TiledMap map;

	private Camera cam;

	public TiledMapTest() {
		super(20*TILE_SIZE, 15*TILE_SIZE, "TiledMapTest");
	}
	@Override
	public void setup() {
		map = new TiledMap(COLS, ROWS, TILE_SIZE);

		for( int i = 0; i < COLS; i++ ) {
			for( int j = 0; j < ROWS; j++ ) {
				map.setTile(i, j, new Tile(i, j));
			}
		}

		cam = new Camera();
	}

	@Override
	public void update( double delta ) {

	}

	@Override
	public void draw() {
		drawing.clear();
		map.render(getDrawingLayer().getGraphics(), cam);
	}

	@Override
	public void mouseDragged() {
		cam.x -= (mouseX-pmouseX);
		cam.y -= (mouseY-pmouseY);
	}

	@Override
	public void mouseWheelMoved() {
		MouseWheelEvent mwe = (MouseWheelEvent) mouseEvent;
		cam.zoom += mwe.getWheelRotation();
	}

	public static void main( String[] args ) {
		new TiledMapTest();
	}

}
