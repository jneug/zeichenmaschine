package schule.ngb.zm.util.test;

import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;
import schule.ngb.zm.util.io.ImageLoader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

public final class ImageAssertions {

	private static boolean SAVE_DIFF_IMAGE_ON_FAIL = false;

	private static File DIFF_IMAGE_PATH = new File("build/test-results/diff");

	private static AssertionFailedError ASSERTION_FAILED_ERROR = null;


	public static boolean isSaveDiffImageOnFail() {
		return SAVE_DIFF_IMAGE_ON_FAIL;
	}

	public static final void setSaveDiffImageOnFail( boolean saveOnFail ) {
		SAVE_DIFF_IMAGE_ON_FAIL = saveOnFail;
	}

	public static File getDiffImagePath() {
		return DIFF_IMAGE_PATH;
	}

	public static void assertEquals( BufferedImage expected, BufferedImage actual ) {
		assertEquals(expected, actual, () -> "Actual image differs from expected buffer.");
	}

	public static void assertEquals( BufferedImage expected, BufferedImage actual, String message ) {
		assertEquals(expected, actual, () -> message);
	}

	public static void assertEquals( BufferedImage expected, BufferedImage actual, Supplier<String> messageSupplier ) {
		// Compare image dimensions
		int expectedHeight = expected.getHeight(), expectedWidth = expected.getWidth();
		int actualHeight = actual.getHeight(), actualWidth = actual.getWidth();
		try {
			Assertions.assertEquals(expectedHeight, actualHeight);
			Assertions.assertEquals(expectedWidth, actualWidth);
		} catch( AssertionFailedError afe ) {
			ASSERTION_FAILED_ERROR = afe;
			fail(expected, actual, messageSupplier);
		}

		// TODO: Fix comparison of transparent pixels
		for( int x = 0; x < actualWidth; x++ ) {
			for( int y = 0; y < actualHeight; y++ ) {
				try {
					Assertions.assertTrue(comparePixels(expected.getRGB(x, y), actual.getRGB(x, y)));
				} catch( AssertionFailedError afe ) {
					ASSERTION_FAILED_ERROR = afe;
					fail(expected, actual, messageSupplier);
				}
			}
		}
	}

	public static void assertNotEquals( BufferedImage expected, BufferedImage actual ) {
		assertNotEquals(expected, actual, () -> "Actual image is the same as expected buffer.");
	}

	public static void assertNotEquals( BufferedImage expected, BufferedImage actual, String message ) {
		assertNotEquals(expected, actual, () -> message);
	}

	public static void assertNotEquals( BufferedImage expected, BufferedImage actual, Supplier<String> messageSupplier ) {
		// Compare image dimensions
		int expectedHeight = expected.getHeight(), expectedWidth = expected.getWidth();
		int actualHeight = actual.getHeight(), actualWidth = actual.getWidth();
		if( expectedHeight != actualHeight || expectedWidth != actualWidth ) {
			// Image dimensions differ, assertion is true
			return;
		}

		for( int x = 0; x < actualWidth; x++ ) {
			for( int y = 0; y < actualHeight; y++ ) {
				if( !comparePixels(expected.getRGB(x, y), actual.getRGB(x, y)) ) {
					// Found different pixels, assertion is true
					return;
				}
			}
		}

		// Images are the same, fail without diff
		fail(expected, actual, messageSupplier, false);
	}

	private static void fail( BufferedImage expected, BufferedImage actual, Supplier<String> messageSupplier ) {
		fail(expected, actual, messageSupplier, SAVE_DIFF_IMAGE_ON_FAIL);
	}

	private static void fail( BufferedImage expected, BufferedImage actual, Supplier<String> messageSupplier, boolean saveDiffImage ) {
		if( saveDiffImage ) {
			saveDiffImage(expected, actual);
		}
		throw new AssertionFailedError(
			messageSupplier != null ? messageSupplier.get() : null,
			ASSERTION_FAILED_ERROR
		);
	}

	private static boolean comparePixels( int a, int b ) {
		// TODO: Fix comparison of transparent pixels
		return a == b || ((0xFF000000 & a) == 0 && (0xFF000000 & b) == 0);
	}

	public static BufferedImage createDiffImage( BufferedImage expected, BufferedImage actual ) {
		// Error color (white)
		int errorColor = 0xFF00FF;

		int expectedHeight = expected.getHeight(), expectedWidth = expected.getWidth();
		int actualHeight = actual.getHeight(), actualWidth = actual.getWidth();
		int maxHeight = Math.max(expectedHeight, actualHeight), maxWidth = Math.max(expectedWidth, actualWidth);

		BufferedImage diff = ImageLoader.createImage(maxWidth, maxHeight);
		for( int x = 0; x < maxWidth; x++ ) {
			for( int y = 0; y < maxHeight; y++ ) {
				diff.setRGB(x, y, 0);
				if( x > actualWidth || y > actualHeight || x > expectedWidth || y > expectedHeight ) {
					// Set overflow pixels to error color
					diff.setRGB(x, y, errorColor);
				} else if( !comparePixels(actual.getRGB(x, y), expected.getRGB(x, y)) ) {
					// Set differences to error color
					// If both pixels are transparent, the color dows not matter ...
					// TODO: saturate error color based on how different the colors are?
					diff.setRGB(x, y, errorColor);
				}
			}
		}

		return diff;
	}

	public static boolean saveDiffImage( BufferedImage expected, BufferedImage actual ) {
		BufferedImage diff = createDiffImage(expected, actual);
		try {
			File diffFile = new File(DIFF_IMAGE_PATH, makeDiffName());
			if( !diffFile.getParentFile().exists() ) {
				diffFile.mkdirs();
			}
			ImageLoader.saveImage(diff, diffFile);
		} catch( IOException ioe ) {
			// We fail anyways at this point
			// TODO: Log something?
			return false;
		}
		return true;
	}

	private static String makeDiffName() {
		return System.currentTimeMillis() + ".png";
	}

	private ImageAssertions() {
	}

}
