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
        DatabaseManager.executeUpdate("TRUNCATE users");

    }

    @Override
    public void createUser(UserData userData) {
        DatabaseManager.executeUpdate("INSERT INTO users (username, password, email) VALUES(?,?,?);",userData.username(),userData.password(),userData.email());


    }

    @Override
    public UserData getUser(UserData userData) {
        try (Connection conn = DatabaseManager.getConnection();){

            try(var statement = conn.prepareStatement("SELECT username, password, email FROM users WHERE username=?");) {
                statement.setString(1, userData.username());

                try(var rs = statement.executeQuery();) {

                    if (rs.next()) {
                        return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                    } else {
                        return null;
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
