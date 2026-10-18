package quorridor.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class WallTest {

  @Test
  void testConstructorValid() {
    // Partición válida: muro horizontal en el centro del tablero
    Wall w1 = new Wall(3, 4, Orientation.HORIZONTAL);
    assertEquals(3, w1.getPositionY());
    assertEquals(4, w1.getPositionX());
    assertEquals(Orientation.HORIZONTAL, w1.getOrientation());

    // Partición válida: muro vertical en el centro del tablero
    Wall w2 = new Wall(5, 2, Orientation.VERTICAL);
    assertEquals(Orientation.VERTICAL, w2.getOrientation());
  }

  @Test
  void testConstructorBoundaryValid() {
    // Valores frontera válidos: las 4 esquinas del rango de anclaje (0 y 7)
    assertDoesNotThrow(() -> new Wall(0, 0, Orientation.HORIZONTAL));
    assertDoesNotThrow(() -> new Wall(7, 7, Orientation.HORIZONTAL));
    assertDoesNotThrow(() -> new Wall(0, 7, Orientation.VERTICAL));
    assertDoesNotThrow(() -> new Wall(7, 0, Orientation.VERTICAL));
  }

  @Test
  void testConstructorInvalid() {
    // Valores límite inválidos en Y: -1 y 8 (justo fuera de 0..7)
    assertThrows(IllegalArgumentException.class, () -> new Wall(-1, 4, Orientation.HORIZONTAL));
    assertThrows(IllegalArgumentException.class, () -> new Wall(8, 4, Orientation.HORIZONTAL));

    // Valores límite inválidos en X: -1 y 8
    assertThrows(IllegalArgumentException.class, () -> new Wall(4, -1, Orientation.VERTICAL));
    assertThrows(IllegalArgumentException.class, () -> new Wall(4, 8, Orientation.VERTICAL));

    // Partición inválida: valores muy fuera del tablero
    assertThrows(IllegalArgumentException.class, () -> new Wall(-5, 15, Orientation.HORIZONTAL));

    // Partición inválida: orientación nula
    assertThrows(IllegalArgumentException.class, () -> new Wall(3, 3, null));
  }

  // Data-driven: los casos (particiones y valores límite) se leen de wall_anchors.csv
  @ParameterizedTest(name = "{3}")
  @CsvFileSource(resources = "/wall_anchors.csv", numLinesToSkip = 1)
  void testIsValidAnchor(int positionY, int positionX, boolean expected, String caso) {
    assertEquals(expected, Wall.isValidAnchor(positionY, positionX));
  }
}