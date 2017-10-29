package UI;

import Model.Board;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//this GUI class displays the board in a JPanel and handles mouse events
public class GamePanel extends JPanel implements MouseListener {
    //instance of board
    private Board board;

    //mouse coordinates
    private static int mouseX, mouseY;

    //private boolean isClicked = false;
    private int clicks = 0;

    //constructor which initialises all needed objects and variables
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

    //update method which calls the board's update
    public void update() {
        board.update();
        repaint(); //repaint needs to be called to update the board
    }

    //paint the board
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        board.paintComponent(g2d); //the board paints itself, pieces paint themselves in the board
    }


    //gets mouse coords when mouse position occurs
    private void setMousePosition(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();

    }

    //event listener. Mouse pressed seems to be more accurate than mouseClicked for some reason
    public void mousePressed(MouseEvent e) {
        setMousePosition(e);
        clicks++;

        if (clicks > 2)
            clicks = 0;

        if (clicks == 1) {
            board.addSource(mouseX, mouseY);
        }
        if (clicks == 2) {
            board.addDestination(mouseX, mouseY);
            clicks = 0;
        }

    }

    //the rest of the mouse events that need to be implemented
    public void mouseClicked(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
}
