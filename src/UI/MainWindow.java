package UI;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame{

    private CardLayout cardLayout;
    private GameWindow gameWindow;
    private MenuPanel menuPanel;
    private JPanel mainPanel;

    public MainWindow() {
        setPreferredSize(new Dimension(GameWindow.WIDTH, GameWindow.HEIGHT));



    }

    public void update() {
        gameWindow.update();
    }

    public void paintComponent(Graphics2D g2d) {
        gameWindow.paintComponent(g2d);
    }


}
