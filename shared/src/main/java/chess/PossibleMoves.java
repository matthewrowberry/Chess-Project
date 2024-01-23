package chess;

import java.util.ArrayList;

public class PossibleMoves {
    ChessPiece.PieceType type;
    ChessGame.TeamColor color;
    ChessBoard board;
    ChessPosition myPosition;
    public PossibleMoves(ChessPiece.PieceType type){
        this.type = type;
    }

    public ArrayList<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition){
        this.board = board;
        this.myPosition = myPosition;
        this.color = board.getPiece(myPosition).getTeamColor();
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
            if(inBounds(myPosition.getRow()+i[0],myPosition.getColumn()+i[1]) && board.getPiece(new ChessPosition(myPosition.getRow()+i[0], myPosition.getColumn()+i[1]))==null){
                moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+i[0], myPosition.getColumn()+i[1]),null));
            }
            else if(inBounds(myPosition.getRow()+i[0],myPosition.getColumn()+i[1]) && board.getPiece(new ChessPosition(myPosition.getRow()+i[0], myPosition.getColumn()+i[1])).getTeamColor()!=color){
                moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+i[0], myPosition.getColumn()+i[1]),null));
            }

        }
        return moves;
    }

    private ChessPiece getNext(int row, int col){
        return board.getPiece(new ChessPosition(myPosition.getRow()+row, myPosition.getColumn()+col));
    }

    private ArrayList<ChessMove> pawn() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        //white pawns
        if(board.getPiece(myPosition).getTeamColor()== ChessGame.TeamColor.WHITE) {

            if(getNext(1,0) == null){

                if(myPosition.getRow()!=7) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), null));
                }
                else{
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.KNIGHT));

                }

                if(myPosition.getRow()==2 && getNext(2,0) == null){
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()),null));

                }
            }

            if(getNext(1,1) != null && getNext(1,1).getTeamColor()== ChessGame.TeamColor.BLACK){
                if(myPosition.getRow()==7){
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1),ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1),ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1),ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1),ChessPiece.PieceType.BISHOP));

                }
                else{
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1),null));

                }
            }

            if(getNext(1,-1) != null && getNext(1,-1).getTeamColor()== ChessGame.TeamColor.BLACK){
                if(myPosition.getRow()==7){
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1),ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1),ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1),ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1),ChessPiece.PieceType.BISHOP));

                }
                else{
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1),null));

                }
            }


        } else if (board.getPiece(myPosition).getTeamColor()== ChessGame.TeamColor.BLACK) {
            if(getNext(-1,0) == null){

                if(myPosition.getRow()!=2) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), null));
                }
                else{
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.KNIGHT));

                }

                if(myPosition.getRow()==7 && getNext(-2,0) == null){
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()),null));

                }
            }

            if(getNext(-1,1) != null && getNext(-1,1).getTeamColor()== ChessGame.TeamColor.WHITE){
                if(myPosition.getRow()==2){
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1),ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1),ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1),ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1),ChessPiece.PieceType.BISHOP));

                }
                else{
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1),null));

                }
            }

            if(getNext(-1,-1) != null && getNext(-1,-1).getTeamColor()== ChessGame.TeamColor.WHITE){
                if(myPosition.getRow()==2){
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1),ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1),ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1),ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1),ChessPiece.PieceType.BISHOP));

                }
                else{
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1),null));

                }
            }
        }

        return moves;
    }

    private ArrayList<ChessMove> pawn1(){
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        //white starts on row 2
        if(board.getPiece(myPosition).getTeamColor()== ChessGame.TeamColor.WHITE){
            if(board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()))==null && myPosition.getRow()!=7){
                moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()),null));
                if(myPosition.getRow()==2 && board.getPiece(new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()))==null){
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()),null));

                }
            }

            if(board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1))!=null) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1)).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), null));

                }
                if (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1)).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), null));

                }
            }
            //promotion
            if(myPosition.getRow()==7 && board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()))==null){
                moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()),ChessPiece.PieceType.QUEEN));

                moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()),ChessPiece.PieceType.BISHOP));

                moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()),ChessPiece.PieceType.KNIGHT));

                moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()),ChessPiece.PieceType.ROOK));
            }
        }
        else if(board.getPiece(myPosition).getTeamColor()== ChessGame.TeamColor.BLACK){
            if(board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()))==null  && myPosition.getRow()!=2){
                moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()),null));
                if(myPosition.getRow()==7 && board.getPiece(new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()))==null){
                    moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()),null));

                }
            }

            if(board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1))!=null) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1)).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), null));

                }
                if (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1)).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), null));

                }
            }
            //promotion?
            if(myPosition.getRow()==2 && board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()))==null){
                moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()),ChessPiece.PieceType.QUEEN));

                moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()),ChessPiece.PieceType.BISHOP));

                moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()),ChessPiece.PieceType.KNIGHT));

                moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()),ChessPiece.PieceType.ROOK));
            }
        }


        return moves;
    }

    public ArrayList<ChessMove> checkDirection1(int x, int y){
        //new arraylist
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessPosition position = new ChessPosition(myPosition.getRow()+y, myPosition.getColumn()+x);
        if(position.getColumn()+x<=8 && position.getColumn()+x>0 && position.getRow()+y>0 && position.getRow()<=8 && board.getPiece(position)!=null){
            moves.add(new ChessMove(myPosition,position,null));
        }
        while(position.getColumn()+x<=8 && position.getColumn()+x>0 && position.getRow()+y>0 && position.getRow()<=8 && board.getPiece(position)==null){
            moves.add(new ChessMove(myPosition,position,null));
            position = new ChessPosition(position.getRow()+y, position.getColumn()+x);
            if(position.getColumn()+x<=8 && position.getColumn()+x>0 && position.getRow()+y>0 && position.getRow()<=8 && board.getPiece(position)!=null){
                moves.add(new ChessMove(myPosition,position,null));
            }
        }

        return moves;
    }

    public ArrayList<ChessMove> checkDirection(int row, int col) {

        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int newRow = myPosition.getRow()+row;
        int newCol=myPosition.getColumn()+col;
        while(inBounds(newRow, newCol) && board.getPiece(new ChessPosition(newRow,newCol)) == null){
            moves.add(getChessMove(newRow,newCol));
            newRow += row;
            newCol += col;
        }

        if(inBounds(newRow, newCol) && board.getPiece(new ChessPosition(newRow,newCol)) != null && board.getPiece(new ChessPosition(newRow,newCol)).getTeamColor() != this.color){
            moves.add(getChessMove(newRow,newCol));

        }

        return moves;

    }

    private boolean inBounds(int row,int col){
        return row>=1 && row<=8 && col >=1 && col <= 8;
    }

    private ChessMove getChessMove(int row,int col){
        return new ChessMove(myPosition,new ChessPosition(row,col));
    }
}
