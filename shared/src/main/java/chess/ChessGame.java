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
    private boolean whiteTurn;
    private ChessBoard board;
    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        if(whiteTurn){
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
            whiteTurn = true;
        }
        whiteTurn = false;
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
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        for(ChessMove move : getPiece(startPosition).pieceMoves(board,startPosition)){
            board.makeMove(move);

            if(!isInCheck(getPiece(startPosition).getTeamColor())){
                moves.add(move);
            }

            board.undoMove(move);

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
        if(validMoves(move.getStartPosition()).contains(move)) {
            board.makeMove(move);
        }
        else {
            throw new InvalidMoveException();
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
                if(newBoard.getPiece(getPos(row,col)).getPieceType() == ChessPiece.PieceType.KING && newBoard.getPiece(getPos(row,col)).getTeamColor() == teamColor){
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
                if(newBoard.getPiece(tempPos).getTeamColor() != teamColor && CheckForCheck(newBoard.getPiece(tempPos).pieceMoves(newBoard,tempPos),king)){
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
                if(getPiece(board,row,col).getTeamColor()==teamColor){
                    //get each piece's valid moves
                    for(ChessMove move : validMoves(getPos(row,col))){
                        //make each move
                        board.makeMove(move);
                        //check if still in check
                        if(!isInCheck(teamColor)){
                            //if not still in check undo move and return false
                            board.undoMove(move);
                            return false;
                        }

                        board.undoMove(move);
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
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        board = new ChessBoard();
        for(int row = 1; row<9; row++){
            for(int col = 1; col<9; col++){
                this.board.addPiece(getPos(row,col),board.getPiece(getPos(row,col)));
            }
        }
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