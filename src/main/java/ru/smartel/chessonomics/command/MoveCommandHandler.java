package ru.smartel.chessonomics.command;

import com.github.bhlangonijr.chesslib.move.Move;
import ru.smartel.chessonomics.dto.ConnectionContext;

public class MoveCommandHandler implements CommandHandler {

    @Override
    public boolean accepts(String command) {
        return command.startsWith("move ");
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
        var moveAsString = command.split(" ")[1];
        if (moveAsString.equals("null")) {
            chessBoard.doNullMove();
            connectionContext.setPoints(connectionContext.getPoints() + 1);
            connectionContext.getOpponentContext().sendMessageToClient(command);
            return;
        }
        Move move;
        try {
            move = new Move(moveAsString, chessBoard.getSideToMove());
        } catch (Exception e) {
            connectionContext.sendMessageToClient("error InvalidMove");
            return;
        }
        if (!chessBoard.legalMoves().contains(move)) {
            connectionContext.sendMessageToClient("error IllegalMove");
            return;
        }
        chessBoard.doMove(move);
        connectionContext.getOpponentContext().sendMessageToClient(command);
    }
}
