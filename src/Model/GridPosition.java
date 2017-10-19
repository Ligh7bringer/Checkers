package Model;

public class GridPosition {
    private int row;
    private int col;

    public GridPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        return this.getRow() + " " + getCol();
    }

    //turns out equals needs to be overridden in order to be able to check if a list contains an object of this type
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!GridPosition.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final GridPosition other = (GridPosition) obj;
        if ((this.getRow() != other.getRow() || this.getCol() != other.getCol())) {
            return false;
        }

        return true;
    }

}
