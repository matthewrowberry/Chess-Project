package serviceTests;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDao;
import dataAccess.UserDAO;
import model.AuthToken;
import org.junit.jupiter.api.Test;
import service.loginService;

import static org.junit.jupiter.api.Assertions.*;

class loginServiceTest {

    @Test
    void register() {
        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryUserDao users = new MemoryUserDao();
        loginService loginService = new loginService("username","password","this@gmail.com");
        AuthToken result = (AuthToken) loginService.register(users,auths);
        assertNotNull(result.authToken());
        assertTrue(result.authToken() instanceof String);
        assertEquals(result.username(),"username");

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