package UI;

import Model.Board;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GamePanel extends JPanel implements MouseListener {
    //instance of board
    private Board board;

    //mouse coordinates
    private static int mouseX, mouseY;

    //private boolean isClicked = false;
    private int clicks = 0;

    public GamePanel() {
        board = new Board();
        Dimension size = getPreferredSize();
        size.width = Board.SIZE * Board.TILE_WIDTH ;
        size.height =  Board.SIZE * Board.TILE_HEIGHT;
        setPreferredSize(size);

        //add mouse listeners
        addMouseListener(this);

        Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 5);
        Border bevelBorder = BorderFactory.createRaisedBevelBorder();
        Border bevelLineBorder = new CompoundBorder(bevelBorder, lineBorder);
        setBorder(bevelLineBorder);
        setBackground(Color.WHITE);
    }

    private int sourceX, sourceY, destX, destY;
    public void update() {
        if(clicks == 1) {
           // sourceX = mouseX;
            //sourceY = mouseY;
            board.addSource(mouseX, mouseY);
        }
        if(clicks == 2) {
            //destX = mouseX;
            //destY = mouseY;
            board.addDestination(mouseX, mouseY);
            clicks = 0;
        }

        board.update();

        repaint();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        board.paintComponent(g2d); //the board paints itself, pieces paint themselves in the board
    }


    //get mouse coords when event we are listening to occurs
    private void setMousePosition(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    //event listeners. Mouse pressed seems to be more accurate than mouseClicked for some reason
    public void mousePressed(MouseEvent e) {
        setMousePosition(e);
        clicks++;

        if(clicks > 2)
            clicks = 0;

        System.out.println("CLICKS " + clicks);
    }

    //need these although I am not going to use them
    public void mouseClicked(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
}
