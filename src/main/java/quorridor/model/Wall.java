package quorridor.model;

/**
 * Wall: positionY, positionX (anchor 0-7), orientation
 */
public class Wall {
  private int positionY;
  private int positionX;
  private Orientation orientation;

  // Invariant: anchor inside [0-7] and orientation not null
  boolean invariant() {
    return (positionY >= 0 && positionY <= 7 &&
        positionX >= 0 && positionX <= 7 &&
        orientation != null);
  }

  public Wall(int positionY, int positionX, Orientation orientation) {
    // Pre-conditions
    if (positionY < 0 || positionY > 7 || positionX < 0 || positionX > 7) {
      throw new IllegalArgumentException("Wall anchor must be between 0 and 7");
    }
    if (orientation == null) {
      throw new IllegalArgumentException("Orientation can not be null");
    }

    // Logic
    this.positionY = positionY;
    this.positionX = positionX;
    this.orientation = orientation;

    assert invariant();
  }

  // Getters
  public int getPositionY() { return this.positionY; }
  public int getPositionX() { return this.positionX; }
  public Orientation getOrientation() { return this.orientation; }
}