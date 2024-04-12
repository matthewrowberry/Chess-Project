package webSocketMessages.userCommands;

import chess.ChessMove;

public class Move extends UserGameCommand{
    ChessMove move;
    int gameID;
    public Move(CommandType command,String authToken, ChessMove move, int gameID) {
        super(authToken);
        this.move = move;
        this.commandType = command;
        this.gameID = gameID;
    }

    public ChessMove getMove(){
        return move;
    }
    public int getGameID(){
        return gameID;
    }

}
