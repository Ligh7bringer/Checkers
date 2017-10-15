import java.util.LinkedList;

public class GameHistory {
    private static LinkedList<GridPosition[]> moves = new LinkedList<>();

    public static void recordMove(GridPosition source, GridPosition dest) {
        GridPosition[] gps = new GridPosition[2];
        gps[0] = source;
        gps[1] = dest;
        moves.add(gps);
    }

    public static LinkedList<GridPosition[]> getMoves() {
        return moves;
    }
}
