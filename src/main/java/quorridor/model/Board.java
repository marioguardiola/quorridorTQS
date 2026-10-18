package quorridor.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Board: 9x9 cells + list of placed walls.
 * Pawn positions are stored in each Player, not here.
 */
public class Board {
  public static final int SIZE = 9;

  // Max walls on the board: 2 players x 10 walls each
  public static final int MAX_WALLS = 20;

  private List<Wall> walls;

  public Board() {
    this.walls = new ArrayList<>();
  }

  // Invariant: the wall list exists and never has more than MAX_WALLS walls
  boolean invariant() {
    return (walls != null && walls.size() <= MAX_WALLS);
  }

  // Adds a wall to the board
  public void addWall(Wall wall) {
    // Invariant (begin)
    assert invariant();

    // Pre-condition
    if (wall == null) {
      throw new IllegalArgumentException("Wall can not be null");
    }

    // Logic
    int countPre = walls.size();
    walls.add(wall);

    // Post-condition: exactly one more wall
    assert walls.size() == countPre + 1;

    // Invariant (end)
    assert invariant();
  }

  // Returns true if a wall blocks moving from (fromY, fromX) to the adjacent cell (toY, toX)
  public boolean isBlocked(int fromY, int fromX, int toY, int toX) {
    return false; // TODO: implementar con TDD
  }

  // Getters
  public int getWallCount() { return this.walls.size(); }
}