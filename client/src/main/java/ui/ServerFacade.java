package ui;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import dependencies.AuthToken;
import dependencies.GameDataRedacted;
import dependencies.GameList;
import webSocketMessages.userCommands.GameID;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ServerFacade {
    String authtoken;
    Map<Integer, Integer> games;
    PrintHelper printer = new PrintHelper();

    WebSocket webSocket;

    ChessGame game;
    ChessBoard board = new ChessBoard();
    String hostport;

    public ServerFacade() {
        this(8080);
    }

    public ServerFacade(int port){
        hostport = Integer.toString(port);
        games = new HashMap<>();
        board.resetBoard();


    }

    private HttpURLConnection setup(String url, String requestMethod){
        URI uri = null;
        try {
            uri = new URI("http://localhost:"+hostport+"/"+url);
        } catch (URISyntaxException e) {
            System.out.println("URL Problem");

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

    private void printBoard(ChessBoard board){
        printer.printBoard(board);
        System.out.println();
        printer.printBoard(board,false);
    }

    public String register(String username, String password, String email){
        HttpURLConnection http = setup("user","POST");

// Write out the body
        var body = Map.of("username", username, "password", password,"email",email);
        Object prestuff = makeRequest(http, body, AuthToken.class, true);
        if(! (prestuff instanceof Integer)) {
            AuthToken stuff = (AuthToken) prestuff;
            authtoken = stuff.authToken();
            return "Logged in as " + stuff.username();


        }
        else if(prestuff.equals(400)){
            return "Error: bad request";
        }
        else if(prestuff.equals(403)){
            return "Error: already taken";
        }
        else{
            return "Error: description";
        }




    }


    private Object makeRequest(HttpURLConnection http, Map body, Type type, boolean parameters){

        if(parameters) {
            try (var outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(body);
                outputStream.write(jsonBody.getBytes());
            } catch (IOException e) {
                System.out.println("request parameters not initialized");
            }
        }

        // Make the request
        try {
            http.connect();
        } catch (IOException e) {
            System.out.println("hi");
            System.out.println(e.toString());
        }

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            Object stuff;
            if(type != null) {
                stuff = new Gson().fromJson(inputStreamReader, type);
            }
            else{
                stuff = 200;
            }

            return stuff;


        }catch (Exception E){

            int respBody;
            try {
                respBody = http.getResponseCode();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }



            return respBody;
        }


    }

    public String login(String username, String password){

        HttpURLConnection http = setup("session","POST");


// Write out the body
        var body = Map.of("username", username, "password", password);

        Object stuff = makeRequest(http, body, AuthToken.class, true);
        if(stuff instanceof AuthToken) {
            AuthToken auth = (AuthToken) stuff;
            authtoken = auth.authToken();
            return "Logged in as " + auth.username();

        }
        else if(stuff.equals(400)){
            return "Error: bad request";
        }
        else if(stuff.equals(401)){
            return "Error: unauthorized";
        }
        else if(stuff.equals(403)){
            return "Error: already taken";
        }
        else{
            return "Error: description";
        }

    }

    public String logout(){
        String result = "";
        HttpURLConnection http = setup("session","DELETE");

        Object stuff = makeRequest(http, null, null, false);
        if(stuff.equals(200)){
            authtoken="";
            result += "Logged out";
        }
        else if (stuff.equals(401)){
            result += "Error: unauthorized";
        }
        else{
            result += "Error: Description";
        }

        return result;
    }

    public String createGame(String gameName){
        String result = "";
        HttpURLConnection http = setup("game","POST");



// Write out the body

        Map<String, String> body = null;
        try {
            body = Map.of("gameName", gameName);
        } catch (Exception e) {
            return "Game name cannot be null";
        }
        Object prestuff = makeRequest(http, body, GameDataRedacted.class, true);
        if(! (prestuff instanceof Integer)) {
            GameDataRedacted stuff = (GameDataRedacted) prestuff;
            result = "Game with name of \"" + gameName + "\" created";
        }
        else if(prestuff.equals(400)){
            result = "Error: bad request";
        }
        else if(prestuff.equals(401)){
            result = "Error: unauthorized";
        }
        else{
            result = "Error: description";
        }


        return result;
    }

    public String listGames(){
        String result = "";
        HttpURLConnection http = setup("game","GET");



// Write out the body

        Object prestuff = makeRequest(http, null, GameList.class, false);

        if(! (prestuff instanceof Integer)) {
            GameList stuff = (GameList) prestuff;
            GameDataRedacted game;
            for (int i = 1; i <= stuff.games().size(); i++) {
                game = stuff.games().get(i - 1);
                games.put(i, game.gameID());
                result += i + ") ";
                result += game.gameName()+"\n";
                result += "\tWhite team: " + game.whiteUsername()+"\n";
                result += "\tBlack team: " + game.blackUsername()+"\n";

            }
        }
        else if(prestuff.equals(401)){
            result = "Error: unauthorized";
        }
        else {
            result = "Error: description";
        }

        return result;
    }

    public String joinGame(String color, int gameID){


        //"playerColor":"WHITE/BLACK", "gameID": 1234
        HttpURLConnection http = setup("game","PUT");



// Write out the body

        int game;

        try {
            game = games.get(gameID);
        } catch (Exception e) {
            return "invalid game number";
        }

        var body = Map.of("playerColor",color, "gameID", game);

        Object stuff = makeRequest(http, body, null, true);
        if(stuff.equals(200)){
            webSocket(game);

            printBoard(board);
            return "";
        }
        else if(stuff.equals(400)) {
            return "Error: bad request";

        }
        else if(stuff.equals(401)){
            return "Error: unauthorized";
        }
        else if(stuff.equals(403)){
            return "Error: already taken";
        }
        else{
            return "Error: description";
        }







    }

    public String joinObserver( int gameID){

        HttpURLConnection http = setup("game","PUT");



// Write out the body

        Map<String, Integer> body = null;
        try {
            body = Map.of("gameID", games.get(gameID));
        } catch (Exception e) {
            return"Invalid game Number";
        }

        Object stuff = makeRequest(http, body, null, true);
        if(stuff.equals(200)) {
            printBoard(board);
            return "";
        }
        else if(stuff.equals(400)) {
            return "Error: bad request";

        }
        else if(stuff.equals(401)){
            return "Error: unauthorized";
        }
        else if(stuff.equals(403)){
            return "Error: already taken";
        }
        else{
            return "Error: description";
        }


    }

    private String[] getString(){
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] arguments = line.split(" ");

        return arguments;
    }



    private void webSocket(int game){
        try {
            webSocket = new WebSocket();
            GameID command = new GameID(authtoken,game, UserGameCommand.CommandType.JOIN_PLAYER);
            Gson sender = new Gson();


            webSocket.send(sender.toJson(command));
            String[] request;
            boolean escape = true;
            while(escape){
                System.out.print("[Gameplay] >>> ");
                request = getString();
                switch(request[0]){
                    case "Help" -> System.out.println("hehe");
                    case "Leave" -> escape = false;
                    default -> System.out.println("Invalid Entry");

                }



            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

