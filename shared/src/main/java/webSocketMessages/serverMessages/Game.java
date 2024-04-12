package webSocketMessages.serverMessages;

import chess.ChessGame;

public class Game extends ServerMessage {
    ChessGame game;
    public Game(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public ChessGame getGame(){
        return game;
    }
}
