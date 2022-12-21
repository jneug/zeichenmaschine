package schule.ngb.zm.game;

import schule.ngb.zm.Constants;
import schule.ngb.zm.shapes.Bounds;
import schule.ngb.zm.util.Log;

import java.awt.Graphics2D;

public class TiledMap extends Map {

	private int columns, rows, tileSize;

	private Tile[][] tiles;

	public TiledMap( int columns, int rows, int tileSize ) {
		this.columns = columns;
		this.rows = rows;
		this.tileSize = tileSize;

		tiles = new Tile[columns][rows];
	}

	public void setTile( int column, int row, Tile tile ) {
		tiles[column][row] = tile;
	}

	public void render( Graphics2D g, Camera view ) {
		Bounds viewBounds = view.getBounds();

		double zoomFactor = Constants.map(view.zoom, -100, 100, -2.0, 2.0);
		zoomFactor = Constants.limit(zoomFactor, -2.0, 2.0);

		double zoomTileSize = zoomFactor * tileSize;

		int minCol, maxCol, minRow, maxRow, dX, dY;

		if( viewBounds.getMinX() < 0 ) {
			minCol = 0;
			dX = (int)(-1 * viewBounds.getMinX());
		} else {
			minCol = (int)(viewBounds.getMinX() / tileSize);
			dX = -1 * (int)(viewBounds.getMinX()) % tileSize;
		}
		maxCol = Math.min((int)(viewBounds.getMaxX() / tileSize), columns-1);

		if( viewBounds.getMinY() < 0 ) {
			minRow = 0;
			dY = (int)(-1 * viewBounds.getMinY());
		} else {
			minRow = (int)(viewBounds.getMinY() / tileSize);
			dY = -1 * (int)(viewBounds.getMinY()) % tileSize;
		}
		maxRow = Math.min((int)(viewBounds.getMaxY() / tileSize), rows-1);

		for( int r = minRow; r <= maxRow; r++ ) {
			for( int c = minCol; c <= maxCol; c++ ) {
				if( tiles[c][r] != null ) {
					tiles[c][r].render(g, dX + (c-minCol) * tileSize, dY + (r-minRow) * tileSize, tileSize);
				}
			}
		}
	}

	private static final Log LOG = Log.getLogger(TiledMap.class);
}
