package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.DatabaseAuthDao;
import dataAccess.DatabaseUserDao;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseAuthDaoTest {
DatabaseAuthDao auths;
    @BeforeEach
    void init() throws SQLException, DataAccessException {
        auths = new DatabaseAuthDao();
        auths.clear();
    }
    @Test
    void createAuth() {
        auths.createAuth("ugh","no");
        assertEquals(auths.getUsername("no"),"ugh");
    }
    @Test
    void createAuthFail() {
        assertThrows(RuntimeException.class, () -> {
            auths.createAuth(null, null);
        });
    }

    @Test
    void deleteAuth() {
        auths.createAuth("ugh","no");
        assertEquals(auths.getUsername("no"),"ugh");
        auths.deleteAuth("no");
        assertNull(auths.getUsername("no"));
    }

    @Test
    void deleteAuthFail(){
        auths.createAuth("ugh","no");
        assertEquals(auths.getUsername("no"),"ugh");
        auths.deleteAuth("yes");
        assertNotNull(auths.getUsername("no"));
    }

    @Test
    void getUsername() {
        auths.createAuth("ugh","no");
        assertEquals(auths.getUsername("no"),"ugh");
    }

    @Test
    void getUsernameFail() {
        auths.createAuth("ugh","no");
        assertNotEquals(auths.getUsername("hi"),"ugh");
    }

    @Test
    void checkAuth() {
        auths.createAuth("ugh","no");
        assertEquals(auths.checkAuth("no"),new UserData("ugh",null,null));
    }

    @Test
    void checkAuthFail() {
        auths.createAuth("ugh","no");
        assertNotEquals(auths.checkAuth("non"),new UserData("ugh",null,null));
    }

    @Test
    void clear() {
        auths.createAuth("ugh","no");
        assertEquals(auths.getUsername("no"),"ugh");
        auths.clear();
        assertNull(auths.getUsername("no"));

    }
}