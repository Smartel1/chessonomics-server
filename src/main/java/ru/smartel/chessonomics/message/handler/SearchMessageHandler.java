package ru.smartel.chessonomics.message.handler;

import ru.smartel.chessonomics.dto.ConnectionContext;
import ru.smartel.chessonomics.dto.PlayerStatus;
import ru.smartel.chessonomics.message.ErrorMessage;
import ru.smartel.chessonomics.message.SearchMessage;
import ru.smartel.chessonomics.message.Message;
import ru.smartel.chessonomics.util.ChessUtil;

import java.util.concurrent.ConcurrentHashMap;

import static com.github.bhlangonijr.chesslib.Side.BLACK;
import static com.github.bhlangonijr.chesslib.Side.WHITE;

public class SearchMessageHandler implements MessageHandler {
    private final ConcurrentHashMap<String, ConnectionContext> searchingPlayers;

    public SearchMessageHandler(ConcurrentHashMap<String, ConnectionContext> searchingPlayers) {
        this.searchingPlayers = searchingPlayers;
    }

    @Override
    public boolean accepts(Message message) {
        return message instanceof SearchMessage;
    }

    @Override
    public void process(ConnectionContext connectionContext, Message message) {
        if (connectionContext.getPlayer() == null) {
            connectionContext.sendMessageToClient(ErrorMessage.UNAUTHENTICATED.toTcpString());
            return;
        }
        var playerName = ((SearchMessage) message).getPlayerName();
        connectionContext.getPlayer().setName(playerName);
        connectionContext.getPlayer().setStatus(PlayerStatus.SEARCHING);
        searchingPlayers.put(playerName, connectionContext);
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

                var chessBoard = ChessUtil.initBoard();
                var randomSide = Math.random() > 0.5 ? WHITE : BLACK;

                context1.initGame(chessBoard, randomSide, context2);
                context2.initGame(chessBoard, randomSide.flip(), context1);

                searchingPlayers.clear();
            }
        }
    }
}
