package quorridor.model;

/**
 * Wall: positionY, positionX (anchor 0-7), orientation
 */
public class Wall {
  private int positionY;
  private int positionX;
  private Orientation orientation;

  public Wall(int positionY, int positionX, Orientation orientation) {
    // Logic (todavía sin comprobar nada)
    this.positionY = positionY;
    this.positionX = positionX;
    this.orientation = orientation;
  }

  // Getters
  public int getPositionY() { return this.positionY; }
  public int getPositionX() { return this.positionX; }
  public Orientation getOrientation() { return this.orientation; }
}