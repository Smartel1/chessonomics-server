package ru.smartel.chessonomics.command;

import ru.smartel.chessonomics.dto.ConnectionContext;

public class MoveCommandHandler implements CommandHandler {

    @Override
    public boolean accepts(String command) {
        return command.startsWith("move ");
    }

    @Override
    public void process(ConnectionContext connectionContext, String command) {
        if (connectionContext.getPlayer() == null) {
            connectionContext.sendMessageToClient("you should login first");
        }
        // stub
        connectionContext.sendMessageToClient("move e7e6");
    }
}
