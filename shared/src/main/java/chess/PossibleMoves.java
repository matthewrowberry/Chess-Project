package chess;

import java.util.ArrayList;

public class PossibleMoves {
    ChessPiece.PieceType type;
    ChessBoard board;
    ChessPosition myPosition;
    public PossibleMoves(ChessPiece.PieceType type){
        this.type = type;
    }

    public ArrayList<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition){
        this.board = board;
        this.myPosition = myPosition;

        return switch(type){
            case ROOK -> rook();
            case KING -> king();
            case QUEEN -> queen();
            case BISHOP -> bishop();
            case KNIGHT -> knight();
            case PAWN -> pawn();
        };

    }

    private ArrayList<ChessMove> rook(){
        ArrayList<ChessMove> combined = checkDirection(1,0);
        combined.addAll(checkDirection(0,1));
        combined.addAll(checkDirection(-1,0));
        combined.addAll(checkDirection(0,-1));
        return combined;
    }

    private ArrayList<ChessMove> bishop(){
        ArrayList<ChessMove> combined = checkDirection(1,1);
        combined.addAll(checkDirection(-1,1));
        combined.addAll(checkDirection(-1,-1));
        combined.addAll(checkDirection(1,-1));
        return combined;
    }

    private ArrayList<ChessMove> queen(){
        ArrayList<ChessMove> combined = checkDirection(1,0);
        combined.addAll(checkDirection(0,1));
        combined.addAll(checkDirection(-1,0));
        combined.addAll(checkDirection(0,-1));
        combined.addAll(checkDirection(-1,1));
        combined.addAll(checkDirection(-1,-1));
        combined.addAll(checkDirection(1,-1));
        combined.addAll(checkDirection(1,1));
        return combined;
    }

    private ArrayList<ChessMove> king(){
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        for(int x = -1; x<1; x++){
            for(int y = -1; y<1; y++){
                if(x!=0 || y!=0 && board.getPiece(new ChessPosition(myPosition.getRow()+y, myPosition.getColumn()+x))==null){
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+y, myPosition.getColumn()+x),null));
                }
            }
        }
        return moves;
    }

    private ArrayList<ChessMove> knight(){
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int[][] possible = {{1,2},{2,1},{-1,2},{-2,1},{1,-2},{2,-1},{-2,-1},{-1,-2}};
        for(int[] i : possible){
            if(board.getPiece(new ChessPosition(myPosition.getRow()+i[0], myPosition.getColumn()+i[1]))==null){
                moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+i[0], myPosition.getColumn()+i[1]),null));
            }
        }
        return moves;
    }

    private ArrayList<ChessMove> pawn(){
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        //white starts on bottom
        if(board.getPiece(myPosition).getTeamColor()== ChessGame.TeamColor.WHITE){
            if(board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()))==null){
                moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()),null));
            }
            if(myPosition.getRow()==2 && board.getPiece(new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()))==null){
                moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()),null));

            }
        }
        else if(board.getPiece(myPosition).getTeamColor()== ChessGame.TeamColor.BLACK){
            if(board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()))==null){
                moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()),null));
            }
            if(myPosition.getRow()==7 && board.getPiece(new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()))==null){
                moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()),null));

            }
        }

        return moves;
    }

    public ArrayList<ChessMove> checkDirection(int x, int y){
        //new arraylist
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessPosition position = new ChessPosition(myPosition.getRow()+y, myPosition.getColumn()+x);

        while(board.getPiece(position)==null){
            moves.add(new ChessMove(myPosition,position,null));
            position = new ChessPosition(position.getRow()+y, position.getColumn()+x);

        }

        return moves;
    }
}
