package models;

public class GameBoard {

  private Player p1;

  private Player p2;

  private boolean gameStarted;

  private int turn;

  private char[][] boardState;

  private int winner;

  private boolean isDraw;

  /**
   * Default constructor for GameBoard.
   */
  public GameBoard() {
    p1 = new Player();
    p1.setId(1);

    gameStarted = false;
    turn = 1;
    boardState = new char[][] {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
    winner = 0;
    isDraw = false;
  }

  /**
   * This method is called when a player wants to make a move.
   *
   * @param playerId the ID of the player who makes the move.
   * @param x the x coordinate of the move
   * @param y the y coordinate of the move
   * @return the Message to return to the client
   */
  public Message makeMove(int playerId, int x, int y) {
    Message ret = new Message();

    // Check if any player is uninitialized.
    if (p1.getType() == 0 || p2.getType() == 0) {
      ret.setCode(400);
      ret.setMoveValidity(false);
      ret.setMessage("Game not ready");
      return ret;
    }

    // Get player Info.
    char type;
    if (playerId == 1) {
      type = p1.getType();
    } else if (playerId == 2) {
      type = p2.getType();
    } else {
      ret.setCode(400);
      ret.setMoveValidity(false);
      ret.setMessage("Invalid player ID");
      return ret;
    }

    // Check if it's the player's turn.
    if (turn != playerId) {
      ret.setCode(400);
      ret.setMoveValidity(false);
      ret.setMessage("Not your turn");
      return ret;
    }

    if (x < 0 || x > 2 || y < 0 || y > 2) {
      ret.setCode(400);
      ret.setMoveValidity(false);
      ret.setMessage("Invalid coordinates");
      return ret;
    }

    if (boardState[x][y] != 0) {
      ret.setCode(400);
      ret.setMoveValidity(false);
      ret.setMessage("Place already taken");
      return ret;
    }

    boardState[x][y] = type;
    if (playerId == 1) {
      turn = 2;
    } else {
      turn = 1;
    }

    ret.setCode(100);
    ret.setMoveValidity(true);
    ret.setMessage("");
    return ret;
  }

  /**
   * Updates the winner field if necessary. Checks the winning conditions.
   */
  public void updateWinner() {
    // Check all rows.
    for (int i = 0; i < 3; i++) {
      int sum = 0;
      for (int j = 0; j < 3; j++) {
        if (boardState[i][j] == p1.getType()) {
          sum++;
        } else if (boardState[i][j] == p2.getType()) {
          sum--;
        }
      }

      if (sum == 3) {
        winner = 1;
        return;
      } else if (sum == -3) {
        winner = 2;
        return;
      }
    }


    // Check all columns.
    for (int j = 0; j < 3; j++) {
      int sum = 0;
      for (int i = 0; i < 3; i++) {
        if (boardState[i][j] == p1.getType()) {
          sum++;
        } else if (boardState[i][j] == p2.getType()) {
          sum--;
        }
      }

      if (sum == 3) {
        winner = 1;
        return;
      } else if (sum == -3) {
        winner = 2;
        return;
      }
    }


    int sum = 0;
    // Check the diagonal.
    for (int i = 0; i < 3; i++) {
      if (boardState[i][i] == p1.getType()) {
        sum++;
      } else if (boardState[i][i] == p2.getType()) {
        sum--;
      }
    }

    if (sum == 3) {
      winner = 1;
      return;
    } else if (sum == -3) {
      winner = 2;
      return;
    }

    sum = 0;
    // Check the antidiagonal.
    for (int i = 0; i < 3; i++) {
      if (boardState[i][2 - i] == p1.getType()) {
        sum++;
      } else if (boardState[i][2 - i] == p2.getType()) {
        sum--;
      }
    }

    if (sum == 3) {
      winner = 1;
      return;
    } else if (sum == -3) {
      winner = 2;
      return;
    }

    sum = 0;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (boardState[i][j] != 0) {
          sum++;
        }
      }
    }

    if (sum == 9) {
      isDraw = true;
    }
  }

  public Player getP1() {
    return p1;
  }

  public void setP1(Player p1) {
    this.p1 = p1;
  }

  public Player getP2() {
    return p2;
  }

  public void setP2(Player p2) {
    this.p2 = p2;
  }

  public boolean isGameStarted() {
    return gameStarted;
  }

  public void setGameStarted(boolean gameStarted) {
    this.gameStarted = gameStarted;
  }

  public int getTurn() {
    return turn;
  }

  public void setTurn(int turn) {
    this.turn = turn;
  }

  /**
   * This method makes a deep copy of boardState and returns it.
   *
   * @return a deep copy of boardState
   */
  public char[][] getBoardState() {
    char[][] ret = new char[][] {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
    for (int i = 0; i < 3; i++) {
      System.arraycopy(boardState[i], 0, ret[i], 0, 3);
    }
    return ret;
  }

  /**
   * This methods make a deep copy of the argument and store it.
   *
   * @param boardState a 2d array representing the board's state
   */
  public void setBoardState(char[][] boardState) {
    this.boardState = new char[][] {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
    for (int i = 0; i < 3; i++) {
      System.arraycopy(boardState[i], 0, this.boardState[i], 0, 3);
    }
  }

  public int getWinner() {
    return winner;
  }

  public void setWinner(int winner) {
    this.winner = winner;
  }

  public boolean isDraw() {
    return isDraw;
  }

  public void setDraw(boolean draw) {
    isDraw = draw;
  }
}
