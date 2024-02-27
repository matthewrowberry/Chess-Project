package dataAccess;

import Records.User;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDao implements UserDAO{
    private Map<String, User> users;
    public MemoryUserDao(){
        users = new HashMap<>();
    }

    @Override
    public void createUser(User user) {
        users.put(user.username(),user);
    }

    @Override
    public User getUser(User user) {
        return users.get(user.username());
    }

    @Override
    public void clear() {
        users.clear();
    }
}
