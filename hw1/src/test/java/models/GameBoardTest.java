package models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameBoardTest {
  private static GameBoard gameBoard;

  /** Tests the initialization of GameBoard. */
  @Test
  @Order(1)
  public void initTest() {
    gameBoard = new GameBoard();
    Assertions.assertEquals(1, gameBoard.getP1().getId());
    Assertions.assertFalse(gameBoard.isGameStarted());
    Assertions.assertEquals(1, gameBoard.getTurn());
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        Assertions.assertEquals(0, gameBoard.getBoardState()[i][j]);
      }
    }
    Assertions.assertEquals(0, gameBoard.getWinner());
    Assertions.assertFalse(gameBoard.isDraw());
  }

  /** Tests that makeMove fails if either player is uninitialized. */
  @Test
  @Order(2)
  public void makeMoveTestInvalidUnitialized() {
    gameBoard.setP2(new Player());
    Message msg = gameBoard.makeMove(1, 0, 0);
    Assertions.assertEquals(400, msg.getCode());
    Assertions.assertEquals("Game not ready", msg.getMessage());
  }

  /** Tests that makeMove fails if the move is out of bounds. */
  @Test
  @Order(3)
  public void makeMoveTestInvalidOutOfBounds() {
    gameBoard.getP1().setType('X');
    gameBoard.getP2().setType('O');
    Message msg = gameBoard.makeMove(1, 5, 2);
    Assertions.assertEquals(400, msg.getCode());
    Assertions.assertEquals("Invalid coordinates", msg.getMessage());
  }

  /** Tests that makeMove fails if the player plays not in their turn. */
  @Test
  @Order(4)
  public void makeMoveTestInvalidNotTurn() {
    Message msg = gameBoard.makeMove(2, 1, 2);
    Assertions.assertEquals(400, msg.getCode());
    Assertions.assertEquals("Not your turn", msg.getMessage());
  }

  /** Tests that makeMove works. */
  @Test
  @Order(5)
  public void makeMoveTestValid() {
    Message msg = gameBoard.makeMove(1, 1, 2);
    Assertions.assertEquals(100, msg.getCode());
    Assertions.assertEquals('X', gameBoard.getBoardState()[1][2]);
    Assertions.assertEquals(2, gameBoard.getTurn());
  }

  /** Tests that makeMove fails if the place is already taken. */
  @Test
  @Order(6)
  public void makeMoveTestInvalidDuplicate() {
    Message msg = gameBoard.makeMove(2, 1, 2);
    Assertions.assertEquals(400, msg.getCode());
    Assertions.assertEquals("Place already taken", msg.getMessage());
  }

  /** Tests that makeMove fails if the player ID is invalid. */
  @Test
  @Order(7)
  public void makeMoveTestInvalidPlayer() {
    Message msg = gameBoard.makeMove(5, 1, 2);
    Assertions.assertEquals(400, msg.getCode());
    Assertions.assertEquals("Invalid player ID", msg.getMessage());
  }

  /** Tests that updateWinner works for a row. */
  @Test
  @Order(8)
  public void updateWinnerTestHorizontal() {
    gameBoard.setBoardState(
        new char[][] {
          {'O', 0, 0},
          {'X', 'X', 'X'},
          {'O', 0, 0}
        });
    gameBoard.updateWinner();
    Assertions.assertEquals(1, gameBoard.getWinner());
  }

  /** Tests that updateWinner works for a row. */
  @Test
  @Order(9)
  public void updateWinnerTestVertical() {
    gameBoard.setBoardState(
        new char[][] {
          {0, 'O', 0},
          {'X', 'O', 'X'},
          {'X', 'O', 0}
        });
    gameBoard.updateWinner();
    Assertions.assertEquals(2, gameBoard.getWinner());
  }

  /** Tests that updateWinner works for the diagonal. */
  @Test
  @Order(10)
  public void updateWinnerTestDiag() {
    gameBoard.setBoardState(
        new char[][] {
          {'O', 0, 0},
          {'X', 'O', 'X'},
          {'X', 'O', 'O'}
        });
    gameBoard.updateWinner();
    Assertions.assertEquals(2, gameBoard.getWinner());
  }

  /** Tests that updateWinner works for the antidiagonal. */
  @Test
  @Order(11)
  public void updateWinnerTestAntiDiag() {
    gameBoard.setBoardState(
        new char[][] {
          {'O', 0, 0},
          {'X', 'O', 'X'},
          {'X', 'O', 'O'}
        });
    gameBoard.updateWinner();
    Assertions.assertEquals(2, gameBoard.getWinner());
  }
}
