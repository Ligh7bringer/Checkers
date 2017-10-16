package Model;

public class GridPosition {
    private int x;
    private int y;

    public GridPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return this.getX() + ", " + getY();
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
        if ((this.getX() != other.getX() || this.getY() != other.getY())) {
            return false;
        }

        return true;
    }

}
