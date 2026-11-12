package quorridor.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quorridor.model.Board;
import quorridor.model.Player;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de GameController (clase de alto nivel).
 * Mock objects hechos a mano: MockGameView (Vista) y MockMoveValidation
 * (reglas de movimiento), inyectados por el constructor (Inversion of Control).
 */
public class GameControllerTest {
  private Player player1;
  private Player player2;
  private MockMoveValidation mockValidation;
  private MockGameView mockView;
  private GameController controller;

  @BeforeEach
  void setUp() {
    // Fixture: jugador 1 abajo (8,4) con meta en la fila 0,
    // jugador 2 arriba (0,4) con meta en la fila 8
    player1 = new Player("Player 1", 1, 10, 8, 4, 0);
    player2 = new Player("Player 2", 2, 10, 0, 4, 8);
    mockValidation = new MockMoveValidation();
    mockView = new MockGameView();
    controller = new GameController(new Board(), player1, player2, mockValidation, mockView);
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
}