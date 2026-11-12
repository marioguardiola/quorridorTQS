package quorridor.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de Player (clase de bajo nivel).
 * Caja negra: particiones equivalentes y valores límite.
 * Rangos: muros [0, 10], posición Y y X [0, 8], meta Y en {0, 8}.
 * Para cada rango [a, b] se prueban: exteriores (a-1, b+1), frontera (a, b),
 * interiores a frontera (a+1, b-1) e interiores.
 */
public class PlayerTest {

  // ---------- Constructor ----------

  @Test
  void testConstructorValid() {
    // Caja negra - partición válida: jugador normal, se guardan todos los atributos
    Player p = new Player("Irene", 1, 10, 0, 0, 8);
    assertEquals("Irene", p.getName());
    assertEquals(1, p.getIdUser());
    assertEquals(10, p.getWalls());
    assertEquals(0, p.getPositionY());
    assertEquals(0, p.getPositionX());
    assertEquals(8, p.getGoalY());
  }

  @Test
  void testConstructorValidBoundaries() {
    // Caja negra - valores frontera: muros 0 y 10, posición en las 4 esquinas (0 y 8)
    assertDoesNotThrow(() -> new Player("A", 1, 0, 0, 0, 8));
    assertDoesNotThrow(() -> new Player("A", 1, 10, 8, 8, 0));
    assertDoesNotThrow(() -> new Player("A", 1, 10, 0, 8, 8));
    assertDoesNotThrow(() -> new Player("A", 1, 10, 8, 0, 0));

    // Caja negra - partición válida: en una fila intermedia se acepta cualquier meta (0 u 8)
    assertDoesNotThrow(() -> new Player("A", 1, 10, 4, 4, 0));
    assertDoesNotThrow(() -> new Player("A", 1, 10, 4, 4, 8));
  }

  @Test
  void testConstructorInnerBoundaries() {
    // Caja negra - valores interiores a frontera de los muros: 1 y 9
    assertEquals(1, new Player("A", 1, 1, 4, 4, 0).getWalls());
    assertEquals(9, new Player("A", 1, 9, 4, 4, 0).getWalls());

    // Caja negra - valores interiores a frontera de la posición: 1 y 7
    Player p = new Player("A", 1, 10, 1, 7, 0);
    assertEquals(1, p.getPositionY());
    assertEquals(7, p.getPositionX());
  }

  @Test
  void testConstructorInvalidName() {
    // Caja negra - partición inválida: nombre nulo
    assertThrows(IllegalArgumentException.class, () -> new Player(null, 1, 10, 0, 0, 8));
    // Caja negra - valor límite: nombre vacío (longitud 0)
    assertThrows(IllegalArgumentException.class, () -> new Player("", 1, 10, 0, 0, 8));
    // Caja negra - partición inválida: nombre solo con espacios
    assertThrows(IllegalArgumentException.class, () -> new Player(" ", 1, 10, 0, 0, 8));
  }

  @Test
  void testConstructorInvalidWalls() {
    // Caja negra - valores límite exteriores de los muros: -1 y 11
    assertThrows(IllegalArgumentException.class, () -> new Player("A", 1, -1, 0, 0, 8));
    assertThrows(IllegalArgumentException.class, () -> new Player("A", 1, 11, 0, 0, 8));
  }

  @Test
  void testConstructorInvalidPosition() {
    // Caja negra - valores límite exteriores de la posición: -1 y 9, en Y y en X
    assertThrows(IllegalArgumentException.class, () -> new Player("A", 1, 10, -1, 0, 8));
    assertThrows(IllegalArgumentException.class, () -> new Player("A", 1, 10, 9, 0, 8));
    assertThrows(IllegalArgumentException.class, () -> new Player("A", 1, 10, 0, -1, 8));
    assertThrows(IllegalArgumentException.class, () -> new Player("A", 1, 10, 0, 9, 8));
  }

  @Test
  void testConstructorInvalidGoal() {
    // Caja negra - valores límite exteriores de la meta: -1 y 9
    assertThrows(IllegalArgumentException.class, () -> new Player("A", 1, 10, 4, 4, -1));
    assertThrows(IllegalArgumentException.class, () -> new Player("A", 1, 10, 4, 4, 9));
    // Caja negra - partición inválida: meta en una fila intermedia (5)
    assertThrows(IllegalArgumentException.class, () -> new Player("A", 1, 10, 4, 4, 5));
  }

  @Test
  void testConstructorGoalCoherence() {
    // Caja negra - partición inválida: empezar en la fila 0 con meta en la fila 0
    assertThrows(IllegalArgumentException.class, () -> new Player("A", 1, 10, 0, 4, 0));
    // Caja negra - partición inválida: empezar en la fila 8 con meta en la fila 8
    assertThrows(IllegalArgumentException.class, () -> new Player("A", 2, 10, 8, 4, 8));
  }

  // ---------- setPosition / getPosition ----------

  @Test
  void testSetPositionValidBoundaries() {
    // Caja negra - valores frontera: las 4 esquinas del tablero (0 y 8)
    Player p = new Player("A", 1, 10, 0, 0, 8);
    int[][] corners = {{0, 0}, {8, 8}, {0, 8}, {8, 0}};
    for (int[] c : corners) {
      p.setPosition(c[0], c[1]);
      assertEquals(c[0], p.getPositionY());
      assertEquals(c[1], p.getPositionX());
    }
  }

  @Test
  void testSetPositionInnerBoundaries() {
    // Caja negra - valores interiores a frontera: 1 y 7, en Y y en X
    Player p = new Player("A", 1, 10, 0, 0, 8);
    p.setPosition(1, 7);
    assertEquals(1, p.getPositionY());
    assertEquals(7, p.getPositionX());
    p.setPosition(7, 1);
    assertEquals(7, p.getPositionY());
    assertEquals(1, p.getPositionX());
  }

  @Test
  void testSetPositionInvalid() {
    Player p = new Player("A", 1, 10, 0, 4, 8);
    // Caja negra - valores límite exteriores: -1 y 9, en Y y en X (precondición)
    assertThrows(IllegalArgumentException.class, () -> p.setPosition(-1, 4));
    assertThrows(IllegalArgumentException.class, () -> p.setPosition(9, 4));
    assertThrows(IllegalArgumentException.class, () -> p.setPosition(0, -1));
    assertThrows(IllegalArgumentException.class, () -> p.setPosition(0, 9));

    // La posición no cambia tras un intento fallido
    assertEquals(0, p.getPositionY());
    assertEquals(4, p.getPositionX());
  }

  @Test
  void testGetPosition() {
    // Caja negra - partición válida: getPosition devuelve un Position con las coordenadas
    Player p = new Player("A", 1, 10, 8, 4, 0);
    Position pos = p.getPosition();
    assertEquals(8, pos.getY());
    assertEquals(4, pos.getX());

    // Después de moverse, getPosition refleja la nueva posición
    p.setPosition(7, 4);
    assertEquals(7, p.getPosition().getY());
    assertEquals(4, p.getPosition().getX());
  }

  // ---------- useWall / hasWalls ----------

  @Test
  void testUseWall() {
    // Caja negra - valor frontera superior: con 10 muros, usar uno deja 9 (postcondición)
    Player p = new Player("A", 1, 10, 0, 0, 8);
    p.useWall();
    assertEquals(9, p.getWalls());
  }

  @Test
  void testUseWallUntilEmpty() {
    // Caja negra - valor interior a frontera: con 1 muro todavía puede usarlo
    Player p = new Player("A", 1, 1, 0, 0, 8);
    assertTrue(p.hasWalls());
    p.useWall();
    assertEquals(0, p.getWalls());
    assertFalse(p.hasWalls());

    // Caja negra - valor frontera inferior: con 0 muros no puede usar ninguno (precondición)
    assertThrows(IllegalStateException.class, p::useWall);
    assertEquals(0, p.getWalls()); // el estado no cambia tras el fallo
  }

  @Test
  void testHasWalls() {
    // Caja negra - valor frontera superior (10) -> tiene muros
    assertTrue(new Player("A", 1, 10, 0, 0, 8).hasWalls());
    // Caja negra - valor interior (5) -> tiene muros
    assertTrue(new Player("A", 1, 5, 0, 0, 8).hasWalls());
    // Caja negra - valor interior a frontera (1): justo encima del límite de walls > 0
    assertTrue(new Player("A", 1, 1, 0, 0, 8).hasWalls());
    // Caja negra - valor frontera inferior (0) -> no tiene muros
    assertFalse(new Player("A", 1, 0, 0, 0, 8).hasWalls());
  }

  // ---------- hasReachedGoal ----------

  @Test
  void testHasReachedGoal() {
    // Jugador que empieza arriba y tiene la meta en la fila 8
    Player p1 = new Player("A", 1, 10, 0, 7, 8);
    // Caja negra - partición: todavía en la fila de salida -> no ha llegado
    assertFalse(p1.hasReachedGoal());
    // Caja negra - partición: en una fila intermedia -> no ha llegado
    p1.setPosition(4, 2);
    assertFalse(p1.hasReachedGoal());
    // Caja negra - valor límite: en la fila 7, justo antes de la meta -> no ha llegado
    p1.setPosition(7, 2);
    assertFalse(p1.hasReachedGoal());
    // Caja negra - valor frontera: en la fila meta (8) -> ha llegado
    p1.setPosition(8, 2);
    assertTrue(p1.hasReachedGoal());

    // Jugador que empieza abajo y tiene la meta en la fila 0
    Player p2 = new Player("B", 2, 10, 8, 4, 0);
    assertFalse(p2.hasReachedGoal());
    // Caja negra - valor límite: en la fila 1, justo antes de la meta -> no ha llegado
    p2.setPosition(1, 5);
    assertFalse(p2.hasReachedGoal());
    // Caja negra - valor frontera: en la fila meta (0) -> ha llegado
    p2.setPosition(0, 5);
    assertTrue(p2.hasReachedGoal());
  }
}