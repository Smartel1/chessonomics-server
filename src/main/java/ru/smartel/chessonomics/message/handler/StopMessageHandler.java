package ru.smartel.chessonomics.message.handler;

import ru.smartel.chessonomics.dto.ConnectionContext;
import ru.smartel.chessonomics.message.ErrorMessage;
import ru.smartel.chessonomics.message.Message;
import ru.smartel.chessonomics.message.StopMessage;

import java.util.concurrent.ConcurrentHashMap;

public class StopMessageHandler implements MessageHandler {
    private final ConcurrentHashMap<String, ConnectionContext> searchingPlayers;

    public StopMessageHandler(ConcurrentHashMap<String, ConnectionContext> searchingPlayers) {
        this.searchingPlayers = searchingPlayers;
    }

    @Override
    public boolean accepts(Message message) {
        return message instanceof StopMessage;
    }

    @Override
    public void process(ConnectionContext connectionContext, Message message) {
        if (connectionContext.getPlayer() == null) {
            connectionContext.sendMessageToClient(ErrorMessage.UNAUTHENTICATED.toTcpString());
            return;
        }
        if (!searchingPlayers.containsKey(connectionContext.getPlayer().getName())) {
            connectionContext.sendMessageToClient(ErrorMessage.NOT_SEARCHING.toTcpString());
            return;
        }
        searchingPlayers.remove(connectionContext.getPlayer().getName());
        connectionContext.sendMessageToClient("stopped");
    }
}
