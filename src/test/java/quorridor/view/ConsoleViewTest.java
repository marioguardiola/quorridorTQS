package quorridor.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quorridor.model.Board;
import quorridor.model.Orientation;
import quorridor.model.Player;
import quorridor.model.Wall;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de ConsoleView (Vista).
 * La Vista recibe la entrada y la salida por el constructor (Inversion of Control):
 * en lugar de System.in / System.out le pasamos streams en memoria y comprobamos el texto.
 * Objetivo: cobertura de sentencias y de decisiones de la Vista.
 */
public class ConsoleViewTest {

  private ByteArrayOutputStream output;
  private Board board;
  private Player player1;
  private Player player2;

  @BeforeEach
  void setUp() {
    output = new ByteArrayOutputStream();
    board = new Board();
    player1 = new Player("Player 1", 1, 10, 8, 4, 0);
    player2 = new Player("Player 2", 2, 10, 0, 4, 8);
  }

  // Crea la vista con la entrada indicada y la salida en memoria
  private ConsoleView viewWithInput(String input) {
    return new ConsoleView(new ByteArrayInputStream(input.getBytes()), new PrintStream(output));
  }

  // Devuelve la línea del tablero que empieza por el número de fila indicado
  private String boardRow(int row) {
    for (String line : output.toString().split("\n")) {
      if (line.startsWith(row + "  ")) {
        return line;
      }
    }
    return null;
  }

  @Test
  void testShowBoardEmpty() {
    // Partición: tablero sin muros -> aparecen los dos peones y ningún muro
    // Caja blanca: cellSymbol devuelve "1", "2" y "." (sus 3 caminos)
    viewWithInput("").showBoard(board, player1, player2);

    assertEquals("0   .   .   .   .   2   .   .   .   . ", boardRow(0));
    assertEquals("8   .   .   .   .   1   .   .   .   . ", boardRow(8));
    assertFalse(output.toString().contains("|"));
    assertFalse(output.toString().contains("---"));
  }

  @Test
  void testShowBoardHorizontalWall() {
    // Partición: muro horizontal -> se dibuja "---" debajo de las 2 casillas que tapa
    board.addWall(new Wall(3, 2, Orientation.HORIZONTAL));
    viewWithInput("").showBoard(board, player1, player2);

    assertTrue(output.toString().contains("---"));
    assertFalse(output.toString().contains("|"));
  }

  @Test
  void testShowBoardVerticalWall() {
    // Partición: muro vertical -> se dibuja "|" a la derecha de las 2 casillas que tapa
    board.addWall(new Wall(3, 2, Orientation.VERTICAL));
    viewWithInput("").showBoard(board, player1, player2);

    assertEquals("3   .   .   . | .   .   .   .   .   . ", boardRow(3));
    assertEquals("4   .   .   . | .   .   .   .   .   . ", boardRow(4));
    assertFalse(output.toString().contains("---"));
  }

  @Test
  void testShowTurn() {
    // Se muestra el nombre, los muros restantes y la fila de meta del jugador
    viewWithInput("").showTurn(player1);

    String text = output.toString();
    assertTrue(text.contains("Turno de Player 1"));
    assertTrue(text.contains("muros restantes: 10"));
    assertTrue(text.contains("meta: fila 0"));
  }

  @Test
  void testShowMessage() {
    viewWithInput("").showMessage("Movimiento no válido");

    assertTrue(output.toString().contains(">> Movimiento no válido"));
  }

  @Test
  void testShowWinner() {
    viewWithInput("").showWinner(player2);

    assertTrue(output.toString().contains("Ha ganado Player 2"));
  }

  @Test
  void testReadCommand() {
    // Caja blanca - decisión hasNextLine() cierta: hay una orden -> la devuelve
    ConsoleView view = viewWithInput("m 7 4\n");

    assertEquals("m 7 4", view.readCommand());
    assertTrue(output.toString().contains("> "));
  }

  @Test
  void testReadCommandNoInput() {
    // Caja blanca - decisión hasNextLine() falsa: no hay más entrada -> null
    ConsoleView view = viewWithInput("");

    assertNull(view.readCommand());
  }

  @Test
  void testReadCommandSeveralLines() {
    // Varias órdenes seguidas: se leen en orden y al acabar devuelve null
    ConsoleView view = viewWithInput("m 7 4\nw 6 3 h\n");

    assertEquals("m 7 4", view.readCommand());
    assertEquals("w 6 3 h", view.readCommand());
    assertNull(view.readCommand());
  }
}