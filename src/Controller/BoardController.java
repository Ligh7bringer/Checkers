package Controller;

import Model.Board;
import Model.GameType;
import Model.Move;

import java.util.LinkedList;

public class BoardController {
    private static Board board;
    private static GameType gameType;
    private static LinkedList<Move> replay;

    public BoardController(Board b) {
        board = b;
    }

    public static void setupGame(GameType t) {
        gameType = t;
        if(t == GameType.AI_VS_AI) {
            board.setupAiGame();
            board.startAiTimer();
        } else
            board.startGame(t);
    }

    public static void replayGame(String name) {
        if(!ReplayHandler.parseReplay(name).isEmpty()) {
            replay = ReplayHandler.parseReplay(name);
            board.replayGame();
            Board.startTimer();
        }
    }

    public static GameType getGameType() {
        return gameType;
    }

    public static int[] getPieceCount() {
        return board.getPieceCount();
    }

    public static LinkedList<Move> getReplay() {
        return replay;
    }
}
