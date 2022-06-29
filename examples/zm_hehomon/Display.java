import schule.ngb.zm.Drawable;
import schule.ngb.zm.Options;
import schule.ngb.zm.shapes.Rectangle;
import schule.ngb.zm.shapes.ShapeGroup;
import schule.ngb.zm.shapes.Text;

public class Display extends ShapeGroup {

	private Rectangle background;

	private Text text;

	public Display( int pX, int pY ) {
		super(pX, pY);
		background = new Rectangle(0, 0, 400, 110);
		background.setFillColor(0, 133);

		text = new Text(5, 5, "Der Kampf beginnt");
		text.setFontColor(255);
		text.setFontsize(20);
		text.setAnchor(NORTHWEST);

		add(background, text);
	}

	public void setText( String pText ) {
		text.setText(pText);
	}

}
