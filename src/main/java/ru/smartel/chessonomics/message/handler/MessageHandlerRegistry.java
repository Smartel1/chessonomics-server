package ru.smartel.chessonomics.message.handler;

import ru.smartel.chessonomics.dto.ConnectionContext;
import ru.smartel.chessonomics.message.Message;

import java.util.Arrays;
import java.util.List;

public class MessageHandlerRegistry {
    private final List<MessageHandler> messageParsers;

    public MessageHandlerRegistry(MessageHandler... messageHandlers) {
        this.messageParsers = Arrays.asList(messageHandlers);
    }

    public void process(ConnectionContext connectionContext, Message message) {
        messageParsers.stream()
                .filter(messageHandler -> messageHandler.accepts(message))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No handler for message " + message))
                .process(connectionContext, message);
    }
}
