package ru.smartel.chessonomics.message.handler;

import ru.smartel.chessonomics.dto.ConnectionContext;
import ru.smartel.chessonomics.dto.Player;
import ru.smartel.chessonomics.dto.PlayerStatus;
import ru.smartel.chessonomics.message.LoginMessage;
import ru.smartel.chessonomics.message.Message;

public class LoginMessageHandler implements MessageHandler {

    @Override
    public boolean accepts(Message message) {
        return message instanceof LoginMessage;
    }

    @Override
    public void process(ConnectionContext connectionContext, Message message) {
        var player = new Player(((LoginMessage) message).getPlayerName(), PlayerStatus.LOGGED_IN);
        connectionContext.setPlayer(player);
        connectionContext.sendMessageToClient("hello " + player.getName());
    }
}
