package quorridor.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

  @Test
  void testConstructorNormal() {
    Position pos = new Position(1, 1);
    assertEquals(1, pos.getY());
    assertEquals(1, pos.getX());
  }

  @Test
  void testConstructorValidBoundaries() {
    // Valid limits: 0 and 8
    Position min = new Position(0, 0);
    assertEquals(0, min.getY());
    assertEquals(0, min.getX());

    Position max = new Position(8, 8);
    assertEquals(8, max.getY());
    assertEquals(8, max.getX());
  }

  @Test
  void testConstructorInvalidBoundaries() {
    // Out of the board -> -1, 9
    assertThrows(IllegalArgumentException.class, () -> new Position(-1, -1));
    assertThrows(IllegalArgumentException.class, () -> new Position(0, -1));
    assertThrows(IllegalArgumentException.class, () -> new Position(-1, 0));
    assertThrows(IllegalArgumentException.class, () -> new Position(9, 9));
    assertThrows(IllegalArgumentException.class, () -> new Position(0, 9));
    assertThrows(IllegalArgumentException.class, () -> new Position(9, 0));
  }

  @Test
  void testSetY() {
    Position pos = new Position(2, 7);
    assertEquals(2, pos.getY());

    pos.setY(3);
    assertEquals(3, pos.getY());
    assertEquals(7, pos.getX()); // postcondition: X is not modified

    // Valid boundaries
    pos.setY(0);
    assertEquals(0, pos.getY());
    pos.setY(8);
    assertEquals(8, pos.getY());

    // Invalid boundaries -> -1, 9
    assertThrows(IllegalArgumentException.class, () -> pos.setY(-1));
    assertThrows(IllegalArgumentException.class, () -> pos.setY(9));
    assertEquals(8, pos.getY()); // state unchanged after failed call
  }

  @Test
  void testSetX() {
    Position pos = new Position(2, 7);
    assertEquals(7, pos.getX());

    pos.setX(6);
    assertEquals(6, pos.getX());
    assertEquals(2, pos.getY()); // postcondition: Y is not modified

    // Valid boundaries
    pos.setX(0);
    assertEquals(0, pos.getX());
    pos.setX(8);
    assertEquals(8, pos.getX());

    // Invalid boundaries -> -1, 9
    assertThrows(IllegalArgumentException.class, () -> pos.setX(-1));
    assertThrows(IllegalArgumentException.class, () -> pos.setX(9));
    assertEquals(8, pos.getX()); // state unchanged after failed call
  }
}