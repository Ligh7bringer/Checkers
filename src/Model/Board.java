package Model;

import Controller.*;
import UI.InformationPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

//the board class handles everything to do with the board
public class Board implements ActionListener {
    public static final int SIZE = 8; //board width and height, i.e. number of tiles per row and column
    public static final int TILE_WIDTH = 80; // dimensions of tiles
    public static final int TILE_HEIGHT = 80;

    //position of tile which was clicked
    private GridPosition highlightedTile;
    private ArrayList<GridPosition> availableTiles = new ArrayList<>();

    //coordinates of tiles which the player clicked
    private int sourceX, sourceY, destX, destY;
    private boolean gameOver;

    //store all the pieces here
    private Piece[][] pieces;

    //instance of move controller and board controller
    private MoveController moveController;
    private BoardController boardController;

    //store the game type
    private GameType gameType;
    //instance of AI
    private AI ai;

    //timer for AI vs. AI games
    private Timer aiTimer;

    //colour scheme for the board
    private Color colour1 = Color.BLACK, colour2 = Color.WHITE;

    private boolean showLastMove = true;

    //constructor for board
    public Board(){
        pieces = new Piece[SIZE][SIZE]; //create the array
        moveController = new MoveController(this);
        boardController = new BoardController(this);
        ai = new AI(this, moveController);
        gameOver = false;
    }

    //resets a game
    public void startGame(GameType t, int colourScheme) {
        setColours(colourScheme);
        availableTiles.clear();
        highlightedTile = null;
        gameOver = false;
        TurnManager.reset();
        if(timer != null && timer.isRunning())
            timer.stop();
        if(aiTimer != null && aiTimer.isRunning())
            aiTimer.stop();

        gameType = t;
        InformationPanel.clearMoves();
        InformationPanel.setErrorText("");
        GameHistory.clearAll();
        pieces = new Piece[SIZE][SIZE];
        initialiseBoard();
    }

    private void setColours(int colourScheme) {
        if(colourScheme == 1) {
            colour1 = Color.BLACK;
            colour2 = Color.WHITE;
        }
        if(colourScheme == 2) {
            colour1 = Color.BLACK;
            colour2 = new Color(153, 0, 0);
        }
        if(colourScheme == 3) {
            colour1 = new Color(150, 131, 21);
            colour2 = Color.WHITE;
        }
    }

    //starts an AI vs AI game
    public void setupAiGame(int colourScheme) {
        startGame(GameType.AI_VS_AI, colourScheme);
        aiTimer = new Timer(800, this);
    }

    //starts the AI timer
    public void startAiTimer() {
        aiTimer.start();
    }

    //this creates the starting layout of the board
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
        if(gameType != GameType.AI_VS_AI) {
            availableTiles = new ArrayList<>();
            sourceX = convertToGridCoords(x);
            sourceY = convertToGridCoords(y);
            highlightTile(sourceX, sourceY);

            if (validatePlayer(sourceY, sourceX)) {
                availableTiles.addAll(moveController.getPossibleJumps(sourceY, sourceX));
                if (availableTiles.isEmpty())
                    availableTiles = moveController.getPossibleMoves(sourceY, sourceX);
            }
        }
    }

    //save the coordinates of the destination tile
    public void addDestination(int x, int y) {
        if(gameType != GameType.AI_VS_AI) {
            destX = convertToGridCoords(y);
            destY = convertToGridCoords(x);
            validateMove(sourceY, sourceX, destX, destY);
        }
    }

    //checks if a move from row, col to destRow, destCol is valid
    public void validateMove(int row, int col, int destRow, int destCol) {
        if(validatePlayer(row, col)) { //make sure the player is trying to move their own piece
            //force the player to jump if they can
            if((!moveController.getAllJumps(TurnManager.getCurrentColour()).isEmpty() && !moveController.getAllJumps(TurnManager.getCurrentColour()).contains(new GridPosition(row, col))) || (!moveController.getPossibleJumps(row, col).isEmpty())
                    && !moveController.getPossibleJumps(row, col).contains(new GridPosition(destRow, destCol))) {
                System.out.println("You have to take a jump if you have one available!");
                InformationPanel.setErrorText("You have to jump!");
            } else if(moveController.getPossibleJumps(row, col).contains(new GridPosition(destRow, destCol))) { //jump
                boolean isKing = getPiece(row, col).getType() == Type.WHITE_KING || getPiece(row, col).getType() == Type.BLACK_KING;
                Type sourceType = getPiece(row, col).getType();
                movePiece(row, col, destRow, destCol);
                getPiece(destRow, destCol).crownPiece();
                Type destType = getPiece(destRow, destCol).getType();
                Piece removed = removeEnemyAfterJump(row, col, destRow, destCol, isKing);
                GameHistory.recordMove(new Move(new Piece(sourceType, new GridPosition(row, col)), new Piece(destType, new GridPosition(destRow, destCol)), removed));
                if(!moveController.getPossibleJumps(destRow, destCol).isEmpty()) {
                    InformationPanel.setErrorText("You may jump again!");
                    //TurnManager.nextTurn();
                    System.out.println("Double jump!!!");
                } else {
                    TurnManager.nextTurn();
                    InformationPanel.setErrorText("");
                }
            } else if (!moveController.getPossibleMoves(row, col).isEmpty() && moveController.getPossibleMoves(row, col).contains(new GridPosition(destRow, destCol))) { //regular move
                Type sourceType = getPiece(row, col).getType();
                movePiece(row, col, destRow, destCol);
                getPiece(destRow, destCol).crownPiece();
                Type destType = getPiece(destRow, destCol).getType();
                GameHistory.recordMove(new Move(new Piece(sourceType, new GridPosition(row, col)), new Piece(destType, new GridPosition(destRow, destCol)), null));
                TurnManager.nextTurn();
                InformationPanel.setErrorText("");
            } else {
                InformationPanel.setErrorText("Illegal move!");
                System.out.println("Illegal move!");
            }
        } else {
            InformationPanel.setErrorText("Invalid piece!");
            System.out.println("Invalid piece!");
        }


        if(!availableTiles.isEmpty())
            availableTiles.clear();
        highlightedTile = null;

        if(isWinner() != 0) {
            System.out.println("game over! player" + isWinner() + "won!");
            InformationPanel.setErrorText("Player " + isWinner() + " (" + TurnManager.getNextColour() + ") won!");
            gameOver = true;
            if(gameType != GameType.REPLAY) {
                String s = JOptionPane.showInputDialog("Save replay?");
                if ((s != null) && (s.length() > 0)) {
                    ReplayHandler.saveReplay(s);
                }
            }
        }

    }

    //paints the board and checkers
    public void paintComponent(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        //starting coordinates = top left corner of the window
        int x = 0;
        int y = 0;

        //loop through the board, draw the tiles and look for pieces
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if ((i % 2 == 1 && j % 2 == 1) || (j % 2 == 0 && i % 2 == 0)) { //some modulo magic to do alternating colours for the board tiles
                    //set appropriate colours
                    //g2d.setColor(new Color(153, 0, 0));
                    g2d.setColor(colour2);
                } else {
                    g2d.setColor(colour1);
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

        if(!GameHistory.getMoves().isEmpty() && showLastMove) {
            Move lastMove = GameHistory.getMoves().getLast();
            DrawHelper.showLastMove(g2d, lastMove);
        }

        if(availableTiles != null) {
            g2d.setColor(new Color(153, 255, 51));
            for(GridPosition gp : availableTiles) {
                g2d.fillRect(gp.getCol() * TILE_WIDTH, gp.getRow() * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
            }
        }

        //draw the game over message
        if(gameOver) {
            g2d.setFont(new Font("Arial", Font.PLAIN, 60));
            String text = "Game over!";
            FontMetrics fm = g2d.getFontMetrics();
            int totalWidth = (fm.stringWidth(text) * 2) + 4;

            int text_x = (TILE_WIDTH * SIZE - totalWidth) / 2;
            int text_y = (TILE_HEIGHT * SIZE - fm.getHeight()) / 2;
            text_x += (fm.stringWidth(text) + 6) / 2;
            g2d.setColor(new Color(0, 149, 153));

            g2d.drawString(text, text_x, text_y + ((fm.getDescent() + fm.getAscent()) / 2));
        }


    }

    //this is only used when playing vs AI and it's the AI's turn
    private int counter = 0; //this counter delays the AI's moves, should result in the AI waiting for between 0 and 2 seconds
    public void update() {
        counter++;
        if(counter > 60)
            counter = 0;

        if(gameType == GameType.VS_AI && TurnManager.getCurrentPlayer() == 2 && counter == 60) {
            GridPosition[] gps = ai.getMove(Type.WHITE);
            if(gps != null)
                validateMove(gps[0].getRow(), gps[0].getCol(), gps[1].getRow(), gps[1].getCol());
            else
                gameOver = true;
        }
    }

    //makes sure a player can only move their own pieces during their turn
    private boolean validatePlayer(int row, int col) {
        if(getPiece(row, col).getType() == Type.EMPTY) {
            //System.out.println("empty tile at" + sourceX + ", " + sourceY);
            return false;
        } else if((getPiece(row, col).getType() == Type.BLACK || getPiece(row, col).getType() == Type.BLACK_KING ) && TurnManager.getCurrentPlayer() == 1) {
            return true;
        } else if((getPiece(row, col).getType() == Type.WHITE || getPiece(row, col).getType() == Type.WHITE_KING ) && TurnManager.getCurrentPlayer() == 2) {
            return true;
        }

        return false;
    }

    //moves piece from one tile to another
    private void movePiece(int sourceX, int sourceY, int destX, int destY) {
        pieces[destX][destY] = pieces[sourceX][sourceY]; //move to new position
        pieces[sourceX][sourceY] = null; //make old position null, it should be empty
        pieces[destX][destY].setGridPosition(new GridPosition(destX, destY));
    }

    //converts window coordinates to position in grid
    private int convertToGridCoords(int screenCoord) {
        return screenCoord / TILE_WIDTH; //can be divided by tile height or tile width because they are the same, a tile is square
    }

    //removes piece at position row, col on the grid
    public void removePiece(int row, int col) {
        pieces[row][col] = null;
    }

    //calculates the position of a piece which needs to be removed after a jump and returns its coordinates
    private Piece removeEnemyAfterJump(int row, int col, int destRow, int destCol, boolean isKing) {
        Piece p;
        int rowDiff = destRow - row;
        int colDiff = destCol - col;
        if(TurnManager.getCurrentPlayer() == 2 || isKing) {
            if(rowDiff > 0 && colDiff > 0) {
                p = getPiece(row+1, col+1);
                removePiece(row + 1, col + 1);
                return p;
            }
            if(rowDiff > 0 && colDiff < 0) {
                p = getPiece(row+1, col-1);
                removePiece(row + 1, col - 1);
                return p;
            }
        }
        if(TurnManager.getCurrentPlayer() == 1 || isKing) {
            if(rowDiff < 0 && colDiff > 0) {
                p = getPiece(row-1, col+1);
                removePiece(row - 1, col + 1);
                return p;
            }
            if(rowDiff < 0 && colDiff < 0) {
                p = getPiece(row-1, col-1);
                removePiece(row - 1, col - 1);
                return p;
            }
        }

        return new Piece(Type.EMPTY, new GridPosition(-1, -1));
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

    //replays a game
    public void replayGame() {
        timer = new Timer(700, this);
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

    //adds a piece on the board at the given position of type t
    public void addPiece(GridPosition gp, Type t) {
        pieces[gp.getRow()][gp.getCol()] = new Piece(t, gp);
    }

    //checks if a player has lost all their checkers
    //returns 2 if player 1 has 0 checkers
    //returns 1 if player 2 has 0 checkers
    //basically it returns the winner
    private int isWinner() {
        if(getPieceCount()[0] == 0)
            return 2;
        if(getPieceCount()[1] == 0)
            return 1;

        return 0;
    }

    //returns the count of black and white pieces
    public int[] getPieceCount() {
        int black = 0;
        int white = 0;
        for(int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if(pieces[i][j] != null && (pieces[i][j].getType() == Type.BLACK_KING || pieces[i][j].getType() == Type.BLACK))
                    black++;
                else if(pieces[i][j] != null && (pieces[i][j].getType() == Type.WHITE_KING || pieces[i][j].getType() == Type.WHITE))
                    white++;
            }
        }

        return new int[]{black, white};
    }

    //action listener for the timers
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == aiTimer) {
            makeMove();
        }
        if(e.getSource() == timer) {
            LinkedList<Move> replay = BoardController.getReplay();
            if(!replay.isEmpty()) {
                Move m = replay.removeFirst();
                GridPosition source = m.getSource().getGridPosition();
                GridPosition dest = m.getDestination().getGridPosition();
                validateMove(source.getRow(), source.getCol(), dest.getRow(), dest.getCol());
            } else {
                timer.stop();
            }
        }
    }

    //validates AI moves
    private void makeMove() {
        if (gameType == GameType.AI_VS_AI) {
            GridPosition[] gps = ai.getMove(TurnManager.getCurrentColour());
            if(gps != null)
                validateMove(gps[0].getRow(), gps[0].getCol(), gps[1].getRow(), gps[1].getCol());
            else {
                //System.out.println("Game over! no more moves!");
                InformationPanel.setErrorText(TurnManager.getNextColour() + " won!");
                aiTimer.stop();
                gameOver = true;
            }
            if(isWinner() != 0) {
                InformationPanel.setErrorText("Player " + isWinner() + " (" + TurnManager.getNextColour() + ") won!");
                aiTimer.stop();
                gameOver = true;
            }
        }
    }

    public void setShowLastMove() {
        this.showLastMove = !showLastMove;
    }

}