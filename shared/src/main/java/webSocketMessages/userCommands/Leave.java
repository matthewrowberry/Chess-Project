package webSocketMessages.userCommands;

public class Leave extends UserGameCommand{
    int gameID;
    public Leave(String authToken,CommandType command,int GameID) {

        super(authToken);
        commandType = command;
        this.gameID = GameID;
        
    }

    public int getGameID(){
        return gameID;
    }
}
