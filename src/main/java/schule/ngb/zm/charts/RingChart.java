package schule.ngb.zm.charts;

import schule.ngb.zm.Color;
import schule.ngb.zm.shapes.Circle;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

public class RingChart extends Circle {

	public static String DEFAULT_LABEL = "%.2f";

	public interface RingChartValue extends ChartValue {
		double getMax();
	}

	private class BasicRingChartValue extends BasicChartValue implements RingChartValue {

		public BasicRingChartValue( double maxValue, double value, String label, Color color ) {
			super(maxValue, value, label, color);
		}

		public double getMax() {
			return getX();
		}

	}


	protected RingChartValue[] values;

	public RingChart( double x, double y, double radius ) {
		this(x, y, radius, 3);
	}

	public RingChart( double x, double y, double radius, int initalSize ) {
		super(x, y, radius);
		this.values = new RingChartValue[initalSize];
	}


	public void setValue( int pRing, RingChartValue pValue ) {
		if( pRing < values.length ) {
			values[pRing] = pValue;
		} else {
			throw new ArrayIndexOutOfBoundsException("Cannot set ring " + pRing + " of " + values.length);
		}
	}

	public void setValue( int pRing, double pValue, double pMax ) {
		setValue(pRing, new BasicRingChartValue(pMax, pValue, String.format(DEFAULT_LABEL, pValue), randomNiceColor()));
	}

	public void setValue( int pRing, double pValue, double pMax, String pLabel ) {
		setValue(pRing, new BasicRingChartValue(pMax, pValue, pLabel, randomNiceColor()));
	}

	public void setValue( int pRing, double pValue, double pMax, String pLabel, Color pColor ) {
		setValue(pRing, new BasicRingChartValue(pMax, pValue, pLabel, pColor));
	}

	public void setColors( Color... pColors ) {
		for( int i = 0; i < values.length; i++ ) {
			if( i < pColors.length ) {
				values[i].setColor(pColors[i]);
			}
		}
	}

	public void setColor( int i, Color pColor ) {
		values[i].setColor(pColor);
	}

	@Override
	public void draw( Graphics2D graphics, AffineTransform transform ) {
		AffineTransform originalTransform = graphics.getTransform();
		graphics.setTransform(transform);

		double len = values.length + 1;
		double gap = radius * 0.02;

		//strokeWeight = radius/len;
		//strokeWeight = ((radius - 2 * values.length) - gap) / len;
		int outline = 1;
		strokeWeight = (radius - gap * values.length) / len;

		Stroke outlineStroke = new BasicStroke((float) strokeWeight, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		Stroke ringStroke = new BasicStroke((float) (strokeWeight - 2 * outline), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);


		int startAngle = 90;
		for( int i = 0; i < values.length; i++ ) {
			RingChartValue rcv = values[values.length - 1 - i];
			if( rcv != null ) {
				/*int ringX = (int) (-((i + 1) * strokeWeight));
				int ringY = (int) (-((i + 1) * strokeWeight) - (i * gap));
				int ringR = (int) ((i + 1) * strokeWeight * 2); */
				int ringX = (int) (i * (strokeWeight + gap));
				int ringY = (int) (i * (strokeWeight + gap));
				int ringR = (int) ((values.length - i) * (strokeWeight + gap) * 2);
				int angle = (int) round(rcv.getValue() * 360.0 / rcv.getMax());

				if( rcv.getValue() > rcv.getMax() ) {
					graphics.setStroke(outlineStroke);
					graphics.setColor(strokeColor.getJavaColor());
					graphics.drawArc(ringX, ringY, ringR, ringR, startAngle - angle + 360, -60);

					graphics.setStroke(ringStroke);
					graphics.setColor(rcv.getColor().getJavaColor());
					graphics.drawArc(ringX, ringY, ringR, ringR, startAngle - angle + 360, -60);

					graphics.setStroke(outlineStroke);
					graphics.setColor(strokeColor.getJavaColor());
					graphics.drawArc(ringX, ringY, ringR, ringR, startAngle - angle + 330, -330);

					graphics.setStroke(ringStroke);
					graphics.setColor(rcv.getColor().getJavaColor());
					graphics.drawArc(ringX, ringY, ringR, ringR, startAngle - angle + 340, -340);
				} else {
					graphics.setStroke(outlineStroke);
					graphics.setColor(strokeColor.getJavaColor());
					graphics.drawArc(ringX, ringY, ringR, ringR, startAngle, -angle);

					graphics.setStroke(ringStroke);
					graphics.setColor(rcv.getColor().getJavaColor());
					graphics.drawArc(ringX, ringY + outline, ringR, ringR, startAngle, -angle);
				}
			}
		}

		graphics.setTransform(originalTransform);
	}

}
