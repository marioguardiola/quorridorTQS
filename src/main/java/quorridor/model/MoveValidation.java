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

  // Path available and jump pawn if its trapped
  public boolean isValidMove(Position from, Position to, Position opponentPos) {
    // Assert invariant
    assert invariant();

    // Pre-conditions
    if (from == null || to == null) {
      throw new IllegalArgumentException("Positions cannot be null");
    }

    int diffY = Math.abs(from.getY() - to.getY());
    int diffX = Math.abs(from.getX() - to.getX());
    boolean valid = false;

    // 1. Normal movement -> 1 square
    if (diffY + diffX == 1) {
      // 1. Normal movement -> 1 square (cannot land on the opponent)
      boolean onOpponent = opponentPos != null
          && opponentPos.getY() == to.getY()
          && opponentPos.getX() == to.getX();
      valid = !onOpponent && !board.isBlocked(from.getY(), from.getX(), to.getY(), to.getX());
    }else if ((diffY == 2 && diffX == 0) || (diffY == 0 && diffX == 2)) {
      // Calculate where is the opponent
      int midY = (from.getY() + to.getY()) / 2;
      int midX = (from.getX() + to.getX()) / 2;
      Position middlePos = new Position(midY, midX);

      // Verify that it is in the square
      if (opponentPos != null && opponentPos.getY() == midY && opponentPos.getX() == midX) {
        // Verify walls arround
        boolean blockedFirstStep = board.isBlocked(from.getY(), from.getX(), midY, midX);
        boolean blockedSecondStep = board.isBlocked(midY, midX, to.getY(), to.getX());
        valid = !blockedFirstStep && !blockedSecondStep;
      }
    }

    assert invariant();
    return valid;
  }

  // Sobrecarga por compatibilidad si se llama sin oponente (movimiento simple)
  public boolean isValidMove(Position from, Position to) {
    return isValidMove(from, to, null);
  }
}