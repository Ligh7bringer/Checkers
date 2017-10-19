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
        for(GridPosition[] gp : moves)
            System.out.println(gp[0].toString() + "; " + gp[1].toString());
    }

    public static void cleanUp() {
        while(moves.size() - currentIndex != 1) {
            moves.removeLast();
        }
    }

    public static void decrementIndex() {
        currentIndex--;
    }

    public static void incrementIndex() {
        currentIndex++;
        if(currentIndex >= moves.size())
            currentIndex = moves.size() - 1;
    }

    public static boolean canUndo() {
        return canUndo;
    }

    public static void setCanUndo(boolean canUndo) {
        GameHistory.canUndo = canUndo;
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
