package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.*;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import webSocketMessages.serverMessages.Game;
import webSocketMessages.serverMessages.Message;
import webSocketMessages.serverMessages.Oopsie;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.sql.SQLException;

@WebSocket
public class MyWebSocketHandler{

    private final ConnectionsManager connections = new ConnectionsManager();
    private final UserDAO users = new DatabaseUserDao();

    private final AuthDAO auths = new DatabaseAuthDao();

    private final GameDAO games = new DatabaseGameDao();



    public MyWebSocketHandler() throws SQLException, DataAccessException {
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        Gson basic = new Gson();
        UserGameCommand.CommandType command = basic.fromJson(message, UserGameCommand.class).getCommandType();
        System.out.println(command);
        switch(command){
            case JOIN_PLAYER -> joinPlayer(session,message);
            case JOIN_OBSERVER -> joinObserver(session,message);
            case MAKE_MOVE -> makeMove(session,message);
            case LEAVE -> leave(session,message);
            case RESIGN -> resign(session,message);
        }
        //session.getRemote().sendString("WebSocket response: " + message);
    }

    private void resign(Session session, String message) {

        Gson json = new Gson();
        Resign resign = json.fromJson(message, Resign.class);

        GameData gameData = games.getGame(resign.getGameID());
        if(!gameData.game().gameOver) {
            if (auths.getUsername(resign.getAuthString()).equals(gameData.whiteUsername()) || auths.getUsername(resign.getAuthString()).equals(gameData.blackUsername())) {
                ChessGame game = gameData.game();
                game.gameOver();
                System.out.println(game.gameOver);
                games.updateGame(gameData.gameID(), new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game));
                try {
                    session.getRemote().sendString(json.toJson(new Message("You resigned")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String username = auths.getUsername(resign.getAuthString());

                //connections.update(resign.getGameID(), session,json.toJson(new Game(game)));
                connections.notify(resign.getGameID(), session, username + " has resigned.");
            } else {
                try {
                    session.getRemote().sendString(json.toJson(new Oopsie(ServerMessage.ServerMessageType.ERROR, "Observer can't resign")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }else{
            try {
                session.getRemote().sendString(json.toJson(new Oopsie(ServerMessage.ServerMessageType.ERROR, "Game Has already been resigned")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void leave(Session session, String message) {
        Gson json = new Gson();
        System.out.println(message);
        Leave request = json.fromJson(message, Leave.class);
        System.out.println(request);


        connections.removeConnection(request.getGameID(),session);

        connections.notify(request.getGameID(), session,auths.getUsername(request.getAuthString())+" has left.");
    }

    private void makeMove(Session session, String message) {

        boolean err = false;
        Gson json = new Gson();
        Move move = json.fromJson(message, Move.class);
        String color = null;
        String username = auths.getUsername(move.getAuthString());
        GameData gameData = games.getGame(move.getGameID());
        ChessGame game = gameData.game();
        if(game.gameOver){
            Oopsie error = new Oopsie(ServerMessage.ServerMessageType.ERROR,"No move allowed; The Game is over.");
            err = true;
            try {
                session.getRemote().sendString(json.toJson(error));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }else {

            if (game.getTeamTurn() == ChessGame.TeamColor.WHITE) {
                color = "White";
                System.out.println("white");
                System.out.println(gameData.whiteUsername() + " - " + username);
                System.out.println(gameData.whiteUsername().trim().equals(username.trim()));
                if (gameData.whiteUsername().equals(username)) {
                    try {
                        System.out.println("time to move");
                        System.out.println(game.getTeamTurn());
                        game.makeMove(move.getMove());
                    } catch (InvalidMoveException e) {
                        System.out.println("invalid");
                        Oopsie error = new Oopsie(ServerMessage.ServerMessageType.ERROR, "That's an invalid move");

                        try {
                            session.getRemote().sendString(json.toJson(error));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        err = true;
                    }
                } else {
                    err = true;
                    Oopsie error = new Oopsie(ServerMessage.ServerMessageType.ERROR, "Unfortunately, it's not your turn.");

                    try {
                        session.getRemote().sendString(json.toJson(error));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } else {
                color = "Black";
                if (gameData.blackUsername().equals(username)) {
                    try {
                        game.makeMove(move.getMove());
                    } catch (InvalidMoveException e) {
                        Oopsie error = new Oopsie(ServerMessage.ServerMessageType.ERROR, "That's an invalid move");
                        try {
                            session.getRemote().sendString(json.toJson(error));

                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        err = true;
                    }
                } else {
                    err = true;
                    Oopsie error = new Oopsie(ServerMessage.ServerMessageType.ERROR, "My liege, it's not your turn");

                    try {
                        session.getRemote().sendString(json.toJson(error));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
        if(!err) {
            System.out.println(game.getBoard().toString());
            games.updateGame(move.getGameID(), new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game));

            Game sendIt = new Game(game);
            try {
                session.getRemote().sendString(json.toJson(sendIt));
                connections.update(gameData.gameID(), session,json.toJson(sendIt));
                connections.notify(gameData.gameID(), session,color + move.getMove().toString());
            } catch (IOException e) {
                System.out.println(e.toString());
            }
            System.out.println("Finished");
        }
    }



    private void joinObserver(Session session, String message) {
        Gson json = new Gson();
        GameID request = json.fromJson(message, GameID.class);
        connections.addConnection(request.getGameID(),session);
        if(games.getGame(request.getGameID())!=null) {
            if(auths.getUsername(request.getAuthString())!=null) {
                Game game = new Game(games.getGame(request.getGameID()).game());
                connections.notify(request.getGameID(), session, auths.getUsername(request.getAuthString()) + " has joined the game as an Observer");

                try {
                    session.getRemote().sendString(json.toJson(game));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                try {
                    session.getRemote().sendString(json.toJson(new Oopsie(ServerMessage.ServerMessageType.ERROR, "Invalid AuthToken")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }else{
            try {
                session.getRemote().sendString(json.toJson(new Oopsie(ServerMessage.ServerMessageType.ERROR, "Invalid GameID")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void joinPlayer(Session session, String message) {
        Gson json = new Gson();
        GameID request = json.fromJson(message, GameID.class);
        connections.addConnection(request.getGameID(),session);
        if(games.getGame(request.getGameID())!=null) {
            if(auths.getUsername(request.getAuthString())!= null) {
                Game game = new Game(games.getGame(request.getGameID()).game());
                System.out.println(request.getPlayerColor());
                if (request.getPlayerColor() == ChessGame.TeamColor.WHITE) {
                    if (auths.getUsername(request.getAuthString()).equals(games.getGame(request.getGameID()).whiteUsername())) {
                        connections.notify(request.getGameID(), session, auths.getUsername(request.getAuthString()) + " has joined the game as White");
                        try {
                            session.getRemote().sendString(json.toJson(game));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            session.getRemote().sendString(json.toJson(new Oopsie(ServerMessage.ServerMessageType.ERROR, "You are not the designated White Team")));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else if (request.getPlayerColor() == ChessGame.TeamColor.BLACK) {
                    if (auths.getUsername(request.getAuthString()).equals(games.getGame(request.getGameID()).blackUsername())) {
                        connections.notify(request.getGameID(), session, auths.getUsername(request.getAuthString()) + " has joined the game as BLACK");
                        try {
                            session.getRemote().sendString(json.toJson(game));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            session.getRemote().sendString(json.toJson(new Oopsie(ServerMessage.ServerMessageType.ERROR, "You are not the designated White Team")));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            else{
                try {
                    session.getRemote().sendString(json.toJson(new Oopsie(ServerMessage.ServerMessageType.ERROR, "Invalid authToken")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }else{
            try {
                session.getRemote().sendString(json.toJson(new Oopsie(ServerMessage.ServerMessageType.ERROR, "Invalid GameID")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }


}
