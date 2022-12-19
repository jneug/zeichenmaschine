package schule.ngb.zm.shapes.charts;

import schule.ngb.zm.Color;
import schule.ngb.zm.shapes.Circle;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class PieChart extends Circle {

	public static String DEFAULT_LABEL = "%.2f";

	protected ArrayList<ChartValue> values;

	protected HashMap<ChartValue, Color> colorMap;

	protected double sum = 0;

	protected boolean clockwise = true, labels = true, labelsInline = false;

	public PieChart( double x, double y, double radius ) {
		this(x, y, radius, null);
	}

	public PieChart( double x, double y, double radius, double[] pValues ) {
		super(x, y, radius);

		if( pValues != null ) {
			this.values = new ArrayList<>(pValues.length);

			for( double value : pValues ) {
				this.addValue(value);
				sum += value;
			}
		} else {
			this.values = new ArrayList<>(10);
		}

		colorMap = new HashMap<>();
	}

	public double getSum() {
		return sum;
	}

	public boolean isClockwise() {
		return clockwise;
	}

	public void setClockwise( boolean pClockwise ) {
		this.clockwise = pClockwise;
	}

	public boolean isLabelsVisible() {
		return labels;
	}

	public void setLabelsVisible( boolean pLabels ) {
		this.labels = pLabels;
	}

	public boolean isLabelsInline() {
		return labelsInline;
	}

	public void setLabelsInline( boolean pLabelsInline ) {
		this.labelsInline = pLabelsInline;
	}

	public ChartValue[] getChartValues() {
		ChartValue[] ret = new ChartValue[values.size()];
		return values.toArray(ret);
	}

	public double[] getValues() {
		double[] ret = new double[values.size()];
		int i = 0;
		for( ChartValue cv : values ) {
			ret[i++] = cv.getValue();
		}
		return ret;
	}

	public void addValue( double pValue ) {
		addValue(pValue, randomNiceColor());
	}

	public void addValue( double pValue, Color pColor ) {
		addValue(pValue, String.format(DEFAULT_LABEL, pValue), pColor);
	}

	public void addValue( double pValue, String pLabel, Color pColor ) {
		addValue(new BasicChartValue(pValue, pLabel, pColor));
	}

	public void addValue( ChartValue pValue ) {
		values.add(pValue);
		sum += pValue.getValue();
	}

	public void addValues( double[] pValues ) {
		for( double value : pValues ) {
			addValue(value);
		}
	}

	public void addValues( ChartValue[] pValues ) {
		for( ChartValue value : pValues ) {
			addValue(value);
		}
	}

	public void addValues( Collection<ChartValue> pValues ) {
		for( ChartValue value : pValues ) {
			addValue(value);
		}
	}

	public void removeValue( int i ) {
		if( i < values.size() ) {
			ChartValue pcv = values.remove(i);
			if( pcv != null ) {
				sum -= pcv.getValue();
			}
		}
	}

	public void removeValue( ChartValue pValue ) {
		if( values.remove(pValue) ) {
			sum -= pValue.getValue();
		}
	}

	public void removeValues( ChartValue[] pValues ) {
		for( ChartValue value : pValues ) {
			removeValue(value);
		}
	}

	public void removeValues( Collection<ChartValue> pValues ) {
		for( ChartValue value : pValues ) {
			removeValue(value);
		}
	}

	public double getValue( int i ) {
		return values.get(i).getValue();
	}

	public void setValue( int i, double pValue ) {
		values.get(i).setValue(pValue);
	}

	public boolean containsValue( ChartValue pValue ) {
		return values.contains(pValue);
	}

	public String[] getLabels() {
		String[] ret = new String[values.size()];
		int i = 0;
		for( ChartValue cv : values ) {
			ret[i++] = cv.getLabel();
		}
		return ret;
	}

	public String getLabel( int i ) {
		return values.get(i).getLabel();
	}

	public void setLabel( int i, String pLabel ) {
		values.get(i).setLabel(pLabel);
	}

	public void setLabels( String... pLabels ) {
		for( int i = 0; i < values.size(); i++ ) {
			if( i < pLabels.length ) {
				values.get(i).setLabel(pLabels[i]);
			}
		}
	}

	public Color[] getColors() {
		Color[] ret = new Color[values.size()];
		int i = 0;
		for( ChartValue bcv : values ) {
			ret[i] = bcv.getColor();
		}
		return ret;
	}

	public void setColors( Color... pColors ) {
		for( int i = 0; i < values.size(); i++ ) {
			if( i < pColors.length ) {
				values.get(i).setColor(pColors[i]);
			}
		}
	}

	public Color getColor( int i ) {
		return values.get(i).getColor();
	}

	public void setColor( int i, Color pColor ) {
		values.get(i).setColor(pColor);
	}

	@Override
	public void draw( Graphics2D graphics, AffineTransform transform ) {
		AffineTransform originalTransform = graphics.getTransform();
		graphics.setTransform(transform);

		int startAngle = 0;
		for( int i = 0; i < values.size(); i++ ) {
			ChartValue pcv;
			if( clockwise ) {
				pcv = values.get(values.size() - i - 1);
			} else {
				pcv = values.get(i);
			}
			int angle = 360 - startAngle;
			if( i < values.size() - 1 ) {
				angle = (int) round(pcv.getValue() * 360.0 / sum);
			}

			if( pcv.getColor() == null ) {
				if( !colorMap.containsKey(pcv) ) {
					colorMap.put(pcv, randomNiceColor());
				}
				graphics.setColor(colorMap.get(pcv).getJavaColor());
			} else {
				graphics.setColor(pcv.getColor().getJavaColor());
			}
			graphics.fillArc(0, 0, (int) (radius * 2), (int) (radius * 2), startAngle, angle);

			if( labels ) {
				String label = pcv.getLabel();
				if( label == null ) {
					label = String.format("%.2f", pcv.getValue());
				}

				int lWidth = graphics.getFontMetrics().stringWidth(label);
				int lHeight = graphics.getFontMetrics().getAscent();

				double heading = radians(startAngle + angle * .5);

				boolean onLeft = (heading > HALF_PI && heading < PI + HALF_PI);


				AffineTransform tt = graphics.getTransform();
				graphics.translate(radius, radius);


				if( labelsInline ) {
					if( pcv.getColor() == null ) {
						graphics.setColor(colorMap.get(pcv).textcolor().getJavaColor());
					} else {
						graphics.setColor(pcv.getColor().textcolor().getJavaColor());
					}

					if( onLeft ) {
						graphics.rotate(-heading - PI);
						graphics.translate(-radius + 10, lHeight * .5);
					} else {
						graphics.rotate(-heading);
						graphics.translate(radius - 10 - lWidth, lHeight * .5);
					}
				} else {
					double lX = (radius + 10) * Math.cos(-heading);
					double lY = (radius + 10) * Math.sin(-heading);

					if( onLeft ) {
						graphics.translate(lX - lWidth, lY + lHeight * .5);
					} else {
						graphics.translate(lX, lY + lHeight * .5);
					}
				}

				/*
				if( labelsInline ) {
					lX *= (radius - 10);
					lY *= (radius - 10);
					if( lX > 0 ) {
						lX -= lWidth;
					}
					if( lY < 0 ) {
						lY += lHeight;
					}

					if( pcv.getColor() == null ) {
						graphics.setColor(colorMap.get(pcv).textcolor().getJavaColor());
					} else {
						graphics.setColor(pcv.getColor().textcolor().getJavaColor());
					}

					graphics.translate(radius, radius);
					graphics.fillOval(0, 0, 5, 6);
					graphics.rotate(-Math.atan2(lY, lX));
				} else {
					double heading = Math.atan2(lY, lX);
					if( lX < 0 ) {
						graphics.translate(-lWidth, 0);
					}
					if( lY > 0 ) {
						graphics.translate(0, lHeight);
					}

					graphics.rotate(Math.atan2(lY, lX));
					if( lX < 0 ) {
						graphics.translate(-radius - 10, 0);
					} else {
						graphics.translate(radius + 10, 0);
					}
					graphics.fillOval(0, 0, 5, 6);
				}
				*/

				graphics.drawString(label, 0, 0);
				graphics.setTransform(tt);
			}

			startAngle += angle;
		}

		graphics.setColor(getStrokeColor().getJavaColor());
		graphics.setStroke(getStroke());
		graphics.drawOval(0, 0, (int) (radius * 2), (int) (radius * 2));

		graphics.setTransform(originalTransform);
	}

}

