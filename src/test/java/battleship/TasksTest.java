package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Tasks.
 * Author: 122992
 * Date: 2026-04-26 22:12:54
 * Cyclomatic Complexity:
 * - constructor: 1
 * - menu(): 6
 * - menuHelp(): 1
 * - buildFleet(): 3
 * - readShip(): 1
 * - readPosition(): 1
 * - readClassicPosition(): 4
 */
class TasksTest {

    private Tasks tasks; // Tasks contains only static methods but an instance is created per requirement

    private final PrintStream originalOut = System.out;
    private final java.io.InputStream originalIn = System.in;

    @BeforeEach
    void setUp() {
        tasks = new Tasks();
    }

    @AfterEach
    void tearDown() {
        tasks = null;
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    // constructor: 1
    @Test
    void constructor() {
        assertNotNull(tasks, "Error: expected Tasks instance but got null");
    }

    // menu(): 6 - multiple independent simple paths
    @Test
    void menu1() {
        // Path: immediate exit (user types the quit command)
        ByteArrayInputStream in = new ByteArrayInputStream("desisto\n".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));

        Tasks.menu();

        String output = out.toString();
        assertTrue(output.contains("Bons ventos!"), "Error: expected goodbye message but got " + output);
    }

    @Test
    void menu2() {
        // Path: ask for help then quit
        ByteArrayInputStream in = new ByteArrayInputStream("ajuda desisto\n".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));

        Tasks.menu();

        String output = out.toString();
        assertTrue(output.contains("AJUDA DO MENU"), "Error: expected help header but got " + output);
    }

    @Test
    void menu3() {
        // Path: unknown command then quit -> goes to default
        ByteArrayInputStream in = new ByteArrayInputStream("xyz desisto\n".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));

        Tasks.menu();

        String output = out.toString();
        assertTrue(output.contains("Que comando é esse"), "Error: expected default-case message but got " + output);
    }

    @Test
    void menu4() {
        // Path: MAPA with no fleet (should skip printing map) then quit
        ByteArrayInputStream in = new ByteArrayInputStream("mapa desisto\n".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));

        Tasks.menu();

        String output = out.toString();
        assertTrue(output.contains("Bons ventos!"), "Error: expected goodbye message but got " + output);
    }

    @Test
    void menu5() {
        // Path: RAJADA with null game (no-op) then quit
        ByteArrayInputStream in = new ByteArrayInputStream("rajada desisto\n".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));

        Tasks.menu();

        String output = out.toString();
        assertTrue(output.contains("Bons ventos!"), "Error: expected goodbye message but got " + output);
    }

    @Test
    void menu6() {
        // Path: SIMULA with null game (no-op) then quit
        ByteArrayInputStream in = new ByteArrayInputStream("simula desisto\n".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));

        Tasks.menu();

        String output = out.toString();
        assertTrue(output.contains("Bons ventos!"), "Error: expected goodbye message but got " + output);
    }

    // menuHelp(): 1
    @Test
    void menuHelp() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Tasks.menuHelp();

        String output = out.toString();
        assertTrue(output.contains("AJUDA DO MENU"), "Error: expected help header but got " + output);
    }

    // buildFleet(): 3
    @Test
    void buildFleet1() {
        // Path: empty input should cause a NoSuchElementException when readShip attempts to read
        Scanner sc = new Scanner("");
        assertThrows(NoSuchElementException.class, () -> Tasks.buildFleet(sc), "Error: expected NoSuchElementException for empty input");
    }

    @Test
    void buildFleet2() {
        // Path: incomplete ship token sequence
        Scanner sc = new Scanner("barca 1");
        assertThrows(NoSuchElementException.class, () -> Tasks.buildFleet(sc), "Error: expected NoSuchElementException for incomplete tokens");
    }

    @Test
    void buildFleet3() {
        // Path: provide 11 non-adjacent single-cell ships so buildFleet can complete
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int r = 0; r < Game.BOARD_SIZE && count < Fleet.FLEET_SIZE; r += 2) {
            for (int c = 0; c < Game.BOARD_SIZE && count < Fleet.FLEET_SIZE; c += 2) {
                sb.append("barca ").append(r).append(" ").append(c).append(" n ");
                count++;
            }
        }
        Scanner sc = new Scanner(sb.toString());
        Fleet fleet = Tasks.buildFleet(sc);
        assertEquals(Fleet.FLEET_SIZE.intValue(), fleet.getShips().size(), "Error: expected fleet size " + Fleet.FLEET_SIZE + " but got " + fleet.getShips().size());
    }

    // readShip(): 1
    @Test
    void readShip() {
        // Valid ship
        Scanner sc = new Scanner("barca 1 2 n");
        Ship s = Tasks.readShip(sc);
        assertNotNull(s, "Error: expected a Ship instance but got null");
        assertEquals("Barca", s.getCategory(), "Error: expected category Barca but got " + s.getCategory());
    }

    // readPosition(): 1
    @Test
    void readPosition() {
        Scanner sc = new Scanner("3 4");
        Position p = Tasks.readPosition(sc);
        assertAll("position",
                () -> assertEquals(3, p.getRow(), "Error: expected row 3 but got " + p.getRow()),
                () -> assertEquals(4, p.getColumn(), "Error: expected column 4 but got " + p.getColumn())
        );
    }

    // readClassicPosition(): 4
    @Test
    void readClassicPosition1() {
        // compact format A3
        Scanner sc = new Scanner("A3");
        IPosition p = Tasks.readClassicPosition(sc);
        assertEquals(new Position('A', 3), p, "Error: expected Position A3 but got " + p);
    }

    @Test
    void readClassicPosition2() {
        // separated format B 4
        Scanner sc = new Scanner("B 4");
        IPosition p = Tasks.readClassicPosition(sc);
        assertEquals(new Position('B', 4), p, "Error: expected Position B4 but got " + p);
    }

    @Test
    void readClassicPosition3() {
        // lowercase compact format c5
        Scanner sc = new Scanner("c5");
        IPosition p = Tasks.readClassicPosition(sc);
        assertEquals(new Position('C', 5), p, "Error: expected Position C5 but got " + p);
    }

    @Test
    void readClassicPosition4() {
        // invalid format should throw
        Scanner sc = new Scanner("5A");
        assertThrows(IllegalArgumentException.class, () -> Tasks.readClassicPosition(sc), "Error: expected IllegalArgumentException for invalid format");
    }
}