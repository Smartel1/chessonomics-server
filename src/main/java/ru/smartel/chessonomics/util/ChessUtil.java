package ru.smartel.chessonomics.util;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.PieceType;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.game.GameContext;
import com.github.bhlangonijr.chesslib.game.GameMode;
import com.github.bhlangonijr.chesslib.game.VariationType;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.github.bhlangonijr.chesslib.PieceType.BISHOP;
import static com.github.bhlangonijr.chesslib.PieceType.KNIGHT;
import static com.github.bhlangonijr.chesslib.PieceType.PAWN;
import static com.github.bhlangonijr.chesslib.PieceType.QUEEN;
import static com.github.bhlangonijr.chesslib.PieceType.ROOK;
import static com.github.bhlangonijr.chesslib.Square.A1;
import static com.github.bhlangonijr.chesslib.Square.A2;
import static com.github.bhlangonijr.chesslib.Square.A7;
import static com.github.bhlangonijr.chesslib.Square.A8;
import static com.github.bhlangonijr.chesslib.Square.B1;
import static com.github.bhlangonijr.chesslib.Square.B2;
import static com.github.bhlangonijr.chesslib.Square.B7;
import static com.github.bhlangonijr.chesslib.Square.B8;
import static com.github.bhlangonijr.chesslib.Square.C1;
import static com.github.bhlangonijr.chesslib.Square.C2;
import static com.github.bhlangonijr.chesslib.Square.C7;
import static com.github.bhlangonijr.chesslib.Square.C8;
import static com.github.bhlangonijr.chesslib.Square.D1;
import static com.github.bhlangonijr.chesslib.Square.D8;
import static com.github.bhlangonijr.chesslib.Square.F1;
import static com.github.bhlangonijr.chesslib.Square.F2;
import static com.github.bhlangonijr.chesslib.Square.F7;
import static com.github.bhlangonijr.chesslib.Square.F8;
import static com.github.bhlangonijr.chesslib.Square.G1;
import static com.github.bhlangonijr.chesslib.Square.G2;
import static com.github.bhlangonijr.chesslib.Square.G7;
import static com.github.bhlangonijr.chesslib.Square.G8;
import static com.github.bhlangonijr.chesslib.Square.H1;
import static com.github.bhlangonijr.chesslib.Square.H2;
import static com.github.bhlangonijr.chesslib.Square.H7;
import static com.github.bhlangonijr.chesslib.Square.H8;

public class ChessUtil {

    public static Map<PieceType, Set<Square>> getSpawnSquaresForWhite() {
        return Map.of(PAWN, new HashSet<>(Set.of(A2, B2, C2, F2, G2, H2)),
                ROOK, new HashSet<>(Set.of(A1, H1)),
                KNIGHT, new HashSet<>(Set.of(B1, G1)),
                BISHOP, new HashSet<>(Set.of(C1, F1)),
                QUEEN, new HashSet<>(Set.of(D1))
        );
    }

    public static Map<PieceType, Set<Square>> getSpawnSquaresForBlack() {
        return Map.of(PAWN, new HashSet<>(Set.of(A7, B7, C7, F7, G7, H7)),
                ROOK, new HashSet<>(Set.of(A8, H8)),
                KNIGHT, new HashSet<>(Set.of(B8, G8)),
                BISHOP, new HashSet<>(Set.of(C8, F8)),
                QUEEN, new HashSet<>(Set.of(D8))
        );
    }

    public static int getPieceCost(PieceType pieceType) {
        switch (pieceType) {
            case PAWN:
                return 1;
            case KNIGHT:
            case BISHOP:
                return 3;
            case ROOK:
                return 5;
            case QUEEN:
                return 9;
            default:
                throw new IllegalArgumentException("No cost specified for piece " + pieceType);
        }
    }

    public static Board initBoard() {
        var gameContext = new GameContext(GameMode.HUMAN_VS_HUMAN, VariationType.NORMAL);
        gameContext.setStartFEN("4k3/3pp3/8/8/8/8/3PP3/4K3 w ---- - 0 1");
        return new Board(gameContext, true);
    }
}
