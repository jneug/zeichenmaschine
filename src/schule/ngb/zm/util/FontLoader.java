package schule.ngb.zm.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class FontLoader {

	private static final Map<String, Font> fontCache = new ConcurrentHashMap<>();

	public static Font loadFont( String source ) {
		Objects.requireNonNull(source, "Font source may not be null");
		if( source.length() == 0 ) {
			throw new IllegalArgumentException("Font source may not be empty.");
		}

		if( fontCache.containsKey(source) ) {
			LOG.trace("Retrieved font <%s> from font cache.", source);
			return fontCache.get(source);
		}

		// Look for System fonts
		Font font = Font.decode(source);
		if( font != null && source.toLowerCase().contains(font.getFamily().toLowerCase()) ) {
			fontCache.put(source, font);
			LOG.debug("Loaded system font for <%s>.", source);
			return font;
		} else {
			font = null;
		}

		// Load userfonts
		try( InputStream in = ResourceStreamProvider.getResourceStream(source) ) {
			font = Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(Font.PLAIN);

			if( font != null ) {
				fontCache.put(source, font);
				//ge.registerFont(font);
			}
			LOG.debug("Loaded custom font from <%s>.", source);
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
