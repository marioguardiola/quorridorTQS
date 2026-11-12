package quorridor.controller;

import java.util.List;
import quorridor.model.Board;
import quorridor.model.Player;
import quorridor.model.Wall;

/**
 * Mock object (hecho a mano) de Board.
 * No comprueba solapamientos ni caminos: acepta o rechaza el muro
 * según lo que el test le indique con setAccept(...).
 */
public class MockBoard extends Board {

  private boolean accept = true;  // ¿Aceptará el próximo muro?
  public int addWallCount = 0;    // Cuántas veces el controlador ha intentado añadir un muro

  // El test decide si el tablero aceptará o rechazará el muro
  public void setAccept(boolean accept) {
    this.accept = accept;
  }

  @Override
  public void addWall(Wall wall, List<Player> players) {
    addWallCount++;
    if (!accept) {
      // Simula lo que hace el Board real cuando un muro no se puede colocar
      throw new IllegalArgumentException("Mock: wall rejected");
    }
  }
}