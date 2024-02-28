package service;

import Records.*;

import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import model.AuthToken;
import model.UserData;

import java.util.Objects;
import java.util.UUID;

public class LoginService {
    private String username, password, email;
    public LoginService(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public LoginService(String username, String password){
        new LoginService(username,password,null);
    }

    public LoginService(){
        new LoginService(null,null,null);
    }

    private AuthToken getAuth(String username, AuthDAO auths){
        String authToken = UUID.randomUUID().toString();
        auths.createAuth(username,authToken);
        return new AuthToken(username,authToken);
    }

    public Object register(UserDAO users, AuthDAO auths){
        UserData userData = new UserData(username,password,email);
        if(username==null || password==null || email==null){
            return new FullError(new ErrorNumber(400),new ErrorMessage("Error: bad request"));
        }

        if(users.getUser(userData)==null){
            users.createUser(userData);
            return getAuth(username,auths);
        }
        else{
            return new FullError(new ErrorNumber(403),new ErrorMessage("Error: already taken"));
        }

    }

    public Object login(UserDAO users, AuthDAO auths){

        UserData userData = new UserData(username,password,null);
        userData = users.getUser(userData);
        if(userData != null && Objects.equals(password, userData.password())){
            return getAuth(username,auths);

        }
        else{
            return new FullError(new ErrorNumber(401),new ErrorMessage("Error: unauthorized"));
        }
    }

    public Object logout(AuthDAO auths,String auth){
        System.out.println(auth);
        if(auths.checkAuth(auth).username()!=null) {
            auths.deleteAuth(auth);
            return null;
        }
        else{
            return new FullError(new ErrorNumber(401),new ErrorMessage("Error: unauthorized"));
        }

    }

    public boolean checkAuth(String authToken, AuthDAO auths){
        return auths.checkAuth(authToken).username() != null;
    }
}
