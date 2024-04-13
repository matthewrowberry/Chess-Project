package webSocketMessages.userCommands;

public class Leave extends UserGameCommand{
    int gameID;
    public Leave(String authToken,CommandType command,int gameID) {

        super(authToken);
        commandType = command;
        this.gameID = gameID;
        
    }

    public int getGameID(){
        return gameID;
    }
}
