package models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersisterTest {
  private static Persister persister;

  /** Clear database file. */
  @BeforeAll
  public static void init() {
    try {
      Files.deleteIfExists(Paths.get("hw1.db"));
    } catch (IOException e) {
      e.printStackTrace();
      Assertions.fail();
    }
  }

  /** Tests the initialization of Persister. */
  @Test
  @Order(1)
  public void initTest() {
    try {
      persister = new Persister();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      Assertions.fail();
    }
  }

  /** Tests fresh run with no persisted data. */
  @Test
  @Order(2)
  public void freshTest() {
    GameBoard res = persister.restore();
    Assertions.assertNull(res);
  }

  /** Tests storing data. */
  @Test
  @Order(3)
  public void saveTest() {
    GameBoard gameBoard = new GameBoard();
    gameBoard.setGameStarted(true);
    persister.save(gameBoard);
  }

  /** Tests initialization after a crash. */
  @Test
  @Order(4)
  public void reinitTest() {
    try {
      persister = new Persister();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      Assertions.fail();
    }
  }

  /** Tests that Persister correctly restores data. */
  @Test
  @Order(5)
  public void restoreTest() {
    GameBoard gameBoard = persister.restore();
    Assertions.assertTrue(gameBoard.isGameStarted());
  }

}
