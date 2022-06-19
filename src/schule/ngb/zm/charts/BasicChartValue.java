package schule.ngb.zm.charts;

import schule.ngb.zm.Color;

public class BasicChartValue implements ChartValue {

	private double xValue = 0;

	private double value;

	private String label = null;

	private Color color = null;

	public BasicChartValue( double value ) {
		this.value = value;
	}

	public BasicChartValue( double xValue, double value ) {
		this.xValue = xValue;
		this.value = value;
	}

	public BasicChartValue( double value, String label ) {
		this.value = value;
		this.label = label;
	}

	public BasicChartValue( double xValue, double value, String label ) {
		this.xValue = xValue;
		this.value = value;
		this.label = label;
	}

	public BasicChartValue( double value, Color color ) {
		this.value = value;
		this.color = color;
	}

	public BasicChartValue( double xValue, double value, Color color ) {
		this.xValue = xValue;
		this.value = value;
		this.color = color;
	}

	public BasicChartValue( double value, String label, Color color ) {
		this.value = value;
		this.label = label;
		this.color = color;
	}

	public BasicChartValue( double xValue, double value, String label, Color color ) {
		this.xValue = xValue;
		this.value = value;
		this.label = label;
		this.color = color;
	}

	@Override
	public double getX() {
		return xValue;
	}

	@Override
	public double getValue() {
		return value;
	}

	@Override
	public void setValue( double pValue ) {
		this.value = pValue;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setLabel( String pLabel ) {
		this.label = pLabel;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void setColor( Color pColor ) {
		this.color = pColor;
	}

}
