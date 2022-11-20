package ru.smartel.chessonomics.message.handler;

import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.PieceType;
import com.github.bhlangonijr.chesslib.Square;
import ru.smartel.chessonomics.dto.ConnectionContext;
import ru.smartel.chessonomics.dto.PlayerStatus;
import ru.smartel.chessonomics.message.ErrorMessage;
import ru.smartel.chessonomics.message.Message;
import ru.smartel.chessonomics.message.SpawnMessage;

import static ru.smartel.chessonomics.util.ChessUtil.getPieceCost;

public class SpawnMessageHandler implements MessageHandler {

    @Override
    public boolean accepts(Message message) {
        return message instanceof SpawnMessage;
    }

    @Override
    public void process(ConnectionContext connectionContext, Message message) {
        if (connectionContext.getPlayer() == null) {
            connectionContext.sendMessageToClient(ErrorMessage.UNAUTHENTICATED.toTcpString());
            return;
        }
        if (connectionContext.getPlayer().getStatus() != PlayerStatus.PLAYING) {
            connectionContext.sendMessageToClient(ErrorMessage.NOT_PLAYING.toTcpString());
            return;
        }
        var chessBoard = connectionContext.getChessBoard();
        if (chessBoard.getSideToMove() != connectionContext.getPlayerSide()) {
            connectionContext.sendMessageToClient(ErrorMessage.NOT_YOUR_MOVE.toTcpString());
            return;
        }
        PieceType pieceType = ((SpawnMessage) message).getPieceType();
        Square square = ((SpawnMessage) message).getSquare();
        if (!connectionContext.getAvailableSpawnSquares().containsKey(pieceType) ||
                !connectionContext.getAvailableSpawnSquares().get(pieceType).contains(square)) {
            connectionContext.sendMessageToClient(ErrorMessage.INVALID_SPAWN_SQUARE.toTcpString());
            return;
        }
        if (chessBoard.getPiece(square) != Piece.NONE) {
            connectionContext.sendMessageToClient(ErrorMessage.SPAWN_NOT_VACANT.toTcpString());
            return;
        }
        int cost = getPieceCost(pieceType);
        if (connectionContext.getPoints() < cost) {
            connectionContext.sendMessageToClient(ErrorMessage.NOT_ENOUGH_POINTS.toTcpString());
            return;
        }

        chessBoard.setPiece(Piece.make(chessBoard.getSideToMove(), pieceType), square);
        chessBoard.doNullMove();
        connectionContext.getAvailableSpawnSquares().get(pieceType).remove(square);
        connectionContext.setPoints(connectionContext.getPoints() - cost);

        connectionContext.getOpponentContext().sendMessageToClient(message.toTcpString());
    }
}
