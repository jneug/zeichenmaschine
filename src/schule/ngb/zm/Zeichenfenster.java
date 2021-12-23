package schule.ngb.zm;

import schule.ngb.zm.formen.Formenebene;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;

public class Zeichenfenster extends Konstanten implements Runnable, MouseInputListener, KeyListener {

	protected Object mouseLock = new Object();
	/*
	 * Attribute für den Zugriff aus Unterklassen.
	 */
	protected Leinwand leinwand;
	protected Zeichenebene zeichnung;
	protected Formenebene formen;
	protected int tick = 0;
	protected long laufzeit = 0L;
	protected double delta = 0.0;
	protected double mausX = 0.0, mausY = 0.0, lmausX = 0.0, lmausY = 0.0;
	protected int breite = STD_BREITE, hoehe = STD_HOEHE;
	/*
	 * Interne Attribute zur Steuerung der Zeichenamschine.
	 */
	//
	private JFrame frame;
	private boolean running = false;
	private int framesPerSecond;

	public Zeichenfenster() {
		this(APP_NAME);
	}

	public Zeichenfenster( String pTitel ) {
		this(STD_BREITE, STD_HOEHE, pTitel);
	}

	public Zeichenfenster( int pBreite, int pHoehe, String pTitel ) {
		frame = new JFrame(pTitel);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch( Exception e ) {
			System.err.println("Fehler beim Setzen des look and feel.");
		}

		breite = pBreite;
		hoehe = pHoehe;

		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		leinwand = new Leinwand(pBreite, pHoehe);
		frame.setContentPane(leinwand);

		framesPerSecond = STD_FPS;

		zeichnung = getZeichenEbene();
		formen = getFormenEbene();

		einstellungen();

		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing( WindowEvent e ) {
				running = false;
				super.windowClosing(e);
			}
		});

		frame.pack();
		frame.requestFocusInWindow();
		frame.setVisible(true);

		running = true;
		new Thread(this).start();
	}

	public final void setSize( int pWidth, int pHeight ) {
		//frame.setSize(pWidth, pHeight);

		if( leinwand != null ) {
			leinwand.setSize(pWidth, pHeight);
		}
		breite = pWidth;
		hoehe = pHeight;
		frame.pack();
	}

	public final int getBreite() {
		return breite;
	}

	public final int getHoehe() {
		return hoehe;
	}

	public final void setTitel( String pTitel ) {
		frame.setTitle(pTitel);
	}

	public final Leinwand getLeinwand() {
		return leinwand;
	}

	public final void hinzu( Ebene pEbene ) {
		leinwand.hinzu(pEbene);
	}

	public final Zeichenebene getZeichenEbene() {
		Zeichenebene layer = leinwand.getEbene(Zeichenebene.class);
		if( layer == null ) {
			layer = new Zeichenebene(getBreite(), getHoehe());
			leinwand.hinzu(0, layer);
		}
		return layer;
	}

	public final Formenebene getFormenEbene() {
		Formenebene layer = leinwand.getEbene(Formenebene.class);
		if( layer == null ) {
			layer = new Formenebene(getBreite(), getHoehe());
			leinwand.hinzu(layer);
		}
		return layer;
	}

	public final int getFramesPerSecond() {
		return framesPerSecond;
	}

	public final void setFramesPerSecond( int pFramesPerSecond ) {
		framesPerSecond = pFramesPerSecond;
	}

	@Override
	public final void run() {
		long start = System.currentTimeMillis();
		long current = System.nanoTime();
		int _tick = 0;
		long _runtime = 0;
		tick = 0;
		laufzeit = 0;

		vorbereiten();

		while( running ) {
			int dt = (int) ((System.nanoTime() - current) / 1E6);
			current = System.nanoTime();
			delta = (dt / 1000.0);

			saveMousePosition();

			aktualisieren(delta);
			zeichnen();

			if( leinwand != null ) {
				//drawing.invalidate();
				frame.repaint();
			}

			try {
				int sleep = Math.round(1000 / framesPerSecond);
				if( dt >= sleep ) {
					sleep -= dt % sleep;
				}
				Thread.sleep(Math.max(0, sleep));
			} catch( InterruptedException e ) {
				// Interrupt not relevant
			} finally {
				_tick += 1;
				_runtime = System.currentTimeMillis() - start;
				tick = _tick;
				laufzeit = _runtime;
			}
		}
	}

	/*
	 * Methoden, die von Unterklassen überschrieben werden können / sollen.
	 */
	public void einstellungen() {

	}

	public void vorbereiten() {

	}

	public void zeichnen() {

	}

	public void aktualisieren( double delta ) {
		running = false;
	}

	/*
	 * Mouse handling
	 */

	@Override
	public void mouseClicked( MouseEvent e ) {
		saveMousePosition(e.getPoint());
		mausklick();
	}

	public void mausklick() {
	}

	@Override
	public void mousePressed( MouseEvent e ) {
		saveMousePosition(e.getPoint());
		maustasteRunter();
	}

	public void maustasteRunter() {
	}

	@Override
	public void mouseReleased( MouseEvent e ) {
		saveMousePosition(e.getPoint());
		maustasteHoch();
	}

	public void maustasteHoch() {
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
		mausGezogen();
	}

	public void mausGezogen() {

	}

	@Override
	public void mouseMoved( MouseEvent e ) {
		saveMousePosition(e.getPoint());
		mausBewegt();
	}

	public void mausBewegt() {

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
		lmausX = mausX;
		lmausY = mausY;

		java.awt.Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
		java.awt.Point compLoc = leinwand.getLocationOnScreen();
		mausX = mouseLoc.x - compLoc.x;
		mausY = mouseLoc.y - compLoc.y;
	}

	/*
	 * Keyboard handling
	 */

	@Override
	public void keyTyped( KeyEvent e ) {
		tastendruck();
	}

	public void tastendruck() {

	}

	@Override
	public void keyPressed( KeyEvent e ) {
		tasteRunter();
	}

	public void tasteRunter() {

	}

	@Override
	public void keyReleased( KeyEvent e ) {
		tasteHoch();
	}

	public void tasteHoch() {

	}
}
