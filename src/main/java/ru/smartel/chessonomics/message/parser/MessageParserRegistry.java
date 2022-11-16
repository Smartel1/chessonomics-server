package ru.smartel.chessonomics.message.parser;

import ru.smartel.chessonomics.message.Message;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MessageParserRegistry {
    private final List<MessageParser<?>> messageParsers;

    public MessageParserRegistry(MessageParser<?>... messageParsers) {
        this.messageParsers = Arrays.asList(messageParsers);
    }

    public Optional<? extends Message> parse(String input) {
        return messageParsers.stream()
                .map(parser -> parser.parse(input))
                .filter(Optional::isPresent)
                .map(Optional::orElseThrow)
                .findFirst();
    }
}
