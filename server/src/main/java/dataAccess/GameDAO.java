package dataAccess;

import model.GameData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface GameDAO extends DAO {
    public GameData getGame(int id);


    public void deleteGame(int id);

    public void updateGame(int id,GameData game);

    public List<GameData> getGames();
    public int insertGame(GameData game);
}
