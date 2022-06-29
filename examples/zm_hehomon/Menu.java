import schule.ngb.zm.shapes.Rectangle;
import schule.ngb.zm.shapes.ShapeGroup;
import schule.ngb.zm.shapes.Text;

public class Menu extends ShapeGroup {

    private Rectangle background;

    private Text[] lines;

    public Menu( Hehomon pHehomon ) {
		super(0, 0);

		background = new Rectangle(0, 0, 400, 110);
		background.setFillColor(0, 133);
        add(background);

		lines = new Text[4];
		for( int i = 0; i < lines.length; i++ ) {
			lines[i] = new Text(5, 5+i*25, "");
			lines[i].setFontColor(WHITE);
			lines[i].setFontsize(20);
			lines[i].setAnchor(NORTHWEST);
		}
        add(lines);

		setText(0, pHehomon.getNameAngr1());
		setText(1, pHehomon.getNameAngr2());
		setText(2, pHehomon.getNameVert1());
		setText(3, pHehomon.getNameVert2());
    }

    public void setText( int pLineNumber, String pText ) {
		if( pLineNumber < lines.length && pLineNumber >= 0 ) {
			char button = new char[]{'A', 'S', 'D', 'F'}[pLineNumber];
			lines[pLineNumber].setText(button + ": " + pText);
		}
    }

}
