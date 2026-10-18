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
  // Returns true if a wall blocks moving from (fromY, fromX) to the adjacent cell (toY, toX)
  // v1: only vertical moves (up/down), which are blocked by HORIZONTAL walls
  public boolean isBlocked(int fromY, int fromX, int toY, int toX) {
    // The groove between the two rows is identified by the smaller row
    int grooveY = Math.min(fromY, toY);

    for (Wall wall : walls) {
      // A horizontal wall at (Y, X) covers columns X and X+1,
      // so it blocks column fromX if its X is fromX or fromX - 1
      if (wall.getOrientation() == Orientation.HORIZONTAL
          && wall.getPositionY() == grooveY
          && (wall.getPositionX() == fromX || wall.getPositionX() == fromX - 1)) {
        return true;
      }
    }
    return false;
  }

  // Getters
  public int getWallCount() { return this.walls.size(); }
}