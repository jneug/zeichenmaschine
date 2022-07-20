package schule.ngb.zm.shapes.charts;

import schule.ngb.zm.shapes.Rectangle;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;

public class LineChart extends Rectangle {

	protected SortedMap<Double, ChartValue> values;

	protected boolean drawLines = true;

	public LineChart( double x, double y, double width, double height ) {
		this(x, y, width, height, null, null);
	}

	public LineChart( double x, double y, double width, double height, double[] pCoordinates, double[] pValues ) {
		super(x, y, width, height);

		this.values = new TreeMap<>();
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
		values.put(pCoordinate, new BasicChartValue(pCoordinate, pValue));
	}

	public void removeValue( double x ) {
		values.remove(x);
	}

	public double getValue( double x ) {
		ChartValue value = values.get(x);
		if( value == null )
			throw new NoSuchElementException(String.format("No chart value for x-value <%f> found.", x));
		return value.getValue();
	}

	public void setValue( double x, double pValue ) {
		ChartValue value = values.get(x);
		if( value != null ) {
			value.setValue(pValue);
		} else {
			this.addValue(x, pValue);
		}
	}

	@Override
	public void draw( Graphics2D graphics, AffineTransform transform ) {
		double xMax = values.lastKey();
		double yMax = Double.MIN_VALUE;

		Collection<ChartValue> val = values.values();
		for( ChartValue lcv : val ) {
			if( lcv.getValue() > yMax ) {
				yMax = lcv.getValue();
			}
		}

		double xUnit = width/xMax;
		double yUnit = height/yMax;

		AffineTransform originalTransform = graphics.getTransform();
		graphics.setTransform(transform);

		drawAxes(graphics, xUnit, yUnit);

		ChartValue lastLcv = null;
		for( ChartValue lcv : val ) {
			if( drawLines && lastLcv != null ) {
				graphics.setColor(getStrokeColor().getJavaColor());
				graphics.setStroke(createStroke());
				graphics.drawLine((int)(lastLcv.getX()*xUnit), (int)(height - lastLcv.getValue()*yUnit), (int)(lcv.getX()*xUnit), (int)(height - lcv.getValue()*yUnit));
				drawDot(graphics, lastLcv, xUnit, yUnit);
			}

			drawDot(graphics, lcv, xUnit, yUnit);
			lastLcv = lcv;
		}

		graphics.setTransform(originalTransform);
	}

	private void drawDot( Graphics2D graphics, ChartValue lcv, double xUnit, double yUnit ) {
		int dotSize = (int) round(strokeWeight * 2);
		graphics.setColor(getFillColor().getJavaColor());
		graphics.fillRect((int)(lcv.getX()*xUnit - dotSize), (int)(height - lcv.getValue()*yUnit - dotSize), dotSize+dotSize, dotSize+dotSize);
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
