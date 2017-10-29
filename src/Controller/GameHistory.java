package Controller;
import Model.Move;
import java.util.LinkedList;

//this class stores all moves in a game so they can be replayed or undone/redone
public class GameHistory {
    private static LinkedList<Move> moves = new LinkedList<>(); //stores all moves in a game
    private static LinkedList<Move> copy = new LinkedList<>(); //this is used for the InformationPanel display of moves (this is probably bad?)
    private static Move redoMove; //the last undone move is stored as the redo move

    //stores a move in the moves LinkedList
    public static void recordMove(Move m) {
        moves.add(m);
        copy.add(m);
    }

    //returns the last move
    static Move getUndoMove() {
        if(moves.size() < 1)
            return null;
        else {
            redoMove = moves.removeLast();
            return redoMove;
        }
    }

    //returns the last undone move so it can be redone
    static Move getRedoMove() {
        Move temp = redoMove;
        redoMove = null;
        return temp;
    }

    //deletes everything from both linked lists
    public static void clearAll() {
        copy.clear();
        moves.clear();
    }

    //getters
    public static LinkedList<Move> getMoves() {
        return moves;
    }

    public static LinkedList<Move> getCopy() {
        return copy;
    }
}
