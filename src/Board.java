import java.awt.*;

public class Board {
    private final int SIZE = 8; //board width and height, i.e. number of tiles per row and column
    private final int TILE_WIDTH = 60; // dimensions of tiles
    private final int TILE_HEIGHT = 60;

    //store all the pieces here
    private Piece[][] pieces;

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
        }
        for(int row=1; row < (SIZE); row+=2)
            pieces[6][row] = new Piece(Type.BLACK);

        for(int row=1; row < (SIZE); row+=2){
            pieces[0][row] = new Piece(Type.WHITE);
            pieces[2][row] = new Piece(Type.WHITE);
        }
        for(int row=0; row < (SIZE); row +=2)
            pieces[1][row] = new Piece(Type.WHITE);
    }

    public void update(int x, int y) {
        //calculate position in the grid from screen coordinates
        //is this accurate enough though?
        int col = x / TILE_WIDTH;
        int row = y / TILE_HEIGHT;
        //debugging
        System.out.println("ROW: " + row + "; COL: " + col);

        if (pieces[row][col] != null ) {
            pieces[row][col] = null;
        }
    }

    //the board should paint itself
    public void paint(Graphics2D g2d) {
        //starting coordinates = top left corner of the window
        int x = 0;
        int y = 0;

        //loop through the board and look for pieces
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if ((i % 2 == 1 && j % 2 == 1) || (j % 2 == 0 && i % 2 == 0)) { //some modulo magic to do alternating colours for the board tiles
                    //set appropriate colours
                    g2d.setColor(Color.red);
                } else {
                    g2d.setColor(Color.black);
                }

                //actually draw the tile
                g2d.fillRect(x, y, TILE_WIDTH, TILE_HEIGHT);

                //if there's a piece on that tile
                if(pieces[i][j] != null)
                    pieces[i][j].paint(g2d, x, y); //draw it

                //move right
                x = x + TILE_WIDTH;
            }
            //when it's time for the next row, move to the left (x = 0)
            x = 0;
            //move down
            y += TILE_HEIGHT;
        }
    }
}
