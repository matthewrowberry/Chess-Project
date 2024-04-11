package ui;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

public class WebSocket extends Endpoint {

    public Session session;

    public WebSocket() throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println(message);
            }
        });

    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void end(){
        try {
            this.session.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
