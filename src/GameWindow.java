import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import javax.swing.*;

public class GameWindow extends JPanel implements Runnable, MouseListener, MouseMotionListener {
    //window dimensions
    private static final int WIDTH = 480;
    private static final int HEIGHT = 480;

    //instance of board
    private static Board board;

    //mouse coordinates
    public static int mouseX, mouseY;
    private boolean isClicked = false;

    //game loop variables
    private Thread thread;
    private int targetFPS = 30;
    private long targetTime = 1000 / targetFPS;
    private boolean isRunning = false;

    //graphics
    private Graphics2D g2d;

    //constructor
    public GameWindow() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT)); //set window width and height
        setFocusable(true); // this should help with mouse events?
        //add mouse listeners
        addMouseListener(this);
        addMouseMotionListener(this);

        board = new Board(); // initialise the board
        start(); //start main game loop
    }

    // main game loop
    public void run() {
        long startTime, deltaTime, wait; //some time tracking
        while(isRunning) {
            startTime = System.currentTimeMillis();

            update();
            repaint();

            deltaTime = System.currentTimeMillis() - startTime;
            wait = targetTime - deltaTime;
            if(wait < 5)
                wait = 5;
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stop();
    }

    //this method starts the thread
    private void start() {
        isRunning = true; //we need to know the game loop has started
        thread = new Thread(this, "Game loop"); //initialise the thread
        thread.start(); //start the thread
    }

    //stops the thread and the game
    private void stop() {
        isRunning = false; //we need to know the game loop has ended
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //update, called every frame
    public void update() {
        if(isClicked) {
            board.update(mouseX, mouseY);
            isClicked = false;
        }
    }

    // this will draw everything hopefully
    public void paint(Graphics g) {
        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //turn antialising on for nicer graphics

        board.paint(g2d); //the board paints itself, pieces paint themselves in the board

        g2d.dispose(); //dispose
    }

    //get mouse coords when event we are listening to occurs
    private void setMousePosition(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    //main method
    public static void main(String[] args) {
        JFrame frame = new JFrame("Checkers"); //create frame
        //set properties of the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.add(new GameWindow(), BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null); // this should display the window in the middle of the screen ?
        frame.setVisible(true);
        frame.setBackground(Color.lightGray);
    }

    //event listeners
    public void mouseClicked(MouseEvent e) {
        setMousePosition(e);
        isClicked = true;
        System.out.println(mouseX + "; " + mouseY);
    }

    public void mouseDragged(MouseEvent e) {
        setMousePosition(e);
    }

    public void mouseMoved(MouseEvent e) {

    }

    //need these although I am not gong to use them
    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
}