import schule.ngb.zm.Color;
import schule.ngb.zm.Zeichenmaschine;

public class GenColors extends Zeichenmaschine {

	public static void main( String[] args ) {
		new GenColors();
	}

	public GenColors() {
		super(800, 800, "Nice Colors");
	}

	@Override
	public void setup() {
		genPattern();
	}

	public void genPattern() {
		drawing.noStroke();

		int SIZE = 40;
		for( int i = 0; i < width/SIZE; i++ ) {
			for( int j = 0; j < height/SIZE; j++ ) {
				Color c = randomNiceColor();
				float[] hsl = Color.RGBtoHSL(c.getRGBA(), null);
				System.out.printf("%f, %f, %f\n", hsl[0], hsl[1], hsl[2]);
				drawing.setFillColor(c);
				drawing.square(i*SIZE, j*SIZE, SIZE, NORTHWEST);
			}
		}
	}

	@Override
	public void mouseClicked() {
		genPattern();
		redraw();
	}

}
