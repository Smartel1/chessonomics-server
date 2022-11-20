package ru.smartel.chessonomics.message.handler;

import ru.smartel.chessonomics.dto.ConnectionContext;
import ru.smartel.chessonomics.dto.PlayerStatus;
import ru.smartel.chessonomics.message.ErrorMessage;
import ru.smartel.chessonomics.message.GiveMessage;
import ru.smartel.chessonomics.message.Message;

public class GiveMessageHandler implements MessageHandler {

    @Override
    public boolean accepts(Message message) {
        return message instanceof GiveMessage;
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
        connectionContext.getOpponentContext().sendMessageToClient(message.toTcpString());

        connectionContext.getOpponentContext().clearGame();
        connectionContext.clearGame();
    }
}
