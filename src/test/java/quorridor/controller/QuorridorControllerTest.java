package quorridor.controller;

import org.junit.jupiter.api.Test;
import quorridor.model.Board;
import quorridor.model.Player;

import static org.junit.jupiter.api.Assertions.*;

class QuorridorControllerTest {

  @Test
  void testControllerInitialization() {
    // Al inicializar el controlador, debe crear el tablero y los dos jugadores
    QuorridorController controller = new QuorridorController();

    assertNotNull(controller.getBoard());
    assertEquals(2, controller.getPlayers().size());

    // El primer jugador suele empezar abajo (fila 8, columna 4) con meta en la fila 0
    Player currentPlayer = controller.getCurrentPlayer();
    assertNotNull(currentPlayer);
    assertEquals(0, currentPlayer.getGoalY());
  }
}