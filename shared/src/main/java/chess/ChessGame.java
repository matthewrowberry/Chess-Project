package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private boolean WhiteTurn;
    ChessBoard board;
    public ChessGame() {
        WhiteTurn = true;
        board = new ChessBoard();
        board.resetBoard();

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        if(WhiteTurn){
            return TeamColor.WHITE;
        }
        return TeamColor.BLACK;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        if(team == TeamColor.WHITE){
            WhiteTurn = true;
        }
        else{
            WhiteTurn = false;
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
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if(board.getPiece(startPosition) == null){
            return null;
        }
        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> moves = piece.pieceMoves(board,startPosition);
        Iterator iterator = moves.iterator();
        ArrayList<ChessMove> newMoves = new ArrayList<ChessMove>();

        while(iterator.hasNext()) {
            ChessMove move = (ChessMove) iterator.next();
            if(CheckValid(move)){
                newMoves.add(move);
            }
        }

        return newMoves;

    }

    private boolean CheckValid(ChessMove move){
        ChessPosition kingPos = null;
        ChessBoard newBoard = new ChessBoard();
        for(int row = 1; row<=8; row++){
            for(int col = 1; col<=8; col++){
                //if we're not on the start of the moved piece, copy in the piece there to our new board
                if(!(row == move.getStartPosition().getRow() && col == move.getStartPosition().getColumn())){
                    //SIMPLIFY WITH CODE DUPLICATION FIX
                    newBoard.addPiece(getPosition(row,col), board.getPiece(getPosition(row,col)));
                }else{
                    ChessPosition newPosition = getPosition(move.getEndPosition().getRow(),move.getEndPosition().getColumn());
                    newBoard.addPiece(newPosition,board.getPiece(getPosition(row,col)));
                }
                if(board.getPiece(getPosition(row,col)) != null && board.getPiece(getPosition(row,col)).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(getPosition(row,col)).getTeamColor() == board.getPiece(move.getStartPosition()).getTeamColor()){
                    kingPos = getPosition(row,col);
                }
            }
        }



        Collection<ChessMove> movesToCheck;
        for(int row = 1; row<=8; row++) {
            for (int col = 1; col <= 8; col++) {
                if(newBoard.getPiece(getPosition(row,col)).getTeamColor() != board.getPiece(move.getStartPosition()).getTeamColor()) {
                    movesToCheck = newBoard.getPiece(getPosition(row, col)).pieceMoves(newBoard, getPosition(row, col));
                    for (ChessMove enemyMove : movesToCheck) {
                        if (!CheckKing(enemyMove, kingPos)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }


    //returns false if king is in danger
    private boolean CheckKing(ChessMove move, ChessPosition kingPosition){
        return move.getEndPosition() != kingPosition;
    }

    private ChessPosition getPosition(int row, int col){
        return new ChessPosition(row,col);
    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
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
                this.board.addPiece(getPosition(row,col),board.getPiece(getPosition(row,col)));
            }
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        throw new RuntimeException("Not implemented");
    }
}
