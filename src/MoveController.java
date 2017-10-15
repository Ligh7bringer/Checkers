import java.util.ArrayList;

public class MoveController {

    private Board board;

    public MoveController(Board b) {
        board = b;
    }

    //returns true if a regular move (move diagonally one tile) is legal
    public boolean isMoveLegal(int gridX, int gridY, int destX, int destY) {
        if(board.isTileOccupied(gridX, gridY) && !board.isTileOccupied(destX, destY)) { //if source has a piece and destination is an empty tile
            Piece piece = board.getPiece(gridX, gridY);
            if(piece.getType() == Type.WHITE) {
                //legal moves for white pieces:
                //move diagonally to the left
                if(destX - gridX == 1 && destY - gridY == -1) {
                    return true;
                } else if (destX - gridX == 1 && destY - gridY == 1) { //move diagonally to the right
                    return true;
                }
            } else if (piece.getType() == Type.BLACK) {
                //legal moves for black, in the opposite direction:
                //move diagonally to the left
                if(destX - gridX == -1 && destY - gridY == -1) {
                    return true;
                } else if (destX - gridX == -1 && destY - gridY == 1) { //move diagonally to the right
                    return true;
                }
            }
        }

        return false;
    }

    //TODO improve this
    public boolean isMoveJump(int row, int col, int destRow, int destCol) {
        try {
            if( (board.isTileOccupied(row, col) && board.getPiece(row, col).getType() == Type.WHITE) && (board.isTileOccupied(row+1, col-1)
                    && board.getPiece(row+1, col-1).getType() == Type.BLACK) && !board.isTileOccupied(row+2, col-2) ) {
                board.movePiece(row, col, row+2, col-2);
                board.removePiece(row+1, col-1);
                return true;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Out of bounds exception.");
        }

        return false;
    }

    //this should be a separate method because we don't want to have one huge method that does everything
    //TODO can this be done in a different way without so many if...else statements?
    public ArrayList<GridPosition> getPossibleJumps(int row, int col) {
        ArrayList<GridPosition> jumps = new ArrayList<>();
        try {
            if (board.getPiece(row, col).getType() == Type.WHITE) {
                if (board.isTileOccupied(row, col) && (board.isTileOccupied(row + 1, col - 1)
                        && board.getPiece(row + 1, col - 1).getType() == Type.BLACK) && !board.isTileOccupied(row + 2, col - 2)) {
                    jumps.add(new GridPosition(row + 2, col - 2));
                }
                if (board.isTileOccupied(row, col) && (board.isTileOccupied(row + 1, col + 1)
                        && board.getPiece(row + 1, col + 1).getType() == Type.BLACK) && !board.isTileOccupied(row + 2, col + 2)) {
                    jumps.add(new GridPosition(row + 2, col + 2));
                }
            } else {
                if (board.isTileOccupied(row, col) && (board.isTileOccupied(row - 1, col - 1) && board.getPiece(row - 1, col - 1).getType() == Type.WHITE)
                        && !board.isTileOccupied(row - 2, col - 2)) {
                    jumps.add(new GridPosition(row - 2, col - 2));
                }
                if(board.isTileOccupied(row, col) && (board.isTileOccupied(row - 1, col + 1) && board.getPiece(row - 1, col + 1).getType() == Type.WHITE)
                        && !board.isTileOccupied(row-2, col+2)) {
                    jumps.add(new GridPosition(row-2, col+2));
                }
            }

        } catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("FUCK SAKE!!");
        }

        return jumps;
    }

    //a player has to jump if they can so this will be needed later
    //TODO change this so it actually returns all pieces which can jump
    public void hasToJump() {
        for(Piece[] row : board.getPieces()) {
            for(Piece p : row) {
                if(p != null && p.getType() == board.getCurrentColour()) {
                    System.out.println(p.getType());
                }
            }
        }
    }

    //so we need this method to enforce jumping when possible TODO IMPLEMENT maybe find a better way?
    private boolean canJump(int gridX, int gridY) {
        return false;
    }

}
