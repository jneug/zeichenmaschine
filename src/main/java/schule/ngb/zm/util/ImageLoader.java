package schule.ngb.zm.util;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ImageLoader {

	public static boolean caching = true;

	private static Map<String, SoftReference<BufferedImage>> imageCache = new ConcurrentHashMap<>();

	private static SoftReference<BufferedImage> NOCACHE = new SoftReference<>(null);

	/**
	 * Lädt ein Bild von der angegebenen Quelle {@code source}.
	 * <p>
	 * Die Bilddatei wird nach den Regeln von
	 * {@link ResourceStreamProvider#getInputStream(String)} gesucht und
	 * geöffnet. Tritt dabei ein Fehler auf oder konnte keine passende Datei
	 * gefunden werden, wird {@code null} zurückgegeben.
	 * <p>
	 * Ist der {@link #activateCache() Cache aktiviert} und ein Bild mit der
	 * angegebenen Quelle schon vorhanden, wird das gespeicherte Bild
	 * zurückgegeben.
	 *
	 * @param source
	 * @return
	 * @see #loadImage(String, boolean)
	 */
	public static BufferedImage loadImage( String source ) {
		return loadImage(source, caching);
	}

	/**
	 * Lädt ein Bild von der angegebenen Quelle <var>source</var> und gibt das
	 * Bild zurück oder {@code null}, wenn das Bild nicht geladen werden
	 * konnte. Ist ein Bild mit der angegebenen Quelle im Cache, wird das
	 * gespeicherte Bild zurückgegeben. Dies kann mit {@code caching = false}
	 * verhindert werden.
	 * <p>
	 * Wurde das Chaching global deaktiviert, kann mit <code>caching =
	 * true</code> das Bild trotzdem aus dem Cache geladen werden, wenn es
	 * vorhanden ist.
	 *
	 * @param source Die Quelle des Bildes.
	 * @param caching Ob das Bild (falls vorhanden) aus dem Zwischenspeicher
	 * 	geladen werden soll.
	 * @return
	 */
	public static BufferedImage loadImage( String source, boolean caching ) {
		Validator.requireNotNull(source, "Image source may not be null");
		Validator.requireNotEmpty(source, "Image source may not be empty.");

		if( caching && isCached(source) ) {
			return getCache(source);
		}

		BufferedImage img = null;
		try( InputStream in = ResourceStreamProvider.getInputStream(source) ) {
			//URL url = ResourceStreamProvider.getResourceURL(source);
			//BufferedImage img = ImageIO.read(url);

			img = ImageIO.read(in);

			if( caching && img != null ) {
				putCache(source, img);
			}
		} catch( IOException ioex ) {
			LOG.error(ioex, "Error loading image file from source <%s>.", source);
		}
		return img;
	}

	/**
	 * Lädt ein Bild aus der angegebenen Quelle unter dem angegebenen Namen in
	 * den Cache.
	 * <p>
	 * Der {@code name} kann beliebig gewählt werden. Existiert unter dem Namen
	 * schon ein Bild im Zwischenspeicher, wird dieses überschrieben.
	 * <p>
	 * Wenn der Cache aktiviert ist, werden zukünftige Aufrufe von
	 * {@link #loadImage(String)} mit {@code name} als Quelle das gespeicherte
	 * Bild zurückgeben.
	 *
	 * @param name Name des Bildes im Zwischenspeicher.
	 * @param source Quelle, aus dem das Bild geladen werden soll.
	 * @return {@code true}, wenn das Bild erfolgreich geladen wurde,
	 *    {@code false} sonst.
	 * @see #loadImage(String)
	 */
	public static boolean preloadImage( String name, String source ) {
		BufferedImage img = loadImage(source, true);
		if( img != null ) {
			putCache(name, img);
			return true;
		}
		return false;
	}

	/**
	 * Speiechert das angegebene Bild unter dem angegebenen Namen im Cache.
	 * <p>
	 * Exisitert zu {@code name} schon ein Bild im Zwischenspeicher
	 * ({@code ImageLoader.isCached(name) == true}), dann wird dieses
	 * überschrieben.
	 * <p>
	 * Wenn der Cache aktiviert ist, werden zukünftige Aufrufe von
	 * {@link #loadImage(String)} mit {@code name} als Quelle das gespeicherte
	 * Bild zurückgeben.
	 *
	 * @param name Name des Bildes im Zwischenspeicher.
	 * @param img ZU speicherndes Bild.
	 */
	public static void preloadImage( String name, BufferedImage img ) {
		putCache(name, img);
	}

	/**
	 * Prüft, ob zum angegebenen Namen ein Bild im Cache gespeichert ist.
	 *
	 * @param name Name des Bildes im Cache.
	 * @return {@code true}, wenn es ein Bild zum Namen gibt, sonst
	 *    {@code false}.
	 */
	public static boolean isCached( String name ) {
		SoftReference<BufferedImage> imgRef = imageCache.get(name);
		return imgRef != null && imgRef != NOCACHE && imgRef.get() != null;
	}

	/**
	 * Entfernt das Bild zum angegebenen Namen aus dem Cache. Gibt es zum Namen
	 * kein Bild im Zwischenspeicher, dann passiert nichts.
	 *
	 * @param name Name des Bildes im Cache.
	 */
	public static void invalidateCache( final String name ) {
		if( imageCache.containsKey(name) ) {
			imageCache.remove(name);
		}
	}

	/**
	 * Speichert ein Bild als {@link SoftReference} im Cache.
	 *
	 * @param name Name des Bildes im Zwischenspeicher.
	 * @param img Das zu speichernde Bild.
	 */
	private static void putCache( final String name, final BufferedImage img ) {
		imageCache.put(name, new SoftReference<>(img));
	}

	/**
	 * Holt ein Bild aus dem Cache.
	 * <p>
	 * Prüft nicht, ob ein Bild vorhanden ist. Dies sollte vom Aufrufenden
	 * übernommen werden, da sonst eine {@link NullPointerException} erzeugt
	 * werden kann.
	 *
	 * @param name
	 * @return
	 */
	private static BufferedImage getCache( final String name ) {
		return imageCache.get(name).get();
	}

	/**
	 * Deaktiviert den Cache für die angegebene Quelle.
	 * <p>
	 * Selbst wenn der {@link #activateCache() Cache aktiviert} ist, wird das
	 * Bild zur angegebenen Quelle niemals zwischengespeichert und immer neu
	 * geladen.
	 *
	 * @param name Die Quelle des Bildes.
	 */
	public static void preventCache( final String name ) {
		imageCache.put(name, NOCACHE);
	}

	/**
	 * Leer den Cache und löschte alle bisher gespeicherten Bilder.
	 * <p>
	 * Auch vorher mit {@link #preventCache(String)} verhinderte Caches werden
	 * gelöscht und müssen neu gesetzt werden.
	 */
	public static void clearCache() {
		imageCache.clear();
	}

	/**
	 * Aktiviert den Cache.
	 * <p>
	 * Der Cache ist ein Zwischenspeicher für geladene Bilder. Wenn er aktiviert
	 * ist (Standard), dann werden mit dem {@code ImageLoader} geladene Bilder
	 * im Zwischenspeicher abgelegt. Bei jedem folgenden laden desselben Bildes
	 * (bzw. eines Bildes mit derselben {@code source}), wird das gespeicherte
	 * Bild zurückgegeben und nicht komplett neu geladen.
	 * <p>
	 * <b>Wichtig:</b> Bildreferenzen, die aus dem Cache geladen werden,
	 * verweisen alle auf dasselbe Objekt. Änderungen schalgen sich daher in
	 * allen anderen Versionen des Bildes nieder (inklusive dem Bild im
	 * Zwischenspeicher). Für Änderungen sollte daher immer
	 * {@link #copyImage(BufferedImage) eine Kopie} des Bildes erstellt werden:
	 * <pre>
	 * BufferedImage originalImage = ImageLoader.loadImage("assets/image.gif");
	 * BufferedImage copiedImage = ImageLoader.copyImage(originalImage);
	 * </pre>
	 * <p>
	 * Alternativ kann der Cache umgangen werden, indem
	 * {@link #loadImage(String, boolean)} verwendet wird.
	 */
	public static void activateCache() {
		caching = true;
	}

	/**
	 * Deaktiviert den Cache.
	 *
	 * @see #activateCache()
	 */
	public static void deactivateCache() {
		caching = false;
	}


	/**
	 * Erstellt eine exakte Kopie des angegebenen Bildes als neues Objekt.
	 * <p>
	 * Die Methode ist hilfreich, wenn ein Bild aus dem
	 * {@link #activateCache() Cache} geladen wurde und dann verändert werden
	 * soll, ohne den Cache (oder andere Referenzen auf das Bild) zu verändern.
	 *
	 * @param image Das Originalbild.
	 * @return Eine exakte Kopie des Originals.
	 */
	public static BufferedImage copyImage( BufferedImage image ) {
		ColorModel cm = image.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = image.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	/**
	 * Erstellt ein {@code BufferedImage} mit demselben Inhalt wie das
	 * angegebene {@code Image}.
	 *
	 * @param image Das Originalbild.
	 * @return Eine exakte Kopie des Originals.
	 */
	public static BufferedImage copyImage( Image image ) {
		if( image instanceof BufferedImage ) {
			return copyImage((BufferedImage) image);
		} else {
			//BufferedImage outimage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
			BufferedImage outimage = createImage(image.getWidth(null), image.getHeight(null));

			Graphics2D g = outimage.createGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, outimage.getWidth(), outimage.getHeight());
			g.drawImage(image, 0, 0, null);
			g.dispose();

			return outimage;
		}
	}

	/**
	 * Speichert das angegebene Bild in der angegebenen Datei auf der
	 * Festplatte.
	 *
	 * @param image Das zu speichernde Bild. Falls die Datei schon existiert,
	 * 	wird sie überschrieben.
	 * @param file Die Zieldatei.
	 * @throws NullPointerException Falls {@code image} oder {@code file}
	 *                              {@code null} ist.
	 * @throws IOException          Falls es einen Fehler beim Speichern gab.
	 */
	public static void saveImage( Image image, File file ) throws IOException {
		saveImage(copyImage(image), file, false);
	}

	/**
	 * Speichert das angegebene Bild in der angegebenen Datei auf der
	 * Festplatte.
	 *
	 * @param image Das zu speichernde Bild. Falls die Datei schon existiert,
	 * 	wird sie überschrieben.
	 * @param file Die Zieldatei.
	 * @param overwriteIfExists Bei {@code true} wird eine vorhandene Datei
	 * 	überschrieben, bei {@code false} wird eine {@link IOException} geworfen,
	 * 	wenn die Datei schon exisitiert.
	 * @throws NullPointerException Falls {@code image} oder {@code file}
	 *                              {@code null} ist.
	 * @throws IOException          Falls es einen Fehler beim Speichern gab.
	 */
	public static void saveImage( Image image, File file, boolean overwriteIfExists ) throws IOException {
		saveImage(copyImage(image), file, overwriteIfExists);
	}

	/**
	 * Speichert das angegebene Bild in der angegebenen Datei auf der
	 * Festplatte.
	 *
	 * @param image Das zu speichernde Bild. Falls die Datei schon existiert,
	 * 	wird sie überschrieben.
	 * @param file Die Zieldatei.
	 * @throws NullPointerException Falls {@code image} oder {@code file}
	 *                              {@code null} ist.
	 * @throws IOException          Falls es einen Fehler beim Speichern gab.
	 */
	public static void saveImage( BufferedImage image, File file ) throws IOException {
		saveImage(image, file, false);
	}

	/**
	 * Speichert das angegebene Bild in der angegebenen Datei auf der
	 * Festplatte.
	 *
	 * @param image Das zu speichernde Bild. Falls die Datei schon existiert,
	 * 	wird sie überschrieben.
	 * @param file Die Zieldatei.
	 * @param overwriteIfExists Bei {@code true} wird eine vorhandene Datei
	 * 	überschrieben, bei {@code false} wird eine {@link IOException} geworfen,
	 * 	wenn die Datei schon exisitiert.
	 * @throws NullPointerException Falls {@code image} oder {@code file}
	 *                              {@code null} ist.
	 * @throws IOException          Falls es einen Fehler beim Speichern gab.
	 */
	public static void saveImage( BufferedImage image, File file, boolean overwriteIfExists ) throws IOException {
		if( image == null ) {
			throw new NullPointerException("Image may not be <null>.");
		}
		if( file == null ) {
			throw new NullPointerException("File may not be <null>.");
		}

		if( file.isFile() ) {
			// Datei existiert schon
			if( !overwriteIfExists ) {
				throw new IOException("File already exists. Delete target file before saving image.");
			} else if( !file.canWrite() ) {
				throw new IOException("File already exists and is not writeable. Change permissions before saving again.");
			}
		}

		// Dateiformat anhand der Dateiendung ermitteln oder PNG nutzen
		String filename = file.getName();
		String formatName = "png";
		if( filename.lastIndexOf('.') >= 0 ) {
			formatName = filename.substring(filename.lastIndexOf('.') + 1);
		} else {
			file = new File(file.getAbsolutePath() + ".png");
		}

		// Datei schreiben
		ImageIO.write(image, formatName, file);
	}

	/**
	 * Erstellt ein neues, leeres {@code BufferedImage} passend für dieses
	 * Anzeigegerät.
	 *
	 * @param width Breite des leeren Bildes.
	 * @param height Höhe des leeren Bildes.
	 * @return Ein neues, leeres Bild.
	 */
	public static BufferedImage createImage( int width, int height ) {
		return createImage(width, height, BufferedImage.TYPE_INT_RGB);
	}

	/**
	 * Erstellt ein neues, leeres {@code BufferedImage} passend für dieses
	 * Anzeigegerät.
	 *
	 * @param width Breite des neuen Bildes.
	 * @param height Höhe des neuen Bildes.
	 * @param type {@link BufferedImage#getType() Typ} des neuen Bildes.
	 * @return Ein neues, leeres Bild.
	 */
	public static BufferedImage createImage( int width, int height, int type ) {
		return GraphicsEnvironment
			.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice()
			.getDefaultConfiguration()
			.createCompatibleImage(width, height, type);
	}

	private ImageLoader() {
	}

	private static final Log LOG = Log.getLogger(ImageLoader.class);

}