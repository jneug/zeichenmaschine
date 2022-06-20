import org.w3c.dom.css.Rect;
import schule.ngb.zm.Color;
import schule.ngb.zm.Updatable;
import schule.ngb.zm.shapes.Rectangle;
import schule.ngb.zm.shapes.ShapeGroup;

public class Hitpoints extends ShapeGroup implements Updatable {

	private Hehomon hehomon;

	private Rectangle rectMax, rectCurrent;

	public Hitpoints( int pX, int pY, Hehomon pHehomon ) {
		hehomon = pHehomon;

		rectMax = new Rectangle(pX,pY,206,11);
		rectMax.setFillColor(0);
		rectMax.noStroke();
		rectCurrent = new Rectangle(pX+3,pY+3,200,5);
		rectCurrent.setFillColor(255);
		rectCurrent.noStroke();

		update(0);

		add(rectMax);
		add(rectCurrent);
	}

	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public void update( double delta ) {
		double lpAnteil = ((double)hehomon.getLp()/hehomon.getLpMax());

		rectCurrent.setWidth( (int)((double)(rectMax.getWidth()-6) * lpAnteil) );
		rectCurrent.setFillColor(
			new Color(
				255 - ((int)(255.0 * lpAnteil)),
				(int)(255.0 * lpAnteil),
				0
			)
		);
	}

}
