package schule.ngb.zm;

import schule.ngb.zm.util.Log;
import schule.ngb.zm.util.Validator;
import schule.ngb.zm.util.io.ImageLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Ein Zeichenfenster ist das Programmfenster für die Zeichenmaschine.
 * <p>
 * Das Zeichenfenster implementiert hilfreiche Funktionen, um ein
 * Programmfenster mit einer Zeichenleinwand als zentrales Element zu erstellen.
 * Ein Zeichenfenster kann auch ohne eine Zeichenmaschine verwendet werden, um
 * eigene Programmabläufe zu implementieren.
 */
public class Zeichenfenster extends JFrame {

	/**
	 * Setzt das Look and Feel auf den Standard des Systems.
	 * <p>
	 * Sollte einmalig vor Erstellen des ersten Programmfensters aufgerufen
	 * werden.
	 */
	public static final void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch( Exception ex ) {
			LOG.error(ex, "Couldn't set the look and feel: %s", ex.getMessage());
		}
	}

	/**
	 * Ermittelt ein {@link GraphicsDevice Anzeigegerät}, auf dem ein neues
	 * Zeichenfenster angezeigt werden soll. In der Regel ist dies der
	 * Bildschirm, auf dem sich derzeit der Mauszeiger befindet. Kann kein
	 * solcher Bildschirm ermittelt werden, wird das
	 * {@link GraphicsEnvironment#getDefaultScreenDevice() Standardgerät}
	 * zurückgegeben.
	 *
	 * @return Das Anzeigegerät, auf dem ein neues Fenster angezeigt werden
	 * 	sollte.
	 */
	public static final GraphicsDevice getGraphicsDevice() {
		// Wir suchen den Bildschirm, der derzeit den Mauszeiger enthält, um
		// das Zeichenfenster dort zu zentrieren.
		// TODO: (ngb) Wenn wir in BlueJ sind, sollte das Fenster neben dem Editor öffnen.
		java.awt.Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = environment.getScreenDevices();
		GraphicsDevice displayDevice = null;
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
		return displayDevice;
	}

	/**
	 * Das Anzeigegerät, auf dem die Zeichenmaschine gestartet wurde (muss nicht
	 * gleich dem Aktuellen sein, wenn das Fenster verschoben wurde).
	 */
	private final GraphicsDevice displayDevice;

	/**
	 * Bevorzugte Abmessungen der Zeichenleinwand. Für das Zeichenfenster hat es
	 * Priorität die Leinwand auf dieser Größe zu halten. Gegebenenfalls unter
	 * Missachtung anderer Größenvorgaben. Allerdings kann das Fenster keine
	 * Garantie für die Größe der Leinwand übernehmen.
	 */
	private int canvasPreferredWidth, canvasPreferredHeight;

	/**
	 * Speichert, ob die Zeichenmaschine mit {@link #setFullscreen(boolean)} in
	 * den Vollbildmodus versetzt wurde.
	 */
	private boolean fullscreen = false;

	/**
	 * {@code KeyListener}, um den Vollbild-Modus mit der Escape-Taste zu
	 * verlassen. Wird von {@link #setFullscreen(boolean)} automatisch
	 * hinzugefügt und entfernt.
	 */
	private final KeyListener fullscreenExitListener = new KeyAdapter() {
		@Override
		public void keyPressed( KeyEvent e ) {
			if( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
				// canvas.removeKeyListener(this);
				setFullscreen(false);
				e.consume();
			}
		}
	};

	// Die Zeichenleinwand dieses Fensters.
	private final Zeichenleinwand canvas;

	/**
	 * Erstellt ein neues Zeichenfenster mit dem angegebenen Titel und einer
	 * {@link Zeichenleinwand} in der angegebenen Größe.
	 *
	 * @param width Die Breite der Zeichenleinwand.
	 * @param height Die Höhe der Zeichenleinwand.
	 * @param title Der Titel des Fensters.
	 */
	@SuppressWarnings( "unused" )
	public Zeichenfenster( int width, int height, String title ) {
		this(new Zeichenleinwand(width, height), title, getGraphicsDevice());
	}

	/**
	 * Erstellt ein neues Zeichenfenster mit dem angegebenen Titel und einer
	 * {@link Zeichenleinwand} in der angegebenen Größe auf dem angegebenen
	 * Anzeigegerät.
	 *
	 * @param width Die Breite der Zeichenleinwand.
	 * @param height Die Höhe der Zeichenleinwand.
	 * @param title Der Titel des Fensters.
	 * @param displayDevice Das Anzeigegerät für das Fenster.
	 */
	@SuppressWarnings( "unused" )
	public Zeichenfenster( int width, int height, String title, GraphicsDevice displayDevice ) {
		this(new Zeichenleinwand(width, height), title, displayDevice);
	}

	/**
	 * Erstellt ein neues Zeichenfenster mit dem angegebenen Titel und der
	 * angegebene {@link Zeichenleinwand}.
	 *
	 * @param canvas Die Zeichenleinwand.
	 * @param title Der Titel des Fensters.
	 */
	public Zeichenfenster( Zeichenleinwand canvas, String title ) {
		this(canvas, title, getGraphicsDevice());
	}

	/**
	 * Erstellt ein neues Zeichenfenster mit dem angegebenen Titel und der
	 * angegebene {@link Zeichenleinwand} auf dem angegebenen Anzeigegerät.
	 *
	 * @param canvas Die Zeichenleinwand.
	 * @param title Der Titel des Fensters.
	 * @param displayDevice Das Anzeigegerät für das Fenster.
	 */
	public Zeichenfenster( Zeichenleinwand canvas, String title, GraphicsDevice displayDevice ) {
		super(Validator.requireNotNull(displayDevice, "displayDevice").getDefaultConfiguration());
		this.displayDevice = displayDevice;

		Validator.requireNotNull(canvas, "Every Zeichenfenster needs a Zeichenleinwand, but got <null>.");

		this.canvasPreferredWidth = canvas.getWidth();
		this.canvasPreferredHeight = canvas.getHeight();
		this.add(canvas, BorderLayout.CENTER);
		this.canvas = canvas;

		// Konfiguration des Frames
		this.setTitle(title == null ? "Zeichenfenster " + Constants.APP_VERSION : title);
		// Kann vom Aufrufenden überschrieben werden
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// Das Icon des Fensters ändern
		try {
			if( Zeichenmaschine.MACOS ) {
				InputStream iconStream = this.getClass().getResourceAsStream("icon_512.png");
				if( iconStream != null ) {
					Image icon = ImageIO.read(iconStream);
					// Dock Icon in macOS setzen
					Taskbar taskbar = Taskbar.getTaskbar();
					taskbar.setIconImage(icon);
				} else {
					LOG.warn("Could not load dock-icon");
				}
			} else {
				ArrayList<Image> icons = new ArrayList<>(4);
				for( int size : new int[]{32, 64, 128, 512} ) {
					URL icnUrl = Zeichenmaschine.class.getResource("icon_" + size + ".png");
					if( icnUrl != null ) {
						icons.add(ImageIO.read(icnUrl));
					}
				}

				if( icons.isEmpty() ) {
					LOG.warn("Could not load dock-icon");
				} else {
					this.setIconImages(icons);
				}
			}
		} catch( IllegalArgumentException | IOException e ) {
			LOG.warn("Could not load image icons: %s", e.getMessage());
		} catch( SecurityException | UnsupportedOperationException macex ) {
			// Dock Icon in macOS konnte nicht gesetzt werden :(
			LOG.warn("Could not set dock icon: %s", macex.getMessage());
		}
		// Fenster zusammenbauen, auf dem Bildschirm positionieren ...
		this.pack();
		this.setResizable(false);
		this.setFocusable(true);
		this.setLocationByPlatform(true);
		// this.centerFrame();
	}

	/**
	 * Liefert das Anzeigegerät, auf dem dieses Fenster erstellt wurde.
	 * <p>
	 * Das Anzeigegerät muss nicht unbedingt gleich dem sein, auf dem sich das
	 * Fenster derzeit befindet, wenn das Fenster verschoben wurde.
	 *
	 * @return Das Anzeigegerät.
	 */
	@SuppressWarnings( "unused" )
	public GraphicsDevice getDisplayDevice() {
		return displayDevice;
	}

	/**
	 * Liefert die Abmessungen des Anzeigegeräts, auf dem das Fenster gestartet
	 * wurde.
	 *
	 * @return Die Abmessungen des Anzeigegeräts.
	 */
	public Rectangle getScreenBounds() {
		// return GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		return displayDevice.getDefaultConfiguration().getBounds();
	}

	/**
	 * Liefert die Zeichenleinwand dieses Fensters.
	 *
	 * @return Die Zeichenleinwand.
	 */
	@SuppressWarnings( "unused" )
	public Zeichenleinwand getCanvas() {
		return canvas;
	}

	/**
	 * Liefert die Abmessungen der Zeichenleinwand zurück.
	 *
	 * @return Die Abmessungen der Zeichenleinwand.
	 */
	public Rectangle getCanvasBounds() {
		return canvas.getBounds();
	}

	/**
	 * Zentriert das Zeichenfenster auf dem aktuellen Bildschirm.
	 */
	public final void centerFrame() {
		java.awt.Rectangle screenBounds = getScreenBounds();
		java.awt.Rectangle frameBounds = getBounds();
		this.setLocation(
			(int) (screenBounds.x + (screenBounds.width - frameBounds.width) / 2.0),
			(int) (screenBounds.y + (screenBounds.height - frameBounds.height) / 2.0)
		);
	}

	/**
	 * Setzt die Größe der Zeichenleinwand auf die angegebenen Werte.
	 *
	 * @param newWidth Neue Breite der Zeichenleinwand.
	 * @param newHeight Neue Höhe der Zeichenleinwand.
	 */
	public void setCanvasSize( int newWidth, int newHeight ) {
		// TODO: (ngb) Put constrains on max/min frame/canvas size
		if( fullscreen ) {
			canvasPreferredWidth = newWidth;
			canvasPreferredHeight = newHeight;
			setFullscreen(false);
		} else {
			canvas.setSize(newWidth, newHeight);
			canvasPreferredWidth = canvas.getWidth();
			canvasPreferredHeight = canvas.getHeight();
			this.pack();
		}
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
			// Temporarily stop rendering
			while( canvas.isRendering() ) {
				try {
					canvas.suspendRendering();
				} catch( InterruptedException ex ) {
					LOG.info(ex, "setFullsceen(true) was interrupted and canceled.");
					return;
				}
			}

			if( pEnable && !fullscreen ) {
				// Activate fullscreen
				dispose();
				setUndecorated(true);
				setResizable(false);
				displayDevice.setFullScreenWindow(this);

				// Register ESC to exit fullscreen
				canvas.addKeyListener(fullscreenExitListener);

				// Reset canvas size to its new bounds to recreate buffer and drawing surface
				java.awt.Rectangle canvasBounds = getCanvasBounds();
				canvas.setSize(canvasBounds.width, canvasBounds.height);
				//canvas.requestFocus();
				canvas.requestFocus();

				fullscreen = true;
			} else if( !pEnable && fullscreen ) {
				displayDevice.setFullScreenWindow(null);
				dispose();
				setUndecorated(false);
				setResizable(false);

				canvas.removeKeyListener(fullscreenExitListener);
				canvas.setSize(canvasPreferredWidth, canvasPreferredHeight);

				setVisible(true);
				pack();

				//canvas.requestFocus();
				canvas.requestFocus();

				fullscreen = false;
			}

			// Resume rendering
			canvas.resumeRendering();
		}
	}

	/**
	 * Prüft, ob sich dieses Zeichenfenster im Vollbild befindet.
	 *
	 * @return {@code true}, wenn das Fenster im Vollbild ist, {@code false}
	 * 	sonst.
	 */
	public boolean isFullscreen() {
		Window win = displayDevice.getFullScreenWindow();
		return fullscreen && win.equals(this);
	}

	private static final Log LOG = Log.getLogger(Zeichenfenster.class);

}
