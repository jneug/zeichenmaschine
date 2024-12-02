package schule.ngb.zm;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import schule.ngb.zm.layers.DrawingLayer;
import schule.ngb.zm.util.io.ImageLoader;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static schule.ngb.zm.util.test.ImageAssertions.assertEquals;
import static schule.ngb.zm.util.test.ImageAssertions.setSaveDiffImageOnFail;

public class TestmaschineTest {

	private static Testmaschine tm;

	private static DrawingLayer drawing;

	@BeforeAll
	static void beforeAll() {
		setSaveDiffImageOnFail(true);

		tm = new Testmaschine();
		drawing = tm.getDrawingLayer();
		assertNotNull(drawing);
	}

	@AfterAll
	static void afterAll() {
		tm.exit();
	}

	@BeforeEach
	void setUp() {
		drawing.clear();
	}

	@Test
	void testSaveDiffImage() {
		drawing.noStroke();
		drawing.setAnchor(Constants.NORTHWEST);
		drawing.setFillColor(Constants.BLUE);
		drawing.rect(0, 0, 400, 400);
		drawing.setFillColor(Constants.RED);
		drawing.rect(100, 100, 200, 200);

		BufferedImage img1 = ImageLoader.createImage(400, 400);
		Graphics2D graphics = img1.createGraphics();

		graphics.setColor(Constants.BLUE.getJavaColor());
		graphics.fillRect(0, 0, 400, 400);
		graphics.setColor(Constants.RED.getJavaColor());
		graphics.fillRect(100, 100, 200, 200);

		assertEquals(drawing.buffer, drawing.buffer);
		assertEquals(ImageLoader.copyImage(drawing.buffer), drawing.buffer);
		assertEquals(img1, drawing.buffer);
		assertEquals(img1, tm.getImage());
	}

	@Test
	void testGrid() {
//		tm.setGrid(50, 10);
		tm.delay(2000);
	}

}
