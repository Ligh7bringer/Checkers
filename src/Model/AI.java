package Model;

import Controller.MoveController;

import java.util.ArrayList;
import java.util.Random;

public class AI {
    private Board board;
    private MoveController moveController;
    private Random r = new Random();

    public AI(Board b, MoveController m) {
        this.board = b;
        this.moveController = m;
    }

    public GridPosition[] getMove(Type t) {
        ArrayList<GridPosition> canJump = moveController.getAllJumps(t);
        ArrayList<GridPosition> canMove = new ArrayList<>();

        if(!canJump.isEmpty()) {
            int index = r.nextInt(canJump.size());
            GridPosition[] jump = new GridPosition[3];
            jump[0] = canJump.get(index);
            jump[1] = moveController.getPossibleJumps(jump[0].getRow(), jump[0].getCol()).get(0);
            jump[2] = new GridPosition(-1, -1);
            return jump;
        } else {
            for (Piece[] row : board.getPieces()) {
                for (Piece piece : row) {
                    if (piece != null && (piece.getType() == t || piece.getType() == Board.getCurrentKing())) {
                        GridPosition gp = piece.getGridPosition();
                        if (!moveController.getPossibleMoves(gp.getRow(), gp.getCol()).isEmpty()) {
                            canMove.add(gp);
                        }

                    }
                }
            }
            if(!canMove.isEmpty()) {
                int index = r.nextInt(canMove.size());
                GridPosition[] move = new GridPosition[2];
                move[0] = canMove.get(index);
                move[1] = moveController.getPossibleMoves(move[0].getRow(), move[0].getCol()).get(0);
                return move;

            }

            System.out.println("something went wrong!");
            return null;
        }
    }
}
