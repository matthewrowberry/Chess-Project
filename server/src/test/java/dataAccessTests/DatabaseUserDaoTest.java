package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.DatabaseUserDao;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseUserDaoTest {
    DatabaseUserDao users;
    @BeforeEach
    void init() throws SQLException, DataAccessException {
        users = new DatabaseUserDao();
        users.clear();
    }
    @Test
    void clear() {
        users.createUser(new UserData("I","dislike","this"));
        assertNotNull(users.getUser(new UserData("I",null,null)));
        users.clear();
        assertNull(users.getUser(new UserData("I",null,null)));
    }

    @Test
    void createUser() {
        users.createUser(new UserData("I","dislike","this"));
        assertEquals(users.getUser(new UserData("I",null,null)),new UserData("I","dislike","this"));

    }

    @Test
    void createUserFail() {
        users.createUser(new UserData("I","dislike","this"));
        assertNotEquals(users.getUser(new UserData("I",null,null)),new UserData("I","dislik","this"));
        assertNotEquals(users.getUser(new UserData("J",null,null)),new UserData("I","dislike","this"));

    }

    @Test
    void getUser() {
        users.createUser(new UserData("I","dislike","this"));
        assertEquals(users.getUser(new UserData("I",null,null)),new UserData("I","dislike","this"));

    }

    @Test
    void getUserFail() {
        users.createUser(new UserData("I","dislike","this"));
        assertNull(users.getUser(new UserData("J",null,null)));

    }
}