package quorridor.model;

public class MoveValidation {
  private final Board board;
  boolean invariant() {
    return (board != null);
  }

  public MoveValidation(Board board) {
    // Pre-condition
    if (board == null) {
      throw new IllegalArgumentException("Board cannot be null");
    }

    this.board = board;

    // Assert invariant
    assert invariant();
  }

  public boolean isValidMove(Position from, Position to) {
    // Assert invariant al inicio
    assert invariant();

    // Pre-conditions
    if (from == null || to == null) {
      throw new IllegalArgumentException("Positions cannot be null");
    }

    // Movement square to square must be adjacent
    int diffY = Math.abs(from.getY() - to.getY());
    int diffX = Math.abs(from.getX() - to.getX());

    if (diffY + diffX != 1) {
      return false;
    }

    // Check if there's any wall between squares
    boolean result = !board.isBlocked(from.getY(), from.getX(), to.getY(), to.getX());

    assert invariant();

    return result;
  }
}