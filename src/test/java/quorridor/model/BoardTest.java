package quorridor.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

  @Test
  void testNewBoardIsEmpty() {
    // Valor límite (lista buida): un tablero nuevo no tiene ningún muro
    Board board = new Board();
    assertEquals(0, board.getWallCount());
  }

  @Test
  void testAddWall() {
    Board board = new Board();

    // Valor límite (un únic element): añadir un muro -> el contador pasa de 0 a 1
    board.addWall(new Wall(2, 3, Orientation.HORIZONTAL));
    assertEquals(1, board.getWallCount());

    // Partición válida (varios elementos): añadir otro muro -> de 1 a 2
    board.addWall(new Wall(5, 5, Orientation.VERTICAL));
    assertEquals(2, board.getWallCount());
  }

  @Test
  void testAddWallNull() {
    // Partición inválida: no se puede añadir un muro nulo (precondición)
    Board board = new Board();
    assertThrows(IllegalArgumentException.class, () -> board.addWall(null));
  }
}