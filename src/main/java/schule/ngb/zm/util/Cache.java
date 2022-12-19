package schule.ngb.zm.util;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Ein Cache ist ein {@link Map} Implementation, die Inhaltsobjekte in einer
 * {@link Reference} speichert und als Zwischenspeicher für Objekte dienen kann,
 * deren Erstellung aufwendig ist.
 * <p>
 * Für einen Cache ist nicht garantiert, dass ein eingefügtes Objekt beim
 * nächsten Aufruf noch vorhanden ist, da die Referenz inzwischen vom Garbage
 * Collector gelöscht worden sein kann.
 * <p>
 * Als interne Map wird einen {@link ConcurrentHashMap} verwendet.
 * <p>
 * Ein passender Cache wird mittels der Fabrikmethoden {@link #newSoftCache()}
 * und {@link #newWeakCache()} erstellt.
 * <pre><code>
 * Cache&lt;String, Image&gt; imageCache = Cache.newSoftCache();
 * </code></pre>
 *
 * @param <K> Der Typ der Schlüssel.
 * @param <V> Der Typ der Objekte.
 */
public final class Cache<K, V> implements Map<K, V> {

	/**
	 * Erstellt einen Cache mit {@link SoftReference} Referenzen.
	 *
	 * @param <K> Der Typ der Schlüssel.
	 * @param <V> Der Typ der Objekte.
	 * @return Ein Cache.
	 */
	public static <K, V> Cache<K, V> newSoftCache() {
		return new Cache<>(SoftReference::new);
	}

	/**
	 * Erstellt einen Cache mit {@link WeakReference} Referenzen.
	 *
	 * @param <K> Der Typ der Schlüssel.
	 * @param <V> Der Typ der Objekte.
	 * @return Ein Cache.
	 */
	public static <K, V> Cache<K, V> newWeakCache() {
		return new Cache<>(WeakReference::new);
	}


	private final Map<K, Reference<V>> cache = new ConcurrentHashMap<>();

	private final Reference<V> NOCACHE;

	private final Function<V, Reference<V>> refSupplier;

	private Cache( Function<V, Reference<V>> refSupplier ) {
		this.refSupplier = refSupplier;
		NOCACHE = refSupplier.apply(null);
	}

	@Override
	public int size() {
		return cache.size();
	}

	@Override
	public boolean isEmpty() {
		return cache.isEmpty();
	}

	@Override
	public boolean containsKey( Object key ) {
		return cache.containsKey(key) && cache.get(key).get() != null;
	}

	@Override
	public boolean containsValue( Object value ) {
		return cache.values().stream()
			.anyMatch(( ref ) -> ref.get() != null && ref.get() == value);
	}

	@Override
	public V get( Object key ) {
		if( cache.containsKey(key) ) {
			return cache.get(key).get();
		}
		return null;
	}

	/**
	 * Deaktiviert das Caching für den angegebenen Schlüssel.
	 * <p>
	 * Folgende Aufrufe von {@link #put(Object, Object)} mit demselben Schlüssel
	 * haben keinen Effekt. Um das Caching wieder zu aktivieren, muss
	 * {@link #remove(Object)} mit dem Schlüssel aufgerufen werden,
	 *
	 * @param key Der Schlüssel.
	 */
	public void disableCache( K key ) {
		cache.put(key, NOCACHE);
	}

	/**
	 * Prüft, ob der für den angegebenen Schlüssel zuvor
	 * {@link #disableCache(Object)} aufgerufen wurde.
	 *
	 * @param key Der Schlüssel.
	 * @return {@code true}, wenn der Schlüssel nicht gespeichert wird.
	 */
	public boolean isCachingDisabled( K key ) {
		return cache.get(key) == NOCACHE;
	}

	@Override
	public V put( K key, V value ) {
		if( !isCachingDisabled(key) ) {
			V prev = remove(key);
			cache.put(key, refSupplier.apply(value));
			return prev;
		} else {
			return null;
		}
	}

	@Override
	public V remove( Object key ) {
		Reference<V> ref = cache.get(key);
		cache.remove(key);

		V prev = null;
		if( ref != null ) {
			prev = ref.get();
			ref.clear();
		}
		return prev;
	}

	@Override
	public void putAll( Map<? extends K, ? extends V> m ) {
		for( Entry<? extends K, ? extends V> e : m.entrySet() ) {
			put(e.getKey(), e.getValue());
		}
	}

	@Override
	public void clear() {
		cache.clear();
	}

	@Override
	public Set<K> keySet() {
		return cache.keySet();
	}

	@Override
	public Collection<V> values() {
		return cache.values().stream()
			.filter(( ref ) -> ref.get() != null)
			.map(( ref ) -> ref.get())
			.collect(Collectors.toList());
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return cache.entrySet().stream()
			.filter(( e ) -> e.getValue() != null && e.getValue().get() != null)
			.map(( e ) -> new SoftCacheEntryView(e.getKey()))
			.collect(Collectors.toSet());
	}

	private final class SoftCacheEntryView implements Map.Entry<K, V> {

		private K key;

		public SoftCacheEntryView( K key ) {
			this.key = key;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return Cache.this.get(key);
		}

		@Override
		public V setValue( V value ) {
			return Cache.this.put(key, value);
		}

	}

}
