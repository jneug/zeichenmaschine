package schule.ngb.zm.util.io;

import schule.ngb.zm.Zeichenmaschine;
import schule.ngb.zm.util.Log;
import schule.ngb.zm.util.Validator;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Hilfsklasse, um {@link InputStream}s für Ressourcen zu erhalten.
 */
@SuppressWarnings("unused")
public class ResourceStreamProvider {

	/**
	 * Ermittelt zur angegebenen Quelle einen passenden {@link URL} (<em>Unified
	 * Resource Locator</em>). Eine passende Datei-Ressource wird wie folgt
	 * ermittelt:
	 * <ol>
	 * <li>Ist {@code source} eine existierende Datei
	 *    ({@code new File(source}.isFile() == true})?</li>
	 * <li>Ist {@code source} ein relativer Pfad im Projekt ({@code getResource(source) != null})?</li>
	 * <li>Ist {@code source} im Classpath enthalten ({@code getClassLoader().getResource(source) != null})?</li>
	 * <li>Ansonsten erstelle ein {@link URL}-Objekt.</li>
	 * </ol>
	 * <p>
	 * Ein {@code URL} für die erste gefundene Resource wird zurückgegeben.
	 * <p>
	 * Auftretende Exceptions werden als {@link IOException} geworfen.
	 * Bei einer Exception werden die nachfolgenden Quellen nicht mehr abgefragt.
	 * Eine {@link java.net.MalformedURLException} beim Konstruieren des {@code URL}
	 * zu einer Datei verhindert daher, dass noch im Classpath gesucht wird.
	 *
	 * @param source Eine Quelle für die Ressource (Absoluter Dateipfad,
	 * 	Dateipfad im Classpath oder Netzwerkressource)
	 * @return Ein {@code InputStream} für die Ressource
	 * @throws NullPointerException     Falls {@code source} {@code null} ist.
	 * @throws IllegalArgumentException Falls {@code source} ein leerer String
	 *                                  ist.
	 * @throws IOException              Geworfen beim Erzeugen einer URL zu
	 *                                  einer bestehenden Ressource.
	 */
	public static URL getResourceURL( String source ) throws NullPointerException, IllegalArgumentException, IOException {
		Validator.requireNotNull(source, "Resource source may not be null");
		Validator.requireNotEmpty(source, "Resource source may not be empty.");

		// Ist source ein valider Dateipfad?
		File file = new File(source);
		if( file.isFile() ) {
			return file.toURI().toURL();
		}
		// Ist source im Classpath vorhanden?
		URL url = Zeichenmaschine.class.getClassLoader().getResource(source);
		if( url != null ) {
			return url;
		}
		// Dann versuchen aus source direkt eine URL zu machen.
		return new URL(source);
	}

	/**
	 * Sucht eine zur angegebenen Quelle passende Ressource und öffnet einen
	 * passenden {@link InputStream}. Die konkrete Art des Streams hängt davon
	 * ab, welche Art an Ressource gefunden wird:
	 * <ol>
	 * <li>Ist {@code source} eine existierende Datei
	 *    {@code new File(source).isFile() == true}, dann wird ein
	 *    {@link FileInputStream} erstellt.</li>
	 * <li>Ist {@code source} ein relativer Pfad im Projekt, wird ein passender
	 * 	Stream mit {@link Class#getResourceAsStream(String)} geöffnet.</li>
	 * <li>Ist {@code source} im Classpath enthalten, wird ein passender Stream
	 * 	mit {@link ClassLoader#getResourceAsStream(String)} geöffnet.</li>
	 * <li>Ist {@code source} eine gültige {@link URL}, dann wird ein
	 *    {@link URL#openStream() Netwerkstream} geöffnet.</li>
	 * </ol>
	 * <p>
	 * Die Möglichen Ressourcen werden in der gelisteten Reihenfolge durchprobiert
	 * und der erste gefundene Stream zurückgegeben. Auftretende Exceptions
	 * werden als {@link IOException} geworfen. Das bedeutet, falls für {@code source}
	 * keine gültige Ressource gefunden werden kann, wird am Ende die
	 * von {@link URL#openStream()} erzeugte {@code IOException} geworfen.
	 * <p>
	 * Bei einer Exception werden die nachfolgenden Quellen nicht mehr abgefragt.
	 * Eine {@link SecurityException} beim Öffnen des {@code FileInputStream}
	 * (zum Beispiel, weil auf eine existierende Datei keine Leserechte bestehen)
	 * verhindert daher, dass noch im Classpath gesucht wird.
	 *
	 * @param source Eine Quelle für die Ressource (Absoluter Dateipfad,
	 * 	Dateipfad im Classpath oder Netzwerkresource).
	 * @return Ein {@code InputStream} für die Ressource.
	 * @throws NullPointerException     Falls {@code source} {@code null} ist.
	 * @throws IllegalArgumentException Falls {@code source} ein leerer String
	 *                                  ist.
	 * @throws IOException              Geworfen beim Öffnen des Streams zu
	 *                                  einer bestehenden Ressource oder falls
	 *                                  keine passende Ressource gefunden
	 *                                  wurde.
	 */
	public static InputStream getInputStream( String source ) throws NullPointerException, IllegalArgumentException, IOException {
		return getResourceURL(source).openStream();
	}

	/**
	 * Sucht eine zur angegebenen Quelle passende Ressource und öffnet einen
	 * passenden {@link OutputStream}.
	 * <p>
	 * Es wird davon ausgegangen, dass die Quelle auf eine Datei verweist, auf
	 * die Schreibrechte bestehen. Ist dies nicht der Fall, wird eine
	 * {@link IOException} geworfen.
	 *
	 * @param source Eine Quelle für die Ressource (Absoluter Dateipfad,
	 * 	Dateipfad im Classpath).
	 * @return {@code OutputStream} für die Ressource.
	 * @throws IOException Falls die Quelle kein gültiges Ziel beschreibt.
	 */
	public static OutputStream getOutputStream( String source ) throws IOException {
		try {
			return Files.newOutputStream(Path.of(getResourceURL(source).toURI()));
		} catch( URISyntaxException ex ) {
			throw new IOException(ex);
		}
	}

	/**
	 * Sucht eine zur angegebenen Quelle passende Ressource und öffnet einen
	 * {@link BufferedReader}.
	 * <p>
	 * Die Suche erfolgt, wie bei {@link #getInputStream(String)} beschrieben.
	 * <p>
	 * Als {@link Charset} wird {@link StandardCharsets#UTF_8 UTF-8} verwendet.
	 *
	 * @param source Eine Quelle für die Ressource (Absoluter Dateipfad,
	 * 	Dateipfad im Classpath oder Netzwerkresource).
	 * @return Ein {@code BufferedReader} für die Quelle.
	 * @throws IOException Falls die Quelle nicht exisitert oder nicht geöffnet
	 *                     werden kann.
	 * @see #getInputStream(String)
	 */
	public static BufferedReader getReader( String source ) throws IOException {
		return getReader(source, StandardCharsets.UTF_8);
	}

	/**
	 * Sucht eine zur angegebenen Quelle passende Ressource und öffnet einen
	 * {@link BufferedReader} mit dem angegebenen Charset.
	 * <p>
	 * Die Suche erfolgt wie bei {@link #getInputStream(String)} beschrieben.
	 *
	 * @param source Eine Quelle für die Ressource (Absoluter Dateipfad,
	 * 	Dateipfad im Classpath oder Netzwerkresource).
	 * @param charset Der zu verwendende Charset.
	 * @return Ein {@code BufferedReader} für die Quelle.
	 * @throws IOException Falls die Quelle nicht exisitert oder nicht geöffnet
	 *                     werden kann.
	 * @see #getInputStream(String)
	 */
	public static BufferedReader getReader( String source, Charset charset ) throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream(source), charset.newDecoder()));
	}

	/**
	 * Sucht eine zur angegebenen Quelle passende Datei und öffnet einen
	 * {@link BufferedWriter}.
	 * <p>
	 * Die Suche erfolgt wie bei {@link #getOutputStream(String)} beschrieben.
	 * <p>
	 * Als {@link Charset} wird {@link StandardCharsets#UTF_8 UTF-8} verwendet.
	 *
	 * @param source Eine Quelle für die Ressource (Absoluter Dateipfad,
	 * 	Dateipfad im Classpath oder Netzwerkresource).
	 * @return Ein {@code BufferedWriter} für die Quelle.
	 * @throws IOException Falls die Quelle nicht exisitert oder nicht geöffnet
	 *                     werden kann.
	 * @see #getOutputStream(String)
	 */
	public static BufferedWriter getWriter( String source ) throws IOException {
		return getWriter(source, StandardCharsets.UTF_8);
	}

	/**
	 * Sucht eine zur angegebenen Quelle passende Datei und öffnet einen
	 * {@link BufferedWriter}.
	 * <p>
	 * Die Suche erfolgt wie bei {@link #getOutputStream(String)} beschrieben.
	 * <p>
	 * Als {@link Charset} wird {@link StandardCharsets#UTF_8 UTF-8} verwendet.
	 *
	 * @param source Eine Quelle für die Ressource (Absoluter Dateipfad,
	 * 	Dateipfad im Classpath oder Netzwerkresource).
	 * @return Ein {@code BufferedWriter} für die Quelle.
	 * @param charset Der zu verwendende Charset.
	 * @throws IOException Falls die Quelle nicht exisitert oder nicht geöffnet
	 *                     werden kann.
	 * @see #getOutputStream(String)
	 */
	public static BufferedWriter getWriter( String source, Charset charset ) throws IOException {
		return new BufferedWriter(new OutputStreamWriter(getOutputStream(source), charset.newEncoder()));
	}

	// Privater C'tor
	private ResourceStreamProvider() {
	}


	private static final Log LOG = Log.getLogger(ResourceStreamProvider.class);

}
