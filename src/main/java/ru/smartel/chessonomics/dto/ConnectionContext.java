package ru.smartel.chessonomics.dto;

import java.io.PrintWriter;

public class ConnectionContext {
    private Player player;
    private PrintWriter outputWriter;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setOutputWriter(PrintWriter outputWriter) {
        if (this.outputWriter != null) {
            throw new IllegalStateException("Cant change output writer after first set");
        }
        this.outputWriter = outputWriter;
    }

    public void sendMessageToClient(String message) {
        new Thread(() -> {
            synchronized (outputWriter) {
                System.out.println("Sent command: " + message);
                outputWriter.println(message);
            }
        }).start();
    }
}
