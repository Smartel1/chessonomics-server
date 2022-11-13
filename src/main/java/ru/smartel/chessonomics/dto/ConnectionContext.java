package ru.smartel.chessonomics.dto;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;

import java.io.PrintWriter;

import static ru.smartel.chessonomics.dto.PlayerStatus.PLAYING;

public class ConnectionContext {
    private Player player;
    private PrintWriter outputWriter;
    /**
     * Board object is shared between two ConnectionContexts
     */
    private Board chessBoard;
    private Side playerSide;
    private ConnectionContext opponentContext;

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

    public Board getChessBoard() {
        return chessBoard;
    }

    public Side getPlayerSide() {
        return playerSide;
    }

    public ConnectionContext getOpponentContext() {
        return opponentContext;
    }

    public void initGame(Board chessBoard, Side playerSide, ConnectionContext opponentContext) {
        this.player.setStatus(PLAYING);
        this.chessBoard = chessBoard;
        this.playerSide = playerSide;
        this.opponentContext = opponentContext;
        sendMessageToClient("started " + opponentContext.getPlayer().getName() + " " + playerSide);
    }

    public void sendMessageToClient(String message) {
        new Thread(() -> {
            synchronized (outputWriter) {
                System.out.println("Sent command to " + player.getName() + ": " + message);
                outputWriter.println(message);
            }
        }).start();
    }
}
