package quorridor.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quorridor.model.Player;
import quorridor.model.Orientation;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de GameController (clase de alto nivel).
 * Mock objects hechos a mano: MockGameView (Vista), MockMoveValidation (reglas de
 * movimiento) y MockBoard (tablero), inyectados por el constructor (Inversion of Control).
 *
 * Caja blanca - path coverage de placeWall (ver diagrama de actividad UML del informe):
 * V(G) = E - N + 2 = 20 - 15 + 2 = 7 caminos independientes (P1..P7).
 * Cada test de placeWall indica qué camino recorre.
 */
public class GameControllerTest {
  private Player player1;
  private Player player2;
  private MockMoveValidation mockValidation;
  private MockGameView mockView;
  private GameController controller;
  private MockBoard mockBoard;

  @BeforeEach
  void setUp() {
    // Fixture: jugador 1 abajo (8,4) con meta en la fila 0,
    // jugador 2 arriba (0,4) con meta en la fila 8
    player1 = new Player("Player 1", 1, 10, 8, 4, 0);
    player2 = new Player("Player 2", 2, 10, 0, 4, 8);

    // Mock objects hechos a mano, inyectados por el constructor (Inversion of Control)
    mockBoard = new MockBoard();
    mockValidation = new MockMoveValidation();
    mockView = new MockGameView();
    controller = new GameController(mockBoard, player1, player2, mockValidation, mockView);
  }

  @Test
  void testConstructorFirstTurn() {
    // Partición válida: al empezar la partida, el turno es del jugador 1
    assertEquals(player1, controller.getCurrentPlayer());
  }

  @Test
  void testMovePawnValid() {
    // Partición válida: el validador (mock) acepta el movimiento
    mockValidation.setValid(true);

    assertTrue(controller.movePawn(7, 4));

    // El peón se ha movido a la casilla destino
    assertEquals(7, player1.getPositionY());
    assertEquals(4, player1.getPositionX());
    // Mock (expectation): el controlador ha consultado al validador una vez
    assertEquals(1, mockValidation.callCount);
    // Cambia el turno al jugador 2, y se le indica a la Vista
    assertEquals(player2, controller.getCurrentPlayer());
    assertEquals(player2, mockView.lastTurn);
  }

  @Test
  void testMovePawnInvalid() {
    // Partición inválida: el validador (mock) rechaza el movimiento
    mockValidation.setValid(false);

    assertFalse(controller.movePawn(7, 4));

    // El peón no se ha movido
    assertEquals(8, player1.getPositionY());
    // El turno no cambia
    assertEquals(player1, controller.getCurrentPlayer());
    // Interacción con la Vista: se muestra un mensaje de error
    assertEquals(1, mockView.messages.size());
  }

  // ---------- movePawn v2: ganador y casillas fuera del tablero ----------

  @Test
  void testMovePawnWinner() {
    // Valor frontera: el jugador 1 pasa de la fila 1 a su fila meta (0) -> gana
    // (columna 3 para no coincidir con el jugador 2, que está en (0,4))
    player1.setPosition(1, 3);
    mockValidation.setValid(true);

    assertTrue(controller.movePawn(0, 3));

    // Interacción con la Vista: se anuncia al ganador
    assertEquals(player1, mockView.winner);
    assertTrue(controller.isGameOver());
  }

  @Test
  void testMovePawnAfterGameOver() {
    // Partición inválida: con la partida terminada no se puede mover
    player1.setPosition(1, 3);
    mockValidation.setValid(true);
    controller.movePawn(0, 3); // gana el jugador 1

    assertFalse(controller.movePawn(1, 3));
    // Mock (expectation): el validador solo se consultó en el primer movimiento
    assertEquals(1, mockValidation.callCount);
  }

  @Test
  void testMovePawnOutOfBoard() {
    // Valores límite exteriores: casilla destino fuera del tablero (9 y -1)
    assertFalse(controller.movePawn(9, 4));
    assertFalse(controller.movePawn(-1, 4));

    // El peón no se mueve y la Vista muestra un error por cada intento
    assertEquals(8, player1.getPositionY());
    assertEquals(2, mockView.messages.size());
    // Mock (expectation): no se llega a consultar al validador
    assertEquals(0, mockValidation.callCount);
  }

  // ---------- placeWall v1: el tablero acepta o rechaza el muro ----------

  @Test
  void testPlaceWallValid() {
    // Caja blanca - path coverage: camino P6 (1-3-5-7-9-11-12-14-15),
    // muro colocado por el jugador 1 (rama true del ternario del turno)
    // Partición válida: el tablero (mock) acepta el muro
    mockBoard.setAccept(true);

    assertTrue(controller.placeWall(3, 3, Orientation.HORIZONTAL));

    // Mock (expectation): el controlador ha pedido al tablero colocar el muro
    assertEquals(1, mockBoard.addWallCount);
    // El jugador 1 ha gastado un muro (de 10 a 9)
    assertEquals(9, player1.getWalls());
    // Cambia el turno al jugador 2, y se le indica a la Vista
    assertEquals(player2, controller.getCurrentPlayer());
    assertEquals(player2, mockView.lastTurn);
  }

  @Test
  void testPlaceWallRejectedByBoard() {
    // Caja blanca - path coverage: camino P5 (1-3-5-7-9-10-15), el tablero lanza excepción
    // Partición inválida: el tablero (mock) rechaza el muro
    // (se solapa con otro o encierra a un jugador)
    mockBoard.setAccept(false);

    assertFalse(controller.placeWall(3, 3, Orientation.HORIZONTAL));

    // El jugador no gasta ningún muro
    assertEquals(10, player1.getWalls());
    // El turno no cambia
    assertEquals(player1, controller.getCurrentPlayer());
    // Interacción con la Vista: se muestra un mensaje de error
    assertEquals(1, mockView.messages.size());
  }

  // ---------- placeWall v2: sin muros, ancla inválida, partida terminada ----------

  @Test
  void testPlaceWallNoWallsLeft() {
    // Caja blanca - path coverage: camino P2 (1-3-4-15), al jugador no le quedan muros
    // Valor frontera inferior: el jugador tiene 0 muros -> no puede colocar ninguno
    Player noWalls = new Player("Player 1", 1, 0, 8, 4, 0);
    GameController c = new GameController(mockBoard, noWalls, player2, mockValidation, mockView);

    assertFalse(c.placeWall(3, 3, Orientation.HORIZONTAL));

    // Mock (expectation): ni siquiera se intenta colocar el muro en el tablero
    assertEquals(0, mockBoard.addWallCount);
    // Interacción con la Vista: se muestra un mensaje de error
    assertEquals(1, mockView.messages.size());
    // El turno no cambia
    assertEquals(noWalls, c.getCurrentPlayer());
  }

  @Test
  void testPlaceWallInvalidAnchor() {
    // Caja blanca - path coverage: camino P3 (1-3-5-6-15), ancla fuera de rango
    // Valores límite exteriores del ancla del muro: 8 y -1 (rango válido [0, 7])
    assertFalse(controller.placeWall(8, 3, Orientation.HORIZONTAL));
    assertFalse(controller.placeWall(3, -1, Orientation.VERTICAL));

    // No se intenta colocar en el tablero, y la Vista muestra un error por cada intento
    assertEquals(0, mockBoard.addWallCount);
    assertEquals(2, mockView.messages.size());
  }

  @Test
  void testPlaceWallAfterGameOver() {
    // Caja blanca - path coverage: camino P1 (1-2-15), la partida ya ha terminado
    // Partición inválida: con la partida terminada no se pueden colocar muros
    player1.setPosition(0, 3); // el jugador 1 ya está en su fila meta

    assertFalse(controller.placeWall(3, 3, Orientation.HORIZONTAL));

    // No se toca el tablero y el jugador no gasta ningún muro
    assertEquals(0, mockBoard.addWallCount);
    assertEquals(10, player1.getWalls());
  }

  // ---------- Caja blanca: tests añadidos tras analizar la cobertura ----------

  @Test
  void testMovePawnOutOfBoardX() {
    // Caja blanca - condition coverage: X fuera del tablero (9 y -1), es decir,
    // las condiciones del || que dependen de la columna toman el valor true
    assertFalse(controller.movePawn(4, 9));
    assertFalse(controller.movePawn(4, -1));
    assertEquals(2, mockView.messages.size());
    assertEquals(0, mockValidation.callCount);
  }

  @Test
  void testMovePawnSecondPlayerTurn() {
    // Caja blanca - decision coverage: rama false del ternario del rival
    // (currentPlayer == player1 es false porque mueve el jugador 2)
    mockValidation.setValid(true);
    controller.movePawn(7, 4); // jugador 1, pasa el turno

    assertTrue(controller.movePawn(1, 4)); // jugador 2
    assertEquals(1, player2.getPositionY());
    // El turno vuelve al jugador 1
    assertEquals(player1, controller.getCurrentPlayer());
  }

  @Test
  void testPlayer2Wins() {
    // Caja negra - valor frontera: el jugador 2 pasa de la fila 7 a su fila meta (8) -> gana
    // Caja blanca: rama "ganador" de movePawn cuando el jugador actual es el 2
    player2.setPosition(7, 3); // a un paso de su meta (fila 8)
    mockValidation.setValid(true);
    controller.movePawn(7, 4); // jugador 1, pasa el turno

    assertTrue(controller.movePawn(8, 3));
    // Interacción con la Vista: se anuncia al jugador 2 como ganador
    assertSame(player2, mockView.winner);
    assertSame(player2, controller.getWinner());
    assertTrue(controller.isGameOver());
  }

  @Test
  void testPlaceWallSecondPlayerTurn() {
    // Caja blanca - path coverage: camino P7 (1-3-5-7-9-11-13-14-15),
    // muro colocado por el jugador 2 (rama false del ternario del turno)
    mockBoard.setAccept(true);
    controller.placeWall(3, 3, Orientation.HORIZONTAL); // jugador 1

    assertTrue(controller.placeWall(5, 5, Orientation.VERTICAL)); // jugador 2
    // El jugador 2 ha gastado un muro, y el turno vuelve al jugador 1
    assertEquals(9, player2.getWalls());
    assertEquals(player1, controller.getCurrentPlayer());
  }

  @Test
  void testPlaceWallNullOrientation() {
    // Caja blanca - path coverage: camino P4 (1-3-5-7-8-15), orientación nula
    // Partición inválida: muro sin orientación
    assertFalse(controller.placeWall(3, 3, null));

    // No se toca el tablero, la Vista muestra un error y nada más cambia
    assertEquals(0, mockBoard.addWallCount);
    assertEquals(1, mockView.messages.size());
    assertEquals(10, player1.getWalls());
    assertEquals(player1, controller.getCurrentPlayer());
  }

  // ---------- getWinner y start ----------

  @Test
  void testGetWinner() {
    // Partición: ningún jugador en su meta -> no hay ganador (null)
    assertNull(controller.getWinner());

    // Partición: el jugador 1 en su fila meta (0) -> el ganador es el jugador 1
    player1.setPosition(0, 3);
    assertSame(player1, controller.getWinner());
  }

  @Test
  void testStart() {
    // Interacción con la Vista: al empezar se muestra el tablero una vez
    // y el turno del jugador 1
    controller.start();

    assertEquals(1, mockView.boardShownCount);
    assertEquals(player1, mockView.lastTurn);
  }

  // ---------- Precondiciones del constructor (Diseño por Contrato) ----------

  @Test
  void testConstructorInvalidArguments() {
    // Partición inválida: cada una de las dependencias nula (precondición)
    assertThrows(IllegalArgumentException.class, () ->
        new GameController(null, player1, player2, mockValidation, mockView));
    assertThrows(IllegalArgumentException.class, () ->
        new GameController(mockBoard, null, player2, mockValidation, mockView));
    assertThrows(IllegalArgumentException.class, () ->
        new GameController(mockBoard, player1, null, mockValidation, mockView));
    assertThrows(IllegalArgumentException.class, () ->
        new GameController(mockBoard, player1, player2, null, mockView));
    assertThrows(IllegalArgumentException.class, () ->
        new GameController(mockBoard, player1, player2, mockValidation, null));
    // Partición inválida: el mismo jugador dos veces (precondición)
    assertThrows(IllegalArgumentException.class, () ->
        new GameController(mockBoard, player1, player1, mockValidation, mockView));
  }
}