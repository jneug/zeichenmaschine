package schule.ngb.zm.anim;

import schule.ngb.zm.Constants;
import schule.ngb.zm.Updatable;
import schule.ngb.zm.util.Validator;
import schule.ngb.zm.util.events.EventDispatcher;

import java.util.function.DoubleUnaryOperator;

public abstract class Animation<T> extends Constants implements Updatable {

	protected int runtime;

	protected int elapsedTime = 0;

	protected boolean running = false, finished = false;

	protected DoubleUnaryOperator easing;

	public Animation() {
		this.runtime = Constants.DEFAULT_ANIM_RUNTIME;
		this.easing = Constants.DEFAULT_EASING;
	}

	public Animation( DoubleUnaryOperator easing ) {
		this.runtime = Constants.DEFAULT_ANIM_RUNTIME;
		this.easing = Validator.requireNotNull(easing, "easing");
	}

	public Animation( int runtime ) {
		this.runtime = runtime;
		this.easing = Constants.DEFAULT_EASING;
	}

	public Animation( int runtime, DoubleUnaryOperator easing ) {
		this.runtime = runtime;
		this.easing = Validator.requireNotNull(easing, "easing");
	}

	public int getRuntime() {
		return runtime;
	}

	public void setRuntime( int pRuntime ) {
		this.runtime = pRuntime;
	}

	public DoubleUnaryOperator getEasing() {
		return easing;
	}

	public void setEasing( DoubleUnaryOperator pEasing ) {
		this.easing = Validator.requireNotNull(pEasing, "easing");
	}

	public abstract T getAnimationTarget();

	public final void start() {
		this.initialize();
		elapsedTime = 0;
		running = true;
		finished = false;
		animate(easing.applyAsDouble(0.0));
		dispatchEvent("start");
	}

	public final void stop() {
		running = false;
		// Make sure the last animation frame was interpolated correctly
		animate(easing.applyAsDouble((double) elapsedTime / (double) runtime));
		this.finish();
		finished = true;
		dispatchEvent("stop");
	}

	public void initialize() {
		// Intentionally left blank
	}

	public void finish() {
		// Intentionally left blank
	}

	public final void await() {
		while( !finished ) {
			Thread.yield();
		}
	}

	@Override
	public boolean isActive() {
		return running;
	}

	@Override
	public void update( double delta ) {
		elapsedTime += (int) (delta * 1000);
		if( elapsedTime > runtime )
			elapsedTime = runtime;

		double t = (double) elapsedTime / (double) runtime;
		if( t >= 1.0 ) {
			stop();
		} else {
			animate(getEasing().applyAsDouble(t));
		}
	}

	/**
	 * Setzt den Fortschritt der Animation auf den angegebenen Wert.
	 * <p>
	 * {@code e} liegt in der Regel zwischen 0 und 1. Je nach verwendeten
	 * {@link Easing} Funktion kann der Wert aber in Ausnahmefällen unter 0 oder
	 * über 1 liegen. Die {@code step()} Methode muss dem nicht Rechnung tragen
	 * und kann wenn sinnvoll den {@code e} Wert auf [0, 1] limitieren:
	 * <pre><code>
	 * e = Constants.limit(e, 0, 1);
	 * </code></pre>
	 *
	 * @param e Fortschritt der Animation, nachdem die Easing-Funktion angewandt
	 * 	wurde.
	 */
	public abstract void animate( double e );

	EventDispatcher<Animation, AnimationListener> eventDispatcher;

	private EventDispatcher<Animation, AnimationListener> initializeEventDispatcher() {
		if( eventDispatcher == null ) {
			eventDispatcher = new EventDispatcher<>();
			eventDispatcher.registerEventType("start", ( a, l ) -> l.animationStarted(a));
			eventDispatcher.registerEventType("stop", ( a, l ) -> l.animationStopped(a));
		}
		return eventDispatcher;
	}

	private void dispatchEvent( String type ) {
		if( eventDispatcher != null ) {
			eventDispatcher.dispatchEvent(type, this);
		}
	}

	public void addListener( AnimationListener listener ) {
		initializeEventDispatcher().addListener(listener);
	}

	public void removeListener( AnimationListener listener ) {
		initializeEventDispatcher().removeListener(listener);
	}

}
