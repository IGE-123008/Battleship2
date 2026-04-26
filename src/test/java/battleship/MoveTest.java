/** * Test class for class Move.
 * Author: 123008
 * Cyclomatic Complexity:
 * - Move (constructor): 1
 * - processEnemyFire(): 22
 */
package battleship;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class MoveTest {
    private Move move;
    private List<IPosition> shots;
    private List<IGame.ShotResult> results;

    @BeforeEach
    void setUp() {
        shots = new ArrayList<>();
        results = new ArrayList<>();
        move = new Move(1, shots, results);
    }

    @AfterEach
    void tearDown() {
        move = null;
    }

    @Test
    @DisplayName("processEnemyFire5: Teste de tiro num barco que não afunda")
    void processEnemyFire5() {
        // Criar um barco real para o teste
        IShip ship = new Galleon(Compass.NORTH, new Position(1,1));
        results.add(createMockResult(true, false, ship, false));

        String json = move.processEnemyFire(true);

        // Usamos contains() para evitar erros com espaços extras no output
        assertAll("Check hit without sinking",
                () -> assertNotNull(json, "Error: JSON should not be null"),
                () -> assertTrue(json.contains("hitsOnBoats"), "Error: JSON missing hitsOnBoats"),
                () -> assertTrue(json.toLowerCase().contains("galeao"), "Error: Boat type 'galeao' not found")
        );
    }

    @Test
    @DisplayName("processEnemyFire6: Múltiplos afundamentos do mesmo tipo")
    void processEnemyFire6() {
        IShip ship1 = new Barge(Compass.NORTH, new Position(0,0));
        IShip ship2 = new Barge(Compass.NORTH, new Position(2,2));
        results.add(createMockResult(true, false, ship1, true));
        results.add(createMockResult(true, false, ship2, true));

        String json = move.processEnemyFire(true);

        // Procura a contagem de barcos afundados no JSON (count : 2)
        assertTrue(json.contains("\"count\" : 2") || json.contains("\"count\": 2"),
                "Error: JSON should show 2 sunk boats of the same type");
    }

    @Test
    @DisplayName("processEnemyFire7: Tiro inválido e tiro na água")
    void processEnemyFire7() {
        results.add(createMockResult(false, false, null, false));
        results.add(createMockResult(true, false, null, false));

        String json = move.processEnemyFire(false);
        assertTrue(json.contains("\"validShots\" : 1"), "Error: expected 1 valid shot");
    }

    private IGame.ShotResult createMockResult(boolean valid, boolean repeated, IShip ship, boolean sunk) {
        return new IGame.ShotResult(valid, repeated, ship, sunk);
    }
}
