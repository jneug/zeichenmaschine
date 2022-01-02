package schule.ngb.zm.shapes;

public class Rhombus extends Kite {

	public Rhombus( double x, double y, double width, double height ) {
		super(x, y, width, height, 0.5);
		setAnchor(CENTER);
	}

	public Rhombus( Rhombus rhombus ) {
		this(rhombus.x, rhombus.y, rhombus.width, rhombus.height);
		this.copyFrom(rhombus);
	}

	@Override
	public void setRatio( double ratio ) {
		// Für eine Raute ist das Verhältnis immer 50/5ß
		super.setRatio(0.5);
	}

	@Override
	public void copyFrom( Shape shape ) {
		super.copyFrom(shape);
		super.setRatio(0.5);
	}

	@Override
	public Kite copy() {
		return new Kite(this);
	}

}
