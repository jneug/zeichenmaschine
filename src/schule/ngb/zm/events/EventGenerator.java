package schule.ngb.zm.events;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.BiConsumer;

public class EventGenerator<E, L extends Listener<E>> {

	private CopyOnWriteArraySet<L> listeners;

	private ConcurrentMap<String, BiConsumer<E, L>> eventRegistry;

	public EventGenerator() {
		listeners = new CopyOnWriteArraySet<>();
		eventRegistry = new ConcurrentHashMap<>();
	}

	public void registerEventType( String eventKey, BiConsumer<E, L> dispatcher ) {
		if( !eventRegistered(eventKey) ) {
			eventRegistry.put(eventKey, dispatcher);
		}
	}

	public void addListener( L listener ) {
		listeners.add(listener);
	}

	public void removeListener( L listener ) {
		listeners.remove(listener);
	}

	public boolean hasListeners() {
		return !listeners.isEmpty();
	}

	public boolean eventRegistered( String eventKey ) {
		return eventRegistry.containsKey(eventKey);
	}

	public void dispatchEvent( String eventKey, final E event ) {
		if( eventRegistered(eventKey) ) {
			final BiConsumer<E, L> dispatcher = eventRegistry.get(eventKey);
			listeners.stream().forEach(( listener ) -> {
				dispatcher.accept(event, listener);
			});
		}
	}

}
