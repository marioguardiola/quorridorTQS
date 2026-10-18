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

  // Returns true if the given wall overlaps or crosses any wall already on the board
  public boolean isOverlapping(Wall wall) {
    return false; // TODO: implementar con TDD
  }

  // Returns true if (y, x) is a cell inside the 9x9 board
  private boolean isInside(int y, int x) {
    return (y >= 0 && y < SIZE && x >= 0 && x < SIZE);
  }

  // Returns true if a wall blocks moving from (fromY, fromX) to the adjacent cell (toY, toX)
  // v2: vertical moves are blocked by HORIZONTAL walls, lateral moves by VERTICAL walls
  public boolean isBlocked(int fromY, int fromX, int toY, int toX) {
    // Invariant (begin)
    assert invariant();

    // Pre-conditions: both cells inside the board and orthogonally adjacent
    if (!isInside(fromY, fromX) || !isInside(toY, toX)) {
      throw new IllegalArgumentException("Cell out of board");
    }
    if (Math.abs(fromY - toY) + Math.abs(fromX - toX) != 1) {
      throw new IllegalArgumentException("Cells must be adjacent (no diagonals, no jumps)");
    }

    // Logic
    boolean verticalMove = (fromX == toX);
    int grooveY = Math.min(fromY, toY);  // groove between two rows
    int grooveX = Math.min(fromX, toX);  // groove between two columns

    for (Wall wall : walls) {
      if (verticalMove) {
        // Horizontal wall at (Y, X) covers columns X and X+1
        if (wall.getOrientation() == Orientation.HORIZONTAL
            && wall.getPositionY() == grooveY
            && (wall.getPositionX() == fromX || wall.getPositionX() == fromX - 1)) {
          return true;
        }
      } else {
        // Vertical wall at (Y, X) covers rows Y and Y+1
        if (wall.getOrientation() == Orientation.VERTICAL
            && wall.getPositionX() == grooveX
            && (wall.getPositionY() == fromY || wall.getPositionY() == fromY - 1)) {
          return true;
        }
      }
    }
    return false;
  }

  // Getters
  public int getWallCount() { return this.walls.size(); }
}