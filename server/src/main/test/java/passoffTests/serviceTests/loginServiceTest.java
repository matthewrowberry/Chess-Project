package passoffTests.serviceTests;

import dataAccess.MemoryUserDao;
import dataAccess.UserDAO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class loginServiceTest {

    @Test
    void register() {
        UserDAO users = new MemoryUserDao();

    }

    @Test
    void login() {
    }

    @Test
    void logout() {
    }

    @Test
    void checkAuth() {
    }
}