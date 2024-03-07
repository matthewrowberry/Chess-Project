package server;

import Records.ErrorMessage;
import Records.ErrorNumber;
import Records.FullError;
import model.UserData;
import com.google.gson.Gson;
import dataAccess.*;
import service.DbService;
import service.GameService;
import service.LoginService;
import spark.*;

import java.sql.SQLException;
import java.util.Objects;

public class Server {
    private UserDAO users;
    private AuthDAO auths;
    private GameDAO games;
    private DatabaseUserDao users2;
    Gson parser;
    public int run(int desiredPort) throws Exception {
        users = new DatabaseUserDao();
        //users = new MemoryUserDao();
        auths = new MemoryAuthDAO();
        games = new MemoryGameDAO();
        parser = new Gson();
        Spark.port(desiredPort);

        Spark.staticFiles.location("homepage");

        // Register your endpoints and handle exceptions here.




        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::login);
        Spark.delete("/session",this::logout);
        Spark.delete("/db", this::clear);
        Spark.post("/game",this::createGame);
        Spark.get("/game",this::getGames);
        Spark.put("/game",this::joinGame);


        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object returner(Object result, Response res){
        if(result instanceof FullError){
            res.status(((FullError) result).number().code());
            var body = parser.toJson(((FullError) result).message());
            res.body(body);
            return body;

        }
        var body = parser.toJson(result);
        res.status(200);
        res.body(body);
        return body;
    }
    private Object registerUser(Request req, Response res){

        LoginService loginService = parser.fromJson(req.body(), LoginService.class);

        var result = loginService.register(users,auths);
        return returner(result,res);
    }

    private Object login(Request req, Response res){
        LoginService loginService = parser.fromJson(req.body(), LoginService.class);
        var result = loginService.login(users,auths);
        return returner(result,res);
    }

    private Object logout(Request req, Response res){
        LoginService loginService = new LoginService();
        var result = loginService.logout(auths,req.headers("authorization"));
        return returner(result,res);
    }

    private Object clear(Request req, Response res){
        DbService dbService = new DbService();
        dbService.clear(users,auths,games);
        return returner(new UserData(null,null,null),res);
    }

    private boolean authExists(Request req){
        return req.headers("authorization")==null || Objects.equals(req.headers("authorization"), "");
    }

    private Object badRequest(Request req, Response res){
        return returner(new FullError(new ErrorNumber(400),new ErrorMessage("Error: bad request")),res);
    }
    private Object createGame(Request req, Response res){

        if(authExists(req)){
            return badRequest(req,res);
        }

        GameService gameService = parser.fromJson(req.body(), GameService.class);

        Object result = gameService.create(req.headers("authorization"),games,auths);

        return returner(result,res);

    }
    private Object getGames(Request req, Response res){
        if(authExists(req)){
            return badRequest(req,res);
        }
        GameService gameService = new GameService();
        Object result = gameService.getGames(req.headers("authorization"),games,auths);
        return returner(result,res);
    }

    private Object joinGame(Request req, Response res){
        if(authExists(req)){
            return badRequest(req,res);
        }

        GameService gameService = parser.fromJson(req.body(), GameService.class);
        Object result = gameService.joinGame(req.headers("authorization"),games,auths);
        return returner(result,res);
    }
}