package schule.ngb.zm.shapes.charts;

import schule.ngb.zm.Color;
import schule.ngb.zm.Options;
import schule.ngb.zm.shapes.Rectangle;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class ChartAxes extends Rectangle {

	private double xMin = 0.0, xMax = 10.0, xStep = 1.0;

	private double yMin = 0.0, yMax = 10.0, yStep = 1.0;

	private int xTicks = 1, yTicks = 1;

	private Color gridColor = Color.LIGHTGRAY;

	private boolean showArrows = true, showGrid = false;

	public ChartAxes( double x, double y, double width, double height ) {
		super(x, y, width, height);
		setAnchor(Options.Direction.CENTER);
	}

	public void setAxesColor( Color pColor ) {
		setStrokeColor(pColor);
	}

	@Override
	public void draw( Graphics2D graphics, AffineTransform transform ) {

		double xUnit = width / (xMax - xMin);
		double yUnit = height / (yMax - yMin);

		AffineTransform originalTransform = graphics.getTransform();
		graphics.setTransform(transform);

		if( showGrid ) {
			graphics.setColor(gridColor.getJavaColor());
			if( xTicks > 0 ) {
				for( int i = 0; i < (xMax - xMin); i += xTicks ) {
					graphics.drawLine((int) (i * xUnit), (int) (height), (int) (i * xUnit), (int) (height - (yMax - yMin) * yUnit));
				}
			}
			if( yTicks > 0 ) {
				for( int i = 0; i < (yMax - yMin); i += yTicks ) {
					graphics.drawLine(0, (int) (height - i * yUnit), (int) ((xMax - xMin) * xUnit), (int) (height - i * yUnit));
				}
			}
		}

		graphics.setColor(strokeColor.getJavaColor());
		graphics.setStroke(getStroke());
		graphics.drawLine(0, (int)(height), (int)((xMax-xMin) * xUnit), (int)(height));
		graphics.drawLine(0, (int)(height), 0, (int)(height - (yMax-yMin) * yUnit));
		if( showArrows ) {
			int tipSize = 3;
			graphics.drawLine((int)((xMax-xMin) * xUnit), (int)(height), (int)((xMax-xMin) * xUnit-tipSize), (int)(height-tipSize));
			graphics.drawLine((int)((xMax-xMin) * xUnit), (int)(height), (int)((xMax-xMin) * xUnit-tipSize), (int)(height+tipSize));
			graphics.drawLine(0, (int)(height - (yMax-yMin) * yUnit), -tipSize, (int)(height - (yMax-yMin) * yUnit +tipSize));
			graphics.drawLine(0, (int)(height - (yMax-yMin) * yUnit),  tipSize, (int)(height - (yMax-yMin) * yUnit +tipSize));
		}

		int tickSize = 3;
		if( xTicks > 0 ) {
			for( int i = 0; i < (xMax - xMin); i += xTicks ) {
				graphics.drawLine((int) (i * xUnit), (int) (height+tickSize), (int) (i * xUnit), (int) (height-tickSize));
			}
		}
		if( yTicks > 0 ) {
			for( int i = 0; i < (yMax - yMin); i += yTicks ) {
				graphics.drawLine(-tickSize, (int) (height - i * yUnit), tickSize, (int) (height - i * yUnit));
			}
		}

		graphics.setTransform(originalTransform);
	}

}
