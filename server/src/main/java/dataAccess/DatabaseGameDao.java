package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.GameDataRedacted;
import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DatabaseGameDao implements GameDAO{

    Gson parser;
    public DatabaseGameDao(){
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        parser = new Gson();
    }

    private final String[] createStatement = {
            """
            CREATE TABLE IF NOT EXISTS  games (
                          `gameID` int NOT NULL,
                          `whiteUsername` varchar(255),
                          `blackUsername` varchar(255),
                          `gameName` varchar(255),
                          `game` text,
                          PRIMARY KEY (`gameID`)
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
        DatabaseManager.executeUpdate("TRUNCATE games");
    }

    @Override
    public GameData getGame(int id) {
        try (Connection conn = DatabaseManager.getConnection();){

            try(var statement = conn.prepareStatement("SELECT * FROM games WHERE gameID=?");) {
                statement.setInt(1, id);

                try(var rs = statement.executeQuery();) {
                    if (rs.next()) {
                        return new GameData(rs.getInt("gameID"),rs.getString("whiteUsername"),rs.getString("blackUsername"),rs.getString("gameName"),parser.fromJson(rs.getString("game"), ChessGame.class));
                    } else {
                        return null;
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateGame(int id, GameData game) {
        DatabaseManager.executeUpdate("DELETE FROM games WHERE gameID=?",id);
        insertGame(id,game);
    }

    @Override
    public List<GameDataRedacted> getGames() {
        List<GameDataRedacted> games = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();){

            try(var statement = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName FROM games");) {


                try(var rs = statement.executeQuery();) {
                    while(rs.next()) {
                        games.add(new GameDataRedacted(rs.getInt("gameID"),rs.getString("whiteUsername"),rs.getString("blackUsername"),rs.getString("gameName")));
                    }
                }
            }
            return games;
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public int insertGame(GameData game){
        Random rand = new Random();
        int num;

        do{
            num = rand.nextInt(1000);


        }while(getGame(num)!=null);
        return insertGame(num,game);
    }

    private int insertGame(int num,GameData game) {


        DatabaseManager.executeUpdate("INSERT INTO games (gameID, whiteUsername, blackUsername,gameName,game) VALUES(?,?,?,?,?);",num,game.whiteUsername(),game.blackUsername(),game.gameName(),game.game());
        return num;
    }
}
