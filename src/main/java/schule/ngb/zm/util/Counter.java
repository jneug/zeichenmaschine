package schule.ngb.zm.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Eine Helferklasse, um Dinge zu zählen.
 * <p>
 * Im einfachsten Fall kann der Zähler als geteilte Zählvariable genutzt werden,
 * die mit {@link #inc()} und {@link #dec()} aus verschiedenen Objekten oder
 * Methoden verändert werden kann.
 * <p>
 * Der Zähler kann aber auch Objekte zählen, indem die Instanzen an
 * {@link #count(Object)} übergeben werden. Am Ende kann mit {@link #getCount()}
 * die Anzahl der Objekte abgerufen werden.
 * <p>
 * Handelt es sich bei den Objekten um Zahlen, dann merkt sich ein
 * {@code Counter} auch das Maximum, das Minimum, die Summe und den Durchschnitt
 * der gezählten Werte.
 * <p>
 * Ein Zähler kann auch komplette Arrays oder Listen von Zahlen zählen und die
 * obigen Statistiken auswerten.
 */
@SuppressWarnings( "unused" )
public final class Counter {

	/**
	 * Erstellt einen neuen {@code Counter}, der alle Integer im angegebenen
	 * Array gezählt hat.
	 *
	 * @param values Die zu zählenden Werte.
	 * @return Ein neuer {@code Counter}.
	 */
	public static Counter fromArray( int[] values ) {
		return new Counter().countAll(values);
	}

	/**
	 * Erstellt einen neuen {@code Counter}, der alle Doubles im angegebenen
	 * Array gezählt hat.
	 *
	 * @param values Die zu zählenden Werte.
	 * @return Ein neuer {@code Counter}.
	 */
	public static Counter fromArray( double[] values ) {
		return new Counter().countAll(values);
	}

	/**
	 * Erstellt einen neuen {@code Counter}, der alle Zahlen im angegebenen
	 * Array gezählt hat.
	 *
	 * @param values Die zu zählenden Werte.
	 * @return Ein neuer {@code Counter}.
	 */
	public static Counter fromArray( Number[] values ) {
		return new Counter().countAll(values);
	}

	/**
	 * Erstellt einen neuen {@code Counter}, der alle Zahlen in der angegebenen
	 * Liste gezählt hat.
	 *
	 * @param values Die zu zählenden Werte.
	 * @return Ein neuer {@code Counter}.
	 */
	public static Counter fromList( List<Number> values ) {
		return new Counter().countAll(values);
	}

	/**
	 * Aktuelle Anzahl gezählter Werte.
	 */
	private final AtomicInteger count = new AtomicInteger(0);

	/**
	 * Statistiken zu den gezählten Werten.
	 */
	private double min = Double.NaN, max = Double.NaN, sum = Double.NaN;

	/**
	 * Erstellt einen neuen, leeren {@code Counter}.
	 */
	public Counter() {
	}

	/**
	 * Ertstellt einen neuen {@code Counter}, der mit dem angegebenen Wert
	 * initialisiert ist.
	 *
	 * @param initial Wert des Zählers zu Beginn.
	 */
	public Counter( int initial ) {
		count.set(initial);
	}

	/**
	 * @return Die aktuelle Anzahl.
	 */
	public int getCount() {
		return count.get();
	}

	/**
	 * @return Das Maxium der bisher gezählten Werte.
	 */
	public double getMax() {
		synchronized( count ) {
			return max;
		}
	}

	/**
	 * @return Das Minimum der bisher gezählten Werte.
	 */
	public double getMin() {
		synchronized( count ) {
			return min;
		}
	}

	/**
	 * @return Die Summe der bisher gezählten Werte.
	 */
	public double getSum() {
		synchronized( count ) {
			return sum;
		}
	}

	/**
	 * @return Der Mittelwert der bisher gezählten Werte.
	 */
	public double getAvg() {
		if( Double.isNaN(sum) ) {
			return Double.NaN;
		} else {
			return sum / count.doubleValue();
		}
	}

	/**
	 * Setzt den Zähler auf den angegebenen Wert.
	 * <p>
	 * Die anderen Statistiken werden nicht verändert.
	 *
	 * @param count Der neue Wert des Zählers.
	 * @return Dieser Zähler selbst (method chaining).
	 */
	@SuppressWarnings( "UnusedReturnValue" )
	public Counter setCount( int count ) {
		this.count.set(count);
		return this;
	}

	/**
	 * Setzt den Zähler auf Null.
	 * <p>
	 * Die Statistiken werden auf {@link Double#NaN} gesetzt.
	 *
	 * @return Dieser Zähler selbst (method chaining).
	 */
	@SuppressWarnings( "UnusedReturnValue" )
	public Counter reset() {
		count.set(0);
		synchronized( count ) {
			min = Double.NaN;
			max = Double.NaN;
			sum = Double.NaN;
		}
		return this;
	}

	/**
	 * Erhöht den Zähler um Eins.
	 * <p>
	 * Die anderen Statistiken werden nicht verändert.
	 *
	 * @return Dieser Zähler selbst (method chaining).
	 */
	@SuppressWarnings( "UnusedReturnValue" )
	public Counter inc() {
		this.count.incrementAndGet();
		return this;
	}

	/**
	 * Verringert den Zähler um Eins.
	 * <p>
	 * Die anderen Statistiken werden nicht verändert.
	 *
	 * @return Dieser Zähler selbst (method chaining).
	 */
	@SuppressWarnings( "UnusedReturnValue" )
	public Counter dec() {
		this.count.decrementAndGet();
		return this;
	}

	/**
	 * Zählt den angegebenen Wert.
	 * <p>
	 * Erhöht den Zähler um Eins und aktualisiert die Statistiken.
	 *
	 * @param value Der neue Wert.
	 * @return Dieser Zähler selbst (method chaining).
	 */
	@SuppressWarnings( "UnusedReturnValue" )
	public Counter count( double value ) {
		inc();
		// Update stats
		synchronized( count ) {
			sum = Double.isNaN(sum) ? value : sum + value;
			if( Double.isNaN(max) || max < value )
				max = value;
			if( Double.isNaN(min) || min > value )
				min = value;
		}
		return this;
	}

	/**
	 * Zählt die angegebene Zahl.
	 * <p>
	 * Erhöht den Zähler um Eins und aktualisiert die Statistiken.
	 *
	 * @param num Die neue Zahl.
	 * @return Dieser Zähler selbst (method chaining).
	 */
	@SuppressWarnings( "UnusedReturnValue" )
	public Counter count( Number num ) {
		return count(num.doubleValue());
	}

	/**
	 * Zählt das angegebenen Objekt.
	 * <p>
	 * Erhöht den Zähler um Eins.
	 *
	 * @param obj Ein beliebiges Objekt.
	 * @return Dieser Zähler selbst (method chaining).
	 */
	@SuppressWarnings( "UnusedReturnValue" )
	public Counter count( Object obj ) {
		if( obj instanceof Number ) {
			return count((Number) obj);
		} else {
			inc();
			return this;
		}
	}

	/**
	 * Zöhlt alle Werte im angegebenen Array.
	 *
	 * @param values Das Array der neuen Werte.
	 * @return Dieser Zähler selbst (method chaining).
	 * @see #count(double)
	 */
	@SuppressWarnings( "UnusedReturnValue" )
	public synchronized Counter countAll( double[] values ) {
		for( double value : values ) {
			count(value);
		}
		return this;
	}

	/**
	 * Zöhlt alle Werte im angegebenen Array.
	 *
	 * @param values Das Array der neuen Werte.
	 * @return Dieser Zähler selbst (method chaining).
	 * @see #count(double)
	 */
	@SuppressWarnings( "UnusedReturnValue" )
	public synchronized Counter countAll( int[] values ) {
		for( double value : values ) {
			count(value);
		}
		return this;
	}

	/**
	 * Zöhlt alle Zahlen im angegebenen Array.
	 *
	 * @param values Das Array der neuen Zahlen.
	 * @return Dieser Zähler selbst (method chaining).
	 * @see #count(Number)
	 */
	@SuppressWarnings( "UnusedReturnValue" )
	public synchronized Counter countAll( Number[] values ) {
		for( Number value : values ) {
			count(value);
		}
		return this;
	}

	/**
	 * Zöhlt alle Zahlen in der angegebenen Sammlung.
	 *
	 * @param values Die Sammlung der neuen Zahlen.
	 * @return Dieser Zähler selbst (method chaining).
	 * @see #count(Number)
	 */
	@SuppressWarnings( "UnusedReturnValue" )
	public synchronized Counter countAll( Collection<Number> values ) {
		for( Number value : values ) {
			count(value);
		}
		return this;
	}

}
