package ru.smartel.chessonomics.message.parser;

import com.github.bhlangonijr.chesslib.PieceType;
import com.github.bhlangonijr.chesslib.Square;
import ru.smartel.chessonomics.message.SpawnMessage;

import java.util.Optional;

import static java.util.Optional.empty;

public class SpawnMessageParser implements MessageParser<SpawnMessage> {

    @Override
    public Optional<SpawnMessage> parse(String input) {
        if (!input.startsWith("spawn ")) {
            return empty();
        }
        var commandParts = input.split(" ");
        if (commandParts.length != 3) {
            return empty();
        }
        return Optional.of(new SpawnMessage(
                PieceType.valueOf(commandParts[1]),
                Square.valueOf(commandParts[2])));
    }
}
