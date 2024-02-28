package passoffTests.serviceTests;

import Records.FullError;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDao;
import model.AuthToken;
import org.junit.jupiter.api.Test;
import service.LoginService;

import static org.junit.jupiter.api.Assertions.*;

class loginServiceTest {


    @Test
    void registerSuccess() {
        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryUserDao users = new MemoryUserDao();
        LoginService loginService = new LoginService("username","password","this@gmail.com");
        Object result = (AuthToken) loginService.register(users,auths);
        assertTrue(result instanceof AuthToken);
        AuthToken result1 = (AuthToken) result;
        assertNotNull(result1.authToken());
        assertEquals(result1.username(),"username");

    }

    @Test
    void registerError() {
        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryUserDao users = new MemoryUserDao();
        LoginService loginService = new LoginService("username",null,"this@gmail.com");
        Object result = loginService.register(users,auths);
        assertNotNull(result);
        assertInstanceOf(FullError.class, result);

        assertEquals(((FullError) result).message().message(),"Error: bad request");
        assertEquals(((FullError) result).number().code(),400);


    }

    @Test
    void login() {
        assertTrue(true);
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