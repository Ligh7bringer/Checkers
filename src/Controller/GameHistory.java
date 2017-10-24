package Controller;

import Model.GridPosition;

import java.util.LinkedList;

public class GameHistory {
    private static LinkedList<GridPosition[]> moves = new LinkedList<>();
    private static LinkedList<GridPosition[]> copy = new LinkedList<>();
    private static int currentIndex;
    private static boolean canUndo = true;

    public static void recordMove(GridPosition source, GridPosition dest, GridPosition removedPiece) {
        GridPosition[] gps = new GridPosition[3];
        gps[0] = source;
        gps[1] = dest;
        gps[2] = removedPiece;
        moves.add(gps);
        copy.add(gps);
        currentIndex = moves.size() - 1;
    }

    public static void cleanUp() {
        if(moves.size() != 0) {
            while (moves.size() - currentIndex != 1) {
                moves.removeLast();
            }
        }
        System.out.println("SIZE: " + moves.size() + "; INDEX: " + currentIndex);
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

    public static LinkedList<GridPosition[]> getMoves() {
        return moves;
    }

    public static LinkedList<GridPosition[]> getCopy() {
        return copy;
    }
}
