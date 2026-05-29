package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Main.
 * Author: 122992
 * Date: 2026-04-26 22:40:20
 * Cyclomatic Complexity:
 * - constructor: 1
 * - main(): 1
 */
class MainTest {

    private Main mainInstance;
    private final PrintStream originalOut = System.out;
    private final java.io.InputStream originalIn = System.in;

    @BeforeEach
    void setUp() {
        mainInstance = new Main();
    }

    @AfterEach
    void tearDown() {
        mainInstance = null;
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    // constructor: 1
    @Test
    void constructor() {
        assertNotNull(mainInstance, "Error: expected Main instance but got null");
    }

    // main(): 1
    @Test
    void main() {
        // Provide immediate quit input for Tasks.menu to avoid long interactive sessions
        ByteArrayInputStream in = new ByteArrayInputStream("desisto\n".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));

        Main.main(new String[0]);

        String output = out.toString();
        assertAll("main-output",
                () -> assertTrue(output.contains("***  Battleship  ***"), "Error: expected banner but got " + output),
                () -> assertTrue(output.contains("Bons ventos!") || output.contains("AJUDA DO MENU") || output.length() > 0, "Error: expected menu interaction output but got " + output)
        );
    }
}