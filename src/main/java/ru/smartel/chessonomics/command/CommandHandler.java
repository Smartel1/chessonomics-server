package ru.smartel.chessonomics.command;

import ru.smartel.chessonomics.dto.ConnectionContext;

public interface CommandHandler {
    boolean accepts(String command);

    void process(ConnectionContext connectionContext, String command);
}
