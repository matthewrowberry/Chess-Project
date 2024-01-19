package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] board;
    public ChessBoard() {
        this.board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {

        this.board[position.getRow()][position.getColumn()] = piece;

    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {

        return this.board[position.getRow()][position.getColumn()];

    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {


        this.board = new ChessPiece[8][8];

        //pawns
        for(int i = 0; i<7; i++){
            this.board[i][1] = new ChessPiece(ChessGame.TeamColor.valueOf(WHITE), ChessPiece.PieceType.valueOf(PAWN));
            this.board[i][1] = new ChessPiece(ChessGame.TeamColor.valueOf(BLACK), ChessPiece.PieceType.valueOf(PAWN));
        }
        //rooks
        this.board[0][0] = new ChessPiece(ChessGame.TeamColor.valueOf(WHITE), ChessPiece.PieceType.valueOf(ROOK));
        this.board[0][7] = new ChessPiece(ChessGame.TeamColor.valueOf(BLACK), ChessPiece.PieceType.valueOf(ROOK));
        this.board[7][0] = new ChessPiece(ChessGame.TeamColor.valueOf(WHITE), ChessPiece.PieceType.valueOf(ROOK));
        this.board[7][7] = new ChessPiece(ChessGame.TeamColor.valueOf(BLACK), ChessPiece.PieceType.valueOf(ROOK));
        //knights
        this.board[1][0] = new ChessPiece(ChessGame.TeamColor.valueOf(WHITE), ChessPiece.PieceType.valueOf(KNIGHT));
        this.board[1][7] = new ChessPiece(ChessGame.TeamColor.valueOf(BLACK), ChessPiece.PieceType.valueOf(KNIGHT));
        this.board[6][0] = new ChessPiece(ChessGame.TeamColor.valueOf(WHITE), ChessPiece.PieceType.valueOf(KNIGHT));
        this.board[6][7] = new ChessPiece(ChessGame.TeamColor.valueOf(BLACK), ChessPiece.PieceType.valueOf(KNIGHT));
        //Bishops
        this.board[2][0] = new ChessPiece(ChessGame.TeamColor.valueOf(WHITE), ChessPiece.PieceType.valueOf(BISHOP));
        this.board[2][7] = new ChessPiece(ChessGame.TeamColor.valueOf(BLACK), ChessPiece.PieceType.valueOf(BISHOP));
        this.board[5][0] = new ChessPiece(ChessGame.TeamColor.valueOf(WHITE), ChessPiece.PieceType.valueOf(BISHOP));
        this.board[5][7] = new ChessPiece(ChessGame.TeamColor.valueOf(BLACK), ChessPiece.PieceType.valueOf(BISHOP));
        //Queen
        this.board[3][0] = new ChessPiece(ChessGame.TeamColor.valueOf(WHITE), ChessPiece.PieceType.valueOf(QUEEN));
        this.board[4][7] = new ChessPiece(ChessGame.TeamColor.valueOf(BLACK), ChessPiece.PieceType.valueOf(QUEEN));
        //King
        this.board[4][0] = new ChessPiece(ChessGame.TeamColor.valueOf(WHITE), ChessPiece.PieceType.valueOf(KING));
        this.board[3][7] = new ChessPiece(ChessGame.TeamColor.valueOf(BLACK), ChessPiece.PieceType.valueOf(KING));


    }
}
