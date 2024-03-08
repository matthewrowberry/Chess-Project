package dataAccessTests;

import chess.*;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseAuthDao;
import dataAccess.DatabaseGameDao;
import model.GameData;
import model.GameDataRedacted;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseGameDaoTest {
    DatabaseGameDao games;
    ChessGame game;
    @BeforeEach
    void init() throws SQLException, DataAccessException {
        games = new DatabaseGameDao();
        games.clear();
        game = new ChessGame();
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        game.setBoard(board);

    }

    @Test
    void clear() {
        int id = games.insertGame(new GameData(1,"yolo","ugh","wut",game));
        assertNotNull(games.getGame(id));
        games.clear();
        assertNull(games.getGame(id));
    }

    @Test
    void getGame() {
        int id = games.insertGame(new GameData(1,"yolo","ugh","wut",game));
        assertEquals(new GameData(id,"yolo","ugh","wut",game),games.getGame(id));

    }
    @Test
    void getGameFail() {
        int id = games.insertGame(new GameData(1,"yolo","ugh","wut",game));
        assertNotEquals(new GameData(0,"yolo","ugh","wut",null),games.getGame(id));

    }

    @Test
    void updateGame() {
        int id = games.insertGame(new GameData(1,"yolo","ugh","wut",game));
        ChessGame other = new ChessGame();
        ChessGame game1 = new ChessGame();
        ChessBoard board2 = new ChessBoard();
        board2.resetBoard();
        game1.setBoard(board2);
        try {
            game1.makeMove(new ChessMove(new ChessPosition(2,2),new ChessPosition(3,2)));
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
        try {
            game.makeMove(new ChessMove(new ChessPosition(2,2),new ChessPosition(3,2)));
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }

        Gson json = new Gson();

        games.updateGame(id,new GameData(id,"yolo","ugh","wut",game));

        assertEquals(new GameData(id,"yolo","ugh","wut",game1),games.getGame(id));


    }

    @Test
    void updateGameFail() {
        int id = games.insertGame(new GameData(1,"yolo","ugh","wut",game));
        ChessGame other = new ChessGame();
        ChessGame game1 = new ChessGame();
        ChessBoard board2 = new ChessBoard();
        board2.resetBoard();
        game1.setBoard(board2);

        try {
            game.makeMove(new ChessMove(new ChessPosition(2,2),new ChessPosition(3,2)));
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
        System.out.println(game.toString());
        Gson json = new Gson();
        System.out.println(json.toJson(game));
        games.updateGame(id,new GameData(id,"yolo","ugh","wut",game));

        assertNotEquals(new GameData(id,"yolo","ugh","wut",game1),games.getGame(id));


    }

    @Test
    void getGames() {
        int id1 = games.insertGame(new GameData(1,"yolo","ugh","wut",game));
        int id2 = games.insertGame(new GameData(1,"yolo","ugh","wut",game));
        ArrayList<GameDataRedacted> list = new ArrayList<>();
        list.add(new GameDataRedacted(id1,"yolo","ugh","wut"));
        list.add(new GameDataRedacted(id2,"yolo","ugh","wut"));
        Collections.sort(list, Comparator.comparingInt(GameDataRedacted::gameID));

        assertEquals(games.getGames(),list);
    }

    @Test
    void getGamesFail() {
        int id1 = games.insertGame(new GameData(1,"yolo","ugh","wut",game));
        int id2 = games.insertGame(new GameData(1,"yolo","ugh","wut",game));
        ArrayList<GameDataRedacted> list = new ArrayList<>();
        list.add(new GameDataRedacted(id1,"yolo","ugh","wut"));
        assertNotEquals(games.getGames(),list);
    }

    @Test
    void insertGame() {
        int id = games.insertGame(new GameData(1,"yolo","ugh","wut",game));
        assertEquals(new GameData(id,"yolo","ugh","wut",game),games.getGame(id));

    }

    @Test
    void InsertGameFail() {
        int id = games.insertGame(new GameData(1,"yolo","ugh","wut",game));
        assertNotEquals(new GameData(id,"yolo1","ugh","wut",game),games.getGame(id));

    }
}