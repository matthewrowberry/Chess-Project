package webSocketMessages.serverMessages;

public class Message extends ServerMessage{
    private String message;
    public Message(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;

    }

    public String getMessage(){
        return message;
    }
}
