package ru.smartel.chessonomics.message;

public class GiveMessage implements Message {
    @Override
    public String toTcpString() {
        return "give";
    }
}
