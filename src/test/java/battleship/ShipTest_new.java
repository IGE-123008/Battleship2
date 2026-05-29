package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ShipTest_new {

    private Ship ship;
    private Position pos;

    @BeforeEach
    void setUp() {
        pos = new Position(5, 5);
        ship = new Frigate(Compass.NORTH, pos);
    }

    @AfterEach
    void tearDown() {
        ship = null;
    }

    // --- 100% BUILD SHIP ---
    @Test @DisplayName("buildShip: Todos os ramos")
    void buildShipFull() {
        assertAll("Fábrica",
                () -> assertTrue(Ship.buildShip("barca", Compass.NORTH, pos) instanceof Barge),
                () -> assertTrue(Ship.buildShip("caravela", Compass.NORTH, pos) instanceof Caravel),
                () -> assertTrue(Ship.buildShip("nau", Compass.NORTH, pos) instanceof Carrack),
                () -> assertTrue(Ship.buildShip("fragata", Compass.NORTH, pos) instanceof Frigate),
                () -> assertTrue(Ship.buildShip("galeao", Compass.NORTH, pos) instanceof Galleon),
                () -> assertNull(Ship.buildShip("invalido", Compass.NORTH, pos))
        );
    }

    // --- 100% BORDAS (TOP, BOTTOM, LEFT, RIGHT) ---
    @Test @DisplayName("Bordas: Testa todas as direções para limpar os switches")
    void testAllBoundaries() {
        Ship s = new Frigate(Compass.SOUTH, new Position(5, 5));
        Ship e = new Frigate(Compass.EAST, new Position(5, 5));
        Ship w = new Frigate(Compass.WEST, new Position(5, 5));

        // North
        assertEquals(5, ship.getTopMostPos());
        assertEquals(8, ship.getBottomMostPos());
        // South
        assertTrue(s.getTopMostPos() <= s.getBottomMostPos());
        // East
        assertEquals(5, e.getLeftMostPos());
        assertEquals(8, e.getRightMostPos());
        // West
        assertTrue(w.getLeftMostPos() <= w.getRightMostPos());
    }

    // --- 100% ADJACENTES E PROXIMIDADE ---
    @Test @DisplayName("Adjacentes: Caso no canto para forçar limites")
    void testAdjacentInCorner() {
        // Um barco no canto (0,0) faz com que muitas posições adjacentes sejam inválidas
        // Isto força os ramos 'falsos' dos teus loops de adjacência
        Ship cornerShip = new Frigate(Compass.NORTH, new Position(0, 0));
        List<IPosition> adj = cornerShip.getAdjacentPositions();
        assertNotNull(adj);

        // Testa proximidade True e False
        assertTrue(cornerShip.tooCloseTo(new Position(0, 1)));
        assertFalse(cornerShip.tooCloseTo(new Position(9, 9)));
    }

    @Test @DisplayName("tooCloseTo: Barco vs Barco")
    void testTooCloseShip() {
        Ship ship2 = new Frigate(Compass.NORTH, new Position(5, 6)); // Colado
        Ship ship3 = new Frigate(Compass.NORTH, new Position(0, 0)); // Longe
        assertTrue(ship.tooCloseTo(ship2), "Deve dar true para barco colado");
        assertFalse(ship.tooCloseTo(ship3), "Deve dar false para barco longe");
    }

    // --- 100% ESTADO (SHOOT, SINK, FLOATING, OCCUPIES) ---
    @Test @DisplayName("Estado: Ramos de vida e ocupação")
    void testStateBranches() {
        // Occupies
        assertTrue(ship.occupies(new Position(5, 5)));
        assertFalse(ship.occupies(new Position(0, 0)));

        // Shoot e Floating
        assertTrue(ship.stillFloating());
        ship.shoot(new Position(5, 5)); // Tiro certeiro
        ship.shoot(new Position(0, 0)); // Tiro na água (ramo falso do if no shoot)

        ship.sink();
        assertFalse(ship.stillFloating());
    }

    // --- GETTERS E TOSTRING ---
    @Test @DisplayName("Getters e ToString")
    void testMisc() {
        assertEquals("Fragata", ship.getCategory());
        assertEquals(4, ship.getSize());
        assertEquals(pos, ship.getPosition());
        assertEquals(Compass.NORTH, ship.getBearing());
        assertNotNull(ship.getPositions());
        assertEquals("[Fragata n F6]", ship.toString());
    }
}