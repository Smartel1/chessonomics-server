package ru.smartel.chessonomics.message.parser;

import ru.smartel.chessonomics.message.SearchMessage;

import java.util.Optional;

import static java.util.Optional.empty;

public class SearchMessageParser implements MessageParser<SearchMessage> {
    @Override
    public Optional<SearchMessage> parse(String input) {
        if (!input.startsWith("search ")) {
            return empty();
        }
        var commandParts = input.split(" ");
        if (commandParts.length != 2) {
            return empty();
        }
        return Optional.of(new SearchMessage(commandParts[1]));
    }
}
