package Model;

//this class defines a position on the board
public class GridPosition {
    private int row; //the row a piece is on
    private int col; //the column a piece is on

    //constructor
    public GridPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    //custom toString mainly used for debugging
    @Override
    public String toString() {
        return this.getRow() + ", " + getCol();
    }

    //custom equals method which is used when calling ArrayList.contains()
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!GridPosition.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final GridPosition other = (GridPosition) obj;

        return (this.getRow() == other.getRow() && this.getCol() == other.getCol());
    }

    //getters
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

}
