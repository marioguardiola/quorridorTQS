# Quorridor - Testing&Quality (2026-2027)

##  Authors
Irene López - 1661436
Mario Guardiola Jiménez - 1705769

## Supervisor
Olivier Penacchio 

## Tools
* Language: Java
* Extension: Test Runner for Java (JUnit)
* Testing: JUnit
* Version Control: GitHub : https://github.com/marioguardiola/quorridorTQS.git

## Architecture
MVC - Model View Controller
* Model: Quorridor system (Board, Players, Wall, Pawn, Validation rules). 
* Controller: Quorridor logic between user actions and environment and update board. 
* View: User Interface

## Work
Each function has its test. Commit every change or update. 

## Quorridor mechanics
Quoridor is an abstract strategy game for 2 or 4 players where the goal is to be the first to get your pawn to any square on the opposite end of the board.

* Main components:
* Board: A 9x9 grid.
* Pawns: One per player. In a 2-player game, each starts on the center square of their baseline.
* Walls: Barriers placed in the grooves between squares. In a 2-player game, each player starts with an inventory of 10 walls.

* Turn mechanics:
On your turn, you must take exactly one of these two actions:
1- Move your pawn: Advance one square orthogonally (up, down, left, or right). Diagonals are not allowed, and you cannot move through walls. If two pawns are adjacent and facing each other, you can jump over your opponent.

2- Place a wall: Place a barrier covering exactly the length of two squares. It cannot overlap existing walls and remains in place for the rest of the game.

* Rules
You can never completely trap an opponent. Whenever you place a wall, you must leave at least one open path (no matter how long or complex) for every pawn to reach its target line.
---
