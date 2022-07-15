package schule.ngb.zm.anim;

import java.util.function.DoubleUnaryOperator;

/**
 * @see <a href="https://easings.net/de">Cheat Sheet für Easing-Funktionen</a>
 */
public class Easing {

	public static final DoubleUnaryOperator DEFAULT_EASING = Easing::smooth;

	public static final DoubleUnaryOperator thereAndBack() {
		return Easing::thereAndBack;
	}

	public static final DoubleUnaryOperator thereAndBack( final DoubleUnaryOperator baseEasing ) {
		return ( t ) -> thereAndBack(t, baseEasing);
	}

	public static final double thereAndBack( double t ) {
		return thereAndBack(t, DEFAULT_EASING);
	}

	public static final double thereAndBack( double t, DoubleUnaryOperator baseEasing ) {
		if( t < 0.5 ) {
			return baseEasing.applyAsDouble(2 * t);
		} else {
			return baseEasing.applyAsDouble(2 - 2 * t);
		}
	}

	public static final DoubleUnaryOperator halfAndHalf( final DoubleUnaryOperator firstEasing, final DoubleUnaryOperator secondEasing ) {
		return ( t ) -> halfAndHalf(t, firstEasing, secondEasing);
	}

	public static final DoubleUnaryOperator halfAndHalf( final DoubleUnaryOperator firstEasing, final DoubleUnaryOperator secondEasing, final double split ) {
		return ( t ) -> halfAndHalf(t, firstEasing, secondEasing, split);
	}

	public static final double halfAndHalf( double t, DoubleUnaryOperator firstEasing, DoubleUnaryOperator secondEasing ) {
		return halfAndHalf(t, firstEasing, secondEasing, 0.5);
	}

	public static final double halfAndHalf( double t, DoubleUnaryOperator firstEasing, DoubleUnaryOperator secondEasing, double split ) {
		if( t < split ) {
			return firstEasing.applyAsDouble(2 * t);
		} else {
			return secondEasing.applyAsDouble(1 - 2 * t);
		}
	}


	/*
	 * Functions taken from easings.net
	 */

	public static final DoubleUnaryOperator linear() {
		return Easing::linear;
	}

	public static final double linear( double t ) {
		return t;
	}

	public static final DoubleUnaryOperator quadIn() {
		return Easing::quadIn;
	}

	public static final double quadIn( double t ) {
		return t * t;
	}

	public static final DoubleUnaryOperator quadOut() {
		return Easing::quadOut;
	}

	public static final double quadOut( double t ) {
		return 1 - (1 - t) * (1 - t);
	}

	public static final DoubleUnaryOperator quadInOut() {
		return Easing::quadInOut;
	}

	public static final double quadInOut( double t ) {
		return t < 0.5 ? 2 * t * t : 1 - Math.pow(-2 * t + 2, 2) / 2;
	}

	public static final DoubleUnaryOperator cubicIn() {
		return Easing::cubicIn;
	}

	public static final double cubicIn( double t ) {
		return t * t * t;
	}

	public static final DoubleUnaryOperator cubicOut() {
		return Easing::cubicOut;
	}

	public static final double cubicOut( double t ) {
		return 1 - Math.pow(1 - t, 3);
	}

	public static final DoubleUnaryOperator cubicInOut() {
		return Easing::cubicInOut;
	}

	public static final double cubicInOut( double t ) {
		return t < 0.5 ? 4 * t * t * t : 1 - Math.pow(-2 * t + 2, 3) / 2;
	}

	public static final DoubleUnaryOperator sineIn() {
		return Easing::sineIn;
	}

	public static final double sineIn( double t ) {
		return 1 - Math.cos((t * Math.PI) / 2);
	}

	public static final DoubleUnaryOperator sineOut() {
		return Easing::sineOut;
	}

	public static final double sineOut( double t ) {
		return Math.sin((t * Math.PI) / 2);
	}

	public static final DoubleUnaryOperator sineInOut() {
		return Easing::sineInOut;
	}

	public static final double sineInOut( double t ) {
		return -(Math.cos(Math.PI * t) - 1) / 2;
	}

	public static final DoubleUnaryOperator elasticIn() {
		return Easing::elasticIn;
	}

	public static final double elasticIn( double t ) {
		double c4 = (2 * Math.PI) / 3;

		return t == 0
			? 0
			: t == 1
			? 1
			: -Math.pow(2, 10 * t - 10) * Math.sin((t * 10 - 10.75) * c4);
	}

	public static final DoubleUnaryOperator elasticOut() {
		return Easing::elasticOut;
	}

	public static final double elasticOut( double t ) {
		double c4 = (2 * Math.PI) / 3;

		return t == 0
			? 0
			: t == 1
			? 1
			: Math.pow(2, -10 * t) * Math.sin((t * 10 - 0.75) * c4) + 1;
	}

	public static final DoubleUnaryOperator elasticInOut() {
		return Easing::elasticInOut;
	}

	public static final double elasticInOut( double t ) {
		double c5 = (2 * Math.PI) / 4.5;

		return t == 0
			? 0
			: t == 1
			? 1
			: t < 0.5
			? -(Math.pow(2, 20 * t - 10) * Math.sin((20 * t - 11.125) * c5)) / 2
			: (Math.pow(2, -20 * t + 10) * Math.sin((20 * t - 11.125) * c5)) / 2 + 1;
	}

	public static final DoubleUnaryOperator bounceIn() {
		return Easing::bounceIn;
	}

	public static final double bounceIn( double t ) {
		return 1 - bounceOut(1 - t);
	}

	public static final DoubleUnaryOperator bounceOut() {
		return Easing::bounceOut;
	}

	public static final double bounceOut( double t ) {
		double n1 = 7.5625;
		double d1 = 2.75;

		if( t < 1.0 / d1 ) {
			return n1 * t * t;
		} else if( t < 2.0 / d1 ) {
			return n1 * (t -= 1.5 / d1) * t + 0.75;
		} else if( t < 2.5 / d1 ) {
			return n1 * (t -= 2.25 / d1) * t + 0.9375;
		} else {
			return n1 * (t -= 2.625 / d1) * t + 0.984375;
		}
	}

	public static final DoubleUnaryOperator bounceInOut() {
		return Easing::bounceInOut;
	}

	public static final double bounceInOut( double t ) {
		return t < 0.5
			? (1 - bounceOut(1 - 2 * t)) / 2
			: (1 + bounceOut(2 * t - 1)) / 2;
	}

	public static final DoubleUnaryOperator backIn() {
		return Easing::backIn;
	}

	public static final double backIn( double t ) {
		double c1 = 1.70158;
		double c3 = c1 + 1;

		return c3 * t * t * t - c1 * t * t;
	}

	public static final DoubleUnaryOperator backOut() {
		return Easing::backOut;
	}

	public static final double backOut( double t ) {
		double c1 = 1.70158;
		double c3 = c1 + 1;

		return 1 + c3 * Math.pow(t - 1, 3) + c1 * Math.pow(t - 1, 2);
	}

	public static final DoubleUnaryOperator backInOut() {
		return Easing::backInOut;
	}

	public static final double backInOut( double t ) {
		double c1 = 1.70158;
		double c2 = c1 * 1.525;

		return t < 0.5
			? (Math.pow(2 * t, 2) * ((c2 + 1) * 2 * t - c2)) / 2
			: (Math.pow(2 * t - 2, 2) * ((c2 + 1) * (t * 2 - 2) + c2) + 2) / 2;
	}


	/*
	 * Functions from manim community
	 */

	public static final DoubleUnaryOperator smooth() {
		return Easing::smooth;
	}

	public static final double smooth( double t ) {
		double error = sigmoid(-INFLECTION / 2.0);
		return Math.min(
			Math.max(
				(sigmoid(INFLECTION * (t - 0.5)) - error) / (1 - 2 * error),
				0
			),
			1.0
		);
	}

	public static final double rushIn( double t ) {
		return 2 * smooth(t / 2.0);
	}

	public static final double rushOut( double t ) {
		return 2 * smooth(t / 2.0 + 0.5) - 1;
	}

	public static final double doubleSmooth( double t ) {
		if( t < 0.5 )
			return 0.5 * smooth(2 * t);
		else
			return 0.5 * (1 + smooth(2 * t - 1));
	}

	public static final double hobbit( double t ) {
		double new_t = t < 0.5 ? 2 * t : 2 * (1 - t);
		return smooth(new_t);
	}


	public static final DoubleUnaryOperator wiggle() {
		return Easing::wiggle;
	}

	public static final DoubleUnaryOperator wiggle( final int wiggles ) {
		return (t) -> Easing.wiggle(t, wiggles);
	}

	public static final double wiggle( double t ) {
		return wiggle(t, 2);
	}

	public static final double wiggle( double t, int wiggles ) {
		return hobbit(t) * Math.sin(wiggles * Math.PI * t);
	}

	public static double INFLECTION = 10.0;

	public static final double sigmoid( double x ) {
		return 1.0 / (1 + Math.exp(-x));
	}


	private Easing() {
	}

}
