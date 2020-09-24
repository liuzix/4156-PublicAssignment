package controllers;

import com.google.gson.Gson;
import io.javalin.Javalin;
import java.io.IOException;
import java.util.Queue;

import models.GameBoard;
import models.Message;
import models.Player;
import org.eclipse.jetty.websocket.api.Session;

class PlayGame {
  private static GameBoard gameBoard;

  private static final int PORT_NUMBER = 8080;

  private static Javalin app;

  /** Main method of the application.
   * @param args Command line arguments
   */
  public static void main(final String[] args) {

    app = Javalin.create(config -> {
      config.addStaticFiles("/public");
    }).start(PORT_NUMBER);

    // Test Echo Server
    app.post("/echo", ctx -> {
      ctx.result(ctx.body());
    });

    app.get("/newgame", ctx -> {
      gameBoard = new GameBoard();
      ctx.redirect("/tictactoe.html");
    });

    app.post("/startgame", ctx -> {
      try {
        String type = ctx.req.getParameter("type");

        // We only accept X or O as "type".
        if (type.length() != 1) {
          ctx.status(400);
        }
        if (type.charAt(0) != 'X' && type.charAt(0) != 'O') {
          ctx.status(400);
        }
        if (gameBoard == null) {
          ctx.status(404);
        }

        gameBoard.getP1().setType(type.charAt(0));
        gameBoard.setGameStarted(true);

        Gson gson = new Gson();
        String jsonStr = gson.toJson(gameBoard);
        ctx.result(jsonStr);
      } catch (Exception e) {
        ctx.status(400);
        System.out.print(e.getMessage());
      }
    });

    app.get("/joingame", ctx -> {
      if (gameBoard == null) {
        ctx.status(404);
        return;
      }

      // If player 1 has not chosen a "type", then no one can join the game.
      if (gameBoard.getP1().getType() == 0) {
        ctx.status(400);
        return;
      }

      if (gameBoard.getP2() != null) {
        // Someone has already joined the game.
        ctx.status(409);
        return;
      }

      Player p2 = new Player();
      p2.setId(2);

      if (gameBoard.getP1().getType() == 'X') {
        p2.setType('O');
      } else {
        p2.setType('X');
      }

      gameBoard.setP2(p2);
      Gson gson = new Gson();
      String jsonStr = gson.toJson(gameBoard);
      sendGameBoardToAllPlayers(jsonStr);

      ctx.redirect("/tictactoe.html?p=2");
    });

    app.post("/move/:playerId", ctx -> {
      if (gameBoard == null) {
        ctx.status(404);
        return;
      }

      try {
        int playerId = ctx.pathParam("playerId", Integer.class).get();
        int x = ctx.formParam("x", Integer.class).get();
        int y = ctx.formParam("y", Integer.class).get();

        Message msg = gameBoard.makeMove(playerId, x, y);

        Gson gson = new Gson();
        String msgJsonStr = gson.toJson(msg);
        ctx.result(msgJsonStr);

        gameBoard.updateWinner();

        String gameBoardJsonStr = gson.toJson(gameBoard);
        sendGameBoardToAllPlayers(gameBoardJsonStr);
      } catch (Exception e) {
        ctx.status(400);
        System.out.print(e.getMessage());
      }
    });

    // Web sockets - DO NOT DELETE or CHANGE
    app.ws("/gameboard", new UiWebSocket());
  }

  /** Send message to all players.
   * @param gameBoardJson Gameboard JSON
   * @throws IOException Websocket message send IO Exception
   */
  private static void sendGameBoardToAllPlayers(final String gameBoardJson) {
    Queue<Session> sessions = UiWebSocket.getSessions();
    for (Session sessionPlayer : sessions) {
      try {
        sessionPlayer.getRemote().sendString(gameBoardJson);
      } catch (IOException e) {
        // Add logger here
      }
    }
  }

  public static void stop() {
    app.stop();
  }
}
