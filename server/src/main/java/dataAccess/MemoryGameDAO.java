package dataAccess;

import model.GameData;
import model.GameDataRedacted;

import java.util.*;

public class MemoryGameDAO implements GameDAO {
    Map<Integer,GameData> games;
    public MemoryGameDAO(){
        games = new LinkedHashMap<>();
    }

    @Override
    public void clear() {
        games.clear();
    }

    @Override
    public GameData getGame(int id) {
        return games.get(id);
    }

    /*@Override
    public void deleteGame(int id) {
        games.remove(id);
    }*/

    @Override
    public void updateGame(int id, GameData game) {
        games.put(id,game);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryGameDAO that = (MemoryGameDAO) o;
        return Objects.equals(games, that.games);
    }

    @Override
    public int hashCode() {
        return Objects.hash(games);
    }

    @Override
    public List<GameDataRedacted> getGames() {
        List<GameDataRedacted> export = new ArrayList<>();
        for(Map.Entry<Integer,GameData> game : games.entrySet()){
            GameData gameValue = game.getValue();
            export.addLast(new GameDataRedacted(gameValue.gameID(),gameValue.whiteUsername(),gameValue.blackUsername(),gameValue.gameName()));
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
