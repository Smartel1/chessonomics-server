package ru.smartel.chessonomics.message.parser;

import ru.smartel.chessonomics.message.FindMessage;

import java.util.Optional;

import static java.util.Optional.empty;

public class FindMessageParser implements MessageParser<FindMessage> {
    @Override
    public Optional<FindMessage> parse(String input) {
        if (input.equals("find")) {
            return Optional.of(new FindMessage());
        }
        return empty();
    }
}
