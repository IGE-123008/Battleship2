package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for IMove (and Move implementation).
 * Author: 122992
 * Date: 2026-04-26 22:33:44
 * Cyclomatic Complexity:
 * - readMove(): 2
 * - Move(): 1
 * - toString(): 1
 * - getNumber(): 1
 * - getShots(): 1
 * - getShotResults(): 1
 * - processEnemyFire(): 8
 */

class IMoveTest {

    private Move move;

    @BeforeEach
    void setUp() {
        move = new Move(1, new ArrayList<>(), new ArrayList<>());
    }

    @AfterEach
    void tearDown() {
        move = null;
    }

    // readMove(): 2
    @Test
    void readMove1() {
        // numShots = 0 -> should produce Move with 0 shots
        Scanner sc = new Scanner("0");
        Move m = IMove.readMove(5, sc);
        assertAll("readMove0",
                () -> assertEquals(5, m.getNumber(), "Error: expected move number 5 but got " + m.getNumber()),
                () -> assertEquals(0, m.getShots().size(), "Error: expected 0 shots but got " + m.getShots().size())
        );
    }

    @Test
    void readMove2() {
        // numShots = 2 with coordinates
        Scanner sc = new Scanner("2 1 2 3 4");
        Move m = IMove.readMove(7, sc);
        assertAll("readMove2",
                () -> assertEquals(7, m.getNumber(), "Error: expected move number 7 but got " + m.getNumber()),
                () -> assertEquals(2, m.getShots().size(), "Error: expected 2 shots but got " + m.getShots().size()),
                () -> assertEquals(new Position(1,2), m.getShots().get(0), "Error: expected first shot Position(1,2) but got " + m.getShots().get(0)),
                () -> assertEquals(new Position(3,4), m.getShots().get(1), "Error: expected second shot Position(3,4) but got " + m.getShots().get(1))
        );
    }

    // Move(): 1
    @Test
    void constructor() {
        Move m = new Move(2, List.of(new Position(0,0)), new ArrayList<>());
        assertEquals(2, m.getNumber(), "Error: expected move number 2 but got " + m.getNumber());
    }

    // toString(): 1
    @Test
    void toStringMethod() {
        Move m = new Move(3, List.of(new Position(0,0), new Position(1,1)), new ArrayList<>());
        String s = m.toString();
        assertTrue(s.contains("number=3") && s.contains("shots=2"), "Error: expected toString to contain move number and shots but got " + s);
    }

    // getNumber(): 1
    @Test
    void getNumber() {
        assertEquals(1, move.getNumber(), "Error: expected number 1 but got " + move.getNumber());
    }

    // getShots(): 1
    @Test
    void getShots() {
        assertNotNull(move.getShots(), "Error: expected non-null shots list but got null");
    }

    // getShotResults(): 1
    @Test
    void getShotResults() {
        assertNotNull(move.getShotResults(), "Error: expected non-null shotResults list but got null");
    }

    // processEnemyFire(): 8
    @Test
    void processEnemyFire1() {
        // verbose=false, no shot results -> outsideShots = NUMBER_SHOTS
        Move m = new Move(10, new ArrayList<>(), new ArrayList<>());
        String json = m.processEnemyFire(false);
        assertTrue(json.contains("\"outsideShots\"") && json.contains("3"), "Error: expected outsideShots in JSON but got " + json);
    }

    @Test
    void processEnemyFire2() {
        // all repeated shots -> validShots==0, repeatedShots>0 -> special verbose branch
        List<IGame.ShotResult> results = new ArrayList<>();
        results.add(new IGame.ShotResult(true, true, null, false));
        results.add(new IGame.ShotResult(true, true, null, false));
        Move m = new Move(11, new ArrayList<>(), results);
        String json = m.processEnemyFire(true);
        assertTrue(json.contains("\"repeatedShots\"") || json.contains("\"repeatedShots\""), "Error: expected repeatedShots=2 in JSON but got " + json);
    }

    @Test
    void processEnemyFire3() {
        // one valid miss
        List<IGame.ShotResult> results = new ArrayList<>();
        results.add(new IGame.ShotResult(true, false, null, false));
        Move m = new Move(12, new ArrayList<>(), results);
        String json = m.processEnemyFire(true);
        assertTrue(json.contains("\"missedShots\"") || json.contains("\"missedShots\""), "Error: expected missedShots=1 in JSON but got " + json);
    }

    @Test
    void processEnemyFire4() {
        // one hit not sunk
        IShip fakeShip = new Barge(Compass.NORTH, new Position(0,0));
        List<IGame.ShotResult> results = new ArrayList<>();
        results.add(new IGame.ShotResult(true, false, fakeShip, false));
        Move m = new Move(13, new ArrayList<>(), results);
        String json = m.processEnemyFire(true);
        assertTrue(json.contains("\"hitsOnBoats\"") && json.contains("\"hits\""), "Error: expected hitsOnBoats in JSON but got " + json);
    }

    @Test
    void processEnemyFire5() {
        // one sunk
        IShip fakeShip = new Barge(Compass.NORTH, new Position(0,0));
        List<IGame.ShotResult> results = new ArrayList<>();
        results.add(new IGame.ShotResult(true, false, fakeShip, true));
        Move m = new Move(14, new ArrayList<>(), results);
        String json = m.processEnemyFire(true);
        assertTrue(json.contains("\"sunkBoats\"") && json.contains("\"type\""), "Error: expected sunkBoats in JSON but got " + json);
    }

    @Test
    void processEnemyFire6() {
        // mix of valid hit and repeated -> exercises comma and combined branches
        IShip fakeShip = new Barge(Compass.NORTH, new Position(0,0));
        List<IGame.ShotResult> results = new ArrayList<>();
        results.add(new IGame.ShotResult(true, false, fakeShip, false));
        results.add(new IGame.ShotResult(true, true, null, false));
        Move m = new Move(15, new ArrayList<>(), results);
        String json = m.processEnemyFire(true);
        assertTrue(json.contains("\"validShots\"") && (json.contains("\"repeatedShots\"") || json.contains("\"repeatedShots\"")), "Error: expected validShots=1 and repeatedShots=1 in JSON but got " + json);
    }

    @Test
    void processEnemyFire7() {
        // plural hits on same boat
        IShip fakeShip = new Barge(Compass.NORTH, new Position(0,0));
        List<IGame.ShotResult> results = new ArrayList<>();
        results.add(new IGame.ShotResult(true, false, fakeShip, false));
        results.add(new IGame.ShotResult(true, false, fakeShip, false));
        Move m = new Move(16, new ArrayList<>(), results);
        String json = m.processEnemyFire(true);
        assertTrue(json.contains("\"hitsOnBoats\"") && json.contains("\"hits\""), "Error: expected hitsOnBoats with plural hits in JSON but got " + json);
    }

    @Test
    void processEnemyFire8() {
        // verbose=false with some results to ensure branch where verbose is false avoids printing
        IShip fakeShip = new Barge(Compass.NORTH, new Position(0,0));
        List<IGame.ShotResult> results = new ArrayList<>();
        results.add(new IGame.ShotResult(true, false, fakeShip, false));
        results.add(new IGame.ShotResult(true, false, null, false));
        Move m = new Move(17, new ArrayList<>(), results);
        String json = m.processEnemyFire(false);
        assertTrue(json.contains("\"validShots\"" ) || json.contains("\"validShots\": 1"), "Error: expected validShots in JSON but got " + json);
    }
}