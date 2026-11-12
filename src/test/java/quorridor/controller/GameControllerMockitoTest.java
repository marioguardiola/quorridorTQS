package quorridor.controller;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quorridor.model.Orientation;
import quorridor.model.Player;
import quorridor.view.GameView;

/**
 * Tests de GameController con un mock de la Vista hecho con Mockito.
 * A diferencia de MockGameView (hecho a mano), aquí no hace falta escribir ninguna clase:
 * Mockito crea el objeto falso, y con verify(...) comprobamos qué métodos de la Vista
 * ha llamado el controlador (interacción con la Vista).
 */
public class GameControllerMockitoTest {
  private Player player1;
  private Player player2;
  private MockBoard mockBoard;
  private MockMoveValidation mockValidation;
  private GameView viewMock;
  private GameController controller;

  @BeforeEach
  void setUp() {
    // Fixture: los mismos jugadores que en GameControllerTest
    player1 = new Player("Player 1", 1, 10, 8, 4, 0);
    player2 = new Player("Player 2", 2, 10, 0, 4, 8);
    mockBoard = new MockBoard();
    mockValidation = new MockMoveValidation();

    // Mock object con Mockito: una Vista falsa creada en una sola línea
    viewMock = mock(GameView.class);
    controller = new GameController(mockBoard, player1, player2, mockValidation, viewMock);
  }

  @Test
  void testMovePawnValidShowsTurn() {
    // Partición válida: movimiento aceptado
    mockValidation.setValid(true);
    controller.movePawn(7, 4);

    // Mockito (verify): la Vista recibe el cambio de turno al jugador 2...
    verify(viewMock).showTurn(player2);
    // ...y nunca un mensaje de error ni un ganador
    verify(viewMock, never()).showMessage(anyString());
    verify(viewMock, never()).showWinner(any());
  }

  @Test
  void testMovePawnInvalidShowsMessage() {
    // Partición inválida: movimiento rechazado
    mockValidation.setValid(false);
    controller.movePawn(7, 4);

    // Mockito (verify): la Vista muestra exactamente este mensaje, y el turno no cambia
    verify(viewMock).showMessage("Movimiento no válido");
    verify(viewMock, never()).showTurn(any());
  }

  @Test
  void testMovePawnWinnerShowsWinner() {
    // Valor frontera: el jugador 1 llega a su fila meta (0)
    player1.setPosition(1, 3);
    mockValidation.setValid(true);
    controller.movePawn(0, 3);

    // Mockito (verify): la Vista anuncia al ganador, y no se pasa el turno
    verify(viewMock).showWinner(player1);
    verify(viewMock, never()).showTurn(any());
  }

  @Test
  void testPlaceWallRejectedShowsMessage() {
    // Partición inválida: el tablero rechaza el muro
    mockBoard.setAccept(false);
    controller.placeWall(3, 3, Orientation.HORIZONTAL);

    // Mockito (verify): la Vista muestra un mensaje que empieza por este texto
    verify(viewMock).showMessage(startsWith("No se puede colocar el muro"));
    verify(viewMock, never()).showTurn(any());
  }
}