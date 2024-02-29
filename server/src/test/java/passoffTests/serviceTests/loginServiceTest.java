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
    void doubleRegisterFail() {
        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryUserDao users = new MemoryUserDao();
        LoginService loginService = new LoginService("username", "password", "this@gmail.com");
        Object result = (AuthToken) loginService.register(users, auths);
        assertTrue(result instanceof AuthToken);
        AuthToken result1 = (AuthToken) result;
        assertNotNull(result1.authToken());
        assertEquals(result1.username(), "username");

        result = loginService.register(users, auths);
        assertInstanceOf(FullError.class, result);
        FullError result2 = (FullError) result;
        assertEquals(((FullError) result2).message().message(), "Error: already taken");
        assertEquals(((FullError) result2).number().code(), 403);
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

        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryUserDao users = new MemoryUserDao();
        LoginService loginService = new LoginService("username","password","this@gmail.com");
        loginService.register(users,auths);


        Object result =  loginService.login(users,auths);
        assertInstanceOf(AuthToken.class,result);
        AuthToken result1 = (AuthToken) result;
        assertNotNull( result1.authToken());
        assertEquals("username",result1.username());

    }

    @Test
    void loginError() {

        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryUserDao users = new MemoryUserDao();
        LoginService loginService = new LoginService("username","password","this@gmail.com");
        loginService.register(users,auths);

        loginService = new LoginService("username","password1");

        Object result =  loginService.login(users,auths);
        assertInstanceOf(FullError.class,result);
        FullError result1 = (FullError) result;
        assertEquals(401,result1.number().code());
        assertEquals("Error: unauthorized",result1.message().message());

    }

    @Test
    void logout() {
        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryUserDao users = new MemoryUserDao();
        LoginService loginService = new LoginService("username","password","this@gmail.com");
        AuthToken authToken = (AuthToken) loginService.register(users,auths);
        String auth = authToken.authToken();

        assertTrue(loginService.checkAuth(auth,auths));

        loginService.logout(auths, auth);
        assertFalse(loginService.checkAuth(auth,auths));


    }

    @Test
    void logoutError() {

        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryUserDao users = new MemoryUserDao();
        LoginService loginService = new LoginService("username","password","this@gmail.com");


        assertInstanceOf(FullError.class,loginService.logout(auths, "hi"));

    }

    @Test
    void checkAuth() {
        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryUserDao users = new MemoryUserDao();
        LoginService loginService = new LoginService("username","password","this@gmail.com");
        AuthToken authToken = (AuthToken) loginService.register(users,auths);
        String auth = authToken.authToken();

        assertTrue(loginService.checkAuth(auth,auths));

    }

    @Test
    void checkWrongAuth() {
        MemoryAuthDAO auths = new MemoryAuthDAO();
        MemoryUserDao users = new MemoryUserDao();
        LoginService loginService = new LoginService("username","password","this@gmail.com");
        AuthToken authToken = (AuthToken) loginService.register(users,auths);
        String auth = authToken.authToken();

        assertFalse(loginService.checkAuth("hello world",auths));
    }
}