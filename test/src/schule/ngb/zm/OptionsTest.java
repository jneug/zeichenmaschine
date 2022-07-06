package schule.ngb.zm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static schule.ngb.zm.Options.Direction.*;

class OptionsTest {

	@Test
	void testDirections() {
		assertTrue(NORTH.in(NORTH));
		assertTrue(NORTH.in(NORTHEAST));
		assertTrue(EAST.in(NORTHEAST));
		assertTrue(NORTH.in(NORTHWEST));
		assertTrue(WEST.in(NORTHWEST));
		assertFalse(NORTH.in(SOUTH));
		assertFalse(NORTH.in(EAST));
		assertFalse(NORTH.in(WEST));
		assertFalse(NORTH.in(SOUTHWEST));
		assertFalse(NORTH.in(SOUTHEAST));

		assertFalse(NORTHEAST.in(NORTH));
		assertFalse(NORTHEAST.in(SOUTH));
		assertFalse(NORTHEAST.in(NORTHWEST));
		assertTrue(NORTHEAST.in(NORTHEAST));

		assertTrue(SOUTH.contains(SOUTH));
		assertFalse(SOUTH.contains(NORTH));
		assertFalse(SOUTH.contains(EAST));
		assertFalse(SOUTH.contains(WEST));
		assertFalse(SOUTH.contains(NORTHWEST));
		assertFalse(SOUTH.contains(SOUTHWEST));
		assertFalse(SOUTH.contains(SOUTHEAST));

		assertTrue(SOUTHEAST.contains(SOUTH));
		assertTrue(SOUTHEAST.contains(EAST));
		assertTrue(SOUTHWEST.contains(SOUTH));
		assertTrue(SOUTHWEST.contains(WEST));
		assertFalse(SOUTHWEST.contains(NORTH));
		assertFalse(SOUTHWEST.contains(SOUTHEAST));
		assertTrue(SOUTHWEST.contains(SOUTHWEST));
		assertFalse(SOUTHWEST.contains(EAST));
		assertFalse(SOUTHWEST.contains(NORTHWEST));
	}

	@Test
	public void testOppositeDirection() {
		assertEquals(SOUTH, NORTH.inverse());
		assertEquals(NORTH, SOUTH.inverse());
		assertEquals(WEST, EAST.inverse());
		assertEquals(EAST, WEST.inverse());

		assertEquals(SOUTHEAST, NORTHWEST.inverse());
	}

}
