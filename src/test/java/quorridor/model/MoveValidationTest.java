package quorridor.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests de MoveValidation (clase de bajo nivel).
 * Mock object (Mockito): Board se sustituye por un mock para probar MoveValidation
 * aislado del tablero real. Con when(...) decidimos si hay muro o no, y con verify(...)
 * comprobamos qué consultas hace MoveValidation al tablero.
 * Caja negra: particiones (movimiento simple, salto recto, distancias inválidas)
 * y valores límite.
 */
class MoveValidationTest {
  private Board boardMock;
  private MoveValidation validator;

  @BeforeEach
  void setUp() {
    // Fixture: mock de Board (Mockito) inyectado por el constructor (Inversion of Control)
    boardMock = Mockito.mock(Board.class);
    validator = new MoveValidation(boardMock);
  }

  // ---------- Movimiento simple (1 casilla) ----------

  @Test
  void testIsValidMove_FreePath() {
    // Caja negra - partición válida: movimiento de 1 casilla sin muro
    Position from = new Position(4, 4);
    Position to = new Position(4, 5);

    // Mock: el tablero responde que no hay muro
    when(boardMock.isBlocked(4, 4, 4, 5)).thenReturn(false);

    assertTrue(validator.isValidMove(from, to));
    // Mock: se ha consultado el tablero exactamente para ese movimiento
    verify(boardMock).isBlocked(4, 4, 4, 5);
  }

  @Test
  void testIsValidMove_BlockedByWall() {
    // Caja negra - partición inválida: movimiento de 1 casilla con muro en medio
    Position from = new Position(4, 4);
    Position to = new Position(4, 5);

    // Mock: el tablero responde que hay muro
    when(boardMock.isBlocked(4, 4, 4, 5)).thenReturn(true);

    assertFalse(validator.isValidMove(from, to));
    verify(boardMock).isBlocked(4, 4, 4, 5);
  }

  @Test
  void testIsValidMove_InvalidDistances() {
    Position from = new Position(4, 4);
    Position opponent = new Position(4, 5);

    // Caja negra - valor límite: distancia 0 (misma casilla)
    assertFalse(validator.isValidMove(from, new Position(4, 4), opponent));
    // Caja negra - partición inválida: diagonal (distancia 1 + 1)
    assertFalse(validator.isValidMove(from, new Position(3, 3), opponent));
    // Caja negra - partición inválida: distancia 2 + 1 (ni simple ni salto recto)
    assertFalse(validator.isValidMove(from, new Position(2, 3), opponent));
    // Caja negra - valor límite exterior: distancia 3 (más que un salto)
    assertFalse(validator.isValidMove(from, new Position(1, 4), opponent));

    // Mock: con distancias inválidas ni siquiera se consulta el tablero
    verify(boardMock, never()).isBlocked(anyInt(), anyInt(), anyInt(), anyInt());
  }

  @Test
  void testIsValidMove_CannotMoveOntoOpponent() {
    // Caja negra - partición inválida: el destino es la casilla del rival
    Position from = new Position(4, 4);
    Position to = new Position(3, 4);
    Position opponent = new Position(3, 4);

    assertFalse(validator.isValidMove(from, to, opponent));
    // Mock: se rechaza sin consultar el tablero
    verify(boardMock, never()).isBlocked(anyInt(), anyInt(), anyInt(), anyInt());
  }

  @Test
  void testIsValidMove_OpponentAdjacentButNotOnDestination() {
    Position from = new Position(4, 4);
    Position to = new Position(3, 4);

    // Caja negra - partición válida: sin rival (null)
    assertTrue(validator.isValidMove(from, to, null));
    // Caja negra - partición válida: rival en otra fila y otra columna
    assertTrue(validator.isValidMove(from, to, new Position(0, 0)));
    // Caja negra - valor límite: rival en la misma fila que el destino, columna de al lado
    assertTrue(validator.isValidMove(from, to, new Position(3, 5)));
  }

  // ---------- Salto recto (2 casillas por encima del rival) ----------

  @Test
  void testIsValidMove_ValidJump() {
    // Caja negra - partición válida: salto recto con el rival en medio y sin muros
    Position from = new Position(6, 4);
    Position opponent = new Position(5, 4);
    Position to = new Position(4, 4);

    // Mock: ningún tramo del salto tiene muro
    when(boardMock.isBlocked(6, 4, 5, 4)).thenReturn(false);
    when(boardMock.isBlocked(5, 4, 4, 4)).thenReturn(false);

    assertTrue(validator.isValidMove(from, to, opponent));
    // Mock: se consultan los dos tramos del salto
    verify(boardMock).isBlocked(6, 4, 5, 4);
    verify(boardMock).isBlocked(5, 4, 4, 4);
  }

  @Test
  void testIsValidMove_JumpBlockedByWall() {
    // Caja negra - partición inválida: muro DETRÁS del rival (segundo tramo)
    Position from = new Position(6, 4);
    Position opponent = new Position(5, 4);
    Position to = new Position(4, 4);

    // Mock: el segundo tramo tiene muro
    when(boardMock.isBlocked(6, 4, 5, 4)).thenReturn(false);
    when(boardMock.isBlocked(5, 4, 4, 4)).thenReturn(true);

    assertFalse(validator.isValidMove(from, to, opponent));
    verify(boardMock).isBlocked(6, 4, 5, 4);
    verify(boardMock).isBlocked(5, 4, 4, 4);
  }

  // Data-driven: el salto se prueba en las 4 direcciones con los mismos asserts
  // Caja negra - partición válida: salto hacia arriba, abajo, izquierda y derecha
  @ParameterizedTest(name = "salto de ({0},{1}) sobre ({2},{3}) a ({4},{5})")
  @CsvSource({
      "6,4,5,4,4,4",   // up
      "2,4,3,4,4,4",   // down
      "4,6,4,5,4,4",   // left
      "4,2,4,3,4,4"    // right
  })
  void testIsValidMove_JumpAllDirections(int fy, int fx, int oy, int ox, int ty, int tx) {
    // Mock sin when(...): por defecto isBlocked devuelve false (no hay muros)
    assertTrue(validator.isValidMove(new Position(fy, fx), new Position(ty, tx),
        new Position(oy, ox)));
    verify(boardMock).isBlocked(fy, fx, oy, ox);
    verify(boardMock).isBlocked(oy, ox, ty, tx);
  }

  @Test
  void testIsValidMove_JumpBlockedBeforeOpponent() {
    // Caja negra - partición inválida: muro ENTRE los dos jugadores (primer tramo)
    when(boardMock.isBlocked(6, 4, 5, 4)).thenReturn(true);
    when(boardMock.isBlocked(5, 4, 4, 4)).thenReturn(false);

    assertFalse(validator.isValidMove(new Position(6, 4), new Position(4, 4), new Position(5, 4)));
  }

  @Test
  void testIsValidMove_JumpOpponentNotInTheMiddle() {
    Position from = new Position(6, 4);
    Position to = new Position(4, 4);

    // Caja negra - partición inválida: el rival no está en la casilla del medio
    // (fila distinta de la del medio)
    assertFalse(validator.isValidMove(from, to, new Position(4, 5)));
    // Caja negra - valor límite: misma fila que el medio, pero columna de al lado
    assertFalse(validator.isValidMove(from, to, new Position(5, 3)));

    // Mock: sin rival en medio no se consulta el tablero
    verify(boardMock, never()).isBlocked(anyInt(), anyInt(), anyInt(), anyInt());
  }

  // ---------- Precondiciones ----------

  @Test
  void testIsValidMove_NullPositions() {
    Position p = new Position(4, 4);
    // Caja negra - partición inválida: origen nulo (precondición)
    assertThrows(IllegalArgumentException.class, () -> validator.isValidMove(null, p, null));
    // Caja negra - partición inválida: destino nulo (precondición)
    assertThrows(IllegalArgumentException.class, () -> validator.isValidMove(p, null, null));
  }

  @Test
  void testConstructorNullBoard() {
    // Caja negra - partición inválida: tablero nulo en el constructor (precondición)
    assertThrows(IllegalArgumentException.class, () -> new MoveValidation(null));
  }

  // ---------- Integración con el Board real (sin mock) ----------

  @Test
  void testIsValidMove_IntegrationWithRealBoard() {
    // Prueba de integración: MoveValidation con un Board real, para comprobar que
    // las dos clases colaboran bien (complementa los tests con el mock)
    Board real = new Board();
    real.addWall(new Wall(4, 4, Orientation.HORIZONTAL)); // cubre columnas 4 y 5, entre filas 4 y 5
    MoveValidation v = new MoveValidation(real);

    // Partición inválida: el muro impide subir de (5,4) a (4,4)
    assertFalse(v.isValidMove(new Position(5, 4), new Position(4, 4)));
    // Partición válida: en la columna 3 no hay muro
    assertTrue(v.isValidMove(new Position(5, 3), new Position(4, 3)));
  }
}