package models;

import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Persister {
  private final Connection connection;

  public Persister() throws SQLException {
    connection = DriverManager.getConnection("jdbc:sqlite:hw1.db");
  }

  /**
   * This method restores persisted GameBoard.
   *
   * @return the persisted GameBoard. Null if none is present.
   */
  public GameBoard restore() {
    try (Statement stmt = connection.createStatement()) {
      stmt.execute("CREATE TABLE IF NOT EXISTS dat ("
              + "k VARCHAR PRIMARY KEY,"
              + "content TEXT"
              + ")");
      try (ResultSet res = stmt.executeQuery("SELECT content FROM dat WHERE k = 'gameboard'")) {
        if (res.next()) {
          String str = res.getString(1);
          Gson gson = new Gson();
          return gson.fromJson(str, GameBoard.class);
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  /**
   * This method saves the GameBoard to disk.
   *
   * @param gameBoard the GameBoard to be saved.
   */
  public void save(GameBoard gameBoard) {
    String sql = "REPLACE INTO dat (k, content) values ('gameboard', ?)";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      Gson gson = new Gson();
      String str = gson.toJson(gameBoard);
      stmt.setString(1, str);
      stmt.execute();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
}
