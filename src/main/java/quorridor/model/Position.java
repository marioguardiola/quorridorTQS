package quorridor.model;

public class Position {
  private int y, x;

  public boolean invariant(){
    return ((y>=0 && y<=8) && (x<=8 && x>=0));
  }

  public Position(int y, int x) {
    // Pre-conditions
    if (y < 0 || y > 8 || x < 0 || x > 8) {throw new IllegalArgumentException("The position is out of the board");}

    // Logic
    this.y = y;
    this.x = x;

    assert invariant();
  }

  public void setY(int y) {
    // Assert invariant
    assert invariant();

    // Pre-condition
    if(y < 0 || y > 8) {throw new IllegalArgumentException("Position Y invailid");}

    // Logic
    this.y = y;

    assert invariant();
  }
  public void setX(int x) {
    // Assert invariant
    assert invariant();

    // Pre-condition
    if(x < 0 || x > 8) {throw new IllegalArgumentException("Position X invailid");}

    // Logic
    this.x = x;

    assert invariant();
  }
  public int getY() {return y;}
  public int getX() {return x;}
}
