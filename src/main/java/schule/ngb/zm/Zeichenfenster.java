package schule.ngb.zm;

import schule.ngb.zm.util.Log;
import schule.ngb.zm.util.Validator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;

public class Zeichenfenster extends JFrame {

	public static final void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch( Exception ex ) {
			LOG.error(ex, "Couldn't set the look and feel: %s", ex.getMessage());
		}
	}

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
	 * Das Anzeigefenster, auf dem die ZM gestartet wurde (muss nicht gleich
	 * dem Aktuellen sein, wenn das Fenster verschoben wurde).
	 */
	private GraphicsDevice displayDevice;

	private int width, height;

	/**
	 * Höhe und Breite der Zeichenmaschine, bevor sie mit
	 * {@link #setFullscreen(boolean)} in den Vollbild-Modus versetzt wurde.
	 * Wird verwendet, um die Fenstergröße wiederherzustellen, sobald der
	 * Vollbild-Modus verlassen wird.
	 */
	private int initialWidth, initialHeight;

	/**
	 * Speichert, ob die Zeichenmaschine mit {@link #setFullscreen(boolean)}
	 * in den Vollbildmodus versetzt wurde.
	 */
	private boolean fullscreen = false;

	/**
	 * {@code KeyListener}, um den Vollbild-Modus mit der Escape-Taste zu
	 * verlassen. Wird von {@link #setFullscreen(boolean)} automatisch
	 * hinzugefügt und entfernt.
	 */
	private KeyListener fullscreenExitListener = new KeyAdapter() {
		@Override
		public void keyPressed( KeyEvent e ) {
			if( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
				// canvas.removeKeyListener(this);
				setFullscreen(false);
			}
		}
	};

	private Zeichenleinwand canvas;

	public Zeichenfenster( int width, int height, String title ) {
		this(width, height, title, getGraphicsDevice());
	}

	public Zeichenfenster( int width, int height, String title, GraphicsDevice displayDevice ) {
		super(Validator.requireNotNull(displayDevice).getDefaultConfiguration());
		this.displayDevice = displayDevice;

		// Konfiguration des Frames
		this.setTitle(title);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// Das Icon des Fensters ändern
		try {
			if( Zeichenmaschine.MACOS ) {
				URL iconUrl = Zeichenmaschine.class.getResource("icon_512.png");
				Image icon = ImageIO.read(iconUrl);
				// Dock Icon in macOS setzen
				Taskbar taskbar = Taskbar.getTaskbar();
				taskbar.setIconImage(icon);
			} else {
				ArrayList<Image> icons = new ArrayList<>(4);
				for( int size = 32; size <= 512; size *= size ) {
					icons.add(ImageIO.read(new File("icon_" + size + ".png")));
				}

				this.setIconImages(icons);
			}
		} catch( IllegalArgumentException | IOException e ) {
			LOG.warn(e, "Could not load image icons: %s", e.getMessage());
		} catch( SecurityException | UnsupportedOperationException macex ) {
			// Dock Icon in macOS konnte nicht gesetzt werden :(
			LOG.warn(macex, "Could not set dock icon: %s", macex.getMessage());
		}

		java.awt.Rectangle bounds = getScreenBounds();
		this.width = bounds.width;
		this.height = bounds.height;

		// Fenster zusammenbauen, auf dem Bildschirm zentrieren ...
		this.pack();
		this.setResizable(false);
		this.centerFrame();
	}

	public void addCanvas( Zeichenleinwand canvas ) {
		this.canvas = canvas;
		this.add(canvas, 0);

		this.width = canvas.getWidth();
		this.height = canvas.getHeight();
		this.pack();
	}

	public java.awt.Rectangle getScreenBounds() {
		return displayDevice.getDefaultConfiguration().getBounds();
	}

	public java.awt.Rectangle getCanvasBounds() {
		java.awt.Insets insets = getInsets();
		return new java.awt.Rectangle(insets.top, insets.left, width, height);
	}

	/**
	 * Zentriert das Zeichenfenster auf dem aktuellen Bildschirm.
	 */
	public final void centerFrame() {
		java.awt.Rectangle bounds = getScreenBounds();
		this.setLocation(
			(int) (bounds.x + (bounds.width - this.getWidth()) / 2.0),
			(int) (bounds.y + (bounds.height - this.getHeight()) / 2.0)
		);
	}

	@Override
	public void setSize( int newWidth, int newHeight ) {
		java.awt.Rectangle bounds = getScreenBounds();
		java.awt.Insets insets = this.getInsets();

		if( fullscreen ) {
			initialWidth = Math.min(newWidth, bounds.width - insets.left - insets.right);
			initialHeight = Math.min(newHeight, bounds.height - insets.top - insets.bottom);
			setFullscreen(false);
		} else {
			width = Math.min(newWidth, bounds.width - insets.left - insets.right);
			height = Math.min(newHeight, bounds.height - insets.top - insets.bottom);

			if( canvas != null ) {
				canvas.setSize(width, height);
			}

			// this.pack();
			super.setSize(
				width + insets.left + insets.right,
				height + insets.top + insets.bottom
			);
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
			if( pEnable && !fullscreen ) {
				// Store width / height
				initialWidth = getWidth();
				initialHeight = getHeight();

				// frame.setUndecorated(true);
				this.setResizable(false); // Should be set anyway
				displayDevice.setFullScreenWindow(this);

				java.awt.Rectangle bounds = getScreenBounds();
				this.setSize(bounds.width, bounds.height);
				// Register ESC to exit fullscreen
				canvas.addKeyListener(fullscreenExitListener);

				fullscreen = true;
			} else if( !pEnable && fullscreen ) {
				fullscreen = false;

				canvas.removeKeyListener(fullscreenExitListener);
				displayDevice.setFullScreenWindow(null);
				this.setSize(initialWidth, initialHeight);
				this.pack();
				// frame.setUndecorated(false);
			}
		}
	}

	public boolean isFullscreen() {
		Window win = displayDevice.getFullScreenWindow();
		return fullscreen && win.equals(this);
	}

	private static final Log LOG = Log.getLogger(Zeichenfenster.class);

}
