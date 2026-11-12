package quorridor.controller;

import java.util.ArrayList;
import java.util.List;
import quorridor.model.Board;
import quorridor.model.Player;
import quorridor.view.GameView;

/**
 * Mock object (hecho a mano) de la Vista.
 * No dibuja nada: solo guarda lo que el controlador le pide mostrar,
 * para que los tests puedan comprobarlo.
 */
public class MockGameView implements GameView {

  // Lo que el controlador le ha pedido mostrar
  public List<String> messages = new ArrayList<>();
  public Player lastTurn = null;
  public Player winner = null;
  public int boardShownCount = 0;

  @Override
  public void showBoard(Board board, Player player1, Player player2) {
    boardShownCount++;
  }

  @Override
  public void showTurn(Player player) {
    lastTurn = player;
  }

  @Override
  public void showMessage(String message) {
    messages.add(message);
  }

  @Override
  public void showWinner(Player player) {
    winner = player;
  }
}