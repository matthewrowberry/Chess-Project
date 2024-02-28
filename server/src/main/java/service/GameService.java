package service;

import Records.ErrorMessage;
import Records.ErrorNumber;
import Records.FullError;
import chess.ChessGame;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import model.GameData;
import model.GameID;
import model.GameList;
import model.UserData;

import java.util.Objects;

public class GameService {
    String gameName,playerColor;
    int gameID;
    
    public GameService(String gameName,String playerColor,int gameID){
        this.gameName = gameName;
        this.playerColor = playerColor;
        this.gameID = gameID;
    }
    public GameService(String gameName){
        new GameService(gameName,null,-1);
    }
    public GameService(){
        new GameService(null,null,-1);
    }
    public GameService(String playerColor, int gameID){
        new GameService(null, playerColor,gameID);
    }

    public Object create(String authorization, GameDAO games, AuthDAO auths) {
        loginService loginService = new loginService();
        if(loginService.checkAuth(authorization,auths)) {

            ChessGame game = new ChessGame();
            GameData insert = new GameData(0, "", "", gameName, game);

            return new GameID(games.insertGame(insert));
        }
        return new FullError(new ErrorNumber(401),new ErrorMessage("Error: Unauthorized"));
    }

    public Object getGames(String authorization, GameDAO games, AuthDAO auths){
        loginService loginService = new loginService();
        if(loginService.checkAuth(authorization,auths)) {

            return new GameList(games.getGames());
        }
        return new FullError(new ErrorNumber(401),new ErrorMessage("Error: Unauthorized"));

    }
    
    public Object joinGame(String authorization, GameDAO games, AuthDAO auths) {
        loginService loginService = new loginService();
        if (loginService.checkAuth(authorization, auths)) {
            GameData game = games.getGame(gameID);
            if(Objects.equals(playerColor, "WHITE")){
                if(Objects.equals(game.whiteUsername(), "")){
                    GameData update = new GameData(game.gameID(),auths.getUsername(authorization),game.blackUsername(),game.gameName(),game.game());
                    games.updateGame(gameID,update);
                    return new UserData(null,null,null);
                } else if (Objects.equals(game.whiteUsername(), auths.getUsername(authorization))) {
                    return new UserData(null,null,null);
                } else{
                    return new FullError(new ErrorNumber(403),new ErrorMessage("Error: already taken"));

                }
            }
            else if(Objects.equals(playerColor, "BLACK")){
                if(Objects.equals(game.blackUsername(), "")){
                    GameData update = new GameData(game.gameID(),game.whiteUsername(),auths.getUsername(authorization),game.gameName(),game.game());
                    games.updateGame(gameID,update);
                    return new UserData(null,null,null);
                } else if (Objects.equals(game.blackUsername(), auths.getUsername(authorization))) {
                    return new UserData(null,null,null);
                } else{
                    return new FullError(new ErrorNumber(403),new ErrorMessage("Error: already taken"));

                }
            } else if (playerColor == "") {
                return new UserData(null,null,null);

            } else{
                return new FullError(new ErrorNumber(400),new ErrorMessage("Error: bad request"));
            
            }
        }
        return new FullError(new ErrorNumber(401),new ErrorMessage("Error: Unauthorized"));
    }
}
