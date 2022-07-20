package schule.ngb.zm.anim;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import schule.ngb.zm.Color;
import schule.ngb.zm.Constants;
import schule.ngb.zm.Options;
import schule.ngb.zm.Zeichenmaschine;
import schule.ngb.zm.layers.ShapesLayer;
import schule.ngb.zm.shapes.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.DoubleUnaryOperator;

import static org.junit.jupiter.api.Assertions.*;

class AnimationsTest {

	private static Zeichenmaschine zm;

	private static ShapesLayer shapes;

	@BeforeAll
	static void beforeAll() {
		zm = new Zeichenmaschine(400, 400, "zm-test: Animations", false);
		shapes = zm.getShapesLayer();
		assertNotNull(shapes);
	}

	@AfterAll
	static void afterAll() {
		zm.exit();
	}

	@BeforeEach
	void setUp() {
		shapes.removeAll();
	}

	@Test
	void animateMove() {
		Shape s = new Circle(0, 0, 10);
		shapes.add(s);

		_animateMove(s, 2500, Easing.DEFAULT_EASING);
		assertEquals(zm.getWidth(), s.getX(), 0.0001);
		assertEquals(zm.getHeight(), s.getY(), 0.0001);

		_animateMove(s, 2500, Easing.thereAndBack(Easing.linear()));
		assertEquals(0.0, s.getX(), 0.0001);
		assertEquals(0.0, s.getY(), 0.0001);

		_animateMove(s, 4000, Easing::bounceInOut);
		assertEquals(zm.getWidth(), s.getX(), 0.0001);
		assertEquals(zm.getHeight(), s.getY(), 0.0001);
	}

	private void _animateMove( Shape s, int runtime, DoubleUnaryOperator easing ) {
		s.moveTo(0, 0);
		Future<Shape> future = Animations.animate(
			s, runtime,
			easing,
			( e ) -> Constants.interpolate(0, zm.getWidth(), e),
			( t, p ) -> {
				t.moveTo(p, p);
			}
		);
		assertNotNull(future);
		try {
			assertEquals(s, future.get());
		} catch( Exception e ) {
			fail(e);
		}
	}

	@Test
	void animateCircle() {
		Shape s = new Circle(0, 0, 10);
		shapes.add(s);

		_animateCircle(s, 5000, Easing.linear());
	}

	private void _animateCircle( Shape s, final int runtime, final DoubleUnaryOperator easing ) {
		final int midX = (int) (zm.getWidth() * .5);
		final int midY = (int) (zm.getHeight() * .5);
		final int radius = (int) (zm.getWidth() * .25);

		Animator<Shape, Double> ani = new Animator<Shape, Double>() {
			@Override
			public double easing( double t ) {
				return easing.applyAsDouble(t);
			}

			@Override
			public Double interpolator( double e ) {
				return Constants.interpolate(0, 360, e);
			}

			@Override
			public void applicator( Shape s, Double angle ) {
				double rad = Math.toRadians(angle);
				s.moveTo(midX + radius * Math.cos(rad), midY + radius * Math.sin(rad));
			}
		};

		Future<Shape> future = Animations.animate(s, runtime, ani);
		assertNotNull(future);
		try {
			assertEquals(s, future.get());
		} catch( Exception e ) {
			fail(e);
		}
	}

	@Test
	void animateRotate() {
		Shape s = new Rectangle(0, 0, 129, 80);
		s.setAnchor(Constants.CENTER);
		shapes.add(s);

		_animateRotate(s, 3000, Easing::cubicIn);
		assertEquals(zm.getWidth() * 0.5, s.getX(), 0.0001);
		assertEquals(zm.getHeight() * 0.5, s.getY(), 0.0001);
		assertEquals(0.0, s.getRotation(), 0.0001);

		_animateRotate(s, 500, Easing::elasticInOut);
		assertEquals(zm.getWidth() * 0.5, s.getX(), 0.0001);
		assertEquals(zm.getHeight() * 0.5, s.getY(), 0.0001);
		assertEquals(0.0, s.getRotation(), 0.0001);

		_animateRotate(s, 1000, Easing::bounceOut);
		assertEquals(zm.getWidth() * 0.5, s.getX(), 0.0001);
		assertEquals(zm.getHeight() * 0.5, s.getY(), 0.0001);
		assertEquals(0.0, s.getRotation(), 0.0001);

		_animateRotate(s, 6000, Easing::backInOut);
		assertEquals(zm.getWidth() * 0.5, s.getX(), 0.0001);
		assertEquals(zm.getHeight() * 0.5, s.getY(), 0.0001);
		assertEquals(0.0, s.getRotation(), 0.0001);
	}

	private void _animateRotate( Shape s, int runtime, DoubleUnaryOperator easing ) {
		s.moveTo(zm.getWidth() * .5, zm.getHeight() * .5);
		s.rotateTo(0);
		Future<Shape> future = Animations.animate(
			s, runtime,
			easing,
			( e ) -> s.rotateTo(Constants.interpolate(0, 720, e))
		);
		assertNotNull(future);
		try {
			assertEquals(s, future.get());
		} catch( Exception e ) {
			fail(e);
		}
	}

	@Test
	void animateColor() {
		Shape s = new Ellipse(0, 0, 129, 80);
		s.setAnchor(Constants.CENTER);
		shapes.add(s);

		_animateColor(s, Color.RED, 1000, Easing.DEFAULT_EASING);
		assertEquals(Color.RED, s.getFillColor());
		_animateColor(s, Color.BLUE, 1500, Easing::backInOut);
		assertEquals(Color.BLUE, s.getFillColor());
		_animateColor(s, Color.GREEN, 2000, Easing::bounceOut);
		assertEquals(Color.GREEN, s.getFillColor());
		_animateColor(s, Color.YELLOW, 300, Easing::thereAndBack);
		assertEquals(Color.GREEN, s.getFillColor());
	}

	private void _animateColor( Shape s, Color to, int runtime, DoubleUnaryOperator easing ) {
		s.moveTo(zm.getWidth() * .5, zm.getHeight() * .5);
		final Color from = s.getFillColor();
		Future<Shape> future = Animations.animate(
			s, runtime,
			easing,
			( e ) -> Color.interpolate(from, to, e),
			( t, c ) -> t.setFillColor(c)
		);
		assertNotNull(future);
		try {
			assertEquals(s, future.get());
		} catch( Exception e ) {
			fail(e);
		}
	}


	@Test
	void animatePropertyColor() {
		Shape s = new Ellipse(0, 0, 129, 80);
		s.setAnchor(Constants.CENTER);
		shapes.add(s);

		_animatePropertyColor(s, Color.RED, 1000, Easing.DEFAULT_EASING);
		assertEquals(Color.RED, s.getFillColor());
		_animatePropertyColor(s, Color.BLUE, 1500, Easing::backInOut);
		assertEquals(Color.BLUE, s.getFillColor());
		_animatePropertyColor(s, Color.GREEN, 2000, Easing::bounceOut);
		assertEquals(Color.GREEN, s.getFillColor());
		_animatePropertyColor(s, Color.YELLOW, 300, Easing::thereAndBack);
		assertEquals(Color.GREEN, s.getFillColor());
	}

	private void _animatePropertyColor( Shape s, Color to, int runtime, DoubleUnaryOperator easing ) {
		s.moveTo(zm.getWidth() * .5, zm.getHeight() * .5);
		final Color from = s.getFillColor();
		Future<Shape> future = Animations.animateProperty(
			s, from, to, runtime, easing, s::setFillColor
		);
		assertNotNull(future);
		try {
			assertEquals(s, future.get());
		} catch( Exception e ) {
			fail(e);
		}
	}

	@Test
	void animatePropertyReflect() {
		Shape s = new Ellipse(0, 200, 129, 80);
		shapes.add(s);

		try {
			Animations.animateProperty("x", s, 400, 1000, Easing.DEFAULT_EASING);
			Animations.animateProperty("strokeColor", s, Color.RED, 1000, Easing.DEFAULT_EASING).get();
		} catch( InterruptedException | ExecutionException e ) {
			fail(e);
		}
	}

	@Test
	void animateManim() {
		Shape s = new Circle(0, 0, 10);
		shapes.add(s);
		Text t = new Text(0, 0, "Easing");
		t.setAnchor(Options.Direction.EAST);
		t.alignTo(Options.Direction.NORTHEAST, -20.0);
		shapes.add(t);

		t.setText("rushIn");
		_animateMove(s, 2500, Easing::rushIn);
		t.setText("rushOut");
		_animateMove(s, 2500, Easing::rushOut);
		t.setText("hobbit");
		_animateMove(s, 2500, Easing::hobbit);
		t.setText("wiggle(2)");
		_animateMove(s, 2500, Easing::wiggle);
		t.setText("wiggle(4)");
		_animateMove(s, 2500, Easing.wiggle(4));
		t.setText("doubleSmooth");
		_animateMove(s, 2500, Easing::doubleSmooth);
	}

}
