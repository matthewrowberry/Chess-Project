package webSocketMessages.userCommands;

public class GameID extends UserGameCommand {

    int gameID;
    public GameID(String authToken, int gameID, CommandType command) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = command;
    }

    public int getGameID(){
        return this.gameID;
    }


}
