package dataAccess;

import model.GameData;
import model.GameDataRedacted;

import java.util.*;

public class MemoryGameDAO implements GameDAO {
    Map<Integer,GameData> games;
    public MemoryGameDAO(){
        games = new HashMap<>();
    }

    @Override
    public void clear() {

    }

    @Override
    public GameData getGame(int id) {
        return null;
    }

    @Override
    public void deleteGame(int id) {

    }

    @Override
    public void updateGame(int id, GameData game) {

    }

    @Override
    public List<GameDataRedacted> getGames() {
        List<GameDataRedacted> export = new ArrayList<>();
        for(Map.Entry<Integer,GameData> game : games.entrySet()){
            GameData gameValue = game.getValue();
            export.add(new GameDataRedacted(gameValue.gameID(),gameValue.whiteUsername(),gameValue.blackUsername(),gameValue.gameName()));
        }
        return export;
    }

    @Override
    public int insertGame(GameData game) {

        Random rand = new Random();
        int num;

        do{
            num = rand.nextInt(1000);

        }while(games.get(num)!=null);
        GameData updated = new GameData(num,game.whiteUsername(),game.blackUsername(),game.gameName(), game.game());
        games.put(num,updated);
        return num;
    }
}
