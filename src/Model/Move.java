package Model;

public class Move {

    private Piece source;
    private Piece destination;
    private Piece removed;

    public Move(Piece s, Piece d, Piece r) {
        source = s;
        destination = d;
        removed = r;
    }

    public Piece getSource() {
        return source;
    }

    public Piece getDestination() {
        return destination;
    }

    public Piece getRemoved() {
        return removed;
    }

    @Override
    public String toString() {
        return source.toString() + " -> " + destination.toString();
    }
}
