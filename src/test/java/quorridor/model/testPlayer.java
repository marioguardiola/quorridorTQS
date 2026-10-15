package quorridor.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class testPlayer {

  // Constructor
  @Test
  void testPlayer() {
    // Normal player
    Player p1 = new Player("Irene", 1, 10, 0, 0, 8);
    assertEquals("Irene", p1.getName());
    assertEquals(10, p1.getWalls());
    assertEquals(0, p1.getPositionY());
    assertEquals(0, p1.getPositionX());
    assertEquals(8, p1.getGoalY());

    /* Invalid constructors */
    // Player with name null
    assertThrows(IllegalArgumentException.class, () ->
        new Player(null, 10, 10, 8, 8, 0));

    // Player with no name
    assertThrows(IllegalArgumentException.class, () ->
        new Player(" ", 10, 10, 8, 8, 0));

    // Player out of board (9x9)
    assertThrows(IllegalArgumentException.class, () ->
        new Player("Irene", 4, 10, -1, 0, 8));
    assertThrows(IllegalArgumentException.class, () ->
        new Player("Irene", 4, 10, 9, 0, 8));
    assertThrows(IllegalArgumentException.class, () ->
        new Player("Irene", 4, 10, 0, -1, 8));
    assertThrows(IllegalArgumentException.class, () ->
        new Player("Irene", 4, 10, 9, 9, 8));

    // Player with -1 walls || 11 walls
    assertThrows(IllegalArgumentException.class, () ->
        new Player("Irene", 1, 11, 0, 0, 8));
    assertThrows(IllegalArgumentException.class, () ->
        new Player("Irene", 4, -1, 0, 0, 8));

    // GoalBorder Player wrong
    // Starts in (0,0), goal can not be 0
    assertThrows(IllegalArgumentException.class, () ->
        new Player("Irene", 1, 10, 0, 4, 0)
    );

    // Starts in (0,0) goal can not be 5
    assertThrows(IllegalArgumentException.class, () ->
        new Player("Irene", 1, 10, 0, 4, 5)
    );

    // Starts in (8,8) goal can not be 8
    assertThrows(IllegalArgumentException.class, () ->
        new Player("Irene", 2, 10, 8, 4, 8)
    );
  }

  @Test
  void testSetPosition(){
    // Assert boundary values (0,0), (8,8), (0,8), (8,0)
    Player p1 = new Player("Irene", 1, 10, 0, 0, 8);

    p1.setPosition(0,0);
    assertEquals(0,p1.getPositionY());
    assertEquals(0,p1.getPositionX());

    p1.setPosition(8, 8);
    assertEquals(8, p1.getPositionY());
    assertEquals(8, p1.getPositionX());

    p1.setPosition(0, 8);
    assertEquals(0, p1.getPositionY());
    assertEquals(8, p1.getPositionX());

    p1.setPosition(8, 0);
    assertEquals(8, p1.getPositionY());
    assertEquals(0, p1.getPositionX());

    // Assert limit values
    assertThrows(IllegalArgumentException.class, () -> p1.setPosition(-1, 4));
    assertThrows(IllegalArgumentException.class, () -> p1.setPosition(9, 4));
    assertThrows(IllegalArgumentException.class, () -> p1.setPosition(0, -1));
    assertThrows(IllegalArgumentException.class, () -> p1.setPosition(0, 9));
  }

  @Test
  void testHasWalls(){
    // Assert boundary values 0, 10
    Player p1 = new Player("Irene", 1, 10, 0, 0, 8);
    // assertTrue(p1.hasWalls()); // Fallará hasta implementar

    Player p2 = new Player("Irene", 2, 0, 0, 0, 8);
    // assertFalse(p2.hasWalls()); // Fallará hasta implementar

    Player p3 = new Player("Irene", 3, 5, 0, 0, 8);
    // assertTrue(p3.hasWalls()); // Fallará hasta implementar

    // Assert limit values
    assertThrows(IllegalArgumentException.class, () ->
        new Player("Irene", 1, -1, 0, 4, 8)
    );
    assertThrows(IllegalArgumentException.class, () ->
        new Player("Irene", 1, 11, 0, 4, 8)
    );
  }

  @Test
  void testHasReachedGoals(){
    Player p1 = new Player("Irene", 1, 10, 0, 0, 8);
    assertFalse(p1.hasReachedGoal());

    /* TODO: Aqui hay una confusion, como determinamos de donde sale cada uno para determinar el goalX
        Es decir si (0,0) -> goal = 8 || (8,8) -> goal = 0 */
    Player p2 = new Player("Irene", 2, 3, 8,3,8);
    assertTrue(p2.hasReachedGoal());

    Player p3 = new Player("Irene", 3, 3, 0,3,0);
    assertTrue(p2.hasReachedGoal());

    // Assert limit values
    assertThrows(IllegalArgumentException.class, () -> new Player("Irene", 1, 10, 0, 4, 9));
    assertThrows(IllegalArgumentException.class, () -> new Player("Irene", 1, 10, 0, 4, -1));
  }

}