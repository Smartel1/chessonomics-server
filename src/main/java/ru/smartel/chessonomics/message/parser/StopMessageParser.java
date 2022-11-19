package ru.smartel.chessonomics.message.parser;

import ru.smartel.chessonomics.message.StopMessage;

import java.util.Optional;

import static java.util.Optional.empty;

public class StopMessageParser implements MessageParser<StopMessage> {
    @Override
    public Optional<StopMessage> parse(String input) {
        if (input.equals("stop")) {
            return Optional.of(new StopMessage());
        }
        return empty();
    }
}
