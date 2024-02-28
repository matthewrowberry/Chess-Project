package dataAccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDao implements UserDAO{
    private Map<String, UserData> users;
    public MemoryUserDao(){
        users = new HashMap<>();
    }

    @Override
    public void createUser(UserData userData) {
        users.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(UserData userData) {
        return users.get(userData.username());
    }

    @Override
    public void clear() {
        users.clear();
    }
}
