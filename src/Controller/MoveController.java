package Controller;

import Model.Board;
import Model.GridPosition;
import Model.Piece;
import Model.Type;
import com.sun.deploy.security.ValidationState;
import sun.awt.image.ImageWatched;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

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

    //a player has to jump if they can so this will be needed later
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
        GridPosition[] gps;
        if(!GameHistory.getMoves().isEmpty()) {
            try {
                gps = GameHistory.getMoves().get(GameHistory.getCurrentIndex()); //get the last move from game history
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Nothing to undo!");
                return false;
            }
            GameHistory.decrementIndex(); //decrement current index
            //GameHistory.cleanUp();

            GridPosition source = gps[1]; //get source
            GridPosition dest = gps[0]; //get destination
            GridPosition removedPiece = gps[2]; //get removed piece if move was jump

            board.movePiece(source.getRow(), source.getCol(), dest.getRow(), dest.getCol()); //undo
            if(removedPiece != null)
                board.getPieces()[removedPiece.getRow()][removedPiece.getCol()] = new Piece(TurnManager.getCurrentColour(), removedPiece); //add the removed piece

            TurnManager.nextTurn(); //switch player
            return true;
        } else {
            System.out.println("No more moves!"); //debug
            return false;
        }
    }

    public static void redoLastMove() {
        GridPosition[] gps;
        if(!GameHistory.getMoves().isEmpty()) {
            try {
                gps = GameHistory.getMoves().get(GameHistory.getCurrentIndex() + 1); //get the last move from game history
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Nothing to redo!");
                return;
            }
            GameHistory.incrementIndex(); //increment current index

            GridPosition source = gps[1]; //get source
            GridPosition dest = gps[0]; //get destination
            GridPosition removedPiece = gps[2]; //get removed piece if move was jump

            board.movePiece(dest.getRow(), dest.getCol(), source.getRow(), source.getCol()); //redo
            if(removedPiece != null)
                board.removePiece(removedPiece.getRow(), removedPiece.getCol());

            GameHistory.cleanUp();
            if(removedPiece != null)
                GameHistory.recordMove(dest, source, removedPiece);
            else
                GameHistory.recordMove(dest, source, null);

            TurnManager.nextTurn();
        } else {
            System.out.println("No more moves");
        }
    }

    //returns the opposite type
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
