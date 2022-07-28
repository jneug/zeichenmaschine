package schule.ngb.zm;

import schule.ngb.zm.shapes.*;

public class TestInput extends Zeichenmaschine {

	public static void main( String[] args ) {
		new TestInput();
	}

	private Text text;

	@Override
	public void setup() {

		//setFramesPerSecond(5);
		shapes.add(new Point(200, 200));
		shapes.add(new Point(200, 200));

		shapes.getShape(0).setFillColor(GREEN);
		shapes.getShape(1).setFillColor(PINK);

		text = new Text(10, 42, "");
		text.setFontSize(32);
		text.setAnchor(NORTHWEST);
		shapes.add(text);
	}

	@Override
	public void update( double delta ) {
		shapes.getShape(0).moveTo(mouseX, mouseY);
		shapes.getShape(1).moveTo(pmouseX, pmouseY);
	}

	@Override
	public void draw() {
		drawing.line(pmouseX, pmouseY, mouseX, mouseY);
	}

	public void keyPressed() {
		text.setText(""+key);
	}

}
