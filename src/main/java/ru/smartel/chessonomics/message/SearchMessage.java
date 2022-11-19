package ru.smartel.chessonomics.message;

public class SearchMessage implements Message {
    private final String playerName;

    public SearchMessage(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public String toTcpString() {
        return String.format("search %s", playerName);
    }
}
