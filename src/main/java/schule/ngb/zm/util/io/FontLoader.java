package schule.ngb.zm.util.io;

import schule.ngb.zm.util.Log;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class FontLoader {

	private static final Map<String, Font> fontCache = new ConcurrentHashMap<>();

	/**
	 * Lädt eine Schrift aus einer Datei.
	 * <p>
	 * Die Schrift wird unter ihrem Dateinamen in den Schriftenspeicher geladen
	 * und kann danach in der Zeichenmaschine benutzt werden.
	 * <p>
	 * Eine Datei mit dem Namen "fonts/Font-Name.ttf" würde mit dem Namen
	 * "Font-Name" geladen und kann danach zum Beispiel in einem
	 * {@link schule.ngb.zm.shapes.Text} mit {@code text.setFont("Font-Name");}
	 * verwendet werden.
	 *
	 * @param source
	 * @return
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
		Objects.requireNonNull(source, "Font source may not be null");
		if( source.length() == 0 ) {
			throw new IllegalArgumentException("Font source may not be empty.");
		}

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
		} catch( IOException ioex ) {
			LOG.error(ioex, "Error loading custom font file from source <%s>.", source);
		} catch( FontFormatException ffex ) {
			LOG.error(ffex, "Error creating custom font from source <%s>.", source);
		}

		return font;
	}

	private FontLoader() {
	}

	private static final Log LOG = Log.getLogger(FontLoader.class);

}
