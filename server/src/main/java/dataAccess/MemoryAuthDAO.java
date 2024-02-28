package dataAccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO{
    Map<String, String> auths;
    public MemoryAuthDAO(){
        auths = new HashMap<>();
    }


    @Override
    public void createAuth(String username,String authToken) {

        auths.put(authToken,username);
    }

    @Override
    public void deleteAuth(String authToken) {
        auths.remove(authToken);
    }

    @Override
    public String getUsername(String auth) {
        return auths.get(auth);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryAuthDAO that = (MemoryAuthDAO) o;
        return Objects.equals(auths, that.auths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(auths);
    }

    @Override
    public UserData checkAuth(String authToken) {
        return new UserData(auths.get(authToken),null,null);
    }

    @Override
    public void clear() {
        auths.clear();
    }
}
