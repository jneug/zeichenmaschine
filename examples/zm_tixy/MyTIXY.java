import schule.ngb.zm.Color;
import schule.ngb.zm.Constants;

public class MyTIXY extends Constants {

	public double update( double t, int i, int x, int y ) {
		return sin(t-sqrt(pow((x-7.5),2)+pow((y-6),2)));
	}

	public void update( double t, Dot dot ) {
		if( dot.color.equals(Dot.DOT_RED) ) {
			dot.color = BLUE;
		}
	}

}
