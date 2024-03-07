package dataAccess;

import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseAuthDao implements AuthDAO{

    public DatabaseAuthDao(){
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void createAuth(String username, String authToken) {
        try {
            Connection conn = DatabaseManager.getConnection();
            var statement = conn.prepareStatement("INSERT INTO users (authToken, username) VALUES(?,?);");
            statement.setString(1, authToken);
            statement.setString(2, username);

            statement.executeUpdate();
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private final String[] createStatement = {
            """
            CREATE TABLE IF NOT EXISTS  auths (
                          `authToken` varchar(255) NOT NULL,
                          `username` varchar(255) NOT NULL,
                          PRIMARY KEY (`authToken`)
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
    public void deleteAuth(String authToken) {
        try {
            Connection conn = DatabaseManager.getConnection();
            var statement = conn.prepareStatement("DELETE FROM pet WHERE authToken=?");
            statement.setString(1, authToken);

            statement.executeUpdate();

        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getUsername(String auth) {
        try {
            Connection conn = DatabaseManager.getConnection();
            var statement = conn.prepareStatement("SELECT username FROM users WHERE authToken=?");
            statement.setString(1, auth);

            var rs = statement.executeQuery();
            if(rs.next()) {
                return rs.getString("username");
            }else{
                return null;
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserData checkAuth(String authToken) {
        try {
            Connection conn = DatabaseManager.getConnection();
            var statement = conn.prepareStatement("SELECT username FROM users WHERE authToken=?");
            statement.setString(1, authToken);

            var rs = statement.executeQuery();
            if(rs.next()) {
                return new UserData(rs.getString("username"),null,null);
            }else{
                return null;
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() {
        try {
            Connection conn = DatabaseManager.getConnection();
            var statement = conn.prepareStatement("TRUNCATE auths");

            statement.executeUpdate();
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
