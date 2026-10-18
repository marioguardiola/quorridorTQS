package quorridor.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Board: 9x9 cells + list of placed walls.
 * Pawn positions are stored in each Player, not here.
 */
public class Board {
  public static final int SIZE = 9;

  private List<Wall> walls;

  public Board() {
    this.walls = new ArrayList<>();
  }

  // Adds a wall to the board
  public void addWall(Wall wall) {
    // TODO: implementar con TDD
  }

  // Returns true if a wall blocks moving from (fromY, fromX) to the adjacent cell (toY, toX)
  public boolean isBlocked(int fromY, int fromX, int toY, int toX) {
    return false; // TODO: implementar con TDD
  }

  // Getters
  public int getWallCount() { return this.walls.size(); }
}