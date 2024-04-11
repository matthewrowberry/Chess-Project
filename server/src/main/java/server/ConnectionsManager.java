package server;

import model.AuthToken;
import org.eclipse.jetty.websocket.api.Session;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConnectionsManager {
    Map<Integer, ArrayList<Session>> users;

    public ConnectionsManager() {
        users = new HashMap<>();
    }

    public void addConnection(int Game, Session session){
        System.out.println(Game);
        if(users.get(Game)==null) {
            ArrayList<Session> newList = new ArrayList<>();
            newList.add(session);
            users.put(Game,newList);
        }
        else{
            users.get(Game).add(session);
        }
    }

    public void removeConnection(int Game, Session session){
        try{
            for(int i = 0; i<users.get(Game).size(); i++){
                if(users.get(Game).get(i)==session){
                    users.get(Game).remove(i);
                }
            }
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void notify(int Game, Session session){
        try{
            for(int i = 0; i<users.get(Game).size(); i++){
                Session thisSession = users.get(Game).get(i);
                if(thisSession!=session){
                    if(thisSession.isOpen()){
                        thisSession.getRemote().sendString("hey");
                    }
                    else{
                        users.get(Game).remove(i);
                        i--;
                    }
                }
            }
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
}
