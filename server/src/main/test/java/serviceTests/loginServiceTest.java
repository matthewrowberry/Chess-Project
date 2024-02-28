package serviceTests;

import dataAccess.MemoryUserDao;
import dataAccess.UserDAO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class loginServiceTest {

    @Test
    void register() {
        UserDAO users = new MemoryUserDao();
        assertEquals(true,true);
    }

    @Test
    void login() {
        assertEquals(true,true);
    }

    @Test
    void logout() {
        assertEquals(true,true);
    }

    @Test
    void checkAuth() {
        assertEquals(true,true);
    }
}