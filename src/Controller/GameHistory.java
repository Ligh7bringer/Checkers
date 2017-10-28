package Controller;
import Model.GridPosition;
import Model.Move;
import Model.Piece;

import java.util.LinkedList;

public class GameHistory {
    private static LinkedList<Move> moves = new LinkedList<>();
    private static LinkedList<Move> copy = new LinkedList<>();
    private static int currentIndex;
    private static Move redoMove;
    //private static Piece source, dest;

    public static void recordMove(Move m) {
        moves.add(m);
        copy.add(m);
        currentIndex = moves.size() - 1;
    }

     public static void cleanUp() {
        if(moves.size() != 0) {
            while (moves.size() - currentIndex != 1) {
                moves.removeLast();
            }
        }
        //System.out.println("SIZE: " + moves.size() + "; INDEX: " + currentIndex);
    }

    //TODO IMPLEMENT THESE METHODS AS THE NEW UNDO/REDO SYSTEM
    static Move getUndoMove() {
        redoMove = moves.removeLast();
        return redoMove;
    }

    static Move getRedoMove() {
        Move temp = redoMove;
        redoMove = null;
        return temp;
    }


    static void decrementIndex() {
        currentIndex--;
        if(currentIndex < 0)
            currentIndex = 0;
    }

    static void incrementIndex() {
        currentIndex++;
        if(currentIndex >= moves.size())
            currentIndex = moves.size() - 1;
    }

    public static void clearAll() {
        copy.clear();
        moves.clear();
    }

    public static int getCurrentIndex() {
        return currentIndex;
    }

    public static LinkedList<Move> getMoves() {
        return moves;
    }

    public static LinkedList<Move> getCopy() {
        return copy;
    }
}
