package battleship;

import org.junit.jupiter.api.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Compass.
 * Author: britoeabreu
 * Date: 2023-10-10
 * Time: 15:30
 * Cyclomatic Complexity for each method:
 * - Constructor: 1
 * - getDirection: 1
 * - toString: 1
 * - charToCompass: 4
 */
public class CompassTest {

	private Compass compass;

	@BeforeEach
	void setUp() {
		// Use a concrete enum instance for tests
		compass = Compass.NORTH;
	}

	@AfterEach
	void tearDown() {
		compass = null;
	}

	// constructor: 1
	@Test
	void constructor() {
		assertNotNull(Compass.values(), "Error: expected enum values array but got null");
	}

	// randomBearing(): 1
	@Test
	void randomBearing() {
		Compass b = Compass.randomBearing();
		// Ensure returned bearing is one of the enum values
		assertTrue(Arrays.asList(Compass.values()).contains(b), "Error: expected one of " + Arrays.toString(Compass.values()) + " but got " + b);
	}

	// getDirection(): 1
	@Test
	void getDirection() {
		char d = compass.getDirection();
		assertEquals('n', d, "Error: expected direction 'n' for NORTH but got " + d);
	}

	// toString(): 1
	@Test
	void toStringMethod() {
		String s = compass.toString();
		assertEquals("n", s, "Error: expected toString() 'n' for NORTH but got " + s);
	}

	// charToCompass(): 5
	@Test
	void charToCompass1() {
		Compass c = Compass.charToCompass('n');
		assertEquals(Compass.NORTH, c, "Error: expected Compass.NORTH for 'n' but got " + c);
	}

	@Test
	void charToCompass2() {
		Compass c = Compass.charToCompass('s');
		assertEquals(Compass.SOUTH, c, "Error: expected Compass.SOUTH for 's' but got " + c);
	}

	@Test
	void charToCompass3() {
		Compass c = Compass.charToCompass('e');
		assertEquals(Compass.EAST, c, "Error: expected Compass.EAST for 'e' but got " + c);
	}

	@Test
	void charToCompass4() {
		Compass c = Compass.charToCompass('o');
		assertEquals(Compass.WEST, c, "Error: expected Compass.WEST for 'o' but got " + c);
	}

	@Test
	void charToCompass5() {
		Compass c = Compass.charToCompass('x');
		assertNull(c, "Error: expected null for unknown char but got " + c);
	}

}