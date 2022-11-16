package ru.smartel.chessonomics.dto;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.PieceType;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import ru.smartel.chessonomics.util.ChessUtil;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

import static ru.smartel.chessonomics.dto.PlayerStatus.PLAYING;

public class ConnectionContext {
    private Player player;
    private PrintWriter outputWriter;
    /**
     * Board object is shared between two ConnectionContexts
     */
    private Board chessBoard;
    private Side playerSide;
    private Map<PieceType, Set<Square>> availableSpawnSquares;
    private int points;
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

    public Map<PieceType, Set<Square>> getAvailableSpawnSquares() {
        return availableSpawnSquares;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void initGame(Board chessBoard, Side playerSide, ConnectionContext opponentContext) {
        this.player.setStatus(PLAYING);
        this.chessBoard = chessBoard;
        this.playerSide = playerSide;
        this.opponentContext = opponentContext;
        this.availableSpawnSquares = playerSide == Side.WHITE ? ChessUtil.getSpawnSquaresForWhite() : ChessUtil.getSpawnSquaresForBlack();
        this.points = 0;
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
