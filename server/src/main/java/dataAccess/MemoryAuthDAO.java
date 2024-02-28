package dataAccess;

import Records.AuthToken;
import Records.User;

import java.util.HashMap;
import java.util.Map;

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
    public User checkAuth(String authToken) {
        return new User(auths.get(authToken),null,null);
    }

    @Override
    public void clear() {
        auths.clear();
    }
}
