package ru.smartel.chessonomics.message;

public class MoveMessage implements Message {
    private final String move;

    public MoveMessage(String move) {
        this.move = move;
    }

    public String getMove() {
        return move;
    }

    @Override
    public String toTcpString() {
        return String.format("move %s", move);
    }
}
