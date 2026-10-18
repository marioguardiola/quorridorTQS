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

  // ---------- isBlocked v1: movimientos verticales ----------

  @Test
  void testIsBlockedEmptyBoard() {
    // Valor límite (lista buida): sin muros nunca hay bloqueo
    Board board = new Board();
    assertFalse(board.isBlocked(2, 3, 3, 3));
  }

  @Test
  void testIsBlockedMoveDown() {
    // Bajar de (2,3) a (3,3)

    // Partición: muro H en (2,3) cubre X=3 y X=4 -> bloquea
    Board b1 = new Board();
    b1.addWall(new Wall(2, 3, Orientation.HORIZONTAL));
    assertTrue(b1.isBlocked(2, 3, 3, 3));

    // Partición: muro H en (2,2) cubre X=2 y X=3 -> bloquea
    Board b2 = new Board();
    b2.addWall(new Wall(2, 2, Orientation.HORIZONTAL));
    assertTrue(b2.isBlocked(2, 3, 3, 3));

    // Valor frontera: muro H en (2,4) cubre X=4 y X=5 (justo a la derecha) -> no bloquea
    Board b3 = new Board();
    b3.addWall(new Wall(2, 4, Orientation.HORIZONTAL));
    assertFalse(b3.isBlocked(2, 3, 3, 3));

    // Valor frontera: muro H en (2,1) cubre X=1 y X=2 (justo a la izquierda) -> no bloquea
    Board b4 = new Board();
    b4.addWall(new Wall(2, 1, Orientation.HORIZONTAL));
    assertFalse(b4.isBlocked(2, 3, 3, 3));

    // Partición: muro H en otra fila (1,3) -> no bloquea
    Board b5 = new Board();
    b5.addWall(new Wall(1, 3, Orientation.HORIZONTAL));
    assertFalse(b5.isBlocked(2, 3, 3, 3));

    // Partición: muro VERTICAL en (2,3) no bloquea un movimiento vertical
    Board b6 = new Board();
    b6.addWall(new Wall(2, 3, Orientation.VERTICAL));
    assertFalse(b6.isBlocked(2, 3, 3, 3));
  }

  @Test
  void testIsBlockedMoveUp() {
    // Subir de (3,3) a (2,3): el mismo muro H en (2,3) bloquea (simetría)
    Board b1 = new Board();
    b1.addWall(new Wall(2, 3, Orientation.HORIZONTAL));
    assertTrue(b1.isBlocked(3, 3, 2, 3));

    // Partición: muro H en (3,3) está por debajo -> no bloquea subir
    Board b2 = new Board();
    b2.addWall(new Wall(3, 3, Orientation.HORIZONTAL));
    assertFalse(b2.isBlocked(3, 3, 2, 3));
  }
}