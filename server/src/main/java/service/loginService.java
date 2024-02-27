package service;

import Records.AuthToken;
import Records.User;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;

import java.util.UUID;

public class loginService {
    private String username, password, email;
    public loginService(String username, String password, String email){

    }

    public loginService(String username, String password){
        new loginService(username,password,null);
    }
    public AuthToken register(UserDAO users, AuthDAO auths){
        User user = new User(username,password,email);
        String authToken = UUID.randomUUID().toString();
        if(users.getUser(user)==null){
            users.createUser(user);
            auths.createAuth(username,authToken);
            return new AuthToken(username,authToken);
        }
        return null;
    }
}
