
import java.awt.*;
import java.util.ArrayList;

public class Board {
    private static final int SIZE = 8; //board width and height, i.e. number of tiles per row and column
    public static final int TILE_WIDTH = 60; // dimensions of tiles
    public static final int TILE_HEIGHT = 60;

    //position of tile which was clicked
    private GridPosition highlightedTile;

    //store all the pieces here
    private Piece[][] pieces;

    //which player's turn is it
    private boolean playerOne = true;

    //constructor for board
    public Board(){
        pieces = new Piece[SIZE][SIZE]; //create the array
        initialiseBoard(); //call initialise to add pieces to the array
    }

    //this creates the starting layout of the board
    //is there a more efficient way to do this?
    private void initialiseBoard() {
        for(int row=0; row < (SIZE); row+=2){
            pieces[5][row] = new Piece(Type.BLACK);
            pieces[7][row] = new Piece(Type.BLACK);
            pieces[6][row+1] = new Piece(Type.BLACK);
        }
        for(int row=1; row < (SIZE); row+=2){
            pieces[0][row] = new Piece(Type.WHITE);
            pieces[2][row] = new Piece(Type.WHITE);
            pieces[1][row-1] = new Piece(Type.WHITE);
        }
    }

    //the board has its own update
    //parameters in order: source x, source y, destination x, destination y
    public void update(int sx, int sy, int dx, int dy) {
        //calculate position in the grid from screen coordinates
        //is this accurate enough though?
        int col = convertToGridCoords(sx);
        int row = convertToGridCoords(sy);
        int destCol = convertToGridCoords(dx);
        int destRow = convertToGridCoords(dy);

        //make sure the player has selected one of their pieces
        if(validatePlayer(row, col)) {
            //isMoveJump(row, col, destRow, destCol);
            if (isMoveLegal(row, col, destRow, destCol)) {
                movePiece(row, col, destRow, destCol);
            } else {
                System.out.println("Illegal move!");
                switchPlayer(); //if the move is illegal, switch players as they are switched at the end of this method again
            }
            highlightedTile = null;

            switchPlayer();
        } else {
            highlightedTile = null;
            System.out.println("INVALID PIECE");
        }
    }

    //the board should paintComponent itself
    public void paintComponent(Graphics2D g2d) {
        g2d.fillRect(0, 0, GameWindow.WIDTH, GameWindow.HEIGHT);
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
                    pieces[i][j].paint(g2d, x, y); //draw it

                if(highlightedTile != null) {
                    //System.out.println(highlightedTile.getX() + ", " + highlightedTile.getY());
                    g2d.setColor(Color.YELLOW);
                    g2d.drawRect(highlightedTile.getX() * TILE_WIDTH, highlightedTile.getY() * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
                }

                //move right
                x = x + TILE_WIDTH;
            }
            //when it's time for the next row, move to the very left (x = 0)
            x = 0;
            //move down, next row
            y += TILE_HEIGHT;
        }
    }

    //we need this to make sure a player can only move their own pieces
    public boolean validatePlayer(int gridX, int gridY) {
        if(getPiece(gridX, gridY) == null) {
            return false;
        } else if(getPiece(gridX, gridY).getType() == Type.BLACK && playerOne) {
            return true;
        } else if(getPiece(gridX, gridY).getType() == Type.WHITE && !playerOne) {
            return true;
        }

        return false;
    }

    private boolean isMoveJump(int row, int col, int destRow, int destCol) {
        if(row >= 6 || col <= 2) //make sure we don't get out of bounds exception
            return false;
        else if( (isTileOccupied(row, col) && getPiece(row, col).getType() == Type.WHITE) && (isTileOccupied(row+1, col-1)
                && getPiece(row+1, col-1).getType() == Type.BLACK) && !isTileOccupied(row+2, col-2) ) {
            movePiece(row, col, row+2, col-2);
            removePiece(row+1, col-1);
            return true;
        }

        return false;
    }

    //returns true if a regular move (move diagonally one tile) is legal
    private boolean isMoveLegal(int gridX, int gridY, int destX, int destY) {
        if(isTileOccupied(gridX, gridY) && !isTileOccupied(destX, destY)) { //if source has a piece and destination is an empty tile
            Piece piece = getPiece(gridX, gridY);
            if(piece.getType() == Type.WHITE) {
                //legal moves for white pieces:
                //move diagonally to the left
                if(destX - gridX == 1 && destY - gridY == -1) {
                    return true;
                } else if (destX - gridX == 1 && destY - gridY == 1) { //move diagonally to the right
                    return true;
                }
            } else if (piece.getType() == Type.BLACK) {
                //legal moves for black, in the opposite direction:
                //move diagonally to the left
                if(destX - gridX == -1 && destY - gridY == -1) {
                    return true;
                } else if (destX - gridX == -1 && destY - gridY == 1) { //move diagonally to the right
                    return true;
                }
            }
        }

        return false;
    }

    //moves piece from one tile to another
    private void movePiece(int sourceX, int sourceY, int destX, int destY) {
        pieces[destX][destY] = pieces[sourceX][sourceY]; //move to new position
        pieces[sourceX][sourceY] = null; //make old position null, it should be empty
    }

    //returns true if there is a piece on a given tile
    //return false if tile is unoccupied
    public boolean isTileOccupied(int gridX, int gridY) {
        if(pieces[gridX][gridY] != null) {
            System.out.println("Tile " + gridX + ", " + gridY + " is occupied");
            return true;
        }

        System.out.println("Tile " + gridX + ", " + gridY + " is unoccupied");
        return false;
    }

    //so we need this method to enforce jumping when possible TODO IMPLEMENT maybe find a better way?
    private boolean canJump(int gridX, int gridY) {
        return false;
    }

    //this will be used to indicate it's the next player's turn
    private void switchPlayer() {
        playerOne = !playerOne;
    }

    //return the player who is supposed to play now
    public int getCurrentPlayer() {
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
    private Piece getPiece(int gridX, int gridY) {
        return pieces[gridX][gridY];
    }

    //remove piece at position gridX, gridY
    private void removePiece(int gridX, int gridY) {
        pieces[gridX][gridY] = null;
    }

    public void highlightTile(int x, int y) {
        highlightedTile = new GridPosition(convertToGridCoords(x), convertToGridCoords(y));
    }
}
