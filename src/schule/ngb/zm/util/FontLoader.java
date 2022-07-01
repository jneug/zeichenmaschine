package schule.ngb.zm.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FontLoader {

	private static final Map<String, Font> fontCache = new ConcurrentHashMap<>();

	public static Font loadFont( String source ) {
		if( source == null || source.length() == 0 )
			throw new IllegalArgumentException("Font source may not be null or empty.");

		if( fontCache.containsKey(source) ) {
			return fontCache.get(source);
		}

		// Look for System fonts
		Font font = Font.decode(source);
		if( font != null && source.toLowerCase().contains(font.getFamily().toLowerCase()) ) {
			fontCache.put(source, font);
			return font;
		}

		// Load userfonts
		try {
			InputStream in;

			File file = new File(source);
			if( file.isFile() ) {
				in = new FileInputStream(file);
			} else {
				// load ressource relative to .class-file
				in = FontLoader.class.getResourceAsStream(source);

				// relative to ClassLoader
				if( in == null ) {
					in = FontLoader.class.getClassLoader().getResourceAsStream(source);
				}

				// load form web or jar-file
				if( in == null ) {
					in = new URL(source).openStream();
				}


				font = Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(Font.PLAIN);
			}

			if( font != null ) {
				fontCache.put(source, font);
				//ge.registerFont(font);
			}

			return font;
		} catch( IOException | FontFormatException ex ) {
			// TODO: Log error!
			return null;
		}
	}

	private FontLoader() {
	}

}
