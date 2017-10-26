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
            //System.out.println(index);
            GridPosition[] jump = new GridPosition[3];
            jump[0] = canJump.get(index);
            index = r.nextInt(moveController.getPossibleJumps(jump[0].getRow(), jump[0].getCol()).size());
            jump[1] = moveController.getPossibleJumps(jump[0].getRow(), jump[0].getCol()).get(index);
            jump[2] = new GridPosition(-1, -1);
            return jump;
        } else {
            for (int i =0; i < 8; i++) {
                for (int j =0; j < 8; j++) {
                    if (board.getPieces()[i][j] != null && (board.getPieces()[i][j].getType() == t || board.getPieces()[i][j].getType() == getKing(t))) {
                        //GridPosition gp = new GridPosition(i, j);
                        if (!moveController.getPossibleMoves(i, j).isEmpty()) {
                            canMove.add(new GridPosition(i, j));
                        }

                    }
                }
            }

            if(!canMove.isEmpty()) {
                int index = r.nextInt(canMove.size());
                System.out.println(index);
                GridPosition[] move = new GridPosition[2];
                move[0] = canMove.get(index);
                index = r.nextInt(moveController.getPossibleMoves(move[0].getRow(), move[0].getCol()).size());
                move[1] = moveController.getPossibleMoves(move[0].getRow(), move[0].getCol()).get(index);

                return move;
            }

            System.out.println("something went wrong!");
            return null;
        }
    }

    private Type getKing(Type t) {
        if(t == Type.WHITE)
            return Type.WHITE_KING;

        return Type.BLACK_KING;
    }
}
