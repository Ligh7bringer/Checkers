package Controller;

import Model.*;
import java.util.ArrayList;

//this class handles most checker move logic
public class MoveController {
    private static Board board;

    public MoveController(Board b) {
        board = b;
    }

    //returns all possible landing positions after a jump
    public ArrayList<GridPosition> getPossibleJumps(int row, int col) {
        ArrayList<GridPosition> jumps = new ArrayList<>();
        Piece piece = board.getPiece(row, col);
        try {
            if (piece.getType() == Type.WHITE || piece.getType() == Type.WHITE_KING || piece.getType() == Type.BLACK_KING ) {
                if (board.isTileOccupied(row, col) && ( board.isTileOccupied(row + 1, col - 1)
                            && getOppositeType(piece.getType()).contains(board.getPiece(row + 1, col - 1).getType())) && !board.isTileOccupied(row + 2, col - 2)) {
                    jumps.add(new GridPosition(row + 2, col - 2));
                }
                if (board.isTileOccupied(row, col) && (board.isTileOccupied(row + 1, col + 1)
                        && getOppositeType(piece.getType()).contains(board.getPiece(row + 1, col + 1).getType())) && !board.isTileOccupied(row + 2, col + 2)) {
                    jumps.add(new GridPosition(row + 2, col + 2));
                }
            }
            if(piece.getType() == Type.BLACK || piece.getType() == Type.WHITE_KING || piece.getType() == Type.BLACK_KING) {
                if (board.isTileOccupied(row, col) && (board.isTileOccupied(row - 1, col - 1) && getOppositeType(piece.getType()).contains(board.getPiece(row - 1, col - 1).getType()))
                            && !board.isTileOccupied(row - 2, col - 2)) {
                    jumps.add(new GridPosition(row - 2, col - 2));
                }
                if(board.isTileOccupied(row, col) && (board.isTileOccupied(row - 1, col + 1) && getOppositeType(piece.getType()).contains(board.getPiece(row - 1, col + 1).getType()))
                        && !board.isTileOccupied(row-2, col+2)) {
                    jumps.add(new GridPosition(row-2, col+2));
                }
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("getPossibleJumps array out of bounds exception!");
        } catch(NullPointerException e) {
            System.out.println("getPossibleJumps null pointer exception!");
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
                    if (!board.isTileOccupied(row + 1, col - 1) ) {
                        moves.add(new GridPosition(row + 1, col - 1));
                    }
                    if (!board.isTileOccupied(row + 1, col + 1)) { //move diagonally to the right
                        moves.add(new GridPosition(row + 1, col + 1));
                    }
                }
                if (piece.getType() == Type.BLACK || piece.getType() == Type.WHITE_KING || piece.getType() == Type.BLACK_KING) {
                    //legal moves for black, in the opposite direction:
                    //move diagonally to the left
                    if (!board.isTileOccupied(row - 1, col - 1)) {
                        moves.add(new GridPosition(row - 1, col - 1));
                    }
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

    //returns all pieces that can jump
    public ArrayList<GridPosition> getAllJumps(Type t) {
        ArrayList<GridPosition> canJump = new ArrayList<>();

        for(int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                Piece p = board.getPiece(i, j);
                //System.out.println(Board.getCurrentKing());
                if(board.getPieces()[i][j] != null && (p.getType() == t || p.getType() == TurnManager.getCurrentKing())) {
                    if(!getPossibleJumps(i, j).isEmpty()) {
                        //System.out.println("This one can jump: " + i + ", " + j);
                        canJump.add(new GridPosition(i, j));
                    }
                }
            }
        }

        return canJump;
    }

    //undoes the last play
    public static boolean undoLastMove() {
        Move move = GameHistory.getUndoMove();
        if(move != null) {
            Piece removedPiece = move.getRemoved(); //get removed piece if move was jump
            Piece source = move.getSource();

            GridPosition s = move.getSource().getGridPosition();
            GridPosition d = move.getDestination().getGridPosition();

            System.out.println("Undoing: " + d.toString() + " -> " + s.toString());
            //board.movePiece(d.getRow(), d.getCol(), s.getRow(), s.getCol()); //undo
            board.removePiece(d.getRow(), d.getCol());
            board.addPiece(s, source.getType());
            if(removedPiece != null) {
                GridPosition r = removedPiece.getGridPosition();
                board.addPiece(r, removedPiece.getType()); //add the removed piece
                //TurnManager.nextTurn();
            }

            TurnManager.nextTurn(); //switch player
            return true;
        } else {
            System.out.println("No more moves!"); //debug
            return false;
        }
    }

    //redoes the last undone play
    public static void redoLastMove() {
        Move move = GameHistory.getRedoMove();
        if(move != null) {

            Piece source = move.getSource(); //get source
            Piece dest = move.getDestination(); //get destination

            System.out.println("redoing: " + dest.toString() + " -> " + source.toString());

            GridPosition s = source.getGridPosition();
            GridPosition d = dest.getGridPosition();
            board.validateMove(s.getRow(), s.getCol(), d.getRow(), d.getCol()); //redo
        } else {
            System.out.println("No more moves");
        }
    }

    //returns the opposite type of Type t
    private ArrayList<Type> getOppositeType(Type t) {
        ArrayList<Type> opposite = new ArrayList<>();
        if(t == Type.WHITE) {
            opposite.add(Type.BLACK);
        } else if(t == Type.BLACK) {
            opposite.add(Type.WHITE);
        } else if(t == Type.WHITE_KING) {
            opposite.add(Type.BLACK);
            opposite.add(Type.BLACK_KING);
        } else if(t == Type.BLACK_KING) {
            opposite.add(Type.WHITE);
            opposite.add(Type.WHITE_KING);
        }

        return opposite;
    }

}
