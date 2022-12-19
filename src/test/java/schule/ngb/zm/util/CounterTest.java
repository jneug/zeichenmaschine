package schule.ngb.zm.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CounterTest {

	@Test
	void counter() {
		Counter cnt = new Counter();
		assertEquals(0, cnt.getCount());
		assertEquals(Double.NaN, cnt.getMax());
		assertEquals(Double.NaN, cnt.getMin());
		assertEquals(Double.NaN, cnt.getSum());
	}

	@Test
	void getCount() {
		Counter cnt = new Counter();
		assertEquals(0, cnt.getCount());
		cnt.inc();
		assertEquals(1, cnt.getCount());
		cnt.inc(); cnt.inc();
		assertEquals(3, cnt.getCount());
		cnt.setCount(33);
		assertEquals(33, cnt.getCount());
	}

	@Test
	void getMax() {
		Counter cnt = new Counter();
		assertEquals(Double.NaN, cnt.getMax());
		cnt.count(2);
		assertEquals(2, cnt.getMax());
		cnt.count(8);
		assertEquals(8, cnt.getMax());
		cnt.count(4);
		assertEquals(8, cnt.getMax());
	}

	@Test
	void getMin() {
		Counter cnt = new Counter();
		assertEquals(Double.NaN, cnt.getMin());
		cnt.count(2);
		assertEquals(2, cnt.getMin(), "min(2)");
		cnt.count(8);
		assertEquals(2, cnt.getMin(), "min(2,8)");
		cnt.count(1);
		assertEquals(1, cnt.getMin(), "min(2,8,1)");
	}

	@Test
	void getSum() {
		Counter cnt = new Counter();
		assertEquals(Double.NaN, cnt.getSum());
		cnt.count(2);
		assertEquals(2, cnt.getSum());
		cnt.count(8);
		assertEquals(10, cnt.getSum());
		cnt.count(-1);
		assertEquals(9, cnt.getSum());
	}

	@Test
	void reset() {
		Counter cnt = new Counter();
		cnt.inc();
		cnt.count(2);
		assertEquals(2, cnt.getCount());
		assertEquals(2, cnt.getSum());
		cnt.reset();
		assertEquals(0, cnt.getCount());
		assertEquals(Double.NaN, cnt.getSum());
	}

	@Test
	void inc() {
		Counter cnt = new Counter();
		assertEquals(0, cnt.getCount());
		cnt.inc();
		assertEquals(1, cnt.getCount());
		cnt.inc().inc();
		assertEquals(3, cnt.getCount());
	}

	@Test
	void dec() {
		Counter cnt = new Counter();
		assertEquals(0, cnt.getCount());
		cnt.dec();
		assertEquals(-1, cnt.getCount());
		cnt.dec().dec();
		assertEquals(-3, cnt.getCount());
	}

}
