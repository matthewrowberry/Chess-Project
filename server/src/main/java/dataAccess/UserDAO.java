package dataAccess;

import model.UserData;

public interface UserDAO extends DAO{

    public void createUser(UserData userData);

    public UserData getUser(UserData userData);
}
