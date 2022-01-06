package schule.ngb.zm;

import schule.ngb.zm.shapes.ShapesLayer;
import schule.ngb.zm.util.ImageLoader;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
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

	public static boolean IN_BLUEJ;

	static {
		IN_BLUEJ = System.getProperty("java.class.path").contains("bluej");
	}

	/*
	 * Attributes to be accessed by subclasses.
	 */

	protected Zeichenleinwand canvas;

	protected ColorLayer background;

	protected DrawingLayer drawing;

	protected ShapesLayer shapes;

	protected int tick = 0;

	protected long runtime = 0L;

	protected double delta = 0.0;

	protected double mouseX = 0.0, mouseY = 0.0, pmouseX = 0.0, pmouseY = 0.0;

	protected boolean mousePressed = false;

	protected int mouseButton = 0;

	protected char key = ' ';

	protected int keyCode = 0;

	protected boolean keyPressed = false;

	protected int width, height;

	protected int screenWidth, screenHeight;


	/*
	 * Interne Attribute zur Steuerung der Zeichenmaschine.
	 */

	private JFrame frame;

	private GraphicsEnvironment environment;

	private GraphicsDevice displayDevice;

	private boolean running = false, isDrawing = false, isUpdating = false;

	private int framesPerSecond;

	private Thread mainThread;

	private boolean quitAfterTeardown = false, initialized = false;


	public Zeichenmaschine() {
		this(APP_NAME + " " + APP_VERSION);
	}

	public Zeichenmaschine( String title ) {
		this(STD_WIDTH, STD_HEIGHT, title);
	}

	public Zeichenmaschine( int width, int height, String title ) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch( Exception e ) {
			System.err.println("Error setting the look and feel: " + e.getMessage());
		}


		// Looking for the screen currently holding the mouse pointer
		// that will be used as the screen device for this Zeichenmaschine
		java.awt.Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
		environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = environment.getScreenDevices();
		for( GraphicsDevice gd: devices ) {
			if( gd.getDefaultConfiguration().getBounds().contains(mouseLoc) ) {
				displayDevice = gd;
				break;
			}
		}
		if( displayDevice == null ) {
			displayDevice = environment.getDefaultScreenDevice();
		}


		this.width = width;
		this.height = height;
		java.awt.Rectangle displayBounds = displayDevice.getDefaultConfiguration().getBounds();
		this.screenWidth = (int)displayBounds.getWidth();
		this.screenHeight = (int)displayBounds.getHeight();


		frame = new JFrame(displayDevice.getDefaultConfiguration());
		frame.setTitle(title);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		canvas = new Zeichenleinwand(width, height);
		frame.add(canvas);

		framesPerSecond = STD_FPS;

		background = getBackgroundLayer();
		drawing = getDrawingLayer();
		shapes = getShapesLayer();

		// TODO: When to call settings?
		settings();

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

		frame.pack();
		frame.setResizable(false);
		centerFrame();
		frame.setVisible(true);

		canvas.allocateBuffer();

		running = true;
		mainThread = new Zeichenthread();
		mainThread.start();

		//frame.requestFocusInWindow();
		canvas.requestFocus();

		initialized = true;
	}

	// TODO: Implement in conjunction with Zeichenfenster
	public final void createFrame( String title ) {

	}

	public void show() {
		if( !frame.isVisible() ) {
			frame.setVisible(true);
		}
	}

	public void hide() {
		if( frame.isVisible() ) {
			frame.setVisible(false);
		}
	}

	public final void centerFrame() {
		// TODO: Center on current display (not main display by default)
		// TODO: Position at current BlueJ windows if IN_BLUEJ
		//frame.setLocationRelativeTo(null);
		//frame.setLocationRelativeTo(displayDevice.getFullScreenWindow());

		java.awt.Rectangle bounds = displayDevice.getDefaultConfiguration().getBounds();
		frame.setLocation(
			(int)(bounds.x + (screenWidth-frame.getWidth())/2.0),
			(int)(bounds.y + (screenHeight-frame.getHeight())/2.0)
		);
	}

	public final void redraw() {
		canvas.render();
		//canvas.invalidate();
		//frame.repaint();
		//hide();
		//show();
	}

	public final void stop() {
		running = false;
	}

	public void quit() {
		//quit(!IN_BLUEJ);
		quit(true);
	}

	public void quit( boolean exit ) {
		frame.setVisible(false);
		canvas.dispose();
		frame.dispose();

		if( exit ) {
			System.exit(0);
		}
	}

	public final void setSize( int width, int height ) {
		//frame.setSize(width, height);

		if( canvas != null ) {
			canvas.setSize(width, height);
		}
		this.width = width;
		this.height = height;
		frame.pack();
	}

	public final int getWidth() {
		return width;
	}

	public final int getHeight() {
		return height;
	}

	public final void setTitle( String pTitel ) {
		frame.setTitle(pTitel);
	}

	public final Zeichenleinwand getCanvas() {
		return canvas;
	}

	public final void addLayer( Layer layer ) {
		canvas.addLayer(layer);
		layer.setSize(getWidth(), getHeight());
	}

	public final ColorLayer getBackgroundLayer() {
		ColorLayer layer = canvas.getLayer(ColorLayer.class);
		if( layer == null ) {
			layer = new ColorLayer(STD_BACKGROUND);
			canvas.addLayer(0, layer);
		}
		return layer;
	}

	public final DrawingLayer getDrawingLayer() {
		DrawingLayer layer = canvas.getLayer(DrawingLayer.class);
		if( layer == null ) {
			layer = new DrawingLayer(getWidth(), getHeight());
			canvas.addLayer(1, layer);
		}
		return layer;
	}

	public final ShapesLayer getShapesLayer() {
		ShapesLayer layer = canvas.getLayer(ShapesLayer.class);
		if( layer == null ) {
			layer = new ShapesLayer(getWidth(), getHeight());
			canvas.addLayer(layer);
		}
		return layer;
	}

	public final int getFramesPerSecond() {
		return framesPerSecond;
	}

	public final void setFramesPerSecond( int pFramesPerSecond ) {
		framesPerSecond = pFramesPerSecond;
	}

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

	public void saveImage( String filepath ) {
		BufferedImage img = new BufferedImage(canvas.getWidth(),canvas.getHeight(), BufferedImage.TYPE_INT_RGB);

		Graphics2D g = img.createGraphics();
		g.setColor(STD_BACKGROUND.getColor());
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		canvas.draw(g);
		g.dispose();

		try {
			ImageLoader.saveImage(img, new File(filepath), true);
		} catch ( IOException ex ) {
			ex.printStackTrace();
		}
	}

	public ImageLayer snapshot() {
		BufferedImage img = new BufferedImage(canvas.getWidth(),canvas.getHeight(), BufferedImage.TYPE_INT_RGB);

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

	/*
	 * Methoden, die von Unterklassen überschrieben werden können / sollen.
	 */
	public void settings() {

	}

	public void setup() {

	}

	public void draw() {

	}

	public void teardown() {

	}

	public void update( double delta ) {
		running = false;
	}

	public void delay( int ms ) {
		if( ms <= 0 ) {
			return;
		}

		long timer = 0L;
		if( isDrawing ) {
			// Immediately show the current drawing before waiting
			// Measure the render time and subtract from the waiting ms
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
	 * Mouse handling
	 */
	@Override
	public final void mouseClicked( MouseEvent e ) {
		saveMousePosition(e);
		mouseClicked();
	}

	public void mouseClicked() {
	}

	@Override
	public final void mousePressed( MouseEvent e ) {
		saveMousePosition(e);
		mousePressed = true;
		mousePressed();
	}

	public void mousePressed() {
	}

	@Override
	public final void mouseReleased( MouseEvent e ) {
		saveMousePosition(e);
		mousePressed = false;
		mouseReleased();
	}

	public void mouseReleased() {
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
		saveMousePosition(e);
		mouseDragged();
	}

	public void mouseDragged() {

	}

	@Override
	public final void mouseMoved( MouseEvent e ) {
		saveMousePosition(e);
		mouseMoved();
	}

	public void mouseMoved() {

	}

	private void saveMousePosition( MouseEvent e ) {
		pmouseX = mouseX;
		pmouseY = mouseY;

		mouseX = e.getX();
		mouseY = e.getY();
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

	}

	@Override
	public final void keyPressed( KeyEvent e ) {
		saveKeys(e);
		keyPressed = true;
		keyPressed();
	}

	public void keyPressed() {

	}

	@Override
	public final void keyReleased( KeyEvent e ) {
		saveKeys(e);
		keyPressed = false;
		keyReleased();
	}

	public void keyReleased() {

	}

	private final void saveKeys( KeyEvent e ) {
		key = e.getKeyChar();
		keyCode = e.getKeyCode();
	}

	class Zeichenthread extends Thread {

		@Override
		public final void run() {
			// Wait for full initialization before start
			while( !initialized ) {
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

			while( running ) {
				// delta in seconds
				delta = (System.nanoTime() - beforeTime) / 1000000000.0;
				beforeTime = System.nanoTime();

				//saveMousePosition();

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

			teardown();
			if( quitAfterTeardown ) {
				quit();
			}
		}

		public void handleUpdate( double delta ) {
			if( isUpdating ) {
				return;
			}
			isUpdating = true;
			update(delta);
			isUpdating = false;
		}

		public void handleDraw() {
			if( isDrawing ) {
				return;
			}
			isDrawing = true;
			draw();
			isDrawing = false;
		}

	}

}
