package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.Game;
import webSocketMessages.serverMessages.Message;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

public class WebSocket extends Endpoint {

    private Session session;
    private ArrayList<String> messages;
    private ChessGame game;
    private Gson json = new Gson();
    private PrintHelper printer = new PrintHelper();

    boolean color = true;

    public WebSocket() throws Exception {
        messages = new ArrayList<>();
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {

                ServerMessage type = json.fromJson(message, ServerMessage.class);
                if(type.getServerMessageType()== ServerMessage.ServerMessageType.NOTIFICATION){
                    System.out.println(json.fromJson(message, Message.class).getMessage());
                }else if(type.getServerMessageType()== ServerMessage.ServerMessageType.LOAD_GAME){
                    System.out.println(message);
                    System.out.println(json.fromJson(message, Game.class));
                    game = json.fromJson(message, Game.class).getGame();

                    printer.printBoard(game.getBoard(),color);
                }
                else if(type.getServerMessageType()== ServerMessage.ServerMessageType.ERROR){

                }
                else{
                    System.out.println("uh oh");
                }
                System.out.print("[Gameplay] >>> ");
            }
        });

    }

    public void setColor(String color){
        if(color.equals("WHITE")){
            this.color = true;
        }
        else{
            this.color = false;
        }

    }

    public Collection<ChessMove> getValidMoves(ChessPosition start){
        return game.validMoves(start);
    }

    public ChessBoard getBoard(){
        return game.getBoard();
    }
    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }


    public void redraw(){
        printer.printBoard(game.getBoard(),this.color);
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
