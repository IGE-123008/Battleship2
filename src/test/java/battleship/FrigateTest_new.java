package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Frigate.
 * Author: Raquel Almeida 122982
 * Date: 2026-04-26
 * Cyclomatic Complexity:
 * - Frigate (Constructor): 4 (um para cada rumo do Compass)
 */
public class FrigateTest_new {

    private IPosition startPos;

    @BeforeEach
    void setUp() {
        startPos = new Position(2, 2); // C3
    }

    @AfterEach
    void tearDown() {
        startPos = null;
    }

    @Test
    @DisplayName("Frigate1: Construtor com rumo NORTH")
    void frigateNorth() {
        Frigate f = new Frigate(Compass.NORTH, startPos);
        assertAll("Verificação NORTH",
                () -> assertEquals(4, f.getPositions().size(), "Deve ter 4 posições"),
                () -> assertEquals(2, f.getPositions().get(0).getRow(), "Primeira row deve ser 2"),
                () -> assertEquals(5, f.getPositions().get(3).getRow(), "Última row deve ser 5")
        );
    }

    @Test
    @DisplayName("Frigate2: Construtor com rumo SOUTH")
    void frigateSouth() {
        Frigate f = new Frigate(Compass.SOUTH, startPos);
        // No teu código, SOUTH faz o mesmo que NORTH (pos.getRow() + r)
        assertAll("Verificação SOUTH",
                () -> assertEquals(4, f.getPositions().size()),
                () -> assertEquals(5, f.getPositions().get(3).getRow())
        );
    }

    @Test
    @DisplayName("Frigate3: Construtor com rumo EAST")
    void frigateEast() {
        Frigate f = new Frigate(Compass.EAST, startPos);
        assertAll("Verificação EAST",
                () -> assertEquals(4, f.getPositions().size()),
                () -> assertEquals(2, f.getPositions().get(0).getColumn(), "Primeira col deve ser 2"),
                () -> assertEquals(5, f.getPositions().get(3).getColumn(), "Última col deve ser 5")
        );
    }

    @Test
    @DisplayName("Frigate4: Construtor com rumo WEST")
    void frigateWest() {
        Frigate f = new Frigate(Compass.WEST, startPos);
        // No teu código, WEST faz o mesmo que EAST (pos.getColumn() + c)
        assertAll("Verificação WEST",
                () -> assertEquals(4, f.getPositions().size()),
                () -> assertEquals(5, f.getPositions().get(3).getColumn())
        );
    }

    @Test
    @DisplayName("Verificação de Categoria e Tamanho")
    void frigateBasics() {
        Frigate f = new Frigate(Compass.NORTH, startPos);
        assertAll("Atributos base",
                () -> assertEquals("Fragata", f.getCategory(), "Categoria deve ser Fragata"),
                () -> assertEquals(4, f.getSize(), "Tamanho deve ser 4")
        );
    }
}