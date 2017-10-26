package Controller;

public class TurnManager {
    private static int numberOfTurns;

    public static void nextTurn() {
        numberOfTurns++;
    }

    public static int getCurrentPlayer() {
        return (numberOfTurns % 2) + 1;
    }

    public static void reset() {
        numberOfTurns = 0;
    }
}
