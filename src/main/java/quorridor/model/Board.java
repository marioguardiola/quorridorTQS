package quorridor.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Board: 9x9 cells + list of placed walls.
 * Pawn positions are stored in each Player, not here.
 */
public class Board {
  public static final int SIZE = 9;

  // Max walls on the board: 2 players x 10 walls each
  public static final int MAX_WALLS = 20;

  // Orthogonal directions used by hasPath: up, down, left, right
  private static final int[] DIR_Y = {-1, 1, 0, 0};
  private static final int[] DIR_X = {0, 0, -1, 1};

  private List<Wall> walls;

  public Board() {
    this.walls = new ArrayList<>();
  }

  // Invariant: the wall list exists and never has more than MAX_WALLS walls
  boolean invariant() {
    return (walls != null && walls.size() <= MAX_WALLS);
  }

  // Versión que acepta sólo el muro (para compatibilidad o tableros sin jugadores)
  public void addWall(Wall wall) {
    addWall(wall, null);
  }

  // Full version: receives the players to check that no one gets trapped
  public void addWall(Wall wall, List<Player> players) {
    // Invariant (begin)
    assert invariant();

    // Pre-conditions
    if (wall == null) {
      throw new IllegalArgumentException("Wall can not be null");
    }
    if (walls.size() >= MAX_WALLS) {
      throw new IllegalStateException("Board already has the maximum number of walls");
    }
    if (isOverlapping(wall)) {
      throw new IllegalArgumentException("Wall overlaps an existing wall");
    }

    // Logic: temporarily add the wall to check paths
    int countPre = walls.size();
    walls.add(wall);

    // Check if every player still has at least one valid path to their goal
    if (players != null) {
      for (Player player : players) {
        if (!hasPath(player.getPositionY(), player.getPositionX(), player.getGoalY())) {
          // Rollback: remove the wall because it traps a player
          walls.remove(walls.size() - 1);
          throw new IllegalArgumentException(
              "Wall cannot be placed: it completely traps player " + player.getName());
        }
      }
    }

    // Post-condition: exactly one more wall on the board
    assert walls.size() == countPre + 1;

    // Invariant (end)
    assert invariant();
  }

  /**
   * Checks if there is at least one valid path from (startY, startX) to the target
   * goal row (targetGoalY) using a Breadth-First Search (BFS) traversal,
   * respecting placed walls.
   */
  public boolean hasPath(int startY, int startX, int targetGoalY) {
    // Invariant check
    assert invariant();

    // Pre-conditions
    if (!isInside(startY, startX)) {
      throw new IllegalArgumentException("Start position is out of the board");
    }
    if (targetGoalY != 0 && targetGoalY != SIZE - 1) {
      throw new IllegalArgumentException("Target goal Y must be 0 or 8");
    }

    // If already at the goal row, path exists
    if (startY == targetGoalY) {
      return true;
    }

    // v2: visited cells in a boolean matrix (instead of "y,x" strings)
    boolean[][] visited = new boolean[SIZE][SIZE];
    Queue<int[]> queue = new LinkedList<>();
    queue.add(new int[]{startY, startX});
    visited[startY][startX] = true;

    while (!queue.isEmpty()) {
      int[] current = queue.poll();
      int currY = current[0];
      int currX = current[1];

      // Reached the target goal row
      if (currY == targetGoalY) {
        return true;
      }

      for (int i = 0; i < DIR_Y.length; i++) {
        int nextY = currY + DIR_Y[i];
        int nextX = currX + DIR_X[i];

        // Next cell: inside the board, not visited yet and not blocked by a wall
        if (isInside(nextY, nextX) && !visited[nextY][nextX]
            && !isBlocked(currY, currX, nextY, nextX)) {
          visited[nextY][nextX] = true;
          queue.add(new int[]{nextY, nextX});
        }
      }
    }

    return false; // Queue emptied without reaching the goal -> player is trapped
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