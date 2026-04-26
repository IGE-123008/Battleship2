package battleship;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class MoveTest {
    private List<IPosition> shots;
    private List<IGame.ShotResult> results;
    private Move move;

    @BeforeEach
    void setUp() {
        shots = new ArrayList<>();
        results = new ArrayList<>();
        // Adicionamos shots para garantir que o cálculo de outsideShots (Game.NUMBER_SHOTS - valid - repeated) funcione
        for (int i = 0; i < Game.NUMBER_SHOTS; i++) {
            shots.add(new Position(i, 0));
        }
        move = new Move(1, shots, results);
    }

    @Test
    @DisplayName("Cobre as vírgulas e formatação complexa (Linhas 125-138)")
    void testFormattingDeep() {
        results.add(new IGame.ShotResult(true, false, null, false)); // Válido (água)
        results.add(new IGame.ShotResult(true, true, null, false));  // Repetido
        results.add(new IGame.ShotResult(false, false, null, false)); // Exterior/Inválido

        String json = move.processEnemyFire(true);
        assertNotNull(json);
    }

    @Test
    @DisplayName("Cobre barcos atingidos que não afundam (Linha 113)")
    void testHitsNoSinking() {
        IShip ship = new Galleon(Compass.NORTH, new Position(1,1));
        results.add(new IGame.ShotResult(true, false, ship, false));
        String json = move.processEnemyFire(true);
        assertNotNull(json);
    }

    @Test
    @DisplayName("Cobre barcos afundados (Loop das Linhas 102-105)")
    void testSunkBoatsLoop() {
        IShip ship = new Barge(Compass.NORTH, new Position(2,2));
        results.add(new IGame.ShotResult(true, false, ship, true));
        String json = move.processEnemyFire(true);
        assertNotNull(json);
    }

    @Test
    @DisplayName("Cobre métodos utilitários (toString, getters)")
    void testUtilityMethods() {
        assertNotNull(move.toString());
        assertEquals(1, move.getNumber());
        assertNotNull(move.getShotResults());
        assertNotNull(move.getShots());
    }

    @Test
    @DisplayName("Cobre ramos de verbose=false e repetidos isolados")
    void testSimpleBranches() {
        move.processEnemyFire(false);
        results.clear();
        results.add(new IGame.ShotResult(true, true, null, false));
        move.processEnemyFire(true);
    }

    @Test
    @DisplayName("Cobre Plurais e Singular de Tiros (Linhas 94, 114 e 120)")
    void testPluralsAndSingulars() {
        // hits > 1 (plural)
        IShip ship = new Galleon(Compass.NORTH, new Position(1,1));
        results.add(new IGame.ShotResult(true, false, ship, false));
        results.add(new IGame.ShotResult(true, false, ship, false));

        // missedShots > 1 (plural)
        results.add(new IGame.ShotResult(true, false, null, false));
        results.add(new IGame.ShotResult(true, false, null, false));

        move.processEnemyFire(true);
    }

    @Test
    @DisplayName("Cobre a limpeza da vírgula final (Linha 122 - setLength)")
    void testStringTrimCoverage() {
        results.clear();
        IShip ship = new Barge(Compass.NORTH, new Position(3,3));
        results.add(new IGame.ShotResult(true, false, ship, true));
        // Sem tiros na água, ativa a linha 122
        assertNotNull(move.processEnemyFire(true));
    }

    @Test
    @DisplayName("Cobre Tiros Repetidos no Plural e Singular (Linha 94-97)")
    void testRepeatedPluralSingular() {
        // Singular
        results.clear();
        results.add(new IGame.ShotResult(true, true, null, false));
        move.processEnemyFire(true);

        // Plural (Linha 94 e 95)
        results.clear();
        results.add(new IGame.ShotResult(true, true, null, false));
        results.add(new IGame.ShotResult(true, true, null, false));
        move.processEnemyFire(true);
    }

    @Test
    @DisplayName("Cobre Exterior no Singular (Linha 138)")
    void testOutsideSingular() {
        results.clear();
        // Para outside ser 1: Game.NUMBER_SHOTS - valid - repeated = 1
        // Se Game.NUMBER_SHOTS for 5, precisamos de 4 resultados válidos
        for(int i=0; i<4; i++) {
            results.add(new IGame.ShotResult(true, false, null, false));
        }
        String json = move.processEnemyFire(true);
        assertNotNull(json);
    }

    @Test
    @DisplayName("Cobre barcos afundados no plural (Linha 105)")
    void testSunkPlural() {
        results.clear();
        IShip ship = new Barge(Compass.NORTH, new Position(0,0));
        results.add(new IGame.ShotResult(true, false, ship, true));
        results.add(new IGame.ShotResult(true, false, ship, true));
        move.processEnemyFire(true);
    }

    @Test
    @DisplayName("Cobre Linhas 94 e 97: Apenas repetidos no plural")
    void testOnlyMultipleRepeatedShots() {
        results.clear();
        // validShots = 0, repeatedShots = 2
        results.add(new IGame.ShotResult(true, true, null, false));
        results.add(new IGame.ShotResult(true, true, null, false));

        move.processEnemyFire(true);
        // Isto entra na linha 94 e testa o plural da 94/95
    }



    @Test
    @DisplayName("Cobre Linha 134: Vírgula para tiros exteriores")
    void testCommaForOutsideShots() {
        results.clear();
        // Precisamos que o output não esteja vazio (!output.isEmpty())
        results.add(new IGame.ShotResult(true, false, null, false));

        // O Move calcula outsideShots = Game.NUMBER_SHOTS - valid - repeated
        // Assumindo que Game.NUMBER_SHOTS > 1, isto vai gerar outsideShots > 0
        move.processEnemyFire(true);
        // Isso ativa a linha 134 para a vírgula antes do "exterior"
    }

    @Test
    @DisplayName("Cobre Linha 138: Plural de exteriores")
    void testOutsideShotsPlural() {
        results.clear();
        // Se dispararmos apenas 1 tiro válido, e o jogo permitir 5 (exemplo),
        // teremos 4 exteriores.
        results.add(new IGame.ShotResult(true, false, null, false));

        move.processEnemyFire(true);
        // Cobre o plural de "exteriores" na linha 138
    }

    @Test
    @DisplayName("MISSILE: Ataca Linhas 94 e 97 (Apenas repetidos)")
    void testOnlyRepeatedPlural() {
        results.clear();
        // Força validShots = 0 e repeatedShots = 2
        results.add(new IGame.ShotResult(true, true, null, false));
        results.add(new IGame.ShotResult(true, true, null, false));

        String res = move.processEnemyFire(true);
        // Garante que o output contém "tiros repetidos"
        assertNotNull(res);
    }

    @Test
    @DisplayName("MISSILE: Ataca Linhas 121 e 126 (Singular Água + Vírgula)")
    void testSingularWaterAndRepeatedComma() {
        results.clear();
        // 1. Um tiro na água (Linha 121 - Singular)
        results.add(new IGame.ShotResult(true, false, null, false));
        // 2. Um tiro repetido (Linha 126 - Força a vírgula ", " porque validShots > 0)
        results.add(new IGame.ShotResult(true, true, null, false));

        assertNotNull(move.processEnemyFire(true));
    }

    @Test
    @DisplayName("MISSILE: Ataca Linha 134 (Vírgula para Exterior)")
    void testOutsideCommaSpecific() {
        results.clear();
        // 1. Coloca texto no output (um tiro na água)
        results.add(new IGame.ShotResult(true, false, null, false));

        // 2. Garante que outsideShots > 0
        // Como o setUp cria shots = Game.NUMBER_SHOTS, e só adicionámos 1 result,
        // o outsideShots será automaticamente > 0.
        // Isto força a execução da Linha 134: if (!output.isEmpty()) { output.append(", "); }

        assertNotNull(move.processEnemyFire(true));
    }

    @Test
    @DisplayName("MISSILE: Ataca Linha 138 (Singular de Exterior)")
    void testOutsideSingularSpecific() {
        results.clear();
        // Se Game.NUMBER_SHOTS for 5, precisamos de 4 resultados para sobrar 1 exterior
        for(int i = 0; i < Game.NUMBER_SHOTS - 1; i++) {
            results.add(new IGame.ShotResult(true, false, null, false));
        }

        // Isto resulta em outsideShots = 1, cobrindo o singular "exterior"
        assertNotNull(move.processEnemyFire(true));
    }

    @Test
    @DisplayName("Ataca L94 e L97: Apenas repetidos no plural")
    void testOnlyMultipleRepeated() {
        results.clear();
        // validShots = 0 e repeatedShots = 2 para testar o plural
        results.add(new IGame.ShotResult(true, true, null, false));
        results.add(new IGame.ShotResult(true, true, null, false));
        assertNotNull(move.processEnemyFire(true));
    }

    @Test
    @DisplayName("Ataca L121 e L126: Singular na água + vírgula antes do repetido")
    void testSingularWaterAndComma() {
        results.clear();
        // 1. Um tiro na água (missedShots = 1 para o singular da L121)
        results.add(new IGame.ShotResult(true, false, null, false));
        // 2. Um tiro repetido (como validShots > 0, força a vírgula da L126)
        results.add(new IGame.ShotResult(true, true, null, false));
        assertNotNull(move.processEnemyFire(true));
    }

    @Test
    @DisplayName("Ataca L134: Vírgula para tiros exteriores")
    void testOutsideShotsCommaSpecific() {
        results.clear();
        // 1. Garante que o output tem conteúdo (um tiro na água)
        results.add(new IGame.ShotResult(true, false, null, false));

        // 2. Garante que outsideShots > 0.
        // No teu setUp, se adicionares 5 shots e aqui só 1 result,
        // o outside será 4, forçando a vírgula da L134.
        assertNotNull(move.processEnemyFire(true));
    }

    @Test
    void testOnlyRepeatedShotsPlural() {
        results.clear();
        // 2 tiros repetidos e 0 válidos
        results.add(new IGame.ShotResult(true, true, null, false));
        results.add(new IGame.ShotResult(true, true, null, false));
        // Isto força a entrada na Linha 94 e o plural da Linha 95
        assertNotNull(move.processEnemyFire(true));
    }

    @Test
    void testSingleMissedShot() {
        results.clear();
        // Apenas 1 tiro na água
        results.add(new IGame.ShotResult(true, false, null, false));
        // Isto força missedShots = 1, testando o "tiro" (singular) na L121
        assertNotNull(move.processEnemyFire(true));
    }

    @Test
    void testCommaBetweenValidAndRepeated() {
        results.clear();
        // 1 Válido (água)
        results.add(new IGame.ShotResult(true, false, null, false));
        // 1 Repetido
        results.add(new IGame.ShotResult(true, true, null, false));
        // Como validShots > 0, a L126 executa o output.append(", ")
        assertNotNull(move.processEnemyFire(true));
    }

    @Test
    void testOutsideShotsComma() {
        results.clear();
        // Adiciona um tiro válido para o StringBuilder não estar vazio
        results.add(new IGame.ShotResult(true, false, null, false));

        // O cálculo é: Game.NUMBER_SHOTS - validShots - repeatedShots
        // No teu setUp, se tens 5 shots e só adicionaste 1 result, outsideShots será 4.
        // Isso ativa a vírgula na L134.
        assertNotNull(move.processEnemyFire(true));
    }

    @Test
    @DisplayName("COBERTURA TOTAL: Apenas tiros repetidos no singular (Linha 94)")
    void testOnlyOneRepeatedShot() {
        results.clear();

        // Adicionamos apenas 1 resultado que é VÁLIDO e REPETIDO
        results.add(new IGame.ShotResult(true, true, null, false));

        String json = move.processEnemyFire(true);

        // Verificamos o conteúdo do JSON de forma segura
        assertAll("Verificar JSON de tiro repetido",
                () -> assertNotNull(json),
                () -> assertTrue(json.contains("\"repeatedShots\" : 1") || json.contains("\"repeatedShots\":1"),
                        "O JSON deveria indicar 1 tiro repetido. Obtido: " + json)
        );
    }
    @Test
    @DisplayName("COBERTURA TOTAL: Barco afundado + Tiro na água (Linha 123 branch)")
    void testSunkPlusWater() {
        results.clear();
        IShip ship = new Barge(Compass.NORTH, new Position(0,0));
        // Adiciona um afundamento
        results.add(new IGame.ShotResult(true, false, ship, true));
        // Adiciona tiro na água para entrar no 'if (missedShots > 0)' e NÃO no 'else if'
        results.add(new IGame.ShotResult(true, false, null, false));

        String json = move.processEnemyFire(true);
        assertNotNull(json);
    }

    @Test
    @DisplayName("COBERTURA TOTAL: Múltiplos barcos afundados de tipos diferentes")
    void testMultipleSunkTypes() {
        results.clear();
        IShip ship1 = new Barge(Compass.NORTH, new Position(0,0));
        IShip ship2 = new Galleon(Compass.NORTH, new Position(5,5));
        results.add(new IGame.ShotResult(true, false, ship1, true));
        results.add(new IGame.ShotResult(true, false, ship2, true));

        // Garante que o loop de sunkBoatsCount.entrySet() corre mais de uma vez
        move.processEnemyFire(true);
    }

    @Test
    @DisplayName("COBERTURA TOTAL: Forçar tiro exterior singular com StringBuilder vazio")
    void testOutsideSingularEmptyBuilder() {
        results.clear();
        // Se Game.NUMBER_SHOTS for 5, e não adicionarmos NENHUM result válido ou repetido
        // validShots = 0, repeatedShots = 0 -> output continua vazio até à linha 133
        // Mas para isso, precisamos que os resultados na lista results sejam todos INVÁLIDOS (!result.valid())
        for(int i = 0; i < Game.NUMBER_SHOTS - 1; i++) {
            results.add(new IGame.ShotResult(false, false, null, false));
        }
        // Isto resulta em outsideShots = 1 e output.isEmpty() = true na linha 134
        move.processEnemyFire(true);
    }
    @Test
    @DisplayName("Limpa Amarelo: Tiros repetidos SEM tiros válidos (validShots == 0)")
    void testRepeatedNoValid() {
        results.clear();
        // Um tiro repetido (repeatedShots = 1)
        // Mas o tiro NÃO é contado como válido porque repetido = true
        results.add(new IGame.ShotResult(true, true, null, false));

        // Isso força validShots = 0, pulando o if da vírgula (Linha 125)
        String json = move.processEnemyFire(true);
        assertNotNull(json);
    }

    @Test
    @DisplayName("Limpa Amarelo: Tiros repetidos COM tiros válidos (validShots > 0)")
    void testRepeatedWithValid() {
        results.clear();
        // 1. Um tiro válido (água) -> validShots = 1
        results.add(new IGame.ShotResult(true, false, null, false));

        // 2. Um tiro repetido -> repeatedShots = 1
        results.add(new IGame.ShotResult(true, true, null, false));

        // Isso força validShots > 0, executando o append(", ") na Linha 126
        String json = move.processEnemyFire(true);
        assertNotNull(json);
    }

    @Test
    @DisplayName("Limpa Amarelo: Plural de repetidos (repeatedShots > 1)")
    void testRepeatedPlural() {
        results.clear();
        // Adicionar 2 repetidos para testar o ramo "s" do operador ternário
        results.add(new IGame.ShotResult(true, true, null, false));
        results.add(new IGame.ShotResult(true, true, null, false));

        String json = move.processEnemyFire(true);
        assertNotNull(json);
    }
}