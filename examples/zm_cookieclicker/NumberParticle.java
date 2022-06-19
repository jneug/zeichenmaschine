import schule.ngb.zm.Updatable;
import schule.ngb.zm.shapes.Text;

public class NumberParticle extends Text implements Updatable {

	double sinOffset, life = 0;

	public NumberParticle( double x, double y, int number ) {
		super(x,y,"+"+number);
		sinOffset = random(0.0, PI);
		life = 1.5;
		setStrokeColor(255);
		setFontsize(36);
	}

	@Override
	public boolean isActive() {
		return (life > 0);
	}

	@Override
	public void update( double delta ) {
		if( isActive() ) {
			double deltaX = sin(sinOffset + life);
			x += deltaX;
			y -= 100*delta;

			life -= delta;
			setStrokeColor(strokeColor, (int) interpolate(0, 255, life / 1.5));
		}
	}

}
