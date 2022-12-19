package schule.ngb.zm.util.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventDispatcherTest {

	class TestEvent {

		private String data;

		private String type;

		public TestEvent( String data, boolean isStart ) {
			this.data = data;
			this.type = isStart ? "start" : "stop";
		}

		public String getData() {
			return data;
		}

		public String getType() {
			return type;
		}

	}

	interface TestListener extends Listener<TestEvent> {

		void startEvent( TestEvent t );

		void stopEvent( TestEvent t );

	}

	@Test
	void eventRegistry() {
		EventDispatcher<TestEvent, TestListener> gen = new EventDispatcher<>();

		gen.registerEventType("start", ( event, listener ) -> listener.startEvent(event));
		gen.registerEventType("stop", ( event, listener ) -> listener.stopEvent(event));

		gen.addListener(new TestListener() {
			@Override
			public void startEvent( TestEvent t ) {
				assertEquals("start", t.getType());
				assertTrue(t.getData().startsWith("Start Event"));
			}

			@Override
			public void stopEvent( TestEvent t ) {
				assertEquals("stop", t.getType());
				assertTrue(t.getData().startsWith("Stop Event"));
			}
		});

		gen.dispatchEvent("start", new TestEvent("Start Event 1", true));
		gen.dispatchEvent("stop", new TestEvent("Stop Event 1", false));
		gen.dispatchEvent("stop", new TestEvent("Stop Event 2", false));
		gen.dispatchEvent("start", new TestEvent("Start Event 2", true));
	}

}
