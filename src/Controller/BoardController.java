package Controller;

import Model.Board;
import Model.GameType;

public class BoardController {
    private static Board board;

    public BoardController(Board b) {
        board = b;
    }

    public static void setupGame(GameType t) {
        board.startGame(t);
    }
}
