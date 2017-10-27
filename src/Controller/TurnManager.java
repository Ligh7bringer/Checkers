package Controller;

import Model.Type;
import UI.InformationPanel;

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

    //get the colour of the player whose turn it is
    public static Type getCurrentColour() {
        if(getCurrentPlayer() == 1)
            return Type.BLACK;
        else
            return Type.WHITE;
    }

    //returns current player's king colour
    public static Type getCurrentKing() {
        if(getCurrentPlayer() == 1)
            return Type.BLACK_KING;
        else
            return Type.WHITE_KING;
    }

}
