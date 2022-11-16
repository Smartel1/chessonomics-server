package ru.smartel.chessonomics.command;

import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.PieceType;
import com.github.bhlangonijr.chesslib.Square;
import ru.smartel.chessonomics.dto.ConnectionContext;

import static ru.smartel.chessonomics.util.ChessUtil.getPieceCost;

public class SpawnCommandHandler implements CommandHandler {

    @Override
    public boolean accepts(String command) {
        return command.startsWith("spawn ");
    }

    @Override
    public void process(ConnectionContext connectionContext, String command) {
        if (connectionContext.getPlayer() == null) {
            connectionContext.sendMessageToClient("error MustLogin");
            return;
        }
        var chessBoard = connectionContext.getChessBoard();
        if (chessBoard.isMated() || chessBoard.isDraw()) {
            connectionContext.sendMessageToClient("error GameIsEnded");
            return;
        }
        if (chessBoard.getSideToMove() != connectionContext.getPlayerSide()) {
            connectionContext.sendMessageToClient("error NotYourMove");
            return;
        }
        var commandPieces = command.split(" ");
        if (commandPieces.length < 3) {
            connectionContext.sendMessageToClient("error WrongCommand");
            return;
        }
        PieceType pieceType;
        Square square;
        try {
            pieceType = PieceType.valueOf(commandPieces[1]);
            square = Square.valueOf(commandPieces[2]);
        } catch (Exception e) {
            connectionContext.sendMessageToClient("error WrongCommand");
            return;
        }
        if (!connectionContext.getAvailableSpawnSquares().containsKey(pieceType) ||
                !connectionContext.getAvailableSpawnSquares().get(pieceType).contains(square)) {
            connectionContext.sendMessageToClient("error InvalidSpawnSquare");
            return;
        }
        if (chessBoard.getPiece(square) != Piece.NONE) {
            connectionContext.sendMessageToClient("error SquareNotVacant");
            return;
        }
        int cost = getPieceCost(pieceType);
        if (connectionContext.getPoints() < cost) {
            connectionContext.sendMessageToClient("error NoEnoughPoints");
            return;
        }

        chessBoard.setPiece(Piece.make(chessBoard.getSideToMove(), pieceType), square);
        chessBoard.doNullMove();
        connectionContext.getAvailableSpawnSquares().get(pieceType).remove(square);
        connectionContext.setPoints(connectionContext.getPoints() - cost);

        connectionContext.getOpponentContext().sendMessageToClient(command);
    }
}
