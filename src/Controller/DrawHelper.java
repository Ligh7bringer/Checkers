package Controller;

import Model.Board;
import Model.GridPosition;
import Model.Move;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class DrawHelper {
    private static Image skull;
    private static final int WIDTH = 25;
    private static final int HEIGHT = 25;

    public static void showLastMove(Graphics2D g2d, Move lastMove) {

        GridPosition source = lastMove.getSource().getGridPosition();
        int x1 = source.getCol() * Board.TILE_WIDTH +  Board.TILE_WIDTH / 2;
        int y1 = source.getRow() *  Board.TILE_WIDTH +  Board.TILE_WIDTH / 2;
        GridPosition dest = lastMove.getDestination().getGridPosition();
        int x2 = dest.getCol() *  Board.TILE_WIDTH +  Board.TILE_WIDTH / 2;
        int y2 = dest.getRow() *  Board.TILE_WIDTH +  Board.TILE_WIDTH / 2;
        drawArrowLine(g2d, x1, y1, x2, y2, WIDTH, HEIGHT);
        if(lastMove.getRemoved() != null) {
            drawX(g2d, lastMove.getRemoved().getGridPosition());
            //drawSkull(g2d, lastMove.getRemoved().getGridPosition());
        }
    }

    /**
     * Draw an arrow line between two points.
     * @param g2d the graphics component.
     * @param x1 x-position of first point.
     * @param y1 y-position of first point.
     * @param x2 x-position of second point.
     * @param y2 y-position of second point.
     * @param d  the width of the arrow.
     * @param h  the height of the arrow.
     */
    public static void drawArrowLine(Graphics2D g2d, int x1, int y1, int x2, int y2, int d, int h) {
        g2d.setColor(new Color(0, 149, 153, 160));
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx*dx + dy*dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm*cos - ym*sin + x1;
        ym = xm*sin + ym*cos + y1;
        xm = x;

        x = xn*cos - yn*sin + x1;
        yn = xn*sin + yn*cos + y1;
        xn = x;

        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};

        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(x1, y1, x2, y2);
        g2d.fillPolygon(xpoints, ypoints, 3);
    }

    //draws an X at specified grid position
    public static void drawX(Graphics2D g2d, GridPosition gp) {
        final int OFFSET = 15;
        int x1 = gp.getCol() * Board.TILE_WIDTH + OFFSET;
        int y1 = gp.getRow() * Board.TILE_WIDTH + OFFSET;

        int x2 = gp.getCol() * Board.TILE_WIDTH + Board.TILE_WIDTH - OFFSET;
        int y2 = gp.getRow() * Board.TILE_WIDTH + Board.TILE_WIDTH - OFFSET;

        g2d.setColor(new Color(255, 21, 29));
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(x1, y1, x2, y2);

        x1 = gp.getCol() * Board.TILE_WIDTH + Board.TILE_WIDTH - OFFSET;
        y1 = gp.getRow() * Board.TILE_WIDTH + OFFSET;

        x2 = gp.getCol() * Board.TILE_WIDTH + OFFSET;
        y2 = gp.getRow() * Board.TILE_WIDTH + Board.TILE_WIDTH - OFFSET;

        g2d.drawLine(x1, y1, x2, y2);
    }

    public static void drawSkull(Graphics2D g2d, GridPosition removed) {
        try {
            skull = ImageIO.read(new File("res/skull.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int x = removed.getCol() * Board.TILE_WIDTH + 5;
        int y = removed.getRow() * Board.TILE_WIDTH;

        g2d.drawImage(skull, x, y, null);
    }
}
