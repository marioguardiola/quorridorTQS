package quorridor.model;

public class Position {
  int y, x;

  public boolean invariant(){
    return ((y>=0 && y<=8) && (x<=8 && x>=0));
  }

  Position(int y, int x) {
    // Pre-conditions
    if (y < 0 || y > 8 || x < 0 || x > 8) {throw new IllegalArgumentException("The position is out of the board");}

    // Logic
    this.y = y;
    this.x = x;

    assert invariant();
  }

  void setY(int y) {
    // Assert invariant
    assert invariant();

    // Pre-condition
    if(y < 0 || y > 8) {throw new IllegalArgumentException("Position Y invailid");}

    // Logic
    this.y = y;

    assert invariant();
  }
  void setX(int x) {
    // Assert invariant
    assert invariant();

    // Pre-condition
    if(x < 0 || x > 8) {throw new IllegalArgumentException("Position Y invailid");}

    // Logic
    this.x = x;

    assert invariant();
  }
  int getY() {return y;}
  int getX() {return x;}
}
