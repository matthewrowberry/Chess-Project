package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private boolean blackTurn;
    private ChessBoard board;
    private boolean wcastleRight = true;
    private boolean wcastleLeft = true;
    private boolean bcastleRight = true;
    private boolean bcastleLeft = true;

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        if(blackTurn){
            return TeamColor.BLACK;
        }
        return TeamColor.WHITE;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        if(team == TeamColor.WHITE){
            blackTurn = false;
        }else {
            blackTurn = true;
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */


    //throw new RuntimeException("Not implemented");
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if(getPiece(board,startPosition.getRow(),startPosition.getColumn())==null){
            return null;
        }
        TeamColor color = getPiece(startPosition).getTeamColor();
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        ChessBoard backup = setNewBoard(board);
        for(ChessMove move : getPiece(startPosition).pieceMoves(board,startPosition)){

            makePracticeMove(move);

            if(getPiece(move.getEndPosition())!=null&&!isInCheck(getPiece(move.getEndPosition()).getTeamColor())){
                if(getPiece(move.getEndPosition()).getPieceType()== ChessPiece.PieceType.KING && Math.abs(move.getEndPosition().getColumn()-move.getStartPosition().getColumn())>1) {
                    if(move.getEndPosition().getColumn()-move.getStartPosition().getColumn()>1){
                        if(!rookInCheck(getPos(move.getEndPosition().getRow(),move.getEndPosition().getColumn()-1),board)){
                            if(move.getStartPosition().getRow()==1 && wcastleRight) {
                                moves.add(move);
                            }else if(move.getStartPosition().getRow()==8 && bcastleRight){
                                moves.add(move);
                            }
                        }
                    }
                    else{
                        if(!rookInCheck(getPos(move.getEndPosition().getRow(),move.getEndPosition().getColumn()+1),board)){
                            if(move.getStartPosition().getRow()==1 && wcastleLeft) {
                                moves.add(move);
                            }else if(move.getStartPosition().getRow()==8 && bcastleLeft){
                                moves.add(move);
                            }
                        }
                    }
                }
                else{
                    moves.add(move);
                }


            }

            setBoard(backup);

        }

        return moves;
    }


    private ChessPiece getPiece(ChessPosition position){
        return getPiece(this.board,position.getRow(),position.getColumn());
    }
    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(validMoves(move.getStartPosition()).contains(move) && getPiece(move.getStartPosition()).getTeamColor()==getTeamTurn()) {
            if(board.getPiece(move.getStartPosition()).getPieceType()== ChessPiece.PieceType.KING && Math.abs(move.getEndPosition().getColumn()-move.getStartPosition().getColumn())>1){
                if(move.getEndPosition().getColumn()-move.getStartPosition().getColumn()<0&&((move.getStartPosition().getRow()==8 &&bcastleLeft)||(move.getStartPosition().getRow()==1 &&wcastleLeft))){
                    board.makeMove(move);
                    ChessPosition start = new ChessPosition(move.getStartPosition().getRow(),move.getStartPosition().getColumn()-4);
                    ChessPosition end = new ChessPosition(move.getStartPosition().getRow(),move.getStartPosition().getColumn()-1);

                    board.makeMove(new ChessMove(start,end));
                }
                else if (((move.getStartPosition().getRow()==8 &&bcastleRight)||(move.getStartPosition().getRow()==1 &&wcastleRight))){
                    board.makeMove(move);
                    ChessPosition start = new ChessPosition(move.getStartPosition().getRow(),move.getStartPosition().getColumn()+3);
                    ChessPosition end = new ChessPosition(move.getStartPosition().getRow(),move.getStartPosition().getColumn()+1);

                    board.makeMove(new ChessMove(start,end));
                }
            }else {
                if(move.getStartPosition().getRow()==8) {
                    if (board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.KING) {
                        bcastleLeft = false;
                        bcastleRight = false;
                    } else if (board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.ROOK && move.getStartPosition().getColumn() == 1) {
                        bcastleLeft = false;

                    } else if (board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.ROOK && move.getStartPosition().getColumn() == 8) {
                        bcastleRight = false;

                    }
                }
                else if(move.getStartPosition().getRow()==1) {
                    if (board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.KING) {
                        wcastleLeft = false;
                        wcastleRight = false;
                    } else if (board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.ROOK && move.getStartPosition().getColumn() == 1) {
                        wcastleLeft = false;

                    } else if (board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.ROOK && move.getStartPosition().getColumn() == 8) {
                        wcastleRight = false;

                    }
                }
                board.makeMove(move);
            }
            blackTurn = !blackTurn;
        }
        else {
            throw new InvalidMoveException();
        }
    }

    private void makePracticeMove(ChessMove move){
        if(board.getPiece(move.getStartPosition()).getPieceType()== ChessPiece.PieceType.KING && Math.abs(move.getEndPosition().getColumn()-move.getStartPosition().getColumn())>1){
            if(move.getEndPosition().getColumn()-move.getStartPosition().getColumn()<0){
                board.makeMove(move);
                ChessPosition start = new ChessPosition(move.getStartPosition().getRow(),move.getStartPosition().getColumn()-4);
                ChessPosition end = new ChessPosition(move.getStartPosition().getRow(),move.getStartPosition().getColumn()-1);

                board.makeMove(new ChessMove(start,end));
            }
            else{
                board.makeMove(move);
                ChessPosition start = new ChessPosition(move.getStartPosition().getRow(),move.getStartPosition().getColumn()+3);
                ChessPosition end = new ChessPosition(move.getStartPosition().getRow(),move.getStartPosition().getColumn()+1);

                board.makeMove(new ChessMove(start,end));
            }
        }else {
            board.makeMove(move);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(teamColor,board);
    }

    //is teamcolor in check
    public boolean isInCheck(TeamColor teamColor,ChessBoard newBoard) {
        ChessPosition king = null;
        //get teamcolor's king position
        for(int row = 1; row<9; row++) {
            for (int col = 1; col < 9; col++) {
                if(newBoard.getPiece(getPos(row,col))!= null && newBoard.getPiece(getPos(row,col)).getPieceType() == ChessPiece.PieceType.KING && newBoard.getPiece(getPos(row,col)).getTeamColor() == teamColor){
                    king = getPos(row,col);
                }
            }
        }
        //for each position
        for(int row = 1; row<9; row++){
            for(int col = 1; col<9; col++){
                //save position
                ChessPosition tempPos = getPos(row,col);
                //if the piece is the other color, check if that pieces moves include a move that kills our king
                if(newBoard.getPiece(tempPos)!= null && newBoard.getPiece(tempPos).getTeamColor() != teamColor && CheckForCheck(newBoard.getPiece(tempPos).pieceMoves(newBoard,tempPos),king)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean rookInCheck(ChessPosition rookPos,ChessBoard newBoard) {
        ChessPosition king = rookPos;
        TeamColor teamColor = getPiece(rookPos).getTeamColor();

        //for each position
        for(int row = 1; row<9; row++){
            for(int col = 1; col<9; col++){
                //save position
                ChessPosition tempPos = getPos(row,col);
                //if the piece is the other color, check if that pieces moves include a move that kills our king
                if(newBoard.getPiece(tempPos)!= null && newBoard.getPiece(tempPos).getTeamColor() != teamColor && CheckForCheck(newBoard.getPiece(tempPos).pieceMoves(newBoard,tempPos),king)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean CheckForCheck(Collection<ChessMove> moves,ChessPosition king){
        //for each move put in
        for(ChessMove move : moves){
            //check if one of the endpositions is the king of the other team.
            if(move.getEndPosition().equals(king)){
                return true;
            }
        }
        return false;
    }


    private ChessPosition getPos(int row, int col){
        return new ChessPosition(row,col);
    }
    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */

    //checks if teamColor is in checkmate. So
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
            return false;
        }
        ChessBoard newBoard = null;
        //for each position
        for(int row = 1; row<9; row++){
            for(int col = 1; col<9; col++){
                //if it's teamColor's team
                if(getPiece(board,row,col) != null && getPiece(board,row,col).getTeamColor()==teamColor){
                    //get each piece's valid moves
                    ChessBoard backup = setNewBoard(board);
                    for(ChessMove move : validMoves(getPos(row,col))){

                        //make each move
                        board.makeMove(move);
                        //check if still in check
                        if(!isInCheck(teamColor)){
                            //if not still in check undo move and return false
                            setBoard(backup);
                            return false;
                        }

                        setBoard(backup);
                    }
                }
            }
        }
        return true;
    }

    private ChessPiece getPiece(ChessBoard board,int row,int col){
        return board.getPiece(getPos(row,col));
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        for(int row = 1; row<=8; row++){
            for(int col = 1; col<=8; col++){
                if(getPiece(getPos(row,col)) != null && getPiece(getPos(row,col)).getTeamColor()==teamColor) {
                    moves.addAll(validMoves(getPos(row, col)));
                }
            }
        }
        if(moves.isEmpty()){
            return true;
        }
        return false;

    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = new ChessBoard();
        for(int row = 1; row<9; row++){
            for(int col = 1; col<9; col++){
                this.board.addPiece(getPos(row,col),board.getPiece(getPos(row,col)));
            }
        }
    }

    public ChessBoard setNewBoard(ChessBoard oldBoard) {
        ChessBoard board = new ChessBoard();
        for(int row = 1; row<9; row++){
            for(int col = 1; col<9; col++){
                board.addPiece(getPos(row,col),oldBoard.getPiece(getPos(row,col)));
            }
        }
        return board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}