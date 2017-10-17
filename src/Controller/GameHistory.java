package Controller;

import Model.GridPosition;

import java.util.LinkedList;

public class GameHistory {
    private static LinkedList<GridPosition[]> moves = new LinkedList<>();
    private static LinkedList<GridPosition[]> copy = new LinkedList<>();

    public static void recordMove(GridPosition source, GridPosition dest, GridPosition removedPiece) {
        GridPosition[] gps = new GridPosition[3];
        gps[0] = source;
        gps[1] = dest;
        gps[2] = removedPiece;
        moves.add(gps);
        copy.add(gps);
    }

    public static LinkedList<GridPosition[]> getMoves() {
        return moves;
    }

    public static LinkedList<GridPosition[]> getCopy() {
        return copy;
    }
}
