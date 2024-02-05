package chess;

import java.util.ArrayList;

public class PossibleMoves {

    ChessPiece.PieceType type;
    ChessGame.TeamColor color;
    int myRow, myCol;
    ChessBoard board;
    ChessPosition position;


    public PossibleMoves() {

    }

    public ArrayList<ChessMove> getMoves(ChessBoard board, ChessPosition position){
        this.board = board;
        color = board.getPiece(position).getTeamColor();
        type = board.getPiece(position).getPieceType();
        myRow = position.getRow();
        myCol = position.getColumn();
        this.position = position;

        return switch(type){
            case KING -> King();
            case QUEEN -> Queen();
            case BISHOP -> Bishop();


            case KNIGHT -> Knight();
            case ROOK -> Rook();
            case PAWN -> Pawn();
        };


    }

    private ArrayList<ChessMove> Bishop(){
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        moves.addAll(getDirection(1,1));
        moves.addAll(getDirection(-1,-1));
        moves.addAll(getDirection(-1,1));
        moves.addAll(getDirection(1,-1));
        return moves;

    }

    private ArrayList<ChessMove> Rook(){
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        moves.addAll(getDirection(1,0));
        moves.addAll(getDirection(-1,0));
        moves.addAll(getDirection(0,1));
        moves.addAll(getDirection(0,-1));

        return moves;

    }

    private ArrayList<ChessMove> Queen(){
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        moves.addAll(getDirection(1,1));
        moves.addAll(getDirection(-1,-1));
        moves.addAll(getDirection(-1,1));
        moves.addAll(getDirection(1,-1));
        moves.addAll(getDirection(1,0));
        moves.addAll(getDirection(-1,0));
        moves.addAll(getDirection(0,1));
        moves.addAll(getDirection(0,-1));
        return moves;

    }

    private ArrayList<ChessMove> Knight(){
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int[][] possibilities = {{2,1},{1,2},{-2,1},{-1,2},{2,-1},{1,-2},{-2,-1},{-1,-2}};
        for(int i[] : possibilities){
            if(inBoundsOffset(i[0],i[1]) && getPieceOffset(i[0],i[1]) == null){
                moves.add(getChessMoveOffset(i[0],i[1]));
            }
            else if(inBoundsOffset(i[0],i[1]) && getPieceOffset(i[0],i[1]).getTeamColor() != color){
                moves.add(getChessMoveOffset(i[0],i[1]));
            }
        }
        return moves;

    }

    private ArrayList<ChessMove> King(){
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int[][] possibilities = {{1,1},{1,0},{1,-1},{0,1},{0,-1},{-1,1},{-1,0},{-1,-1}};

        for(int i[] : possibilities){
            if(inBoundsOffset(i[0],i[1]) && getPieceOffset(i[0],i[1]) == null){
                moves.add(getChessMoveOffset(i[0],i[1]));
            }
            else if(inBoundsOffset(i[0],i[1]) && getPieceOffset(i[0],i[1]).getTeamColor() != color){
                moves.add(getChessMoveOffset(i[0],i[1]));
            }
        }

        if(myRow==1 || myRow==8 && myCol == 5){
            if(getPieceOffset(0,-4)!=null && getPieceOffset(0,-4).getPieceType()==ChessPiece.PieceType.ROOK&&getPieceOffset(0,-1)==null&&getPieceOffset(0,-2)==null){
                moves.add(getChessMoveOffset(0,-2));
            }
            if(getPieceOffset(0,3)!=null && getPieceOffset(0,3).getPieceType()==ChessPiece.PieceType.ROOK&&getPieceOffset(0,1)==null&&getPieceOffset(0,2)==null){
                moves.add(getChessMoveOffset(0,2));
            }

        }


        return moves;
    }

    private ArrayList<ChessMove> Pawn(){
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        moves.addAll(forward());
        moves.addAll(attack());
        return moves;

    }

    private ArrayList<ChessMove> forward(){
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int start, promote, offset;
        if(color == ChessGame.TeamColor.WHITE){
            start = 2;
            promote = 7;
            offset = 1;
        }
        else{
            start = 7;
            promote = 2;
            offset = -1;
        }

        if(myRow==start){
            if(getPieceOffset(offset,0) == null){
                moves.add(getChessMoveOffset(offset,0));
                if(getPieceOffset(2*offset,0) == null){
                    moves.add(getChessMoveOffset(2*offset,0));
                }
            }
        }
        else if(myRow == promote){
            moves.addAll(PromoteOffset(offset,0));
        }
        else{
            if(getPieceOffset(offset,0) == null){
                moves.add(getChessMoveOffset(offset,0));
            }
        }

        return moves;

    }

    private ArrayList<ChessMove> attack() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int start, promote, offset;
        if (color == ChessGame.TeamColor.WHITE) {

            promote = 7;
            offset = 1;
        } else {

            promote = 2;
            offset = -1;
        }

        if (myRow == promote) {
            if (getPieceOffset(offset, 1) != null && getPieceOffset(offset, 1).getTeamColor() != color) {
                moves.addAll(PromoteOffset(offset, 1));
            }
            if (getPieceOffset(offset, -1) != null && getPieceOffset(offset, -1).getTeamColor() != color) {
                moves.addAll(PromoteOffset(offset, -1));
            }

        } else {
            if (getPieceOffset(offset, 1) != null && getPieceOffset(offset, 1).getTeamColor() != color) {
                moves.add(getChessMoveOffset(offset,1));
            }
            if (getPieceOffset(offset, -1) != null && getPieceOffset(offset, -1).getTeamColor() != color) {
                moves.add(getChessMoveOffset(offset,-1));
            }
        }


        return moves;

    }

    private ArrayList<ChessMove> PromoteOffset(int row, int col) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        moves.add(getChessMoveOffset(row,col, ChessPiece.PieceType.QUEEN));
        moves.add(getChessMoveOffset(row,col, ChessPiece.PieceType.ROOK));
        moves.add(getChessMoveOffset(row,col, ChessPiece.PieceType.BISHOP));
        moves.add(getChessMoveOffset(row,col, ChessPiece.PieceType.KNIGHT));

        return moves;
    }

    private ArrayList<ChessMove> getDirection(int row, int col){
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int currentRow = row;
        int currentCol = col;
        while(inBoundsOffset(currentRow,currentCol)&&getPieceOffset(currentRow,currentCol) == null){
            moves.add(getChessMoveOffset(currentRow,currentCol));
            currentRow += row;
            currentCol += col;
        }
        if(inBoundsOffset(currentRow,currentCol)&&getPieceOffset(currentRow,currentCol).getTeamColor()!= color){
            moves.add(getChessMoveOffset(currentRow,currentCol));
        }

        return moves;


    }

    private ChessMove getChessMoveOffset(int row, int col){
        return new ChessMove(position,getPositionOffset(row,col));
    }
    private ChessMove getChessMoveOffset(int row, int col,ChessPiece.PieceType newtype){
        return new ChessMove(position,getPositionOffset(row,col),newtype);
    }

    private ChessPosition getPositionOffset(int row, int col){
        return new ChessPosition(myRow+row,myCol+col);
    }



    private ChessPiece getPieceOffset(int row, int col){
        return board.getPiece(new ChessPosition(myRow+row,myCol+col));
    }



    private boolean inBoundsOffset(int row, int col){
        return myRow+row>0 && myRow+row<9 && myCol+col>0 && myCol+col<9;
    }


}
