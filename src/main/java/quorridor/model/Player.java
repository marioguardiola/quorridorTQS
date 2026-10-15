package quorridor.model;

/**
 * Pawn: name, wall (10), position (x,y), goalY
 * Remember: read methods don't need post-condition
 */
public class Player {
  private String name;
  private int idUser;
  private int walls;
  private int positionY;
  private int positionX;
  private int goalY;

  // Prepare correctly the setUp
  boolean invariant(){
    //Walls [0-10], posY posX [0-8], goalY 0 o 8,
    return (walls <= 10 && walls >= 0 &&
            positionX >= 0 && positionX <= 8 &&
            positionY >= 0 && positionY <= 8 &&
            (goalY == 0 || goalY == 8) );
  }

  public Player(String name, int idUser, int walls, int positionY, int positionX, int goalY) {
    // Pre-condition
    if (name == null || name.trim().isEmpty()) {throw new IllegalArgumentException("Name can not be null");}
    if (walls < 0 || walls > 10) {throw new IllegalArgumentException("Walls must be between 0 and 10");}
    if (positionX < 0 || positionX > 8 || positionY < 0 || positionY > 8) {
      throw new IllegalArgumentException("Position out of board");}
    if (goalY != 0 && goalY != 8) {throw new IllegalArgumentException("Goal must be 0 or 8");}
    if (positionY == 0 && goalY != 8) {throw new IllegalArgumentException("Goal wrong established");}
    if (positionY == 8 && goalY != 0) {throw new IllegalArgumentException("Goal wrong established");}

    // Logic
    this.name = name;
    this.idUser = idUser;
    this.walls = walls;
    this.positionY = positionY;
    this.positionX = positionX;
    this.goalY = goalY;

    assert invariant();

  }

  // Getters
  public String getName() { return this.name; }
  public int getWalls() { return this.walls; }
  public int getPositionY() { return this.positionY; }
  public int getPositionX() { return this.positionX; }
  public int getGoalY() { return this.goalY; } // CORREGIDO

  // Methods
  public void setPosition(int positionY, int positionX) {
    // Assert invariant
    assert invariant();

    // Pre-conditions
    if (positionX < 0 || positionX > 8 || positionY < 0 || positionY > 8) {
      throw new IllegalArgumentException("Position out of board");
    }

    // Logic
    this.positionX = positionX;
    this.positionY = positionY;

    assert invariant();

  }

  public void useWall() {
    // Assert invariant
    assert invariant();

    // Pre-condition
    if (!hasWalls()) {throw new IllegalStateException("El jugador no tiene muros disponibles");}

    // Logic
    int wallsBefore = walls;
    this.walls--;

    // Post-condition
    assert(wallsBefore-1 == this.walls);

    assert invariant();

  }

  public boolean hasWalls() {
    return (this.walls > 0);
  }

  public boolean hasReachedGoal() {
    return(this.positionY == this.goalY);
  }

}