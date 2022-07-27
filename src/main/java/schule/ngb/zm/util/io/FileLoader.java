package schule.ngb.zm.util.io;

import schule.ngb.zm.util.Log;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

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
			LOG.error(ex, "Error while loading lines from source <%s>", source);
		}

		return Collections.emptyList();
	}

	public static String loadText( String source ) {
		return loadText(source, UTF8);
	}

	public static String loadText( String source, Charset charset ) {
		try {
			return Files.readString(Paths.get(ResourceStreamProvider.getResourceURL(source).toURI()), charset);
		} catch( IOException | URISyntaxException ex ) {
			LOG.error(ex, "Error while loading text from source <%s>", source);
		}

		return "";
	}

	public static String[][] loadCsv( String source, boolean skipFirst ) {
		return loadCsv(source, ',', skipFirst, UTF8);
	}

	public static String[][] loadCsv( String source, char separator, boolean skipFirst, Charset charset ) {
		try( Stream<String> lines = Files
			.lines(Paths.get(ResourceStreamProvider.getResourceURL(source).toURI()), charset)
		) {
			int n = skipFirst ? 1 : 0;
			return lines.skip(n).map(
					( line ) -> line.split(Character.toString(separator))
				).toArray(String[][]::new);
		} catch( IOException | URISyntaxException ex ) {
			LOG.error(ex, "Error while loading csv source <%s>", source);
		}

		return new String[0][0];
	}

	public static double[][] loadValues( String source, char separator, boolean skipFirst ) {
		return loadValues(source, separator, skipFirst, UTF8);
	}

	public static double[][] loadValues( String source, char separator, boolean skipFirst, Charset charset ) {
		try( Stream<String> lines = Files
			.lines(Paths.get(ResourceStreamProvider.getResourceURL(source).toURI()), charset)
		) {
			int n = skipFirst ? 1 : 0;
			return lines.skip(n).map(
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
			LOG.error(ex, "Error while loading double values from csv source <%s>", source);
		}

		return new double[0][0];
	}

	public FileLoader() {
	}

	private static final Log LOG = Log.getLogger(FileLoader.class);

}
