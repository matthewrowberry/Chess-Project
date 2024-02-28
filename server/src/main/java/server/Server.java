package server;

import Records.FullError;
import Records.User;
import com.google.gson.Gson;
import dataAccess.*;
import service.DbService;
import service.loginService;
import spark.*;

public class Server {
    private UserDAO users;
    private AuthDAO auths;
    private GameDAO games;
    Gson parser;
    public int run(int desiredPort) {
        users = new MemoryUserDao();
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

        loginService loginService = parser.fromJson(req.body(), loginService.class);

        var result = loginService.register(users,auths);
        return returner(result,res);
    }

    private Object login(Request req, Response res){
        loginService loginService = parser.fromJson(req.body(), loginService.class);
        var result = loginService.login(users,auths);
        return returner(result,res);
    }

    private Object logout(Request req, Response res){
        loginService loginService = new loginService();
        var result = loginService.logout(auths,req.headers("authorization"));
        return returner(result,res);
    }

    private Object clear(Request req, Response res){
        DbService dbService = new DbService();
        dbService.clear(users,auths,games);
        return returner(new User(null,null,null),res);
    }

    private Object createGame(Request req, Response res){

    }
}