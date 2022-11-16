package ru.smartel.chessonomics.message;

import com.github.bhlangonijr.chesslib.PieceType;
import com.github.bhlangonijr.chesslib.Square;

public class SpawnMessage implements Message {
    private final PieceType pieceType;
    private final Square square;

    public SpawnMessage(PieceType pieceType, Square square) {
        this.pieceType = pieceType;
        this.square = square;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public Square getSquare() {
        return square;
    }

    @Override
    public String toTcpString() {
        return String.format("spawn %s %s", pieceType.name(), square.name());
    }
}
