package schule.ngb.zm.layers;

import org.junit.jupiter.api.Test;
import schule.ngb.zm.Constants;
import schule.ngb.zm.Zeichenmaschine;

import static org.junit.jupiter.api.Assertions.*;

class DrawingLayerTest {

	@Test
	void imageRotateAndScale() {
		Zeichenmaschine zm = new Zeichenmaschine();
		zm.getDrawingLayer().imageRotateAndScale(
			"WitchCraftIcons_122_t.PNG",
			50, 100,
			90,
			300, 200,
			Constants.NORTHWEST
		);
		zm.redraw();

		try {
			Thread.sleep(4000);
		} catch( InterruptedException e ) {
			throw new RuntimeException(e);
		}
	}

}
