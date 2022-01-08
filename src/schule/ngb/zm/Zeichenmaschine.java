package schule.ngb.zm;

import schule.ngb.zm.shapes.ShapesLayer;
import schule.ngb.zm.util.ImageLoader;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Hauptklasse der Zeichenmaschine.
 * <p>
 * Projekte der Zeichenmaschine sollten als Unterklasse implementiert werden.
 * Die Klasse übernimmt die Initialisierung eines Programmfensters und der
 * nötigen Komponenten.
 */
public class Zeichenmaschine extends Constants implements MouseInputListener, KeyListener {

	/**
	 * Gibt an, ob die Zeichenmaschine aus BlueJ heraus gestartet wurde.
	 */
	public static boolean IN_BLUEJ;

	static {
		IN_BLUEJ = System.getProperty("java.class.path").contains("bluej");
	}

	/*
	 * Objektvariablen, die von Unterklassen benutzt werden können.
	 */

	/**
	 * Die Leinwand, auf die alles gezeichnet wird. Die Leinwand enthält
	 * {@link Layer Ebenen}, die einzelne Zeichnungen enthalten. Die Inhalte
	 * aller Ebenen werden einmal pro Frame auf die Hauptleinwand übertragen.
	 */
	protected Zeichenleinwand canvas;

	/**
	 * Ebene mit der Hintergrundfarbe.
	 */
	protected ColorLayer background;

	/**
	 * Zeichenebene
	 */
	protected DrawingLayer drawing;

	/**
	 * Formenebene
	 */
	protected ShapesLayer shapes;

	/**
	 * Anzahl der Ticks (Frames), die das Programm bisher läuft.
	 */
	protected int tick = 0;

	/**
	 * Die Zeit in Millisekunden, die das Programm seit seinem Start läuft.
	 */
	protected long runtime = 0L;

	/**
	 * Der Zeitunterschied zum letzten Frame in Sekunden.
	 */
	protected double delta = 0.0;

	/**
	 * Die aktuelle {@code x}-Koordinate der Maus.
	 */
	protected double mouseX = 0.0;

	/**
	 * Die aktuelle {@code y}-Koordinate der Maus.
	 */
	protected double mouseY = 0.0;

	/**
	 * Die letzte {@code x}-Koordinate der Maus (wird einmal pro Frame
	 * aktualisiert).
	 */
	protected double pmouseX = 0.0;

	/**
	 * Die letzte {@code y}-Koordinate der Maus (wird einmal pro Frame
	 * aktualisiert).
	 */
	protected double pmouseY = 0.0;

	/**
	 * Gibt an, ob ein Mausknopf derzeit gedrückt ist.
	 */
	protected boolean mousePressed = false;

	/**
	 * Der aktuell gedrückte Mausknopf. Die Mausknöpfe werden durch die Konstanten
	 * {@link #NOBUTTON}, {@link #BUTTON1}, {@link #BUTTON2} und {@link #BUTTON3}
	 * angegeben. (Sie stimmen mit den Konstanten in {@link MouseEvent} überein.
	 *
	 * @see MouseEvent
	 */
	protected int mouseButton = NOBUTTON;

	/**
	 * Das zuletzt ausgelöste {@code MouseEvent}.
	 */
	protected MouseEvent mouseEvent;

	/**
	 * Gibt an, ob derzeit eine Taste gedrückt ist.
	 */
	protected boolean keyPressed = false;

	/**
	 * Das Zeichen der zuletzt gedrückten Taste.
	 */
	protected char key = ' ';

	/**
	 * Der Tastencode der zuletzt gedrückten Taste. Die Keycodes können in der
	 * Klasse {@link KeyEvent} nachgesehen werden. (Zum Beispiel
	 * {@link KeyEvent#VK_ENTER}.)
	 */
	protected int keyCode = 0;

	/**
	 * Das zuletzt ausgelöste {@link KeyEvent}.
	 */
	protected KeyEvent keyEvent;

	/**
	 * Die Höhe der Zeichenleinwand.
	 */
	protected int width;

	/**
	 * Die Breite der Zeichenleinwand.
	 */
	protected int height;

	/**
	 * Die Breite des Bildschirms, auf dem das Zeichenfenster geöffnet wurde.
	 * <p>
	 * Beachte, dass sich die Breite nicht anpasst, wenn das Zeichenfenster auf
	 * einen anderen Bildschirm verschoben wird.
	 */
	protected int screenWidth;

	/**
	 * Die Höhe des Bildschirms, auf dem das Zeichenfenster geöffnet wurde.
	 * <p>
	 * Beachte, dass sich die Höhe nicht anpasst, wenn das Zeichenfenster auf
	 * einen anderen Bildschirm verschoben wird.
	 */
	protected int screenHeight;


	/*
	 * Interne Attribute zur Steuerung der Zeichenmaschine.
	 */
	//@formatter:off
	// Das Zeichenfenster der Zeichenmaschine
	private JFrame frame;
	// Die Graphics-Objekte für das aktuelle Fenster.
	private GraphicsEnvironment environment;
	private GraphicsDevice displayDevice;

	// Aktueller Zustand der Zeichenmaschine.
	private Options.AppState state = Options.AppState.INITIALIZING;
	private boolean running = false;

	// Aktuelle Frames pro Sekunde der Zeichenmaschine.
	private int framesPerSecond;

	// Hauptthread der Zeichenmaschine.
	private Thread mainThread;

	// Gibt an, ob nach Ende des Hauptthreads das Programm beendet werden soll,
	// oder das Zeichenfenster weiter geöffnet bleibt.
	private boolean quitAfterTeardown = false;
	//@formatter:on


	/**
	 * Erstellt eine neue Zeichenmaschine.
	 */
	public Zeichenmaschine() {
		this(APP_NAME + " " + APP_VERSION);
	}

	/**
	 * Erstellt eine neue Zeichenmaschine mit dem angegebene Titel.
	 *
	 * @param title Der Titel, der oben im Fenster steht.
	 */
	public Zeichenmaschine( String title ) {
		this(STD_WIDTH, STD_HEIGHT, title);
	}

	/**
	 * Erstellt eine neue zeichenmaschine mit einer Leinwand der angebenen
	 * Größe und dem angegebenen Titel.
	 *
	 * @param width  Breite der {@link Zeichenleinwand Zeichenleinwand}.
	 * @param height Höhe der {@link Zeichenleinwand Zeichenleinwand}.
	 * @param title  Der Titel, der oben im Fenster steht.
	 */
	public Zeichenmaschine( int width, int height, String title ) {
		// Setzen des "Look&Feel"
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch( Exception e ) {
			System.err.println("Error setting the look and feel: " + e.getMessage());
		}

		// Wir suchen den Bildschirm, der derzeit den Mauszeiger enthält, um
		// das Zeichenfenster dort zu zentrieren.
		java.awt.Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
		environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = environment.getScreenDevices();
		for( GraphicsDevice gd : devices ) {
			if( gd.getDefaultConfiguration().getBounds().contains(mouseLoc) ) {
				displayDevice = gd;
				break;
			}
		}
		// Keinen passenden Bildschirm gefunden. Wir nutzen den Standard.
		if( displayDevice == null ) {
			displayDevice = environment.getDefaultScreenDevice();
		}

		// Wir kennen nun den Bildschirm und können die Breite / Höhe abrufen.
		this.width = width;
		this.height = height;
		java.awt.Rectangle displayBounds = displayDevice.getDefaultConfiguration().getBounds();
		this.screenWidth = (int) displayBounds.getWidth();
		this.screenHeight = (int) displayBounds.getHeight();

		// Erstellen des Zeichenfensters
		frame = new JFrame(displayDevice.getDefaultConfiguration());
		frame.setTitle(title);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// Erstellen der Leinwand
		canvas = new Zeichenleinwand(width, height);
		frame.add(canvas);

		// Die drei Standardebenen merken, für den einfachen Zugriff aus unterklassen.
		background = getBackgroundLayer();
		drawing = getDrawingLayer();
		shapes = getShapesLayer();

		// FPS setzen
		framesPerSecond = STD_FPS;

		// Settings der Unterklasse aufrufen, falls das Fenster vor dem Öffnen
		// verändert werden soll.
		// TODO: When to call settings?
		settings();

		// Listener hinzufügen, um auf Maus- und Tastatureingaben zu hören.
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addKeyListener(this);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing( WindowEvent e ) {
				if( running ) {
					running = false;
					quitAfterTeardown = true;
				} else {
					quit();
				}
			}
		});

		// Fenster zusammenbauen, auf dem Bildschirm zentrieren ...
		frame.pack();
		frame.setResizable(false);
		centerFrame();
		// ... und anzeigen!
		frame.setVisible(true);

		// Nach dem Anzeigen kann die Pufferstrategie erstellt werden.
		canvas.allocateBuffer();

		// Erstellen des Haupt-Zeichenthreads.
		running = true;
		mainThread = new Zeichenthread();

		//frame.requestFocusInWindow();
		canvas.requestFocus();

		// Fertig mit der Initialisierung!
		state = Options.AppState.INITIALIZED;
		// Los geht's ...
		mainThread.start();
	}

	/**
	 * Erstellt ein neues Zeichenfesnter mit der aktuellen Konfiguration.
	 *
	 * @param title
	 */
	// TODO: Implement in conjunction with Zeichenfenster
	private final Zeichenfenster createFrame( String title ) {
		return null;
	}

	/**
	 * Zentriert das Zeichenfenster auf dem aktuellen Bildschirm.
	 */
	public final void centerFrame() {
		// TODO: Center on current display (not main display by default)
		// TODO: Position at current BlueJ windows if IN_BLUEJ
		//frame.setLocationRelativeTo(null);
		//frame.setLocationRelativeTo(displayDevice.getFullScreenWindow());

		java.awt.Rectangle bounds = displayDevice.getDefaultConfiguration().getBounds();
		frame.setLocation(
			(int) (bounds.x + (screenWidth - frame.getWidth()) / 2.0),
			(int) (bounds.y + (screenHeight - frame.getHeight()) / 2.0)
		);
	}

	/**
	 * Zeigt das Zeichenfenster an.
	 *
	 * @see JFrame#setVisible(boolean)
	 */
	public void show() {
		if( !frame.isVisible() ) {
			frame.setVisible(true);
		}
	}

	/**
	 * Versteckt das Zeichenfenster.
	 *
	 * @see JFrame#setVisible(boolean)
	 */
	public void hide() {
		if( frame.isVisible() ) {
			frame.setVisible(false);
		}
	}

	/**
	 * Zeichnet die {@link Zeichenleinwand} neu und zeigt den aktuellen Inhalt
	 * im Zeichenfenster an.
	 */
	public final void redraw() {
		canvas.render();
		//canvas.invalidate();
		//frame.repaint();
		//hide();
		//show();
	}

	/**
	 * Stoppt den Zeichenthread. Nachdem der aktuelle Frame gezeichnet wurde
	 * wird {@link #teardown()} aufgerufen. Das Programm wird nicht beendet und
	 * das Zeichenfenster bleibt weiter angezeigt.
	 */
	public final void stop() {
		running = false;
	}

	/**
	 * Beendet das Programm.
	 */
	public void quit() {
		//quit(!IN_BLUEJ);
		quit(true);
	}

	/**
	 * Beendet das Programm. Falls <var>exit</var> gleich {@code true} ist,
	 * wird die komplette Virtuelle Maschine beendet.
	 *
	 * @param exit Ob die VM beendet werden soll.
	 * @see System#exit(int)
	 */
	public void quit( boolean exit ) {
		frame.setVisible(false);
		canvas.dispose();
		frame.dispose();

		if( exit ) {
			System.exit(0);
		}
	}

	/**
	 * Setzte die Größe der {@link Zeichenleinwand}.
	 *
	 * @param width
	 * @param height
	 */
	public final void setSize( int width, int height ) {
		//frame.setSize(width, height);

		if( canvas != null ) {
			canvas.setSize(width, height);
		}
		this.width = width;
		this.height = height;

		frame.pack();
	}

	/**
	 * Die Breite der {@link Zeichenleinwand}.
	 *
	 * @return Die Breite der {@link Zeichenleinwand}.
	 */
	public final int getWidth() {
		return width;
	}

	/**
	 * Die Höhe der {@link Zeichenleinwand}.
	 *
	 * @return Die Höhe der {@link Zeichenleinwand}.
	 */
	public final int getHeight() {
		return height;
	}

	/**
	 * Setzt den Titel des Zeichenfensters.
	 *
	 * @param title Der Titel, der oben im Zeichenfenster angezeigt wird.
	 */
	public final void setTitle( String title ) {
		frame.setTitle(title);
	}

	/**
	 * Gibt die Zeichenleinwand zurück.
	 *
	 * @return Die Zeichenleinwand.
	 */
	public final Zeichenleinwand getCanvas() {
		return canvas;
	}

	/**
	 * Fügt der {@link Zeichenleinwand} eine weitere {@link Layer Ebene}
	 * hinzu.
	 *
	 * @param layer Die neue Ebene.
	 */
	public final void addLayer( Layer layer ) {
		canvas.addLayer(layer);
		layer.setSize(getWidth(), getHeight());
	}

	/**
	 * Gibt die Anzahl der {@link Layer Ebenen} in der {@link Zeichenleinwand}
	 * zurück.
	 *
	 * @return Die Anzahl der Ebenen.
	 */
	public int getLayerCount() {
		return canvas.getLayerCount();
	}

	/**
	 * Gibt die {@link Layer Ebene} am angegebenen Index zurück. Gibt es keine
	 * Ebene mit diesem Index.
	 *
	 * @param index
	 * @return
	 */
	public final Layer getLayer( int index ) {
		return canvas.getLayer(index);
	}

	/**
	 * Gibt die erste (unterste) {@link Layer Ebene} der angegebenen Klasse
	 * zurück.
	 *
	 * <pre>
	 *     DrawingLayer draw = getLayer(DrawingLayer.class);
	 * </pre>
	 *
	 * @param layerClass
	 * @param <LT>
	 * @return
	 */
	public final <LT extends Layer> LT getLayer( Class<LT> layerClass ) {
		return canvas.getLayer(layerClass);
	}

	/**
	 * Gibt die {@link ColorLayer Ebene} mit der Hintergrundfarbe zurück. Gibt es
	 * keine solche Ebene, so wird eine erstellt und der {@link Zeichenleinwand}
	 * hinzugefügt.
	 * <p>
	 * In der Regel sollte dies dieselbe Ebene sein wie {@link #background}.
	 *
	 * @return Die Hintergrundebene.
	 */
	public final ColorLayer getBackgroundLayer() {
		ColorLayer layer = canvas.getLayer(ColorLayer.class);
		if( layer == null ) {
			layer = new ColorLayer(STD_BACKGROUND);
			canvas.addLayer(0, layer);
		}
		return layer;
	}

	/**
	 * Gibt die Standard-{@link DrawingLayer Zeichenebene} zurück. Gibt es
	 * keine solche Ebene, so wird eine erstellt und der {@link Zeichenleinwand}
	 * hinzugefügt.
	 * <p>
	 * In der Regel sollte dies dieselbe Ebene sein wie {@link #drawing}.
	 *
	 * @return Die Zeichenebene.
	 */
	public final DrawingLayer getDrawingLayer() {
		DrawingLayer layer = canvas.getLayer(DrawingLayer.class);
		if( layer == null ) {
			layer = new DrawingLayer(getWidth(), getHeight());
			canvas.addLayer(1, layer);
		}
		return layer;
	}

	/**
	 * Gibt die Standard-{@link ShapesLayer Formenebene} zurück. Gibt es
	 * keine solche Ebene, so wird eine erstellt und der {@link Zeichenleinwand}
	 * hinzugefügt.
	 * <p>
	 * In der Regel sollte dies dieselbe Ebene sein wie {@link #shapes}.
	 *
	 * @return Die Formenebene.
	 */
	public final ShapesLayer getShapesLayer() {
		ShapesLayer layer = canvas.getLayer(ShapesLayer.class);
		if( layer == null ) {
			layer = new ShapesLayer(getWidth(), getHeight());
			canvas.addLayer(layer);
		}
		return layer;
	}

	/**
	 * Gibt die aktuellen Frames pro Sekunde zurück.
	 *
	 * @return Angepeilte Frames pro Sekunde
	 */
	public final int getFramesPerSecond() {
		return framesPerSecond;
	}

	/**
	 * Setzt die Anzahl an Frames pro Sekunde auf einen neuen Wert.
	 *
	 * @param pFramesPerSecond Neue FPS.
	 */
	public final void setFramesPerSecond( int pFramesPerSecond ) {
		framesPerSecond = pFramesPerSecond;
	}

	/**
	 * Speichert den aktuellen Inhalt der {@link Zeichenleinwand} in einer
	 * Bilddatei auf der Festplatte. Zur Auswahl der Zieldatei wird dem Nutzer
	 * ein {@link JFileChooser} angezeigt.
	 */
	public void saveImage() {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jfc.setMultiSelectionEnabled(false);

		int status = jfc.showSaveDialog(frame);
		if( status == JFileChooser.APPROVE_OPTION ) {
			File outfile = jfc.getSelectedFile();
			if( outfile.isDirectory() ) {
				outfile = new File(outfile.getAbsolutePath() + File.separator + "zeichenmaschine.png");
			}
			saveImage(outfile.getAbsolutePath());
		}
	}

	/**
	 * Speichert den aktuellen Inhalt der {@link Zeichenleinwand} in einer
	 * Bilddatei im angegebenen Dateipfad auf der Festplatte.
	 */
	public void saveImage( String filepath ) {
		BufferedImage img = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);

		Graphics2D g = img.createGraphics();
		g.setColor(STD_BACKGROUND.getColor());
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		canvas.draw(g);
		g.dispose();

		try {
			ImageLoader.saveImage(img, new File(filepath), true);
		} catch( IOException ex ) {
			ex.printStackTrace();
		}
	}

	/**
	 * Erstellt eine Momentanaufnahme des aktuellen Inhalts der
	 * {@link Zeichenleinwand} und erstellt daraus eine {@link ImageLayer Bildebene}.
	 * Die Ebene wird automatisch der {@link Zeichenleinwand} vor dem
	 * {@link #background} hinzugefügt.
	 *
	 * @return Die neue Bildebene.
	 */
	public ImageLayer snapshot() {
		BufferedImage img = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);

		Graphics2D g = img.createGraphics();
		g.setColor(STD_BACKGROUND.getColor());
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		canvas.draw(g);
		g.dispose();

		/*
		float factor = 0.8f;
		float base = 255f * (1f - factor);
		RescaleOp op = new RescaleOp(factor, base, null);
		BufferedImage filteredImage
			= new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		op.filter(img, filteredImage);
		*/

		ImageLayer imgLayer = new ImageLayer(img);
		if( canvas.getLayer(0) instanceof ColorLayer ) {
			canvas.addLayer(1, imgLayer);
		} else {
			canvas.addLayer(0, imgLayer);
		}
		return imgLayer;
	}

	/**
	 * Pausiert die Schleife der Zeichenmaschine für die angegebene Anzahl an
	 * Millisekunden.
	 *
	 * @param ms Schlafenszeit in Millisekunden.
	 */
	public final void delay( int ms ) {
		if( ms <= 0 ) {
			return;
		}

		long timer = 0L;
		if( state == Options.AppState.DRAWING ) {
			// Falls gerade draw() ausgeführt wird, zeigen wir den aktuellen
			// Stand der Zeichnung auf der Leinwand an. Die Zeit für das
			// Rendern wird gemessen und von der Wartezeit abgezogen.
			timer = System.nanoTime();
			canvas.render();
			timer = System.nanoTime() - timer;
		}

		try {
			int sub = (int) Math.ceil(timer / 1000000.0);

			if( sub >= ms ) {
				return;
			}

			Thread.sleep(ms - sub, (int) (timer % 1000000L));
		} catch( InterruptedException ex ) {
			// Nothing
		}
	}

	/*
	 * Methoden, die von Unterklassen überschrieben werden können / sollen.
	 */

	/**
	 * Die Settings werden einmal beim Erstellten der Zeichenmaschine aufgerufen.
	 * <p>
	 * {@code settings()} wird nur selten benötigt, wenn das Zeichenfenster
	 */
	public void settings() {
		// Intentionally left blank
	}

	/**
	 * Methode, die von Unterklassen überschrieben werden sollte, um die
	 * Zeichenmaschine vor dem Start zu konfigurieren. Hier können vorbereitende
	 * Befehle ausgeführt werden, die die {@link Zeichenleinwand} zu
	 * initialisieren, neue Objekte instanziieren und Variablen initialisieren.
	 */
	public void setup() {
		// Intentionally left blank
	}

	/**
	 * {@code update()} wird einmal pro Frame vor {@link #draw()} aufgerufen, um
	 * notwendige Aktualisierungen vorzunehmen. Im Gegensatz zu {@link #draw()}
	 * bekommt {@code update()} zusätzlich {@link #delta} übergeben, um die
	 * Aktualisierungen abhängig von der echten Verzögerung zwischen zwei Frames
	 * zu berechnen.
	 * <p>
	 * {@code delta} wird in Sekunden angegeben. Um eine Form zum Beispiel
	 * um {@code 50} Pixel pro Sekunde in {@code x}-Richtung zu bewegen,
	 * kann so vorgegangen werden:
	 * <pre>
	 * shape.move(50*delta, 0.0);
	 * </pre>
	 *
	 * @param delta
	 */
	public void update( double delta ) {
		running = false;
	}

	/**
	 * {@code draw()} wird einmal pro Frame aufgerufen. Bei einer
	 * {@link #getFramesPerSecond() Framerate} von {@code 60} also in etwa 60-Mal
	 * pro Sekunde. In der {@code draw}-Methode wird der Inhalt der Ebenen
	 * manipuliert und deren Inhalte gezeichnet. Am Ende des Frames werden alle
	 * Ebenen auf die {@link Zeichenleinwand} übertragen.
	 * <p>
	 * {@code draw()} stellt die wichtigste Methode für eine Zeichenmaschine dar,
	 * da hier die Zeichnung des Programms erstellt wird.
	 */
	public void draw() {
		// Intentionally left blank
	}

	/**
	 * {@code teardown()} wird aufgerufen, sobald die Schleife des Hauptprogramms
	 * beendet wurde. Dies passiert entweder nach dem ersten Durchlauf (wenn keine
	 * eigene {@link #update(double)} erstellt wurde), nach dem Aufruf von
	 * {@link #stop()} oder nachdem das {@link Zeichenfenster} geschlossen wurde.
	 * <p>
	 * In {@code teardown()} kann zum Beispiel der Abschlussbildschirm eines
	 * Spiels oder der Abspann einer Animation angezeigt werden, oder mit
	 * {@link #saveImage()} die erstellte Zeichnung abgespeichert werden.
	 */
	public void teardown() {
		// Intentionally left blank
	}

	/*
	 * Mouse handling
	 */
	@Override
	public final void mouseClicked( MouseEvent e ) {
		mouseEvent = e;
		mouseClicked();
	}

	public void mouseClicked() {
		// Intentionally left blank
	}

	@Override
	public final void mousePressed( MouseEvent e ) {
		mouseEvent = e;
		mousePressed = true;
		mouseButton = e.getButton();
		mousePressed();
	}

	public void mousePressed() {
		// Intentionally left blank
	}

	@Override
	public final void mouseReleased( MouseEvent e ) {
		mouseEvent = e;
		mousePressed = false;
		mouseButton = NOBUTTON;
		mouseReleased();
	}

	public void mouseReleased() {
		// Intentionally left blank
	}

	@Override
	public final void mouseEntered( MouseEvent e ) {
		// Intentionally left blank
	}

	@Override
	public final void mouseExited( MouseEvent e ) {
		// Intentionally left blank
	}

	@Override
	public final void mouseDragged( MouseEvent e ) {
		mouseEvent = e;
		mouseDragged();
	}

	public void mouseDragged() {
		// Intentionally left blank
	}

	@Override
	public final void mouseMoved( MouseEvent e ) {
		mouseEvent = e;
		mouseMoved();
	}

	public void mouseMoved() {
		// Intentionally left blank
	}

	private void saveMousePosition( MouseEvent event ) {
		if( mouseEvent != null && event.getComponent() == canvas ) {
			pmouseX = mouseX;
			pmouseY = mouseY;

			mouseX = event.getX();
			mouseY = event.getY();
		}
	}

	private void saveMousePosition() {
		pmouseX = mouseX;
		pmouseY = mouseY;

		// Calculates mouse position based on screen, not based on canvas
		java.awt.Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
		java.awt.Point compLoc = canvas.getLocationOnScreen();
		mouseX = mouseLoc.x - compLoc.x;
		mouseY = mouseLoc.y - compLoc.y;
	}

	/*
	 * Keyboard handling
	 */
	@Override
	public final void keyTyped( KeyEvent e ) {
		saveKeys(e);
		keyTyped();
	}

	public void keyTyped() {
		// Intentionally left blank
	}

	@Override
	public final void keyPressed( KeyEvent e ) {
		saveKeys(e);
		keyPressed = true;
		keyPressed();
	}

	public void keyPressed() {
		// Intentionally left blank
	}

	@Override
	public final void keyReleased( KeyEvent e ) {
		saveKeys(e);
		keyPressed = false;
		keyReleased();
	}

	public void keyReleased() {
		// Intentionally left blank
	}

	private final void saveKeys( KeyEvent event ) {
		keyEvent = event;
		key = event.getKeyChar();
		keyCode = event.getKeyCode();
	}

	class Zeichenthread extends Thread {

		@Override
		public final void run() {
			// Wait for full initialization before start
			while( state != Options.AppState.INITIALIZED ) {
				delay(1);
			}

			// start of thread in ms
			final long start = System.currentTimeMillis();
			// current time in ns
			long beforeTime = System.nanoTime();
			// store for deltas
			long overslept = 0L;
			// internal counters for tick and runtime
			int _tick = 0;
			long _runtime = 0;
			// public counters for access by subclasses
			tick = 0;
			runtime = 0;

			// call setup of subclass
			setup();

			state = Options.AppState.RUNNING;
			while( running ) {
				// delta in seconds
				delta = (System.nanoTime() - beforeTime) / 1000000000.0;
				beforeTime = System.nanoTime();

				saveMousePosition(mouseEvent);

				handleUpdate(delta);
				handleDraw();

				if( canvas != null ) {
					canvas.render();
					//canvas.invalidate();
					//frame.repaint();
				}

				// delta time in ns
				long afterTime = System.nanoTime();
				long dt = afterTime - beforeTime;
				long sleep = ((1000000000L / framesPerSecond) - dt) - overslept;


				if( sleep > 0 ) {
					try {
						Thread.sleep(sleep / 1000000L, (int) (sleep % 1000000L));
					} catch( InterruptedException e ) {
						// Interrupt not relevant
					}

					overslept = (System.nanoTime() - afterTime) - sleep;
				} else {
					overslept = 0L;

				}

				_tick += 1;
				_runtime = System.currentTimeMillis() - start;
				tick = _tick;
				runtime = _runtime;
			}
			state = Options.AppState.STOPPED;

			teardown();
			state = Options.AppState.TERMINATED;

			if( quitAfterTeardown ) {
				quit();
			}
		}

		public void handleUpdate( double delta ) {
			if( state == Options.AppState.RUNNING ) {
				state = Options.AppState.UPDATING;
				update(delta);
				state = Options.AppState.RUNNING;
			}
		}

		public void handleDraw() {
			if( state == Options.AppState.RUNNING ) {
				state = Options.AppState.DRAWING;
				draw();
				state = Options.AppState.RUNNING;
			}
		}

	}

}
