package quorridor.model;

/**
 * Wall: positionY, positionX (anchor 0-7), orientation
 */
public class Wall {
  // A wall covers 2 cells, so on a 9x9 board its anchor goes from 0 to 7
  public static final int MIN_ANCHOR = 0;
  public static final int MAX_ANCHOR = 7;

  private int positionY;
  private int positionX;
  private Orientation orientation;

  // Invariant: anchor inside [MIN_ANCHOR, MAX_ANCHOR] and orientation not null
  boolean invariant() {
    return (isValidAnchor(positionY, positionX) && orientation != null);
  }

  // Returns true if (positionY, positionX) is a valid anchor for a wall
  public static boolean isValidAnchor(int positionY, int positionX) {
    return (positionY >= MIN_ANCHOR && positionY <= MAX_ANCHOR &&
        positionX >= MIN_ANCHOR && positionX <= MAX_ANCHOR);
  }

  public Wall(int positionY, int positionX, Orientation orientation) {
    // Pre-conditions
    if (!isValidAnchor(positionY, positionX)) {
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