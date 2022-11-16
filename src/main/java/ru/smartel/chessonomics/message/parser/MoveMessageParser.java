package ru.smartel.chessonomics.message.parser;

import ru.smartel.chessonomics.message.MoveMessage;

import java.util.Optional;

import static java.util.Optional.empty;

public class MoveMessageParser implements MessageParser<MoveMessage> {

    @Override
    public Optional<MoveMessage> parse(String input) {
        if (!input.startsWith("move ")) {
            return empty();
        }
        var commandParts = input.split(" ");
        if (commandParts.length != 2) {
            return empty();
        }
        var move = commandParts[1];
        return Optional.of(new MoveMessage(move.equals("null") ? null : move));
    }
}
