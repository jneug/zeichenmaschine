package schule.ngb.zm.anim;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import schule.ngb.zm.Color;
import schule.ngb.zm.Testmaschine;
import schule.ngb.zm.Zeichenmaschine;
import schule.ngb.zm.layers.ShapesLayer;
import schule.ngb.zm.shapes.Circle;
import schule.ngb.zm.shapes.Shape;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AnimationGroupsTest {

	private static Testmaschine zm;

	private static ShapesLayer shapes;

	@BeforeAll
	static void beforeAll() {
		zm = new Testmaschine();
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
	void animationGroup() {
		Shape s = new Circle(0, 0, 10);
		shapes.add(s);

		Animation<Shape> anims = new AnimationGroup<>(
			500,
			Arrays.asList(
				new MoveAnimation(s, 200, 200, 2000, Easing.DEFAULT_EASING),
				new FillAnimation(s, Color.GREEN, 1000, Easing.sineIn())
			)
		);
		Animations.playAndWait(anims);
		assertEquals(200, s.getX());
		assertEquals(200, s.getY());
		assertEquals(Color.GREEN, s.getFillColor());
	}

	@Test
	void animationSequence() {
		Shape s = new Circle(0, 0, 10);
		shapes.add(s);

		Animation<Shape> anims = new AnimationSequence<>(
			Arrays.asList(
				new CircleAnimation(s, 200, 0, 90, false, 1000, Easing::rushIn),
				new CircleAnimation(s, 200, 400, 90, 1000, Easing::rushOut),
				new CircleAnimation(s, 200, 400, 90, false, 1000, Easing::rushIn),
				new CircleAnimation(s, 200, 0, 90, 1000, Easing::rushOut)
			)
		);
		Animations.playAndWait(anims);
		assertEquals(0, s.getX());
		assertEquals(0, s.getY());
	}

	@Test
	void animationSequenceContinous() {
		Shape s = new Circle(0, 0, 10);
		shapes.add(s);

		Animation<Shape> anims = new ContinousAnimation<>(new AnimationSequence<>(
			Arrays.asList(
				new CircleAnimation(s, 200, 0, 90, false, 1000, Easing::rushIn),
				new CircleAnimation(s, 200, 400, 90, 1000, Easing::rushOut),
				new CircleAnimation(s, 200, 400, 90, false, 1000, Easing::rushIn),
				new CircleAnimation(s, 200, 0, 90, 1000, Easing::rushOut)
			)
		), false);
		Animations.playAndWait(anims);
		zm.delay(8000);
		anims.stop();
	}

}
