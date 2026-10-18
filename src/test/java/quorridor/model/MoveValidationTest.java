package quorridor.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
  void testIsValidMove_InvalidDistance() {
    Position from = new Position(2, 2);
    Position to = new Position(2, 4); // Salto de 2 casillas (inválido)

    assertFalse(validator.isValidMove(from, to));

    // No need to consultate board -> Logic invalid
    verify(boardMock, never()).isBlocked(anyInt(), anyInt(), anyInt(), anyInt());
  }
}