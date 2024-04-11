package ui;

import java.util.Scanner;

public class UI {

    boolean loggedIn,quit,inGame;
    ServerFacade comms;
    public UI() {
        loggedIn = false;
        quit = false;
        inGame = false;
        comms = new ServerFacade();

    }

    public void run(){
        System.out.println("Welcome to 240 chess. Type Help to get started.\n");
        String[] arguments;

        while(true){
            if(loggedIn){
                if(!inGame){
                    System.out.print("[LOGGED_IN] >>> ");
                    arguments = this.getString();
                    switch(arguments[0]){
                        case "create" -> createGame(arguments);
                        case "list" -> listGames();
                        case "join" -> joinGame(arguments);
                        case "observe" -> joinObserver(arguments);
                        case "logout" -> logout();
                        case "quit" -> quit = true;
                        case "Help" -> handleHelp();
                        default -> System.out.println("invalid entry");
                    }
                }
                else{
                System.out.print("[LOGGED_IN] >>> ");
                arguments = this.getString();
                switch(arguments[0]){
                    case "Help" -> handleHelp();
                    case "Redraw" -> listGames();
                    case "Leave" -> joinGame(arguments);
                    case "Make" -> joinObserver(arguments);
                    case "Resign" -> logout();
                    case "Highlight" -> quit = true;
                    default -> System.out.println("invalid entry");
                }
                }
            }
            else{
                System.out.print("[LOGGED_OUT] >>> ");
                arguments = this.getString();
                switch (arguments[0]){
                    case "help" -> handleHelp();
                    case "register" -> register(arguments);
                    case "quit" -> quit = true;
                    case "login" -> login(arguments);
                    default -> System.out.println("invalid entry");
                }


            }
            if(quit){
                break;
            }
        }

    }

    private void handleHelp(){
        if(loggedIn){
            if(inGame){
                System.out.println("Stuff");
            }
            else {
                System.out.println("create <NAME> - a game");
                System.out.println("list - games");
                System.out.println("join <ID> [WHITE|BLACK|<empty>] - a game");
                System.out.println("observe <ID> - a game");
                System.out.println("logout - when you are done");
            }
        }
        else {
            System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
            System.out.println("login <USERNAME> <PASSWORD> - to play chess");

        }
        System.out.println("quit - playing chess");
        System.out.println("help - with possible commands");
    }

    private void sendMessage(String[] args){
        StringBuilder message = new StringBuilder();
        for(int i = 1; i<args.length; i++){
            message.append(args[i]);
        }

    }
    private void register(String[] args){
        if(args.length==4) {
            String result = comms.register(args[1], args[2], args[3]);
            if(result.contains("Logged")){
                loggedIn = true;
            }
            System.out.println(result);

        }
        else{
            System.out.println("Invalid number of arguments");
        }

    }

    private void login(String[] args){
        if(args.length==3) {
            String result = comms.login(args[1], args[2]);
            if(result.contains("Logged")) {
                loggedIn = true;
            }
            System.out.println(result);
        }
        else{
            System.out.println("Invalid number of arguments");
        }

    }

    private void createGame(String[] args){
        if(args.length==2) {
            System.out.println(comms.createGame(args[1]));
        }
        else{
            System.out.println("Invalid number of arguments");
        }

    }

    private void listGames(){

            System.out.println(comms.listGames());


    }

    private void joinGame(String[] args){
        if(args.length==3) {
            try {
                System.out.println(comms.joinGame(args[2], Integer.parseInt(args[1])));
                //inGame = true;
            }
            catch(Exception e){
                System.out.println(e.toString());
                System.out.println("Invalid entry, are the number and string switched?");
            }
        }
        else if(args.length == 2){

            try {
                System.out.println(comms.joinGame("", Integer.parseInt(args[1])));
                //inGame = true;
            } catch (NumberFormatException e) {
                System.out.println("Must be a number");
            }
        }
        else{
            System.out.println("Invalid number of arguments");
        }

    }

    private void joinObserver(String[] args){
        if(args.length==2) {
            System.out.println(comms.joinObserver(Integer.parseInt(args[1])));
        }
        else{
            System.out.println("Invalid number of arguments");
        }

    }

    private void logout(){
        String result = comms.logout();
        System.out.println(result);
        if(result.contains("Logged")) {
            loggedIn = false;
        }
    }

    private String[] getString(){
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] arguments = line.split(" ");

        return arguments;
    }
}
