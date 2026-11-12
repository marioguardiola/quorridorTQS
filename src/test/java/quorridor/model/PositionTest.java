package quorridor.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de Position (clase de bajo nivel).
 * Caja negra: particiones equivalentes y valores límite del rango [0, 8] en Y y en X:
 * exteriores (-1, 9), frontera (0, 8), interiores a frontera (1, 7) e interiores.
 */
public class PositionTest {

  // ---------- Constructor ----------

  @Test
  void testConstructorNormal() {
    // Caja negra - partición válida: casilla interior del tablero
    Position pos = new Position(4, 5);
    assertEquals(4, pos.getY());
    assertEquals(5, pos.getX());
  }

  @Test
  void testConstructorValidBoundaries() {
    // Caja negra - valor frontera inferior: (0, 0)
    Position min = new Position(0, 0);
    assertEquals(0, min.getY());
    assertEquals(0, min.getX());

    // Caja negra - valor frontera superior: (8, 8)
    Position max = new Position(8, 8);
    assertEquals(8, max.getY());
    assertEquals(8, max.getX());
  }

  @Test
  void testConstructorInnerBoundaries() {
    // Caja negra - valores interiores a frontera: 1 y 7, en Y y en X
    Position p1 = new Position(1, 7);
    assertEquals(1, p1.getY());
    assertEquals(7, p1.getX());

    Position p2 = new Position(7, 1);
    assertEquals(7, p2.getY());
    assertEquals(1, p2.getX());
  }

  @Test
  void testConstructorInvalidBoundaries() {
    // Caja negra - valores límite exteriores en las dos coordenadas a la vez: -1 y 9
    assertThrows(IllegalArgumentException.class, () -> new Position(-1, -1));
    assertThrows(IllegalArgumentException.class, () -> new Position(9, 9));

    // Caja negra - valores límite exteriores solo en X (Y válida): -1 y 9
    assertThrows(IllegalArgumentException.class, () -> new Position(0, -1));
    assertThrows(IllegalArgumentException.class, () -> new Position(0, 9));

    // Caja negra - valores límite exteriores solo en Y (X válida): -1 y 9
    assertThrows(IllegalArgumentException.class, () -> new Position(-1, 0));
    assertThrows(IllegalArgumentException.class, () -> new Position(9, 0));
  }

  // ---------- setY / setX ----------

  @Test
  void testSetY() {
    Position pos = new Position(2, 7);
    assertEquals(2, pos.getY());

    // Caja negra - partición válida: valor interior; X no se modifica (postcondición)
    pos.setY(3);
    assertEquals(3, pos.getY());
    assertEquals(7, pos.getX());

    // Caja negra - valores frontera válidos: 0 y 8
    pos.setY(0);
    assertEquals(0, pos.getY());
    pos.setY(8);
    assertEquals(8, pos.getY());

    // Caja negra - valores interiores a frontera: 1 y 7
    pos.setY(1);
    assertEquals(1, pos.getY());
    pos.setY(7);
    assertEquals(7, pos.getY());

    // Caja negra - valores límite exteriores: -1 y 9 (precondición)
    assertThrows(IllegalArgumentException.class, () -> pos.setY(-1));
    assertThrows(IllegalArgumentException.class, () -> pos.setY(9));
    assertEquals(7, pos.getY()); // el estado no cambia tras el fallo
  }

  @Test
  void testSetX() {
    Position pos = new Position(2, 7);
    assertEquals(7, pos.getX());

    // Caja negra - partición válida: valor interior; Y no se modifica (postcondición)
    pos.setX(6);
    assertEquals(6, pos.getX());
    assertEquals(2, pos.getY());

    // Caja negra - valores frontera válidos: 0 y 8
    pos.setX(0);
    assertEquals(0, pos.getX());
    pos.setX(8);
    assertEquals(8, pos.getX());

    // Caja negra - valores interiores a frontera: 1 y 7
    pos.setX(1);
    assertEquals(1, pos.getX());
    pos.setX(7);
    assertEquals(7, pos.getX());

    // Caja negra - valores límite exteriores: -1 y 9 (precondición)
    assertThrows(IllegalArgumentException.class, () -> pos.setX(-1));
    assertThrows(IllegalArgumentException.class, () -> pos.setX(9));
    assertEquals(7, pos.getX()); // el estado no cambia tras el fallo
  }
}