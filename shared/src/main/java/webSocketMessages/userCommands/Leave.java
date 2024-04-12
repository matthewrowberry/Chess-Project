package webSocketMessages.userCommands;

public class Leave extends UserGameCommand{
    public Leave(String authToken,CommandType command) {

        super(authToken);
        commandType = command;
        
    }
}
