package ru.smartel.chessonomics.message;

public class LoginMessage implements Message {
    private final String playerName;

    public LoginMessage(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public String toTcpString() {
        return String.format("login %s", playerName);
    }
}
