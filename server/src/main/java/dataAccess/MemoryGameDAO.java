package dataAccess;

import model.GameData;

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
    public List<GameData> getGames() {
        List<GameData> export = new ArrayList<>();
        for(Map.Entry<Integer,GameData> game : games.entrySet()){
            export.add(game.getValue());
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
