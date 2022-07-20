package schule.ngb.zm.shapes.charts;

import schule.ngb.zm.Color;

/**
 * Basisimplemenation eines {@link ChartValue}.
 * <p>
 * Die Klasse implementiert das Interface auf die einfachste mögliche Weise als
 * reine Datenhaltungsklasse. Sie wird von den Diagrammen verwendet, um reine
 * Double Werte als {@code ChartValue} zu speichern.
 * <p>
 * Um einfach einen Datenpunkt zu erstellen, wird eine umfassende Auswahl an
 * Konstruktoren angeboten.
 */
public class BasicChartValue implements ChartValue {

	/**
	 * Der x-Wert diese Datenpunktes.
	 */
	private double xValue = 0;

	/**
	 * Der Datenwert des Datenpunktes.
	 */
	private double value;

	/**
	 * Die Beschriftung des Datenpunktes.
	 */
	private String label = null;

	/**
	 * Die Farbe des Datenpunktes.
	 */
	private Color color = null;

	/**
	 * Erstellt einen neuen Datenpunkt mit dem angegebenen Datenwert.
	 *
	 * @param value Der Datenwert.
	 */
	public BasicChartValue( double value ) {
		this.value = value;
	}

	/**
	 * Erstellt einen neuen Datenpunkt mit dem angegebenen x- und Datenwert.
	 *
	 * @param xValue Der x-Wert.
	 * @param value Der Datenwert.
	 */
	public BasicChartValue( double xValue, double value ) {
		this.xValue = xValue;
		this.value = value;
	}

	/**
	 * Erstellt einen neuen Datenpunkt mit dem angegebenen Datenwert und der
	 * angegebenen Beschriftung.
	 *
	 * @param value Der Datenwert.
	 * @param label Die Beschriftung.
	 */
	public BasicChartValue( double value, String label ) {
		this.value = value;
		this.label = label;
	}

	/**
	 * Erstellt einen neuen Datenpunkt mit dem angegebenen x- und Datenwert und
	 * der angegebenen Beschriftung.
	 *
	 * @param xValue Der x-Wert.
	 * @param value Der Datenwert.
	 * @param label Die Beschriftung.
	 */
	public BasicChartValue( double xValue, double value, String label ) {
		this.xValue = xValue;
		this.value = value;
		this.label = label;
	}

	/**
	 * Erstellt einen neuen Datenpunkt mit dem angegebenen Datenwert und der
	 * angegebenen Farbe.
	 *
	 * @param value Der Datenwert.
	 * @param color Die Farbe.
	 */
	public BasicChartValue( double value, Color color ) {
		this.value = value;
		this.color = color;
	}

	/**
	 * Erstellt einen neuen Datenpunkt mit dem angegebenen x- und Datenwert und
	 * der angegebenen Farbe.
	 *
	 * @param xValue Der x-Wert.
	 * @param value Der Datenwert.
	 * @param color Die Farbe.
	 */
	public BasicChartValue( double xValue, double value, Color color ) {
		this.xValue = xValue;
		this.value = value;
		this.color = color;
	}

	/**
	 * Erstellt einen neuen Datenpunkt mit dem angegebenen Datenwert, der
	 * angegebenen Beschriftung und Farbe.
	 *
	 * @param value Der Datenwert.
	 * @param label Die Beschriftung.
	 * @param color Die Farbe.
	 */
	public BasicChartValue( double value, String label, Color color ) {
		this.value = value;
		this.label = label;
		this.color = color;
	}

	/**
	 * Erstellt einen neuen Datenpunkt mit dem angegebenen x- und Datenwert, der
	 * angegebenen Beschriftung und Farbe.
	 *
	 * @param xValue Der x-Wert.
	 * @param value Der Datenwert.
	 * @param label Die Beschriftung.
	 * @param color Die Farbe.
	 */
	public BasicChartValue( double xValue, double value, String label, Color color ) {
		this.xValue = xValue;
		this.value = value;
		this.label = label;
		this.color = color;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getX() {
		return xValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getValue() {
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue( double pValue ) {
		this.value = pValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return label;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLabel( String pLabel ) {
		this.label = pLabel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color getColor() {
		return color;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setColor( Color pColor ) {
		this.color = pColor;
	}

}
