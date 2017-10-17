package Model;

import Controller.GameHistory;
import Controller.MoveController;

import java.awt.*;
import java.util.ArrayList;

//TODO: FIX THE SCREEN AND GRID COORDINATE MESS IN THIS CLASS!!!

public class Board {
    public static final int SIZE = 8; //board width and height, i.e. number of tiles per row and column
    public static final int TILE_WIDTH = 80; // dimensions of tiles
    public static final int TILE_HEIGHT = 80;

    //position of tile which was clicked
    private GridPosition highlightedTile;
    private ArrayList<GridPosition> availableTiles;

    private int sourceX, sourceY, destX, destY;

    //store all the pieces here
    private Piece[][] pieces;

    //which player's turn is it
    private static boolean playerOne = true;

    //instance of move controller
    private MoveController moveController;

    //constructor for board
    public Board(){
        pieces = new Piece[SIZE][SIZE]; //create the array
        initialiseBoard(); //call initialise to add pieces to the array
        moveController = new MoveController(this);
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

    //the board has its own validateMove
    //parameters: source x, source y, destination x, destination y
    public void validateMove(int row, int col, int destRow, int destCol) {
        if(validatePlayer(row, col)) { //make sure the player is trying to move their own piece
            //force the player to jump if they can
            if((!moveController.getAllJumps().isEmpty() && !moveController.getAllJumps().contains(new GridPosition(row, col))) || (!moveController.getPossibleJumps(row, col).isEmpty())
                    && !moveController.getPossibleJumps(row, col).contains(new GridPosition(destRow, destCol))) {
                System.out.println("You have to take a jump if you have one available!");
            } else if(moveController.getPossibleJumps(row, col).contains(new GridPosition(destRow, destCol))) {
                movePiece(row, col, destRow, destCol);
                GridPosition gp = removeEnemyAfterJump(row, col, destRow, destCol);
                GameHistory.recordMove(new GridPosition(row, col), new GridPosition(destRow, destCol), gp);
                switchPlayer();
            } else if (moveController.isMoveLegal(row, col, destRow, destCol)) {
                movePiece(row, col, destRow, destCol);
                GameHistory.recordMove(new GridPosition(row, col), new GridPosition(destRow, destCol), null);
                switchPlayer();
            } else {
                System.out.println("Illegal move!");
            }
            //highlightedTile = null;

            //switchPlayer();
        } else {
            //highlightedTile = null;
            System.out.println("INVALID PIECE");
        }

        availableTiles.clear();
        highlightedTile = null;
    }

    //the board should paintComponent itself
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
                if(getPiece(i, j) != null)
                    getPiece(i, j).paintComponent(g2d, x, y); //draw it

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
            g2d.drawRect(highlightedTile.getX() * TILE_WIDTH, highlightedTile.getY() * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
        }

        if(availableTiles != null) {
            g2d.setColor(new Color(153, 255, 51));
            for(GridPosition gp : availableTiles) {
                g2d.fillRect(gp.getY() * TILE_WIDTH, gp.getX() * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
            }
        }

    }

    //we need this to make sure a player can only move their own pieces
    public boolean validatePlayer(int row, int col) {
        if(getPiece(row, col) == null) {
            System.out.println("empty tile at" + sourceX + ", " + sourceY);
            return false;
        } else if(getPiece(row, col).getType() == Type.BLACK && playerOne) {
            return true;
        } else if(getPiece(row, col).getType() == Type.WHITE && !playerOne) {
            return true;
        }

        return false;
    }

    //moves piece from one tile to another
    public void movePiece(int sourceX, int sourceY, int destX, int destY) {
        pieces[destX][destY] = pieces[sourceX][sourceY]; //move to new position
        pieces[sourceX][sourceY] = null; //make old position null, it should be empty
        pieces[destX][destY].setGridPosition(new GridPosition(destX, destY));
    }

    //returns true if there is a piece on a given tile
    //return false if tile is unoccupied
    public boolean isTileOccupied(int row, int col) {
        if(pieces[row][col] != null) {
            return true;
        }

        return false;
    }

    //this will be used to indicate it's the next player's turn
    public void switchPlayer() {
        playerOne = !playerOne;
    }

    //return the player who is supposed to play now
    public static int getCurrentPlayer() {
        if(playerOne)
            return 1;
        else
         return 2;
    }
    //convert window coordinates to position in grid
    public int convertToGridCoords(int screenCoord) {
        return screenCoord / TILE_WIDTH; //can be divided by tile height as well because they are the same, a tile is square
    }

    //returns piece at grid coordinates gridX and gridY
    public Piece getPiece(int row, int col) {
        if(pieces[row][col] == null)
            return null;

        return pieces[row][col];
    }

    //returns the whole array of pieces
    public Piece[][] getPieces() {
        return pieces;
    }

    //get the colour of the player whose turn it is
    public static Type getCurrentColour() {
        if(playerOne)
            return Type.BLACK;
        else
            return Type.WHITE;
    }

    //remove piece at position gridX, gridY
    public void removePiece(int gridX, int gridY) {
        pieces[gridX][gridY] = null;
    }

    private GridPosition removeEnemyAfterJump(int row, int col, int destRow, int destCol) {
        int rowDiff = destRow - row;
        int colDiff = destCol - col;
        if(getCurrentPlayer() == 2) {
            if(rowDiff > 0 && colDiff > 0) {
                removePiece(row + 1, col + 1);
                return new GridPosition(row + 1, col + 1);
            }
            else if(rowDiff > 0 && colDiff < 0) {
                removePiece(row + 1, col - 1);
                return new GridPosition(row + 1, col - 1);
            }
        } else if(getCurrentPlayer() == 1) {
            if(rowDiff < 0 && colDiff > 0) {
                removePiece(row - 1, col + 1);
                return new GridPosition(row - 1, col + 1);
            }
            else if(rowDiff < 0 && colDiff < 0) {
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
        //System.out.println("Source: " + sourceY + ", " + sourceX + " Destination: " + destX + ", " + destY);
        validateMove(sourceY, sourceX, destX, destY);
    }
}
