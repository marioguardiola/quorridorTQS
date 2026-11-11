package quorridor.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class WallTest {

  @Test
  void testConstructorValid() {
    Wall w1 = new Wall(3, 4, Orientation.HORIZONTAL);
    assertEquals(3, w1.getPositionY());
    assertEquals(4, w1.getPositionX());
    assertEquals(Orientation.HORIZONTAL, w1.getOrientation());

    Wall w2 = new Wall(5, 2, Orientation.VERTICAL);
    assertEquals(Orientation.VERTICAL, w2.getOrientation());
  }

  @Test
  void testConstructorBoundaryValid() {
    assertDoesNotThrow(() -> new Wall(0, 0, Orientation.HORIZONTAL));
    assertDoesNotThrow(() -> new Wall(7, 7, Orientation.HORIZONTAL));
    assertDoesNotThrow(() -> new Wall(0, 7, Orientation.VERTICAL));
    assertDoesNotThrow(() -> new Wall(7, 0, Orientation.VERTICAL));
  }

  @Test
  void testConstructorInvalid() {
    // Invalid limit values
    assertThrows(IllegalArgumentException.class, () -> new Wall(-1, 4, Orientation.HORIZONTAL));
    assertThrows(IllegalArgumentException.class, () -> new Wall(8, 4, Orientation.HORIZONTAL));

    assertThrows(IllegalArgumentException.class, () -> new Wall(4, -1, Orientation.VERTICAL));
    assertThrows(IllegalArgumentException.class, () -> new Wall(4, 8, Orientation.VERTICAL));

    // Out of range values
    assertThrows(IllegalArgumentException.class, () -> new Wall(-5, 15, Orientation.HORIZONTAL));

    // Orientation null
    assertThrows(IllegalArgumentException.class, () -> new Wall(3, 3, null));
  }

  @Test
  void testConstructorBoundaryValuesStored() {
    Wall min = new Wall(0, 0, Orientation.VERTICAL);
    assertEquals(0, min.getPositionY());
    assertEquals(0, min.getPositionX());

    Wall max = new Wall(7, 7, Orientation.HORIZONTAL);
    assertEquals(7, max.getPositionY());
    assertEquals(7, max.getPositionX());
    assertEquals(Orientation.HORIZONTAL, max.getOrientation());
  }

  @Test
  void testConstructorInvalidBothOrientations() {
    for (Orientation o : Orientation.values()) {
      assertThrows(IllegalArgumentException.class, () -> new Wall(-1, 0, o));
      assertThrows(IllegalArgumentException.class, () -> new Wall(8, 0, o));
      assertThrows(IllegalArgumentException.class, () -> new Wall(0, -1, o));
      assertThrows(IllegalArgumentException.class, () -> new Wall(0, 8, o));
    }
  }

  @Test
  void testConstructorInvalidAnchorAndNullOrientation() {
    // Both preconditions fail: it must throw anyway
    assertThrows(IllegalArgumentException.class, () -> new Wall(-1, 8, null));
  }

  // Data-driven: los casos (particiones y valores límite) se leen de wall_anchors.csv
  @ParameterizedTest(name = "{3}")
  @CsvFileSource(resources = "/wall_anchors.csv", numLinesToSkip = 1)
  void testIsValidAnchor(int positionY, int positionX, boolean expected, String caso) {
    assertEquals(expected, Wall.isValidAnchor(positionY, positionX));
  }
}