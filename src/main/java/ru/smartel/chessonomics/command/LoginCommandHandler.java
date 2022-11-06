package ru.smartel.chessonomics.command;

import ru.smartel.chessonomics.dto.ConnectionContext;
import ru.smartel.chessonomics.dto.Player;
import ru.smartel.chessonomics.dto.PlayerStatus;

public class LoginCommandHandler implements CommandHandler {

    @Override
    public boolean accepts(String command) {
        return command.startsWith("login ");
    }

    @Override
    public void process(ConnectionContext connectionContext, String command) {
        var commandParts = command.split(" ");
        if (commandParts.length != 2) {
            connectionContext.sendMessageToClient("wrong command");
        }
        var player = new Player(commandParts[1], PlayerStatus.LOGGED_IN);
        connectionContext.setPlayer(player);
        connectionContext.sendMessageToClient("hello " + player.getName());
    }
}
