package ui;

import java.util.Scanner;

public class UI {

    boolean loggedIn,quit;
    Communicator comms;
    public UI() {
        loggedIn = false;
        quit = false;
        comms = new Communicator();

    }

    public void run(){
        System.out.println("Welcome to 240 chess. Type Help to get started.\n");
        String[] arguments;

        while(true){
            if(loggedIn){
                System.out.println("[LOGGED_IN] >>> ");
                arguments = this.getString();
                switch(arguments[0]){
                    case "create" -> createGame(arguments);
                    case "list" -> listGames();
                    case "join" -> joinGame(arguments);
                    case "observe" -> joinObserver(arguments);
                    case "logout" -> logout();
                    case "quit" -> quit = true;
                    case "help" -> handleHelp();
                    default -> System.out.println("invalid entry");
                }
            }
            else{
                System.out.println("[LOGGED_OUT] >>> ");
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
            System.out.println("create <NAME> - a game");
            System.out.println("list - games");
            System.out.println("join <ID> [WHITE|BLACK|<empty>] - a game");
            System.out.println("observe <ID> - a game");
            System.out.println("logout - when you are done");
        }
        else {
            System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
            System.out.println("login <USERNAME> <PASSWORD> - to play chess");

        }
        System.out.println("quit - playing chess");
        System.out.println("help - with possible commands");
    }


    private void register(String[] args){
        if(args.length==4) {
            comms.register(args[1], args[2], args[3]);
        }
        else{
            System.out.println("Invalid number of arguments");
        }

    }

    private void login(String[] args){
        if(args.length==3) {
            comms.login(args[1], args[2]);
            loggedIn = true;
        }
        else{
            System.out.println("Invalid number of arguments");
        }

    }

    private void createGame(String[] args){
        if(args.length==2) {
            comms.createGame(args[1]);
        }
        else{
            System.out.println("Invalid number of arguments");
        }

    }

    private void listGames(){

            comms.listGames();


    }

    private void joinGame(String[] args){
        if(args.length==3) {
            comms.joinGame(args[2], Integer.parseInt(args[1]));
        }
        else{
            System.out.println("Invalid number of arguments");
        }

    }

    private void joinObserver(String[] args){
        if(args.length==2) {
            comms.joinObserver(Integer.parseInt(args[1]));
        }
        else{
            System.out.println("Invalid number of arguments");
        }

    }

    private void logout(){
        comms.logout();
        loggedIn = false;
    }

    private String[] getString(){
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] arguments = line.split(" ");

        return arguments;
    }
}
