import java.awt.*;
import java.io.IOException;
import javax.swing.*;

public class GameWindow extends JPanel {
    private static final int SIZE = 8;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 525;
    private Board board = new Board();

    public GameWindow() throws IOException {
    }

    public void paint(Graphics g) {
        board.paint(g);
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame();
        frame.setTitle("Checkers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new GameWindow());
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
        frame.setBackground(Color.lightGray);
    }
}