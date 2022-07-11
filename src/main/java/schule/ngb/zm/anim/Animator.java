package schule.ngb.zm.anim;

public interface Animator<T, R> {

	double easing(double t);

	R interpolator(double e);

	void applicator(T target, R value);

}
