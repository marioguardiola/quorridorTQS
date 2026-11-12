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

  // Moves the current player's pawn to (toY, toX). Returns true if the move was done
  // v1: validates the move, moves the pawn and changes the turn
  public boolean movePawn(int toY, int toX) {
    Player opponent = (currentPlayer == player1) ? player2 : player1;

    Position from = currentPlayer.getPosition();
    Position to = new Position(toY, toX);
    Position opponentPos = opponent.getPosition();

    // Invalid move: nothing changes and the View shows an error
    if (!moveValidation.isValidMove(from, to, opponentPos)) {
      view.showMessage("Movimiento no válido");
      return false;
    }

    // Valid move: move the pawn, change the turn and tell the View
    currentPlayer.setPosition(toY, toX);
    currentPlayer = opponent;
    view.showTurn(currentPlayer);
    return true;
  }
}