package Controller;

import Model.Board;
import Model.GameType;

public class BoardController {
    private static Board board;
    private static GameType gameType;

    public BoardController(Board b) {
        board = b;
    }

    public static void setupGame(GameType t) {
        gameType = t;
        if(t == GameType.AI_VS_AI) {
            board.setupAiGame();
            board.startAiTimer();
        }
        else
            board.startGame(t);
    }

    public static void replayGame(String name) {
        if(!ReplayHandler.parseReplay(name).isEmpty()) {
            board.replayGame(ReplayHandler.parseReplay(name));
            Board.startTimer();
        }
    }

    public static GameType getGameType() {
        return gameType;
    }


}
