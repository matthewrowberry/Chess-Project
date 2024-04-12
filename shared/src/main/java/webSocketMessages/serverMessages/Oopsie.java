package webSocketMessages.serverMessages;

public class Oopsie extends ServerMessage{
    String message;
    public Oopsie(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
