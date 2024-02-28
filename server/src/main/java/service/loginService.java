package service;

import Records.*;

import dataAccess.AuthDAO;
import dataAccess.UserDAO;

import java.util.Objects;
import java.util.UUID;

public class loginService {
    private String username, password, email;
    public loginService(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public loginService(String username, String password){
        new loginService(username,password,null);
    }

    public loginService(){
        new loginService(null,null,null);
    }

    private AuthToken getAuth(String username,AuthDAO auths){
        String authToken = UUID.randomUUID().toString();
        auths.createAuth(username,authToken);
        return new AuthToken(username,authToken);
    }

    public Object register(UserDAO users, AuthDAO auths){
        User user = new User(username,password,email);
        if(username==null || password==null || email==null){
            return new FullError(new ErrorNumber(400),new ErrorMessage("Error: bad request"));
        }

        if(users.getUser(user)==null){
            users.createUser(user);
            return getAuth(username,auths);
        }
        else{
            return new FullError(new ErrorNumber(403),new ErrorMessage("Error: already taken"));
        }

    }

    public Object login(UserDAO users, AuthDAO auths){

        User user = new User(username,password,null);
        user = users.getUser(user);
        if(Objects.equals(password, user.password())){
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
}
