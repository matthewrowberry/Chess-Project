package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import dependencies.AuthToken;
import dependencies.GameDataRedacted;
import dependencies.GameList;
import dependencies.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.*;
import java.util.Map;

public class Communicator {
    String authtoken;
    public Communicator() {

    }

    private HttpURLConnection setup(String url, String requestMethod){
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        HttpURLConnection http = null;

        try {
            http = (HttpURLConnection) uri.toURL().openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            http.setRequestMethod(requestMethod);
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }

        http.setDoOutput(true);

// Write out a header
        http.addRequestProperty("Content-Type", "application/json");
        http.addRequestProperty("authorization", authtoken);

        return http;
    }

    public String register(String username, String password, String email){
        HttpURLConnection http = setup("http://localhost:8080/user","POST");

// Write out the body
        var body = Map.of("username", username, "password", password,"email",email);
        AuthToken stuff = (AuthToken) makeRequest(http, body, AuthToken.class, true);
        authtoken = stuff.authToken();
        System.out.println(authtoken);
        return null;
    }


    public Object makeRequest(HttpURLConnection http, Map body, Type type, boolean parameters){

        if(parameters) {
            try (var outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(body);
                outputStream.write(jsonBody.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Make the request
        try {
            http.connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            System.out.println(inputStreamReader);
            Object stuff = new Gson().fromJson(inputStreamReader, type);
            return stuff;
        }catch (Exception E){
            System.out.println(E.toString());
        }

        return null;
    }

    public String login(String username, String password) throws IOException {

        HttpURLConnection http = setup("http://localhost:8080/session","POST");


// Write out the body
        var body = Map.of("username", username, "password", password);

        Object stuff = makeRequest(http, body, AuthToken.class, true);

        AuthToken auth = (AuthToken) stuff;
        authtoken = auth.authToken();
        System.out.println(auth.authToken());
        return null;
    }

    public String logout(){

        HttpURLConnection http = setup("http://localhost:8080/session","DELETE");

        Object stuff = makeRequest(http, null, null, false);
        authtoken="";

        return null;
    }

    public String createGame(String gameName){

        HttpURLConnection http = setup("http://localhost:8080/game","POST");



// Write out the body
        var body = Map.of("gameName", gameName);

        GameDataRedacted stuff = (GameDataRedacted) makeRequest(http, body, GameDataRedacted.class, true);

        System.out.println(stuff);


        return null;
    }

    public String listGames(){
        HttpURLConnection http = setup("http://localhost:8080/game","GET");



// Write out the body


        GameList stuff = (GameList) makeRequest(http, null, GameList.class, false);

        System.out.println(stuff);
        return null;
    }

    public String joinGame(String color, int gameID){


        //"playerColor":"WHITE/BLACK", "gameID": 1234
        HttpURLConnection http = setup("http://localhost:8080/game","PUT");



// Write out the body
        var body = Map.of("playerColor",color, "gameID", gameID);

        makeRequest(http, body, null, true);

        return null;
    }

    public String joinObserver( int gameID){

        HttpURLConnection http = setup("http://localhost:8080/game","PUT");



// Write out the body
        var body = Map.of("gameID", gameID);

        makeRequest(http, body, null, true);

        return null;
    }
}
