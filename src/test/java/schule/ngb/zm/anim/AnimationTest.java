package schule.ngb.zm.anim;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import schule.ngb.zm.Testmaschine;
import schule.ngb.zm.layers.ShapesLayer;
import schule.ngb.zm.shapes.Circle;
import schule.ngb.zm.shapes.Shape;
import schule.ngb.zm.util.test.TestEnv;

import static org.junit.jupiter.api.Assertions.*;

public class AnimationTest {

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
	void circleAnimation() {
		Shape s = new Circle(zm.getWidth()/4.0, zm.getHeight()/2.0, 10);
		shapes.add(s);

		CircleAnimation anim = new CircleAnimation(s, zm.getWidth()/2.0, zm.getHeight()/2.0, 360, true, 3000, Easing::linear);
		Animations.playAndWait(anim);
		assertEquals(zm.getWidth()/4.0, s.getX());
		assertEquals(zm.getHeight()/2.0, s.getY());
	}

	@Test
	void fadeAnimation() {
		Shape s = new Circle(zm.getWidth()/4.0, zm.getHeight()/2.0, 10);
		s.setFillColor(s.getFillColor(), 0);
		s.setStrokeColor(s.getStrokeColor(), 0);
		shapes.add(s);

		Animation<Shape> anim = new FadeAnimation(s, 255, 1000);
		Animations.playAndWait(anim);
		assertEquals(s.getFillColor().getAlpha(), 255);
	}

	@Test
	void continousAnimation() {
		Shape s = new Circle(zm.getWidth()/4, zm.getHeight()/2, 10);
		shapes.add(s);

		ContinousAnimation anim = new ContinousAnimation(
			new CircleAnimation(s, zm.getWidth()/2, zm.getHeight()/2, 360, true, 1000, Easing::linear)
		);
		Animations.play(anim);
		zm.delay(3000);
		anim.stop();
	}

}
