package battleship;

import java.util.List;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

	private Game game;

	@BeforeEach
	void setUp() {
		game = new Game(new Fleet());
	}

	@AfterEach
	void tearDown() {
		game = null;
	}

	@Test
	@DisplayName("Deve criar corretamente uma instância de Game")
	void constructor() {
		assertNotNull(game);
		assertNotNull(game.getMyFleet());
		assertNotNull(game.getAlienFleet());
		assertNotNull(game.getMyMoves());
		assertNotNull(game.getAlienMoves());
		assertTrue(game.getMyMoves().isEmpty());
		assertTrue(game.getAlienMoves().isEmpty());
		assertEquals(0, game.getInvalidShots());
		assertEquals(0, game.getRepeatedShots());
		assertEquals(0, game.getHits());
		assertEquals(0, game.getSunkShips());
	}

	@Test
	@DisplayName("Deve devolver a frota do jogador")
	void getMyFleet() {
		assertNotNull(game.getMyFleet());
	}

	@Test
	@DisplayName("Deve devolver a frota inimiga")
	void getAlienFleet() {
		assertNotNull(game.getAlienFleet());
	}

	@Test
	@DisplayName("Deve devolver a lista de movimentos do jogador")
	void getMyMoves() {
		assertNotNull(game.getMyMoves());
		assertTrue(game.getMyMoves().isEmpty());
	}

	@Test
	@DisplayName("Deve devolver a lista de movimentos inimigos")
	void getAlienMoves() {
		assertNotNull(game.getAlienMoves());
		assertTrue(game.getAlienMoves().isEmpty());
	}

	@Test
	@DisplayName("Deve devolver zero tiros repetidos inicialmente")
	void getRepeatedShots() {
		assertEquals(0, game.getRepeatedShots());
	}

	@Test
	@DisplayName("Deve devolver zero tiros inválidos inicialmente")
	void getInvalidShots() {
		assertEquals(0, game.getInvalidShots());
	}

	@Test
	@DisplayName("Deve devolver zero acertos inicialmente")
	void getHits() {
		assertEquals(0, game.getHits());
	}

	@Test
	@DisplayName("Deve devolver zero navios afundados inicialmente")
	void getSunkShips() {
		assertEquals(0, game.getSunkShips());
	}

	@Test
	@DisplayName("Deve devolver zero navios restantes inicialmente")
	void getRemainingShipsInitially() {
		assertEquals(0, game.getRemainingShips());
	}

	@Test
	@DisplayName("Deve contar navios restantes corretamente")
	void getRemainingShips() {
		IFleet fleet = game.getMyFleet();

		Ship ship1 = new Barge(Compass.NORTH, new Position(1, 1));
		Ship ship2 = new Frigate(Compass.EAST, new Position(5, 5));

		fleet.addShip(ship1);
		assertEquals(1, game.getRemainingShips());

		fleet.addShip(ship2);
		assertEquals(2, game.getRemainingShips());

		ship2.sink();
		assertEquals(1, game.getRemainingShips());
	}

	@Test
	@DisplayName("Deve identificar posição não repetida")
	void repeatedShotFalse() {
		Position position = new Position(2, 3);

		assertFalse(game.repeatedShot(position));
	}

	@Test
	@DisplayName("Deve identificar posição repetida depois de disparar")
	void repeatedShotTrue() {
		List<IPosition> positions = List.of(
				new Position(2, 3),
				new Position(2, 4),
				new Position(2, 5)
		);

		game.fireShots(positions);

		assertTrue(game.repeatedShot(new Position(2, 3)));
	}

	@Test
	@DisplayName("Deve registar tiros efetuados")
	void fireShots() {
		List<IPosition> positions = List.of(
				new Position(1, 1),
				new Position(1, 2),
				new Position(1, 3)
		);

		game.fireShots(positions);

		assertFalse(game.getAlienMoves().isEmpty());
	}

	@Test
	@DisplayName("Deve aumentar contador de tiros inválidos")
	void fireSingleShotInvalid() {
		Position invalidPosition = new Position(-1, 5);

		game.fireSingleShot(invalidPosition, false);

		assertEquals(1, game.getInvalidShots());
	}

	@Test
	@DisplayName("Deve aumentar contador de tiros repetidos")
	void fireSingleShotRepeated() {
		Position position = new Position(2, 3);

		game.fireSingleShot(position, false);
		game.fireSingleShot(position, true);

		assertEquals(1, game.getRepeatedShots());
	}

	@Test
	@DisplayName("Deve disparar uma posição válida sem lançar exceções")
	void fireSingleShotValid() {
		Position position = new Position(3, 3);

		assertDoesNotThrow(() -> game.fireSingleShot(position, false));
	}

	@Test
	@DisplayName("Deve gerar tiro aleatório inimigo")
	void randomEnemyFire() {
		String result = game.randomEnemyFire();

		assertNotNull(result);
	}

	@Test
	@DisplayName("Deve converter tiros para JSON")
	void jsonShots() {
		List<IPosition> shots = List.of(
				new Position(1, 1),
				new Position(2, 2)
		);

		String json = game.jsonShots(shots);

		assertNotNull(json);
		assertFalse(json.isEmpty());
	}

	@Test
	@DisplayName("Deve imprimir o tabuleiro sem lançar exceções")
	void printBoard() {
		assertDoesNotThrow(() ->
				game.printBoard(game.getMyFleet(), game.getMyMoves(), true, true)
		);
	}

	@Test
	@DisplayName("Deve executar over sem lançar exceções")
	void over() {
		assertDoesNotThrow(() -> game.over());
	}
}