package schule.ngb.zm.charts;

import schule.ngb.zm.shapes.Rectangle;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

public class LineChart extends Rectangle {

	private class LineChartValue {

		double x;

		double value;

		public LineChartValue( double x, double value ) {
			this.x = x;
			this.value = value;
		}

	}

	protected SortedMap<Double, LineChartValue> values;

	protected boolean drawLines = true;

	public LineChart( double x, double y, double width, double height ) {
		this(x, y, width, height, null, null);
	}

	public LineChart( double x, double y, double width, double height, double[] pCoordinates, double[] pValues ) {
		super(x, y, width, height);

		this.values = new TreeMap<Double, LineChartValue>();
		if( pCoordinates != null || pValues != null ) {
			if( pCoordinates == null ) {
				pCoordinates = new double[0];
			}
			if( pValues == null ) {
				pCoordinates = new double[0];
			}
			for( int i = 0; i < max(pCoordinates.length, pValues.length); i++ ) {
				if( i < pValues.length && i < pCoordinates.length ) {
					addValue(pCoordinates[i], pValues[i]);
				} else if( i < pCoordinates.length ) {
					addValue(pCoordinates[i], 0.0);
				} else {
					addValue(0.0, pValues[i]);
				}
			}
		}
	}

	public void addValue( double pCoordinate, double pValue ) {
		values.put(pCoordinate, new LineChartValue(pCoordinate, pValue));
	}

	public void removeValue( double x ) {
		values.remove(x);
	}

	public double getValue( double x ) {
		return values.get(x).value;
	}

	public void setValue( double x, double pValue ) {
		values.get(x).value = pValue;
	}

	@Override
	public void draw( Graphics2D graphics, AffineTransform transform ) {
		double xMax = values.lastKey();
		double yMax = Double.MIN_VALUE;

		Collection<LineChartValue> val = values.values();
		for( LineChartValue lcv : val ) {
			if( lcv.value > yMax ) {
				yMax = lcv.value;
			}
		}

		double xUnit = width/xMax;
		double yUnit = height/yMax;

		AffineTransform originalTransform = graphics.getTransform();
		graphics.setTransform(transform);

		drawAxes(graphics, xUnit, yUnit);

		LineChartValue lastLcv = null;
		for( LineChartValue lcv : val ) {
			if( drawLines && lastLcv != null ) {
				graphics.setColor(getStrokeColor().getJavaColor());
				graphics.setStroke(createStroke());
				graphics.drawLine((int)(lastLcv.x*xUnit), (int)(height - lastLcv.value*yUnit), (int)(lcv.x*xUnit), (int)(height - lcv.value*yUnit));
				drawDot(graphics, lastLcv, xUnit, yUnit);
			}

			drawDot(graphics, lcv, xUnit, yUnit);
			lastLcv = lcv;
		}

		graphics.setTransform(originalTransform);
	}

	private void drawDot( Graphics2D graphics, LineChartValue lcv, double xUnit, double yUnit ) {
		int dotSize = (int) round(strokeWeight * 2);
		graphics.setColor(getFillColor().getJavaColor());
		graphics.fillRect((int)(lcv.x*xUnit - dotSize), (int)(height - lcv.value*yUnit - dotSize), dotSize+dotSize, dotSize+dotSize);
	}

	private void drawAxes( Graphics2D graphics, double xUnit, double yUnit ) {
		graphics.setColor(BLACK.getJavaColor());
		graphics.setStroke(new BasicStroke());

		graphics.drawLine(0, (int)height, 0, 0);
		graphics.drawLine(0, (int)height, (int)width, (int)height);

		for( double i = xUnit; i <= width; i += xUnit ) {
			int xx = (int)i;
			graphics.drawLine(xx, (int)(height-2), xx, (int)(height+2));
		}
		for( double i = height-yUnit; i >= 0; i -= yUnit ) {
			int yy = (int)i;
			graphics.drawLine(-2, yy, 2, yy);
		}
	}

}
