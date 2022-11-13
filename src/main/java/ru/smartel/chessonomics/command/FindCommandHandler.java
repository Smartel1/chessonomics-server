package ru.smartel.chessonomics.command;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.game.GameContext;
import com.github.bhlangonijr.chesslib.game.GameMode;
import com.github.bhlangonijr.chesslib.game.VariationType;
import ru.smartel.chessonomics.dto.ConnectionContext;

import java.util.concurrent.ConcurrentHashMap;

import static com.github.bhlangonijr.chesslib.Side.BLACK;
import static com.github.bhlangonijr.chesslib.Side.WHITE;

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
            connectionContext.sendMessageToClient("error MustLogin");
            return;
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

                var gameContext = new GameContext(GameMode.HUMAN_VS_HUMAN, VariationType.NORMAL);
                var chessBoard = new Board(gameContext, true);

                var randomSide = Math.random() > 0.5 ? WHITE : BLACK;

                context1.initGame(chessBoard, randomSide, context2);
                context2.initGame(chessBoard, randomSide.flip(), context1);

                searchingPlayers.clear();
            }
        }
    }
}
