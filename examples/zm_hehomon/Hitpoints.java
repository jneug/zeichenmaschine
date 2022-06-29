import org.w3c.dom.css.Rect;
import schule.ngb.zm.Color;
import schule.ngb.zm.Updatable;
import schule.ngb.zm.shapes.Rectangle;
import schule.ngb.zm.shapes.ShapeGroup;

public class Hitpoints extends ShapeGroup implements Updatable {

	private Hehomon hehomon;

	private Rectangle rectMax, rectCurrent;

	public Hitpoints(Hehomon pHehomon ) {
		super();

		hehomon = pHehomon;

		rectMax = new Rectangle(0, 0,206,11);
		rectMax.setFillColor(0);
		rectMax.noStroke();
		rectCurrent = new Rectangle(3,3,200,5);
		rectCurrent.setFillColor(255);
		rectCurrent.noStroke();

		update(0);

		add(rectMax);
		add(rectCurrent);
		nextTo(pHehomon, DOWN);
	}

	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public void update( double delta ) {
		double lpAnteil = ((double)hehomon.getLp()/hehomon.getLpMax());

		rectCurrent.setWidth( morph(0.0, rectMax.getWidth()-6.0, lpAnteil) );
		rectCurrent.setFillColor( Color.interpolate(RED, GREEN, lpAnteil) );
	}

}
