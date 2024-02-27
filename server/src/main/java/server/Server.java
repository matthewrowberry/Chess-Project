package server;

import Records.Registration;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDao;
import dataAccess.UserDAO;
import service.loginService;
import spark.*;

public class Server {
    private loginService login;
    private UserDAO users;
    private AuthDAO auths;
    public int run(int desiredPort) {
        users = new MemoryUserDao();
        auths = new MemoryAuthDAO();
        Spark.port(desiredPort);

        Spark.staticFiles.location("homepage");

        // Register your endpoints and handle exceptions here.

        /*Spark.delete("/db", new Route() {
            public Object handle(Request req, Response res) {
                names.add(req.params(":name"));
                res.type("application/json");
                return new Gson().toJson(Map.of("name", names));
            }
        });*/

        Spark.post("/user", this::registerUser);



        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object registerUser(Request req, Response res){
        Gson parser = new Gson();
        loginService loginService = parser.fromJson(req.body(), loginService.class);
        return parser.toJson(loginService.register(users,auths));
    }
}