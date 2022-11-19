package ru.smartel.chessonomics.message.parser;

import ru.smartel.chessonomics.message.LoginMessage;

import java.util.Optional;

import static java.util.Optional.empty;

public class LoginMessageParser implements MessageParser<LoginMessage> {
    @Override
    public Optional<LoginMessage> parse(String input) {
        if (!input.equals("login")) {
            return empty();
        }
        return Optional.of(new LoginMessage());
    }
}
