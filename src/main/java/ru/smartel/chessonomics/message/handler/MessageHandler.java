package ru.smartel.chessonomics.message.handler;

import ru.smartel.chessonomics.dto.ConnectionContext;
import ru.smartel.chessonomics.message.Message;

public interface MessageHandler {
    boolean accepts(Message message);

    void process(ConnectionContext connectionContext, Message message);
}
