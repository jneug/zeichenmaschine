package schule.ngb.zm.util.io;

import schule.ngb.zm.Zeichenmaschine;
import schule.ngb.zm.util.Log;
import schule.ngb.zm.util.Validator;

import java.io.*;
import java.net.URL;

/**
 * Helferklasse, um {@link InputStream}s für Resourcen zu erhalten.
 */
public class ResourceStreamProvider {

	/**
	 * Sucht eine zur angegebenen Quelle passende Resource und öffnet einen
	 * passenden {@link InputStream}. Die konkrete Art des Streams hängt davon
	 * ab, welche Art an Resource gefunden wird:
	 * <ol>
	 * <li>Ist {@code source} eine existierende Datei
	 *    {@code new File(source}.isFile() == true}, dann wird ein
	 *    {@link FileInputStream} erstellt.</li>
	 * <li>Ist {@code source} ein relativer Pfad im Projekt, wird ein passender
	 * 	Stream mit {@link Class#getResourceAsStream(String)} geöffnet.</li>
	 * <li>Ist {@code source} im Classpath enthalten, wird ein passender Stream
	 * 	mit {@link ClassLoader#getResourceAsStream(String)} geöffnet.</li>
	 * <li>Ist {@code source}  eine gültige {@link URL}, dann wird ein
	 *    {@link URL#openStream() Netwerkstream} geöffnet.</li>
	 * </ol>
	 * <p>
	 * Die Möglichen Resourcen werden in der gelisteten Reihenfolge durchprobiert
	 * und der erste gefundene Stream zurückgegeben. Auftretende Exceptions
	 * werden als {@link IOException} geworfen. Das bedeutet, falls für {@code source}
	 * keine gültige Resource gefunden werden kann, wird am Ende die
	 * von {@link URL#openStream()} erzeugte {@code IOException} geworfen.
	 * <p>
	 * Bei einer Exception werden die folgenden Quellen nicht mehr abgefragt.
	 * Eine {@link SecurityException} beim Öffnen des {@code FileInputStream}
	 * (zum Beispiel, weil auf eine existierende Datei keine Leserechte bestehen)
	 * verhindert daher, dass noch im Classpath gesucht wird.
	 *
	 * @param source Eine Quelle für die Resource (Absoluter Dateipfad,
	 * 	Dateipfad im Classpath oder Netzwerkresource)
	 * @return Ein {@code InputStream} für die Resource
	 * @throws NullPointerException     Falls {@code source} {@code null} ist.
	 * @throws IllegalArgumentException Falls {@code source} ein leerer String
	 *                                  ist.
	 * @throws IOException              Geworfen beim öffnen des Streams zu
	 *                                  einer bestehenden Resource oder falls
	 *                                  keine passende Resource gefunden wurde.
	 */
	public static InputStream getInputStream( String source ) throws NullPointerException, IllegalArgumentException, IOException {
		Validator.requireNotNull(source, "Resource source may not be null");
		Validator.requireNotEmpty(source, "Resource source may not be empty.");

		InputStream in = null;

		// See if source is a readable file
		File file = new File(source);
		try {
			if( file.isFile() ) {
				in = new FileInputStream(file);
			}
		} catch( FileNotFoundException fnfex ) {
			// Somehow an exception occurred, but we still try other sources
		}
		// File does not exist, try other means
		// load ressource relative to .class-file
		if( in == null ) {
			in = Zeichenmaschine.class.getResourceAsStream(source);
		}

		// relative to ClassLoader
		if( in == null ) {
			in = Zeichenmaschine.class.getClassLoader().getResourceAsStream(source);
		}

		// load form web or jar-file
		if( in == null ) {
			in = new URL(source).openStream();
		}

		// One of the above got a valid Stream,
		// otherwise an Exception was thrown
		return in;
	}

	public static InputStream getInputStream( File file ) throws IOException {
		Validator.requireNotNull(file, "Provided file can't be null.");
		return new FileInputStream(file);
	}

	public static InputStream getInputStream( URL url ) throws IOException {
		Validator.requireNotNull(url, "Provided URL can't be null.");
		return url.openStream();
	}

	/**
	 * Ermittelt zur angegebenen Quelle einen passenden {@link URL} (<em>Unified
	 * Resource Locator</em>). Eine passende Datei-Resource wird wie folgt
	 * ermittelt:
	 * <ol>
	 * <li>Ist {@code source} eine existierende Datei
	 *    ({@code new File(source}.isFile() == true})?</li>
	 * <li>Ist {@code source} ein relativer Pfad im Projekt ({@code getResource(source) != null})?.</li>
	 * <li>Ist {@code source} im Classpath enthalten ({@code getClassLoader().getResource(source) != null})?</li>
	 * <li>Ansonten erstellt ein {@link URL}-Objekt.</li>
	 * </ol>
	 * <p>
	 * Ein {@code URL} für die erste gefundene Resource wird zurückgegeben.
	 * Auftretende Exceptions
	 * werden als {@link IOException} geworfen.
	 * <p>
	 * Bei einer Exception werden die folgenden Quellen nicht mehr abgefragt.
	 * Eine {@link java.net.MalformedURLException} beim Konstruieren des {@code URL}
	 * zu einer Datei verhindert daher, dass noch im Classpath gesucht wird.
	 *
	 * @param source Eine Quelle für die Resource (Absoluter Dateipfad,
	 * 	Dateipfad im Classpath oder Netzwerkresource)
	 * @return Ein {@code InputStream} für die Resource
	 * @throws NullPointerException     Falls {@code source} {@code null} ist.
	 * @throws IllegalArgumentException Falls {@code source} ein leerer String
	 *                                  ist.
	 * @throws IOException              Geworfen beim erzeugen eines URL zu
	 *                                  einer bestehenden Resource.
	 */
	public static URL getResourceURL( String source ) throws NullPointerException, IllegalArgumentException, IOException {
		Validator.requireNotNull(source, "Resource source may not be null");
		Validator.requireNotEmpty(source, "Resource source may not be empty.");

		File file = new File(source);
		if( file.isFile() ) {
			return file.toURI().toURL();
		}

		URL url;

		url = Zeichenmaschine.class.getResource(source);
		if( url != null ) {
			return url;
		}

		url = Zeichenmaschine.class.getClassLoader().getResource(source);
		if( url != null ) {
			return url;
		}

		return new URL(source);
	}

	/**
	 * Ver
	 *
	 * @param source
	 * @return
	 * @throws IOException
	 */
	public static OutputStream getOutputStream( String source ) throws IOException {
		Validator.requireNotNull(source, "Resource source may not be null");
		Validator.requireNotEmpty(source, "Resource source may not be empty.");
		URL url = getResourceURL(source);
		return getOutputStream(new File(url.getPath()));
	}

	public static OutputStream getOutputStream( File file ) throws IOException {
		Validator.requireNotNull(file, "Provided file can't be null.");
		return new FileOutputStream(file);
	}

	public static Reader getReader( String source ) throws IOException {
		return new InputStreamReader(getInputStream(source));
	}

	public static Writer getWriter( String source ) throws IOException {
		return new OutputStreamWriter(getOutputStream(source));
	}

	private ResourceStreamProvider() {
	}


	private static final Log LOG = Log.getLogger(ResourceStreamProvider.class);

}
