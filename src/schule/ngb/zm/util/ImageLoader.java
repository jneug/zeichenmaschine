package schule.ngb.zm.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class ImageLoader {

	public static boolean cacheing = true;

	private static HashMap<String, BufferedImage> imageCache = new HashMap<>();

	public static BufferedImage loadImage( String source ) {
		return loadImage(source, cacheing);
	}

	/**
	 * Läadt ein Bild von der angegebenen Quelle <var>source</var> und gibt das
	 * Bild zurück oder <code>null</code>, wenn das Bild nicht geladen werden
	 * konnte. Ist ein Bild mit der angegebenen Quelle im Cache, wird das
	 * gechachete Bild zurückgegeben. Dies kann mit <code>cacheing = false</code>
	 * verhindert werden.
	 * <p>
	 * Wurde chacheing global deaktiviert, kann mit <code>cacheing = true</code>
	 * das Bild trotzdem aus dem Cache geladen werden, wenn es vorhanden ist.
	 *
	 * @param source
	 * @param cacheing
	 * @return
	 */
	public static BufferedImage loadImage( String source, boolean cacheing ) {
		if( source == null || source.length() == 0 )
			throw new IllegalArgumentException("Image source may not be null or empty.");

		if( cacheing && imageCache.containsKey(source) ) {
			BufferedImage cachedImage = imageCache.get(source);
			if( cachedImage != null ) {
				return cachedImage;
			}
		}

		try {
			BufferedImage img;

			// Load image from working dir
			File file = new File(source);
			if( file.isFile() ) {
				img = ImageIO.read(file);
			} else {
				// load ressource relative to .class-file
				URL url = ImageLoader.class.getResource(source);

				// relative to ClassLoader
				if( url == null ) {
					url = ImageLoader.class.getClassLoader().getResource(source);
				}

				// load form web or jar-file
				if( url == null ) {
					url = new URL(source);
				}

				img = ImageIO.read(url);
			}

			if( cacheing && img != null ) {
				imageCache.put(source, img);
			}

			return img;
		} catch( IOException ioe ) {
			return null;
		}
	}

	/**
	 * Loads an image into the cache with a user specified name that may differ
	 * from the image source string.
	 *
	 * @param name
	 * @param source
	 * @return
	 */
	public static boolean preloadImage( String name, String source ) {
		BufferedImage img = loadImage(source, true);
		if( cacheing && img != null ) {
			imageCache.put(name, img);
			return true;
		}
		return false;
	}

	/**
	 * Checks if an image with the given name is currently cached.
	 *
	 * @param name
	 * @return
	 */
	public static boolean isCached( String name ) {
		if( imageCache.containsKey(name) ) {
			return imageCache.get(name) != null;
		}
		return false;
	}

	/**
	 * Remove the specified key from the cache.
	 *
	 * @param name
	 */
	public static void invalidateCache( String name ) {
		if( imageCache.containsKey(name) ) {
			imageCache.remove(name);
		}
	}

	/**
	 * Prevents caching for the specified source.
	 *
	 * @param source
	 */
	public static void noCache( String source ) {
		imageCache.put(source, null);
	}

	public static void clearCache() {
		imageCache.clear();
	}

	public static void enableCaching() {
		cacheing = true;
	}

	public static void disableCaching() {
		cacheing = false;
	}


	public static void saveImage( Image image, File file ) throws IOException {
		saveImage(image, file, false);
	}

	public static void saveImage( Image image, File file, boolean overwriteIfExists ) throws IOException {
		//BufferedImage outimage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
		BufferedImage outimage = createImage(image.getWidth(null), image.getHeight(null));

		Graphics2D g = outimage.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, outimage.getWidth(), outimage.getHeight());
		g.drawImage(image, 0, 0, null);
		g.dispose();

		saveImage(outimage, file, overwriteIfExists);
	}

	public static void saveImage( BufferedImage image, File file ) throws IOException {
		saveImage(image, file, false);
	}

	public static void saveImage( BufferedImage image, File file, boolean overwriteIfExists ) throws IOException {
		if( file.isFile() ) {
			if( !overwriteIfExists ) {
				throw new IOException("File already exists. Delete target file before saving image.");
			} else if( !file.canWrite() ) {
				throw new IOException("File already exists and is not writeable. Change permissions before saving again.");
			}
		}

		String filename = file.getName();
		String formatName = "png";
		if( filename.lastIndexOf('.') >= 0 ) {
			formatName = filename.substring(filename.lastIndexOf('.') + 1);
		} else {
			file = new File(file.getAbsolutePath() + ".png");
		}

		ImageIO.write(image, formatName, file);
	}

	public static BufferedImage createImage( int width, int height ) {
		return createImage(width, height, BufferedImage.TYPE_INT_RGB);
	}

	public static BufferedImage createImage( int width, int height, int type ) {
		return GraphicsEnvironment
			.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice()
			.getDefaultConfiguration()
			.createCompatibleImage(width, height, type);
	}

}
