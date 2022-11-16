package ru.smartel.chessonomics.message;

public class FindMessage implements Message {
    @Override
    public String toTcpString() {
        return "find";
    }
}
