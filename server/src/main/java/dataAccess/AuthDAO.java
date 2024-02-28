package dataAccess;

import Records.AuthToken;
import Records.User;

public interface AuthDAO extends DAO{

    public void createAuth(String username,String authToken);

    public void deleteAuth(String authToken);

    public User checkAuth(String authToken);
}
