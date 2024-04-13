package webSocketMessages.userCommands;

import chess.ChessGame;

public class GameID extends UserGameCommand {

    int gameID;
    ChessGame.TeamColor playerColor;

    public GameID(String authToken, int gameID, CommandType command, ChessGame.TeamColor color) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = command;
        this.playerColor = color;
    }

    public int getGameID(){
        return this.gameID;
    }
    public ChessGame.TeamColor getPlayerColor(){
        return playerColor;
    }

}
