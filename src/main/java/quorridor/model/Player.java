package quorridor.model;

/**
 * Pawn: name, wall (10), position (x,y), goalY
 */
public class Player {
  private String name;
  private int idUser;
  private int walls;
  private int positionY;
  private int positionX;
  private int goalY;

  public Player(String name, int idUser, int walls, int positionY, int positionX, int goalY) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  // Getters
  public String getName() { return this.name; }
  public int getWalls() { return this.walls; }
  public int getPositionY() { return this.positionY; }
  public int getPositionX() { return this.positionX; }
  public int getGoalY() { return this.goalY; } // CORREGIDO

  // Métodos (Sin implementar para TDD)
  public void setPosition(int positionY, int positionX) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  public void useWall() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  public boolean hasWalls() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  public boolean hasReachedGoal() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

}