package dataAccess;

import Records.User;

public interface UserDAO extends DAO{

    public void createUser(User user);

    public User getUser(User user);
}
