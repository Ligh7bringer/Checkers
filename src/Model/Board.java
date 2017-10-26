package Model;

import Controller.BoardController;
import Controller.GameHistory;
import Controller.MoveController;
import Controller.TurnManager;
import UI.InformationPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;


public class Board {
    public static final int SIZE = 8; //board width and height, i.e. number of tiles per row and column
    public static final int TILE_WIDTH = 80; // dimensions of tiles
    public static final int TILE_HEIGHT = 80;

    //position of tile which was clicked
    private GridPosition highlightedTile;
    private ArrayList<GridPosition> availableTiles = new ArrayList<>();

    private int sourceX, sourceY, destX, destY;

    //store all the pieces here
    private Piece[][] pieces;

    //instance of move controller
    private MoveController moveController;
    private BoardController boardController;

    private GameType gameType;
    private AI ai;

    private Timer aiTimer;

    //constructor for board
    public Board(){
        pieces = new Piece[SIZE][SIZE]; //create the array
        moveController = new MoveController(this);
        boardController = new BoardController(this);
        ai = new AI(this, moveController);

    }

    public void startGame(GameType t) {
        TurnManager.reset();
        if(timer != null && timer.isRunning())
            timer.stop();
        if(aiTimer != null && aiTimer.isRunning())
            aiTimer.stop();

        gameType = t;
        InformationPanel.clearMoves();
        GameHistory.clearAll();
        pieces = new Piece[SIZE][SIZE];
        initialiseBoard();
    }

    public void setupAiGame() {
        startGame(GameType.AI_VS_AI);
        aiTimer = new Timer(800, e -> {
            makeMove();
        });
    }

    public void startAiTimer() {
        aiTimer.start();
    }

    private void makeMove() {
        if (gameType == GameType.AI_VS_AI) {
            GridPosition[] gps = ai.getMove(getCurrentColour());
            if(gps != null)
                validateMove(gps[0].getRow(), gps[0].getCol(), gps[1].getRow(), gps[1].getCol());
            else {
                System.out.println("Game over! no more moves!");
                aiTimer.stop();
            }
            if(isWinner()) {
                System.out.println("Game over! no more pieces!");
                aiTimer.stop();
            }
        }
    }

    //this creates the starting layout of the board
    //is there a more efficient way to do this?
    private void initialiseBoard() {
        for(int row=0; row < (SIZE); row+=2){
            pieces[5][row] = new Piece(Type.BLACK, new GridPosition(5, row));
            pieces[7][row] = new Piece(Type.BLACK, new GridPosition(7, row));
            pieces[6][row+1] = new Piece(Type.BLACK, new GridPosition(6, row+1));
        }
        for(int row=1; row < (SIZE); row+=2){
            pieces[0][row] = new Piece(Type.WHITE, new GridPosition(0, row));
            pieces[2][row] = new Piece(Type.WHITE, new GridPosition(2, row));
            pieces[1][row-1] = new Piece(Type.WHITE, new GridPosition(5, row-1));
        }
    }

    //save the coordinates of the tile which has been clicked
    public void addSource(int x, int y) {
        availableTiles = new ArrayList<>();
        sourceX = convertToGridCoords(x);
        sourceY = convertToGridCoords(y);
        highlightTile(sourceX, sourceY);

        if(validatePlayer(sourceY, sourceX)) {
            availableTiles.addAll(moveController.getPossibleJumps(sourceY, sourceX));
            if(availableTiles.isEmpty())
                availableTiles = moveController.getPossibleMoves(sourceY, sourceX);
        }
    }

    //save the coordinates of the destination tile
    public void addDestination(int x, int y) {
        destX = convertToGridCoords(y);
        destY = convertToGridCoords(x);
        validateMove(sourceY, sourceX, destX, destY);
    }

    //the board has its own validateMove
    //parameters: source x, source y, destination x, destination y
    private void validateMove(int row, int col, int destRow, int destCol) {
        if(validatePlayer(row, col)) { //make sure the player is trying to move their own piece
            //force the player to jump if they can
            if((!moveController.getAllJumps(getCurrentColour()).isEmpty() && !moveController.getAllJumps(getCurrentColour()).contains(new GridPosition(row, col))) || (!moveController.getPossibleJumps(row, col).isEmpty())
                    && !moveController.getPossibleJumps(row, col).contains(new GridPosition(destRow, destCol))) {
                System.out.println("You have to take a jump if you have one available!");
            } else if(moveController.getPossibleJumps(row, col).contains(new GridPosition(destRow, destCol))) { //jump
                boolean isKing = getPiece(row, col).getType() == Type.WHITE_KING || getPiece(row, col).getType() == Type.BLACK_KING;
                movePiece(row, col, destRow, destCol);
                getPiece(destRow, destCol).crownPiece();
                GridPosition gp = removeEnemyAfterJump(row, col, destRow, destCol, isKing);
                GameHistory.cleanUp();
                GameHistory.recordMove(new GridPosition(row, col), new GridPosition(destRow, destCol), gp); //save the move
                if(!moveController.getPossibleJumps(destRow, destCol).isEmpty()) {
                    TurnManager.nextTurn();
                    System.out.println("Double jump!!!");
                }
                TurnManager.nextTurn();
            } else if (!moveController.getPossibleMoves(row, col).isEmpty() && moveController.getPossibleMoves(row, col).contains(new GridPosition(destRow, destCol))) { //regular move
                movePiece(row, col, destRow, destCol);
                getPiece(destRow, destCol).crownPiece();
                GameHistory.cleanUp();
                GameHistory.recordMove(new GridPosition(row, col), new GridPosition(destRow, destCol), null);
                TurnManager.nextTurn();
            } else {
                System.out.println("Illegal move!");
            }
        } else {
            System.out.println("Invalid piece!");
        }

        if(isWinner())
            System.out.println("game over!");

        if(!availableTiles.isEmpty())
            availableTiles.clear();
        highlightedTile = null;
    }

    //the board should paint itself
    public void paintComponent(Graphics2D g2d) {
        //starting coordinates = top left corner of the window
        int x = 0;
        int y = 0;

        //loop through the board, draw the tiles and look for pieces
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if ((i % 2 == 1 && j % 2 == 1) || (j % 2 == 0 && i % 2 == 0)) { //some modulo magic to do alternating colours for the board tiles
                    //set appropriate colours
                    g2d.setColor(new Color(153, 0, 0));
                } else {
                    g2d.setColor(Color.black);
                }

                //actually draw the tile
                g2d.fillRect(x, y, TILE_WIDTH, TILE_HEIGHT);

                //if there's a piece on that tile
                if(pieces[i][j] != null)
                    pieces[i][j].paintComponent(g2d, x, y); //draw it

                //move right
                x = x + TILE_WIDTH;
            }
            //when it's time for the next row, move to the very left (x = 0)
            x = 0;
            //move down, next row
            y += TILE_HEIGHT;
        }

        if(highlightedTile != null) {
            g2d.setColor(Color.YELLOW);
            g2d.drawRect(highlightedTile.getRow() * TILE_WIDTH, highlightedTile.getCol() * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
        }

        if(availableTiles != null) {
            g2d.setColor(new Color(153, 255, 51));
            for(GridPosition gp : availableTiles) {
                g2d.fillRect(gp.getCol() * TILE_WIDTH, gp.getRow() * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
            }
        }

    }

    //simple update method which detects which ensures a piece is crowned
    public void update() {
        if(gameType == GameType.VS_AI && getCurrentPlayer() == 2) {
            GridPosition[] gps = ai.getMove(Type.WHITE);
            validateMove(gps[0].getRow(), gps[0].getCol(), gps[1].getRow(), gps[1].getCol());
        }
    }

    //makes sure a player can only move their own pieces during their turn
    private boolean validatePlayer(int row, int col) {
        if(getPiece(row, col).getType() == Type.EMPTY) {
            //System.out.println("empty tile at" + sourceX + ", " + sourceY);
            return false;
        } else if((getPiece(row, col).getType() == Type.BLACK || getPiece(row, col).getType() == Type.BLACK_KING ) && getCurrentPlayer() == 1) {
            return true;
        } else if((getPiece(row, col).getType() == Type.WHITE || getPiece(row, col).getType() == Type.WHITE_KING )&& getCurrentPlayer() == 2) {
            return true;
        }

        return false;
    }

    //moves piece from one tile to another
    public void movePiece(int sourceX, int sourceY, int destX, int destY) {
        pieces[destX][destY] = pieces[sourceX][sourceY]; //move to new position
        pieces[sourceX][sourceY] = null; //make old position null, it should be empty
        try {
            pieces[destX][destY].setGridPosition(new GridPosition(destX, destY));
        } catch (NullPointerException e) {
            System.out.println("Nothing to undo");
        }
    }

    //convert window coordinates to position in grid
    private int convertToGridCoords(int screenCoord) {
        return screenCoord / TILE_WIDTH; //can be divided by tile height as well because they are the same, a tile is square
    }

    //removes piece at position gridX, gridY
    public void removePiece(int gridX, int gridY) {
        pieces[gridX][gridY] = null;
    }

    //calculates the position of a piece which needs to be removed after a jump and returns its coordinates
    private GridPosition removeEnemyAfterJump(int row, int col, int destRow, int destCol, boolean isKing) {
        int rowDiff = destRow - row;
        int colDiff = destCol - col;
        if(getCurrentPlayer() == 2 || isKing) {
            if(rowDiff > 0 && colDiff > 0) {
                removePiece(row + 1, col + 1);
                return new GridPosition(row + 1, col + 1);
            }
            if(rowDiff > 0 && colDiff < 0) {
                removePiece(row + 1, col - 1);
                return new GridPosition(row + 1, col - 1);
            }
        }
        if(getCurrentPlayer() == 1 || isKing) {
            if(rowDiff < 0 && colDiff > 0) {
                removePiece(row - 1, col + 1);
                return new GridPosition(row - 1, col + 1);
            }
            if(rowDiff < 0 && colDiff < 0) {
                removePiece(row - 1, col - 1);
                return new GridPosition(row - 1, col - 1);
            }
        }

        return new GridPosition(0, 0);
    }

    //save the coordinates of the tile that needs to be highlighted
    private void highlightTile(int x, int y) {
        highlightedTile = new GridPosition(x, y);
    }

    //timer to add a delay between moves during replay
    private static Timer timer;
    public static void startTimer() {
        timer.start();
    }

    //replay a game
    public void replayGame(LinkedList<GridPosition[]> replay) {
        timer = new Timer(500, e -> {
            if(!replay.isEmpty()) {
                GridPosition[] gps = replay.removeFirst();
                movePiece(gps[0].getRow(), gps[0].getCol(), gps[1].getRow(), gps[1].getCol());
                if (gps[2] != null) {
                    removePiece(gps[2].getRow(), gps[2].getCol());
                    System.out.println(gps[2].toString());
                }
            } else {
                timer.stop();
            }
        });
    }

    //All the getters one might possibly need:
    //is a position in the grid?
    private boolean isLegalPos(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    //returns true if there is a piece on a given tile
    //return false if tile is unoccupied
    public boolean isTileOccupied(int row, int col) {
        return !isLegalPos(row, col) || pieces[row][col] != null;

    }

    //returns piece at grid coordinates gridX and gridY
    public Piece getPiece(int row, int col) {
        if(!isLegalPos(row, col))
            return new Piece(Type.EMPTY, new GridPosition(-1, -1));

        if(pieces[row][col] == null)
            return new Piece(Type.EMPTY, new GridPosition(-1, -1));

        return pieces[row][col];
    }

    //returns the whole array of pieces
    public Piece[][] getPieces() {
        return pieces;
    }

    //get the colour of the player whose turn it is
    public static Type getCurrentColour() {
        if(getCurrentPlayer() == 1)
            return Type.BLACK;
        else
            return Type.WHITE;
    }

    //return the player who is supposed to play now
    public static int getCurrentPlayer() {
        return TurnManager.getCurrentPlayer();
    }

    //returns current player's king colour
    public static Type getCurrentKing() {
        if(getCurrentPlayer() == 1)
            return Type.BLACK_KING;
        else
            return Type.WHITE_KING;
    }

    private boolean isWinner() {
        int count = 0;
        for(Piece[] row : pieces) {
            for(Piece p : row) {
                if(p != null && (p.getType() == getCurrentColour() || p.getType() == getCurrentKing()))
                    count++;
            }
        }

        return count == 0;
    }
}