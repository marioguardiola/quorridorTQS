package quorridor.view;

import quorridor.model.Board;
import quorridor.model.Player;

/**
 * Vista del juego (MVC).
 * Es una interfaz para poder sustituirla por un mock (MockGameView) en los tests
 * del controlador. La implementación real es ConsoleView.
 */
public interface GameView {

  // Dibuja el tablero con los muros y la posición de los dos jugadores
  void showBoard(Board board, Player player1, Player player2);

  // Indica de quién es el turno
  void showTurn(Player player);

  // Muestra un mensaje al usuario (errores, avisos...)
  void showMessage(String message);

  // Anuncia el ganador de la partida
  void showWinner(Player player);
}