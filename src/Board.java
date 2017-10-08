import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Board {
    private final int SIZE = 8;
    private final int TILE_WIDTH = 60;
    private final int TILE_HEIGHT = 60;

    //private int[][] tiles;
    private Piece[][] pieces;

    public Board() throws IOException {
        //tiles = new int[SIZE][SIZE];
        pieces = new Piece[SIZE][SIZE];
        initialiseBoard();
    }

    private void initialiseBoard() throws IOException {
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

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int x = 0;
        int y = 0;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if ((i % 2 == 1 && j % 2 == 1) || (j % 2 == 0 && i % 2 == 0)) {
                    g2d.setColor(Color.red);
                } else {
                    g2d.setColor(Color.black);
                }

                g.fillRect(x, y, TILE_WIDTH, TILE_HEIGHT);
                if(pieces[i][j] != null)
                    pieces[i][j].paint(g, x, y);

                x = x + TILE_WIDTH;

            }
            x = 0;
            y += TILE_HEIGHT;
        }
    }
}
