package dataAccess;

import java.sql.SQLException;
import java.util.Arrays;

public class DatabaseUserDao {
    DatabaseManager database;
    public DatabaseUserDao() throws DataAccessException, SQLException {

        configureDatabase();



    }


    private final String[] createStatement = {
            """
            CREATE TABLE IF NOT EXISTS  users (
              `username` varchar NOT NULL AUTO_INCREMENT,
              `password` varchar(255) NOT NULL,
              `email` ENUM('CAT', 'DOG', 'FISH', 'FROG', 'ROCK') DEFAULT 'CAT',
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`username`),
            )
            """
    };


    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatement) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }




}
