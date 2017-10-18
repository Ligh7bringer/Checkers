package Controller;

import Model.Board;
import Model.GridPosition;
import Model.Piece;
import Model.Type;

import java.util.ArrayList;

public class MoveController {

    private static Board board;

    public MoveController(Board b) {
        board = b;
    }

    //this should be a separate method because we don't want to have one huge method that does everything
    //TODO can this be done in a different way without so many if...else statements?
    public ArrayList<GridPosition> getPossibleJumps(int row, int col) {
        ArrayList<GridPosition> jumps = new ArrayList<>();
        Piece piece = board.getPiece(row, col);
        try {
            if (piece.getType() == Type.WHITE || piece.getType() == Type.WHITE_KING || piece.getType() == Type.BLACK_KING ) {
                //if(board.isLegalPos(row + 1, col - 1))
                    if (board.isTileOccupied(row, col) && ( board.isTileOccupied(row + 1, col - 1)
                            && board.getPiece(row + 1, col - 1).getType() == getOppositeType(piece.getType())) && !board.isTileOccupied(row + 2, col - 2)) {
                        jumps.add(new GridPosition(row + 2, col - 2));
                    }
                //if(board.isLegalPos(row + 1, col + 1))
                    if (board.isTileOccupied(row, col) && (board.isTileOccupied(row + 1, col + 1)
                            && board.getPiece(row + 1, col + 1).getType() == getOppositeType(piece.getType())) && !board.isTileOccupied(row + 2, col + 2)) {
                        jumps.add(new GridPosition(row + 2, col + 2));
                    }
            }

            if(piece.getType() == Type.BLACK || piece.getType() == Type.BLACK_KING || piece.getType() == Type.WHITE_KING) {
                //if(board.isLegalPos(row - 1, col - 1))
                    if (board.isTileOccupied(row, col) && (board.isTileOccupied(row - 1, col - 1) && board.getPiece(row - 1, col - 1).getType() == getOppositeType(piece.getType()))
                            && !board.isTileOccupied(row - 2, col - 2)) {
                        jumps.add(new GridPosition(row - 2, col - 2));
                    }
                //if(board.isLegalPos(row - 1, col + 1))
                    if(board.isTileOccupied(row, col) && (board.isTileOccupied(row - 1, col + 1) && board.getPiece(row - 1, col + 1).getType() == getOppositeType(piece.getType()))
                            && !board.isTileOccupied(row-2, col+2)) {
                        jumps.add(new GridPosition(row-2, col+2));
                    }
            }

        } catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("getPossibleJumps array out of bounds!");
        } catch(NullPointerException e) {
            System.out.println("getPossibleJumps null pointer!");
        }

        return jumps;
    }

    //return an ArrayList with all the possible regular moves for a piece
    public ArrayList<GridPosition> getPossibleMoves(int row, int col) {
        ArrayList<GridPosition> moves = new ArrayList<>();
        try {
            if (board.isTileOccupied(row, col)) { //if source has a piece
                Piece piece = board.getPiece(row, col);
                if (piece.getType() == Type.WHITE || piece.getType() == Type.WHITE_KING || piece.getType() == Type.BLACK_KING) {
                    //legal moves for white pieces:
                    //move diagonally to the left
                    //if(col != 0 || row != 7)
                        if (!board.isTileOccupied(row + 1, col - 1) ) {
                            moves.add(new GridPosition(row + 1, col - 1));
                        }
                    //if(row != 7 || col != 7)
                        if (!board.isTileOccupied(row + 1, col + 1)) { //move diagonally to the right
                            moves.add(new GridPosition(row + 1, col + 1));
                        }
                }
                if (piece.getType() == Type.BLACK || piece.getType() == Type.WHITE_KING || piece.getType() == Type.BLACK_KING) {
                    //legal moves for black, in the opposite direction:
                    //move diagonally to the left
                    //if(row != 0 || col != 0)
                        if (!board.isTileOccupied(row - 1, col - 1)) {
                                moves.add(new GridPosition(row - 1, col - 1));
                        }
                    //if(row != 0 || col != 7)
                        if (!board.isTileOccupied(row - 1, col + 1)) { //move diagonally to the right
                            moves.add(new GridPosition(row - 1, col + 1));
                        }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("index is out of bounds in getPossibleMoves!!!");
        }

        return moves;
    }

    //a player has to jump if they can so this will be needed later
    public ArrayList<GridPosition> getAllJumps() {
        ArrayList<GridPosition> canJump = new ArrayList<>();

        for(int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                Piece p = board.getPiece(i, j);
                if(board.isTileOccupied(i, j) && (p.getType() == Board.getCurrentColour() || p.getType() == Board.getCurrentKing())) {
                    if(!getPossibleJumps(i, j).isEmpty()) {
                        System.out.println("This one can jump: " + i + ", " + j);
                        //System.out.println("Current King: " + Board.getCurrentKing());
                        canJump.add(new GridPosition(i, j));
                    }
                }
            }
        }

        return canJump;
    }

    //undoes the last play
    public static void undoLastMove() {
        GridPosition[] gps;
        if(!GameHistory.getMoves().isEmpty()) {
            gps = GameHistory.getMoves().removeLast();

            GridPosition source = gps[1];
            GridPosition dest = gps[0];
            GridPosition removedPiece = gps[2];

            board.movePiece(source.getRow(), source.getCol(), dest.getRow(), dest.getCol());
            if(removedPiece != null)
                board.getPieces()[removedPiece.getRow()][removedPiece.getCol()] = new Piece(Board.getCurrentColour(), removedPiece);

            board.switchPlayer();
        } else {
            System.out.println("No more moves!");
        }
    }

    //returns the opposite type
    private Type getOppositeType(Type t) {
        if(t == Type.WHITE || t == Type.WHITE_KING) {
            return Type.BLACK;
        }

        return Type.WHITE;
    }

}
