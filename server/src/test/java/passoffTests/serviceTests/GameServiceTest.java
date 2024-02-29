package passoffTests.serviceTests;

import Records.FullError;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDao;
import model.GameDataRedacted;
import model.GameID;
import model.GameList;
import model.UserData;
import org.junit.jupiter.api.Test;
import passoffTests.Generate;
import service.GameService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    @Test
    void create() {
        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryUserDao users = new MemoryUserDao();
        MemoryGameDAO games = new MemoryGameDAO();
        Generate generate = new Generate();
        String auth = generate.generateAuth(users, auths);
        GameService gameService = new GameService();
        GameID gameID = (GameID) gameService.create(auth,games,auths);
        assertNotNull(gameID);
        assertTrue(0<gameID.gameID()&&1001>gameID.gameID());


    }

    @Test
    void createWithBadAuth() {
        MemoryAuthDAO auths = new MemoryAuthDAO();

        MemoryGameDAO games = new MemoryGameDAO();
        GameService gameService = new GameService();
        FullError error = (FullError) gameService.create("badness",games,auths);
        assertEquals(401,error.number().code());
        assertEquals("Error: unauthorized",error.message().message());
    }

    @Test
    void getGames() {
        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryUserDao users = new MemoryUserDao();
        MemoryGameDAO games = new MemoryGameDAO();
        GameService gameService = new GameService();
        Generate generate = new Generate();
        String auth = generate.generateAuth(users, auths);
        int id = generate.createGame(auth,games,auths);
        GameList list = (GameList) gameService.getGames(auth,games,auths);

        assertNotNull(list);
        assertTrue(list.games().getFirst().gameID()>-1);

    }

    @Test
    void getGamesUnauthorized() {
        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryUserDao users = new MemoryUserDao();
        MemoryGameDAO games = new MemoryGameDAO();
        GameService gameService = new GameService();
        Generate generate = new Generate();
        String auth = generate.generateAuth(users, auths);
        generate.createGame(auth,games,auths);

        Object result = gameService.getGames("hi",games,auths);
        assertInstanceOf(FullError.class,result);
        FullError result1 = (FullError) result;
        assertEquals(401,result1.number().code());
        assertEquals("Error: unauthorized",result1.message().message());

    }

    @Test
    void joinGameWHITE() {
        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryUserDao users = new MemoryUserDao();
        MemoryGameDAO games = new MemoryGameDAO();
        GameService gameService = new GameService();
        Generate generate = new Generate();
        String auth = generate.generateAuth(users, auths);
        int id = generate.createGame(auth,games,auths);
        GameService gameService1 = new GameService("WHITE",id);
        assertInstanceOf(UserData.class,gameService1.joinGame(auth,games,auths));
        GameList list = (GameList) gameService1.getGames(auth,games,auths);
        assertEquals("username",list.games().getFirst().whiteUsername());
    }

    @Test
    void joinGameBlack() {
        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryUserDao users = new MemoryUserDao();
        MemoryGameDAO games = new MemoryGameDAO();
        GameService gameService = new GameService();
        Generate generate = new Generate();
        String auth = generate.generateAuth(users, auths);
        int id = generate.createGame(auth,games,auths);
        GameService gameService1 = new GameService("BLACK",id);
        assertInstanceOf(UserData.class,gameService1.joinGame(auth,games,auths));
        GameList list = (GameList) gameService1.getGames(auth,games,auths);
        assertEquals("username",list.games().getFirst().blackUsername());
    }

    @Test
    void joinNullGameBlack() {

        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryUserDao users = new MemoryUserDao();
        MemoryGameDAO games = new MemoryGameDAO();
        GameService gameService = new GameService();
        Generate generate = new Generate();
        String auth = generate.generateAuth(users, auths);
        int id = generate.createGame(auth,games,auths);
        GameService gameService1 = new GameService("BLACK",-1);
        assertInstanceOf(FullError.class,gameService1.joinGame(auth,games,auths));
        FullError error = (FullError) gameService1.joinGame(auth,games,auths);
        assertEquals(400,error.number().code());
        assertEquals("Error: bad request",error.message().message());
    }

    @Test
    void joinGameBlackAgain() {
        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryUserDao users = new MemoryUserDao();
        MemoryGameDAO games = new MemoryGameDAO();
        GameService gameService = new GameService();
        Generate generate = new Generate();
        String auth = generate.generateAuth(users, auths);
        int id = generate.createGame(auth,games,auths);
        GameService gameService1 = new GameService("BLACK",id);
        assertInstanceOf(UserData.class,gameService1.joinGame(auth,games,auths));
        GameList list = (GameList) gameService1.getGames(auth,games,auths);
        assertEquals("username",list.games().getFirst().blackUsername());
        gameService1 = new GameService("BLACK",id);
        assertInstanceOf(UserData.class,gameService1.joinGame(auth,games,auths));
        list = (GameList) gameService1.getGames(auth,games,auths);
        assertEquals("username",list.games().getFirst().blackUsername());
    }

    @Test
    void joinGameWhiteAgain() {
        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryUserDao users = new MemoryUserDao();
        MemoryGameDAO games = new MemoryGameDAO();
        GameService gameService = new GameService();
        Generate generate = new Generate();
        String auth = generate.generateAuth(users, auths);
        int id = generate.createGame(auth,games,auths);
        GameService gameService1 = new GameService("WHITE",id);
        assertInstanceOf(UserData.class,gameService1.joinGame(auth,games,auths));
        GameList list = (GameList) gameService1.getGames(auth,games,auths);
        assertEquals("username",list.games().getFirst().whiteUsername());
        gameService1 = new GameService("WHITE",id);
        assertInstanceOf(UserData.class,gameService1.joinGame(auth,games,auths));
        list = (GameList) gameService1.getGames(auth,games,auths);
        assertEquals("username",list.games().getFirst().whiteUsername());
    }

    @Test
    void joinGameBlackMisspelled() {
        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryUserDao users = new MemoryUserDao();
        MemoryGameDAO games = new MemoryGameDAO();
        GameService gameService = new GameService();
        Generate generate = new Generate();
        String auth = generate.generateAuth(users, auths);
        int id = generate.createGame(auth,games,auths);
        GameService gameService1 = new GameService("BLOCK",id);
        assertInstanceOf(FullError.class,gameService1.joinGame(auth,games,auths));
        FullError error = (FullError) gameService1.joinGame(auth,games,auths);
        assertEquals(400,error.number().code());
        assertEquals("Error: bad request",error.message().message());
    }

    @Test
    void joinGameBlackUnauthorized() {
        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryUserDao users = new MemoryUserDao();
        MemoryGameDAO games = new MemoryGameDAO();
        GameService gameService = new GameService();
        Generate generate = new Generate();
        String auth = generate.generateAuth(users, auths);
        int id = generate.createGame(auth,games,auths);
        GameService gameService1 = new GameService("BLACK",id);
        auth = "invalid";
        assertInstanceOf(FullError.class,gameService1.joinGame(auth,games,auths));
        FullError error = (FullError) gameService1.joinGame(auth,games,auths);
        assertEquals(401,error.number().code());
        assertEquals("Error: unauthorized",error.message().message());
        }

    @Test
    void joinGameSpectator() {
        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryUserDao users = new MemoryUserDao();
        MemoryGameDAO games = new MemoryGameDAO();
        GameService gameService = new GameService();
        Generate generate = new Generate();
        String auth = generate.generateAuth(users, auths);
        int id = generate.createGame(auth,games,auths);
        GameService gameService1 = new GameService("",id);
        assertInstanceOf(UserData.class,gameService1.joinGame(auth,games,auths));
        GameList list = (GameList) gameService1.getGames(auth,games,auths);
        assertNull(list.games().getFirst().whiteUsername());
        assertNull(list.games().getFirst().blackUsername());
    }

    @Test
    void joinGameBlackError() {
        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryUserDao users = new MemoryUserDao();
        MemoryGameDAO games = new MemoryGameDAO();
        GameService gameService = new GameService();
        Generate generate = new Generate();
        String auth = generate.generateAuth(users, auths);
        int id = generate.createGame(auth,games,auths);
        GameService gameService1 = new GameService("BLACK",id);
        assertInstanceOf(UserData.class,gameService1.joinGame(auth,games,auths));
        auth = generate.generateDiffAuth(users,auths);
        gameService1 = new GameService("BLACK",id);
        Object result = gameService1.joinGame(auth,games,auths);
        assertInstanceOf(FullError.class,result);
        FullError result1 = (FullError) result;
        assertEquals(403,result1.number().code());
        assertEquals("Error: already taken",result1.message().message());


    }

    @Test
    void joinGameWhiteError() {
        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryUserDao users = new MemoryUserDao();
        MemoryGameDAO games = new MemoryGameDAO();
        GameService gameService = new GameService();
        Generate generate = new Generate();
        String auth = generate.generateAuth(users, auths);
        int id = generate.createGame(auth,games,auths);
        GameService gameService1 = new GameService("WHITE",id);
        assertInstanceOf(UserData.class,gameService1.joinGame(auth,games,auths));
        auth = generate.generateDiffAuth(users,auths);
        gameService1 = new GameService("WHITE",id);
        Object result = gameService1.joinGame(auth,games,auths);
        assertInstanceOf(FullError.class,result);
        FullError result1 = (FullError) result;
        assertEquals(403,result1.number().code());
        assertEquals("Error: already taken",result1.message().message());


    }

}