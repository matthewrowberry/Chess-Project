package service;

import Records.ErrorMessage;
import Records.ErrorNumber;
import Records.FullError;
import chess.ChessGame;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import model.GameData;
import model.GameID;

public class GameService {
    String gameName;
    public GameService(String gameName){
        this.gameName = gameName;
    }
    public GameService(){
        new GameService(null);
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

            return games.getGames();
        }
        return new FullError(new ErrorNumber(401),new ErrorMessage("Error: Unauthorized"));

    }
}
