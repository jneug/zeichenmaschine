package schule.ngb.zm;

import schule.ngb.zm.shapes.ShapesLayer;
import schule.ngb.zm.util.ImageLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

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
	protected static ColorLayer background;

	/**
	 * Zeichenebene
	 */
	protected static DrawingLayer drawing;

	/**
	 * Formenebene
	 */
	protected static ShapesLayer shapes;

	////////


	/*
	 * Interne Attribute zur Steuerung der Zeichenmaschine.
	 */
	//@formatter:off
	// Das Zeichenfenster der Zeichenmaschine
	private JFrame frame;
	// Die Graphics-Objekte für das aktuelle Fenster.
	private GraphicsEnvironment environment;
	private GraphicsDevice displayDevice;

	/**
	 * Speichert, ob die Zeichenmaschine mit {@link #setFullscreen(boolean)}
	 * in den Vollbildmodus versetzt wurde.
	 */
	private boolean fullscreen = false;

	/**
	 * Höhe und Breite der Zeichenmaschine, bevor sie mit
	 * {@link #setFullscreen(boolean)} in den Vollbild-Modus versetzt wurde.
	 * Wird verwendet, um die Fenstergröße wiederherzustellen, sobald der
	 * Vollbild-Modus verlassen wird.
	 */
	private int initialWidth, initialHeight;

	/**
	 * KeyListener, um den Vollbild-Modus mit der Escape-Taste zu verlassen.
	 * Wird von {@link #setFullscreen(boolean)} automatisch einzugefügt und
	 * entfernt.
	 */
	KeyListener fullscreenExitListener = new KeyAdapter() {
		@Override
		public void keyPressed( KeyEvent e ) {
			if( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
				// canvas.removeKeyListener(this);
				setFullscreen(false);
			}
		}
	};

	// Aktueller Zustand der Zeichenmaschine.
	private Options.AppState state = Options.AppState.INITIALIZING;
	private boolean running = false;
	private boolean pause_pending = false;

	private boolean stop_after_update = false, run_once = true;

	// Aktuelle Frames pro Sekunde der Zeichenmaschine.
	private int framesPerSecond;

	// Hauptthread der Zeichenmaschine.
	private Thread mainThread;

	// Gibt an, ob nach Ende des Hauptthreads das Programm beendet werden soll,
	// oder das Zeichenfenster weiter geöffnet bleibt.
	private boolean quitAfterTeardown = false;

	// Mauscursor
	private Cursor invisibleCursor = null;
	protected boolean cursorVisible = true;
	//@formatter:on


	/**
	 * Erstellt eine neue Zeichenmaschine mit Standardwerten für Titel und
	 * Größe.
	 * <p>
	 * Siehe {@link #Zeichenmaschine(int, int, String, boolean)} für mehr
	 * Details.
	 */
	public Zeichenmaschine() {
		this(APP_NAME + " " + APP_VERSION);
	}

	/**
	 * Erstellt eine neue Zeichenmaschine mit Standardwerten für Titel und
	 * Größe.
	 * <p>
	 * Siehe {@link #Zeichenmaschine(int, int, String, boolean)} für mehr
	 * Details.
	 *
	 * @param run_once {@code true} beendet die Zeichenmaschine nach einem
	 * 	Aufruf von {@code draw()}.
	 */
	public Zeichenmaschine( boolean run_once ) {
		this(APP_NAME + " " + APP_VERSION, run_once);
	}

	/**
	 * Erstellt eine neue Zeichenmaschine mit dem angegebene Titel und
	 * Standardwerten für die Größe.
	 * <p>
	 * Siehe {@link #Zeichenmaschine(int, int, String, boolean)} für mehr
	 * Details.
	 *
	 * @param title Der Titel, der oben im Fenster steht.
	 */
	public Zeichenmaschine( String title ) {
		this(STD_WIDTH, STD_HEIGHT, title, true);
	}

	/**
	 * Erstellt eine neue Zeichenmaschine mit dem angegebene Titel und
	 * Standardwerten für die Größe.
	 * <p>
	 * Siehe {@link #Zeichenmaschine(int, int, String, boolean)} für mehr
	 * Details.
	 *
	 * @param title Der Titel, der oben im Fenster steht.
	 * @param run_once {@code true} beendet die Zeichenmaschine nach einem
	 * 	Aufruf von {@code draw()}.
	 */
	public Zeichenmaschine( String title, boolean run_once ) {
		this(STD_WIDTH, STD_HEIGHT, title, run_once);
	}

	/**
	 * Erstellt eine neue zeichenmaschine mit einer Leinwand der angegebenen
	 * Größe und dem angegebenen Titel.
	 * <p>
	 * Siehe {@link #Zeichenmaschine(int, int, String, boolean)} für mehr
	 * Details.
	 *
	 * @param width Breite der {@link Zeichenleinwand Zeichenleinwand}.
	 * @param height Höhe der {@link Zeichenleinwand Zeichenleinwand}.
	 * @param title Der Titel, der oben im Fenster steht.
	 */
	public Zeichenmaschine( int width, int height, String title ) {
		this(width, height, title, true);
	}

	/**
	 * Erstellt eine neue zeichenmaschine mit einer Leinwand der angegebenen
	 * Größe und dem angegebenen Titel.
	 * <p>
	 * Die Zeichenmaschine öffnet automatisch ein Fenster mit einer
	 * {@link Zeichenleinwand}, die {@code width} Pixel breit und {@code height}
	 * Pixel hoch ist. Die Leinwand hat immer eine Mindestgröße von 100x100
	 * Pixeln und kann nicht größer als der aktuelle Bildschirm werden. Das
	 * Fenster bekommt den angegebenen Titel.
	 * <p>
	 * Falls {@code run_once} gleich {@code false} ist, werden
	 * {@link #update(double)} und {@link #draw()} entsprechend der
	 * {@link #framesPerSecond} kontinuierlich aufgerufen. Falls das Programm
	 * als Unterklasse der Zeichenmaschine verfasst wird, dann kann auch,
	 * {@code update(double)} überschrieben werden, damit die Maschine nicht
	 * automatisch beendet.
	 *
	 * @param width Breite der {@link Zeichenleinwand Zeichenleinwand}.
	 * @param height Höhe der {@link Zeichenleinwand Zeichenleinwand}.
	 * @param title Der Titel, der oben im Fenster steht.
	 * @param run_once {@code true} beendet die Zeichenmaschine nach einem
	 * 	Aufruf von {@code draw()}.
	 */
	public Zeichenmaschine( int width, int height, String title, boolean run_once ) {
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

		// Das Icon des Fensters ändern
		try {
			ImageIcon icon = new ImageIcon(ImageIO.read(new File("res/icon_64.png")));
			frame.setIconImage(icon.getImage());

			// Dock Icon in macOS setzen
			Taskbar taskbar = Taskbar.getTaskbar();
			taskbar.setIconImage(icon.getImage());
		} catch( IOException e ) {
		}

		// Erstellen der Leinwand
		canvas = new Zeichenleinwand(width, height);
		frame.add(canvas);

		// Die drei Standardebenen merken, für den einfachen Zugriff aus unterklassen.
		background = getBackgroundLayer();
		drawing = getDrawingLayer();
		shapes = getShapesLayer();

		// FPS setzen
		framesPerSecond = STD_FPS;
		this.run_once = run_once;

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
				exit();
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
	 * Aktiviert oder deaktiviert den Vollbildmodus für die Zeichenmaschine.
	 * <p>
	 * Der Vollbildmodus wird abhängig von {@code pEnable} entweder aktiviert
	 * oder deaktiviert. Wird die Zeichenmaschine in den Vollbildmodus versetzt,
	 * dann wird automatisch ein {@link KeyListener} aktiviert, der bei
	 * Betätigung der ESCAPE-Taste den Vollbildmodus verlässt. Wird der
	 * Vollbildmodus verlassen, wird die zuletzt gesetzte Fenstergröße
	 * wiederhergestellt.
	 *
	 * @param pEnable Wenn {@code true}, wird der Vollbildmodus aktiviert,
	 * 	ansonsten deaktiviert.
	 */
	public final void setFullscreen( boolean pEnable ) {
		// See https://docs.oracle.com/javase/tutorial/extra/fullscreen/index.html
		if( displayDevice.isFullScreenSupported() ) {
			if( pEnable && !fullscreen ) {
				// frame.setUndecorated(true);
				frame.setResizable(false); // Should be set anyway
				displayDevice.setFullScreenWindow(frame);
				// Update width / height
				initialWidth = width;
				initialHeight = height;
				setSize(screenWidth, screenHeight);
				// Register ESC as exit fullscreen
				canvas.addKeyListener(fullscreenExitListener);

				fullscreen = true;
				fullscreenChanged();
			} else if( !pEnable && fullscreen ) {
				fullscreen = false;

				canvas.removeKeyListener(fullscreenExitListener);
				displayDevice.setFullScreenWindow(null);
				setSize(initialWidth, initialHeight);
				// frame.setUndecorated(false);
				fullscreenChanged();
			}
		}
	}

	public boolean isFullscreen() {
		Window win = displayDevice.getFullScreenWindow();
		return fullscreen && win != null;
	}

	/**
	 * Gibt den aktuellen {@link Options.AppState Zustand} der Zeichenmaschine
	 * zurück.
	 *
	 * @return Der Zustand der Zeichenmaschine.
	 */
	public Options.AppState getState() {
		return state;
	}

	/**
	 * Zeigt das Zeichenfenster an.
	 */
	public final void show() {
		if( !frame.isVisible() ) {
			frame.setVisible(true);
		}
	}

	/**
	 * Versteckt das Zeichenfenster.
	 */
	public final void hide() {
		if( frame.isVisible() ) {
			frame.setVisible(false);
		}
	}

	/**
	 * Zeichnet die {@link Zeichenleinwand} neu und zeigt den aktuellen Inhalt
	 * im Zeichenfenster an.
	 */
	public final void redraw() {
		if( state == Options.AppState.PAUSED ) {
			draw();
		}
		canvas.render();
		// canvas.invalidate();
		// frame.repaint();
		// hide();
		// show();
	}

	/**
	 * Pausiert die Ausführung von {@link #update(double)} und {@link #draw()}
	 * nach dem nächsten vollständigen Frame.
	 * <p>
	 * Die Zeichenmaschine wechselt in den Zustand
	 * {@link Options.AppState#PAUSED}, sobald der aktuelle Frame beendet
	 * wurde.
	 */
	public final void pause() {
		pause_pending = true;
	}

	/**
	 * Setzt die Ausführung der Zeichenmaschine fort, nachdem diese mit
	 * {@link #pause()} pausiert wurde.
	 */
	public final void resume() {
		pause_pending = false;
		if( state == Options.AppState.PAUSED ) {
			state = Options.AppState.RUNNING;
		}
	}

	/**
	 * Prüft, ob die Zeichenmaschine gerade pausiert ist.
	 *
	 * @return
	 */
	public final boolean isPaused() {
		return state == Options.AppState.PAUSED;
	}

	/**
	 * Stoppt die Zeichenmaschine.
	 * <p>
	 * Nachdem der aktuelle Frame gezeichnet wurde wechselt die Zeichenmaschine
	 * in den Zustand {@link Options.AppState#STOPPED} und ruft
	 * {@link #teardown()} auf. Nachdem {@code teardown()} ausgeführt wurde
	 * wechselt der Zustand zu {@link Options.AppState#TERMINATED}. Das
	 * Zeichenfenster bleibt weiter geöffnet.
	 */
	public final void stop() {
		running = false;
	}

	/**
	 * Beendet die Zeichenmaschine vollständig.
	 * <p>
	 * Das Programm wird {@link #quit() beendet} und alle geöffneten Fenster
	 * geschlossen. Falls die Maschine noch läuft, wird sie zunächst nach dem
	 * nächsten vollständigen Frame {@link #stop() gestoppt}.
	 */
	public final void exit() {
		if( running ) {
			running = false;
			this.quitAfterTeardown = true;
		} else {
			quit(true);
		}
	}

	/**
	 * Beendet das Programm vollständig.
	 */
	public final void quit() {
		//quit(!IN_BLUEJ);
		quit(true);
	}

	/**
	 * Beendet das Programm. Falls {@code exit} gleich {@code true} ist, wird
	 * die komplette VM beendet.
	 *
	 * @param exit Ob die VM beendet werden soll.
	 * @see System#exit(int)
	 */
	public final void quit( boolean exit ) {
		frame.setVisible(false);
		canvas.dispose();
		frame.dispose();

		if( exit ) {
			System.exit(0);
		}
	}

	/**
	 * Interne Methode um die Größe der Zeichenfläche zu ändern.
	 * <p>
	 * Die Methode berücksichtigt nicht den Zustand des Fensters (z.B.
	 * Vollbildmodus) und geht davon aus, dass die aufrufende Methode
	 * sichergestellt hat, dass eine Änderung der Größe der Zeichenfläche
	 * zulässig und sinnvoll ist.
	 *
	 * @param width Neue Breite der Zeichenleinwand.
	 * @param height Neue Höhe der Zeichenleinwand.
	 * @see #setSize(int, int)
	 * @see #setFullscreen(boolean)
	 */
	private void changeSize( int width, int height ) {
		if( canvas != null ) {
			canvas.setSize(width, height);
		}
		this.width = Math.min(Math.max(width, 100), screenWidth);
		this.height = Math.min(Math.max(height, 100), screenHeight);
	}

	/**
	 * Ändert die Größe der {@link Zeichenleinwand}.
	 *
	 * @param width Neue Breite der Zeichenleinwand.
	 * @param height Neue Höhe der Zeichenleinwand.
	 * @see Zeichenleinwand#setSize(int, int)
	 */
	public final void setSize( int width, int height ) {
		if( fullscreen ) {
			initialWidth = Math.min(Math.max(width, 100), screenWidth);
			initialHeight = Math.min(Math.max(height, 100), screenHeight);
			setFullscreen(false);
		} else {
			changeSize(width, height);

			//frame.setSize(width, height);
			frame.pack();
		}
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
	 * Fügt der {@link Zeichenleinwand} eine weitere {@link Layer Ebene} hinzu.
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
	public final int getLayerCount() {
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
	 * Gibt die {@link ColorLayer Ebene} mit der Hintergrundfarbe zurück. Gibt
	 * es keine solche Ebene, so wird eine erstellt und der
	 * {@link Zeichenleinwand} hinzugefügt.
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
	 * Gibt die Standard-{@link DrawingLayer Zeichenebene} zurück. Gibt es keine
	 * solche Ebene, so wird eine erstellt und der {@link Zeichenleinwand}
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
	 * Gibt die Standard-{@link ShapesLayer Formenebene} zurück. Gibt es keine
	 * solche Ebene, so wird eine erstellt und der {@link Zeichenleinwand}
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
	public final void saveImage() {
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
	public final void saveImage( String filepath ) {
		BufferedImage img = ImageLoader.createImage(canvas.getWidth(), canvas.getHeight());

		Graphics2D g = img.createGraphics();
		g.setColor(STD_BACKGROUND.getJavaColor());
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
	 * {@link Zeichenleinwand} und erstellt daraus eine
	 * {@link ImageLayer Bildebene}. Die Ebene wird automatisch der
	 * {@link Zeichenleinwand} vor dem {@link #background} hinzugefügt.
	 *
	 * @return Die neue Bildebene.
	 */
	public final ImageLayer snapshot() {
		BufferedImage img = ImageLoader.createImage(canvas.getWidth(), canvas.getHeight());

		Graphics2D g = img.createGraphics();
		g.setColor(STD_BACKGROUND.getJavaColor());
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
	 * <p>
	 * Falls {@code delay()} während eines Aufrufs von {@link #draw()}
	 * aufgerufen wird, dann wird der aktuelle Zustand der Leinwand angezeigt.
	 * <p>
	 * Die Methode übernimmt keine Garantie, dass die Wartezeit exakt {@code ms}
	 * Millisekunden beträgt. Sie kann etwas kürzer oder (für kurze Wartezeiten)
	 * etwas länger sein. Für zeitkritische Simulationen sollte daher die genaue
	 * Zeitdifferenz gemessen und berücksichtigt werden.
	 *
	 * @param ms Wartezeit in Millisekunden.
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

	/**
	 * Macht den Mauszeiger unsichtbar.
	 * <p>
	 * Nach dem Aufruf gilt {@code cursorVisible == false}.
	 * <p>
	 * Der Aufruf von {@code hideCursor()} ist dasselbe wie der Aufruf von
	 * {@link #setCursor(Cursor) setCursor(null)}.
	 */
	public final void hideCursor() {
		setCursor(null);
	}

	/**
	 * Zeigt den Mauszeiger wieder an, falls er zuvor
	 * {@link #hideCursor() versteckt} wurde.
	 * <p>
	 * Nach dem Aufruf gilt {@code cursorVisible == true}.
	 * <p>
	 * Der Aufruf von {@code hideCursor()} ist dasselbe wie der Aufruf von
	 * {@link #setCursor(int) setCursor(Cursor.DEFAULT_CURSOR)}.
	 */
	public final void showCursor() {
		setCursor(Cursor.DEFAULT_CURSOR);
	}

	/**
	 * Ändert den Mauszeiger auf ein eigenes Bild.
	 * <p>
	 * Das Bild darf die vom Betriebssystem vorgegebene Mindestgröße nicht
	 * überschreiten und kann aus einer beliebigen Quelle geladen werden, oder
	 * direkt im Programm erstellt werden. Die Koordinaten des Hotspot geben an,
	 * an welcher Stelle des Bildes sich die "Spitze" befindet. Die Koordinaten
	 * werden relativ zur oberen linken Ecke des Bildes angegeben.
	 *
	 * @param pCursorImage Ein Bild, das den Mauszeiger ersetzt.
	 * @param hotSpotX Relative x-Koordinate des Hotspots.
	 * @param hotSpotY Relative y-Koordinate des Hotspots.
	 * @see ImageLoader#loadImage(String)
	 * @see Toolkit#createCustomCursor(Image, Point, String)
	 */
	public final void setCursor( Image pCursorImage, int hotSpotX, int hotSpotY ) {
		Point hotSpot = new Point(hotSpotX, hotSpotY);
		setCursor(canvas.getToolkit().createCustomCursor(pCursorImage, hotSpot, "zmCursor"));
	}

	/**
	 * Setzt den Mauszeiger auf eines der vordefinierten Symbole.
	 * <p>
	 * Die Konstanten der Klasse {@link Cursor} definieren 13 Standardzeiger,
	 * die durch angabe der Nummer geladen werden können.
	 * <pre>
	 *     setCursor(Cursor.HAND_CURSOR);
	 * </pre>
	 *
	 * @param pPredefinedCursor Eine der Cursor-Konstanten.
	 * @see Cursor
	 */
	public final void setCursor( int pPredefinedCursor ) {
		setCursor(Cursor.getPredefinedCursor(pPredefinedCursor));
	}

	/**
	 * Setzt den Mauszeiger auf das übergebenen Cursor-Objekt. Wenn
	 * {@code pCursor} {@code null} ist, wird der Mauszeiger unsichtbar gemacht
	 * (dies ist dasselbe wie der Aufruf von {@link #hideCursor()}).
	 *
	 * @param pCursor Ein Cursor-Objekt oder {@code null}.
	 * 	<p>
	 * 	Nach Aufruf der Methode kann über {@link #cursorVisible} abgefragt
	 * 	werden, ob der Cursor zurzeit sichtbar ist oder nicht.
	 */
	public final void setCursor( Cursor pCursor ) {
		if( pCursor == null && cursorVisible ) {
			// Falls null übergeben, Zeiger verstecken

			// Übernommen aus processing.awt.PSurfaceAWT von Processing4
			if( invisibleCursor == null ) {
				BufferedImage cursorImg =
					new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
				invisibleCursor =
					canvas.getToolkit().createCustomCursor(cursorImg, new Point(8, 8), "blank");
			}
			canvas.setCursor(invisibleCursor);
			cursorVisible = false;
		} else if( pCursor != null ) {
			// Zeiger neu zuweisen
			canvas.setCursor(pCursor);
			cursorVisible = true;
		}
	}

	/*
	 * Methoden, die von Unterklassen überschrieben werden können / sollen.
	 */

	/**
	 * Die Settings werden einmal beim Erstellten der Zeichenmaschine
	 * aufgerufen.
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
	 * {@code delta} wird in Sekunden angegeben. Um eine Form zum Beispiel um
	 * {@code 50} Pixel pro Sekunde in {@code x}-Richtung zu bewegen, kann so
	 * vorgegangen werden:
	 * <pre>
	 * shape.move(50*delta, 0.0);
	 * </pre>
	 *
	 * @param delta
	 */
	public void update( double delta ) {
		//running = !run_once;
		stop_after_update = run_once;
	}

	/**
	 * {@code draw()} wird einmal pro Frame aufgerufen. Bei einer
	 * {@link #getFramesPerSecond() Framerate} von 60 also in etwa 60-Mal pro
	 * Sekunde. In der {@code draw}-Methode wird der Inhalt der Ebenen
	 * manipuliert und deren Inhalte gezeichnet. Am Ende des Frames werden alle
	 * Ebenen auf die {@link Zeichenleinwand} übertragen.
	 * <p>
	 * {@code draw()} stellt die wichtigste Methode für eine Zeichenmaschine
	 * dar, da hier die Zeichnung des Programms erstellt wird.
	 */
	public void draw() {
		running = !stop_after_update;
	}

	/**
	 * {@code teardown()} wird aufgerufen, sobald die Schleife des
	 * Hauptprogramms beendet wurde. Dies passiert entweder nach dem ersten
	 * Durchlauf (wenn keine eigene {@link #update(double)} erstellt wurde),
	 * nach dem Aufruf von {@link #stop()} oder nachdem das
	 * {@link Zeichenfenster} geschlossen wurde.
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

	// Window changes
	public void fullscreenChanged() {
		// Intentionally left blank
	}

	////
	// Zeichenthread
	////

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

			// call setup of subclass and wait
			setup();

			state = Options.AppState.RUNNING;
			while( running ) {
				// delta in seconds
				delta = (System.nanoTime() - beforeTime) / 1000000000.0;
				beforeTime = System.nanoTime();

				saveMousePosition(mouseEvent);

				if( state != Options.AppState.PAUSED ) {
					handleUpdate(delta);
					handleDraw();

					if( canvas != null ) {
						canvas.render();
						// canvas.invalidate();
						// frame.repaint();
					}
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

				if( pause_pending ) {
					state = Options.AppState.PAUSED;
					pause_pending = false;
				}
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
