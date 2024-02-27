package dataAccess;

import Records.AuthToken;

public interface AuthDAO extends DAO{

    public void createAuth(String username,String authToken);

    public void deleteAuth(AuthToken authToken);
}
