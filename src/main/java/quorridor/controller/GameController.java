package quorridor.controller;

import quorridor.model.Board;
import quorridor.model.MoveValidation;
import quorridor.model.Player;
import quorridor.view.GameView;

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
  public boolean movePawn(int toY, int toX) {
    return false; // TODO: implementar con TDD
  }
}