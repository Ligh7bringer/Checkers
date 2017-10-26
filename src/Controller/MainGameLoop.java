package Controller;

import UI.GameWindow;

import java.awt.*;
import javax.swing.*;

public class MainGameLoop extends JPanel implements Runnable {
    //game loop variables
    private Thread thread;
    private int targetFPS = 30;
    private long targetTime = 1000 / targetFPS;
    private boolean isRunning = false;

    private GameWindow gameWindow;

    //constructor
    private MainGameLoop() {
        gameWindow = new GameWindow();
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
            wait = (targetTime - deltaTime);
            if(wait < 5)
                wait = 5;
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.println("End of one loop iteration, wait = " + wait);
        }
        stop();
    }

    //this method starts the thread and the game
    private synchronized void start() {
        isRunning = true; //we need to know the game loop has started
        thread = new Thread(this, "Game loop"); //initialise the thread
        thread.start(); //start the thread
    }

    //stops the thread and the game
    private synchronized void stop() {
        isRunning = false; //we need to know the game loop has ended
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //update, called every "frame"
    private void update() {
        gameWindow.update();
        //System.out.println("updating");
    }

    // this will draw everything hopefully
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //turn antialising on for nicer graphics
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        gameWindow.paintComponent(g2d);

        g2d.dispose(); //is this needed?
        System.out.println("repainting");
    }

    //main method
    public static void main(String[] args) {
        //this should be the right way to run swing stuff?
        SwingUtilities.invokeLater(MainGameLoop::new);
    }


}