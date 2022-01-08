package schule.ngb.zm;

import schule.ngb.zm.shapes.ShapesLayer;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Eine Leinwand ist die Hauptkomponente einer Zeichenmaschine. Sie besteht aus
 * mehreren Ebenen, auf denen auf verschiedene Arten gezeichnet werden kann. Die
 * Ebenen lassen sich beliebig übereinander aNORTHen, ausblenden oder wieder
 * löschen.
 * <p>
 * Jede Ebene besitzt eine Zeichenfläche, auf der ihre Zeichnung liegt. Diese
 * zeichenflächen werden pro Frame einmal von "DOWN" nach "UP" auf diese
 * Leinwand gezeichnet.
 */
public class Zeichenleinwand extends Canvas {

	private LinkedList<Layer> layers;

	public Zeichenleinwand( int width, int height ) {
		super.setSize(width, height);
		this.setPreferredSize(this.getSize());
		this.setMinimumSize(this.getSize());
		this.setBackground(Constants.STD_BACKGROUND.getColor());

		// Liste der Ebenen initialisieren und die Standardebenen einfügen
		layers = new LinkedList<>();
		synchronized( layers ) {
			layers.add(new ColorLayer(width, height, Constants.STD_BACKGROUND));
			layers.add(new DrawingLayer(width, height));
			layers.add(new ShapesLayer(width, height));
		}
	}

	/**
	 * Ändert die Größe der Zeichenleinwand auf die angegebene Größe in Pixeln.
	 * Eine Größenänderung hat auch eine Größenänderung aller Ebenen zur Folge.
	 *
	 * @param width  Neue Width der Leinwand in Pixeln.
	 * @param height Neue Höhe der Leinwand in Pixeln.
	 */
	@Override
	public void setSize( int width, int height ) {
		super.setSize(width, height);
		this.setPreferredSize(this.getSize());
		this.setMinimumSize(this.getSize());

		synchronized( layers ) {
			for( Layer layer : layers ) {
				layer.setSize(width, height);
			}
		}
	}

	/**
	 * Fügt der Zeichenleinwand eine Ebene hinzu, die oberhalb aller bisherigen
	 * Ebenen eingefügt wird.
	 *
	 * @param layer Die neue Ebene.
	 */
	public void addLayer( Layer layer ) {
		if( layer != null ) {
			synchronized( layers ) {
				layer.setSize(getWidth(), getHeight());
				layers.add(layer);
			}
		}
	}

	/**
	 * Fügt der Zeichenleinwand eine Ebene an einer bestimmten Stelle hinzu.
	 *
	 * @param i     Index der Ebene, beginnend mit <code>0</code>.
	 * @param layer Die neue Ebene.
	 */
	public void addLayer( int i, Layer layer ) {
		if( layer != null ) {
			synchronized( layers ) {
				layer.setSize(getWidth(), getHeight());
				layers.add(i, layer);
			}
		}
	}

	/**
	 * Gibt die Anzahl der {@link Layer Ebenen} in dieser Leinwand zurück.
	 *
	 * @return Die Anzahl der Ebenen.
	 */
	public int getLayerCount() {
		return layers.size();
	}

	/**
	 * Gibt die Liste der bisher hinzugefügten Ebenen zurück.
	 *
	 * @return Liste der Ebenen.
	 */
	public java.util.List<Layer> getLayers() {
		return layers;
	}

	/**
	 * Holt die Ebene am Index <var>i</var> (beginnend bei <code>0</code>).
	 *
	 * @param i Index der Ebene (beginnend bei <code>0</code>).
	 * @return Die Ebene am Index <var>i</var> oder <code>null</code>.
	 * @throws IndexOutOfBoundsException Falls der Index nicht existiert.
	 */
	public Layer getLayer( int i ) {
		if( layers.size() > i ) {
			return layers.get(i);
		} else {
			throw new IndexOutOfBoundsException("No layer at index " + i + " (max: " + (layers.size() - 1) + ").");
		}
	}

	/**
	 * Sucht die erste Ebene des angegebenen Typs aus der Liste der Ebenen.
	 * Existiert keine solche Ebene, wird <code>null</code> zurückgegeben.
	 *
	 * @param clazz Typ der Ebene.
	 * @param <L>
	 * @return Erste Ebene vom angegeben Typ.
	 */
	public <L extends Layer> L getLayer( Class<L> clazz ) {
		for( Layer layer : layers ) {
			if( layer.getClass().equals(clazz) ) {
				return clazz.cast(layer);
			}
		}
		return null;
	}

	/**
	 * Sucht alle Ebenen von einem bestimmten Typ aus der Liste der Ebenen und
	 * gibt diese als Liste zurück. Die Reihenfolge in der Liste entspricht der
	 * Reihenfolge der Ebenen in der Leinwand (von unten nach oben).
	 *
	 * @param pClazz
	 * @param <L>
	 * @return
	 */
	public <L extends Layer> java.util.List<L> getLayers( Class<L> pClazz ) {
		ArrayList<L> result = new ArrayList<>(layers.size());
		for( Layer layer : layers ) {
			if( layer.getClass().equals(pClazz) ) {
				result.add(pClazz.cast(layer));
			}
		}
		return result;
	}

	public void allocateBuffer() {
		this.createBufferStrategy(2);
	}

	public void dispose() {
		synchronized( layers ) {
			for( Layer layer : layers ) {
				layer.dispose();
			}
		}
	}

	@Override
	public void paint( Graphics g ) {
		render();
	}

	public void draw( Graphics graphics ) {
		Graphics2D g2d = (Graphics2D) graphics.create();
		synchronized( layers ) {
			for( Layer layer : layers ) {
				layer.draw(g2d);
			}
		}
		g2d.dispose();
	}

	public void render() {
		if( getBufferStrategy() == null ) {
			allocateBuffer();
		}

		if( isDisplayable() ) {
			BufferStrategy strategy = this.getBufferStrategy();
			if( strategy != null ) {
				do {
					do {
						Graphics2D g2d = (Graphics2D) strategy.getDrawGraphics();
						g2d.clearRect(0, 0, getWidth(), getHeight());

						synchronized( layers ) {
							for( Layer layer : layers ) {
								layer.draw(g2d);
							}
						}

						g2d.dispose();
					} while( strategy.contentsRestored() );

					// Display the buffer
					if( !strategy.contentsLost() ) {
						strategy.show();

						Toolkit.getDefaultToolkit().sync();
					}

					// Repeat the rendering if the drawing buffer was lost
				} while( strategy.contentsLost() );
			}
		}
	}

}
