import chess.*;
import ui.Communicator;
import ui.PrintHelper;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        PrintHelper printer = new PrintHelper();

        ChessGame game = new ChessGame();
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        printer.printBoard(board);
        System.out.println();
        printer.printBoard(board,false);
        Communicator comms = new Communicator();
        try{
        comms.register("user4","user1","user2");

        }catch(Exception e){
            System.out.println(e.toString());
        }

        try{
            comms.logout();

        }catch(Exception e){
            System.out.println(e.toString());
        }

        try{
            comms.login("user4","user1");

        }catch(Exception e){
            System.out.println(e.toString());
        }




        comms.createGame("testGame");

        comms.listGames();

        comms.joinGame("WHITE",188);

        comms.joinObserver(197);
    }
}