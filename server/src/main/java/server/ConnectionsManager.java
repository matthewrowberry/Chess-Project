package server;

import com.google.gson.Gson;
import model.AuthToken;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.Message;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConnectionsManager {
    Map<Integer, ArrayList<Session>> users;

    public ConnectionsManager() {
        users = new HashMap<>();
    }

    public void addConnection(int game, Session session){
        System.out.println(game);
        if(users.get(game)==null) {
            ArrayList<Session> newList = new ArrayList<>();
            newList.add(session);
            users.put(game,newList);
        }
        else{
            users.get(game).add(session);
        }
    }

    public void removeConnection(int game, Session session){
        try{
            for(int i = 0; i<users.get(game).size(); i++){
                if(users.get(game).get(i)==session){
                    users.get(game).remove(i);
                }
            }
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void notify(int game, Session session, String message){
        Message mail = new Message(message);
        Gson json = new Gson();

        try{
            for(int i = 0; i<users.get(game).size(); i++){
                Session thisSession = users.get(game).get(i);
                if(thisSession!=session){
                    if(thisSession.isOpen()){
                        thisSession.getRemote().sendString(json.toJson(mail));
                    }
                    else{
                        users.get(game).remove(i);
                        i--;
                    }
                }
            }
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void update(int game, Session session, String message){


        try{
            for(int i = 0; i<users.get(game).size(); i++){
                Session thisSession = users.get(game).get(i);
                if(thisSession!=session){
                    if(thisSession.isOpen()){
                        thisSession.getRemote().sendString(message);
                    }
                    else{
                        users.get(game).remove(i);
                        i--;
                    }
                }
            }
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
}
