package dataAccess;

import Records.AuthToken;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO{
    Map<String, String> auths;
    public MemoryAuthDAO(){
        auths = new HashMap<>();
    }


    @Override
    public void createAuth(String username,String authToken) {
        auths.put(username,authToken);
    }

    @Override
    public void deleteAuth(AuthToken authToken) {

    }

    @Override
    public void clear() {
        auths.clear();
    }
}
