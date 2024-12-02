package schule.ngb.zm;


import schule.ngb.zm.layers.DrawingLayer;
import schule.ngb.zm.util.Log;

public class Testmaschine extends Zeichenmaschine {

	static {
		Log.enableGlobalDebugging();
	}

	private DrawingLayer gridLayer;

	public Testmaschine() {
		this(400, 400);
	}

	public Testmaschine( int width, int height ) {
		super(width, height, "Testmaschine", false);
	}

	@Override
	public void settings() {
		gridLayer = new DrawingLayer(getWidth(), getHeight());
		this.getCanvas().addLayer(1, gridLayer);
		setGrid(50, 10);
	}

	public void setGrid( int majorGrid, int minorGrid ) {
		gridLayer.clear();

		gridLayer.clear(LIGHTGRAY);
		gridLayer.setStrokeColor(LIGHTGRAY.darker(20));
		for( int i = 0; i < getWidth(); i += minorGrid ) {
			gridLayer.line(i, 0, i, gridLayer.getHeight());
		}
		for( int i = 0; i < getHeight(); i += minorGrid ) {
			gridLayer.line(0, i, gridLayer.getWidth(), i);
		}

		gridLayer.setStrokeColor(LIGHTGRAY.darker(50));
		for( int i = 0; i < getWidth(); i += majorGrid ) {
			gridLayer.line(i, 0, i, gridLayer.getHeight());
		}
		for( int i = 0; i < getHeight(); i += majorGrid ) {
			gridLayer.line(0, i, gridLayer.getWidth(), i);
		}
	}

}
