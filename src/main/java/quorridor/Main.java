package quorridor;

import quorridor.controller.GameController;
import quorridor.model.Board;
import quorridor.model.MoveValidation;
import quorridor.model.Orientation;
import quorridor.model.Player;
import quorridor.view.ConsoleView;

/**
 * Punto de entrada del juego: crea el Modelo, la Vista y el Controlador (MVC),
 * y ejecuta el bucle de la partida leyendo las órdenes del usuario por consola.
 */
public final class Main {

  private Main() {
  }

  public static void main(String[] args) {
    // Modelo: tablero y jugadores (el 1 empieza abajo y va hacia la fila 0; el 2, al revés)
    Board board = new Board();
    Player player1 = new Player("Jugador 1", 1, 10, Board.SIZE - 1, Board.SIZE / 2, 0);
    Player player2 = new Player("Jugador 2", 2, 10, 0, Board.SIZE / 2, Board.SIZE - 1);

    // Vista y Controlador
    ConsoleView view = new ConsoleView(System.in, System.out);
    GameController controller = new GameController(board, player1, player2,
        new MoveValidation(board), view);

    controller.start();

    // Bucle de la partida: leer una orden y enviarla al controlador
    // (el controlador le dice a la Vista que redibuje el tablero tras cada jugada válida)
    while (!controller.isGameOver()) {
      String line = view.readCommand();
      if (line == null || line.trim().equalsIgnoreCase("q")) {
        view.showMessage("Partida abandonada");
        return;
      }
      executeCommand(line.trim(), controller, view);
    }
  }

  // Interpreta una orden ("m fila columna" o "w fila columna h|v") y la envía al controlador.
  // Si la orden está mal escrita, muestra un mensaje de ayuda
  private static void executeCommand(String line, GameController controller, ConsoleView view) {
    String[] parts = line.split("\\s+");
    try {
      if (parts.length == 3 && parts[0].equalsIgnoreCase("m")) {
        controller.movePawn(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
      } else if (parts.length == 4 && parts[0].equalsIgnoreCase("w")) {
        controller.placeWall(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]),
            parseOrientation(parts[3]));
      } else {
        view.showMessage("Orden no válida. Usa: m fila columna  |  w fila columna h|v  |  q");
      }
    } catch (NumberFormatException e) {
      view.showMessage("La fila y la columna deben ser números");
    }
  }

  // "h" -> HORIZONTAL, "v" -> VERTICAL, cualquier otra cosa -> null (el controlador la rechaza)
  private static Orientation parseOrientation(String text) {
    if (text.equalsIgnoreCase("h")) {
      return Orientation.HORIZONTAL;
    }
    if (text.equalsIgnoreCase("v")) {
      return Orientation.VERTICAL;
    }
    return null;
  }
}