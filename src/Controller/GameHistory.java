package Controller;
import Model.GridPosition;
import Model.Move;
import java.util.LinkedList;

public class GameHistory {
    private static LinkedList<Move> moves = new LinkedList<>();
    private static LinkedList<Move> copy = new LinkedList<>();
    private static int currentIndex;
    private static Move redoMove;

    public static void recordMove(Move m) {
        moves.add(m);
        copy.add(m);
        currentIndex = moves.size() - 1;
    }

    static Move getUndoMove() {
        if(moves.size() < 1)
            return null;
        else {
            redoMove = moves.removeLast();
            return redoMove;
        }
    }

    static Move getRedoMove() {
        Move temp = redoMove;
        redoMove = null;
        return temp;
    }

    public static void clearAll() {
        copy.clear();
        moves.clear();
    }

    public static LinkedList<Move> getMoves() {
        return moves;
    }

    public static LinkedList<Move> getCopy() {
        return copy;
    }
}
