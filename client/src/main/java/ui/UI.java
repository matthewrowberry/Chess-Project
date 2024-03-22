package ui;

import java.util.Scanner;

public class UI {

    boolean loggedIn,quit;
    public UI() {
        loggedIn = false;
        quit = false;

    }

    public void run(){
        System.out.println("Welcom to 240 chess. Type Help to get started.\n");


        while(true){
            if(loggedIn){

            }
            else{
                System.out.println("[LOGGED_OUT] >>> ");
                String[] arguments = this.getString();
                switch (arguments[0]){
                    case "help" -> handleHelp();


                    default -> System.out.println("invalid entry");
                }


            }
            if(quit){
                break;
            }
        }

    }

    private void handleHelp(){
        System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
        System.out.println("login <USERNAME> <PASSWORD> - to play chess");
        System.out.println("quit - playing chess");
        System.out.println("help - with possible commands");
    }

    private String[] getString(){
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] arguments = line.split(" ");

        return arguments;
    }
}
