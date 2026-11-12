package quorridor.controller;

import java.util.List;

import quorridor.model.Wall;
import quorridor.model.Board;
import quorridor.model.MoveValidation;
import quorridor.model.Player;
import quorridor.view.GameView;
import quorridor.model.Position;
import quorridor.model.Orientation;

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

  // Places a wall for the current player at (y, x) with the given orientation.
  // Returns true if the wall was placed
  // v2: before touching the board, checks that the game is not over, that the
  //     player has walls left and that the anchor is valid
  public boolean placeWall(int y, int x, Orientation orientation) {
    // The game is already over: no more walls are allowed
    if (isGameOver()) {
      view.showMessage("La partida ya ha terminado");
      return false;
    }

    // The player has no walls left (checked BEFORE placing the wall on the board)
    if (!currentPlayer.hasWalls()) {
      view.showMessage("No te quedan muros");
      return false;
    }

    // Invalid anchor (outside [0, 7]): show an error instead of crashing
    if (!Wall.isValidAnchor(y, x)) {
      view.showMessage("Posición del muro fuera de rango (0-7)");
      return false;
    }

    Wall wall = new Wall(y, x, orientation);

    try {
      // The board checks overlaps, trapped players and the maximum number of walls
      board.addWall(wall, List.of(player1, player2));
    } catch (IllegalArgumentException | IllegalStateException e) {
      // The board rejected the wall: nothing changes and the View shows an error
      view.showMessage("No se puede colocar el muro: " + e.getMessage());
      return false;
    }

    // Wall placed: the player uses one wall, the turn changes and the View is told
    currentPlayer.useWall();
    currentPlayer = (currentPlayer == player1) ? player2 : player1;
    view.showTurn(currentPlayer);
    return true;
  }
}