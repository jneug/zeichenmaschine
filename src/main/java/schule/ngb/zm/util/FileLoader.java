package schule.ngb.zm.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class FileLoader {

	public static final Charset ASCII = StandardCharsets.US_ASCII;

	public static final Charset UTF8 = StandardCharsets.UTF_8;

	public static final Charset UTF16 = StandardCharsets.UTF_16;

	public static final Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;


	public static List<String> loadLines( String source ) {
		return loadLines(source, UTF8);
	}

	public static List<String> loadLines( String source, Charset charset ) {
		try {
			return Files.readAllLines(Paths.get(ResourceStreamProvider.getResourceURL(source).toURI()), charset);
		} catch( IOException | URISyntaxException ex ) {
			LOG.warn(ex, "Error while loading lines from source <%s>", source);
		}

		return Collections.EMPTY_LIST;
	}

	public static String loadText( String source ) {
		return loadText(source, UTF8);
	}

	public static String loadText( String source, Charset charset ) {
		try {
			return Files.readString(Paths.get(ResourceStreamProvider.getResourceURL(source).toURI()), charset);
		} catch( IOException | URISyntaxException ex ) {
			LOG.warn(ex, "Error while loading text from source <%s>", source);
		}

		return "";
	}

	public static double[][] loadDoubles( String source, char separator, boolean skipFirst ) {
		return loadDoubles(source, separator, skipFirst, UTF8);
	}

	public static double[][] loadDoubles( String source, char separator, boolean skipFirst, Charset charset ) {
		try {
			int n = skipFirst ? 1 : 0;
			return Files
				.lines(Paths.get(ResourceStreamProvider.getResourceURL(source).toURI()), charset)
				.skip(n)
				.map(
					( line ) -> Arrays
						.stream(line.split(Character.toString(separator)))
						.mapToDouble(
							( value ) -> {
								try {
									return Double.parseDouble(value);
								} catch( NumberFormatException nfe ) {
									return 0.0;
								}
							}
						).toArray()
				).toArray(double[][]::new);
		} catch( IOException | URISyntaxException ex ) {
			LOG.warn(ex, "Error while loading double values from csv source <%s>", source);
		}

		return new double[0][0];
	}

	public FileLoader() {
	}

	private static final Log LOG = Log.getLogger(FileLoader.class);

}
