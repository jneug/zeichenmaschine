package schule.ngb.zm.anim;

@SuppressWarnings( "unused" )
public class ContinousAnimation<T> extends Animation<T> {

	private final Animation<T> baseAnimation;

	private int lag = 0;

	/**
	 * Speichert eine Approximation der aktuellen Steigung der Easing-Funktion,
	 * um im Fall {@code easeInOnly == true} nach dem ersten Durchlauf die
	 * passende Geschwindigkeit beizubehalten.
	 */
	private double m = 1.0, lastEase = 0.0;

	private boolean easeInOnly = false;

	public ContinousAnimation( Animation<T> baseAnimation ) {
		this(baseAnimation, 0, false);
	}

	public ContinousAnimation( Animation<T> baseAnimation, int lag ) {
		this(baseAnimation, lag, false);
	}

	public ContinousAnimation( Animation<T> baseAnimation, boolean easeInOnly ) {
		this(baseAnimation, 0, easeInOnly);
	}

	private ContinousAnimation( Animation<T> baseAnimation, int lag, boolean easeInOnly ) {
		super(baseAnimation.getRuntime(), baseAnimation.getEasing());
		this.baseAnimation = baseAnimation;
		this.lag = lag;
		this.easeInOnly = easeInOnly;
	}

	@Override
	public T getAnimationTarget() {
		return baseAnimation.getAnimationTarget();
	}

	@Override
	public void update( double delta ) {
		elapsedTime += (int) (delta * 1000);
		if( elapsedTime >= runtime + lag ) {
			elapsedTime %= (runtime + lag);

			if( easeInOnly && easing != null ) {
				easing = null;
				// runtime = (int)((1.0/m)*(runtime + lag));
			}
		}

		double t = (double) elapsedTime / (double) runtime;
		if( t >= 1.0 ) {
			t = 1.0;
		}
		if( easing != null ) {
			double e = easing.applyAsDouble(t);
			animate(e);
			m = (e-lastEase)/(delta*1000/(asDouble(runtime)));
			lastEase = e;
		} else {
			animate(t);
		}
	}

	@Override
	public void animate( double e ) {
		baseAnimation.animate(e);
	}

}
