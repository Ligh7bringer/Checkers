package Controller;

import Model.Type;

//this class handles everything to do with turns
public class TurnManager {
    private static int numberOfTurns; //store the number of turns

    public static void nextTurn() {
        numberOfTurns++;
    } //increment number of turns

    //returns 1 if numberOfTurns is even, 2 if it's odd which is the current player
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

    //returns the next player's colour
    public static Type getNextColour() {
        if(getCurrentPlayer() == 1) {
            return Type.WHITE;
        } else {
            return Type.BLACK;
        }
    }

}
