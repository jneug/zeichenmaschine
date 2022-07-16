package schule.ngb.zm.anim;

import schule.ngb.zm.Color;
import schule.ngb.zm.Constants;
import schule.ngb.zm.Vector;
import schule.ngb.zm.tasks.FrameSynchronizedTask;
import schule.ngb.zm.tasks.FramerateLimitedTask;
import schule.ngb.zm.tasks.TaskRunner;
import schule.ngb.zm.util.Log;
import schule.ngb.zm.util.Validator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Future;
import java.util.function.*;

public class Animations {

	public static final <T> Future<T> animateProperty( String propName, T target, double to, int runtime, DoubleUnaryOperator easing ) {
		double from;
		try {
			from = callGetter(target, propName, double.class);
		} catch( InvocationTargetException | NoSuchMethodException |
				 IllegalAccessException ex ) {
			throw new RuntimeException("Can't access property getter for animation.", ex);
		}

		Method propSetter;
		try {
			propSetter = findSetter(target, propName, double.class);
		} catch( NoSuchMethodException ex ) {
			throw new RuntimeException("Can't find property setter for animation.", ex);
		}

		return animateProperty(target, from, to, runtime, easing, ( d ) -> {
			try {
				propSetter.invoke(target, d);
			} catch( IllegalAccessException | InvocationTargetException e ) {
				throw new RuntimeException("Can't access property setter for animation.", e);
			}
		});
	}

	public static final <T> Future<T> animateProperty( String propName, T target, Color to, int runtime, DoubleUnaryOperator easing ) {
		Color from;
		try {
			from = callGetter(target, propName, Color.class);
		} catch( InvocationTargetException | NoSuchMethodException |
				 IllegalAccessException ex ) {
			throw new RuntimeException("Can't access property getter for animation.", ex);
		}

		Method propSetter;
		try {
			propSetter = findSetter(target, propName, Color.class);
		} catch( NoSuchMethodException ex ) {
			throw new RuntimeException("Can't find property setter for animation.", ex);
		}

		return animateProperty(target, from, to, runtime, easing, ( d ) -> {
			try {
				propSetter.invoke(target, d);
			} catch( IllegalAccessException | InvocationTargetException e ) {
				throw new RuntimeException("Can't access property setter for animation.", e);
			}
		});
	}

	public static final <T> Future<T> animateProperty( String propName, T target, Vector to, int runtime, DoubleUnaryOperator easing ) {
		Vector from;
		try {
			from = callGetter(target, propName, Vector.class);
		} catch( InvocationTargetException | NoSuchMethodException |
				 IllegalAccessException ex ) {
			throw new RuntimeException("Can't access property getter for animation.", ex);
		}

		Method propSetter;
		try {
			propSetter = findSetter(target, propName, Vector.class);
		} catch( NoSuchMethodException ex ) {
			throw new RuntimeException("Can't find property setter for animation.", ex);
		}

		return animateProperty(target, from, to, runtime, easing, ( d ) -> {
			try {
				propSetter.invoke(target, d);
			} catch( IllegalAccessException | InvocationTargetException e ) {
				throw new RuntimeException("Can't access property setter for animation.", e);
			}
		});
	}

	private static final <T, R> R callGetter( T target, String propName, Class<R> propType ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		String getterName = makeMethodName("get", propName);
		Method getter = target.getClass().getMethod(getterName);
		if( getter != null && getter.getReturnType().equals(propType) ) {
			return (R) getter.invoke(target);
		} else {
			throw new NoSuchMethodException(String.format("No getter for property <%s> found.", propName));
		}
	}

	private static final <T, R> Method findSetter( T target, String propName, Class<R> propType ) throws NoSuchMethodException {
		String setterName = makeMethodName("set", propName);
		Method setter = target.getClass().getMethod(setterName, propType);
		if( setter != null && setter.getReturnType().equals(void.class) && setter.getParameterCount() == 1 ) {
			return setter;
		} else {
			throw new NoSuchMethodException(String.format("No setter for property <%s> found.", propName));
		}
	}

	private static final String makeMethodName( String prefix, String propName ) {
		String firstChar = propName.substring(0, 1).toUpperCase();
		String tail = "";
		if( propName.length() > 1 ) {
			tail = propName.substring(1);
		}
		return prefix + firstChar + tail;
	}

	public static final <T> Future<T> animateProperty( T target, final double from, final double to, int runtime, DoubleUnaryOperator easing, DoubleConsumer propSetter ) {
		Validator.requireNotNull(target);
		Validator.requireNotNull(propSetter);
		return animate(target, runtime, easing, ( e ) -> propSetter.accept(Constants.interpolate(from, to, e)));
	}

	public static final <T> Future<T> animateProperty( T target, final Color from, final Color to, int runtime, DoubleUnaryOperator easing, Consumer<Color> propSetter ) {
		return animate(target, runtime, easing, ( e ) -> propSetter.accept(Color.interpolate(from, to, e)));
	}


	public static final <T> Future<T> animateProperty( T target, final Vector from, final Vector to, int runtime, DoubleUnaryOperator easing, Consumer<Vector> propSetter ) {
		return animate(target, runtime, easing, ( e ) -> propSetter.accept(Vector.interpolate(from, to, e)));
	}

	public static final <T, R> Future<T> animateProperty( T target, R from, R to, int runtime, DoubleUnaryOperator easing, DoubleFunction<R> interpolator, Consumer<R> propSetter ) {
		return animate(target, runtime, easing, interpolator, ( t, r ) -> propSetter.accept(r));
	}


	public static final <T, R> Future<T> animate( T target, int runtime, DoubleUnaryOperator easing, DoubleFunction<R> interpolator, BiConsumer<T, R> applicator ) {
		return animate(target, runtime, easing, ( e ) -> applicator.accept(target, interpolator.apply(e)));
	}

	public static final <T> Future<T> animate( T target, int runtime, DoubleUnaryOperator easing, DoubleConsumer stepper ) {
		/*final long starttime = System.currentTimeMillis();
		return TaskRunner.run(() -> {
			double t = 0.0;
			do {
				// One animation step for t in [0,1]
				stepper.accept(easing.applyAsDouble(t));
				try {
					Thread.sleep(1000 / Constants.framesPerSecond);
				} catch( InterruptedException ex ) {
				}
				t = (double) (System.currentTimeMillis() - starttime) / (double) runtime;
			} while( t < 1.0 );
			stepper.accept(easing.applyAsDouble(1.0));
		}, target);*/
		return TaskRunner.run(new FramerateLimitedTask() {
			double t = 0.0;
			final long starttime = System.currentTimeMillis();
			@Override
			public void update( double delta ) {
				// One animation step for t in [0,1]
				stepper.accept(easing.applyAsDouble(t));
				t = (double) (System.currentTimeMillis() - starttime) / (double) runtime;
				running = (t <= 1.0);
			}

			@Override
			protected void finish() {
				stepper.accept(easing.applyAsDouble(1.0));
			}
		}, target);
	}

	public static final <T, R> Future<T> animate( T target, int runtime, Animator<T, R> animator ) {
		return animate(
			target, runtime,
			animator::easing,
			animator::interpolator,
			animator::applicator
		);
	}

	public static <T> Future<?> animate( Animation<T> animation ) {
		return TaskRunner.run(new FramerateLimitedTask() {
			@Override
			protected void initialize() {
				animation.start();
			}

			@Override
			public void update( double delta ) {
				animation.update(delta);
				running = animation.isActive();
			}
		}, animation);
	}

	public static <T> Future<Animation<T>> animate( Animation<T> animation, DoubleUnaryOperator easing ) {
		final AnimationFacade<T> facade = new AnimationFacade<>(animation, animation.getRuntime(), easing);
		return TaskRunner.run(new FramerateLimitedTask() {
			@Override
			protected void initialize() {
				facade.start();
			}

			@Override
			public void update( double delta ) {
				facade.update(delta);
				running = facade.isActive();
			}
		}, animation);
	}

	public static final Log LOG = Log.getLogger(Animations.class);

}
