package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessPiece.PieceType.*;
import static ui.EscapeSequences.*;

public class PrintHelper {

    private ChessBoard board;
    private String whiteColor = SET_TEXT_COLOR_WHITE;
    private String blackColor = SET_TEXT_COLOR_BLACK;
    private boolean shaded;

    public PrintHelper() {
        shaded = true;
    }



    public void printBoard(ChessBoard board){
        printBoard(board,true);
    }

    public void printBoard(ChessBoard board, boolean whitePerspective){
        printBoard(board, whitePerspective, new ArrayList<ChessPosition>());
    }
    public void printBoard(ChessBoard board, boolean whitePerspective, Collection<ChessPosition> validMoves){
        System.out.println();
        this.board = board;
        int increment, start,start2;
        if(!whitePerspective){
            increment = 1;
            start = 0;
            start2 = 9;
        }else{
            increment = -1;
            start = 9;
            start2 = 0;
        }
        System.out.print(SET_TEXT_COLOR_WHITE);
        for(int row = start; row<10&&row>=0; row+=increment){

            for(int col = start2; col<10&&col>=0; col-=increment){
                if(col == 0 || col == 9 || row == 0 ||row==9){
                    System.out.print(SET_BG_COLOR_BLACK);
                    if((col == 0||col == 9) && (row == 0 || row == 9)){
                        for(int i = 0; i<14; i++) {
                            System.out.print("\u200A");
                        }
                    }
                    if(col != 0 && col != 9){
                        for(int i = 0; i<7; i++) {
                            System.out.print("\u200A");
                        }
                    }
                    switch(col){
                        case 1 -> System.out.print("A");
                        case 2 -> System.out.print("B");
                        case 3 -> System.out.print("C");
                        case 4 -> System.out.print("D");
                        case 5 -> System.out.print("E");
                        case 6 -> System.out.print("F");
                        case 7 -> System.out.print("G");
                        case 8 -> System.out.print("H");
                        default -> System.out.print("");//System.out.print(EMPTY);
                    }
                    if(col != 0 && col != 9){
                        for(int i = 0; i<7; i++) {
                            System.out.print("\u200A");
                        }
                    }
                    if(row != 0 && row != 9){
                        for(int i = 0; i<4; i++) {
                            System.out.print("\u200A");
                        }
                        System.out.print(row);
                        for(int i = 0; i<4; i++) {
                            System.out.print("\u200A");
                        }

                    }
                }
                else{
                    if(shaded){
                        if(validMoves.contains(new ChessPosition(row,col))){
                            System.out.print(SET_BG_COLOR_DARK_GREEN);
                        }else {
                            System.out.print(SET_BG_COLOR_BLACK);
                        }
                    }
                    else{
                        if(validMoves.contains(new ChessPosition(row,col))) {
                            System.out.print(SET_BG_COLOR_GREEN);
                        }
                        else {
                            System.out.print(SET_BG_COLOR_BLUE);
                        }
                    }
                    shaded = !shaded;
                    printPiece(row,col);
                }
            }
            System.out.println();
            shaded = !shaded;
        }

    }

    private void printPiece(int row, int col){
        ChessPiece piece = board.getPiece(new ChessPosition(row,col));
        if(piece==null){
            System.out.print(EMPTY);
        }
        else{
            System.out.print(printSpecial(piece));


        }
    }

    private String printSpecial(ChessPiece piece){
        if(piece.getTeamColor()== ChessGame.TeamColor.BLACK) {
            return switch (piece.getPieceType()) {
                case KING -> WHITE_KING;
                case BISHOP -> WHITE_BISHOP;
                case QUEEN -> WHITE_QUEEN;
                case KNIGHT -> WHITE_KNIGHT;
                case PAWN -> WHITE_PAWN;
                case ROOK -> WHITE_ROOK;
            };
        }else{
            return switch (piece.getPieceType()) {
                case KING -> BLACK_KING;
                case BISHOP -> BLACK_BISHOP;
                case QUEEN -> BLACK_QUEEN;
                case KNIGHT -> BLACK_KNIGHT;
                case PAWN -> BLACK_PAWN;
                case ROOK -> BLACK_ROOK;
            };
        }
    }
}
