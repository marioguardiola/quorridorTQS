package quorridor.view;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import quorridor.model.Board;
import quorridor.model.Player;

/**
 * Vista real del juego (MVC): dibuja el tablero en la consola y lee las órdenes del usuario.
 * No tiene lógica del juego: solo muestra lo que le pide el controlador.
 * Al ser la Vista, no se prueba con tests unitarios (en los tests se sustituye por mocks).
 */
public class ConsoleView implements GameView {

  private final Scanner scanner;
  private final PrintStream out;

  public ConsoleView(InputStream in, PrintStream out) {
    this.scanner = new Scanner(in);
    this.out = out;
  }

  // Dibuja el tablero: "1" y "2" son los jugadores, "|" y "---" son los muros
  @Override
  public void showBoard(Board board, Player player1, Player player2) {
    StringBuilder sb = new StringBuilder();

    // Cabecera con los números de columna
    sb.append("\n   ");
    for (int x = 0; x < Board.SIZE; x++) {
      sb.append(" ").append(x).append("  ");
    }
    sb.append("\n");

    for (int y = 0; y < Board.SIZE; y++) {
      // Fila de casillas, con "|" si hay un muro a la derecha de la casilla
      sb.append(y).append("  ");
      for (int x = 0; x < Board.SIZE; x++) {
        sb.append(" ").append(cellSymbol(y, x, player1, player2)).append(" ");
        if (x < Board.SIZE - 1) {
          sb.append(board.isBlocked(y, x, y, x + 1) ? "|" : " ");
        }
      }
      sb.append("\n");

      // Ranura de debajo, con "---" si hay un muro debajo de la casilla
      if (y < Board.SIZE - 1) {
        sb.append("   ");
        for (int x = 0; x < Board.SIZE; x++) {
          sb.append(board.isBlocked(y, x, y + 1, x) ? "---" : "   ").append(" ");
        }
        sb.append("\n");
      }
    }
    out.print(sb);
  }

  // Símbolo de una casilla: número del jugador que la ocupa, o "." si está vacía
  private String cellSymbol(int y, int x, Player player1, Player player2) {
    if (player1.getPositionY() == y && player1.getPositionX() == x) {
      return "1";
    }
    if (player2.getPositionY() == y && player2.getPositionX() == x) {
      return "2";
    }
    return ".";
  }

  @Override
  public void showTurn(Player player) {
    out.println("\nTurno de " + player.getName() + " (muros restantes: "
        + player.getWalls() + ", meta: fila " + player.getGoalY() + ")");
    out.println("Órdenes:  m fila columna   -> mover el peón        (ej: m 7 4)");
    out.println("          w fila columna h|v -> colocar un muro     (ej: w 6 3 h)");
    out.println("          q                 -> salir");
  }

  @Override
  public void showMessage(String message) {
    out.println(">> " + message);
  }

  @Override
  public void showWinner(Player player) {
    out.println("\n*** ¡Ha ganado " + player.getName() + "! ***");
  }

  // Lee la siguiente orden del usuario. Devuelve null si no hay más entrada
  public String readCommand() {
    out.print("> ");
    return scanner.hasNextLine() ? scanner.nextLine() : null;
  }
}