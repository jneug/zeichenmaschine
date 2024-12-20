package schule.ngb.zm.util.io;

import schule.ngb.zm.util.Cache;
import schule.ngb.zm.util.Log;
import schule.ngb.zm.util.Validator;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Eine Hilfsklasse mit Klassenmethoden, um Schriftarten zu laden.
 * <p>
 * Schriftarten können von verschiedenen Quellen geladen werden. Schriftarten,
 * die aus Dateien geladen wurden, werden in einem internen Cache gespeichert
 * und nachfolgende Versuche, dieselbe Schriftart zu laden, werden aus dem Cache
 * bedient.
 */
public class FontLoader {

	private static final Cache<String, Font> fontCache = Cache.newSoftCache();

	/**
	 * Lädt eine Schrift aus einer Datei.
	 * <p>
	 * Die Methode kann eine Liste von Schriften bekommen und probiert diese
	 * nacheinander zu laden. Die erste Schrift, die Fehlerfrei geladen werden
	 * kann, wird zurückgegeben. Kann keine der Schriften geladen werden, ist
	 * das Ergebnis {@code null}.
	 * <p>
	 * Die gefundene Schrift wird unter ihrem Dateinamen in den
	 * Schriftenspeicher geladen und kann danach in der Zeichenmaschine benutzt
	 * werden.
	 * <p>
	 * Eine Datei mit dem Namen "fonts/Font-Name.ttf" würde mit dem Namen
	 * "Font-Name" geladen und kann danach zum Beispiel in einem
	 * {@link schule.ngb.zm.shapes.Text} mit {@code text.setFont("Font-Name");}
	 * verwendet werden.
	 *
	 * @param source
	 * @return Die erste geladene Schrift oder {@code null}.
	 * @see #loadFont(String, String)
	 */
	public static Font loadFont( String source ) {
		String name = source;
		// Dateipfad entfernen
		int lastIndex = source.lastIndexOf(File.separatorChar);
		if( lastIndex > -1 ) {
			source = source.substring(lastIndex + 1);
		}
		// Dateiendung entfernen
		lastIndex = name.lastIndexOf('.');
		if( lastIndex > -1 ) {
			name = name.substring(0, lastIndex);
		}
		return loadFont(name, source);
	}

	public static Font loadFont( String name, String source ) {
		Validator.requireNotNull(source, "source");
		Validator.requireNotEmpty(source, "source");

		if( fontCache.containsKey(name) ) {
			LOG.trace("Retrieved font <%s> from font cache.", name);
			return fontCache.get(name);
		}

		// Look for System fonts
		Font font = Font.decode(source);
		if( source.toLowerCase().contains(font.getFamily().toLowerCase()) ) {
			fontCache.put(name, font);
			fontCache.put(source, font);
			LOG.debug("Loaded system font for <%s>.", source);
			return font;
		} else {
			font = null;
		}

		// Load userfonts
		try( InputStream in = ResourceStreamProvider.getInputStream(source) ) {
			font = Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(Font.PLAIN);

			if( font != null ) {
				fontCache.put(name, font);
				fontCache.put(source, font);
				//ge.registerFont(font);
			}
			LOG.debug("Loaded custom font from source <%s>.", source);
		} catch( MalformedURLException muex ) {
			LOG.warn("Could not find font resource for <%s>", source);
		} catch( IOException ioex ) {
			LOG.warn(ioex, "Error loading custom font file from source <%s>.", source);
		} catch( FontFormatException ffex ) {
			LOG.warn(ffex, "Error creating custom font from source <%s>.", source);
		}

		return font;
	}

	/**
	 * Lädt eine Schrift aus einer Datei.
	 * <p>
	 * Die Methode kann eine Liste von Schriften bekommen und probiert diese
	 * nacheinander zu laden. Die erste Schrift, die Fehlerfrei geladen werden
	 * kann, wird zurückgegeben. Kann keine der Schriften geladen werden, ist
	 * das Ergebnis {@code null}.
	 * <p>
	 * Die gefundene Schrift wird unter ihrem Dateinamen in den
	 * Schriftenspeicher geladen und kann danach in der Zeichenmaschine benutzt
	 * werden.
	 * <p>
	 * Eine Datei mit dem Namen "fonts/Font-Name.ttf" würde mit dem Namen
	 * "Font-Name" geladen und kann danach zum Beispiel in einem
	 * {@link schule.ngb.zm.shapes.Text} mit {@code text.setFont("Font-Name");}
	 * verwendet werden.
	 *
	 * @param name
	 * @param sources
	 * @return Die erste geladene Schrift oder {@code null}.
	 * @see #loadFont(String, String)
	 */
	public static Font loadFonts( String name, String... sources ) {
		for( String fontSource : sources ) {
			// TODO: Ignore exceptions in this case and throw own at end?
			Font font = loadFont(name, fontSource);
			if( font != null ) {
				return font;
			}
		}
		return null;
	}

	private FontLoader() {
	}

	private static final Log LOG = Log.getLogger(FontLoader.class);

}
