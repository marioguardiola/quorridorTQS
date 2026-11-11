package quorridor.controller;

import org.junit.jupiter.api.Test;
import quorridor.model.Board;
import quorridor.model.Player;

import static org.junit.jupiter.api.Assertions.*;

class QuorridorControllerTest {

  @Test
  void testControllerInitialization() {
    QuorridorController controller = new QuorridorController();

    assertNotNull(controller.getBoard());
    assertEquals(2, controller.getPlayers().size());

    // El primer jugador suele empezar abajo (fila 8, columna 4) con meta en la fila 0
    Player currentPlayer = controller.getCurrentPlayer();
    assertNotNull(currentPlayer);
    assertEquals(0, currentPlayer.getGoalY());
  }

  @Test
  void testSwitchTurnAfterMove() {
    QuorridorController controller = new QuorridorController();

    // Starts player 1
    assertEquals("Player 1", controller.getCurrentPlayer().getName());

    // Try a move
    boolean moved = controller.moveCurrentPlayer(new quorridor.model.Position(7, 4));

    assertTrue(moved);
    // Change player turn
    assertEquals("Player 2", controller.getCurrentPlayer().getName());
  }
}