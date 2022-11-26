package ru.smartel.chessonomics.message;

public enum ErrorMessage implements Message {
    WRONG_COMMAND(1),
    UNAUTHENTICATED(2),
    NOT_PLAYING(3),
    NOT_YOUR_MOVE(4),
    INVALID_SPAWN_SQUARE(5),
    SPAWN_NOT_VACANT(6),
    NOT_ENOUGH_POINTS(7),
    INVALID_MOVE(8),
    ILLEGAL_MOVE(9),
    NOT_SEARCHING(10),
    DUPLICATE_PLAYER(11);

    private final int code;

    ErrorMessage(int code) {
        this.code = code;
    }

    @Override
    public String toTcpString() {
        return String.format("error %s", code);
    }
}
