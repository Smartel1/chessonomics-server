package ru.smartel.chessonomics.message;

public enum ErrorMessage implements Message {
    WRONG_COMMAND(1),
    UNAUTHENTICATED(2),
    GAME_ENDED(3),
    NOT_YOUR_MOVE(4),
    INVALID_SPAWN_SQUARE(5),
    SPAWN_NOT_VACANT(6),
    NOT_ENOUGH_POINTS(7),
    INVALID_MOVE(8),
    ILLEGAL_MOVE(9);

    private final int code;

    ErrorMessage(int code) {
        this.code = code;
    }

    @Override
    public String toTcpString() {
        return String.format("error %s", code);
    }
}
