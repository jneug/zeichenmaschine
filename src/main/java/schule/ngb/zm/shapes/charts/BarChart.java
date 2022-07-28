package schule.ngb.zm.shapes.charts;

import schule.ngb.zm.Color;
import schule.ngb.zm.shapes.Rectangle;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class BarChart extends Rectangle {

	public static String DEFAULT_LABEL = "%.2f";

	protected ArrayList<ChartValue> values;

	protected HashMap<ChartValue, Color> colorMap;

	protected double max = -1;

	protected boolean vertical = false, inverted = false, labels = true, labelsInline = false;


	public BarChart( double x, double y, double width, double height ) {
		this(x, y, width, height, -1);
	}

	public BarChart( double x, double y, double width, double height, double max ) {
		this(x, y, width, height, max, null);
	}

	public BarChart( double x, double y, double width, double height, double max, double[] pValues ) {
		super(x, y, width, height);
		this.max = max;

		if( pValues != null ) {
			this.values = new ArrayList<>(pValues.length);
			for( double value : pValues ) {
				this.addValue(value);
			}
		} else {
			this.values = new ArrayList<>(10);
		}

		colorMap = new HashMap<>();
	}

	public boolean isVertical() {
		return vertical;
	}

	public void setVertical( boolean pPillars ) {
		this.vertical = pPillars;
	}

	public boolean isInverted() {
		return inverted;
	}

	public void setInverted( boolean pInverted ) {
		this.inverted = pInverted;
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

	public double getMax() {
		return max;
	}

	public void setMax( double pMax ) {
		this.max = pMax;
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
		addValue(pValue, pColor, String.format(DEFAULT_LABEL, pValue));
	}

	public void addValue( double pValue, Color pColor, String pLabel ) {
		addValue(new BasicChartValue(pValue, pLabel, pColor));
	}

	public void addValue( ChartValue pValue ) {
		values.add(pValue);
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
			values.remove(i);
		}
	}

	public void removeValue( ChartValue pValue ) {
		values.remove(pValue);
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
		if( vertical ) {
			drawPillars(graphics);
		} else {
			drawBars(graphics);
		}
		graphics.setTransform(originalTransform);
	}

	private void drawBars( Graphics2D graphics ) {
		double max = this.max;
		if( max == -1 ) {
			for( ChartValue cv : values ) {
				if( cv.getValue() > max ) {
					max = cv.getValue();
				}
			}
		}

		double gap = height * .01;
		double barY = gap * .5;
		double barHeight = (height - gap * values.size()) / values.size();

		for( int i = 0; i < values.size(); i++ ) {
			ChartValue cv = values.get(i);
			double barWidth = (int) round(cv.getValue() * width / max);

			if( cv.getColor() != null ) {
				graphics.setColor(cv.getColor().getJavaColor());
			} else {
				graphics.setColor(getFillColor().getJavaColor());
			}
			if( inverted ) {
				graphics.fillRect((int) (width - barWidth), (int) barY, (int) barWidth, (int) barHeight);
			} else {
				graphics.fillRect(0, (int) barY, (int) barWidth, (int) barHeight);
			}

			if( labels ) {
				int lWidth = graphics.getFontMetrics().stringWidth(cv.getLabel());
				int lHeight = graphics.getFontMetrics().getAscent();

				int lX = -10 - lWidth;
				if( inverted ) {
					lX = (int) width + 10;
				}
				if( labelsInline ) {
					//lX = (int) (width - barWidth + 10);
					lX = 10;
					if( inverted ) {
						lX = (int) (width - lWidth - 10);
					}

					if( cv.getColor() != null ) {
						graphics.setColor(cv.getColor().textcolor().getJavaColor());
					} else {
						graphics.setColor(getFillColor().textcolor().getJavaColor());
					}
				}
				int lY = (int) (barY + barHeight * .5 + lHeight * .5);

				graphics.drawString(cv.getLabel(), lX, lY);
			}

			barY += barHeight + gap;
		}
		graphics.setColor(getStrokeColor().getJavaColor());
		graphics.setStroke(getStroke());
		if( inverted ) {
			graphics.drawLine((int) width, 0, (int) width, (int) height);
		} else {
			graphics.drawLine((int) 0, (int) 0, (int) 0, (int) height);
		}
	}

	private void drawPillars( Graphics2D graphics ) {
		double max = this.max;
		if( max == -1 ) {
			for( ChartValue bcv : values ) {
				if( bcv.getValue() > max ) {
					max = bcv.getValue();
				}
			}
		}

		double gap = width * .01;
		double barX = gap * .5;
		double barWidth = (width - gap * values.size()) / values.size();

		for( int i = 0; i < values.size(); i++ ) {
			ChartValue cv = values.get(i);
			double barHeight = (int) round(cv.getValue() * height / max);

			if( cv.getColor() != null ) {
				graphics.setColor(cv.getColor().getJavaColor());
			} else {
				graphics.setColor(getFillColor().getJavaColor());
			}
			if( inverted ) {
				graphics.fillRect((int) barX, 0, (int) barWidth, (int) barHeight);
			} else {
				graphics.fillRect((int) barX, (int) (height - barHeight), (int) barWidth, (int) barHeight);
			}

			if( labels ) {
				int lWidth = graphics.getFontMetrics().stringWidth(cv.getLabel());
				int lHeight = graphics.getFontMetrics().getAscent();

				int lY = (int) (width + 10 + lWidth);
				if( inverted ) {
					lY = -10;
				}
				if( labelsInline ) {
					lY = (int) (width - 10);
					if( inverted ) {
						lY = 10 + lWidth;
					}

					if( cv.getColor() != null ) {
						graphics.setColor(cv.getColor().textcolor().getJavaColor());
					} else {
						graphics.setColor(getFillColor().textcolor().getJavaColor());
					}
				}
				int lX = (int) (barX + barWidth * .5 + lHeight * .5);

				AffineTransform tt = graphics.getTransform();
				graphics.translate(lX, lY);
				graphics.rotate(-HALF_PI);

				graphics.drawString(cv.getLabel(), 0, 0);

				graphics.setTransform(tt);
			}

			barX += barWidth + gap;
		}

		graphics.setColor(getStrokeColor().getJavaColor());
		graphics.setStroke(getStroke());
		if( inverted ) {
			graphics.drawLine(0, 0, (int) width, 0);
		} else {
			graphics.drawLine(0, (int) height, (int) width, (int) height);
		}
	}

}
