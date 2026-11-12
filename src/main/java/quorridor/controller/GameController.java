package quorridor.controller;

import quorridor.model.Board;
import quorridor.model.MoveValidation;
import quorridor.model.Player;
import quorridor.view.GameView;
import quorridor.model.Position;

/**
 * Controlador (MVC): recibe las acciones del usuario, las aplica al Modelo
 * y le dice a la Vista qué mostrar.
 * Todos los objetos se reciben por el constructor (Inversion of Control),
 * para poder sustituirlos por mocks en los tests.
 */
public class GameController {
  private Board board;
  private Player player1;
  private Player player2;
  private MoveValidation moveValidation;
  private GameView view;
  private Player currentPlayer;

  public GameController(Board board, Player player1, Player player2,
                        MoveValidation moveValidation, GameView view) {
    this.board = board;
    this.player1 = player1;
    this.player2 = player2;
    this.moveValidation = moveValidation;
    this.view = view;
    this.currentPlayer = player1; // Empieza el jugador 1
  }

  // Getters
  public Player getCurrentPlayer() { return currentPlayer; }

  // Returns true if one of the players has reached their goal row
  public boolean isGameOver() {
    return player1.hasReachedGoal() || player2.hasReachedGoal();
  }

  // Moves the current player's pawn to (toY, toX). Returns true if the move was done
  // v2: also checks that the game is not over and that the target cell is inside
  //     the board, and announces the winner
  public boolean movePawn(int toY, int toX) {
    // The game is already over: no more moves are allowed
    if (isGameOver()) {
      view.showMessage("La partida ya ha terminado");
      return false;
    }

    // Target cell out of the board: show an error instead of crashing
    if (toY < 0 || toY >= Board.SIZE || toX < 0 || toX >= Board.SIZE) {
      view.showMessage("Casilla fuera del tablero");
      return false;
    }

    Player opponent = (currentPlayer == player1) ? player2 : player1;

    Position from = currentPlayer.getPosition();
    Position to = new Position(toY, toX);
    Position opponentPos = opponent.getPosition();

    // Invalid move: nothing changes and the View shows an error
    if (!moveValidation.isValidMove(from, to, opponentPos)) {
      view.showMessage("Movimiento no válido");
      return false;
    }

    currentPlayer.setPosition(toY, toX);

    // The player has reached their goal row: announce the winner
    if (currentPlayer.hasReachedGoal()) {
      view.showWinner(currentPlayer);
      return true;
    }

    // Normal move: change the turn and tell the View
    currentPlayer = opponent;
    view.showTurn(currentPlayer);
    return true;
  }
}