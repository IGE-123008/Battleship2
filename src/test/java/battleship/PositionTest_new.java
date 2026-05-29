package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Position.
 * Cobertura Completa: Métodos e Ramos.
 */
public class PositionTest_new {

    private Position position;

    @BeforeEach
    void setUp() {
        position = new Position(5, 5); // F6
    }

    @AfterEach
    void tearDown() {
        position = null;
    }

    // 1. MÉTODO: randomPosition()
    @Test @DisplayName("randomPosition: Cobre a geração aleatória")
    void randomPosition() {
        Position p = Position.randomPosition();
        assertNotNull(p);
        assertTrue(p.getRow() >= 0 && p.getRow() < Game.BOARD_SIZE);
    }

    // 2. MÉTODO: Position(char, int)
    @Test @DisplayName("Constructor Classic: Cobre conversão de char e Case Sensitivity")
    void constructorClassic() {
        Position p1 = new Position('a', 1); // Testar minúscula para cobrir Character.toUpperCase
        assertEquals(0, p1.getRow());
        assertEquals(0, p1.getColumn());
    }

    // 3. MÉTODO: Position(int, int)
    @Test @DisplayName("Constructor Int: Cobre inicialização direta")
    void constructorInt() {
        Position p = new Position(9, 9);
        assertEquals(9, p.getRow());
    }

    // 4 & 5. MÉTODOS: getRow() e getColumn()
    @Test @DisplayName("Getters: Cobertura de row e column")
    void testGetters() {
        assertEquals(5, position.getRow());
        assertEquals(5, position.getColumn());
    }

    // 6 & 7. MÉTODOS: getClassicRow() e getClassicColumn()
    @Test @DisplayName("Getters Clássicos: Cobertura de conversão para A-J e 1-10")
    void testClassicGetters() {
        assertEquals('F', position.getClassicRow());
        assertEquals(6, position.getClassicColumn());
    }

    // 8. MÉTODO: isInside() -> Onde costumam faltar ramos
    @Test @DisplayName("isInside: Testar os 4 limites para 100% Branch Coverage")
    void isInside() {
        assertAll("Ramos do isInside",
                () -> assertTrue(position.isInside(), "Centro deve ser true"),
                () -> assertFalse(new Position(-1, 5).isInside(), "Row negativa deve ser false"),
                () -> assertFalse(new Position(5, -1).isInside(), "Col negativa deve ser false"),
                () -> assertFalse(new Position(10, 5).isInside(), "Row acima do limite deve ser false"),
                () -> assertFalse(new Position(5, 10).isInside(), "Col acima do limite deve ser false")
        );
    }

    // 9. MÉTODO: isAdjacentTo(IPosition)
    @Test @DisplayName("isAdjacentTo: Cobertura de vizinhança")
    void isAdjacentTo() {
        assertTrue(position.isAdjacentTo(new Position(5, 6)));
        assertFalse(position.isAdjacentTo(new Position(7, 7)));
    }

    // 10. MÉTODO: adjacentPositions()
    @Test @DisplayName("adjacentPositions: Cobertura do loop e do IF isInside")
    void adjacentPositions() {
        // Canto (0,0) força o 'if (newPosition.isInside())' a ser falso em várias direções
        Position corner = new Position(0, 0);
        assertEquals(3, corner.adjacentPositions().size(), "Canto deve filtrar adjacentes fora do mapa");
        assertEquals(8, position.adjacentPositions().size(), "Centro deve ter 8");
    }

    // 11 & 13. MÉTODOS: isOccupied() e occupy()
    @Test @DisplayName("Occupation: Cobertura de isOccupied e occupy")
    void testOccupation() {
        assertFalse(position.isOccupied());
        position.occupy();
        assertTrue(position.isOccupied());
    }

    // 12 & 14. MÉTODOS: isHit() e shoot()
    @Test @DisplayName("Hit: Cobertura de isHit e shoot")
    void testShoot() {
        assertFalse(position.isHit());
        position.shoot();
        assertTrue(position.isHit());
    }

    // 15. MÉTODO: equals(Object) -> Onde faltam os ramos de instanceof e null
    @Test @DisplayName("equals: Testar todos os ramos independentes")
    void testEquals() {
        assertAll("Ramos do Equals",
                () -> assertTrue(position.equals(position), "Mesma instância"),
                () -> assertTrue(position.equals(new Position(5, 5)), "Mesmas coordenadas"),
                () -> assertFalse(position.equals(new Position(1, 1)), "Coordenadas diferentes"),
                () -> assertFalse(position.equals(null), "Comparar com null"),
                () -> assertFalse(position.equals("String"), "Tipo de objeto diferente"),
                () -> assertFalse(position.equals(new Object()), "Classe diferente")
        );
    }

    // 16. MÉTODO: hashCode()
    @Test @DisplayName("hashCode: Cobertura de geração de hash")
    void testHashCode() {
        assertNotEquals(0, position.hashCode());
        assertEquals(position.hashCode(), new Position(5, 5).hashCode());
    }

    // 17. MÉTODO: toString()
    @Test @DisplayName("toString: Cobertura da representação textual")
    void testToString() {
        assertEquals("F6", position.toString());
    }
}