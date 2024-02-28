package dataAccess;

import model.UserData;

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
    public String getUsername(String auth) {
        return auths.get(auth);
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
