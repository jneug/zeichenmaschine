package schule.ngb.zm.util.io;

import schule.ngb.zm.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
		try( BufferedReader reader = ResourceStreamProvider.getReader(source, charset) ) {
			List<String> result = new ArrayList<>();

			String line;
			while( (line = reader.readLine()) != null ) {
				result.add(line);
			}

			return result;
		} catch( IOException ex ) {
			LOG.error(ex, "Error while loading lines from source <%s>", source);
			return Collections.emptyList();
		}
	}

	public static String loadText( String source ) {
		return loadText(source, UTF8);
	}

	public static String loadText( String source, Charset charset ) {
		try( BufferedReader reader = ResourceStreamProvider.getReader(source, charset) ) {
			StringBuilder result = new StringBuilder();

			String line;
			while( (line = reader.readLine()) != null ) {
				result.append(line).append('\n');
			}

			return result.toString();
		} catch( IOException ex ) {
			LOG.error(ex, "Error while loading string from source <%s>", source);
			return "";
		}
	}

	public static String[][] loadCsv( String source, boolean skipFirst ) {
		return loadCsv(source, ',', skipFirst, UTF8);
	}

	/**
	 * Lädt die Daten aus einer CSV Datei in ein zweidimensionales
	 * String-Array.
	 * <p>
	 * Die Methode ist nicht in der Lage, komplexe CSV-Dateien zu verarbeiten.
	 * Insbesondere können Inhalte, die das Trennzeichen {@code separator}
	 * enthalten, nicht korrekt erkannt werden. Das Trennzeichen wird unabhängig
	 * vom Kontext immer als Zelltrenner erkannt. (Im Normalfall kann das
	 * Trennzeichen durch die Verwendung doppelter Anführungszeichen in der Art
	 * {@code Inhalt,"Inhalt, der Komma enthält",Inhalt} maskiert werden.)
	 * <p>
	 * Es wird auch keine erweiterte Inhaltserkennung ausgeführt, sondern alle
	 * Inhalte als {@code String} gelesen. Die weitere Verarbeitung mit den
	 * passenden Parser-Methoden (beispielsweise
	 * {@link Double#parseDouble(String)}) obligt dem Nutzer.
	 *
	 * @param source Die Quelle der CSV-Daten.
	 * @param separator Das verwendete Trennzeichen.
	 * @param skipFirst Ob die erste Zeile übersprungen werden soll.
	 * @param charset Die zu verwendende Zeichenkodierung.
	 * @return Ein Array mit den Daten als {@code String}s.
	 */
	public static String[][] loadCsv( String source, char separator, boolean skipFirst, Charset charset ) {
		int n = skipFirst ? 1 : 0;
		List<String> lines = loadLines(source, charset);
		return lines.stream().skip(n).map(
			//( line ) -> line.split(Character.toString(separator))
			( line ) -> line.split("\\s*" + separator + "\\s*")
		).toArray(String[][]::new);
	}

	public static double[][] loadValues( String source, char separator, boolean skipFirst ) {
		return loadValues(source, separator, skipFirst, UTF8);
	}

	/**
	 * Lädt Double-Werte aus einer CSV Datei in ein zweidimensionales Array.
	 * <p>
	 * Die gelesenen Strings werden mit {@link Double#parseDouble(String)} in
	 * {@code double} umgeformt. Es leigt in der Verantwortung des Nutzers
	 * sicherzustellen, dass die CSV-Datei auch nur Zahlen enthält, die korrekt
	 * in {@code double} umgewandelt werden können. Zellen für die die
	 * Umwandlung fehlschlägt werden mit 0.0 befüllt.
	 * <p>
	 * Die Methode unterliegt denselben Einschränkungen wie
	 * {@link #loadCsv(String, char, boolean, Charset)}.
	 *
	 * @param source Die Quelle der CSV-Daten.
	 * @param separator Das verwendete Trennzeichen.
	 * @param skipFirst Ob die erste Zeile übersprungen werden soll.
	 * @param charset Die zu verwendende Zeichenkodierung.
	 * @return Ein Array mit den Daten als {@code String}s.
	 */
	public static double[][] loadValues( String source, char separator, boolean skipFirst, Charset charset ) {
		int n = skipFirst ? 1 : 0;
		List<String> lines = loadLines(source, charset);
		return lines.stream().skip(n).map(
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
	}

	public FileLoader() {
	}

	private static final Log LOG = Log.getLogger(FileLoader.class);

}
