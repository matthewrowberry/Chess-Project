package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class PrintHelper {

    private ChessBoard board;
    private String whiteColor = SET_TEXT_COLOR_WHITE;
    private String blackColor = SET_TEXT_COLOR_BLACK;

    public PrintHelper() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    }

    public void test(){
        System.out.println("hi");
        System.out.print("\u001b[35;100m");
        System.out.println("again");
    }

    public void printBoard(ChessBoard board){
        this.board = board;
        for(int row = 0; row<10; row++){

            for(int col = 0; col<10; col++){
                if(col == 0 || col == 9 || row == 0 ||row==9){

                }
                else{

                }
            }
            System.out.println("");
        }

    }

    private void printPiece(int row, int col){
        ChessPiece piece = board.getPiece(new ChessPosition(row,col));
        if(piece==null){
            System.out.print(EMPTY);
        }
        else if(Character.isUpperCase(piece.toString().charAt(0))){
            System.out.print(whiteColor);


        }
    }

    private String printSpecial(ChessPiece piece){
        return switch (piece.getPieceType()) {
            case KING -> "K";
            case BISHOP -> "B";
            case QUEEN -> "Q";
            case KNIGHT -> "N";
            case PAWN -> "P";
            case ROOK -> "R";
        };
    }
}
