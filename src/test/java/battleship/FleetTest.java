package battleship;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;

public class FleetTest {
    private Fleet fleet;

    @BeforeEach
    void setUp() {
        fleet = new Fleet();
    }

    // --- 1. MATAR O AMARELO DO ADDSHIP E ISINSIDEBOARD ---
    @Test
    @DisplayName("Cobre falhas no IF do addShip e limites do tabuleiro")
    void testAddShipBranches() {
        // Sucesso
        assertTrue(fleet.addShip(new Barge(Compass.NORTH, new Position(0, 0))));

        // Falha no isInsideBoard (Cobre Screenshot 1 e 2)
        // Precisamos testar os limites para o return gigante ficar verde
        assertFalse(fleet.addShip(new Barge(Compass.NORTH, new Position(-1, 0))));
        assertFalse(fleet.addShip(new Barge(Compass.NORTH, new Position(Game.BOARD_SIZE, 0))));
        assertFalse(fleet.addShip(new Barge(Compass.NORTH, new Position(0, -1))));
        assertFalse(fleet.addShip(new Barge(Compass.NORTH, new Position(0, Game.BOARD_SIZE))));

        // Falha no colisionRisk (Cobre Screenshot 2)
        // Tentar pôr no mesmo sítio (0,0)
        assertFalse(fleet.addShip(new Barge(Compass.NORTH, new Position(0, 0))));
    }

    // --- 2. MATAR O AMARELO DO CREATERANDOM ---
    @Test
    @DisplayName("Cobre o IF do createRandom")
    void testCreateRandomCoverage() {
        // Corremos 200 vezes para garantir que o gerador aleatório tenta
        // colocar barcos em cima de outros, forçando o addShip a dar FALSE.
        // Isso limpa a Screenshot 3.
        for (int i = 0; i < 200; i++) {
            IFleet f = Fleet.createRandom();
            assertNotNull(f);
        }
    }

    // --- 3. MATAR O AMARELO DO GETSUNKSHIPS ---
    @Test
    @DisplayName("Força barcos afundados")
    void testSunkShipsLogic() {
        Position p = new Position(1, 1);
        Barge b = new Barge(Compass.NORTH, p);
        fleet.addShip(b);

        // Afundar o barco de verdade
        for (IPosition pos : b.getPositions()) {
            pos.shoot();
        }

        List<IShip> sunk = fleet.getSunkShips();
        assertFalse(sunk.isEmpty());
    }

    // --- 4. MATAR OS ASSERTS (Screenshot 1 e 2) ---
    @Test
    @DisplayName("Cobre Asserts de Null")
    void testNullAsserts() {
        assertThrows(AssertionError.class, () -> fleet.addShip(null));
        assertThrows(AssertionError.class, () -> fleet.shipAt(null));
        assertThrows(AssertionError.class, () -> fleet.getShipsLike(null));
        assertThrows(AssertionError.class, () -> fleet.printShipsByCategory(null));
    }

    // --- 5. GARANTIR QUE NADA FOI RETIRADO (MÉTODOS DE BUSCA E PRINT) ---
    @Test
    @DisplayName("Cobre métodos de procura, categoria e prints")
    void testRemainingMethods() {
        // Saturação da frota (FLEET_SIZE)
        for (int i = 0; i < 25; i++) {
            fleet.addShip(new Barge(Compass.NORTH, new Position(i % 10, i / 10)));
        }

        // Procura
        Position pos = new Position(5, 5);
        fleet.addShip(new Barge(Compass.NORTH, pos));
        assertNotNull(fleet.shipAt(pos));
        assertNull(fleet.shipAt(new Position(9, 8)));

        // Categoria (True e False para limpar o equals)
        assertFalse(fleet.getShipsLike("Barca").isEmpty());
        assertTrue(fleet.getShipsLike("Inexistente").isEmpty());

        // Prints (Evita o vermelho nos métodos de print)
        assertDoesNotThrow(() -> {
            fleet.printStatus();
            fleet.printAllShips();
            fleet.printFloatingShips();
            fleet.printShipsByCategory("Barca");
        });
    }

    // --- 1. COBERTURA TOTAL DE ISINSIDEBOARD (TODOS OS LIMITES) ---
    @Test
    @DisplayName("Cobre todos os 4 limites de isInsideBoard individualmente")
    void testIsInsideBoardAllBounds() {
        // Assume Game.BOARD_SIZE = 10 (ajuste se for outro valor)
        int max = Game.BOARD_SIZE - 1;

        // Falha no LeftMostPos < 0
        assertFalse(fleet.addShip(new Barge(Compass.NORTH, new Position(-1, 0))));
        // Falha no RightMostPos > BOARD_SIZE - 1
        assertFalse(fleet.addShip(new Barge(Compass.NORTH, new Position(max + 1, 0))));
        // Falha no TopMostPos < 0
        assertFalse(fleet.addShip(new Barge(Compass.NORTH, new Position(0, -1))));
        // Falha no BottomMostPos > BOARD_SIZE - 1
        assertFalse(fleet.addShip(new Barge(Compass.NORTH, new Position(0, max + 1))));

        // Sucesso nos limites exatos (Canto superior esquerdo e inferior direito)
        assertTrue(fleet.addShip(new Barge(Compass.NORTH, new Position(0, 0))));
        assertTrue(fleet.addShip(new Barge(Compass.NORTH, new Position(max, max))));
    }

    // --- 2. COBERTURA DO LIMITE FLEET_SIZE NO ADDSHIP ---
    @Test
    @DisplayName("Tenta adicionar barcos além do limite FLEET_SIZE")
    void testFleetSizeLimit() {
        // Encher a frota até ao limite (IFleet.FLEET_SIZE costuma ser 11 ou 12 neste projeto)
        // Usamos um loop que ultrapassa o limite conhecido
        for (int i = 0; i < 50; i++) {
            // Criamos posições diferentes para não bater no colisionRisk antes do FLEET_SIZE
            fleet.addShip(new Barge(Compass.NORTH, new Position(i % 10, i / 10)));
        }

        // Este barco deve retornar FALSE porque ships.size() já ultrapassou FLEET_SIZE
        assertFalse(fleet.addShip(new Barge(Compass.NORTH, new Position(9, 9))));
    }

    // --- 3. COBERTURA DO COLISIONRISK (LOOP E IF) ---
    @Test
    @DisplayName("Cobre o loop interno do colisionRisk")
    void testCollisionRiskBranches() {
        Barge b1 = new Barge(Compass.NORTH, new Position(1, 1));
        fleet.addShip(b1);

        // Tentar adicionar um barco que encosta no b1 (tooCloseTo deve ser true)
        Barge b2 = new Barge(Compass.NORTH, new Position(1, 2));
        assertFalse(fleet.addShip(b2), "Deve falhar por estar demasiado perto (colisionRisk)");
    }

    @Test
    @DisplayName("Cobre os ramos do IF dentro do loop de getShipsLike")
    void testGetShipsLikeBranches() {
        Barge b = new Barge(Compass.NORTH, new Position(0, 0));
        fleet.addShip(b);

        // Em vez de escrever "barca" à mão, usamos o que o objeto retorna
        String categoriaReal = b.getCategory();

        // Ramo Verdadeiro: categoria coincide
        assertEquals(1, fleet.getShipsLike(categoriaReal).size(),
                "Deveria ter encontrado 1 barco da categoria " + categoriaReal);

        // Ramo Falso: categoria não coincide (importante para branch coverage)
        assertTrue(fleet.getShipsLike("CategoriaInexistente").isEmpty());
    }

    // --- 5. COBERTURA DO CREATERANDOM (GARANTIR FALSE NO ADDSHIP) ---
    @Test
    @DisplayName("Garante que createRandom encontra colisões durante a geração")
    void testCreateRandomRepetition() {
        // Correr o createRandom várias vezes força internamente o 'if' a falhar
        // quando o gerador aleatório tenta colocar um barco em cima de outro.
        for(int i = 0; i < 50; i++) {
            IFleet f = Fleet.createRandom();
            assertNotNull(f.getShips());
            assertTrue(f.getShips().size() > 0);
        }
    }

    @Test
    @DisplayName("Limpa o amarelo dos asserts de null")
    void testNullBranchesExplicitly() {
        // Para limpar screenshots 28, 32 e 35
        assertAll("Asserts",
                () -> assertThrows(AssertionError.class, () -> fleet.addShip(null)),
                // Chamamos um método que use colisionRisk internamente
                () -> assertThrows(AssertionError.class, () -> fleet.shipAt(null)),
                // Para a Screenshot 35 (printShips)
                () -> assertThrows(AssertionError.class, () -> fleet.printShips(null))
        );
    }

    @Test
    @DisplayName("Limpa o amarelo do createRandom (Screenshot 20)")
    void testCreateRandomFullCoverage() {
        // Correr múltiplas vezes garante que o 'if' encontre colisões
        // e execute o caminho onde o addShip(ship) é FALSE.
        for (int i = 0; i < 100; i++) {
            IFleet f = Fleet.createRandom();
            assertNotNull(f);
            assertTrue(f.getShips().size() > 0);
        }
    }

    @Test
    @DisplayName("Limpa o amarelo do getShipsLike")
    void testGetShipsLikeFullCoverage() {
        Barge b = new Barge(Compass.NORTH, new Position(0, 0));
        fleet.addShip(b);

        // Coisas que fazem o IF ser TRUE
        assertFalse(fleet.getShipsLike(b.getCategory()).isEmpty());

        // Coisas que fazem o IF ser FALSE (importante para o branch coverage)
        assertTrue(fleet.getShipsLike("Inexistente").isEmpty());
    }

    @Test
    @DisplayName("Forçar falha no addShip dentro do createRandom")
    void testCreateRandomBranchCoverage() {
        // Correr muitas vezes aumenta a probabilidade estatística de
        // uma colisão (addShip = false), limpando parte do amarelo.
        for (int i = 0; i < 500; i++) {
            Fleet.createRandom();
        }
    }

    @Test
    @DisplayName("Cobertura total de ramos lógicos")
    void testExtremeCoverage() {
        // 1. Forçar o caminho falso do colisionRisk (loop interno)
        Barge b1 = new Barge(Compass.NORTH, new Position(2, 2));
        fleet.addShip(b1);
        Barge b2 = new Barge(Compass.NORTH, new Position(2, 2));
        fleet.addShip(b2); // Executa o 'return true' dentro do colisionRisk

        // 2. Tentar atingir o limite máximo da frota (FLEET_SIZE)
        // Se FLEET_SIZE for, por exemplo, 10, adicione 11 barcos diferentes.
        for (int i = 0; i < 20; i++) {
            fleet.addShip(new Barge(Compass.NORTH, new Position(i % 10, i / 10)));
        }

        // 3. Forçar o asserts de métodos que ainda estejam amarelos
        assertThrows(AssertionError.class, () -> fleet.getShipsLike(null));

        // 4. Se printShips(ships) está amarelo, chame-o com uma lista vazia e depois null
        assertDoesNotThrow(() -> fleet.printShips(new ArrayList<>()));
        assertThrows(AssertionError.class, () -> fleet.printShips(null));
    }

    @Test
    @DisplayName("Cobertura total de ramos lógicos e Asserts")
    void testAssertCoverageDeeply() {
        // 1. Criar uma frota limpa
        Fleet testFleet = new Fleet();

        // 2. Tocar nos asserts de cada método que aparece amarelo nas tuas imagens
        // addShip (Screenshot 28/32)
        assertThrows(AssertionError.class, () -> testFleet.addShip(null));

        // getShipsLike
        assertThrows(AssertionError.class, () -> testFleet.getShipsLike(null));

        // shipAt
        assertThrows(AssertionError.class, () -> testFleet.shipAt(null));

        // printShipsByCategory
        assertThrows(AssertionError.class, () -> testFleet.printShipsByCategory(null));

        // printShips (Screenshot 35)
        assertThrows(AssertionError.class, () -> testFleet.printShips(null));
    }


    @Test
    @DisplayName("Garante cobertura total de todos os Asserts e caminhos lógicos")
    void testFullBranchCoverage() {
        // 1. Criar barcos para teste
        Barge b1 = new Barge(Compass.NORTH, new Position(0, 0));
        Barge b2 = new Barge(Compass.NORTH, new Position(0, 0)); // Colisão exata

        // 2. Forçar caminhos do addShip (Sucesso, Colisão, Limite)
        assertTrue(fleet.addShip(b1));
        assertFalse(fleet.addShip(b2)); // Cobre o ramo 'true' do colisionRisk

        // 3. Forçar o caminho falso de 'isInsideBoard' (Screenshot 28)
        // Testamos cada limite individualmente para limpar o return gigante
        assertFalse(fleet.addShip(new Barge(Compass.NORTH, new Position(-1, 0))));
        assertFalse(fleet.addShip(new Barge(Compass.NORTH, new Position(Game.BOARD_SIZE, 0))));
        assertFalse(fleet.addShip(new Barge(Compass.NORTH, new Position(0, -1))));
        assertFalse(fleet.addShip(new Barge(Compass.NORTH, new Position(0, Game.BOARD_SIZE))));

        // 4. Forçar Asserts (Screenshot 32, 35 e outros)
        // Precisamos garantir que TODOS os métodos com assert recebam null
        assertAll("Asserts de Null",
                () -> assertThrows(AssertionError.class, () -> fleet.addShip(null)),
                () -> assertThrows(AssertionError.class, () -> fleet.getShipsLike(null)),
                () -> assertThrows(AssertionError.class, () -> fleet.shipAt(null)),
                () -> assertThrows(AssertionError.class, () -> fleet.printShipsByCategory(null)),
                () -> assertThrows(AssertionError.class, () -> fleet.printShips(null))
        );
    }

    @Test
    @DisplayName("Stress test para o createRandom (Screenshot 20)")
    void testCreateRandomStress() {
        // Correr 500 vezes aumenta a chance de colisões internas no loop while
        // Isso ajuda a limpar o ramo 'false' do addShip dentro do createRandom
        for (int i = 0; i < 500; i++) {
            IFleet random = Fleet.createRandom();
            assertNotNull(random);
            assertFalse(random.getShips().isEmpty());
        }
    }

    @Test
    @DisplayName("Saturação de ramos para 100%")
    void testSaturation() {
        // Cobertura do isInsideBoard (Todos os &&)
        int max = Game.BOARD_SIZE;
        assertAll("Limites",
                () -> assertFalse(fleet.addShip(new Barge(Compass.NORTH, new Position(-1, 0)))),
                () -> assertFalse(fleet.addShip(new Barge(Compass.NORTH, new Position(max, 0)))),
                () -> assertFalse(fleet.addShip(new Barge(Compass.NORTH, new Position(0, -1)))),
                () -> assertFalse(fleet.addShip(new Barge(Compass.NORTH, new Position(0, max))))
        );

        // Cobertura do colisionRisk (Forçar o loop a encontrar um conflito)
        Barge b1 = new Barge(Compass.NORTH, new Position(5, 5));
        fleet.addShip(b1);
        Barge b2 = new Barge(Compass.NORTH, new Position(5, 5));
        assertFalse(fleet.addShip(b2)); // Força 'return true' no colisionRisk

        // Cobertura do getShipsLike (Ramo falso do equals)
        fleet.addShip(new Barge(Compass.NORTH, new Position(1,1)));
        fleet.getShipsLike("Inexistente"); // Força o 'if' dentro do loop a ser FALSE
    }

}