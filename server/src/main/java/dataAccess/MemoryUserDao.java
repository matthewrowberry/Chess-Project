package dataAccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryUserDao that = (MemoryUserDao) o;
        return Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(users);
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
