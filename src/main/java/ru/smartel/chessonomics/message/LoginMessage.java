package ru.smartel.chessonomics.message;

public class LoginMessage implements Message {
    @Override
    public String toTcpString() {
        return "login";
    }
}
