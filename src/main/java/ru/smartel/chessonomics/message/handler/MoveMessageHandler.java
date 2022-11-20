package ru.smartel.chessonomics.message.handler;

import com.github.bhlangonijr.chesslib.move.Move;
import ru.smartel.chessonomics.dto.ConnectionContext;
import ru.smartel.chessonomics.dto.PlayerStatus;
import ru.smartel.chessonomics.message.ErrorMessage;
import ru.smartel.chessonomics.message.Message;
import ru.smartel.chessonomics.message.MoveMessage;

public class MoveMessageHandler implements MessageHandler {

    @Override
    public boolean accepts(Message message) {
        return message instanceof MoveMessage;
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
        if (((MoveMessage) message).getMove() == null) {
            chessBoard.doNullMove();
            connectionContext.setPoints(connectionContext.getPoints() + 1);
            connectionContext.getOpponentContext().sendMessageToClient(message.toTcpString());
            return;
        }
        Move move;
        try {
            move = new Move(((MoveMessage) message).getMove(), chessBoard.getSideToMove());
        } catch (Exception e) {
            connectionContext.sendMessageToClient(ErrorMessage.INVALID_MOVE.toTcpString());
            return;
        }
        if (!chessBoard.legalMoves().contains(move)) {
            connectionContext.sendMessageToClient(ErrorMessage.ILLEGAL_MOVE.toTcpString());
            return;
        }
        chessBoard.doMove(move);
        connectionContext.getOpponentContext().sendMessageToClient(message.toTcpString());
        if (chessBoard.isMated() || chessBoard.isDraw()) {
            connectionContext.getOpponentContext().clearGame();
            connectionContext.clearGame();
        }
    }
}
