package schule.ngb.zm.util;

import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SoftCache<K, V> implements Map<K, V> {

	private final Map<K, SoftReference<V>> cache = new ConcurrentHashMap<>();

	private final SoftReference<V> NOCACHE = new SoftReference<>(null);

	public SoftCache() {
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
		return false;
	}

	private boolean containsRef( Object key ) {
		return cache.containsKey(key);
	}

	@Override
	public V get( Object key ) {
		if( cache.containsKey(key) ) {
			return cache.get(key).get();
		}
		return null;
	}

	public void nocache( K key ) {
		cache.put(key, NOCACHE);
	}

	public boolean isNocache( K key ) {
		return cache.get(key) == NOCACHE;
	}

	@Override
	public V put( K key, V value ) {
		if( !isNocache(key) ) {
			V prev = remove(key);
			cache.put(key, new SoftReference<>(value));
			return prev;
		} else {
			return null;
		}
	}

	@Override
	public V remove( Object key ) {
		SoftReference<V> ref = cache.get(key);
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
			return SoftCache.this.get(key);
		}

		@Override
		public V setValue( V value ) {
			return SoftCache.this.put(key, value);
		}

	}

}
