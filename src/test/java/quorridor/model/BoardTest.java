package quorridor.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de Board (clase de bajo nivel).
 * Caja negra: particiones equivalentes y valores límite (incluidos los de listas:
 * lista buida, un únic element, plena).
 * Caja blanca: tests añadidos tras analizar la cobertura de ramas con JaCoCo.
 */
public class BoardTest {

  // ---------- addWall ----------

  @Test
  void testNewBoardIsEmpty() {
    // Caja negra - valor límite (lista buida): un tablero nuevo no tiene ningún muro
    Board board = new Board();
    assertEquals(0, board.getWallCount());
  }

  @Test
  void testAddWall() {
    Board board = new Board();

    // Caja negra - valor límite (un únic element): añadir un muro -> de 0 a 1
    board.addWall(new Wall(2, 3, Orientation.HORIZONTAL));
    assertEquals(1, board.getWallCount());

    // Caja negra - partición válida (varios elementos): añadir otro muro -> de 1 a 2
    board.addWall(new Wall(5, 5, Orientation.VERTICAL));
    assertEquals(2, board.getWallCount());
  }

  @Test
  void testAddWallNull() {
    // Caja negra - partición inválida: no se puede añadir un muro nulo (precondición)
    Board board = new Board();
    assertThrows(IllegalArgumentException.class, () -> board.addWall(null));
  }

  // ---------- isBlocked v1: movimientos verticales ----------

  @Test
  void testIsBlockedEmptyBoard() {
    // Caja negra - valor límite (lista buida): sin muros nunca hay bloqueo
    Board board = new Board();
    assertFalse(board.isBlocked(2, 3, 3, 3));
  }

  @Test
  void testIsBlockedMoveDown() {
    // Bajar de (2,3) a (3,3)

    // Caja negra - partición: muro H en (2,3) cubre X=3 y X=4 -> bloquea
    Board b1 = new Board();
    b1.addWall(new Wall(2, 3, Orientation.HORIZONTAL));
    assertTrue(b1.isBlocked(2, 3, 3, 3));

    // Caja negra - partición: muro H en (2,2) cubre X=2 y X=3 -> bloquea
    Board b2 = new Board();
    b2.addWall(new Wall(2, 2, Orientation.HORIZONTAL));
    assertTrue(b2.isBlocked(2, 3, 3, 3));

    // Caja negra - valor frontera: muro H en (2,4) cubre X=4 y X=5 (justo a la derecha)
    // -> no bloquea
    Board b3 = new Board();
    b3.addWall(new Wall(2, 4, Orientation.HORIZONTAL));
    assertFalse(b3.isBlocked(2, 3, 3, 3));

    // Caja negra - valor frontera: muro H en (2,1) cubre X=1 y X=2 (justo a la izquierda)
    // -> no bloquea
    Board b4 = new Board();
    b4.addWall(new Wall(2, 1, Orientation.HORIZONTAL));
    assertFalse(b4.isBlocked(2, 3, 3, 3));

    // Caja negra - partición: muro H en otra fila (1,3) -> no bloquea
    Board b5 = new Board();
    b5.addWall(new Wall(1, 3, Orientation.HORIZONTAL));
    assertFalse(b5.isBlocked(2, 3, 3, 3));

    // Caja negra - partición: un muro VERTICAL no bloquea un movimiento vertical
    Board b6 = new Board();
    b6.addWall(new Wall(2, 3, Orientation.VERTICAL));
    assertFalse(b6.isBlocked(2, 3, 3, 3));
  }

  @Test
  void testIsBlockedMoveUp() {
    // Caja negra - partición: subir de (3,3) a (2,3), el mismo muro H en (2,3) bloquea
    // (simetría con bajar)
    Board b1 = new Board();
    b1.addWall(new Wall(2, 3, Orientation.HORIZONTAL));
    assertTrue(b1.isBlocked(3, 3, 2, 3));

    // Caja negra - partición: muro H en (3,3) está por debajo -> no bloquea subir
    Board b2 = new Board();
    b2.addWall(new Wall(3, 3, Orientation.HORIZONTAL));
    assertFalse(b2.isBlocked(3, 3, 2, 3));
  }

  // ---------- isBlocked v2: movimientos laterales ----------

  @Test
  void testIsBlockedMoveRight() {
    // Ir a la derecha de (3,2) a (3,3)

    // Caja negra - partición: muro V en (3,2) cubre filas 3 y 4 -> bloquea
    Board b1 = new Board();
    b1.addWall(new Wall(3, 2, Orientation.VERTICAL));
    assertTrue(b1.isBlocked(3, 2, 3, 3));

    // Caja negra - partición: muro V en (2,2) cubre filas 2 y 3 -> bloquea
    Board b2 = new Board();
    b2.addWall(new Wall(2, 2, Orientation.VERTICAL));
    assertTrue(b2.isBlocked(3, 2, 3, 3));

    // Caja negra - valor frontera: muro V en (4,2) cubre filas 4 y 5 (justo debajo)
    // -> no bloquea
    Board b3 = new Board();
    b3.addWall(new Wall(4, 2, Orientation.VERTICAL));
    assertFalse(b3.isBlocked(3, 2, 3, 3));

    // Caja negra - valor frontera: muro V en (1,2) cubre filas 1 y 2 (justo encima)
    // -> no bloquea
    Board b4 = new Board();
    b4.addWall(new Wall(1, 2, Orientation.VERTICAL));
    assertFalse(b4.isBlocked(3, 2, 3, 3));

    // Caja negra - partición: muro V en otra ranura (3,1) -> no bloquea
    Board b5 = new Board();
    b5.addWall(new Wall(3, 1, Orientation.VERTICAL));
    assertFalse(b5.isBlocked(3, 2, 3, 3));
  }

  @Test
  void testIsBlockedMoveLeft() {
    // Caja negra - partición: ir a la izquierda de (3,3) a (3,2), el mismo muro V en (3,2)
    // bloquea (simetría con ir a la derecha)
    Board board = new Board();
    board.addWall(new Wall(3, 2, Orientation.VERTICAL));
    assertTrue(board.isBlocked(3, 3, 3, 2));
  }

  @Test
  void testIsBlockedHorizontalWallLateralMove() {
    // Caja negra - partición: un muro HORIZONTAL no bloquea un movimiento lateral
    // (este test detectó el bug de isBlocked v1)
    Board board = new Board();
    board.addWall(new Wall(3, 2, Orientation.HORIZONTAL));
    assertFalse(board.isBlocked(3, 2, 3, 3));
  }

  @Test
  void testIsBlockedBoardEdge() {
    // Caja negra - valores frontera: movimientos válidos en los bordes del tablero (0 y 8)
    Board board = new Board();
    assertFalse(board.isBlocked(0, 0, 1, 0));
    assertFalse(board.isBlocked(8, 8, 8, 7));
  }

  @Test
  void testIsBlockedInvalidMoves() {
    Board board = new Board();

    // Caja negra - partición inválida: casillas no adyacentes (salto de 2)
    assertThrows(IllegalArgumentException.class, () -> board.isBlocked(2, 3, 4, 3));

    // Caja negra - partición inválida: movimiento en diagonal
    assertThrows(IllegalArgumentException.class, () -> board.isBlocked(2, 3, 3, 4));

    // Caja negra - partición inválida: misma casilla (no hay movimiento)
    assertThrows(IllegalArgumentException.class, () -> board.isBlocked(2, 3, 2, 3));

    // Caja negra - valores límite exteriores: salir del tablero (-1 y 9)
    assertThrows(IllegalArgumentException.class, () -> board.isBlocked(0, 0, -1, 0));
    assertThrows(IllegalArgumentException.class, () -> board.isBlocked(8, 4, 9, 4));
  }

  // ---------- isOverlapping + addWall v2: muros que se solapan ----------

  @Test
  void testIsOverlappingEmptyBoard() {
    // Caja negra - valor límite (lista buida): en un tablero vacío ningún muro se solapa
    Board board = new Board();
    assertFalse(board.isOverlapping(new Wall(2, 3, Orientation.HORIZONTAL)));
  }

  @Test
  void testIsOverlappingSameAnchor() {
    Board board = new Board();
    board.addWall(new Wall(2, 3, Orientation.HORIZONTAL));

    // Caja negra - partición: mismo sitio y misma orientación -> se solapan
    assertTrue(board.isOverlapping(new Wall(2, 3, Orientation.HORIZONTAL)));

    // Caja negra - partición: mismo sitio y orientación distinta -> se cruzan en "+"
    assertTrue(board.isOverlapping(new Wall(2, 3, Orientation.VERTICAL)));
  }

  @Test
  void testIsOverlappingHorizontalShift() {
    // Muro H en (2,3) cubre las columnas 3 y 4
    Board board = new Board();
    board.addWall(new Wall(2, 3, Orientation.HORIZONTAL));

    // Caja negra - valor límite: desplazado 1 columna a la derecha (cubre 4 y 5)
    // -> comparten la 4
    assertTrue(board.isOverlapping(new Wall(2, 4, Orientation.HORIZONTAL)));

    // Caja negra - valor límite: desplazado 1 columna a la izquierda (cubre 2 y 3)
    // -> comparten la 3
    assertTrue(board.isOverlapping(new Wall(2, 2, Orientation.HORIZONTAL)));

    // Caja negra - valor frontera: desplazado 2 columnas -> se tocan por la punta,
    // no se solapan
    assertFalse(board.isOverlapping(new Wall(2, 5, Orientation.HORIZONTAL)));
    assertFalse(board.isOverlapping(new Wall(2, 1, Orientation.HORIZONTAL)));

    // Caja negra - partición: misma columna pero otra ranura (fila 3) -> paralelos
    assertFalse(board.isOverlapping(new Wall(3, 3, Orientation.HORIZONTAL)));
  }

  @Test
  void testIsOverlappingVerticalShift() {
    // Muro V en (2,3) cubre las filas 2 y 3
    Board board = new Board();
    board.addWall(new Wall(2, 3, Orientation.VERTICAL));

    // Caja negra - valor límite: desplazado 1 fila abajo / arriba -> se solapan
    assertTrue(board.isOverlapping(new Wall(3, 3, Orientation.VERTICAL)));
    assertTrue(board.isOverlapping(new Wall(1, 3, Orientation.VERTICAL)));

    // Caja negra - valor frontera: desplazado 2 filas -> se tocan por la punta,
    // no se solapan
    assertFalse(board.isOverlapping(new Wall(4, 3, Orientation.VERTICAL)));
    assertFalse(board.isOverlapping(new Wall(0, 3, Orientation.VERTICAL)));

    // Caja negra - partición: misma fila pero otra ranura (columna 4) -> paralelos
    assertFalse(board.isOverlapping(new Wall(2, 4, Orientation.VERTICAL)));
  }

  @Test
  void testIsOverlappingTJunction() {
    // Caja negra - partición: un muro V que toca el extremo de un muro H (forma de "T")
    // está permitido
    Board board = new Board();
    board.addWall(new Wall(2, 3, Orientation.HORIZONTAL));
    assertFalse(board.isOverlapping(new Wall(2, 4, Orientation.VERTICAL)));
  }

  @Test
  void testAddWallOverlapping() {
    // Caja negra - partición inválida: no se puede añadir un muro que se solapa
    // (precondición)
    Board board = new Board();
    board.addWall(new Wall(2, 3, Orientation.HORIZONTAL));
    assertThrows(IllegalArgumentException.class,
        () -> board.addWall(new Wall(2, 4, Orientation.HORIZONTAL)));

    // El muro rechazado no se ha añadido (el estado no cambia tras el fallo)
    assertEquals(1, board.getWallCount());
  }

  // ---------- Caja blanca: tests añadidos tras analizar la cobertura con JaCoCo ----------

  @Test
  void testIsBlockedWhiteBoxConditions() {
    Board board = new Board();

    // Caja blanca - condition coverage (isInside): X fuera del tablero por la izquierda (-1)
    assertThrows(IllegalArgumentException.class, () -> board.isBlocked(4, 0, 4, -1));

    // Caja blanca - condition coverage (isInside): X fuera del tablero por la derecha (9)
    assertThrows(IllegalArgumentException.class, () -> board.isBlocked(4, 8, 4, 9));

    // Caja blanca - decision coverage: la casilla de ORIGEN fuera del tablero
    // (primera condición del ||)
    assertThrows(IllegalArgumentException.class, () -> board.isBlocked(-1, 4, 0, 4));
  }

  @Test
  void testIsOverlappingNewHorizontalVsExistingVertical() {
    // Caja blanca - condition coverage: muro nuevo H frente a muro existente V en otra
    // posición (forma de "T") -> no se solapan
    Board board = new Board();
    board.addWall(new Wall(2, 3, Orientation.VERTICAL));
    assertFalse(board.isOverlapping(new Wall(2, 4, Orientation.HORIZONTAL)));
  }

  // ---------- hasPath + addWall con jugadores: nadie puede quedar encerrado ----------

  @Test
  void testHasPathEmptyBoard() {
    Board board = new Board();
    // Caja negra - partición válida: tablero vacío, desde la posición inicial (8,4)
    // hay camino hacia la meta en la fila 0
    assertTrue(board.hasPath(8, 4, 0));
    // Caja negra - partición válida: y viceversa (desde la fila 0 a la meta en la fila 8)
    assertTrue(board.hasPath(0, 4, 8));
  }

  @Test
  void testHasPathWithWallsNavigable() {
    // Caja negra - partición válida: hay muros, pero dejan pasar (camino con rodeo)
    Board board = new Board();
    board.addWall(new Wall(4, 2, Orientation.VERTICAL));
    board.addWall(new Wall(4, 4, Orientation.VERTICAL));
    assertTrue(board.hasPath(8, 4, 0));
  }

  @Test
  void testHasPathInvalidGoalOrPosition() {
    Board board = new Board();
    // Caja negra - partición inválida: meta distinta de 0 u 8 (precondición)
    assertThrows(IllegalArgumentException.class, () -> board.hasPath(8, 4, 5));
    // Caja negra - valor límite exterior: posición inicial fuera del tablero (-1)
    // (precondición)
    assertThrows(IllegalArgumentException.class, () -> board.hasPath(-1, 4, 0));
  }

  @Test
  void testAddWallTrapsPlayerThrowsException() {
    Board board = new Board();
    // Jugador en (8,0) con meta en la fila 0
    Player player = new Player("Irene", 1, 10, 8, 0, 0);
    java.util.List<Player> players = java.util.List.of(player);

    // Caja negra - partición válida: un muro que deja camino se coloca sin problema
    board.addWall(new Wall(7, 0, Orientation.HORIZONTAL), players);

    // Caja negra - partición inválida: un muro que encierra al jugador se rechaza
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> board.addWall(new Wall(7, 1, Orientation.VERTICAL), players));
    assertTrue(ex.getMessage().contains("traps"));

    // El muro rechazado se ha quitado (rollback): solo queda el primero
    assertEquals(1, board.getWallCount());
  }

  @Test
  void testHasPathAlreadyAtGoal() {
    // Caja negra - valor frontera: el jugador ya está en su fila meta (0 u 8)
    Board board = new Board();
    assertTrue(board.hasPath(8, 4, 8));
    assertTrue(board.hasPath(0, 4, 0));
  }

  @Test
  void testHasPathDetour() {
    // Caja negra - partición válida: H(7,0) bloquea subir desde las columnas 0 y 1,
    // pero se puede rodear por la columna 2
    Board board = new Board();
    board.addWall(new Wall(7, 0, Orientation.HORIZONTAL));
    assertTrue(board.hasPath(8, 0, 0));
  }

  @Test
  void testHasPathNoPath() {
    // Caja negra - partición inválida (sin camino): el hueco {(8,0),(8,1)} queda sellado
    // con H(7,0) + V(7,1)
    Board board = new Board();
    board.addWall(new Wall(7, 0, Orientation.HORIZONTAL));
    board.addWall(new Wall(7, 1, Orientation.VERTICAL));
    assertFalse(board.hasPath(8, 0, 0));
    assertFalse(board.hasPath(8, 1, 0));

    // Caja negra - valor frontera: la casilla vecina fuera del hueco sí tiene camino
    assertTrue(board.hasPath(8, 2, 0));
  }

  // ---------- addWall: límite de muros en el tablero ----------

  @Test
  void testAddWallMaxWalls() {
    Board board = new Board();
    for (int y = 0; y <= 4; y++) {
      for (int x = 0; x <= 6; x += 2) {
        board.addWall(new Wall(y, x, Orientation.HORIZONTAL));
      }
    }
    // Caja negra - valor frontera (lista plena): se pueden colocar exactamente MAX_WALLS
    assertEquals(Board.MAX_WALLS, board.getWallCount());

    // Caja negra - valor límite exterior: el muro MAX_WALLS + 1 se rechaza (precondición)
    assertThrows(IllegalStateException.class,
        () -> board.addWall(new Wall(5, 0, Orientation.HORIZONTAL)));
    assertEquals(Board.MAX_WALLS, board.getWallCount());
  }
}