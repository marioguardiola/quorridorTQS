package quorridor.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MoveValidationTest {
  private Board boardMock;
  private MoveValidation validator;

  @BeforeEach
  void setUp() {
    // Mocking Board so we can test MoveValidation in total isolation
    boardMock = Mockito.mock(Board.class);
    validator = new MoveValidation(boardMock);
  }

  @Test
  void testIsValidMove_FreePath() {
    Position from = new Position(4, 4);
    Position to = new Position(4, 5);

    // Mocking the board with no wall
    when(boardMock.isBlocked(4, 4, 4, 5)).thenReturn(false);

    assertTrue(validator.isValidMove(from, to));
    verify(boardMock).isBlocked(4, 4, 4, 5);
  }

  @Test
  void testIsValidMove_BlockedByWall() {
    Position from = new Position(4, 4);
    Position to = new Position(4, 5);

    // Mocking the board with wall
    when(boardMock.isBlocked(4, 4, 4, 5)).thenReturn(true);

    assertFalse(validator.isValidMove(from, to));
    verify(boardMock).isBlocked(4, 4, 4, 5);
  }

  @Test
  void testIsValidMove_InvalidDistances() {
    Position from = new Position(4, 4);
    Position opponent = new Position(4, 5);

    assertFalse(validator.isValidMove(from, new Position(4, 4), opponent)); // misma casilla
    assertFalse(validator.isValidMove(from, new Position(3, 3), opponent)); // diagonal
    assertFalse(validator.isValidMove(from, new Position(2, 3), opponent)); // 2 + 1
    assertFalse(validator.isValidMove(from, new Position(1, 4), opponent)); // distancia 3

    verify(boardMock, never()).isBlocked(anyInt(), anyInt(), anyInt(), anyInt());
  }

  @Test
  void testIsValidMove_CannotMoveOntoOpponent() {
    Position from = new Position(4, 4);
    Position to = new Position(3, 4);
    Position opponent = new Position(3, 4);

    assertFalse(validator.isValidMove(from, to, opponent));
    verify(boardMock, never()).isBlocked(anyInt(), anyInt(), anyInt(), anyInt());
  }

  @Test
  void testIsValidMove_ValidJump() {
    Position from = new Position(6, 4);
    Position opponent = new Position(5, 4);
    Position to = new Position(4, 4);

    // Mocking board
    when(boardMock.isBlocked(6, 4, 5, 4)).thenReturn(false);
    when(boardMock.isBlocked(5, 4, 4, 4)).thenReturn(false);

    assertTrue(validator.isValidMove(from, to, opponent));
    verify(boardMock).isBlocked(6, 4, 5, 4);
    verify(boardMock).isBlocked(5, 4, 4, 4);
  }

  @Test
  void testIsValidMove_JumpBlockedByWall() {
    Position from = new Position(6, 4);
    Position opponent = new Position(5, 4);
    Position to = new Position(4, 4);

    // Mocking board: blocked by a wall
    when(boardMock.isBlocked(6, 4, 5, 4)).thenReturn(false);
    when(boardMock.isBlocked(5, 4, 4, 4)).thenReturn(true);

    assertFalse(validator.isValidMove(from, to, opponent));
    verify(boardMock).isBlocked(6, 4, 5, 4);
    verify(boardMock).isBlocked(5, 4, 4, 4);
  }

  @Test
  void testIsValidMove_OpponentAdjacentButNotOnDestination() {
    Position from = new Position(4, 4);
    Position to = new Position(3, 4);

    // Without opponent
    assertTrue(validator.isValidMove(from, to, null));
    // Opponent with dif. Y
    assertTrue(validator.isValidMove(from, to, new Position(0, 0)));
    // Opponent with same Y dif. X
    assertTrue(validator.isValidMove(from, to, new Position(3, 5)));
  }

  // ---------- Salto en las 4 direcciones ----------

  @ParameterizedTest(name = "salto de ({0},{1}) sobre ({2},{3}) a ({4},{5})")
  @CsvSource({
      "6,4,5,4,4,4",   // up
      "2,4,3,4,4,4",   // down
      "4,6,4,5,4,4",   // left
      "4,2,4,3,4,4"    // right
  })
  void testIsValidMove_JumpAllDirections(int fy, int fx, int oy, int ox, int ty, int tx) {
    assertTrue(validator.isValidMove(new Position(fy, fx), new Position(ty, tx),
        new Position(oy, ox)));
    verify(boardMock).isBlocked(fy, fx, oy, ox);
    verify(boardMock).isBlocked(oy, ox, ty, tx);
  }

  @Test
  void testIsValidMove_JumpBlockedBeforeOpponent() {
    // Wall between players
    when(boardMock.isBlocked(6, 4, 5, 4)).thenReturn(true);
    when(boardMock.isBlocked(5, 4, 4, 4)).thenReturn(false);

    assertFalse(validator.isValidMove(new Position(6, 4), new Position(4, 4), new Position(5, 4)));
  }

  @Test
  void testIsValidMove_JumpOpponentNotInTheMiddle() {
    Position from = new Position(6, 4);
    Position to = new Position(4, 4);

    // Y distinta de la del medio
    assertFalse(validator.isValidMove(from, to, new Position(4, 5)));
    // Misma Y que el medio pero otra X
    assertFalse(validator.isValidMove(from, to, new Position(5, 3)));

    verify(boardMock, never()).isBlocked(anyInt(), anyInt(), anyInt(), anyInt());
  }

  @Test
  void testIsValidMove_NullPositions() {
    Position p = new Position(4, 4);
    assertThrows(IllegalArgumentException.class, () -> validator.isValidMove(null, p, null));
    assertThrows(IllegalArgumentException.class, () -> validator.isValidMove(p, null, null));
  }

  @Test
  void testConstructorNullBoard() {
    assertThrows(IllegalArgumentException.class, () -> new MoveValidation(null));
  }

  @Test
  void testIsValidMove_IntegrationWithRealBoard() {
    Board real = new Board();
    real.addWall(new Wall(4, 4, Orientation.HORIZONTAL)); // cubre columnas 4 y 5, entre filas 4 y 5
    MoveValidation v = new MoveValidation(real);

    assertFalse(v.isValidMove(new Position(5, 4), new Position(4, 4))); // muro lo impide
    assertTrue(v.isValidMove(new Position(5, 3), new Position(4, 3)));  // columna sin muro
  }
}