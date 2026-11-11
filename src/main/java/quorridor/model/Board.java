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

  /**
   * Checks if there is at least one valid path from (startY, startX) to (targetGoalY)
   * using a Breadth-First Search (BFS) traversal, respecting placed walls.
   */
  public boolean hasPath(int startY, int startX, int targetGoalY) {
    // Invariant
    assert invariant();

    // Pre-conditions
    if (!isInside(startY, startX)) {
      throw new IllegalArgumentException("Start position is out of the board");
    }
    if (targetGoalY != 0 && targetGoalY != 8) {
      throw new IllegalArgumentException("Target goal Y must be 0 or 8");
    }

    // If already at the goal row, path exists
    if (startY == targetGoalY) {
      return true;
    }

    java.util.Queue<int[]> queue = new java.util.LinkedList<>();
    java.util.Set<String> visited = new java.util.HashSet<>();

    queue.add(new int[]{startY, startX});
    visited.add(startY + "," + startX);

    // Orthogonal directions: up, down, left, right
    int[] dY = {-1, 1, 0, 0};
    int[] dX = {0, 0, -1, 1};

    while (!queue.isEmpty()) {
      int[] current = queue.poll();
      int currY = current[0];
      int currX = current[1];

      // Reached the target goal row
      if (currY == targetGoalY) {
        return true;
      }

      for (int i = 0; i < 4; i++) {
        int nextY = currY + dY[i];
        int nextX = currX + dX[i];

        // Check if next cell is within bounds and not blocked by a wall
        if (isInside(nextY, nextX)) {
          if (!isBlocked(currY, currX, nextY, nextX)) {
            String key = nextY + "," + nextX;
            if (!visited.contains(key)) {
              visited.add(key);
              queue.add(new int[]{nextY, nextX});
            }
          }
        }
      }
    }

    return false; // Player trapped
  }

  // Adds a wall to the board
  public void addWall(Wall wall) {
    // Invariant (begin)
    assert invariant();

    // Pre-condition
    if (wall == null) {
      throw new IllegalArgumentException("Wall can not be null");
    }

    if (isOverlapping(wall)) {
      throw new IllegalArgumentException("Wall overlaps an existing wall");
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
    for (Wall other : walls) {
      // Same anchor: it is the same wall, or they cross each other in a "+"
      if (other.getPositionY() == wall.getPositionY()
          && other.getPositionX() == wall.getPositionX()) {
        return true;
      }

      // Two horizontal walls in the same row, shifted 1 column -> they share a cell
      if (wall.getOrientation() == Orientation.HORIZONTAL
          && other.getOrientation() == Orientation.HORIZONTAL
          && other.getPositionY() == wall.getPositionY()
          && Math.abs(other.getPositionX() - wall.getPositionX()) == 1) {
        return true;
      }

      // Two vertical walls in the same column, shifted 1 row -> they share a cell
      if (wall.getOrientation() == Orientation.VERTICAL
          && other.getOrientation() == Orientation.VERTICAL
          && other.getPositionX() == wall.getPositionX()
          && Math.abs(other.getPositionY() - wall.getPositionY()) == 1) {
        return true;
      }
    }
    return false;
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