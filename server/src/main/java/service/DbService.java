package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class DbService {
    public DbService(){

    }

    public Object clear(UserDAO users, AuthDAO auths, GameDAO games){
        users.clear();
        auths.clear();
        games.clear();
        return null;
    }
}
