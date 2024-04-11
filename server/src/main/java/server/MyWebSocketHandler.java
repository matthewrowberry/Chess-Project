package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import webSocketMessages.userCommands.GameID;
import webSocketMessages.userCommands.UserGameCommand;

@WebSocket
public class MyWebSocketHandler{

    private final ConnectionsManager connections = new ConnectionsManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        Gson basic = new Gson();
        UserGameCommand.CommandType command = basic.fromJson(message, UserGameCommand.class).getCommandType();
        switch(command){
            case JOIN_PLAYER -> joinPlayer(session,message);
            case JOIN_OBSERVER -> joinObserver(session,message);
            case MAKE_MOVE -> makeMove(session,message);
            case LEAVE -> leave(session,message);
            case RESIGN -> resign(session,message);
        }
        session.getRemote().sendString("WebSocket response: " + message);
    }

    private void resign(Session session, String message) {
    }

    private void leave(Session session, String message) {
        Gson json = new Gson();
        GameID request = json.fromJson(message, GameID.class);

        connections.removeConnection(request.getGameID(),session);
    }

    private void makeMove(Session session, String message) {
    }

    private void joinObserver(Session session, String message) {
        Gson json = new Gson();
        GameID request = json.fromJson(message, GameID.class);
        connections.addConnection(request.getGameID(),session);
    }

    private void joinPlayer(Session session, String message) {
        Gson json = new Gson();
        GameID request = json.fromJson(message, GameID.class);

        connections.addConnection(request.getGameID(),session);
    }


}
