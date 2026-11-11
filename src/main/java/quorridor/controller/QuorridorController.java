package quorridor.controller;

import java.util.ArrayList;
import java.util.List;
import quorridor.model.Board;
import quorridor.model.Player;

public class QuorridorController {
  private Board board;
  private List<Player> players;
  private int currentPlayerIndex;

  public QuorridorController() {
    this.board = new Board();
    this.players = new ArrayList<>();

    // SetUp two players with their initial positions.
    players.add(new Player("Player 1", 1, 10, 8, 4, 0));
    players.add(new Player("Player 2", 2, 10, 0, 4, 8));

    this.currentPlayerIndex = 0;
  }

  public Board getBoard() {
    return board;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public Player getCurrentPlayer() {
    if (players.isEmpty()) {
      return null;
    }
    return players.get(currentPlayerIndex);
  }

  /**
   * Intenta mover al jugador actual a la posición de destino.
   * Valida el movimiento utilizando MoveValidation y cambia el turno si es válido.
   */
  public boolean moveCurrentPlayer(quorridor.model.Position to) {
    if (players.isEmpty() || to == null) {
      return false;
    }

    quorridor.model.Player current = getCurrentPlayer();
    quorridor.model.Player opponent = getOpponentPlayer();

    // Instanciamos el validador con el tablero actual
    quorridor.model.MoveValidation validator = new quorridor.model.MoveValidation(board);

    quorridor.model.Position currentPos = new quorridor.model.Position(current.getPositionY(), current.getPositionX());
    quorridor.model.Position opponentPos = opponent != null ? new quorridor.model.Position(opponent.getPositionY(), opponent.getPositionX()) : null;

    // Verificamos si el movimiento es válido (incluyendo saltos si el oponente está en medio)
    boolean isValid = validator.isValidMove(currentPos, to, opponentPos);

    if (isValid) {
      // Actualizamos la posición del jugador
      current.setPosition(to.getY(), to.getX());

      // Cambiamos el turno al siguiente jugador
      currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
      return true;
    }

    return false;
  }

  // Método auxiliar para obtener al oponente del jugador actual
  private quorridor.model.Player getOpponentPlayer() {
    if (players.size() < 2) {
      return null;
    }
    int opponentIndex = (currentPlayerIndex + 1) % players.size();
    return players.get(opponentIndex);
  }
}