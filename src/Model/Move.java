package Model;

//this class is used with the redo/undo features and replays
//stores a single move from the game
public class Move {
    //the reason Pieces are used and not just GridPosition is that Pieces have a type and a GridPosition
    private Piece source; //initial location of the piece
    private Piece destination; //position after a move
    private Piece removed; //taken piece if the move was a jump

    //simple constructor
    public Move(Piece s, Piece d, Piece r) {
        source = s;
        destination = d;
        removed = r;
    }

    //getters
    public Piece getSource() {
        return source;
    }

    public Piece getDestination() {
        return destination;
    }

    public Piece getRemoved() {
        return removed;
    }

    //this returns coordinates that are more easy to understand for humans (x first, then y)
    @Override
    public String toString() {
        return source.getType() + ": " + (source.getGridPosition().getCol()+1) + ", " + (source.getGridPosition().getRow()+1) + " -> "
                + (destination.getGridPosition().getCol() + 1) + ", " + (destination.getGridPosition().getRow() + 1);
    }
}
