package ru.smartel.chessonomics.message.parser;

import ru.smartel.chessonomics.message.GiveMessage;

import java.util.Optional;

import static java.util.Optional.empty;

public class GiveMessageParser implements MessageParser<GiveMessage> {
    @Override
    public Optional<GiveMessage> parse(String input) {
        if (!input.equals("give")) {
            return empty();
        }
        return Optional.of(new GiveMessage());
    }
}
