package webSocketMessages.serverMessages;

public class Oopsie extends ServerMessage{
    String errorMessage;
    public Oopsie(ServerMessageType type, String message) {
        super(type);
        this.errorMessage = message;
    }

    public String getErrorMessage(){
        return errorMessage;
    }
}
