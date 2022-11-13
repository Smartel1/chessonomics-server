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
        if (chessBoard.getSideToMove() != connectionContext.getPlayerSide()) {
            connectionContext.sendMessageToClient("error NotYourMove");
            return;
        }
        Move move;
        try {
            move = new Move(command.split(" ")[1], chessBoard.getSideToMove());
        } catch (Exception e) {
            connectionContext.sendMessageToClient("error InvalidMove");
            return;
        }
        if (!chessBoard.legalMoves().contains(move)) {
            connectionContext.sendMessageToClient("error IllegalMove");
            return;
        }
        chessBoard.doMove(move);
        connectionContext.getOpponentContext().sendMessageToClient("move " + move);
        // todo check mate/draw
    }
}
