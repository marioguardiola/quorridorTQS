package quorridor.model;

/**
 * Pawn: name, wall (10), posiotion (x,y), goalY
 */
public class Player {

  // Atributs
  private String name;
  private int walls;
  private int positionY;
  private int positionX;
  private int goalY;

  public Player(String name, int walls, int positionY, int positionX, int goalY) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  // Getters
  public String getName() { return name; }
  public int getWalls() { return walls; }
  public int getPositionY() { return positionY; }
  public int getPositionX() { return positionX; }
  public int getGoalY() { return goalY; }

  // Methods
  public void setPosition(int positionY, int positionX) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  public boolean hasWalls() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  public boolean hasReachedGoal() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  public void useWall() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

}