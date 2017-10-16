package UI;

import Model.Board;

import java.awt.*;
import javax.swing.*;

public class GameWindow extends JPanel implements Runnable{
    //window dimensions
    public static final int WIDTH = Board.TILE_WIDTH * Board.SIZE + 1;
    public static final int HEIGHT = Board.TILE_HEIGHT * Board.SIZE;

    //game loop variables
    private Thread thread;
    private int targetFPS = 30;
    private long targetTime = 1000 / targetFPS;
    private boolean isRunning = false;

    //graphics
    private Graphics2D g2d;

    //JComponents
    private static JLabel currentPlayer;
    private static JTextArea textArea;
    private static JButton saveButton;

    private static InformationPanel informationPanel;
    private static GamePanel gamePanel;

    //constructor
    private GameWindow() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT)); //set window width and height
        setFocusable(true); // this should help with mouse events?

        saveButton = new JButton("Save game");

        informationPanel = new InformationPanel();
        gamePanel = new GamePanel();
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

    //validateMove, called every "frame"
    public void update() {
        gamePanel.update();
        informationPanel.update();
    }

    // this will draw everything hopefully
    public void paintComponent(Graphics g) {
        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //turn antialising on for nicer graphics
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        gamePanel.paintComponent(g);

        g2d.dispose(); //is this needed?
    }



    //main method
    public static void main(String[] args) {
        GameWindow gw = new GameWindow(); //create an object so the constructor is called? is this bad?
        JFrame frame = new JFrame("Checkers"); //create frame
        //set properties of the frame
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        //layout
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        frame.setLayout(gridBag);
        c.fill = GridBagConstraints.HORIZONTAL;

       /* c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1;
        c.weightx = 0.1;
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = SwingConstants.HORIZONTAL;
        gridBag.setConstraints(saveButton, c);
        frame.add(saveButton);

        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.weighty = 0.5;
        gridBag.setConstraints(gm, c);
        frame.add(gm); */

        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(3,3,3,3);
        gridBag.setConstraints(gamePanel, c);
        frame.add(gamePanel);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        //c.anchor = GridBagConstraints.NORTH;
        gridBag.setConstraints(informationPanel, c);
        frame.add(informationPanel);

        frame.pack();

        frame.setLocationRelativeTo(null); // this should display the window in the middle of the screen ?
        frame.setVisible(true);
        frame.getContentPane().setBackground(Color.WHITE); //this doesn't seem to change the background colour?
    }


}