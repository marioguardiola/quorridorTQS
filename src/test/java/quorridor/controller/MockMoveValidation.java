package quorridor.controller;

import quorridor.model.Board;
import quorridor.model.MoveValidation;
import quorridor.model.Position;

/**
 * Mock object (hecho a mano) de MoveValidation.
 * No aplica las reglas del juego: responde "válido" o "no válido"
 * según lo que el test le indique con setValid(...).
 */
public class MockMoveValidation extends MoveValidation {

  private boolean valid = true;   // Respuesta que devolverá isValidMove
  public int callCount = 0;       // Cuántas veces lo ha consultado el controlador

  public MockMoveValidation() {
    super(new Board()); // MoveValidation exige un tablero no nulo (precondición)
  }

  // El test decide qué responderá el mock
  public void setValid(boolean valid) {
    this.valid = valid;
  }

  @Override
  public boolean isValidMove(Position from, Position to, Position opponentPos) {
    callCount++;
    return valid;
  }
}