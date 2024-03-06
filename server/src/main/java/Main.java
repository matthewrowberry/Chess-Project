import chess.*;
import dataAccess.DataAccessException;
import server.Server;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws Exception {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        Server serve = new Server();
        serve.run(8080);
    }
}