package passoffTests;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthToken;
import model.GameID;
import service.GameService;
import service.LoginService;

public class Generate {
    public Generate(){

    }

    public String generateAuth(UserDAO users, AuthDAO auths){
        LoginService loginService = new LoginService("username","password","this@gmail.com");
        AuthToken authToken = (AuthToken) loginService.register(users,auths);
        return authToken.authToken();
    }
    public String generateDiffAuth(UserDAO users, AuthDAO auths){
        LoginService loginService = new LoginService("username1","password1","this@gmail.com");
        AuthToken authToken = (AuthToken) loginService.register(users,auths);
        return authToken.authToken();
    }

    public int createGame(String auth, GameDAO games, AuthDAO auths){
        GameService gameService = new GameService();
        GameID gameID = (GameID) gameService.create(auth,games,auths);
        return gameID.gameID();
    }
}
