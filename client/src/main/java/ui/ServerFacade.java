package ui;

import chess.*;
import com.google.gson.Gson;
import dependencies.AuthToken;
import dependencies.GameDataRedacted;
import dependencies.GameList;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ServerFacade {
    String authtoken;
    Map<Integer, Integer> games;
    PrintHelper printer = new PrintHelper();

    WebSocket webSocket;
    boolean escape;
    ChessGame game;
    ChessBoard board = new ChessBoard();
    String hostport;

    String COLOR;
    int gameID;

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
            this.gameID = game;
        } catch (Exception e) {
            return "invalid game number";
        }

        var body = Map.of("playerColor",color, "gameID", game);

        Object stuff = makeRequest(http, body, null, true);
        if(stuff.equals(200)){
            COLOR = color;

            webSocket(game,true);
            webSocket.setColor(COLOR);


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
            this.gameID = games.get(gameID);
        } catch (Exception e) {
            return"Invalid game Number";
        }

        Object stuff = makeRequest(http, body, null, true);
        if(stuff.equals(200)) {
            webSocket(this.gameID,false);
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



    private void webSocket(int game, boolean player){
        try {
            webSocket = new WebSocket();
            GameID command = new GameID(authtoken,game, UserGameCommand.CommandType.JOIN_PLAYER);
            Gson sender = new Gson();
            if(player){webSocket.setColor(COLOR);}

            webSocket.send(sender.toJson(command));
            String[] request;
            escape = true;
            while(escape){
                System.out.print("[Gameplay] >>> ");
                request = getString();
                if(player) {
                    switch (request[0]) {
                        case "Help" -> help();
                        case "Move" -> move(request);
                        case "Redraw" -> redraw();
                        case "Highlight" -> highlightMoves(request);
                        case "Leave" -> leave();
                        case "resign" -> resign();


                        default -> System.out.println("Invalid Entry");

                    }
                }else{
                    switch (request[0]) {
                        case "Help" -> help();
                        case "Redraw" -> redraw();
                        case "Leave" -> leave();


                        default -> System.out.println("Invalid Entry");

                    }
                }



            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void move(String[] move){
        ChessPosition start = translate(move[1]);
        ChessPosition end = translate(move[2]);


        ChessPiece.PieceType type = null;
        ChessMove chessMove;


        try {
            if(webSocket.getBoard().getPiece(start).getPieceType()== ChessPiece.PieceType.PAWN && (end.getRow() == 8 || end.getRow()==1)){
                System.out.print("Please select promotion - PAWN, ROOK, BISHOP, KNIGHT, QUEEN \n>>");
                String[] promotion = getString();
                while(type == null) {
                    switch (promotion[0]) {
                        case "PAWN" -> type = ChessPiece.PieceType.PAWN;
                        case "ROOK" -> type = ChessPiece.PieceType.ROOK;
                        case "BISHOP" -> type = ChessPiece.PieceType.BISHOP;
                        case "KNIGHT" -> type = ChessPiece.PieceType.KNIGHT;
                        case "QUEEN" -> type = ChessPiece.PieceType.QUEEN;
                        default -> type = null;
                    }
                }

                chessMove = new ChessMove(start,end,type);
            }
            else {
                chessMove = new ChessMove(start,end);

            }
        } catch (Exception e) {

            throw new RuntimeException(e);
        }

        Gson json = new Gson();
        try {
            webSocket.send(json.toJson(new Move(UserGameCommand.CommandType.MAKE_MOVE,authtoken,chessMove,gameID)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private ChessPosition translate(String format){
        int col = 0;
        switch(format.charAt(0)){
            case 'A' -> col = 1;
            case 'B' -> col = 2;
            case 'C' -> col = 3;
            case 'D' -> col = 4;
            case 'E' -> col = 5;
            case 'F' -> col = 6;
            case 'G' -> col = 7;
            case 'H' -> col = 8;

        }

        if(col == 0){
            return null;
        }
        else{
            return new ChessPosition(Integer.parseInt(format.substring(1)),col);
        }
    }

    private void help(){
        System.out.println("Help - get help");
        System.out.println("Redraw - redraw the chess board");
        System.out.println("Leave - leave the game");
        System.out.println("move <begin> <end> - move from one spot to other spot");
        System.out.println("resign - forfeit the game");
        System.out.println("Highlight <location> - highlight moves you can make");
    }
    private void redraw(){
        webSocket.redraw();
    }

    private void leave(){
        Leave command = new Leave(authtoken,UserGameCommand.CommandType.LEAVE,gameID);
        Gson sender = new Gson();


        try {
            webSocket.send(sender.toJson(command));
        } catch (Exception e) {
            System.out.println("The Websocket connection has expired, or the server has gone offline");
        }

        webSocket.end();
        escape = false;
    }

    private void makeMove(){

    }

    private void resign(){
        Resign resign = new Resign(authtoken,gameID);
        Gson json = new Gson();
        try {
            webSocket.send(json.toJson(resign));
            webSocket.resign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void highlightMoves(String[] start){
        ChessPosition starter = translate(start[1]);
        ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>) webSocket.getValidMoves(starter);
        ArrayList<ChessPosition> validEnds = new ArrayList<>();
        for(ChessMove i:validMoves){
            validEnds.add(i.getEndPosition());
        }
        printer.printBoard(webSocket.getBoard(), webSocket.color,validEnds);

    }
}

