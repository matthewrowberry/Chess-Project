package passoffTests.serviceTests;

import chess.ChessGame;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDao;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;
import service.DbService;

import static org.junit.jupiter.api.Assertions.*;

class DbServiceTest {

    @Test
    void clearTest() {
        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryGameDAO games = new MemoryGameDAO();
        MemoryUserDao users = new MemoryUserDao();
        MemoryAuthDAO auths1 = new MemoryAuthDAO();
        MemoryGameDAO games1 = new MemoryGameDAO();
        MemoryUserDao users1 = new MemoryUserDao();
        auths.createAuth("user","45678976dhkjsl");
        games.insertGame(new GameData(32,"user","otheruser","name",new ChessGame()));
        users.createUser(new UserData("user","pass","this@that.com"));
        DbService dbService = new DbService();
        dbService.clear(users,auths,games);
        assertEquals(auths1,auths);
        assertEquals(games1,games);
        assertEquals(users1,users);
    }
}