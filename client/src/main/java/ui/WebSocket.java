package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.Game;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

public class WebSocket extends Endpoint {

    private Session session;
    private ArrayList<String> messages;
    private ChessGame game;
    private Gson json = new Gson();
    private PrintHelper printer = new PrintHelper();

    public WebSocket() throws Exception {
        messages = new ArrayList<>();
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println(message);
                ServerMessage type = json.fromJson(message, ServerMessage.class);
                if(type.getServerMessageType()== ServerMessage.ServerMessageType.NOTIFICATION){
                    System.out.println("hi");
                }else if(type.getServerMessageType()== ServerMessage.ServerMessageType.LOAD_GAME){
                    game = json.fromJson(message, Game.class).getGame();
                    printer.printBoard(game.getBoard());
                }
                else if(type.getServerMessageType()== ServerMessage.ServerMessageType.ERROR){

                }
                else{
                    System.out.println("uh oh");
                }

            }
        });

    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }



    public String getMessage(){
        if(messages.isEmpty()){
            return "";
        }
        return messages.removeLast();
    }

    public void end(){
        try {
            this.session.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
