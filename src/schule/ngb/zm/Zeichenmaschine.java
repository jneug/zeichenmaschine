package schule.ngb.zm;

import schule.ngb.zm.shapes.ShapesLayer;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;

/**
 * Hauptklasse der Zeichenmaschine.
 *
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

	protected int width = STD_WIDTH, height = STD_HEIGHT;

	private Object mouseLock = new Object();

	private Object keyboardLock = new Object();

	/*
	 * Interne Attribute zur Steuerung der Zeichenamschine.
	 */
	//
	private JFrame frame;

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
			System.err.println("Error setting the look and feel.");
		}

		GraphicsEnvironment environment =
			GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice displayDevice = environment.getDefaultScreenDevice();

		frame = new JFrame(displayDevice.getDefaultConfiguration());
		frame.setTitle(title);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		this.width = width;
		this.height = height;

		canvas = new Zeichenleinwand(width, height);
		frame.add(canvas);

		framesPerSecond = STD_FPS;

		background = getBackgroundLayer();
		drawing = getDrawingLayer();
		shapes = getShapesLayer();

		settings();

		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
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
		// TODO: Center on current display (not main display by default)
		// TODO: Position at current BlueJ windows if IN_BLUEJ
		frame.setLocationRelativeTo(null);
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

	public final void redraw() {
		canvas.render();
		//canvas.invalidate();
		//frame.repaint();
		//hide();
		//show();
	}

	public void quit() {
		quit(!IN_BLUEJ);
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
	public void mouseClicked( MouseEvent e ) {
		saveMousePosition(e.getPoint());
		mouseClicked();
	}

	public void mouseClicked() {
	}

	@Override
	public void mousePressed( MouseEvent e ) {
		saveMousePosition(e.getPoint());
		mousePressed();
	}

	public void mousePressed() {
	}

	@Override
	public void mouseReleased( MouseEvent e ) {
		saveMousePosition(e.getPoint());
		mouseReleased();
	}

	public void mouseReleased() {
	}

	@Override
	public void mouseEntered( MouseEvent e ) {
		saveMousePosition(e.getPoint());
	}

	@Override
	public void mouseExited( MouseEvent e ) {
		saveMousePosition(e.getPoint());
	}

	@Override
	public void mouseDragged( MouseEvent e ) {
		saveMousePosition(e.getPoint());
		mouseDragged();
	}

	public void mouseDragged() {

	}

	@Override
	public void mouseMoved( MouseEvent e ) {
		saveMousePosition(e.getPoint());
		mouseMoved();
	}

	public void mouseMoved() {

	}

	private void saveMousePosition( Point pLocation ) {
		//pmouseX = mouseX;
		//pmouseY = mouseY;
        /*synchronized(mouseLock) {
            mouseX = pLocation.getX()-this.getRootPane().getX();
            mouseY = pLocation.getY()-this.getRootPane().getY();
        }*/
	}

	private void saveMousePosition() {
		pmouseX = mouseX;
		pmouseY = mouseY;

		java.awt.Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
		java.awt.Point compLoc = canvas.getLocationOnScreen();
		mouseX = mouseLoc.x - compLoc.x;
		mouseY = mouseLoc.y - compLoc.y;
	}

	@Override
	public void keyTyped( KeyEvent e ) {
		keyTyped();
	}

	/*
	 * Keyboard handling
	 */

	public void keyTyped() {

	}

	@Override
	public void keyPressed( KeyEvent e ) {
		keyPressed();
	}

	public void keyPressed() {

	}

	@Override
	public void keyReleased( KeyEvent e ) {
		keyReleased();
	}

	public void keyReleased() {

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

				saveMousePosition();

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
