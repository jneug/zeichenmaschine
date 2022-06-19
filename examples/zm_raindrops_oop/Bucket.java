import schule.ngb.zm.shapes.Picture;

public class Bucket extends Picture {

	public Bucket( int x, int y ) {
		super(x, y, "bucket.png");
	}

	public boolean contains( Drop pDrop ) {
		return getBounds().contains(pDrop.getX(), pDrop.getY());
	}

}
