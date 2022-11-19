package ru.smartel.chessonomics.message;

public class StopMessage implements Message {
    @Override
    public String toTcpString() {
        return "stop";
    }
}
