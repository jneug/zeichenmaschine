package schule.ngb.zm.shapes.charts;

import schule.ngb.zm.Color;

/**
 * Schnittstelle für Datenpunkte, die in einem Diagramm dargestellt werden
 * sollen. Durch Implementation dieser Schnittstelle kann eine Klasse in den
 * meisten Diagrammarten dargestllt werden. Einige Diagrammarten benötigen aber
 * spezialisiertere Schnittstellen, die von dieser abgeleitet werden.
 */
public interface ChartValue {

	/**
	 * Gibt den x-Wert des Datenpunktes zurück. Nicht alle Diagrammarten
	 * benötigen einen x-Wert und ignorieren diesen dann gegebenenfalls. Soll
	 * die Klasse nur in Diagrammen ohne x-Wert (zum Beispiel
	 * {@link PieChart Kreis-} oder {@link BarChart Balkendiagramm}) dargestellt
	 * werden, wird empfohlen, dass immer {@code 0} zurückgegeben wird.
	 *
	 * @return Der x-Wert des Datenpunktes.
	 */
	double getX();

	/**
	 * Gibt den Datenwert des Datenpunktes zurück.
	 * <p>
	 * Der Datenwert wird je nach Diagrammtyp anders interpretiert. In
	 * {@link LineChart}s wird er im Zusammenhang mit dem {@link #getX() x-Wert}
	 * als y-Wert eines Datenpunktes genommen. In
	 * {@link PieChart Kreisdiagrammen} wird er als Anteil des Datums im Kreis
	 * verstanden.
	 *
	 * @return Der Wert des Datenpunktes.
	 */
	double getValue();

	/**
	 * Ändert den Datenwert dieses Datenpunktes.
	 *
	 * @param pValue
	 */
	void setValue( double pValue );

	/**
	 * Gibt eine Beschriftung für den Datenpunkt zurück. Wird {@code null}
	 * zurückgegeben, dann erstellt das Diagramm automatisch eine Beschriftung,
	 * falls nötig (in der Regel der Datenwert).
	 *
	 * @return Eine Beschriftung.
	 */
	String getLabel();

	/**
	 * Ändert die Beschriftung dieses Datenpunktes.
	 *
	 * @param pLabel Die neue Beschriftung.
	 */
	void setLabel( String pLabel );

	/**
	 * Gibt eine Farbe für den Datenpunkt zurück. Wird {@code null}
	 * zurückgegeben, dann wählt das Diagramm automatisch eine Farbe für die
	 * Darstellung.
	 *
	 * @return Eine Farbe.
	 */
	Color getColor();

	/**
	 * Ändert die Farbe dieses Datenpunktes.
	 *
	 * @param pColor Die neue Farbe.
	 */
	void setColor( Color pColor );

}
