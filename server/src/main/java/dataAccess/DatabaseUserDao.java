package dataAccess;

import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

public class DatabaseUserDao implements UserDAO{

    public DatabaseUserDao() throws DataAccessException, SQLException {

        configureDatabase();



    }


    private final String[] createStatement = {
            """
            CREATE TABLE IF NOT EXISTS  users (
                          `username` varchar(255) NOT NULL,
                          `password` varchar(255) NOT NULL,
                          `email` varchar(255) NOT NULL,
                          PRIMARY KEY (`username`)
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


    @Override
    public void clear() {
        try {
            Connection conn = DatabaseManager.getConnection();
            var statement = conn.prepareStatement("TRUNCATE users");

            statement.executeUpdate();
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUser(UserData userData) {
        try {
            Connection conn = DatabaseManager.getConnection();
            var statement = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES(?,?,?);");
            statement.setString(1, userData.username());
            statement.setString(2, userData.password());
            statement.setString(3, userData.email());
            statement.executeUpdate();
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public UserData getUser(UserData userData) {
        try {
            Connection conn = DatabaseManager.getConnection();
            var statement = conn.prepareStatement("SELECT username, password, email FROM users WHERE username=?");
            statement.setString(1, userData.username());

            var rs = statement.executeQuery();

            if(rs.next()) {
                return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
            }else{
                return null;
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
