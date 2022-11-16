package ru.smartel.chessonomics.message.parser;

import ru.smartel.chessonomics.message.Message;

import java.util.Optional;

public interface MessageParser<T extends Message> {
    /**
     * parse string to object. Returns empty if it cannot be parsed
     */
    Optional<T> parse(String input);
}
