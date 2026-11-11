package quorridor.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {
  @Test
  void PositionTest(){
    // Normal position
    Position pos = new Position(1,1);
    assertEquals(1, pos.getY());
    assertEquals(1, pos.getX());

    // Assert boundary values -> Position out of the board
    assertThrows(IllegalArgumentException.class, () -> new Position(-1, -1));
    assertThrows(IllegalArgumentException.class, () -> new Position(0, -1));
    assertThrows(IllegalArgumentException.class, () -> new Position(-1, 0));
    assertThrows(IllegalArgumentException.class, () -> new Position(9, 9));
    assertThrows(IllegalArgumentException.class, () -> new Position(0, 9));
    assertThrows(IllegalArgumentException.class, () -> new Position(9, 0));

  }

  @Test
  void testSetY(){
    // Normal position Y
    Position pos = new Position(2,7);
    assertEquals(2, pos.getY());

    pos.setY(3);
    assertEquals(3, pos.getY());

    // Assert boundary values -> -1, 9
    assertThrows(IllegalArgumentException.class, () -> pos.setY(-1));
    assertThrows(IllegalArgumentException.class, () -> pos.setY(9));

  }

  @Test
  void testSetX() {
    // Normal position Y
    Position pos = new Position(2, 7);
    assertEquals(7, pos.getX());

    pos.setX(6);
    assertEquals(6, pos.getX());

    // Assert boundary values -> -1, 9
    assertThrows(IllegalArgumentException.class, () -> pos.setX(-1));
    assertThrows(IllegalArgumentException.class, () -> pos.setX(9));
  }
}
