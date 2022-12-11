package schule.ngb.zm.util;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import schule.ngb.zm.util.abi.List;
import schule.ngb.zm.util.abi.Queue;

import static org.junit.jupiter.api.Assertions.*;

class FakerTest {

	@Test
	void fakeIntegers() {
		LinkedList<Integer> l = new LinkedList<>();
		Faker.fakeIntegers(100, 0, 100, l::add);
		assertEquals(100, l.size());

		LinkedList<Integer> intList = Faker.fakeIntegers(100, 0, 100, LinkedList::new, (list, i) -> list.add(i));
		assertEquals(100, intList.size());

		List<Integer> abiList = Faker.fakeIntegers(100, 0, 100, List::new, (list, i) -> list.append(i));
		assertFalse(abiList.isEmpty());

		Queue<Integer> abiQueue = new Queue<>();
		Faker.fakeIntegers(100, 0, 100, abiQueue::enqueue);
		assertFalse(abiQueue.isEmpty());
	}

}
