package ru.smartel.chessonomics.command;

import ru.smartel.chessonomics.dto.ConnectionContext;
import ru.smartel.chessonomics.dto.PlayerStatus;

import java.util.concurrent.ConcurrentHashMap;

public class FindCommandHandler implements CommandHandler {
    private final ConcurrentHashMap<String, ConnectionContext> searchingPlayers;

    public FindCommandHandler(ConcurrentHashMap<String, ConnectionContext> searchingPlayers) {
        this.searchingPlayers = searchingPlayers;
    }

    @Override
    public boolean accepts(String command) {
        return command.equals("find");
    }

    @Override
    public void process(ConnectionContext connectionContext, String command) {
        if (connectionContext.getPlayer() == null) {
            connectionContext.sendMessageToClient("you should login first");
        }
        searchingPlayers.put(connectionContext.getPlayer().getName(), connectionContext);
        synchronized (searchingPlayers) {
            // dumb implementation of players searching
            if (searchingPlayers.size() > 1) {
                ConnectionContext context1 = null;
                ConnectionContext context2 = null;
                for (ConnectionContext context : searchingPlayers.values()) {
                    if (context1 == null) {
                        context1 = context;
                    } else {
                        context2 = context;
                    }
                }

                context1.getPlayer().setStatus(PlayerStatus.PLAYING);
                context2.getPlayer().setStatus(PlayerStatus.PLAYING);
                context1.sendMessageToClient("found " + context2.getPlayer().getName());
                context2.sendMessageToClient("found " + context1.getPlayer().getName());
                searchingPlayers.clear();
            }
        }
    }
}
