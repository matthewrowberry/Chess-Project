package dataAccess;

public class DatabaseGameDao {
    public DatabaseGameDao(){
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
