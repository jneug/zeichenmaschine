package schule.ngb.zm.util;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Statische Methoden, um Methodenparameter auf Gültigkeit zu prüfen.
 */
@SuppressWarnings( {"unused", "UnusedReturnValue"} )
public class Validator {

	public static final <T> T requireNotNull( T obj, CharSequence paramName ) {
		return requireNotNull(obj, () -> "Parameter <%s> may not be null.".format(paramName.toString()));
	}

	public static final <T> T requireNotNull( T obj, CharSequence paramName, CharSequence msg ) {
		return requireNotNull(obj, () -> msg.toString().format(paramName.toString()));
	}

	public static final <T> T requireNotNull( T obj, Supplier<String> msg ) {
		if( obj == null ) {
			throw new NullPointerException(msg == null ? "Parameter may not be null." : msg.get());
		}
		return obj;
	}

	public static final String requireNotEmpty( String str, CharSequence paramName ) {
		return requireNotEmpty(str, () -> String.format("Parameter <%s> may not be empty string (<%s> provided)", paramName, str));
	}

	public static final String requireNotEmpty( String str, CharSequence paramName, CharSequence msg ) {
		return requireNotEmpty(str, () -> msg.toString().format(paramName.toString()));
	}

	public static final String requireNotEmpty( String str, Supplier<String> msg ) {
		if( str.isEmpty() ) {
			throw new IllegalArgumentException(msg == null ? String.format("Parameter may not be empty string (<%s> provided)", str) : msg.get());
		}
		return str;
	}

	public static final <T> T[] requireNotEmpty( T[] arr, CharSequence paramName ) {
		return requireNotEmpty(arr, () -> String.format("Parameter <%s> may not be empty", paramName));
	}

	public static final <T> T[] requireNotEmpty( T[] arr, CharSequence paramName, CharSequence msg ) {
		return requireNotEmpty(arr, () -> msg.toString().format(paramName.toString()));
	}

	public static final <T> T[] requireNotEmpty( T[] arr, Supplier<String> msg ) {
		if( arr.length == 0 )
			throw new IllegalArgumentException(msg == null ? "Parameter array may not be empty" : msg.get());
		return arr;
	}



	public static final int requirePositive( int i ) {
		return requirePositive(i, (Supplier<String>) null);
	}

	public static final int requirePositive( int i, CharSequence msg ) {
		return requirePositive(i, msg::toString);
	}

	public static final int requirePositive( int i, Supplier<String> msg ) {
		if( i <= 0 )
			throw new IllegalArgumentException(msg == null ? String.format("Parameter must be positive (<%d> provided)", i) : msg.get());
		return i;
	}

	public static final int requireNotNegative( int i ) {
		return requireNotNegative(i, (Supplier<String>) null);
	}

	public static final int requireNotNegative( int i, CharSequence msg ) {
		return requireNotNegative(i, msg::toString);
	}

	public static final int requireNotNegative( int i, Supplier<String> msg ) {
		if( i < 0 )
			throw new IllegalArgumentException(msg == null ? String.format("Parameter may not be negative (<%d> provided)", i) : msg.get());
		return i;
	}

	public static final int requireInRange( int i, int min, int max ) {
		return requireInRange(i, min, max, (Supplier<String>) null);
	}

	public static final int requireInRange( int i, int min, int max, CharSequence msg ) {
		return requireInRange(i, min, max, msg::toString);
	}

	public static final int requireInRange( int i, int min, int max, Supplier<String> msg ) {
		if( i < min || i > max )
			throw new IllegalArgumentException(msg == null ? String.format("Parameter out of range <%d> to <%d> (<%d> provided)", min, max, i) : msg.get());
		return i;
	}

	public static final double requirePositive( double i ) {
		return requirePositive(i, (Supplier<String>) null);
	}

	public static final double requirePositive( double i, CharSequence msg ) {
		return requirePositive(i, msg::toString);
	}

	public static final double requirePositive( double i, Supplier<String> msg ) {
		if( i <= 0 )
			throw new IllegalArgumentException(msg == null ? String.format("Parameter must be positive (<%f> provided)", i) : msg.get());
		return i;
	}

	public static final double requireNotNegative( double i ) {
		return requireNotNegative(i, (Supplier<String>) null);
	}

	public static final double requireNotNegative( double i, CharSequence msg ) {
		return requireNotNegative(i, msg::toString);
	}

	public static final double requireNotNegative( double i, Supplier<String> msg ) {
		if( i < 0 )
			throw new IllegalArgumentException(msg == null ? String.format("Parameter may not be negative (<%f> provided)", i) : msg.get());
		return i;
	}

	public static final double requireInRange( double i, double min, double max ) {
		return requireInRange(i, min, max, (Supplier<String>) null);
	}

	public static final double requireInRange( double i, double min, double max, CharSequence msg ) {
		return requireInRange(i, min, max, msg::toString);
	}

	public static final double requireInRange( double i, double min, double max, Supplier<String> msg ) {
		if( i < min || i > max )
			throw new IllegalArgumentException(msg == null ? String.format("Parameter out of range <%f> to <%f> (<%f> provided)", min, max, i) : msg.get());
		return i;
	}

	/*
	public static final <N extends Number> N requirePositive( N num, Supplier<String> msg ) {
		if( num.doubleValue() <= 0 )
			throw new IllegalArgumentException(msg == null ? String.format("Parameter must be positive (<%s> provided)", num.toString()) : msg.get());
		return num;
	}

	public static final <N extends Number> N requireNotNegative( N num, Supplier<String> msg ) {
		if( num.doubleValue() < 0 )
			throw new IllegalArgumentException(msg == null ? String.format("Parameter may not be negative (<%s> provided)", num.toString()) : msg.get());
		return num;
	}

	public static final <N extends Number> N requireInRange( N num, N min, N max, Supplier<String> msg ) {
		if( num.doubleValue() < min.doubleValue() || num.doubleValue() > max.doubleValue() )
			throw new IllegalArgumentException(msg == null ? String.format("Parameter out of range <%s> to <%s> (<%s> provided)", min.toString(), max.toString(), num.toString()) : msg.get());
		return num;
	}
	*/

	public static final <T> T[] requireSize( T[] arr, int size ) {
		return requireSize(arr, size, (Supplier<String>) null);
	}

	public static final <T> T[] requireSize( T[] arr, int size, CharSequence msg ) {
		return requireSize(arr, size, msg::toString);
	}

	public static final <T> T[] requireSize( T[] arr, int size, Supplier<String> msg ) {
		if( arr.length != size )
			throw new IllegalArgumentException(msg == null ? String.format("Parameter array must have <%d> elements (<%d> provided)", size, arr.length) : msg.get());
		return arr;
	}

	private Validator() {
	}

}
