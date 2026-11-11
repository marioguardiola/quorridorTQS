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
}