package schule.ngb.zm;

import schule.ngb.zm.layers.ColorLayer;
import schule.ngb.zm.util.Log;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.util.*;

/**
 * Eine Leinwand ist die Hauptkomponente einer Zeichenmaschine. Sie besteht aus
 * mehreren Ebenen, auf denen auf verschiedene Arten gezeichnet werden kann. Die
 * Ebenen lassen sich beliebig übereinander legen, ausblenden oder wieder
 * löschen.
 * <p>
 * Jede Ebene besitzt eine Zeichenfläche, auf der ihre Zeichnung liegt. Diese
 * Zeichenflächen werden pro Frame einmal von "unten" nach "oben" auf diese
 * Leinwand gezeichnet.
 */
public class Zeichenleinwand extends Canvas {

	private final Object[] renderLock = new Object[0];

	/**
	 * Liste der hinzugefügten Ebenen.
	 */
	private final List<Layer> layers;

	private boolean rendering = false, suspended = false;

	/**
	 * Erstellt eine neue Zeichenleinwand mit einer festen Größe.
	 *
	 * @param width Breite der Zeichenleinwand.
	 * @param height Höhe der Zeichenleinwand.
	 */
	public Zeichenleinwand( int width, int height ) {
		super.setSize(width, height);
		this.setPreferredSize(getSize());
		this.setMinimumSize(getSize());
		this.setBackground(Constants.DEFAULT_BACKGROUND.getJavaColor());

		layers = Collections.synchronizedList(new LinkedList<>());
	}

	/**
	 * Ob die Leinwand ihren Inhalt gerade zeichnet.
	 *
	 * @return {@code true}, wenn die Inhalte gerade gezeichnet werden.
	 */
	public boolean isRendering() {
		return rendering;
	}

	/**
	 * Pausiert das Zeichnen der Leinwand kurzzeitig.
	 * <p>
	 * Falls die Leinwand gerade beim Zeichnen ist
	 * ({@code isRendering() == true}, blockt die Methode den aufrufenden Thread
	 * so lange, bis das Rendern beendet ist. Danach wird die Ebene nicht mehr
	 * neu gezeichnet, bis {@link #resumeRendering()} aufgerufen wird.
	 * <p>
	 * Das Zeichnen sollte nur dann unterbrochen werden, wenn sich der Kontext
	 * der Canvas-Komponente in seinem Elterncontainer ändert, um Fehler bei
	 * einer fehlenden Container-Hierarchie zu vermeiden.
	 *
	 * @throws InterruptedException Falls der Thread beim Warten unterbrochen
	 *                              wird.
	 */
	public void suspendRendering() throws InterruptedException {
		synchronized( renderLock ) {
			if( isRendering() ) {
				renderLock.wait();
			}

			suspended = true;
		}
	}

	public void resumeRendering() {
		suspended = false;
	}

	/**
	 * Ändert die Größe der Zeichenleinwand auf die angegebene Größe in Pixeln.
	 * <p>
	 * Bei einer Größenänderung wird auch die Größe aller bisher hinzugefügter
	 * {@link Layer Ebenen} angepasst, sodass sie die gesamte Leinwand füllen.
	 *
	 * @param width Neue Width der Leinwand in Pixeln.
	 * @param height Neue Höhe der Leinwand in Pixeln.
	 */
	@Override
	public void setSize( int width, int height ) {
		super.setSize(width, height);
		this.setPreferredSize(getSize());
		this.setMinimumSize(getSize());

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
			layer.setSize(getWidth(), getHeight());
			layers.add(layer);
		}
	}

	/**
	 * Fügt der Zeichenleinwand eine Ebene an einem bestimmten Index hinzu. Wenn
	 * der Index noch nicht existiert (also größer als die
	 * {@link #getLayerCount() Anzahl der Ebenen} ist), dann wird die neue Ebene
	 * als letzte eingefügt. Die aufrufende Methode kann also nicht sicher sein,
	 * dass die neue Ebene am Ende wirklich am Index {@code i} steht.
	 *
	 * @param i Index der Ebene beginnend mit 0.
	 * @param layer Die neue Ebene.
	 */
	public void addLayer( int i, Layer layer ) {
		if( layer != null ) {
			layer.setSize(getWidth(), getHeight());
			if( i > layers.size() ) {
				layers.add(layer);
			} else {
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
	 * Gibt eine Kopie der Liste der bisher hinzugefügten Ebenen zurück.
	 *
	 * @return Liste der Ebenen.
	 */
	public List<Layer> getLayers() {
		return List.copyOf(layers);
	}

	/**
	 * Holt die Ebene am Index {@code i} (beginnend bei 0).
	 *
	 * @param i Index der Ebene (beginnend bei 0).
	 * @return Die Ebene am Index {@code i} oder {@code null}.
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
	 * Existiert keine solche Ebene, wird {@code null} zurückgegeben.
	 *
	 * @param type Klasse der Ebenen, die abgefragt werden.
	 * @param <L> Typ der Ebenen, die abgefragt werden.
	 * @return Erste Ebene vom angegeben Typ.
	 */
	public <L extends Layer> L getLayer( Class<L> type ) {
		synchronized( layers ) {
			for( Layer layer : layers ) {
				if( layer.getClass().equals(type) ) {
					return type.cast(layer);
				}
			}
		}
		return null;
	}

	/**
	 * Sucht alle Ebenen von einem bestimmten Typ aus der Liste der Ebenen und
	 * gibt diese als Liste zurück. Die Reihenfolge in der Liste entspricht der
	 * Reihenfolge der Ebenen in der Leinwand (von unten nach oben).
	 *
	 * @param type Klasse der Ebenen, die abgefragt werden.
	 * @param <L> Typ der Ebenen, die abgefragt werden.
	 * @return Eine Liste mit den vorhandenen Ebenen des abgefragten Typs.
	 */
	public <L extends Layer> List<L> getLayers( Class<L> type ) {
		ArrayList<L> result = new ArrayList<>(layers.size());
		synchronized( layers ) {
			for( Layer layer : layers ) {
				if( layer.getClass().equals(type) ) {
					result.add(type.cast(layer));
				}
			}
		}
		return result;
	}

	public boolean removeLayer( Layer pLayer ) {
		return layers.remove(pLayer);
	}

	public void removeLayers( Layer... removeLayers ) {
		synchronized( layers ) {
			for( Layer layer : removeLayers ) {
				layers.remove(layer);
			}
		}
	}

	public void clearLayers() {
		layers.clear();
	}

	public void updateLayers( double delta ) {
		synchronized( layers ) {
			for( Layer layer : List.copyOf(layers) ) {
				layer.update(delta);
			}
		}
	}

	/**
	 * Erstellt eine passende {@link BufferStrategy} für diese Ebene.
	 */
	public void allocateBuffer() {
		this.createBufferStrategy(2);
	}

	/**
	 * Löscht alle Ebenen der Zeichenebene und gibt deren Ressourcen frei.
	 */
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

	/**
	 * Zeichnet den Inhalt aller {@link Layer Ebenen} in den Grafik-Kontext.
	 *
	 * @param graphics
	 */
	public void draw( Graphics graphics ) {
		Graphics2D g2d = (Graphics2D) graphics.create();
		synchronized( layers ) {
			for( Layer layer : layers ) {
				layer.draw(g2d);
			}
		}
		g2d.dispose();
	}

	/**
	 * Zeigt den aktuellen Inhalt der Zeichenleinwand an.
	 */
	public void render() {
		if( !suspended && isDisplayable() ) {
			if( getBufferStrategy() == null ) {
				allocateBuffer();
			}

			synchronized( renderLock ) {
				rendering = true;
				BufferStrategy strategy = this.getBufferStrategy();
				if( strategy != null ) {
					do {
						do {
							Graphics2D g2d = (Graphics2D) strategy.getDrawGraphics();
							g2d.clearRect(0, 0, getWidth(), getHeight());

							synchronized( layers ) {
								for( Layer layer : List.copyOf(layers) ) {
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
				rendering = false;
				renderLock.notifyAll();
			}
		}
	}

	private static final Log LOG = Log.getLogger(Zeichenleinwand.class);

}
