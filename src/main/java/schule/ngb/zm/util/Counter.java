package schule.ngb.zm.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings( "unused" )
public final class Counter {

	public static Counter fromArray( double[] values ) {
		return new Counter().countAll(values);
	}

	public static Counter fromArray( Number[] values ) {
		return new Counter().countAll(values);
	}

	public static Counter fromList( List<Number> values ) {
		return new Counter().countAll(values);
	}

	private AtomicInteger count = new AtomicInteger(0);

	private double min = Double.NaN, max = Double.NaN, sum = Double.NaN;

	public void Counter() {

	}

	public void Counter( int initial ) {
		count.set(initial);
	}

	public int getCount() {
		return count.get();
	}

	public double getMax() {
		synchronized( count ) {
			return max;
		}
	}

	public double getMin() {
		synchronized( count ) {
			return min;
		}
	}

	public double getSum() {
		synchronized( count ) {
			return sum;
		}
	}

	public double getAvg() {
		if( Double.isNaN(sum) ) {
			return Double.NaN;
		} else {
			return sum / count.doubleValue();
		}
	}

	public Counter setCount( int count ) {
		this.count.set(count);
		return this;
	}

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

	@SuppressWarnings( "UnusedReturnValue" )
	public Counter inc() {
		this.count.incrementAndGet();
		return this;
	}

	@SuppressWarnings( "UnusedReturnValue" )
	public Counter dec() {
		this.count.decrementAndGet();
		return this;
	}

	@SuppressWarnings( "UnusedReturnValue" )
	public Counter count( double value ) {
		inc();
		// Update stats
		synchronized( count ) {
			sum = Double.isNaN(sum) ? value : sum  + value;
			if( Double.isNaN(max) || max < value )
				max = value;
			if( Double.isNaN(min) ||min > value )
				min = value;
		}
		return this;
	}

	@SuppressWarnings( "UnusedReturnValue" )
	public Counter count( Number num ) {
		return count(num.doubleValue());
	}

	@SuppressWarnings( "UnusedReturnValue" )
	public Counter count( Object obj ) {
		if( obj instanceof Number ) {
			return count((Number) obj);
		} else {
			inc();
			return this;
		}
	}

	@SuppressWarnings( "UnusedReturnValue" )
	public synchronized Counter countAll( double[] values ) {
		for( double value: values ) {
			count(value);
		}
		return this;
	}

	@SuppressWarnings( "UnusedReturnValue" )
	public synchronized Counter countAll( Number[] values ) {
		for( Number value: values ) {
			count(value);
		}
		return this;
	}

	@SuppressWarnings( "UnusedReturnValue" )
	public synchronized Counter countAll( Collection<Number> values ) {
		for( Number value: values ) {
			count(value);
		}
		return this;
	}

}
