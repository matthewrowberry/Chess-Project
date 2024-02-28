package dataAccess;

import model.UserData;

public interface AuthDAO extends DAO{

    public void createAuth(String username,String authToken);

    public void deleteAuth(String authToken);

    public UserData checkAuth(String authToken);
}
