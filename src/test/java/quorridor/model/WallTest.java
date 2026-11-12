package quorridor.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

/**
 * Tests de Wall (clase de bajo nivel).
 * Caja negra: particiones equivalentes y valores límite del ancla (rango válido [0, 7]).
 */
public class WallTest {

  @Test
  void testConstructorValid() {
    // Caja negra - partición válida: muro horizontal en el interior del rango
    Wall w1 = new Wall(3, 4, Orientation.HORIZONTAL);
    assertEquals(3, w1.getPositionY());
    assertEquals(4, w1.getPositionX());
    assertEquals(Orientation.HORIZONTAL, w1.getOrientation());

    // Caja negra - partición válida: muro vertical en el interior del rango
    Wall w2 = new Wall(5, 2, Orientation.VERTICAL);
    assertEquals(Orientation.VERTICAL, w2.getOrientation());
  }

  @Test
  void testConstructorBoundaryValid() {
    // Caja negra - valores frontera válidos: las 4 esquinas del rango de anclaje (0 y 7)
    assertDoesNotThrow(() -> new Wall(0, 0, Orientation.HORIZONTAL));
    assertDoesNotThrow(() -> new Wall(7, 7, Orientation.HORIZONTAL));
    assertDoesNotThrow(() -> new Wall(0, 7, Orientation.VERTICAL));
    assertDoesNotThrow(() -> new Wall(7, 0, Orientation.VERTICAL));
  }

  @Test
  void testConstructorInvalid() {
    // Caja negra - valores límite exteriores en Y: -1 y 8 (justo fuera de [0, 7])
    assertThrows(IllegalArgumentException.class, () -> new Wall(-1, 4, Orientation.HORIZONTAL));
    assertThrows(IllegalArgumentException.class, () -> new Wall(8, 4, Orientation.HORIZONTAL));

    // Caja negra - valores límite exteriores en X: -1 y 8
    assertThrows(IllegalArgumentException.class, () -> new Wall(4, -1, Orientation.VERTICAL));
    assertThrows(IllegalArgumentException.class, () -> new Wall(4, 8, Orientation.VERTICAL));

    // Caja negra - partición inválida: valores muy fuera del tablero
    assertThrows(IllegalArgumentException.class, () -> new Wall(-5, 15, Orientation.HORIZONTAL));

    // Caja negra - partición inválida: orientación nula (precondición)
    assertThrows(IllegalArgumentException.class, () -> new Wall(3, 3, null));
  }

  @Test
  void testConstructorBoundaryValuesStored() {
    // Caja negra - valor frontera inferior (0, 0): se guarda correctamente
    Wall min = new Wall(0, 0, Orientation.VERTICAL);
    assertEquals(0, min.getPositionY());
    assertEquals(0, min.getPositionX());

    // Caja negra - valor frontera superior (7, 7): se guarda correctamente
    Wall max = new Wall(7, 7, Orientation.HORIZONTAL);
    assertEquals(7, max.getPositionY());
    assertEquals(7, max.getPositionX());
    assertEquals(Orientation.HORIZONTAL, max.getOrientation());
  }

  @Test
  void testConstructorInvalidBothOrientations() {
    // Caja negra - valores límite exteriores (-1 y 8) para cada partición de orientación
    for (Orientation o : Orientation.values()) {
      assertThrows(IllegalArgumentException.class, () -> new Wall(-1, 0, o));
      assertThrows(IllegalArgumentException.class, () -> new Wall(8, 0, o));
      assertThrows(IllegalArgumentException.class, () -> new Wall(0, -1, o));
      assertThrows(IllegalArgumentException.class, () -> new Wall(0, 8, o));
    }
  }

  @Test
  void testConstructorInvalidAnchorAndNullOrientation() {
    // Caja negra - partición inválida: fallan las dos precondiciones a la vez
    // (ancla fuera de rango y orientación nula) -> debe lanzar excepción igualmente
    assertThrows(IllegalArgumentException.class, () -> new Wall(-1, 8, null));
  }

  // Data-driven: los casos (particiones y valores límite) se leen de wall_anchors.csv
  // Caja negra - para el rango [0, 7]: exteriores (-1, 8), frontera (0, 7),
  // interiores a frontera (1, 6) e interiores, en Y y en X
  @ParameterizedTest(name = "{3}")
  @CsvFileSource(resources = "/wall_anchors.csv", numLinesToSkip = 1)
  void testIsValidAnchor(int positionY, int positionX, boolean expected, String caso) {
    assertEquals(expected, Wall.isValidAnchor(positionY, positionX));
  }
}