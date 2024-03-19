import chess.*;
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
    }
}