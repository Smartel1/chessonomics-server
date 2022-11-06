package ru.smartel.chessonomics.dto;

public class Player {
    private String name;
    private volatile PlayerStatus status;

    public Player(String name, PlayerStatus status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }
}
