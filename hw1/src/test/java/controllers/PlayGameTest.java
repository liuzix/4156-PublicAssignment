package controllers;

import com.google.gson.Gson;
import kong.unirest.Unirest;
import models.GameBoard;
import models.Message;
import models.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlayGameTest {

  /** Runs only once before the testing starts. */
  @BeforeAll
  public static void init() {
    // Start Server
    PlayGame.main(null);
    System.out.println("Server started");
  }

  /** Tests that 404 is returned by startgame when there's no game. */
  @Test
  @Order(1)
  public void startGameTestInvalidNoGame() {
    var res = Unirest.post("http://localhost:8080/startgame?type=X").asString();
    Assertions.assertEquals(404, res.getStatus());
  }

  /** Tests that 400 is returned by joingame when there's no game. */
  @Test
  @Order(1)
  public void joinGameTestInvalidNoGame() {
    var res = Unirest.get("http://localhost:8080/joingame").asString();
    Assertions.assertEquals(400, res.getStatus());
  }

  /** Tests that 404 is returned by move when there's no game. */
  @Test
  @Order(1)
  public void moveTestInvalidNoGame() {
    var res = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    Assertions.assertEquals(404, res.getStatus());
  }

  /** This is a test case to evaluate the newgame endpoint. */
  @Test
  @Order(2)
  public void newGameTest() {
    var res = Unirest.get("http://localhost:8080/newgame").asEmpty();
    Assertions.assertEquals(200, res.getStatus());
  }

  /** Tests that 400 is returned by startgame when type is too long. */
  @Test
  @Order(3)
  public void startGameTestInvalidTypeTooLong() {
    var res = Unirest.post("http://localhost:8080/startgame?type=XX").asString();
    Assertions.assertEquals(400, res.getStatus());
  }

  /** Tests that 400 is returned by startgame when type is an illegal char. */
  @Test
  @Order(4)
  public void startGameTestInvalidTypeIllegalChar() {
    var res = Unirest.post("http://localhost:8080/startgame?type=F").asString();
    Assertions.assertEquals(400, res.getStatus());
  }

  /** Tests that 400 is returned by joingame when there's no player 1. */
  @Test
  @Order(4)
  public void joinGameTestInvalidNoPlayer() {
    var res = Unirest.get("http://localhost:8080/joingame").asString();
    Assertions.assertEquals(400, res.getStatus());
  }

  /** This is a test case to evaluate the startgame endpoint. */
  @Test
  @Order(5)
  public void startGameTestValid() {
    var res = Unirest.post("http://localhost:8080/startgame?type=X").asString();
    Assertions.assertEquals(200, res.getStatus());

    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(res.getBody(), GameBoard.class);
    Player player1 = gameBoard.getP1();
    Assertions.assertEquals('X', player1.getType());
  }

  /** Tests that joingame is successful. */
  @Test
  @Order(6)
  public void joinGameTestValid() {
    var res = Unirest.get("http://localhost:8080/joingame").asString();
    Assertions.assertEquals(200, res.getStatus());
  }

  /** Tests that 409 is returned by joingame if requests are duplicated. */
  @Test
  @Order(7)
  public void joinGameTestInvalidDuplicate() {
    var res = Unirest.get("http://localhost:8080/joingame").asString();
    Assertions.assertEquals(409, res.getStatus());
  }

  /** Tests that 404 is returned by move if params are bd. */
  @Test
  @Order(8)
  public void moveTestInvalidBadParams() {
    var res = Unirest.post("http://localhost:8080/move/1").body("fdsa=0&ys=0").asString();
    Assertions.assertEquals(400, res.getStatus());
  }

  /** Tests that 404 is returned by move if params are bd. */
  @Test
  @Order(9)
  public void moveTestValid() {
    var res = Unirest.post("http://localhost:8080/move/1").body("x=1&y=0").asString();
    Assertions.assertEquals(200, res.getStatus());

    Gson gson = new Gson();
    Message msg = gson.fromJson(res.getBody(), Message.class);
    Assertions.assertEquals(100, msg.getCode());
  }

  /** This method runs only once after all the test cases have been executed. */
  @AfterAll
  public static void close() {
    // Stop Server
    PlayGame.stop();
  }
}
