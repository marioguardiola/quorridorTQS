package quorridor.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

  @Test
  void testConstructorValid() {
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
    assertDoesNotThrow(() -> new Player("A", 1, 0, 0, 0, 8));
    assertDoesNotThrow(() -> new Player("A", 1, 10, 8, 8, 0));
    assertDoesNotThrow(() -> new Player("A", 1, 10, 0, 8, 8));
    assertDoesNotThrow(() -> new Player("A", 1, 10, 8, 0, 0));
    // Fila intermedia: se acepta cualquier meta válida (0 u 8)
    assertDoesNotThrow(() -> new Player("A", 1, 10, 4, 4, 0));
    assertDoesNotThrow(() -> new Player("A", 1, 10, 4, 4, 8));
  }

  @Test
  void testConstructorInvalidName() {
    assertThrows(IllegalArgumentException.class, () -> new Player(null, 1, 10, 0, 0, 8));
    assertThrows(IllegalArgumentException.class, () -> new Player("", 1, 10, 0, 0, 8));
    assertThrows(IllegalArgumentException.class, () -> new Player(" ", 1, 10, 0, 0, 8));
  }

  @Test
  void testConstructorInvalidWalls() {
    // Invalid limit values of Walls: -1 y 11
    assertThrows(IllegalArgumentException.class, () -> new Player("A", 1, -1, 0, 0, 8));
    assertThrows(IllegalArgumentException.class, () -> new Player("A", 1, 11, 0, 0, 8));
  }

  @Test
  void testConstructorInvalidPosition() {
    // Invalid limit values of Positions: -1 y 9
    assertThrows(IllegalArgumentException.class, () -> new Player("A", 1, 10, -1, 0, 8));
    assertThrows(IllegalArgumentException.class, () -> new Player("A", 1, 10, 9, 0, 8));
    assertThrows(IllegalArgumentException.class, () -> new Player("A", 1, 10, 0, -1, 8));
    assertThrows(IllegalArgumentException.class, () -> new Player("A", 1, 10, 0, 9, 8));
  }

  @Test
  void testConstructorInvalidGoal() {
    // Invalid goal values
    assertThrows(IllegalArgumentException.class, () -> new Player("A", 1, 10, 4, 4, -1));
    assertThrows(IllegalArgumentException.class, () -> new Player("A", 1, 10, 4, 4, 5));
    assertThrows(IllegalArgumentException.class, () -> new Player("A", 1, 10, 4, 4, 9));
  }

  // Position - Goal Test
  @Test
  void testConstructorGoalCoherence() {
    assertThrows(IllegalArgumentException.class, () -> new Player("A", 1, 10, 0, 4, 0));
    assertThrows(IllegalArgumentException.class, () -> new Player("A", 2, 10, 8, 4, 8));
  }

  @Test
  void testSetPositionValidBoundaries() {
    Player p = new Player("A", 1, 10, 0, 0, 8);
    int[][] corners = {{0, 0}, {8, 8}, {0, 8}, {8, 0}};
    for (int[] c : corners) {
      p.setPosition(c[0], c[1]);
      assertEquals(c[0], p.getPositionY());
      assertEquals(c[1], p.getPositionX());
    }
  }

  @Test
  void testSetPositionInvalid() {
    Player p = new Player("A", 1, 10, 0, 4, 8);
    assertThrows(IllegalArgumentException.class, () -> p.setPosition(-1, 4));
    assertThrows(IllegalArgumentException.class, () -> p.setPosition(9, 4));
    assertThrows(IllegalArgumentException.class, () -> p.setPosition(0, -1));
    // Check state
    assertEquals(0, p.getPositionY());
    assertEquals(4, p.getPositionX());
  }

  @Test
  void testUseWall() {
    Player p = new Player("A", 1, 10, 0, 0, 8);
    p.useWall();
    // Check post-Condition
    assertEquals(9, p.getWalls());
  }

  @Test
  void testUseWallUntilEmpty() {
    Player p = new Player("A", 1, 1, 0, 0, 8);
    assertTrue(p.hasWalls());
    p.useWall();
    assertEquals(0, p.getWalls());
    assertFalse(p.hasWalls());

    // Cehck pre-condition
    assertThrows(IllegalStateException.class, p::useWall);
    assertEquals(0, p.getWalls()); // estado intacto tras el fallo
  }

  @Test
  void testHasWalls() {
    assertTrue(new Player("A", 1, 10, 0, 0, 8).hasWalls());
    assertTrue(new Player("A", 1, 5, 0, 0, 8).hasWalls());
    assertTrue(new Player("A", 1, 1, 0, 0, 8).hasWalls());
    assertFalse(new Player("A", 1, 0, 0, 0, 8).hasWalls());
  }


  @Test
  void testHasReachedGoal() {
    Player p1 = new Player("A", 1, 10, 0, 7, 8);
    assertFalse(p1.hasReachedGoal());
    p1.setPosition(4, 2); // fila intermedia
    assertFalse(p1.hasReachedGoal());
    p1.setPosition(8, 2);
    assertTrue(p1.hasReachedGoal());

    Player p2 = new Player("B", 2, 10, 8, 4, 0);
    assertFalse(p2.hasReachedGoal());
    p2.setPosition(0, 5);
    assertTrue(p2.hasReachedGoal());
  }
}